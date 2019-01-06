package com.dyz.about.rpc;

public interface Inter {
    default public void print() {
        System.out.println("success!");
    }
}
