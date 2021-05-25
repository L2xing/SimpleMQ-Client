package com.icecola.simplemq.net;

import com.icecola.simplemq.net.bean.Protocol;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.concurrent.EventExecutorGroup;

/**
 * description:
 *
 * @author liuxingxing
 * @date 2021.05.24 23:01
 */
public class SimpleClientHandler extends SimpleChannelInboundHandler<Protocol> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Protocol protocol) throws Exception {

    }
}
