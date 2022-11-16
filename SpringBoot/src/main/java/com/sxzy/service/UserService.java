package com.sxzy.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sxzy.mapper.UserMapper;
import com.sxzy.pojo.User;
import org.springframework.stereotype.Service;


@Service
public class UserService extends ServiceImpl<UserMapper, User>  {

    private UserMapper userMapper;

    QueryWrapper<User> queryWrapper = new QueryWrapper<User>();

    public UserService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }


    //通过用户名查询用户
    public User selectOneByUsername(String username) {
        queryWrapper.eq("username", username);
        return  userMapper.selectOne(queryWrapper);
    }

    //注册一个用户
    public void insertOneUser(String username, String password, String company) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setCompany(company);
        userMapper.insert(user);
    }

    //判断用户是否已经存在
    public boolean ifUserAlreadyExists(String username) {
        queryWrapper.exists("select * from login where username = " + "\"" + username + "\"");
        boolean flag = userMapper.exists(queryWrapper);
        if (flag == true){
            return true;
        }else {
            return false;
        }
    }


}
