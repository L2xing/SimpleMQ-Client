package com.icecola.simplemq.net.bean;

import cn.hutool.core.util.StrUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * description:
 * 操作枚举
 *
 * @author liuxingxing23@jd.com
 * @date 2021/5/23 17:24
 */
@AllArgsConstructor
public enum OperateEnum {

    /**
     * 查看
     */
    CAT("cat"),

    /**
     * 入队
     */
    ENQUEUE("enqueue"),

    /**
     * 出队
     */
    DEQUEUE("dequeue"),

    /**
     * 响应
     */
    RESPONSE("response");

    @Getter
    private final String value;

    public static OperateEnum getEnum(String str) {
        if (StrUtil.isNotEmpty(str)) {
            for (OperateEnum operateEnum : OperateEnum.values()) {
                if (StrUtil.equals(operateEnum.value, str)) {
                    return operateEnum;
                }
            }
        }
        return CAT;
    }

}
