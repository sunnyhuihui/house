package com.xieminghui.house.biz.service;

import com.xieminghui.house.common.entity.User;

import java.util.List;

/**
 * @author Ming
 * @create 2018-06-01 下午12:41
 **/
public interface UserService {


    List<User> getUsers();

    User getUser();
}
