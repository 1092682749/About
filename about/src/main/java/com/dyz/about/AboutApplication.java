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
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.realm.text.TextConfigurationRealm;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
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
                return new SimpleAuthenticationInfo(shiroUser.getName(), shiroUser.getPassword(), getName());
            }

            @Override
            protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
                String name = principalCollection.getPrimaryPrincipal().toString();
                ShiroUser shiroUser = userServiceImpl.findByName(name);
                shiroUser = userServiceImpl.findByID(shiroUser.getId());
                SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
                List<UserRole> userRoles = shiroUser.getUserRoleList();
                userRoles.forEach(userRole -> {
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
        chainDefinition.addPathDefinition("/index", "anon");
        chainDefinition.addPathDefinition("/auth", "anon");
        chainDefinition.addPathDefinition("/error", "anon");
        chainDefinition.addPathDefinition("/login", "anon");
        chainDefinition.addPathDefinition("/**", "authc");
        return chainDefinition;
    }

    //    @ModelAttribute(name = "subject")
//    public Subject subject() {
//        return SecurityUtils.getSubject();
//    }
    //权限管理，配置主要是Realm的管理认证
    @Bean
    public SecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(realm());

        securityManager.setCacheManager(cacheManager());

        securityManager.setSessionManager(sessionManager());
        //注入记住我管理器;
        securityManager.setRememberMeManager(rememberMeManager());
//        securityManager.setSessionManager(sessionManager());
        return securityManager;
    }

    @Bean
    public SessionManager sessionManager() {
        SessionManager sessionManager = new DefaultWebSecurityManager();
        return sessionManager;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        Map<String, String> map = new HashMap<String, String>();
        //登录
        shiroFilterFactoryBean.setLoginUrl("/login");
        //首页
        shiroFilterFactoryBean.setSuccessUrl("/hello");
        //错误页面，认证不通过跳转
        shiroFilterFactoryBean.setUnauthorizedUrl("/errorr");
        //登出
        map.put("/login", "anon");
        map.put("/index", "anon");
        map.put("/logout", "logout");
        map.put("/auth", "anon");
//        //对所有url认证
        map.put("/**", "authc");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(map);
        return shiroFilterFactoryBean;
    }

    public CookieRememberMeManager rememberMeManager() {
        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
        cookieRememberMeManager.setCookie(rememberMeCookie());
        cookieRememberMeManager.setCipherKey(Base64.decode("3AvVhmFLUs0KTA3Kprsdag=="));
        return cookieRememberMeManager;
    }

    public SimpleCookie rememberMeCookie() {
        SimpleCookie simpleCookie = new SimpleCookie("rememberMe");
        //<!-- 记住我cookie生效时间30天 ,单位秒;-->
        simpleCookie.setMaxAge(2592000);
        return simpleCookie;
    }

    @Bean
    public CacheManager cacheManager() {
        return new EhCacheManager();
    }


    @Bean
    public SessionDAO sessionDAO() {
        return new EnterpriseCacheSessionDAO();
    }

    @Bean
    public SessionManager sessionManager(SessionDAO sessionDAO) {
        DefaultWebSessionManager manager = new DefaultWebSessionManager();
        manager.setSessionDAO(sessionDAO);
        manager.setGlobalSessionTimeout(3600000);
        manager.setSessionValidationInterval(3600000);
        return manager;
    }
}
