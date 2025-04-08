package com.lxj.myblog.ws;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.lxj.myblog.domain.entity.ChatRecord;
import com.lxj.myblog.domain.entity.R;
import com.lxj.myblog.domain.entity.RequestMessage;
import com.lxj.myblog.mapper.ChatMapper;
import com.lxj.myblog.mapper.UserMapper;
import com.lxj.myblog.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 用于用户之间相互发送消息
 */
@Slf4j
@ServerEndpoint("/chat/{senderId}")
@Component
public class WebSocketServer {

    // 静态的 mapper 变量
    private static ChatMapper chatMapper;
    private static UserService userService;
    // 注入 mapper
    @Autowired
    private ChatMapper tempChatMapper;

    @Autowired
    private UserService tempUserService;
    // 在对象创建后，将注入的 mapper 赋值给静态变量
    @PostConstruct
    public void init() {
        chatMapper = tempChatMapper;
        userService = tempUserService;
    }


    //用来存储所有的的WebSocketServer对象
    private static final Map<Integer, WebSocketServer> onlineUsers = new ConcurrentHashMap<>();
    private static long COUNT = 0;
    private Session session;
    private Integer senderId;

    /**
     * 连接建立时被调用
     * @param session  websocket的session
     * @param senderId 用户id
     * @Description @PathParam注解用于获取路径参数,是websocket自己实现的一个注解
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("senderId") Integer senderId) throws IOException {
        this.session = session;
        this.senderId = senderId;
        if (onlineUsers.containsKey(senderId)) {
            onlineUsers.remove(senderId);
            onlineUsers.put(senderId, this);
        } else {
            onlineUsers.put(senderId, this);
            userService.updateUserStatus(senderId, "online");
            addCount();
        }
        //广播消息,通知所有用户
        String msg = JSON.toJSONString(R.createMsg(true, null, onlineUsers.keySet()), SerializerFeature.WriteNullStringAsEmpty);
        broadcast(msg);
        log.info("当前在线人数为：{}，分别是：{}", getCount(),onlineUsers.keySet());
    }

    /**
     * 接收到客户端发送的数据时被调用
     * @param message 用户发送的消息
     */
    @OnMessage
    public void onMessage(String message) throws IOException {
        RequestMessage requestMessage = JSON.parseObject(message, RequestMessage.class);
        if (requestMessage.getMsg().isEmpty()) {
            String msg = JSON.toJSONString(R.createMsg(true, null, "消息不能为空"), SerializerFeature.WriteNullStringAsEmpty);
            this.session.getBasicRemote().sendText(msg);
        } else if (requestMessage.getReceiverId().equals(0)) {
            String msg = JSON.toJSONString(R.createMsg(false, this.senderId, requestMessage.getMsg()), SerializerFeature.WriteNullStringAsEmpty);
            broadcast(msg);
        } else if (onlineUsers.containsKey(requestMessage.getReceiverId())) {
            //获取目的用户的Session对象
            WebSocketServer webSocketServer = onlineUsers.get(requestMessage.getReceiverId());
            Session session = webSocketServer.session;
            //发送消息
            String msg = JSON.toJSONString(R.createMsg(false, this.senderId, requestMessage.getMsg()), SerializerFeature.WriteNullStringAsEmpty);
            session.getBasicRemote().sendText(msg);
            // 持久化消息
            saveRecord(this.senderId,requestMessage.getReceiverId(),requestMessage.getMsg());
        } else {
//            String msg = JSON.toJSONString(R.createMsg(true, null, "没有指定的目的联系人"), SerializerFeature.WriteNullStringAsEmpty);
//            this.session.getBasicRemote().sendText(msg);   ChatRecord chatRecord = new ChatRecord();
            saveRecord(this.senderId,requestMessage.getReceiverId(),requestMessage.getMsg());
        }
    }

    /**
     * 连接关闭时被调用
     */
    @OnClose
    public void onClose() throws IOException {
        //从onlineUsers中删除当前用户WebSocketServer对象
        if (onlineUsers.containsKey(this.senderId)) {
            onlineUsers.remove(senderId);
            userService.updateUserStatus(senderId, "offline");
            subCount();
            //通知其他用户，该用户下线了
//            String msg = JSON.toJSONString(R.createMsg(true, null, onlineUsers.keySet()), SerializerFeature.WriteNullStringAsEmpty);
//            broadcast(msg);
            log.info("当前在线人数为：{}，分别是：{}", getCount(),onlineUsers.keySet());
        }
    }

    /**
     * 连接错误时被调用
     * @param error 异常
     */
    @OnError
    public void onError(Throwable error) throws IOException {
        String msg = JSON.toJSONString(R.createMsg(true, null, "发生错误：消息格式不正确"), SerializerFeature.WriteNullStringAsEmpty);
        this.session.getBasicRemote().sendText(msg);
        log.error("用户{}发生错误，原因是：消息格式不正确，具体原因是：{}",this.senderId,error.getMessage());
    }

    /**
     * 广播方法
     * @param message 消息
     */
    private void broadcast(String message) throws IOException {
        //遍历Map集合
        for(WebSocketServer webSocketServer:onlineUsers.values()){
            Session session=webSocketServer.session;
            //发送消息
            session.getBasicRemote().sendText(message);
        }
    }
    public static void saveRecord(Integer senderId,Integer receiverId,String msg){
        ChatRecord chatRecord = new ChatRecord();
        chatRecord.setSenderId(senderId);
        chatRecord.setReceiverId(receiverId);
        chatRecord.setMessage(msg);
        chatRecord.setSendTime(LocalDateTime.now());
        chatMapper.insertChatRecord(chatRecord);
    }

    public static synchronized void addCount(){
        WebSocketServer.COUNT++;
    }

    public static synchronized void subCount(){
        WebSocketServer.COUNT--;
    }

    public static synchronized long getCount(){
        return WebSocketServer.COUNT;
    }
}