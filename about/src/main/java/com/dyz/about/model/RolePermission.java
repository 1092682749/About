package com.dyz.about.model;

public class RolePermission {
    private Integer pid;

    private Integer id;

    private Integer rid;
    private ShiroPermission shiroPermission;

    public ShiroPermission getShiroPermission() {
        return shiroPermission;
    }

    public void setShiroPermission(ShiroPermission shiroPermission) {
        this.shiroPermission = shiroPermission;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRid() {
        return rid;
    }

    public void setRid(Integer rid) {
        this.rid = rid;
    }
}