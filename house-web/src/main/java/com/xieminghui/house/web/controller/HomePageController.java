package com.xieminghui.house.web.controller;

import com.xieminghui.house.biz.service.RecommendService;
import com.xieminghui.house.common.model.House;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * describe:
 *
 * @author sunnyhuihui
 * @email xmh594603296@163.com
 * @date 10/7/18 23:57
 */
@Controller
public class HomePageController {


    @Autowired
    private RecommendService recommendService;



    @RequestMapping("/index")
    public String index(ModelMap modelMap){
        List<House> houses = recommendService.getLastest();
        modelMap.put("recomHouses",houses);
        return "/homepage/index";
    }

    @RequestMapping("")
    public String home(ModelMap modelMap){
        return "redirect:/index";
    }
}
