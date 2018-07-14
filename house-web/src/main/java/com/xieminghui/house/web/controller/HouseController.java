package com.xieminghui.house.web.controller;

import com.xieminghui.house.biz.service.*;
import com.xieminghui.house.common.constants.CommonConstants;
import com.xieminghui.house.common.constants.HouseUserType;
import com.xieminghui.house.common.model.*;
import com.xieminghui.house.common.page.PageData;
import com.xieminghui.house.common.page.PageParams;
import com.xieminghui.house.common.result.ResultMsg;
import com.xieminghui.house.web.interceptor.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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

    @RequestMapping("/house/detail")
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

    @RequestMapping("/house/leaveMsg")
    public String houseMsg(UserMsg userMsg){
        houseService.addUserMsg(userMsg);
        return "redirect:/house/detail?id=" + userMsg.getHouseId() + ResultMsg.successMsg("留言成功").asUrlParams();
    }


    @RequestMapping("/house/toAdd")
    public String toAdd(ModelMap map){
        map.put("citys",cityService.getAllCitys());
        map.put("communitys",houseService.getAllCommunitys());
        return "house/add";
    }

    /**
     * 1 获取用户
     * 2 设置房产状态
     * 3 加入数据库
     * @param house
     * @return
     */
    @RequestMapping("/house/add")
    public String add(House house){
        User user = UserContext.getUser();
        house.setState(CommonConstants.HOUSE_STATE_UP);
        houseService.addHouse(house,user);
        return "redirect:/house/ownlist";
    }


    @RequestMapping("/house/ownlist")
    public String ownlist(House house,Integer pageNum,Integer pageSize,ModelMap modelMap){
        User user = UserContext.getUser();
        house.setUserId(user.getId());
        house.setBookmarked(false);
        modelMap.put("ps",houseService.queryHouse(house,PageParams.build(pageSize,pageNum)));
        modelMap.put("pageType","own");
        return "house/ownlist";
    }


    /**
     * house 评分
     */
    @RequestMapping("/house/rating")
    @ResponseBody
    public ResultMsg houseRate(Double rating,Long id){
        houseService.updateRating(id,rating);
        return ResultMsg.successMsg("ok");
    }


    /**
     *  收藏
     * @param id
     * @return
     */
    @RequestMapping("/house/bookmark")
    @ResponseBody
    public ResultMsg bookmark(Long id){
        User user = UserContext.getUser();
        //第三个参数表示是否收藏
        houseService.bindUser2House(id,user.getId(),true);
        return ResultMsg.successMsg("ok");
    }

    /**
     *  删除收藏
     * @param id
     * @return
     */
    @RequestMapping("/house/unbookmark")
    @ResponseBody
    public ResultMsg unbookmark(Long id){
        User user = UserContext.getUser();
        //第三个参数表示是否收藏
        houseService.unbindUser2House(id,user.getId(),HouseUserType.BOOKMARK);
        return ResultMsg.successMsg("ok");
    }

    @RequestMapping(value="house/del")
    public String delsale(Long id,String pageType){
        User user = UserContext.getUser();
        houseService.unbindUser2House(id,user.getId(),pageType.equals("own")?HouseUserType.SALE:HouseUserType.BOOKMARK);
        return "redirect:/house/ownlist";
    }



    //收藏列表
    @RequestMapping("/house/bookmarked")
    public String bookmarked(House house,Integer pageNum,Integer pageSize,ModelMap modelMap){
        User user = UserContext.getUser();
        house.setBookmarked(true);
        house.setUserId(user.getId());
        modelMap.put("ps",houseService.queryHouse(house,PageParams.build(pageSize,pageNum)));
        modelMap.put("pageType","book");
        return "/house/ownlist";
    }



}
