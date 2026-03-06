package com.divine.common.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 设备类型
 * 针对一套 用户体系
 *
 * @author Lion Li
 */
@Getter
@AllArgsConstructor
public enum FileBizTypeEnum {


    RECEIPT_DETAIL("receipt_detail","入库详情表"),

    ;

    private final String code;
    private final String desc;
}
