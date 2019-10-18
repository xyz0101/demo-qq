package com.jenkin;

import com.jenkin.model.Message;

import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;

public class Const {
    public static final String CLIENT_BASE_DIR="D:/IdeaWorkSpace/demo-01/qq-client/src/main/resources/";
    public static final ConcurrentHashMap<String, Queue<Message>> MESSAGE_QUEUE = new ConcurrentHashMap<>();

}
