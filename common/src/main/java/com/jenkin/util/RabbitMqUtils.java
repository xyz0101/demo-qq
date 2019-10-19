package com.jenkin.util;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class RabbitMqUtils {
    public static Channel getChannel(){
        //设置连接属性。未设置时使用默认值:使用默认账号guest连接到localhost到默认vhost "/"
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("119.29.175.198");
        connectionFactory.setPort(5672);
        connectionFactory.setVirtualHost("jenkin");
        connectionFactory.setUsername("jenkin");
        connectionFactory.setPassword("123456");
        //生成Connection & Channel
        Connection connection = null;
        try {
            connection = connectionFactory.newConnection();
            return connection.createChannel();
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("no Channel!");
    }


    public static void declareQueue(Channel channel,String queueName){
        try {
            channel.queueDeclare(queueName,true,false,false,null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void declareExchange(Channel channel, String exchange, BuiltinExchangeType type){
        try {
            channel.exchangeDeclare(exchange, type);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void queueBindExchange(Channel channel, String queue, String exchange,String routingKey){
        try {
            channel.queueBind(queue, exchange,routingKey);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void sendMessage(Channel channel, String message, String exchange,String routingKey){
        try {
            channel.basicPublish(exchange,routingKey,null,message.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
