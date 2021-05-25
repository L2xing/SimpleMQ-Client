package com.icecola.simplemq.common;

import lombok.Data;

/**
 * description:
 * 消息内容
 *
 * @author liuxingxing
 * @date 2021.05.20 23:00
 */
@Data
public class Message {

    public Message(String text) {
        this.text = text;
    }

    /**
     * 消息内容
     */
    private String text;

}
