package com.xieminghui.house;


import com.xieminghui.house.common.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import com.xieminghui.house.biz.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class AuthTest {

    @Autowired
    private UserService userService;


    @Test
    public void testAuth(){
        User user = userService.auth("xmh594603296@163.com","666666");
        assert user !=null;
    }

}
