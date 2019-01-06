package com.dyz.about;

import com.dyz.about.model.ShiroUser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class Break extends Thread {
    public static Boolean f = true;
    public static volatile int num = 0;
    ShiroUser user;

    Break(ShiroUser user) {
        this.user = user;
    }

    @Override
//    @SuppressWarnings("")
    public void run() {


            int a = 0;

//        try {
//            Thread.sleep(5000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//            while (num == 0) {
//
//            }
            num++;
            while (f){

            }
//            new ReceiveThread(this.user.getName()).start();
//            System.out.println(num);

    }

    public static void main(String[] args) throws InterruptedException {
        ShiroUser user = new ShiroUser();
        user.setName("aaaa");
        new Break(user).start();
        Thread.sleep(2000);
        System.out.println("我已经把bbbb设置进去了");
        user.setName("bbbb");
        num++;
        f = false;
    }
}
class ReceiveThread extends Thread {
    String name;
    ReceiveThread(String name) {
        this.name = name;
    }
    @Override
    public void run() {
        try {
            File file = new File("/Users/qingyun/Desktop/shiro/o.out");
            if (!file.exists()) {
                file.createNewFile();
            }
            ObjectOutputStream obs = new ObjectOutputStream(new FileOutputStream(file));
            ShiroUser user = new ShiroUser();
            user.setName(name);
            obs.writeObject(user);
        }catch (Exception e) {

        }
    }
}
