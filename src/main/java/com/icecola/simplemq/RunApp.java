package com.icecola.simplemq;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.icecola.simplemq.common.Message;
import com.icecola.simplemq.net.NettyClient;
import com.icecola.simplemq.net.bean.OperateEnum;
import com.icecola.simplemq.net.bean.Protocol;
import io.netty.channel.Channel;
import javafx.beans.property.Property;

import java.util.concurrent.TimeUnit;

/**
 * description:
 *
 * @author liuxingxing
 * @date 2021.05.24 22:52
 */
public class RunApp {

    public static void main(String[] args) throws InterruptedException {
        new Thread(() -> {
            try {
                NettyClient client = new NettyClient("127.0.0.1", 6666);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        while (NettyClient.clientChannel == null) {
            TimeUnit.SECONDS.sleep(1);
        }
        Protocol<Message> property = new Protocol<>();
//        Message message = new Message("消息一");
//        property.setData(message);
//        property.setTopic("test");
//        property.setHeader(OperateEnum.ENQUEUE.getValue());
        property.setTopic("test");
        property.setHeader(OperateEnum.DEQUEUE.getValue());
        NettyClient.clientChannel.writeAndFlush(JSONUtil.toJsonStr(property));
        System.out.println("发送成功");
    }
}
