package com.holly.controller.store;

import com.holly.constant.JwtClaimsConstant;
import com.holly.context.BaseContext;
import com.holly.dto.EmployeeDTO;
import com.holly.dto.EmployeeLoginDTO;
import com.holly.entity.Employee;
import com.holly.properties.JwtProperties;
import com.holly.query.EmployeePageQueryDTO;
import com.holly.result.PageResult;
import com.holly.result.Result;
import com.holly.service.EmployeeService;
import com.holly.utils.JwtUtil;
import com.holly.vo.EmployeeLoginVO;
import com.holly.vo.EmployeeVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

/**
 * @description
 */
@Slf4j
@Tag(name = "员工相关接口")
@RestController
@RequestMapping("/store/employee")
@RequiredArgsConstructor
public class EmployeeController {
  
  private final EmployeeService employeeService;
  private final JwtProperties jwtProperties;
  
  @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
  @Operation(summary = "员工登录")
  public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
    log.info("员工登录，参数==> {}", employeeLoginDTO);
    
    // 调用service层方法，进行登录验证
    Employee employee = employeeService.login(employeeLoginDTO);
    
    // 能往下运行没有报错就说明登录成功，生成token
    HashMap<String, Object> claims = new HashMap<>();
    claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
    String token = JwtUtil.createJWT(jwtProperties.getStoreSecretKey(), jwtProperties.getStoreTtl(), claims);
    
    // 将数据封装到专门的VO对象中，供前端使用展示
    EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
            .id(employee.getId())
            .userName(employee.getUsername())
            .name(employee.getName())
            .token(token)
            .build();
    log.info("员工登录成功，返回结果==> {}", employeeLoginVO);
    // 返回登录成功的结果
    return Result.success(employeeLoginVO);
  }
  
  // 退出登录
  @PostMapping("/logout")
  @Operation(summary = "员工退出")
  public Result<String> logout() {
    return Result.success();
  }

  /**
   * 获取当前登录员工信息
   */
  @GetMapping("/me")
  @Operation(summary = "获取当前登录员工信息")
  public Result<EmployeeVO> getCurrentEmployee() {
    Long currentId = BaseContext.getUserId();
    log.info("获取当前员工信息，员工ID：{}", currentId);

    Employee employee = employeeService.getEmployeeById(currentId);
    if (employee == null) {
      return Result.error("员工不存在");
    }

    EmployeeVO employeeVO = EmployeeVO.builder()
            .id(employee.getId())
            .name(employee.getName())
            .username(employee.getUsername())
            .phone(employee.getPhone())
            .sex(employee.getSex())
            .status(employee.getStatus())
            .createTime(employee.getCreateTime())
            .updateTime(employee.getUpdateTime())
            .createUser(employee.getCreateUser())
            .updateUser(employee.getUpdateUser())
            .build();

    return Result.success(employeeVO);
  }
  
  /**
   * 新增员工
   * @param employeeDTO 员工信息数据传输对象
   */
  @PostMapping
  @Operation(summary = "新增员工")
  public Result<?> save(@RequestBody EmployeeDTO employeeDTO) {
    log.info("新增员工，参数==> {}", employeeDTO);
    employeeService.save(employeeDTO);
    return Result.success();
  }
  
  /**
   * 员工分页查询
   * @param employeePageQueryDTO 员工分页查询数据传输对象（员工姓名、页码、每页显示条数）
   * @return 员工分页查询结果
   */
  @GetMapping("/page")
  @Operation(summary = "员工分页查询")
  public Result<PageResult> page(EmployeePageQueryDTO employeePageQueryDTO) {
    log.info("员工分页查询，参数==> {}", employeePageQueryDTO);
    PageResult pageResult = employeeService.pageQuery(employeePageQueryDTO);
    return Result.success(pageResult);
  }
  
  /**
   * 启用禁用员工账号
   * @param id 员工id
   * @param status 员工账号状态（0：禁用，1：启用）
   */
  @PostMapping("/status")
  @Operation(summary = "启用禁用员工账号")
  public Result<?> startOrStop(@RequestParam Long id, @RequestParam Integer status) {
    log.info("启用禁用员工账号，参数==> employeeId: {}, status: {}", id, status);
    employeeService.startOrStop(id, status);
    return Result.success();
  }
  
  @GetMapping
  @Operation(summary = "通过员工id查询员工信息")
  public Result<Employee> getEmployeeById(@RequestParam Long id) {
    log.info("通过员工id查询员工信息，员工id==> {}", id);
    Employee employee = employeeService.getEmployeeById(id);
    return Result.success(employee);
  }
  
  @PutMapping
  @Operation(summary = "编辑员工信息")
  public Result<?> update(@RequestBody EmployeeDTO employeeDTO) {
    log.info("编辑员工信息，参数==> {}", employeeDTO);
    employeeService.update(employeeDTO);
    return Result.success();
  }
  
  @DeleteMapping
  @Operation(summary = "根据员工id删除员工")
  public Result<?> deleteEmployeeById(@RequestParam Long id) {
    log.info("删除员工，员工id==> {}", id);
    employeeService.deleteEmployeeById(id);
    return Result.success();
  }
}
