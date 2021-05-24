package com.icecola.simplemq.net;

import com.icecola.simplemq.net.bean.Protocol;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

import java.util.concurrent.TimeUnit;

/**
 * description:
 *
 * @author liuxingxing
 * @date 2021.05.24 23:38
 */
public class ClientHandler extends ChannelInboundHandlerAdapter {


    private ChannelHandlerContext ctx;
    private ChannelPromise promise;
    private NettyMessage response;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        this.ctx = ctx;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Protocol message = (Protocol) msg;

        ctx.fireChannelRead(msg);
    }

    public synchronized ChannelPromise sendMessage(Object message) {
        while (ctx == null) {
            try {
                TimeUnit.MILLISECONDS.sleep(1);
                //logger.error("等待ChannelHandlerContext实例化");
            } catch (InterruptedException e) {
            }
        }
        promise = ctx.newPromise();
        ctx.writeAndFlush(message);
        return promise;
    }

    public NettyMessage getResponse() {
        return response;
    }

}
