package com.divine.system.domain.dto;

import com.divine.system.domain.entity.SysOssConfig;
import com.divine.common.mybatis.core.domain.BaseEntity;
import com.divine.common.core.validate.AddGroup;
import com.divine.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 对象存储配置业务对象 sys_oss_config
 *
 * @author Lion Li
 * @author 孤舟烟雨
 * @date 2021-08-13
 */

@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = SysOssConfig.class, reverseConvertGenerate = false)
public class SysOssConfigDto extends BaseEntity {

    @Schema(description = "主键")
    @NotNull(message = "主键不能为空", groups = {EditGroup.class})
    private Long ossConfigId;

    @Schema(description = "配置key")
    @NotBlank(message = "配置key不能为空", groups = {AddGroup.class, EditGroup.class})
    @Size(min = 2, max = 100, message = "configKey长度必须介于{min}和{max} 之间")
    private String configKey;

    @Schema(description = "accessKey")
    @NotBlank(message = "accessKey不能为空", groups = {AddGroup.class, EditGroup.class})
    @Size(min = 2, max = 100, message = "accessKey长度必须介于{min}和{max} 之间")
    private String accessKey;

    @Schema(description = "秘钥")
    @NotBlank(message = "secretKey不能为空", groups = {AddGroup.class, EditGroup.class})
    @Size(min = 2, max = 100, message = "secretKey长度必须介于{min}和{max} 之间")
    private String secretKey;

    @Schema(description = "桶名称")
    @NotBlank(message = "桶名称不能为空", groups = {AddGroup.class, EditGroup.class})
    @Size(min = 2, max = 100, message = "bucketName长度必须介于{min}和{max}之间")
    private String bucketName;

    @Schema(description = "前缀")
    private String prefix;

    @Schema(description = "访问站点")
    @NotBlank(message = "访问站点不能为空", groups = {AddGroup.class, EditGroup.class})
    @Size(min = 2, max = 100, message = "endpoint长度必须介于{min}和{max}之间")
    private String endpoint;

    @Schema(description = "自定义域名")
    private String domain;

    @Schema(description = "是否https（Y=是,N=否）")
    private String isHttps;

    @Schema(description = "是否默认（0=是,1=否）")
    private String status;

    @Schema(description = "域")
    private String region;

    @Schema(description = "扩展字段")
    private String ext1;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "桶权限类型(0private 1public 2custom)")
    @NotBlank(message = "桶权限类型不能为空", groups = {AddGroup.class, EditGroup.class})
    private String accessPolicy;

}
