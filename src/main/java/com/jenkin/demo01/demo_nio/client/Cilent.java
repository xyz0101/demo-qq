package com.jenkin.demo01.demo_nio.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Cilent {
    private static final ExecutorService executor = Executors.newSingleThreadExecutor();

    public static void main(String[] args) {
        startClient();
    }

    private static void startClient() {
        try {

            Scanner input = new Scanner(System.in);
            String message = null;
            executor.submit(new MessageDealer());
            while (!"exit".equals((message=input.nextLine())) ){
                MessageDealer.doWrite(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            executor.shutdown();
        }

    }

}
