package com.icecola.simplemq.client.handle;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.icecola.simplemq.bean.Protocol;
import com.icecola.simplemq.queue.Message;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * description:
 *
 * @author liuxingxing23@jd.com
 * @date 2021/5/23 17:44
 */
@Slf4j
public class ProtocolDecoder extends MessageToMessageDecoder<String> {

    @Override
    protected void decode(ChannelHandlerContext ctx, String msg, List<Object> out) throws Exception {
        try {
            Protocol protocol = JSONUtil.toBean(msg, Protocol.class);
            if (protocol.getData() instanceof JSONObject){
                Message message = JSONUtil.toBean((JSONObject) protocol.getData(), Message.class);
                protocol.setData(message);
            }
            out.add(protocol);
        } catch (Exception e) {
            log.info("【自定义协议转化器异常】：", e);
            // 应该终止了
        }

    }
}
