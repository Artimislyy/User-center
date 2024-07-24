package com.lyy.user_center_backend.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lyy.user_center_backend.common.BaseResponse;
import com.lyy.user_center_backend.common.ErrorCode;
import com.lyy.user_center_backend.common.ResultUtils;
import com.lyy.user_center_backend.exception.CustomException;
import com.lyy.user_center_backend.model.domain.User;
import com.lyy.user_center_backend.model.request.UserDeleteRequest;
import com.lyy.user_center_backend.model.request.UserLoginRequest;
import com.lyy.user_center_backend.model.request.UserRegisterRequest;
import com.lyy.user_center_backend.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.env.SystemEnvironmentPropertySourceEnvironmentPostProcessor;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.lyy.user_center_backend.constant.UserConstant.ADMIN_ROLE;
import static com.lyy.user_center_backend.constant.UserConstant.USER_LOGIN_STATE;

//里面有@RequestBody,方法返回的对象会被转换为json对象，而不是解析成一个视图
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @GetMapping("/test")
    public String test(@RequestParam String id) {
//        System.out.println(id);
        return "good";
    }

    @PostMapping("/register")
    //@RequestBody用于将 HTTP 请求的 body 换为UserRegisterRequest类型，并将其传递给方法参数userRegisterRequest。
    //@RequestBody 用于接收 json 格式数据。
    //@RequestParam 用于接收 url 地址传参
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
            throw new CustomException(ErrorCode.PARAMS_ERROR,"参数为空");
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            throw new CustomException(ErrorCode.PARAMS_ERROR,"参数为空");
        }
        Long result = userService.userRegister(userAccount, userPassword, checkPassword);
        return ResultUtils.success(result,"注册成功");
    }

    @PostMapping("/login")
    public BaseResponse<User> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (userLoginRequest == null) {
            throw new CustomException(ErrorCode.PARAMS_ERROR,"参数为空");
        }

        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new CustomException(ErrorCode.PARAMS_ERROR,"参数为空");
        }
        User user = userService.userLogin(userAccount, userPassword,request);
        return ResultUtils.success(user,"登录成功");
    }

    @GetMapping("/search")
    //根据用户名进行查询
    public BaseResponse<List<User>> searchUsers(@RequestParam String username,HttpServletRequest request){
        if(!isAdmin(request)){
            throw new CustomException(ErrorCode.FORBIDDEN,"没有权限");
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if(StringUtils.isNotBlank(username)){
            queryWrapper.like("username",username);
        }
        List<User> userList = userService.list(queryWrapper);
        List<User> list = userList.stream().map(user-> userService.getSafetyUser(user)).collect(Collectors.toList());
        return ResultUtils.success(list,"查询成功");

    }

    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteUser(@RequestBody UserDeleteRequest userDeleteRequest, HttpServletRequest request) {
        if (!isAdmin(request)) {
            throw new CustomException(ErrorCode.FORBIDDEN,"没有权限");
        }
        if (userDeleteRequest.getId() <= 0) {
            throw new CustomException(ErrorCode.PARAMS_ERROR,"用户id<=0");
        }
        Boolean result = userService.removeById(userDeleteRequest.getId());
        return ResultUtils.success(result,"删除成功");
    }
    @GetMapping("/logout")
    public BaseResponse<Boolean> userLogout(HttpServletRequest request){
        if(request.getSession()==null){
            throw new CustomException(ErrorCode.USER_NOT_LOGIN,"用户未登录");
        }
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return ResultUtils.success(true,"退出登录成功");
    }
    private boolean isAdmin(HttpServletRequest request){
        //获取当前登录的用户
        User user = (User)request.getSession().getAttribute(USER_LOGIN_STATE);
        return user != null && user.getUserRole() == ADMIN_ROLE;
    }

}
