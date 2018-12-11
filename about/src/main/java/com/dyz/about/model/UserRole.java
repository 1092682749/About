package com.dyz.about.model;

public class UserRole {
    private Integer id;

    private Integer uid;

    private Integer rid;
    private ShiroRole shiroRole;

    public ShiroRole getShiroRole() {
        return shiroRole;
    }

    public void setShiroRole(ShiroRole shiroRole) {
        this.shiroRole = shiroRole;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public Integer getRid() {
        return rid;
    }

    public void setRid(Integer rid) {
        this.rid = rid;
    }
}