package com.xieminghui.house.biz.service;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.xieminghui.house.biz.mapper.HouseMapper;
import com.xieminghui.house.common.constants.HouseUserType;
import com.xieminghui.house.common.model.*;
import com.xieminghui.house.common.page.PageData;
import com.xieminghui.house.common.page.PageParams;
import com.xieminghui.house.common.utils.BeanHelper;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HouseService {
	
	@Autowired
	private HouseMapper houseMapper;
	
	@Value("${file.prefix}")
	private String imgPrefix;
	
	@Autowired
	private FileService fileService;
	
	@Autowired
	private AgencyService agencyService;
	
	@Autowired
	private MailService mailService;
	

	/**
	 * 1.查询小区
	 * 2.添加图片服务器地址前缀
	 * 3.构建分页结果
	 * @param query
	 */
	public PageData<House> queryHouse(House query, PageParams pageParams) {
		List<House> houses = Lists.newArrayList();
		//查询的房子的名字
		if (!Strings.isNullOrEmpty(query.getName())) {
			Community community = new Community();
			community.setName(query.getName());
			//通过名字查找小区列表
			List<Community> communities = houseMapper.selectCommunity(community);
			if (!communities.isEmpty()) {
				//通过名字查找小区id
				query.setCommunityId(communities.get(0).getId());
			}
		}
		//查询房屋
		houses = queryAndSetImg(query,pageParams);//添加图片服务器地址前缀
		Long count = houseMapper.selectPageCount(query);

		//构建分页的数据 里面有房屋的信息  包括分页的信息
		return PageData.buildPage(houses, count, pageParams.getPageSize(), pageParams.getPageNum());
	}

	public List<House> queryAndSetImg(House query, PageParams pageParams) {
		List<House> houses =   houseMapper.selectPageHouses(query, pageParams);
		houses.forEach(h ->{
			//添加房子首页图
			h.setFirstImg(imgPrefix + h.getFirstImg());
			//添加房子详情页
			h.setImageList(h.getImageList().stream().map(img -> imgPrefix + img).collect(Collectors.toList()));
			//添加房子户型图
		    h.setFloorPlanList(h.getFloorPlanList().stream().map(img -> imgPrefix + img).collect(Collectors.toList()));
		});
		return houses;
	}

	/**
	 *
	 * @return 返回所有小区的列表
	 */
	public List<Community> getAllCommunitys() {
		Community community = new Community();
		return houseMapper.selectCommunity(community);
	}

	/**
	 * 1 添加房屋图片  需要借入fileService
	 * 2 添加户型图片
	 * 3 插入房产信息
	 * 4 绑定用户到房产的关系
	 * @param house
	 * @param user
	 */
	public void addHouse(House house, User user) {
		if (CollectionUtils.isNotEmpty(house.getHouseFiles())) {
			//借助工具类 把多个图片用 ， 号分别开来   List<MultipartFile>
			String images = Joiner.on(",").join(fileService.getImgPaths(house.getHouseFiles()));
		    house.setImages(images);
		}
		if (CollectionUtils.isNotEmpty(house.getFloorPlanFiles())) {
			String images = Joiner.on(",").join(fileService.getImgPaths(house.getFloorPlanFiles()));
		    house.setFloorPlan(images);
		}
		BeanHelper.onInsert(house);
		houseMapper.insert(house);
		bindUser2House(house.getId(),user.getId(),false);
	}

	public void bindUser2House(Long houseId, Long userId, boolean isCollect) {
      HouseUser existhouseUser =     houseMapper.selectHouseUser(userId,houseId,isCollect ? HouseUserType.BOOKMARK.value : HouseUserType.SALE.value);
      //查看房子和用户 关系是否存在，如果存在，就不需要在此绑定， 否则就需要绑定 两者的关系
	  if (existhouseUser != null) {
		  return;
	  }
	  HouseUser houseUser = new HouseUser();
	  houseUser.setHouseId(houseId);
	  houseUser.setUserId(userId);
	  houseUser.setType(isCollect ? HouseUserType.BOOKMARK.value : HouseUserType.SALE.value);
	  BeanHelper.setDefaultProp(houseUser, HouseUser.class);
	  BeanHelper.onInsert(houseUser);
	  houseMapper.insertHouseUser(houseUser);
	}

	public HouseUser getHouseUser(Long houseId){
		HouseUser houseUser =  houseMapper.selectSaleHouseUser(houseId);
		return houseUser;
	}
	
	public House queryOneHouse(Long id) {
		House query = new House();
		query.setId(id);
		List<House> houses = queryAndSetImg(query, PageParams.build(1, 1));
		if (!houses.isEmpty()) {
			return houses.get(0);
		}
		return null;
	}

	public void addUserMsg(UserMsg userMsg) {
        BeanHelper.onInsert(userMsg);
        houseMapper.insertUserMsg(userMsg);
        User agent = agencyService.getAgentDeail(userMsg.getAgentId());
		mailService.sentEmail("来自用户"+userMsg.getEmail()+"的留言", userMsg.getMsg(), agent.getEmail());
	}

	public void updateRating(Long id, Double rating) {
		House house = queryOneHouse(id);
		Double oldRating = house.getRating();
		//不能超过5分
		Double newRating  = oldRating.equals(0D)? rating : Math.min((oldRating+rating)/2, 5);
		House updateHouse = new House();
		updateHouse.setId(id);
		updateHouse.setRating(newRating);
		BeanHelper.onUpdate(updateHouse);
		houseMapper.updateHouse(updateHouse);
	}

	public void unbindUser2House(Long id, Long userId, HouseUserType type) {
	  if (type.equals(HouseUserType.SALE)) {
	      houseMapper.downHouse(id);
	    }else {
	      houseMapper.deleteHouseUser(id, userId, type.value);
	    }
	    
	}

}
