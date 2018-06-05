package com.xieminghui.house.biz.mapper;

import com.xieminghui.house.common.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author Ming
 * @create 2018-06-01 下午12:36
 **/
@Mapper
public interface UserMapper {

    List<User> selectUsers();

    User selectUser();
}
