package com.dyz.about;

import com.dyz.about.dao.ChatMsgRecordMapper;
import com.dyz.about.dao.ClassMapper;
import com.dyz.about.dao.ShiroRoleMapper;
import com.dyz.about.dao.ShiroUserMapper;
import com.dyz.about.model.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest
public class AboutApplicationTests {
    @Resource
    ShiroUserMapper shiroUserMapper;
    @Resource
    ShiroRoleMapper shiroRoleMapper;
    @Resource
    ClassMapper classMapper;
    @Resource
    ChatMsgRecordMapper chatMsgRecordMapper;

    @Test
    public void contextLoads() {
        Classes classes2 = chatMsgRecordMapper.findClassByID(1);
        Classes classes = classMapper.getClass2(1);
        Classes classes1 = classMapper.getClass3(1);
        Teacher teacher = classes.getTeacher();
        List<ShiroRole> shiroRole = shiroRoleMapper.findByID(1);
//        List<RolePermission> list = shiroRole.getRolePermissionList();
//        for (RolePermission rolePermission : list) {
//            System.out.println(rolePermission);
//        }
        ShiroUser shiroUser = shiroUserMapper.findByID(1);
        List<UserRole> userRoleList = shiroUser.getUserRoleList();
        System.out.println(shiroUser);
    }

}
