package com.divine.common.core.exception.file;

import com.divine.common.core.exception.base.BusinessException;

/**
 * 文件信息异常类
 *
 * @author Yisl
 */
public class FileException extends BusinessException {
    private static final long serialVersionUID = 1L;

    public FileException(String code, Object[] args) {
        super("file", code, args, null);
    }

}
