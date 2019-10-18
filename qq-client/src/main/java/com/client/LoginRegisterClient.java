package com.client;

import clientview.ChatView;
import com.alibaba.fastjson.JSON;
import com.jenkin.model.Response;
import com.jenkin.model.User;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LoginRegisterClient {
    private static final String URL = "127.0.0.1";
    private static SocketChannel socketChannel;
    private static final int PORT = 5020;
    private static Selector selector;
    private ChatView view;
    private static  ExecutorService executor;

    public LoginRegisterClient(ChatView chat) {
        try {
            executor = Executors.newSingleThreadExecutor();
            selector = Selector.open();
            this.view = chat;
            socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        startClient(chat);
    }
    public static void stop(){
        try {
            socketChannel.close();
            selector.close();
            executor.shutdown();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void startClient(ChatView chat) {
        try {
            doConnect();
            System.out.println("登录注册客户端启动");
           // while (true) {
                System.out.println("登录注册客户端事件选择");
                selector.select();
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> keyIterator = selectionKeys.iterator();
                while (keyIterator.hasNext()) {
                    SelectionKey next = keyIterator.next();
                    doMessage(next);
                    keyIterator.remove();
                }
           // }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            executor.shutdown();
        }
    }

    private void doConnect() throws IOException {
        /*如果此通道处于非阻塞模式，
        则调用此方法将启动非阻塞连接操作。
        如果立即建立连接，就像本地连接可能发生的那样，则此方法返回true。
        否则，此方法返回false，
        稍后必须通过调用finishConnect方法完成连接操作。*/
        if (socketChannel.connect(new InetSocketAddress(URL, PORT))) {
        } else {
            //连接还未完成，所以注册连接就绪事件，向selector表示关注这个事件
            socketChannel.register(selector, SelectionKey.OP_CONNECT);
        }
    }


    private void doMessage(SelectionKey next) throws IOException {
        if (next.isValid()) {
            SocketChannel socketChannel = (SocketChannel) next.channel();
            if (next.isConnectable()) {
                System.out.println("登录注册连接成功");
                if (socketChannel.finishConnect()) {
                    socketChannel.register(selector, SelectionKey.OP_WRITE);
                } else {
                    System.out.println("连接失败");
                    System.exit(-1);
                }

            }

        }
    }
    private String readMessage() throws IOException {
        socketChannel.register(selector, SelectionKey.OP_READ);
        System.out.println("读取返回值");
        selector.select();
        Set<SelectionKey> selectionKeys = selector.selectedKeys();
        Iterator<SelectionKey> keyIterator = selectionKeys.iterator();
        StringBuilder res = new StringBuilder("");
        while (keyIterator.hasNext()) {
            SelectionKey next = keyIterator.next();
            keyIterator.remove();
            if (next.isReadable()) {
                //创建ByteBuffer，并开辟一个1M的缓冲区
                ByteBuffer buffer = ByteBuffer.allocate(1024);
                //读取请求码流，返回读取到的字节数
                int readBytes = socketChannel.read(buffer);
                //读取到字节，对字节进行编解码
                if (readBytes > 0) {
                    //将缓冲区当前的limit设置为position,position=0，
                    // 用于后续对缓冲区的读取操作
                    buffer.flip();
                    //根据缓冲区可读字节数创建字节数组
                    byte[] bytes = new byte[buffer.remaining()];
                    //将缓冲区可读字节数组复制到新建的数组中
                    buffer.get(bytes);
                    String result = new String(bytes, "UTF-8");
                    System.out.println("登录注册返回==》" + result);
                    res.append(result);

                } else {
                    next.cancel();
                    socketChannel.close();
                }
            }
        }
        socketChannel.register(selector, SelectionKey.OP_WRITE);

        return res.toString();
    }

    public User register(User user){
        try {
            socketChannel.register(selector, SelectionKey.OP_WRITE);
                System.out.println("注册事件选择");
            doLoginOrRegister(user);
            socketChannel.register(selector, SelectionKey.OP_READ);
            String message = this.readMessage();
            Object data = JSON.parseObject(message, Response.class).getData();
            return  JSON.parseObject(JSON.toJSONString(data),User.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public List login(User user){
        try {
            socketChannel.register(selector, SelectionKey.OP_WRITE);
            System.out.println("登录事件选择");
            doLoginOrRegister(user);
            String message = this.readMessage();
            Response response  = JSON.parseObject(message, Response.class);
            if (!"200".equals(response.getResCode())){
                throw new RuntimeException(response.getResMsg());
            }
            List<User> res = new ArrayList<>();
            List list = JSON.parseObject(JSON.toJSONString(response.getData()),List.class);
            if (list!=null) {
                list.forEach(item->{
                    User user1 = JSON.parseObject(JSON.toJSONString(item), User.class);
                    res.add(user1);
                });
                LoginRegisterClient.stop();
            }

            return res;


        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void doLoginOrRegister(User user) throws IOException {
        selector.select();
        Set<SelectionKey> selectionKeys = selector.selectedKeys();
        Iterator<SelectionKey> keyIterator = selectionKeys.iterator();
        while (keyIterator.hasNext()) {
            SelectionKey next = keyIterator.next();
            keyIterator.remove();
            if(next.isValid()&&next.isWritable()) {
                socketChannel = (SocketChannel) next.channel();
                doWrite(JSON.toJSONString(user));
            }
        }
    }

    private void doWrite(String result) throws IOException {
            //将消息编码为字节数组
            byte[] bytes = result.getBytes();
            //根据数组容量创建ByteBuffer
            ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
            //将字节数组复制到缓冲区
            writeBuffer.put(bytes);
            //flip操作
            writeBuffer.flip();
            boolean sendFlag = false;
            if (socketChannel != null && socketChannel.isConnected()) {
                //发送缓冲区的字节数组
                socketChannel.write(writeBuffer);
                System.out.println("发送成功");
                sendFlag = true;
            } else {
                System.err.println("socketChannel为空");
                long b = System.currentTimeMillis();
                while (!sendFlag
                        && (System.currentTimeMillis() - b) < 3000
                ) {
                    if (socketChannel != null && socketChannel.isConnected()) {
                        //发送缓冲区的字节数组
                        try {
                            socketChannel.write(writeBuffer);
                            sendFlag = true;
                            System.out.println("重试");
                            Thread.sleep(300);
                        } catch (Exception e) {
//                    e.printStackTrace();
                        }
                    }

                }
                if (!sendFlag && socketChannel != null && socketChannel.isConnected()) {
                    //发送缓冲区的字节数组
                    System.out.println("重试成功");
                    socketChannel.write(writeBuffer);
                    sendFlag = true;
                }
            }
        }

}
