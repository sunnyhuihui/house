package com.xieminghui.house.web.controller;

import com.xieminghui.house.biz.service.AgencyService;
import com.xieminghui.house.biz.service.HouseService;
import com.xieminghui.house.biz.service.MailService;
import com.xieminghui.house.biz.service.RecommendService;
import com.xieminghui.house.common.constants.CommonConstants;
import com.xieminghui.house.common.model.Agency;
import com.xieminghui.house.common.model.House;
import com.xieminghui.house.common.model.User;
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
 * @date 9/7/18 22:44
 */
@Controller
public class AgencyController {


    @Autowired
    private AgencyService agencyService;

    @Autowired
    private HouseService houseService;

    @Autowired
    private RecommendService recommendService;

    @Autowired
    private MailService mailService;

    @RequestMapping("/agency/agentList")
    public String agentList(Integer pageSize,Integer pageNum,ModelMap modelMap){
        if (pageSize == null){
            pageSize = 6;
        }
        List<House> houses =  recommendService.getHotHouse(CommonConstants.RECOM_SIZE);
        PageData<User> ps = agencyService.getAllAgent(PageParams.build(pageSize,pageNum));
        modelMap.put("recomHouses", houses);
        modelMap.put("ps",ps);
        return "/user/agent/agentList";
    }

    @RequestMapping("/agency/agentDetail")
    public String agentDetail(Long id, ModelMap modelMap){
        User user =  agencyService.getAgentDeail(id);
        List<House> houses =  recommendService.getHotHouse(CommonConstants.RECOM_SIZE);
        House query = new House();
        //通过userid 查询房子信息
        query.setUserId(id);
        query.setBookmarked(false);
        PageData<House> bindHouse = houseService.queryHouse(query, new PageParams(3,1));
        if (bindHouse != null) {
            modelMap.put("bindHouses", bindHouse.getList()) ;
        }
        modelMap.put("recomHouses", houses);
        modelMap.put("agent", user);
        modelMap.put("agencyName", user.getAgencyName());
        return "/user/agent/agentDetail";
    }


    @RequestMapping("/agency/agentMsg")
    public String agentMsg(Long id,String msg,String name,String email, ModelMap modelMap){
        User user =  agencyService.getAgentDeail(id);
        mailService.sentEmail("咨询", "email:"+email+",msg:"+msg, user.getEmail());
        return "redirect:/agency/agentDetail?id="+id +"&" + ResultMsg.successMsg("留言成功").asUrlParams();
    }

    @RequestMapping("/agency/agencyDetail")
    public String agencyDetail(Integer id,ModelMap modelMap){
        Agency agency =  agencyService.getAgency(id);
        List<House> houses =  recommendService.getHotHouse(CommonConstants.RECOM_SIZE);
        modelMap.put("recomHouses", houses);
        modelMap.put("agency", agency);
        return "/user/agency/agencyDetail";
    }





}

