package com.icecola.simplemq.client.handle;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.icecola.simplemq.api.IConsumer;
import com.icecola.simplemq.bean.Protocol;
import com.icecola.simplemq.queue.Message;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

/**
 * description:
 *
 * @author liuxingxing23@jd.com
 * @date 2021/5/23 11:10
 */
@Slf4j
public class ClientProtocolHandler extends SimpleChannelInboundHandler<String> {

    private IConsumer consumer;

    public ClientProtocolHandler(IConsumer consumer) {
        this.consumer = consumer;
    }

    private Protocol<Message> parseMsg(String msg) {
        try {
            Protocol protocol = JSONUtil.toBean(msg, Protocol.class);
            if (protocol.getData() instanceof JSONObject) {
                Message message = JSONUtil.toBean((JSONObject) protocol.getData(), Message.class);
                protocol.setData(message);
            }
            return protocol;
        } catch (
                Exception e) {
            log.info("【自定义协议转化器异常】：", e);
            // 应该终止了
        }
        return null;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        Protocol<Message> messageProtocol = parseMsg(msg);
        log.info("【netty-client获取数据成功】：{}", msg);
        // todo 这里需要处理分情况了

        // 短期内先简单处理
        Object data = messageProtocol.getData();
        if (data instanceof Integer) {
            // CAT 请求
            log.info("【处理分发】 CAT处理：{}", messageProtocol.getData());

        } else if (data instanceof Message) {
            // DEQUEUE 请求
            log.info("【处理分发】 DEQUEUE处理：{}", messageProtocol.getData());
            Message message = (Message) data;
            consumer.onMessages(Arrays.asList(message));
        } else {
            log.info("【处理分发】 其他处理：{}", msg);
        }


    }

}
