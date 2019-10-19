package com.server;

import SocketServer.ServerView;
import com.alibaba.fastjson.JSON;
import com.jenkin.Const;
import com.jenkin.model.Message;
import com.jenkin.util.RabbitMqUtils;
import com.rabbitmq.client.Channel;;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class SingleChatServer {
    private static final int PORT = 8888;
    private static final int SIZE = 1024;
    private static ServerSocketChannel serverSocketChannel ;
    private static Selector selector;
    private static final ConcurrentHashMap<String,SocketChannel> socketMap = new ConcurrentHashMap<>();
    private static volatile boolean SELECT_FLAG= false;

    public SingleChatServer(ServerView view){
        startServer(view);
    }

    private static void startServer(ServerView view) {

        try {
            selector=Selector.open();
            serverSocketChannel = ServerSocketChannel.open();
            //设置为非阻塞
            serverSocketChannel.configureBlocking(false);
            //绑定端口
            serverSocketChannel.socket().bind(new InetSocketAddress(PORT),SIZE);
            //注册选择器，并且设置选择器模式为接受连接
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("服务器启动，端口："+PORT);
            doAccept(view);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void doAccept(ServerView view) {

        while (!SELECT_FLAG) {
            try {
                //阻塞选择器，等待选择事件
                selector.select();
                System.out.println("服务器事件选择");
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> keyIterator = selectionKeys.iterator();
                while (keyIterator.hasNext()) {
                    SelectionKey key = keyIterator.next();
                    //移除当前的key，防止重复处理
                    keyIterator.remove();
                    doMessage(key,view);
                }
                Thread.sleep(1000);


            } catch (Exception e) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                e.printStackTrace();
            }
        }
    }
    private static void doMessage(SelectionKey key,ServerView view) throws IOException {
        //判断当前选择的key的事件是否为接受连接事件
        if (key.isValid()) {
            if (key.isAcceptable()) {
                serverSocketChannel = (ServerSocketChannel) key.channel();
                //获取接受的连接的channel
                SocketChannel socketChannel = serverSocketChannel.accept();
                socketChannel.configureBlocking(false);
                socketChannel.register(selector, SelectionKey.OP_READ);
                System.out.println("接受连接");
            } else {
                try {
                    SocketChannel socketChannel = (SocketChannel) key.channel();
                    //创建ByteBuffer，并开辟一个1M的缓冲区
                    ByteBuffer buffer = ByteBuffer.allocate(SIZE);
                    //读取请求码流，返回读取到的字节数
                    try {
                        int readBytes = socketChannel.read(buffer);
                    //读取到字节，对字节进行编解码
                    if (readBytes > 0) {
                        //将缓冲区当前的limit设置为position=0，
                        // 用于后续对缓冲区的读取操作
                        buffer.flip();
                        //根据缓冲区可读字节数创建字节数组
                        byte[] bytes = new byte[buffer.remaining()];
                        //将缓冲区可读字节数组复制到新建的数组中
                        buffer.get(bytes);
                        String message = new String(bytes, "UTF-8");
                        System.out.println("服务器收到消息：" + message);

                        doWrite(socketChannel,message,view);
                    }else{
                        System.out.println("断开一个连接："+socketChannel.getRemoteAddress());
                        socketChannel.close();
                    }
                    }catch (IOException e){
                        e.printStackTrace();
                        socketChannel.close();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    System.out.println("重新接受连接");
                    serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
                }

            }
        }
    }

    private static void doWrite(SocketChannel socketChannel, String result,ServerView view) throws IOException {
        Channel mqChannel = Const.PRODUCT_PRIVATE_CHANNEL;
        Channel topicChannel = Const.PRODUCT_TOPIC_CHANNEL;

        if(result!=null) {
            Message message = JSON.parseObject(result, Message.class);
            socketMap.put(message.getUser_id(), socketChannel);
            if(message.getUser_id()!=null&&message.getSend_to()!=null) {
                String queue = message.getUser_id()+"->"+message.getSend_to();

                if(message.getChat_model().equals("group")){
                    socketMap.keySet().forEach(item->{
                        if(!item.equals(message.getUser_id())){
                            SocketChannel channel = socketMap.get(item);
                            RabbitMqUtils.sendMessage(topicChannel,result,Const.MESSAGE_TOPIC_EXCHANGE,queue);

                            if(channel!=null&&channel.isConnected()) {
                                try {

                                    sendMessage(channel,result);

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });

                } else{
                    if("0".equals(message.getMsg_model())){
                        if(view!=null) {
                            view.changetext("用户 " + message.getUser_name() + "与" + message.getSend_to() + " 私聊");
                        }else{
                            System.out.println("用户 " + message.getUser_name() + "与" + message.getSend_to() + " 私聊");

                        }
                        SocketChannel channel = socketMap.get(message.getSend_to());
                        RabbitMqUtils.sendMessage(mqChannel,result,Const.MESSAGE_PRIVATE_EXCHANGE,queue);

//                        System.out.println(channel.getRemoteAddress());
                        if(channel!=null&&channel.isConnected()) {
                           sendMessage(channel,result);

                        }
                    }
//                    if(!message.getMsg_model().equals("1")){
//                        System.out.println("server recived message:"+result);
//                       //  sendMessage(socketChannel,result);
//                    }
                }
            }
        }
    }

    private static void sendMessage(SocketChannel channel, String result) throws IOException {
        //将消息编码为字节数组
        byte[] bytes = result.getBytes();
        //根据数组容量创建ByteBuffer
        ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
        //将字节数组复制到缓冲区
        writeBuffer.put(bytes);
        //flip操作
        writeBuffer.flip();
        //发送缓冲区的字节数组
        channel.write(writeBuffer);
        System.out.println("应答");
    }
}
