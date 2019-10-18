package com.jenkin.demo01.demo_nio.server;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class Server {
    private static final int PORT = 22347;
    private static final int SIZE = 1024;
    private static ServerSocketChannel serverSocketChannel ;
    private static Selector selector;
    public static void main(String[] args) {
        startServer();
    }

    private static void startServer() {

        try {
            ByteBuffer buffer= ByteBuffer.allocate(5);
            selector=Selector.open();
            serverSocketChannel = ServerSocketChannel.open();
            //设置为非阻塞
            serverSocketChannel.configureBlocking(false);
            //绑定端口
            serverSocketChannel.socket().bind(new InetSocketAddress(PORT),SIZE);
            //注册选择器，并且设置选择器模式为接受连接
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("服务器启动，端口："+PORT);
            doAccept(buffer,selector,serverSocketChannel);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void doAccept(ByteBuffer buffer, Selector selector, ServerSocketChannel serverSocketChannel) {

        while (true) {
            try {
                //阻塞选择器，等待选择事件
                selector.select();
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> keyIterator = selectionKeys.iterator();
                while (keyIterator.hasNext()) {
                    SelectionKey key = keyIterator.next();
                    //移除当前的key，防止重复处理
                    keyIterator.remove();
                    doMessage(key);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private static void doMessage(SelectionKey key) throws IOException {
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
                SocketChannel socketChannel = (SocketChannel) key.channel();
                //创建ByteBuffer，并开辟一个1M的缓冲区
                ByteBuffer buffer = ByteBuffer.allocate(SIZE);
                //读取请求码流，返回读取到的字节数
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
                    //处理数据
                    String result = "return:"+ message;
                    doWrite(socketChannel,result);
                }
            }
        }
    }

    private static void doWrite(SocketChannel socketChannel, String result) throws IOException {
        //将消息编码为字节数组
        byte[] bytes = result.getBytes();
        //根据数组容量创建ByteBuffer
        ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
        //将字节数组复制到缓冲区
        writeBuffer.put(bytes);
        //flip操作
        writeBuffer.flip();
        //发送缓冲区的字节数组
        socketChannel.write(writeBuffer);
        System.out.println("应答");
    }
}
