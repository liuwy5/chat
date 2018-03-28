package com.chat.controller.chat;

import com.chat.common.util.JsonUtil;
import com.chat.vo.MessageVo;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 *
 * Created by lw on 16-12-16.
 */
@ServerEndpoint(value = "/webSocket")
@Component
public class MyWebSocket {
    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;

    //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
    private static CopyOnWriteArraySet<MyWebSocket> webSocketSet = new CopyOnWriteArraySet<MyWebSocket>();

    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;

    /**
     * 连接建立成功调用的方法*/
    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        webSocketSet.add(this);     //加入set中
        addOnlineCount();           //在线数加1
        System.out.println("有新连接加入！当前在线人数为" + getOnlineCount());
//        try {
//            sendMessage("服务器消息：有新连接加入！当前在线人数为" + getOnlineCount());
//        } catch (IOException e) {
//            System.out.println("IO异常");
//        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        webSocketSet.remove(this);  //从set中删除
        subOnlineCount();           //在线数减1
        System.out.println("有一连接关闭！当前在线人数为" + getOnlineCount());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println("来自客户端的消息:" + message);

        MessageVo messageVo = (MessageVo)JsonUtil.jsonToObject(message, MessageVo.class);
//        messageVo.setContent("服务器收到消息：" + messageVo.getContent());
//        messageVo.setSelf(false);

        //群发消息
        for (MyWebSocket item : webSocketSet) {
            try {
                item.sendMessage(messageVo);

                messageVo.setContent("服务器收到消息：" + messageVo.getContent());
                messageVo.setSelf(false);
                item.sendMessage(messageVo);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 发生错误时调用
     */
     public void onError(Session session, Throwable error) {
         System.out.println("发生错误");
         error.printStackTrace();
     }

     public void sendMessage(String message) throws IOException {
         MessageVo messageVo = new MessageVo();
         messageVo.setContent(message);
         messageVo.setSelf(true);
         messageVo.setDate("2016-12-21");
         String messageJson = JsonUtil.toJsonString(messageVo);
         this.session.getBasicRemote().sendText(messageJson);
         //this.session.getAsyncRemote().sendText(message);
     }

    public void sendMessage(MessageVo message) throws IOException {
        if (message.getDate() == null) {
            message.setDate("2016-12-21");
        }

        String messageJson = JsonUtil.toJsonString(message);
        this.session.getBasicRemote().sendText(messageJson);
        //this.session.getAsyncRemote().sendText(message);
    }


     /**
      * 群发自定义消息
      */
    public static void sendInfo(String message) throws IOException {
        for (MyWebSocket item : webSocketSet) {
            try {
                item.sendMessage(message);
            } catch (IOException e) {
                continue;
            }
        }
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        MyWebSocket.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        MyWebSocket.onlineCount--;
    }
}
