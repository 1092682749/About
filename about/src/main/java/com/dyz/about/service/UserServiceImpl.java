package com.dyz.about.service;

import com.dyz.about.dao.RolePermissionMapper;
import com.dyz.about.dao.ShiroUserMapper;
import com.dyz.about.dao.UserRoleMapper;
import com.dyz.about.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserServiceImpl {
    @Resource
    ShiroUserMapper shiroUserMapper;
    @Resource
    UserRoleMapper userRoleMapper;
    @Resource
    RolePermissionMapper rolePermissionMapper;
    public ShiroUser findByID(int id) {
        ShiroUser shiroUser = shiroUserMapper.findByID(id);
        List<UserRole> userRoles = userRoleMapper.findByUID(id);
        Set<RolePermission> permissions = new HashSet<>();
        for (UserRole userRole : userRoles) {
            permissions.addAll(rolePermissionMapper.findByRID(userRole.getRid()));
        }
        shiroUser.setUserRoleList(userRoles);
        return shiroUser;
    }
    public ShiroUser findByName(String name) {
        return shiroUserMapper.findByName(name);
    }
}
