package com.dyz.about;

import com.dyz.about.model.ShiroUser;

public class Having {
    private final ShiroUser shiroUser = new ShiroUser();
    public void init() {
        shiroUser.setName("aaaa");
    }
    public static void main(String[] args) {
        new Having().init();
    }
}
