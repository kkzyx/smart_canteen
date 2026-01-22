package com.holly.service.impl;

import com.holly.constant.MessageConstant;
import com.holly.constant.PasswordConstant;
import com.holly.constant.StatusConstant;
import com.holly.context.BaseContext;
import com.holly.dto.EmployeeDTO;
import com.holly.dto.EmployeeLoginDTO;
import com.holly.entity.Employee;
import com.holly.exception.AccountLockedException;
import com.holly.exception.AccountNotFoundException;
import com.holly.exception.DeletionNotAllowedException;
import com.holly.exception.PasswordErrorException;
import com.holly.mapper.EmployeeMapper;
import com.holly.query.EmployeePageQueryDTO;
import com.holly.result.PageResult;
import com.holly.service.EmployeeService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.List;
import java.util.Objects;

/**
 * @description 员工service实现类
 */
@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {
  
  private final EmployeeMapper employeeMapper;
  
  @Override
  public Employee login(EmployeeLoginDTO employeeLoginDTO) {
    // 获取前端登录表单传递过来的参数
    String username = employeeLoginDTO.getUsername();
    String password = employeeLoginDTO.getPassword();
    
    // 1、根据用户名查询是否存在该用户
    Employee employee = employeeMapper.getByUsername(username);
    // 2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
    if (employee == null) {
      throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
    }
    
    // 密码对比，将前端传递过来的明文密码进行md5加密之后再和数据库中的密码进行对比
    password = DigestUtils.md5DigestAsHex(password.getBytes());
    if (!password.equals(employee.getPassword())) {
      throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
    }
    
    // 账号是否被锁定
    if (Objects.equals(employee.getStatus(), StatusConstant.DISABLE)) {
      throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
    }
    
    // 3、验证都通过，返回实体对象
    return employee;
  }
  
  @Override
  public void save(EmployeeDTO employeeDTO) {
    Employee employee = new Employee();
    BeanUtils.copyProperties(employeeDTO, employee);
    employee.setStatus(StatusConstant.ENABLE);
    employee.setPassword(DigestUtils.md5DigestAsHex(PasswordConstant.DEFAULT_PASSWORD.getBytes()));
    employee.setCreateUser(BaseContext.getUserId());
    employee.setUpdateUser(BaseContext.getUserId());
    employeeMapper.insert(employee);
  }
  
  @Override
  public PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO) {
    int page = employeePageQueryDTO.getPage();
    int pageSize = employeePageQueryDTO.getPageSize();
    PageHelper.startPage(page, pageSize);
    
    Page<Employee> pageInfo = employeeMapper.pageQuery(employeePageQueryDTO);
    long total = pageInfo.getTotal();
    List<Employee> records = pageInfo.getResult();
    return new PageResult(total, records);
  }
  
  @Override
  public void startOrStop(Long id, Integer status) {
    Employee isAdmin = employeeMapper.getEmployeeById(id);
    if ("admin".equals(isAdmin.getUsername())) {
      throw new DeletionNotAllowedException("不能禁用管理员账号");
    }
    Employee employee = Employee.builder()
            .id(id)
            .status(status)
            .build();
    employeeMapper.update(employee);
  }
  
  @Override
  public Employee getEmployeeById(Long id) {
    Employee employee = employeeMapper.getEmployeeById(id);
    employee.setPassword("******");
    return employee;
  }
  
  @Override
  public void update(EmployeeDTO employeeDTO) {
    Employee employee = new Employee();
    BeanUtils.copyProperties(employeeDTO, employee);
    employeeMapper.update(employee);
  }
  
  @Override
  public void deleteEmployeeById(Long id) {
    Employee employee = employeeMapper.getEmployeeById(id);
    if ("admin".equals(employee.getUsername())) {
      throw new DeletionNotAllowedException("不能删除管理员账号");
    }
    employeeMapper.deleteEmployeeById(id);
  }
}
