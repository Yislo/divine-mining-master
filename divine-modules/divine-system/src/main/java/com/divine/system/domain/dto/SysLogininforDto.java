package com.divine.system.domain.dto;

import com.divine.system.domain.entity.SysLogininfor;
import io.github.linpeilie.annotations.AutoMapper;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 系统访问记录业务对象 sys_logininfor
 *
 * @author Michelle.Chung
 */

@Data
@AutoMapper(target = SysLogininfor.class, reverseConvertGenerate = false)
public class SysLogininforDto {

    @Schema(description = "访问ID")
    private Long infoId;

    @Schema(description = "用户账号")
    private String userName;

    @Schema(description = "设备类型")
    private String deviceType;

    @Schema(description = "登录IP地址")
    private String ipaddr;

    @Schema(description = "登录地点")
    private String loginLocation;

    @Schema(description = "浏览器类型")
    private String browser;

    @Schema(description = "操作系统")
    private String os;

    @Schema(description = "登录状态（0失败 1成功）")
    private String status;

    @Schema(description = "提示消息")
    private String msg;

    @Schema(description = "访问时间")
    private LocalDateTime loginTime;

    @Schema(description = "请求参数")
    private Map<String, Object> params = new HashMap<>();


}
