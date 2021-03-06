package com.xieminghui.house.web.controller;

import com.xieminghui.house.biz.service.UserService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Ming
 * @create 2018-06-01 下午3:44
 **/
@Controller
public class HelloController {

    @Autowired
    private UserService userService;



    @RequestMapping("/index")
    public String index(){
        return "homepage/index";
    }
}
