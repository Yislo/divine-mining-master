package com.divine.system.service;

import com.divine.common.core.domain.dto.RegisterBody;

/**
 * 注册校验方法
 *
 * @author Lion Li
 */
public interface SysRegisterService {

    /**
     * 注册
     */
    void register(RegisterBody registerBody);

    /**
     * 校验验证码
     *
     * @param username 用户名
     * @param code     验证码
     * @param uuid     唯一标识
     */
    void validateCaptcha(String username, String code, String uuid);


}
