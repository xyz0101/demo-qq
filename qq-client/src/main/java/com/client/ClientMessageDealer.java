package com.client;

import clientview.ChatView;
import com.alibaba.fastjson.JSON;
import com.jenkin.model.Message;;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClientMessageDealer implements Runnable {
    private final String URL = "127.0.0.1";
    private SocketChannel socketChannel;
    private final int PORT = 8888;
    private Selector selector;
    private ChatView view;
    private ExecutorService executor =Executors.newFixedThreadPool(20);
    private volatile boolean STOP = false;
    private volatile boolean connectStatus = false;
    public ClientMessageDealer(ChatView view) {
        try {

            STOP=false;
            selector = Selector.open();
            this.view = view;
            socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(false);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public  void stop(){
        try {
            STOP=true;
            socketChannel.register(selector, SelectionKey.OP_CONNECT);
            selector.close();
            socketChannel.close();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void run() {
        try {
            doConnect();
            System.out.println("客户端启动");
            while (!STOP) {
                System.out.println("客户端事件选择");
                selector.select();
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> keyIterator = selectionKeys.iterator();
                while (keyIterator.hasNext()) {
                    SelectionKey next = keyIterator.next();
                    doMessage(next);
                    keyIterator.remove();
                }
            }
            System.out.println("客户端线程退出");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void doMessage(SelectionKey next) throws IOException {
        if (next.isValid()) {
            SocketChannel socketChannel = (SocketChannel) next.channel();
            if (next.isConnectable()) {
                if (socketChannel.finishConnect()) {
                    System.out.println("连接成功");

                    socketChannel.register(selector, SelectionKey.OP_READ);
                    connectStatus=true;
                } else {
                    System.out.println("连接失败");
                    System.exit(-1);
                }
//                this.socketChannel=socketChannel;
            }
            if (next.isReadable()) {
                //创建ByteBuffer，并开辟一个1M的缓冲区
                ByteBuffer buffer = ByteBuffer.allocate(1024);
                //读取请求码流，返回读取到的字节数
                try {
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
                        readMessage(result);
                    } else {
                        next.cancel();
                        socketChannel.close();
                    }
                }catch (IOException e){
                    connectStatus = false;
                    if(!connectStatus){
                        throw  e;
                    }
                    System.err.println(e.getMessage());

                }

            }
        }
    }

    private void readMessage(String result) {
        executor.submit(()->{
            try {
                    System.out.println("NIO收到消息---->"+ result);
                    Message message = JSON.parseObject(result,Message.class);
                    view.showmessage(message);

            }catch (Exception e){
                e.printStackTrace();
            }

        });
    }

    public  void doWrite(String result) throws IOException {
        //将消息编码为字节数组
        byte[] bytes = result.getBytes();
        //根据数组容量创建ByteBuffer
        ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
        //将字节数组复制到缓冲区
        writeBuffer.put(bytes);
        //flip操作
        writeBuffer.flip();
        boolean sendFlag = false;
        if (socketChannel!=null&&socketChannel.isConnected()) {
            //发送缓冲区的字节数组
            socketChannel.write(writeBuffer);
            System.out.println("发送成功");
            sendFlag=true;
        }else {
            System.err.println(socketChannel==null?"socketChannel为空":"socketChannel未连接");
            retrySend(writeBuffer, sendFlag, socketChannel);
        }

    }

    private void retrySend(ByteBuffer writeBuffer, boolean sendFlag, SocketChannel socketChannel) throws IOException {
        long b = System.currentTimeMillis();
        while(!sendFlag
                &&(System.currentTimeMillis()-b)<3000
        ){
            if (socketChannel !=null&& socketChannel.isConnected()) {
                //发送缓冲区的字节数组
                try {
                    socketChannel.write(writeBuffer);
                    sendFlag=true;
                    System.out.println("重试");
                    Thread.sleep(300);
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            }

        }
        if(!sendFlag&& socketChannel !=null&& socketChannel.isConnected()){
            //发送缓冲区的字节数组
            System.out.println("重试成功");
            socketChannel.write(writeBuffer);
            sendFlag=true;
        }
    }

    private void doConnect() throws IOException {
        /*如果此通道处于非阻塞模式，
        则调用此方法将启动非阻塞连接操作。
        如果立即建立连接，就像本地连接可能发生的那样，则此方法返回true。
        否则，此方法返回false，
        稍后必须通过调用finishConnect方法完成连接操作。*/
        if (!socketChannel.connect(new InetSocketAddress(URL, PORT))) {
            System.out.println("连接还未完成！");
            //连接还未完成，所以注册连接就绪事件，向selector表示关注这个事件
            socketChannel.register(selector, SelectionKey.OP_CONNECT);
        }else {
            System.out.println("连接完成！");
            socketChannel.finishConnect();
        }
    }
}