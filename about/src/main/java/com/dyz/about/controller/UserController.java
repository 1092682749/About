package com.dyz.about.controller;

import com.dyz.about.dao.ShiroUserMapper;
import com.dyz.about.model.ShiroUser;
import com.dyz.about.service.UserServiceImpl;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Controller
public class UserController {
    @Autowired
    UserServiceImpl userService;
    @RequestMapping("/login")
    public String login() {
//        Subject subject = SecurityUtils.getSubject();
//        System.out.println(subject.isAuthenticated());
//        UsernamePasswordToken token = new UsernamePasswordToken("123","123");
//        token.setRememberMe(true);
//        subject.login(token);
        return "login";
    }

    @RequestMapping("/index")
    public String index() {
        return "index";
    }

    @RequestMapping("/auth")
    public ModelAndView auth(ShiroUser user) {
        userService.findByID(1);
        Subject subject = SecurityUtils.getSubject();System.out.println(subject.isAuthenticated());
        UsernamePasswordToken token = new UsernamePasswordToken(user.getName(), user.getPassword().toString());
        token.setRememberMe(true);
        Map<String, Object> o = new HashMap<>();
        try {
            subject.login(token);
            o.put("msg", "认证成功");
            System.out.println(subject.isAuthenticated());
            //if no exception, that's it, we're done!
        } catch (UnknownAccountException uae) {
            o.put("msg", "没有该账户");
            //username wasn't in the system, show them an error message?
        } catch (IncorrectCredentialsException ice) {
            //password didn't match, try again?
            o.put("msg", "密码错误");
        } catch (LockedAccountException lae) {
            o.put("msg", "账户被锁");
            //account for that username is locked - can't login.  Show them a message?
        } catch (AuthenticationException ae) {
            //unexpected condition - error?g
            o.put("msg", "未知错误");
        } finally {
        }
        ModelAndView mav = new ModelAndView("hello");
        mav.addObject("map", o);
        return mav;
//        System.out.println(subject.isAuthenticated());
//        return "login";
    }
    @RequiresPermissions("a")
    @SuppressWarnings("Duplicates")
    @RequestMapping("/")
    public String home(HttpServletRequest request, Model model) {

        String name = "World";

        Subject subject = SecurityUtils.getSubject();

        PrincipalCollection principalCollection = subject.getPrincipals();

        if (principalCollection != null && !principalCollection.isEmpty()) {
            Collection<Map> principalMaps = subject.getPrincipals().byType(Map.class);
            if (CollectionUtils.isEmpty(principalMaps)) {
                name = subject.getPrincipal().toString();
            } else {
                name = (String) principalMaps.iterator().next().get("username");
            }
        }

        model.addAttribute("name", name);

        return "hello";
    }

    @RequiresRoles("admin")
    @RequestMapping("/account-info")
    public String home(Model model) {

        String name = "World";

        Subject subject = SecurityUtils.getSubject();

        PrincipalCollection principalCollection = subject.getPrincipals();

        if (principalCollection != null && !principalCollection.isEmpty()) {
            name = principalCollection.getPrimaryPrincipal().toString();
        }

        model.addAttribute("name", name);

        return "account-info";
    }
    @RequestMapping("/errorr")
    public String errorr() {
        return "error";
    }
}
