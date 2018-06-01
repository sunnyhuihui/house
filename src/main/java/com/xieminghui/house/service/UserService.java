package com.xieminghui.house.service;

import com.xieminghui.house.common.entity.User;
import com.xieminghui.house.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author Ming
 * @create 2018-06-01 下午12:41
 **/
public interface UserService {


    List<User> getUsers();

    User getUser();
}
