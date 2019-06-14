package com.landsky.socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint(value = "/websocket")
@Component    //此注解千万千万不要忘记，它的主要作用就是将这个监听器纳入到Spring容器中进行管理
public class WebSocketPush {

    private Session session;

    private static CopyOnWriteArraySet<WebSocketPush> copyOnWriteArraySet = new  CopyOnWriteArraySet<>();
    static Logger logger=LoggerFactory.getLogger(WebSocketPush.class);
    @OnOpen
    public void onOpen (Session session) {



        this.session = session;

        copyOnWriteArraySet.add(this);

        logger.info("【webSocket消息】 有新的连接，连接总数：{}",copyOnWriteArraySet.size());

    }



    @OnClose

    public void onClose () {

        copyOnWriteArraySet.remove(this);

        logger.info("【webSocket消息】 关闭连接，连接总数：{}",copyOnWriteArraySet.size());

    }



    @OnMessage

    public void onMessage (String message) {

        logger.info("【webSocket消息】 有新的信息，信息内容：{}",message);

    }



    public static void  sendMessage (String message) throws EncodeException{

        for(WebSocketPush websocket : copyOnWriteArraySet){

            logger.info("【webSocket消息】 发送信息，信息内容：{}",message);

            try {

                websocket.session.getBasicRemote().sendText(message);

            } catch (IOException e) {

                e.printStackTrace();

            }

        }

    }

}