package com.dyz.about;

import com.dyz.about.model.ShiroUser;
import org.junit.Test;

import java.util.concurrent.*;

public class Having {
    private final ShiroUser shiroUser = new ShiroUser();
    public void init() {
        shiroUser.setName("aaaa");
    }
    public static void main(String[] args) {
        Having having = new Having();
        having.init();
        System.out.println(having.shiroUser.getName());
    }
    @Test
    public void test() {
        ScheduledExecutorService service = Executors.newScheduledThreadPool(2);
        service.schedule(new Runnable() {
            @Override
            public void run() {
                System.out.println("嘀嗒");
            }
        }, 20, TimeUnit.SECONDS);

        service.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                System.out.println("w");
            }
        }, 2, 10, TimeUnit.SECONDS);
        while (!service.isShutdown()) {

        }
    }
}
