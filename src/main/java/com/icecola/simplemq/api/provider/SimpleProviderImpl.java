package com.icecola.simplemq.api.provider;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.icecola.simplemq.api.INetListener;
import com.icecola.simplemq.api.IProvider;
import com.icecola.simplemq.bean.ClientTypeEnum;
import com.icecola.simplemq.bean.OperateEnum;
import com.icecola.simplemq.bean.Protocol;
import com.icecola.simplemq.client.Client;
import com.icecola.simplemq.queue.Message;
import io.netty.channel.Channel;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * description:
 *
 * @author liuxingxing23@jd.com
 * @date 2021/5/25 20:31
 */
public class SimpleProviderImpl implements IProvider, INetListener {

    private String topic;

    private volatile Channel channel;

    private Protocol<Message> messageProtocol;

    private AtomicBoolean isWrite = new AtomicBoolean(true);

    public SimpleProviderImpl(String topic) {
        new Thread(() -> {
            new Client(ClientTypeEnum.Provider, this);
        }).start();
        this.topic = topic;

    }

    @Override
    public Protocol<Message> send(Message message) {
        while (this.channel == null ||
                !this.channel.isWritable() ||
                !isWrite.get()
        ) {
        }
        channel.writeAndFlush(buildSendProtocol(message));
        isWrite.compareAndSet(true, false);
        while (!isWrite.get()){
        }
        return messageProtocol;
    }

    @Override
    public void syncResponse(Protocol<Message> messageProtocol) {
        this.messageProtocol = messageProtocol;
        isWrite.compareAndSet(false, true);
    }

    private Protocol<Message> buildSendProtocol(Message message) {
        Protocol<Message> protocol = new Protocol<>();
        protocol.setTopic(this.topic);
        protocol.setHeader(OperateEnum.ENQUEUE.getValue());
        protocol.setData(message);
        return protocol;
    }

    @Override
    public void successChannel(Channel channel) {
        this.channel = channel;
    }


    public static void main(String[] args) {
        SimpleProviderImpl simpleProvider = new SimpleProviderImpl("111");
        for (int i = 0; i < 10; i++) {
            System.out.println("发送");
            simpleProvider.send(new Message(i + "哈哈哈"));
//            try {
//                TimeUnit.SECONDS.sleep(1);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
        }
    }

}
