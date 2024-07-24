package com.lyy.user_center_backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.lyy.user_center_backend.common.ErrorCode;
import com.lyy.user_center_backend.exception.CustomException;
import com.lyy.user_center_backend.mapper.UserMapper;
import com.lyy.user_center_backend.model.domain.User;
import com.lyy.user_center_backend.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.security.PublicKey;
import java.util.regex.Pattern;

import static com.lyy.user_center_backend.constant.UserConstant.USER_LOGIN_STATE;

/**
* @author 22496
* @description 针对表【user(用户表)】的数据库操作Service实现
* @createDate 2024-07-19 16:09:27
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private  UserMapper userMapper;

    @Override
    public Long userRegister(String userAccount, String userPassword, String checkPassword){

        // 1.校验
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            throw new CustomException(ErrorCode.PARAMS_ERROR,"参数为空");
        }
        if (userAccount.length() < 2) {
            throw new CustomException(ErrorCode.PARAMS_ERROR,"用户账号过短");
        }
        if (userPassword.length() < 6 || checkPassword.length() < 6){
            throw new CustomException(ErrorCode.PARAMS_ERROR,"密码长度过短");
        }

        // 账户不能包含特殊字符
        String userAccountRegex = "^[a-zA-Z0-9_]+$";
        boolean isVaild = Pattern.compile(userAccountRegex).matcher(userAccount).matches();
        if (!isVaild) {
            throw new CustomException(ErrorCode.PARAMS_ERROR,"用户账号包含特殊字符");
        }
        // 密码和校验密码相同
        if (!userPassword.equals(checkPassword)) {
            throw new CustomException(ErrorCode.PARAMS_ERROR,"两次密码不一致");
        }

        // 2.加密
        String salt = BCrypt.gensalt();
        String encryptPassword= BCrypt.hashpw(userPassword, salt);

        // 3.判断账户是否重复
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        //queryWrapper.eq(String column, Object val)
        queryWrapper.eq("userAccount", userAccount).eq("userPassword", encryptPassword);
        long count = this.count(queryWrapper);
        if(count> 0) {
            throw new CustomException(ErrorCode.PARAMS_ERROR,"用户账号重复");
        }

        // 4.插入数据库
        User user = new User();

        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);

        boolean saveResult = this.save(user);
        if (!saveResult) {
            throw new CustomException(ErrorCode.SYSTEM_ERROR,"用户注册失败");
        }
        return user.getId();
    }

    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        // 1.校验
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new CustomException(ErrorCode.PARAMS_ERROR,"参数为空");
        }
        if (userAccount.length() < 2) {
            throw new CustomException(ErrorCode.PARAMS_ERROR,"用户账号过短");
        }
        if (userPassword.length() < 6) {
            throw new CustomException(ErrorCode.PARAMS_ERROR,"密码长度过短");
        }

        // 账户不能包含特殊字符
        String userAccountRegex = "^[a-zA-Z0-9_]+$";
        boolean isVaild = Pattern.compile(userAccountRegex).matcher(userAccount).matches();
        if (!isVaild) {
            throw new CustomException(ErrorCode.PARAMS_ERROR,"用户账号包含特殊字符");
        }

        // 3.查询用户是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        User user = this.getOne(queryWrapper);

        // 4.判断用户是否存在
        if (user == null) {
            throw new CustomException(ErrorCode.USER_NOT_EXIST,"用户不存在");
        }

        // 5.判断密码是否正确
        if (!BCrypt.checkpw(userPassword, user.getUserPassword())) {
            throw new CustomException(ErrorCode.USER_PASSWORD_ERROR,"密码错误");
        }

        // 6.登录成功，将用户信息存入session
        request.getSession().setAttribute(USER_LOGIN_STATE, user);

        // 7.信息脱敏
        User safeUser = getSafetyUser(user);
        return safeUser;
    }
    @Override
    public User getSafetyUser(User user) {
        User safeUser = new User();
        safeUser.setId(user.getId());
        safeUser.setUsername(user.getUsername());
        safeUser.setUserAccount(user.getUserAccount());
        safeUser.setAvatarUrl(user.getAvatarUrl());
        safeUser.setGender(user.getGender());
        safeUser.setPhone(user.getPhone());
        safeUser.setEmail(user.getEmail());
        safeUser.setUserStatus(user.getUserStatus());
        safeUser.setCreateTime(user.getCreateTime());
        safeUser.setUserRole(user.getUserRole());
        return safeUser;
    }
}




