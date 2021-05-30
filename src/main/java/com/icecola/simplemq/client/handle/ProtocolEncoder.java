package com.icecola.simplemq.client.handle;

import cn.hutool.json.JSONUtil;
import com.icecola.simplemq.bean.Protocol;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

/**
 * description:
 * 协议编码器
 *
 * @author liuxingxing23@jd.com
 * @date 2021/5/23 17:44
 */
public class ProtocolEncoder extends MessageToMessageEncoder<Protocol> {

    @Override
    protected void encode(ChannelHandlerContext ctx, Protocol msg, List<Object> out) throws Exception {
        String s = JSONUtil.parse(msg).toString();
        System.out.println("编码器");
        System.out.println(msg);
        out.add(s);
    }
}
