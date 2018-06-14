package com.xieminghui.house.biz.mapper;

import com.xieminghui.house.common.model.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author Ming
 * @create 2018-06-01 下午12:36
 **/
@Mapper
public interface UserMapper {

    List<User> selectUsers();

    int insert(User account);

    int delete(String email);

    int update(User updateUser);


    List<User> selectUsersByQuery(User user);
}
