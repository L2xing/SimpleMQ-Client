package com.icecola.simplemq.client.handle;

import com.icecola.simplemq.bean.Protocol;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * description:
 *
 * @author liuxingxing23@jd.com
 * @date 2021/5/23 11:10
 */
@Slf4j
public class ClientProtocolHandler extends SimpleChannelInboundHandler<Protocol> {


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Protocol msg) throws Exception {
        log.info("【netty-client获取数据成功】：{}", msg);
    }

}
