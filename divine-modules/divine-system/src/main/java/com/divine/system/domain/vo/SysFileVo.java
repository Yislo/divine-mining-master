package com.divine.system.domain.vo;

import com.divine.system.domain.entity.SysFile;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * OSS对象存储视图对象 sys_oss
 *
 * @author Lion Li
 */
@Data
@AutoMapper(target = SysFile.class)
public class SysFileVo implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 文件名
     */
    private String fileName;

    /**
     * URL地址
     */
    private String fileUrl;

    /**
     * 文件后缀名
     */
    private String fileSuffix;

    /**
     * 业务id
     */
    private Long bizId;

    /**
     * 业务类型
     */
    private String bizType;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 上传人
     */
    private String createBy;


}
