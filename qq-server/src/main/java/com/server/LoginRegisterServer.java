package com.server;

import SocketServer.ServerView;
import com.alibaba.fastjson.JSON;
import com.data.Userdao;
import com.jenkin.Const;
import com.jenkin.model.Message;;
import com.jenkin.model.Response;
import com.jenkin.model.User;
import com.jenkin.util.RabbitMqUtils;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class LoginRegisterServer {
    private static final int PORT = 5020;
    private static final int SIZE = 1024;
    private static ServerSocketChannel serverSocketChannel ;
    private static Selector selector;
    private static final ConcurrentHashMap<String,SocketChannel> socketMap = new ConcurrentHashMap<>();
    private static volatile boolean SELECT_FLAG= false;
    public LoginRegisterServer(ServerView view){
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
            System.out.println("登录注册服务器启动，端口："+PORT);
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
                SELECT_FLAG = true;
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> keyIterator = selectionKeys.iterator();
                while (keyIterator.hasNext()) {
                    SelectionKey key = keyIterator.next();
                    //移除当前的key，防止重复处理
                    keyIterator.remove();
                    doMessage(key,view);
                }
                Thread.sleep(1000);
                SELECT_FLAG = false;
                serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            } catch (Exception e) {
                try {
                    Thread.sleep(20000);
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
                System.out.println("登录注册接受连接");
            } else {
                SocketChannel socketChannel = (SocketChannel) key.channel();

                //创建ByteBuffer，并开辟一个1M的缓冲区
                ByteBuffer buffer = ByteBuffer.allocate(SIZE);
                try {
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
                        doRegister(message, view, socketChannel);
                    } else {
                        System.out.println("断开一个连接："+socketChannel.getRemoteAddress());
                        socketChannel.close();
                    }
                }catch (IOException e){
                    e.printStackTrace();
                    System.out.println("断开一个连接："+socketChannel.getRemoteAddress());
                    socketChannel.close();
                }
            }
        }
    }

    private static void doRegister(String message, ServerView view, SocketChannel socketChannel) {
        User user = JSON.parseObject(message,User.class);
        Is_User(user,view,socketChannel);
    }

    private static void doWrite(SocketChannel socketChannel, Object result) throws IOException {
            sendMessage(socketChannel,JSON.toJSONString(result));

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



    private static void Is_User(User user,ServerView view,SocketChannel socketChannel) {
        // TODO Auto-generated method stub
        Userdao u =new Userdao();
        Response response = null;
        if (user.getServertype().equals("loading")) {

            String id = user.getUser_id();
            String psw = user.getUser_pass();
            System.out.println("账号:" + id + "\n密码:" + psw);
            String sql = "select *from user_main where user_id=? and user_pass=?";
            String sql2 = "select *from user_main";
            String sql3 = "update user_main set user_state = '1' where user_id =  '"+id+"'";

            try {
                user =u.getuser(id, psw, sql);
                List<User> list = null;
                if(user!=null){
                    list = new ArrayList<User>();
                    u.changestate(sql3);
                    list = u.getlist(sql2,user.getUser_id());
                    if(view!=null) {
                        view.changetext("用户 " + user.getUser_name() + " 登录成功");
                        view.addonlineuser(user);
                    }else{
                        System.out.println("用户 " + user.getUser_name() + " 登录成功");

                    }
                    bindMessageQueue(list,user);
                    response = Response.successRes(list);
                }else{
                    response = Response.loginErrorRes();
                    System.out.println("登陆失败");
                }
               doWrite(socketChannel,response );
            }  catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
        else if (user.getServertype().equals("register")){
            if(view!=null) {
                view.changetext("一名新用户正在注册");
            }else{
                System.out.println("一名新用户正在注册");
            }
            String sql1="select user_id from user_main where user_phone=?";
            if(u.getid(sql1, user.getUser_phone())==0){
                String sql = "insert into user_main (user_name,user_pass,user_sex,user_birth,user_edu,user_blood,user_location,user_phone,user_email,user_head,user_state) values(?,?,?,?,?,?,?,?,?,?,?)";
                u.adduser(sql, user);
                sql="select user_id from user_main where user_phone=?";
                int id=u.getid(sql, user.getUser_phone());
                user.setUser_id(String.valueOf(id));
                try {
                    response = Response.successRes(user);
                    doWrite(socketChannel,response);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            else {
                response= Response.errorRes("手机号已被占用，请重试");
                try {
                    doWrite(socketChannel,response);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        }
    }

    private static void bindMessageQueue(List<User> list, User user) {
        list.forEach(item->{
            if(!user.getUser_id().equals(item.getUser_id())) {
                String toQueue = user.getUser_id() + "->" + item.getUser_id();
                String backQueue = item.getUser_id() + "->" + user.getUser_id();
                RabbitMqUtils.declareQueue(Const.PRODUCT_PRIVATE_CHANNEL, toQueue);
                RabbitMqUtils.declareQueue(Const.PRODUCT_PRIVATE_CHANNEL, backQueue);
                RabbitMqUtils.queueBindExchange(Const.PRODUCT_PRIVATE_CHANNEL, toQueue, Const.MESSAGE_PRIVATE_EXCHANGE, toQueue);
                RabbitMqUtils.queueBindExchange(Const.PRODUCT_PRIVATE_CHANNEL, backQueue, Const.MESSAGE_PRIVATE_EXCHANGE, backQueue);
            }
            String groupQueue = item.getUser_id() + "->" + item.getUser_id();
            RabbitMqUtils.declareQueue(Const.PRODUCT_TOPIC_CHANNEL, groupQueue);
            RabbitMqUtils.queueBindExchange(Const.PRODUCT_TOPIC_CHANNEL, groupQueue, Const.MESSAGE_TOPIC_EXCHANGE, groupQueue);
        });
        String groupQueue = user.getUser_id() + "->" + user.getUser_id();
        RabbitMqUtils.declareQueue(Const.PRODUCT_TOPIC_CHANNEL, groupQueue);
        RabbitMqUtils.queueBindExchange(Const.PRODUCT_TOPIC_CHANNEL, groupQueue, Const.MESSAGE_TOPIC_EXCHANGE, groupQueue);

    }
}
