package com.divine.system.service.impl;

import cn.dev33.satoken.secure.BCrypt;
import com.divine.common.core.constant.CacheConstants;
import com.divine.common.core.constant.Constants;
import com.divine.common.core.domain.dto.RegisterBody;
import com.divine.common.core.enums.UserType;
import com.divine.common.core.exception.user.CaptchaException;
import com.divine.common.core.exception.user.CaptchaExpireException;
import com.divine.common.core.exception.user.UserException;
import com.divine.common.core.utils.MessageUtils;
import com.divine.common.core.utils.ServletUtils;
import com.divine.common.core.utils.SpringUtils;
import com.divine.common.core.utils.StringUtils;
import com.divine.common.log.event.LogininforEvent;
import com.divine.common.redis.utils.RedisUtils;
import com.divine.common.web.config.properties.CaptchaProperties;
import com.divine.system.domain.dto.SysUserDto;
import com.divine.system.service.SysRegisterService;
import com.divine.system.service.SysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 注册校验方法
 *
 * @author Lion Li
 */
@RequiredArgsConstructor
@Service
public class SysRegisterServiceImpl implements SysRegisterService {

    private final SysUserService userService;
    private final CaptchaProperties captchaProperties;

    /**
     * 注册
     */
    @Override
    public void register(RegisterBody registerBody) {
        String username = registerBody.getUsername();
        String password = registerBody.getPassword();
        // 校验用户类型是否存在
        String userType = UserType.getUserType(registerBody.getUserType()).getUserType();

        boolean captchaEnabled = captchaProperties.getEnable();
        // 验证码开关
        if (captchaEnabled) {
            validateCaptcha(username, registerBody.getCode(), registerBody.getUuid());
        }
        SysUserDto sysUser = new SysUserDto();
        sysUser.setUserName(username);
        sysUser.setNickName(username);
        sysUser.setPassword(BCrypt.hashpw(password));
        sysUser.setUserType(userType);

        if (!userService.checkUserNameUnique(sysUser)) {
            throw new UserException("user.register.save.error", username);
        }
        boolean regFlag = userService.registerUser(sysUser);
        if (!regFlag) {
            throw new UserException("user.register.error");
        }
        recordLogininfor(username, Constants.REGISTER, MessageUtils.message("user.register.success"));
    }

    /**
     * 校验验证码
     *
     * @param username 用户名
     * @param code     验证码
     * @param uuid     唯一标识
     */
    @Override
    public void validateCaptcha(String username, String code, String uuid) {
        String verifyKey = CacheConstants.CAPTCHA_CODE_KEY + StringUtils.defaultString(uuid, "");
        String captcha = RedisUtils.getCacheObject(verifyKey);
        RedisUtils.deleteObject(verifyKey);
        if (captcha == null) {
            recordLogininfor(username, Constants.REGISTER, MessageUtils.message("user.jcaptcha.expire"));
            throw new CaptchaExpireException();
        }
        if (!code.equalsIgnoreCase(captcha)) {
            recordLogininfor(username, Constants.REGISTER, MessageUtils.message("user.jcaptcha.error"));
            throw new CaptchaException();
        }
    }

    /**
     * 记录登录信息
     *
     * @param username 用户名
     * @param status   状态
     * @param message  消息内容
     * @return
     */
    private void recordLogininfor(String username, String status, String message) {
        LogininforEvent logininforEvent = new LogininforEvent();
        logininforEvent.setUsername(username);
        logininforEvent.setStatus(status);
        logininforEvent.setMessage(message);
        logininforEvent.setRequest(ServletUtils.getRequest());
        SpringUtils.context().publishEvent(logininforEvent);
    }

}
