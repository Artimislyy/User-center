package com.lyy.user_center_backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lyy.user_center_backend.model.domain.User;

import javax.servlet.http.HttpServletRequest;

/**
* @author 22496
* @description 针对表【user(用户表)】的数据库操作Service
* @createDate 2024-07-19 16:09:27
*/
public interface UserService extends IService<User> {
    /**
     *
     * @param userAccount
     * @param userPassword
     * @param checkPassword
     * @return
     */
    Long userRegister(String userAccount, String userPassword, String checkPassword);

    User userLogin(String userAccount, String userPassword, HttpServletRequest request);

    User getSafetyUser(User user);

}
