package com.xieminghui.house.biz.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import com.xieminghui.house.common.model.House;
import com.xieminghui.house.common.page.PageParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RecommendService {


  //热门房产的key
  private static final String HOT_HOUSE_KEY = "hot_house";


  private static final Logger logger = LoggerFactory.getLogger(RecommendService.class);

  @Autowired
  private HouseService houseService;


  public void increase(Long id) {
    try {
      Jedis jedis = new Jedis("127.0.0.1");
      //zincrby 这个方法 可以根据id进行加分  1.0代表增加的分数  menber是String类型的
      jedis.zincrby(HOT_HOUSE_KEY, 1.0D, id + "");
      //将列表维持一个数值， 就是要求key超过10的以上的删除掉  排序后
      jedis.zremrangeByRank(HOT_HOUSE_KEY, 0, -11);// 0代表第一个元素,-1代表最后一个元素，保留热度由低到高末尾10个房产
      jedis.close();
    } catch (Exception e) {
      logger.error(e.getMessage(),e);
    }
   
  }


  //得到热门房产
  public List<Long> getHot() {
    try {
      Jedis jedis = new Jedis("127.0.0.1");
      //表示从高到低
      Set<String> idSet = jedis.zrevrange(HOT_HOUSE_KEY, 0, -1);
      jedis.close();
      List<Long> ids = idSet.stream().map(Long::parseLong).collect(Collectors.toList());
      return ids;
    } catch (Exception e) {
      logger.error(e.getMessage(), e);//有同学反应在未安装redis时会报500,在这里做下兼容,
      return Lists.newArrayList();
    }

  }


  //通过大小进行查询热门房产
  public List<House> getHotHouse(Integer size) {
    House query = new House();
    //得到热门列表的id
    List<Long> list = getHot();
    //得到想要查询的列表id范围
    list = list.subList(0,Math.min(list.size(),size));
    if(list.isEmpty()){
      return Lists.newArrayList();
    }
    query.setIds(list);
    final List<Long> orderList = list;
    List<House> houses = houseService.queryAndSetImg(query,new PageParams(size,1));

    //google的内部排序类
    Ordering<House> houseSort = Ordering.natural().onResultOf(hs ->{
      return orderList.indexOf(hs.getId());
    });

    return houseSort.sortedCopy(houses);
  }

  public List<House> getLastest() {
    House query = new House();
    query.setSort("create_time");
    List<House> houses = houseService.queryAndSetImg(query, new PageParams(8, 1));
    return houses;
  }
}
