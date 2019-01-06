package com.dyz.about.rpc;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.Socket;

public class RPCUtils {
    public static <T> T getProxyObject(Class<T> interfaceName) throws IOException, ClassNotFoundException {
        Socket socket = new Socket("localhost", 9090);
        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
//        oos.writeUTF("aaa");
//        oos.flush();
        Object o = ois.readObject();
        return (T) o;
//        return (T)Proxy.newProxyInstance(interfaceName.getClassLoader(), interfaceName.getInterfaces(), new InvocationHandler() {
//
//            @Override
//            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
//
//                return null;
//            }
//        });
    }
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        getProxyObject(Inter.class).print();
    }
 }
