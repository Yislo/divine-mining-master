package com.divine.system.domain.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: Yisl
 * @Description:
 * @Date: 2026/2/18 15:40
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysFileDTO {

    /**
     * 文件地址
     */
    @NotBlank(message = "文件地址不能为空")
    private String fileUrl;

    /**
     * 文件名称
     */
    @NotBlank(message = "文件名称不能为空")
    private String fileName;

    /**
     * 业务id
     */
    @NotBlank(message = "业务id不能为空")
    private Long bizId;

    /**
     * 业务类型
     */
    @NotBlank(message = "业务类型不能为空")
    private String bizType;



    public static String getFileName(String url) {
        if (url == null || url.isEmpty()) {
            return null;
        }
        // 找到最后一个 '/' 的位置
        int lastSlashIndex = url.lastIndexOf('/');
        if (lastSlashIndex >= 0 && lastSlashIndex < url.length() - 1) {
            return url.substring(lastSlashIndex + 1);
        }
        return url;
    }


}
