package com.data;

import com.server.LoginRegisterServer;
import com.server.SingleChatServer;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    private static final ExecutorService executor = Executors.newFixedThreadPool(2);
    public static void main(String[] args) {

            executor.submit(()->{
                new LoginRegisterServer(null);
            });
            executor.submit(()->{
                new SingleChatServer(null);
            });



    }
}
