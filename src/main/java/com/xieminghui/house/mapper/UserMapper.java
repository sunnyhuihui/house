package com.xieminghui.house.mapper;

import com.xieminghui.house.common.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @author Ming
 * @create 2018-06-01 下午12:36
 **/
@Mapper
public interface UserMapper {

    List<User> selectUsers();

    User selectUser();
}
