package com.jenkin;

import com.jenkin.model.Message;
import com.jenkin.util.RabbitMqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;

public class Const {
    public static final String CLIENT_BASE_DIR="D:/IdeaWorkSpace/demo-01/qq-client/src/main/resources/";
    public static final ConcurrentHashMap<String, Queue<Message>> MESSAGE_QUEUE = new ConcurrentHashMap<>();
    public static  Channel MESSAGE_CHANNEL = RabbitMqUtils.getChannel();
    public static final Channel PRODUCT_PRIVATE_CHANNEL = RabbitMqUtils.getChannel();
    public static final Channel PRODUCT_TOPIC_CHANNEL = RabbitMqUtils.getChannel();
    public static final String MESSAGE_PRIVATE_EXCHANGE = "message_exchange";
    public static final String MESSAGE_TOPIC_EXCHANGE = "message_exchange_topic";

    static {
        RabbitMqUtils.declareExchange(PRODUCT_PRIVATE_CHANNEL,MESSAGE_PRIVATE_EXCHANGE, BuiltinExchangeType.DIRECT);
        RabbitMqUtils.declareExchange(PRODUCT_TOPIC_CHANNEL,MESSAGE_TOPIC_EXCHANGE, BuiltinExchangeType.TOPIC);
    }


}
