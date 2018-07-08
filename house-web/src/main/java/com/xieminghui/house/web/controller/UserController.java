package com.xieminghui.house.web.controller;

import com.xieminghui.house.common.constants.CommonConstants;
import com.xieminghui.house.common.model.User;
import com.xieminghui.house.biz.service.UserService;
import com.xieminghui.house.common.result.ResultMsg;
import com.xieminghui.house.common.utils.HashUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @author Ming
 * @create 2018-06-01 下午12:40
 **/
@Controller
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
            modelMap.put("email",account.getEmail());
            return "/user/accounts/registerSubmit";
        }else {
            return "redirect:/accounts/register?"+resultMsg.asUrlParams();
        }
    }

    @RequestMapping("accounts/verify")
    public String verify(String key){
        boolean result = userService.enable(key);
        if(result){
            return "redirect:/index?"+ResultMsg.successMsg("激活成功").asUrlParams();
        }else {
            return "redirect:/accounts/register?"+ResultMsg.errorMsg("激活失败,请确认是否过期");
        }
    }


//----------------------------------------
    /**
     * 登录接口
     */
    @RequestMapping("/accounts/signin")
    public String signin(HttpServletRequest req) {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        //这是用户提交的目标页
        String target = req.getParameter("target");
        if (username == null || password == null) {
            //保存目标也起来，等会登陆成功给他返回
            req.setAttribute("target", target);
            return "/user/accounts/signin";
        }else {
            User user = userService.auth(username,password);
            if(user == null){
                return "redirect:/accounts/signin?" + "target=" + target + "&username=" + username + "&"
                        + ResultMsg.errorMsg("用户名或密码错误").asUrlParams();
            }else {
                HttpSession session = req.getSession(true);
                session.setAttribute(CommonConstants.USER_ATTRIBUTE,user);
                session.setAttribute(CommonConstants.PLAIN_USER_ATTRIBUTE,user);

                return StringUtils.isBlank(target) ? "redirect:/index" : "redirect:" + target;
            }
        }
    }


    /**
     * 登出接口，也就是删除session信息
     * @return
     */
    @RequestMapping("/accounts/logout")
    public String logout(HttpServletRequest request){
        HttpSession session = request.getSession(true);
        //这个方法会注销session的内容
        session.invalidate();
        return "redirect:/index";
    }

    @RequestMapping("/users")
    public List<User> getUsers(){
        return userService.getUsers();
    };




    // ---------------------个人信息页-------------------------
    /**
     * 1.能够提供页面信息 2.更新用户信息
     *
     * @param updateUser
     * @param model
     * @return
     */
    @RequestMapping("/accounts/profile")
    public String profile(HttpServletRequest req, User updateUser, ModelMap model) {
        if (updateUser.getEmail() == null) {
            return "/user/accounts/profile";
        }
        userService.updateUser(updateUser, updateUser.getEmail());
        User query = new User();
        query.setEmail(updateUser.getEmail());
        List<User> users = userService.getUserByQuery(query);
        req.getSession(true).setAttribute(CommonConstants.USER_ATTRIBUTE, users.get(0));
        return "redirect:/accounts/profile?" + ResultMsg.successMsg("更新成功").asUrlParams();
    }


    /**
     * 修改密码
     * @param email
     * @param password
     * @param newPassword
     * @param confirmPassword
     * @param mode
     * @return
     */
    @RequestMapping("accounts/changePassword")
    public String changePassword(String email, String password, String newPassword,
                                 String confirmPassword, ModelMap mode) {
        User user = userService.auth(email, password);
        if (user == null || !confirmPassword.equals(newPassword)) {
            return "redirct:/accounts/profile?" + ResultMsg.errorMsg("密码错误").asUrlParams();
        }
        User updateUser = new User();
        updateUser.setPasswd(HashUtils.encryPassword(newPassword));
        userService.updateUser(updateUser, email);
        return "redirect:/accounts/profile?" + ResultMsg.successMsg("更新成功").asUrlParams();
    }

}
