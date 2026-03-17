package com.divine.common.core.exception.base;

import com.divine.common.core.enums.HttpStatusEnum;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * 错误码
     */
    private final Integer code;

    public BusinessException() {
        super("");
        this.code = HttpStatusEnum.FAIL.getCode();
    }

    public BusinessException(HttpStatusEnum errorCode) {
        super(errorCode.getMsg());
        this.code = errorCode.getCode();
    }

    public BusinessException(String message) {
        super(message);
        this.code = HttpStatusEnum.FAIL.getCode();
    }

    public BusinessException(HttpStatusEnum errorCode, String message) {
        super(message);
        this.code = errorCode.getCode();
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
        this.code = HttpStatusEnum.FAIL.getCode();
    }


}

