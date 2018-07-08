package com.xieminghui.house.web.controller;

import com.xieminghui.house.biz.service.HouseService;
import com.xieminghui.house.common.model.House;
import com.xieminghui.house.common.page.PageData;
import com.xieminghui.house.common.page.PageParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * describe:
 *
 * @author sunnyhuihui
 * @email xmh594603296@163.com
 * @date 8/7/18 11:53
 */
@Controller
public class HouseController {
    @Autowired
    private HouseService houseService;


    /**
     *  1 实现分页
     *  2 支持小区搜索 类型搜索
     *  3 支持排序
     *  4 支持图片 价格 标题 地址等信息
     * @return
     */
    @RequestMapping("/house/list")
    public String houseList(Integer pageSize, Integer pageNum, House query, ModelMap modelMap){
        PageData<House> ps = houseService .queryHouse(query,PageParams.build(pageSize,pageNum));
        modelMap.put("ps",ps);
        modelMap.put("vo",query);
        return "/house/listing";
    }
}
