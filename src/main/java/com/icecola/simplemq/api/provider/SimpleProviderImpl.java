package com.icecola.simplemq.api.provider;

import com.icecola.simplemq.api.INetListener;
import com.icecola.simplemq.api.IProvider;
import com.icecola.simplemq.queue.Message;
import io.netty.channel.Channel;

/**
 * description:
 *
 * @author liuxingxing23@jd.com
 * @date 2021/5/25 20:31
 */
public class SimpleProviderImpl implements IProvider, INetListener {

    @Override
    public void send(Message message) {

    }

    @Override
    public void successChannel(Channel channel) {

    }
}
