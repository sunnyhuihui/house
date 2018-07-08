package com.xieminghui.house.biz.mapper;

import com.xieminghui.house.common.model.*;
import com.xieminghui.house.common.page.PageParams;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface HouseMapper {

	//记得需要Param注解
    public List<House>  selectPageHouses(@Param("house") House house, @Param("pageParams") PageParams pageParams);

    //返回总数方便分页
    public Long selectPageCount(@Param("house") House query);

	public int insert(User account);

	public List<Community> selectCommunity(Community community);

	public int insert(House house);

	public HouseUser selectHouseUser(@Param("userId") Long userId, @Param("id") Long houseId, @Param("type") Integer integer);

	public HouseUser selectSaleHouseUser(@Param("id") Long houseId);

	public int insertHouseUser(HouseUser houseUser);

	public int insertUserMsg(UserMsg userMsg);

	public int updateHouse(House updateHouse);

	public  int downHouse(Long id);

	public int deleteHouseUser(@Param("id") Long id, @Param("userId") Long userId, @Param("type") Integer value);
	
}
