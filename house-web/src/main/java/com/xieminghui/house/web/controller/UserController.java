package com.xieminghui.house.web.controller;

import com.xieminghui.house.common.entity.User;
import com.xieminghui.house.biz.service.UserService;
import com.xieminghui.house.common.result.ResultMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Ming
 * @create 2018-06-01 下午12:40
 **/
@RestController
public class UserController {


    @Autowired
    private UserService userService;


    /**
     *
     * @param account 用户
     * @param modelMap 返回视图
     * @return
     */
    @RequestMapping("/accounts/register")
    public String accountsRegister(User account, ModelMap modelMap){
        if(account == null || account.getName() == null){
            return "/user/accounts/register";//返回用户注册
        }
        //用户验证
        ResultMsg resultMsg = UserHelper.validate(account);
        if(resultMsg.isSuccess() && userService.addAccount(account)){
            return "/user/accounts/registerSubmit";
        }else {
            return "redirect:/accounts/register?"+resultMsg.asUrlParams();
        }



        return "";
    }

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
