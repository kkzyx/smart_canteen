package com.holly.chatService;
import dev.langchain4j.service.*;

//@AiService(
//        wiringMode = AiServiceWiringMode.EXPLICIT,
//        chatModel = "openAiChatModel",
//        streamingChatModel = "openAiStreamingChatModel",
//        chatMemoryProvider = "chatMemoryProvider",
//        tools = {"orderTool","dishRecommendTool"}
//)
public interface ConsultantService {

    //用于聊天的方法，message为用户输入的内容
    @SystemMessage(fromResource = "Consultant.txt")
    TokenStream chat(@MemoryId String memoryId, @UserMessage String message);
}
