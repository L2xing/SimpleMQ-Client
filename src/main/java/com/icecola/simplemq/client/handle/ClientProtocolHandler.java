package com.icecola.simplemq.client.handle;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.icecola.simplemq.api.IConsumer;
import com.icecola.simplemq.api.INetListener;
import com.icecola.simplemq.api.IProvider;
import com.icecola.simplemq.bean.ClientTypeEnum;
import com.icecola.simplemq.bean.Protocol;
import com.icecola.simplemq.queue.Message;
import io.netty.channel.Channel;
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
public class ClientProtocolHandler extends SimpleChannelInboundHandler<Protocol> {

    private ClientTypeEnum clientTypeEnum;

    private INetListener iNetListener;

    private Channel channel;

    public ClientProtocolHandler(ClientTypeEnum typeEnum, INetListener iNetListener) {
        this.clientTypeEnum = typeEnum;
        this.iNetListener = iNetListener;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        this.channel = ctx.channel();
        iNetListener.successChannel(this.channel);
        System.out.println("channelActive:" + this.channel);
        ctx.fireChannelActive();
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
            ((IConsumer) this.iNetListener).onMessages(Arrays.asList(message));
        } else if (data instanceof String) {
            log.info("【处理分发】 DEQUEUE处理：{}", msg.getData());
            ((IProvider) this.iNetListener).syncResponse(msg);
        } else {
            log.info("【处理分发】 其他处理：{}", msg);
        }
    }

}
