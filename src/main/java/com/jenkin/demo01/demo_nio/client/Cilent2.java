package com.jenkin.demo01.demo_nio.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

public class Cilent2 {
    private static final int PORT = 22347;

    public static void main(String[] args) {
        startClient();
    }

    private static void startClient() {
        SocketChannel socketChannel=null;
        try {
            socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1",PORT));
            socketChannel.configureBlocking(false);
            ByteBuffer buffer = ByteBuffer.allocate(102400000);
            int i=0;
            System.out.println("客户端启动");
            Scanner input = new Scanner(System.in);
            String message = null;
            while (!"exit".equals((message=input.nextLine())) ){
                buffer.clear();
                buffer.put(message.getBytes());
                buffer.flip();
                socketChannel.write(buffer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                socketChannel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
