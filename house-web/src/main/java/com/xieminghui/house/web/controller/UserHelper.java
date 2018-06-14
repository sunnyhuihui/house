package com.xieminghui.house.web.controller;

import com.xieminghui.house.common.model.User;
import com.xieminghui.house.common.result.ResultMsg;
import org.springframework.util.StringUtils;

/**
 * @author Ming
 * @create 2018-06-06 下午2:02
 **/
public class UserHelper {

    public static ResultMsg validate(User account){
        if (StringUtils.isEmpty(account.getEmail())) {
            return ResultMsg.errorMsg("Email 有误");
        }
        if (StringUtils.isEmpty(account.getEmail())) {
            return ResultMsg.errorMsg("参数 有误");
        }
        if (StringUtils.isEmpty(account.getConfirmPasswd()) || StringUtils.isEmpty(account.getPasswd())
                || !account.getPasswd().equals(account.getConfirmPasswd())) {
            return ResultMsg.errorMsg("参数 有误");
        }
        if (account.getPasswd().length() < 6) {
            return ResultMsg.errorMsg("密码需要大于6位");
        }
        return ResultMsg.successMsg("");
    }
}
