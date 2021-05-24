package com.icecola.simplemq.net.bean;

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
    private String header;

    /**
     * 主题
     */
    private String topic;

    /**
     * 消息实体
     */
    private T data;

    /**
     * Description:
     * 构建响应
     * @param topic
     * @param data
     * @return com.icecola.simplemq.net.bean.Protocol<K>
     * @author liuxingxing23@jd.com
     * @date 2021/5/23 19:40 
     **/
    public static <K> Protocol<K> buildResponse(String topic,  K data) {
        Protocol<K> kProtocol = new Protocol<>();
        kProtocol.setHeader(OperateEnum.RESPONSE.getValue());
        kProtocol.setTopic(topic);
        kProtocol.setData(data);
        return kProtocol;
    }

}
