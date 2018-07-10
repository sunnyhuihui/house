package com.xieminghui.house.web.controller;

import com.xieminghui.house.biz.service.*;
import com.xieminghui.house.common.constants.CommonConstants;
import com.xieminghui.house.common.model.*;
import com.xieminghui.house.common.page.PageData;
import com.xieminghui.house.common.page.PageParams;
import com.xieminghui.house.common.result.ResultMsg;
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
 * @date 8/7/18 11:53
 */
@Controller
public class HouseController {
    @Autowired
    private HouseService houseService;
    @Autowired
    private AgencyService agencyService;
    @Autowired
    private RecommendService recommendService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private CityService cityService;

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
        List<House> hotHouse = recommendService.getHotHouse(CommonConstants.RECOM_SIZE);
        modelMap.put("recomHouses",hotHouse);
        modelMap.put("ps",ps);
        modelMap.put("vo",query);
        return "/house/listing";
    }

    @RequestMapping("house/detail")
    public String houseDetail(Long id,ModelMap modelMap){
        //根据房屋的id查到房屋
        House house = houseService.queryOneHouse(id);
        //点击热度加1，通过房屋的点击id热度来加1
        recommendService.increase(id);
        List<Comment> comments = commentService.getHouseComments(id,8);
        //然后查到房子的主人
        if(house.getUserId() !=null && !house.getUserId().equals(0)){
            modelMap.put("agent",agencyService.getAgentDeail(house.getUserId()));
        }
        List<House> rcHouses = recommendService.getHotHouse(CommonConstants.RECOM_SIZE);
        modelMap.put("recomHouses",rcHouses);
        modelMap.put("house", house);
        modelMap.put("commentList", comments);
        return "/house/detail";
    }

    @RequestMapping("house/leaveMsg")
    public String houseMsg(UserMsg userMsg){
        houseService.addUserMsg(userMsg);
        return "redirect:/house/detail?id=" + userMsg.getHouseId() + ResultMsg.successMsg("留言成功").asUrlParams();
    }




}
