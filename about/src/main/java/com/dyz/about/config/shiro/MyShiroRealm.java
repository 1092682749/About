package com.dyz.about.config.shiro;

import com.dyz.about.dao.ShiroPermissionMapper;
import com.dyz.about.dao.ShiroRoleMapper;
import com.dyz.about.model.*;
import com.dyz.about.service.UserServiceImpl;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.constraints.Size;
import java.util.List;

//实现AuthorizingRealm接口用户用户认证
public class MyShiroRealm extends AuthorizingRealm {

    //用于用户查询
    @Autowired
    private UserServiceImpl userServiceImpl;
    @Autowired
    private ShiroRoleMapper shiroRoleMapper;
    @Autowired
    private ShiroPermissionMapper shiroPermissionMapper;

    //角色权限和对应权限添加
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        //获取登录用户名
        Integer name= (Integer) principalCollection.getPrimaryPrincipal();
        //查询用户名称
        ShiroUser user = userServiceImpl.findByID(name);
        //添加角色和权限
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        List<ShiroRole> roleList = shiroRoleMapper.findByUID(user.getId());
        List<ShiroPermission> shiroPermissions = shiroPermissionMapper.findByUID(user.getId());
        for (ShiroRole role:roleList) {
            //添加角色
            simpleAuthorizationInfo.addRole(role.getRolename());
        }
        for (ShiroPermission permission: shiroPermissions) {
            //添加权限
            simpleAuthorizationInfo.addStringPermission(permission.getPermission());
        }
        return simpleAuthorizationInfo;
    }

    //用户认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        //加这一步的目的是在Post请求的时候会先进认证，然后在到请求
        if (authenticationToken.getPrincipal() == null) {
            return null;
        }
        //获取用户信息
        String name = authenticationToken.getPrincipal().toString();
        ShiroUser user = userServiceImpl.findByName(name);
        if (user == null) {
            //这里返回后会报出对应异常
            return null;
        } else {
            //这里验证authenticationToken和simpleAuthenticationInfo的信息
            SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(user.getId(), user.getPassword().toString(), getName());
            return simpleAuthenticationInfo;
        }
    }
}
