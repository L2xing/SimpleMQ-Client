package com.icecola.simplemq.client.handle;

import com.icecola.simplemq.api.IConsumer;
import com.icecola.simplemq.api.IProvider;
import com.icecola.simplemq.api.consumer.SimpleConsumerImpl;
import com.icecola.simplemq.bean.Protocol;
import com.icecola.simplemq.queue.Message;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import javax.print.attribute.standard.Media;
import java.util.Arrays;

/**
 * description:
 *
 * @author liuxingxing23@jd.com
 * @date 2021/5/23 11:10
 */
@Slf4j
public class ClientProtocolHandler extends SimpleChannelInboundHandler<Protocol> {

    private IConsumer consumer;

    public ClientProtocolHandler(IConsumer consumer) {
        this.consumer = consumer;
    }




    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Protocol msg) throws Exception {
        log.info("【netty-client获取数据成功】：{}", msg);
        // todo 这里需要处理分情况了

        // 短期内先简单处理
        Object data = msg.getData();
        if (data instanceof Integer) {
            // CAT 请求
            log.info("【处理分发】 CAT处理：{}", msg.getData());

        } else if (data instanceof Message) {
            // DEQUEUE 请求
            log.info("【处理分发】 DEQUEUE处理：{}", msg.getData());
            Message message = (Message) data;
            consumer.onMessages(Arrays.asList(message));
        } else {
            log.info("【处理分发】 其他处理：{}", msg);
        }


    }

}
