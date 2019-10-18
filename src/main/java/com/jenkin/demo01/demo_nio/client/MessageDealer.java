package com.jenkin.demo01.demo_nio.client;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class MessageDealer implements Runnable {
    private  static SocketChannel socketChannel;
    private static final int PORT = 22347;
    private Selector selector;
    public MessageDealer( ){
        try {
            selector = Selector.open();
            socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(false);
        }catch (Exception e){
            e.printStackTrace();
        }


    }
    @Override
    public void run() {
        try {
            doConnect();
            System.out.println("客户端启动");
            while (true) {
                selector.select();
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> keyIterator = selectionKeys.iterator();
                while (keyIterator.hasNext()) {
                    SelectionKey next = keyIterator.next();
                    doMessage(next);
                    keyIterator.remove();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void doMessage(SelectionKey next) throws IOException {
        if (next.isValid()){
            SocketChannel socketChannel = (SocketChannel) next.channel();
            if(next.isConnectable()){
                System.out.println("连接成功");
                if (socketChannel.finishConnect()) {
                    socketChannel.register(selector,SelectionKey.OP_READ);
                }else{
                    System.out.println("连接失败");
                    System.exit(-1);
                }

            }
            if (next.isReadable()){
                //创建ByteBuffer，并开辟一个1M的缓冲区
                ByteBuffer buffer = ByteBuffer.allocate(1024);
                //读取请求码流，返回读取到的字节数
                int readBytes = socketChannel.read(buffer);
                //读取到字节，对字节进行编解码
                if(readBytes>0) {
                    //将缓冲区当前的limit设置为position,position=0，
                    // 用于后续对缓冲区的读取操作
                    buffer.flip();
                    //根据缓冲区可读字节数创建字节数组
                    byte[] bytes = new byte[buffer.remaining()];
                    //将缓冲区可读字节数组复制到新建的数组中
                    buffer.get(bytes);
                    String result = new String(bytes, "UTF-8");
                    System.out.println("accept message:" + result);
                }else{
                    next.cancel();
                    socketChannel.close();
                }
            }
        }
    }
    public static void doWrite(String result) throws IOException {
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
    }
    private void doConnect() throws IOException {
        /*如果此通道处于非阻塞模式，
        则调用此方法将启动非阻塞连接操作。
        如果立即建立连接，就像本地连接可能发生的那样，则此方法返回true。
        否则，此方法返回false，
        稍后必须通过调用finishConnect方法完成连接操作。*/
        if(socketChannel.connect(new InetSocketAddress("127.0.0.1",PORT))){}
        else{
            //连接还未完成，所以注册连接就绪事件，向selector表示关注这个事件
            socketChannel.register(selector,SelectionKey.OP_CONNECT);
        }
    }
}
