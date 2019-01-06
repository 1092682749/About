package com.dyz.about.rpc;

//import io.netty.channel.unix.Socket;Socket

import com.dyz.about.io.nio.Server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.ServerSocketChannel;

public class PRCServer {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(9090);
        Socket socket = serverSocket.accept();
        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
        oos.writeObject(new Server());
        oos.flush();
        oos.close();
    }
}
