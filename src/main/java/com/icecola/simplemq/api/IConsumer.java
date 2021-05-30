package com.icecola.simplemq.api;

import com.icecola.simplemq.queue.Message;

import java.util.List;

/**
 * description:
 * 消费者接口
 *
 * @author liuxingxing23@jd.com
 * @date 2021/5/25 20:26
 */
public interface IConsumer {

    /**
     * Description:
     * 获取到消息
     *
     * @param messages
     * @return void
     * @author liuxingxing23@jd.com
     * @date 2021/5/25 20:28
     **/
    void onMessages(List<Message> messages);

}
