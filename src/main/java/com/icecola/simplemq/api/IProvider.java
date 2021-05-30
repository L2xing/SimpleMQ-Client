package com.icecola.simplemq.api;

import com.icecola.simplemq.queue.Message;


/**
 * description:
 * 生产者接口
 *
 * @author liuxingxing23@jd.com
 * @date 2021/5/25 20:26
 */
public interface IProvider {

    void send(Message message);

}
