package com.divine.common.core.exception.user;

import com.divine.common.core.exception.base.BusinessException;

/**
 * 用户信息异常类
 *
 * @author Yisl
 */
public class UserException extends BusinessException {
    private static final long serialVersionUID = 1L;

    public UserException(String code, Object... args) {
        super("user", code, args, null);
    }
}
