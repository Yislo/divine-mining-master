package com.divine.system.controller.system;


import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.core.util.ObjectUtil;
import com.divine.common.log.annotation.Log;
import com.divine.common.mybatis.core.page.BasePage;
import com.divine.common.web.core.BaseController;
import com.divine.common.core.domain.Result;
import com.divine.common.mybatis.core.page.PageInfoRes;
import com.divine.common.core.validate.QueryGroup;
import com.divine.common.log.enums.BusinessType;
import com.divine.common.core.exception.ServiceException;
import com.divine.system.domain.dto.SysOssDto;
import com.divine.system.domain.vo.SysOssVo;
import com.divine.system.service.SysOssService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotEmpty;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Tag(name = "文件上传")
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/oss")
public class SysOssController extends BaseController {

    private final SysOssService sysSssService;

    /**
     * 查询OSS对象存储列表
     */
    @Operation(summary = "查询OSS对象存储列表")
    @SaCheckPermission("system:oss:list")
    @GetMapping("/list")
    public PageInfoRes<SysOssVo> list(@Validated(QueryGroup.class) SysOssDto bo, BasePage basePage) {
        return sysSssService.queryPageList(bo, basePage);
    }

    /**
     * 查询OSS对象基于id串
     *
     * @param ossIds OSS对象ID串
     */
    @Operation(summary = "查询OSS对象基于id串")
    @SaCheckPermission("system:oss:list")
    @GetMapping("/listByIds/{ossIds}")
    public Result<List<SysOssVo>> listByIds(@NotEmpty(message = "主键不能为空")
                                       @PathVariable Long[] ossIds) {
        List<SysOssVo> list = sysSssService.listByIds(Arrays.asList(ossIds));
        return Result.success(list);
    }

    /**
     * 上传OSS对象存储
     *
     * @param file 文件
     */
    @Operation(summary = "上传OSS对象存储")
    @SaCheckPermission("system:oss:upload")
    @Log(title = "OSS对象存储", businessType = BusinessType.INSERT)
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Result<Map<String, String>> upload(@RequestPart("file") MultipartFile file) {
        if (ObjectUtil.isNull(file)) {
            throw new ServiceException("上传文件不能为空");
        }
        SysOssVo oss = sysSssService.upload(file);
        return Result.success(Map.of(
            "url", oss.getUrl(),
            "fileName", oss.getOriginalName(),
            "ossId", oss.getOssId().toString()
        ));
    }

    /**
     * 下载OSS对象
     *
     * @param ossId OSS对象ID
     */
    @Operation(summary = "下载OSS对象")
    @SaCheckPermission("system:oss:download")
    @GetMapping("/download/{ossId}")
    public void download(@PathVariable Long ossId, HttpServletResponse response) throws IOException {
        sysSssService.download(ossId,response);
    }

    /**
     * 删除OSS对象存储
     *
     * @param ossIds OSS对象ID串
     */
    @Operation(summary = "删除OSS对象存储")
    @SaCheckPermission("system:oss:remove")
    @Log(title = "OSS对象存储", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ossIds}")
    public Result<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ossIds) {
        return toAjax(sysSssService.deleteWithValidByIds(List.of(ossIds), true));
    }

}
