package com.icecola.simplemq.bean;

import lombok.Data;

/**
 * description:
 * 传输协议
 *
 * @author liuxingxing23@jd.com
 * @date 2021/5/23 17:23
 */
@Data
public class Protocol<T> {

    /**
     * 操作头
     */
    private String Header;

    /**
     * 主题
     */
    private String topic;

    /**
     * 消息实体
     */
    private T data;

}
