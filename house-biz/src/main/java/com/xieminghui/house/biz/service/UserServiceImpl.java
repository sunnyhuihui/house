package com.xieminghui.house.biz.service;

import com.xieminghui.house.biz.mapper.UserMapper;
import com.xieminghui.house.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Ming
 * @create 2018-06-01 下午12:44
 **/
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<User> getUsers() {
        return userMapper.selectUsers();
    }

    @Override
    public User getUser() {
        return userMapper.selectUser();
    }
}
