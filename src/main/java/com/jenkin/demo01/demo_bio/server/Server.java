package com.jenkin.demo01.demo_bio.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class Server {
    private static final int PORT = 22345;
    private static final ConcurrentHashMap<String,Socket> socketMap = new ConcurrentHashMap<>();
    private  static  ExecutorService executorService = Executors.newFixedThreadPool(50);
    public static void main(String[] args) {
        startServer();
    }

    private static void startServer() {
        ServerSocket serverSocket =null;
        try{
            serverSocket = new ServerSocket(PORT);
            System.out.println("socket server  start!");

            while (true){
                Socket accept = serverSocket.accept();
                executorService.submit(()->{
                    BufferedReader reader =null;
                    PrintWriter writer =null;
                    try {
                        System.out.println("deal socket");
                        reader =
                                new BufferedReader(new InputStreamReader(accept.getInputStream()));
                        writer =
                                new PrintWriter(accept.getOutputStream());
                        String auth = reader.readLine();
                        checkAuth(writer,auth);
                        socketMap.put(auth,accept);
                        String line = null;
                        while ((line = reader.readLine())!=null) {

                            System.out.println("server recived message:"+line);
                            writer.println("server return message :"+"SR-->"+line);
                            writer.flush();
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }finally {
                        if(reader != null){
                            try {
                                reader.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        reader = null;
                        if(writer != null){
                            writer.close();
                        }
                        writer = null;
                    }

                });
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            executorService.shutdown();
            if(serverSocket != null){
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            serverSocket = null;

        }
    }

    private static void checkAuth(PrintWriter writer, String auth) {
        switch (auth){
            case "Client":
                writer.println("Client 上线了！");
                break;
            case "Client1":
                writer.println("Client1 上线了！");
                break;
            case "Client2":
                writer.println("Client2 上线了！");
                break;
                default:
                    break;
        }
        writer.flush();
    }

}
