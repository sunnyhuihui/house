package com.xieminghui.house.common.entity;

import lombok.Data;

import java.util.Date;

/**
 * @author Ming
 * @create 2018-06-01 下午12:18
 **/
@Data
public class User {


    private Long id;

    private String email;

    private String phone;

    private String name;

    private String passwd;

    private String confirmPasswd;

    private int type;

    private Date createTime;
    
    private String avator;

    private String avatorFile;

    private String newPassword;


    private int enable;

    private Long agencyId;



}
