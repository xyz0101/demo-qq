package com.client;

import clientview.ChatView;
import com.alibaba.fastjson.JSON;
import com.jenkin.demo01.demo_nio.client.MessageDealer;
import com.jenkin.model.Message;;

import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SingleChatClient {
    private static final String URL = "127.0.0.1";
    private static final int PORT = 8888;
    private static  PrintWriter writer =null;
    private    ExecutorService executor = Executors.newSingleThreadExecutor();

    private  ClientMessageDealer clientMessageDealer;
    public  SingleChatClient(ChatView chat){
        startClient(chat);
    }

    private  void startClient(ChatView chat) {
        try{
            ClientMessageDealer clientMessageDealer = new ClientMessageDealer(chat);
            setClientMessageDealer(clientMessageDealer);
            System.out.println("提交客户端线程");
            executor.submit(clientMessageDealer);
        }catch (Exception e){
            e.printStackTrace();
        }

    }


    public ClientMessageDealer getClientMessageDealer() {
        return clientMessageDealer;
    }

    public void setClientMessageDealer(ClientMessageDealer clientMessageDealer) {
        this.clientMessageDealer = clientMessageDealer;
    }
}
