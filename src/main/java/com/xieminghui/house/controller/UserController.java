package com.xieminghui.house.controller;

import com.xieminghui.house.common.entity.User;
import com.xieminghui.house.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author Ming
 * @create 2018-06-01 下午12:40
 **/
@RestController
public class UserController {


    @Autowired
    private UserService userService;


    @RequestMapping("/hello")
    public String hello(){
        return "hello";
    }



    @RequestMapping("/user")
    public User getUser(){
        return userService.getUser();
    }

    @RequestMapping("/users")
    public List<User> getUsers(){
        return userService.getUsers();
    };

}
