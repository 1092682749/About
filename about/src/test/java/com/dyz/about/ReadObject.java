package com.dyz.about;

import com.dyz.about.model.ShiroUser;
import sun.jvm.hotspot.utilities.ObjectReader;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;

public class ReadObject {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        ObjectInputStream objectInput = new ObjectInputStream(new FileInputStream("/Users/qingyun/Desktop/shiro/o.out"));
        ShiroUser shiroUser = (ShiroUser) objectInput.readObject();
        System.out.println(shiroUser.getName());
    }
}
