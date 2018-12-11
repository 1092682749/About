package com.dyz.about;

import com.dyz.about.dao.ShiroUserMapper;
import com.dyz.about.model.ShiroUser;
import com.dyz.about.model.UserRole;
import com.dyz.about.service.UserServiceImpl;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.realm.text.TextConfigurationRealm;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Configuration
@ControllerAdvice
@SpringBootApplication
@MapperScan("com.example.demo.dao")
public class AboutApplication {
    @Resource
    ShiroUserMapper shiroUserMapper;
    @Resource
    UserServiceImpl userServiceImpl;
    private static Logger log = LoggerFactory.getLogger(AboutApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(AboutApplication.class, args);
    }
    @ExceptionHandler(AuthorizationException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String handleException(AuthorizationException e, Model model) {

        // you could return a 404 here instead (this is how github handles 403, so the user does NOT know there is a
        // resource at that location)
        log.debug("AuthorizationException was thrown", e);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("status", HttpStatus.FORBIDDEN.value());
        map.put("message", "No message available");
        model.addAttribute("errors", map);

        return "error";
    }

    @Bean
    public Realm realm() {
//        TextConfigurationRealm realm = new TextConfigurationRealm();
//        realm.setUserDefinitions("joe.coder=password,user\n" +
//                "jill.coder=password,admin");
//
//        realm.setRoleDefinitions("admin=read,write\n" +
//                "user=read");
//        realm.setCachingEnabled(true);
        return new AuthorizingRealm() {

            @Override
            protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
                ShiroUser shiroUser = userServiceImpl.findByName(authenticationToken.getPrincipal().toString());
                if (shiroUser == null) {
                    return null;
                }
                return new SimpleAuthenticationInfo(shiroUser.getName(), shiroUser.getPassword(), "realmName");
            }

            @Override
            protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
                String name = principalCollection.getPrimaryPrincipal().toString();
                ShiroUser shiroUser = userServiceImpl.findByName(name);
                SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
                List<UserRole> userRoles = shiroUser.getUserRoleList();
                userRoles.forEach( userRole -> {
                    simpleAuthorizationInfo.addRole(userRole.getShiroRole().getRolename());
                    userRole.getShiroRole().getRolePermissionList().forEach(rolePermission -> {
                        simpleAuthorizationInfo.addStringPermission(rolePermission.getShiroPermission().getPermission());
                    });
                });
                return simpleAuthorizationInfo;
            }
        };
    }

//    @Bean
    public ShiroFilterChainDefinition shiroFilterChainDefinition() {
        DefaultShiroFilterChainDefinition chainDefinition = new DefaultShiroFilterChainDefinition();
        chainDefinition.addPathDefinition("/login.html", "authc"); // need to accept POSTs from the login form
        chainDefinition.addPathDefinition("/logout", "logout");
        return chainDefinition;
    }

//    @ModelAttribute(name = "subject")
//    public Subject subject() {
//        return SecurityUtils.getSubject();
//    }
    //权限管理，配置主要是Realm的管理认证
//    @Bean
    public SecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(realm());
        return securityManager;
    }
}
