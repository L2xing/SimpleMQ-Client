package com.icecola.simplemq.api.consumer;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.icecola.simplemq.api.IConsumer;
import com.icecola.simplemq.api.INetListener;
import com.icecola.simplemq.bean.OperateEnum;
import com.icecola.simplemq.bean.Protocol;
import com.icecola.simplemq.client.Client;
import com.icecola.simplemq.queue.Message;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * description:
 * 定时拉取
 *
 * @author liuxingxing23@jd.com
 * @date 2021/5/25 20:30
 */
@Slf4j
public class SimpleConsumerImpl implements IConsumer, INetListener {

    private Client client;

    private Channel channel;

    private String topic;

    private String pullRequest;

    /**
     * 线程同步
     */
    private volatile boolean pullAble = false;

    public SimpleConsumerImpl(String topic) {
        log.info("【消费者 正在启动】：topic为:{}", topic);
        this.topic = topic;
        client = new Client((INetListener) this);

    }


    @Override
    public void onMessages(List<Message> messages) {
        for (Message message : messages) {
            log.info("【onMessages获取到数据：message:{}】", message);
            pullAble = true;
        }
    }

    @Override
    public void successChannel(Channel channel) {
        new Thread(()->{
            while (channel.isWritable()) {
                log.info("监听者topic:{} 可连接状态", topic);
                // 发送拉取消息信息
//            if (pullAble) {
                channel.writeAndFlush(getPullProtocol());
//                pullAble = false;
//            }
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }


    private String getPullProtocol() {
        if (StrUtil.isBlank(pullRequest) || StrUtil.isEmpty(pullRequest)) {
            Protocol<Message> protocol = new Protocol<>();
            protocol.setTopic(this.topic);
            protocol.setHeader(OperateEnum.DEQUEUE.getValue());
            protocol.setData(null);
            pullRequest = JSONUtil.toJsonStr(protocol);
        }
        return pullRequest;
    }

    public static void main(String[] args) {
        SimpleConsumerImpl simpleConsumer = new SimpleConsumerImpl("111");
    }
}
