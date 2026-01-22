package com.holly.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
//import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson2.JSON;
import com.holly.chatService.ConsultantService;
import com.holly.chatService.RecommenAssistantService;
import com.holly.context.ToolContext;
import com.holly.dto.AiChatDTO;
import com.holly.entity.*;
import com.holly.enumeration.ChatEventTypeEnum;
import com.holly.mapper.*;
import com.holly.repository.RedisChatMemoryStore;
import com.holly.service.AiAssistantService;
import com.holly.tools.result.ToolResultHolder;
import com.holly.vo.ChatEventVO;
import dev.langchain4j.service.TokenStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.*;
import java.util.stream.Collectors;

import static com.holly.constant.Constants.DISH;

/**
 * AI智能助手服务实现类
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AiAssistantServiceImpl implements AiAssistantService {

    private final OrderMapper orderMapper;
    private final DishMapper dishMapper;
    private final ConsultantService consultantService;
    private final RecommenAssistantService recommendationService;
    private final OrderDetailMapper orderDetailMapper;
    private final DishFlavorMapper dishFlavorMapper;
    private final RedisChatMemoryStore redisChatMemoryStore;
    private final ItemViewMapper itemViewMapper;
    private final ChatEventVO STOP_EVENT = new ChatEventVO(null, ChatEventTypeEnum.STOP.getValue());


    @Override
    public Flux<ChatEventVO> processChat(AiChatDTO chatDTO, Long userId) {
        String message = chatDTO.getMessage();
        String sessionId = chatDTO.getSessionId();
        log.info("处理AI聊天请求，用户ID：{}，消息：{}", userId, message);
        return handleGeneralQuestion(sessionId, message, userId);
    }

    @Override
    public Flux<ChatEventVO> handleGeneralQuestion(String sessionId, String message, Long userId) {
        try {
            // 生成本次请求id
            var requestId = IdUtil.fastSimpleUUID();
            TokenStream stream = consultantService.chat(sessionId, message);
            // 设置工具上下文
            ToolContext.put(ToolContext.REQUEST_ID, requestId);
            ToolContext.put(ToolContext.USER_ID, userId);
            return Flux.create(sink -> {
                stream.onPartialResponse(content -> {
                            ChatEventVO chatEventVO = new ChatEventVO(content, ChatEventTypeEnum.DATA.getValue());
                            sink.next(chatEventVO);
                        })
                        .onCompleteResponse(chatResponse -> {
                            Map<String, Object> map = ToolResultHolder.get(requestId);
                            if (CollUtil.isNotEmpty(map)) {
                                ToolResultHolder.remove(requestId);
                                ChatEventVO chatEventVO = ChatEventVO.builder()
                                        .eventData(map)
                                        .eventType(ChatEventTypeEnum.PARAM.getValue())
                                        .build();
                                //追加参数
                                sink.next(chatEventVO);
                                sink.next(STOP_EVENT);
                            } else {
                                sink.next(STOP_EVENT);
                            }
                            //清理工具上下文
                            ToolContext.clear();
                        })
                        .onToolExecuted(toolExecution -> {
                            String messageId = toolExecution.request().id();
                            ToolResultHolder.put(messageId, ToolContext.REQUEST_ID, requestId);
                        })
                        .onError(error -> {
                            log.error("AI处理通用问题失败", error);
                        })
                        .start();
            });
        } catch (Exception e) {
            log.error("处理通用问题失败", e);
            return Flux.just(STOP_EVENT);
        }
    }

    @Override
    public List<Map<String, Object>> getSmartRecommendations(Long userId, String preference, Long shopId) {
        log.info("智能推荐菜品，用户ID：{}，偏好：{}，店铺ID：{}", userId, preference, shopId);
        try {
            // 1. 获取所有可用菜品
            List<Dish> allDishes = dishMapper.list(null);
            if (allDishes == null || allDishes.isEmpty()) {
                return createEmptyRecommendation();
            }
            //按热度排序
            // 先过滤掉hotSpot为null的记录，再排序
            //热度最高的菜品
            allDishes = allDishes.stream()
                    .filter(dish -> dish.getHotSpot() != null)
                    .sorted(Comparator.comparing(Dish::getHotSpot, Comparator.nullsLast(Comparator.reverseOrder())))
                    .collect(Collectors.toList());

            //根据用户id获取用户已完成历史订单
            List<Orders> orders = orderMapper.getOrdersByUserIdAndStatus(userId, Orders.COMPLETED);
            //获取所有的订单id
            List<Long> orderIds = orders.stream().map(Orders::getId).toList();
            List<OrderDetail> byOrderIds = new ArrayList<>();
            //TODO   判断是否存在历史订单
            if(orderIds!=null && !orderIds.isEmpty()){
                //根据订单id获取所有订单详情
                byOrderIds = orderDetailMapper.getByOrderIds(orderIds);
            }

            //获取所有的菜品id
            List<Long> orderDishIds = byOrderIds.stream().map(OrderDetail::getDishId).toList();
            List<Dish> favoriteDishes = new ArrayList<>();
            //用户购买最多的菜品
            if(orderDishIds!=null && !orderDishIds.isEmpty()){
                favoriteDishes = dishMapper.getDishByIds(orderDishIds);
            }
            //用户购买最多的就是喜欢的菜品
            //根据热度排序
            favoriteDishes = favoriteDishes.stream()
                    .sorted(Comparator.comparing(Dish::getHotSpot, Comparator.nullsLast(Comparator.reverseOrder())))
                    .toList();

            //用户浏览最多的菜品
            List<ItemView> itemViews = itemViewMapper.selectTopItemsByViewCount(DISH, userId, 10);
            //获取所有的菜品id
            List<Long> itemViewDishIds = itemViews.stream().map(ItemView::getItemId).toList();
            List<Dish> itemViewDishes = new ArrayList<>();
            if(itemViewDishIds!=null && !itemViewDishIds.isEmpty()){
                //浏览最多的菜品
                itemViewDishes = dishMapper.getDishByIds(itemViewDishIds);
            }
            //如果无浏览记录，购买记录，偏好，直接返回推荐
            if(itemViewDishes == null || itemViewDishes.isEmpty() && favoriteDishes == null || favoriteDishes.isEmpty() && byOrderIds.isEmpty() || byOrderIds==null){
                List<Map<String, Object>> recommendations = getAiRecommendations(allDishes, preference);
                return recommendations;
            }
            //从热度中取前五个菜品，从用户喜欢的菜品提取十个，从用户浏览中提取五个进行合并 去除掉重复内容，数量最多不得超过20个
            // 取前10个热门菜品
            List<Dish> topHotDishes = allDishes.stream()
                    .limit(5)
                    .toList();

            // 从用户喜欢的菜品中取十个
            favoriteDishes = favoriteDishes.stream()
                    .limit(10)
                    .toList();

            // 从用户浏览中取五个
            itemViewDishes = itemViewDishes.stream()
                    .limit(5)
                    .toList();

            // 合并热门菜品和用户喜欢的菜品和浏览最多的菜品，去除重复
            Set<Long> uniqueDishIds = new LinkedHashSet<>();  // 使用LinkedHashSet保持插入顺序
            List<Dish> mergedDishes = new ArrayList<>();

            // 按优先级顺序添加: 用户喜欢的菜品 > 浏览记录 > 热门菜品
            // 1. 先添加用户喜欢的菜品(最多10个)
            favoriteDishes.stream()
                    .limit(10)
                    .forEach(dish -> {
                        if (uniqueDishIds.add(dish.getId())) {
                            mergedDishes.add(dish);
                        }
                    });

            // 2. 添加用户浏览记录(最多5个)
            itemViewDishes.stream()
                    .limit(5)
                    .forEach(dish -> {
                        if (uniqueDishIds.add(dish.getId()) && mergedDishes.size() < 20) {
                            mergedDishes.add(dish);
                        }
                    });

            // 3. 添加热门菜品(最多5个)
            allDishes.stream()
                    .limit(5)
                    .forEach(dish -> {
                        if (uniqueDishIds.add(dish.getId()) && mergedDishes.size() < 20) {
                            mergedDishes.add(dish);
                        }
                    });

            // 4. 如果合并后的菜品数量不足20个，从所有菜品中补充
            if (mergedDishes.size() < 20) {
                allDishes.forEach(dish -> {
                    if (uniqueDishIds.add(dish.getId()) && mergedDishes.size() < 20) {
                        mergedDishes.add(dish);
                    }
                });
            }

            // 更新allDishes为合并后的列表
            allDishes = mergedDishes;

            // 2. 使用通义 AI分析用户偏好并推荐菜品
            List<Map<String, Object>> recommendations = getAiRecommendations(allDishes, preference);
            return recommendations;
        } catch (Exception e) {
            log.error("智能推荐菜品失败", e);
            return createMockRecommendations(preference);
        }
    }


    /**
     * 使用通义 AI进行智能推荐
     */
    private List<Map<String, Object>> getAiRecommendations(List<Dish> allDishes, String preference) {
        try {
            // 构建菜品信息字符串
            StringBuilder dishesInfo = new StringBuilder();
            // 限制菜品数量避免token过多
            for (int i = 0; i < Math.min(allDishes.size(), 20); i++) {
                Dish dish = allDishes.get(i);
                dishesInfo.append(String.format("%d. %s - ¥%.1f - %s\n",
                        dish.getId(), dish.getName(), dish.getPrice(),
                        dish.getDescription() != null ? dish.getDescription() : "美味佳肴"));
            }

            String userPrompt = String.format(
                    "用户偏好：%s\n\n可选菜品列表：\n%s\n\n请从上述菜品和套餐中推荐5道最符合用户偏好的菜品，至少返回一个套餐，并以JSON格式返回，格式如下：\n" +
                            "[\n" +
                            "  {\n" +
                            "    \"id\": 菜品ID,\n" +
                            "    \"name\": \"菜品名称\",\n" +
                            "    \"price\": 价格,\n" +
                            "    \"reason\": \"推荐理由（30字以内）\"\n" +
                            "  }\n" +
                            "]\n\n" +
                            "注意：只返回JSON数组，不要包含其他文字说明。",
                    preference, dishesInfo.toString()
            );

            // 调用通义 API
            String aiResponse = recommendationService.chat(userPrompt);
            log.info("通义 AI推荐响应：{}", aiResponse);

            // 解析AI响应
            List<Map<String, Object>> recommendations = parseAiRecommendations(aiResponse, allDishes);

            if (recommendations.isEmpty()) {
                // 如果AI解析失败，使用传统方法作为降级
                log.warn("AI推荐解析失败，使用传统推荐方法");
                return getTraditionalRecommendations(allDishes, preference);
            }

            return recommendations;

        } catch (Exception e) {
            log.error("通义 AI推荐失败，使用传统推荐方法", e);
            return getTraditionalRecommendations(allDishes, preference);
        }
    }

    /**
     * 解析AI推荐响应
     */
    private List<Map<String, Object>> parseAiRecommendations(String aiResponse, List<Dish> allDishes) {
        List<Map<String, Object>> recommendations = new ArrayList<>();

        try {
            // 清理响应文本，提取JSON部分
            String jsonStr = aiResponse.trim();
            jsonStr = jsonStr.trim();
            List<Map> recommendationList = JSON.parseArray(jsonStr, Map.class);

            log.info("AI推荐响应：{}", recommendationList);
            for (Map map1 : recommendationList) {
                Map<String, Object> recommendation1 = new HashMap<>();
                String idObj = map1.get("id").toString();
                Long dishId;
                dishId = Long.valueOf(idObj);
                // 查找对应的菜品
                Dish dish = allDishes.stream()
                        .filter(d -> d.getId().equals(dishId))
                        .findFirst()
                        .orElse(null);
                //查询对应的口味
                List<DishFlavor> flavors = dishFlavorMapper.getFlavorsByDishId(dishId);
                if (dish != null) {
                    recommendation1.put("id", dish.getId());
                    recommendation1.put("name", dish.getName());
                    recommendation1.put("price", dish.getPrice());
                    recommendation1.put("image", dish.getImage());
                    recommendation1.put("flavors", flavors);
                    // 提取推荐理由
                    recommendation1.put("reason", map1.get("reason"));
                }
                recommendations.add(recommendation1);
            }
            log.info("推荐结果：{}", recommendations);
        } catch (Exception e) {
            log.error("解析AI推荐响应失败", e);
        }

        return recommendations;
    }

    /**
     * 传统推荐方法（作为AI推荐的降级方案）
     */
    private List<Map<String, Object>> getTraditionalRecommendations(List<Dish> allDishes, String preference) {
        List<Dish> filteredDishes = filterDishesByPreference(allDishes, preference);
        List<Map<String, Object>> recommendations = new ArrayList<>();
        int maxRecommendations = Math.min(5, filteredDishes.size());

        for (int i = 0; i < maxRecommendations; i++) {
            Dish dish = filteredDishes.get(i);
            Map<String, Object> recommendation = new HashMap<>();
            recommendation.put("id", dish.getId());
            recommendation.put("name", dish.getName());
            recommendation.put("price", dish.getPrice());
            recommendation.put("image", dish.getImage());
            recommendation.put("reason", generateRecommendReason(dish, preference));
            recommendations.add(recommendation);
        }

        return recommendations;
    }

    /**
     * 根据用户偏好筛选菜品
     */
    private List<Dish> filterDishesByPreference(List<Dish> dishes, String preference) {
        if (preference == null || preference.trim().isEmpty()) {
            return dishes.stream().limit(5).collect(Collectors.toList());
        }

        String lowerPreference = preference.toLowerCase();
        List<Dish> filtered = new ArrayList<>();

        // 根据关键词筛选
        for (Dish dish : dishes) {
            String dishName = dish.getName().toLowerCase();
            String dishDescription = dish.getDescription() != null ? dish.getDescription().toLowerCase() : "";

            // 辣味偏好
            if (lowerPreference.contains("辣") || lowerPreference.contains("麻")) {
                if (dishName.contains("辣") || dishName.contains("麻") ||
                        dishDescription.contains("辣") || dishDescription.contains("麻")) {
                    filtered.add(dish);
                }
            }
            // 清淡偏好
            else if (lowerPreference.contains("清淡") || lowerPreference.contains("不辣") || lowerPreference.contains("淡")) {
                if (!dishName.contains("辣") && !dishName.contains("麻") &&
                        !dishDescription.contains("辣") && !dishDescription.contains("麻")) {
                    filtered.add(dish);
                }
            }
            // 汤类偏好
            else if (lowerPreference.contains("汤") || lowerPreference.contains("粉")) {
                if (dishName.contains("汤") || dishName.contains("粉") ||
                        dishDescription.contains("汤") || dishDescription.contains("粉")) {
                    filtered.add(dish);
                }
            }
            // 默认推荐热门菜品
            else {
                filtered.add(dish);
            }
        }

        // 如果筛选结果为空，返回前几个菜品
        if (filtered.isEmpty()) {
            return dishes.stream().limit(5).collect(Collectors.toList());
        }

        return filtered.stream().limit(5).collect(Collectors.toList());
    }

    /**
     * 生成推荐理由
     */
    private String generateRecommendReason(Dish dish, String preference) {
        if (preference == null || preference.trim().isEmpty()) {
            return "经典美味，值得一试";
        }

        String lowerPreference = preference.toLowerCase();
        String dishName = dish.getName().toLowerCase();

        if (lowerPreference.contains("辣") && (dishName.contains("辣") || dishName.contains("麻"))) {
            return "香辣过瘾，满足您对辣味的需求";
        } else if (lowerPreference.contains("清淡") && !dishName.contains("辣")) {
            return "口味清淡，营养健康";
        } else if (lowerPreference.contains("汤") && dishName.contains("汤")) {
            return "汤汁鲜美，暖胃又暖心";
        } else if (lowerPreference.contains("粉") && dishName.contains("粉")) {
            return "口感Q弹，汤汁浓郁";
        } else {
            return "根据您的喜好精心推荐";
        }
    }

    /**
     * 创建空推荐结果
     */
    private List<Map<String, Object>> createEmptyRecommendation() {
        return new ArrayList<>();
    }

    /**
     * 创建模拟推荐数据
     */
    private List<Map<String, Object>> createMockRecommendations(String preference) {
        List<Map<String, Object>> mockData = new ArrayList<>();
        //查询数据库获取所有菜品
        List<Dish> allDishes = dishMapper.list(new Dish());
        //根据热度进行排序
        allDishes.sort(Comparator.comparing(Dish::getHotSpot, Comparator.nullsLast(Comparator.reverseOrder())).reversed());

        //最多返回5道菜
        for (int i = 0; i < 5; i++) {
            Map<String, Object> item = new HashMap<>();
            Dish dish = allDishes.get(i);
            item.put("id", dish.getId());
            item.put("name", dish.getName());
            item.put("price", dish.getPrice());
            item.put("image", dish.getImage());
            item.put("reason", dish.getName() + "，" + generateRecommendReason(dish, preference));
            mockData.add(item);
        }

        return mockData;
    }
}
