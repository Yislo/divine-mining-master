package com.divine.system.domain.dto;

import com.divine.system.domain.entity.SysOss;
import com.divine.common.mybatis.core.domain.BaseEntity;
import io.github.linpeilie.annotations.AutoMapper;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * OSS对象存储分页查询对象 sys_oss
 *
 * @author Lion Li
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = SysOss.class, reverseConvertGenerate = false)
public class SysOssDto extends BaseEntity {

    @Schema(description = "ossId")
    private Long ossId;

    @Schema(description = "文件名")
    private String fileName;

    @Schema(description = "原名")
    private String originalName;

    @Schema(description = "文件后缀名")
    private String fileSuffix;

    @Schema(description = "URL地址")
    private String url;

    @Schema(description = "服务商")
    private String service;

}
