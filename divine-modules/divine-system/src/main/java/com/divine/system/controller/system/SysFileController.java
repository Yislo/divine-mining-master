package com.divine.system.controller.system;


import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.core.util.ObjectUtil;
import com.divine.common.core.exception.base.BusinessException;
import com.divine.common.log.annotation.Log;
import com.divine.common.mybatis.core.page.BasePage;
import com.divine.common.web.core.BaseController;
import com.divine.common.core.domain.Result;
import com.divine.common.mybatis.core.page.PageInfoRes;
import com.divine.common.core.validate.QueryGroup;
import com.divine.common.log.enums.BusinessType;
import com.divine.system.domain.dto.SysFileDTO;
import com.divine.system.domain.dto.SysQueryFileDto;
import com.divine.system.domain.vo.SysFileVo;
import com.divine.system.domain.vo.UploadFileVO;
import com.divine.system.service.SysFileService;
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

/**
 * 文件上传
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/file")
public class SysFileController extends BaseController {

    private final SysFileService sysSssService;

    /**
     * 获取文件列表
     * @param dto
     * @param basePage
     * @return
     */
    @SaCheckPermission("system:file:list")
    @GetMapping("/list")
    public PageInfoRes<SysFileVo> list(@Validated(QueryGroup.class) SysQueryFileDto dto, BasePage basePage) {
        return sysSssService.queryPageList(dto, basePage);
    }

    /**
     * 获取文件详情
     * @param ossIds
     * @return
     */
    @SaCheckPermission("system:file:list")
    @GetMapping("/listByIds/{ossIds}")
    public Result<List<SysFileVo>> listByIds(@NotEmpty(message = "主键不能为空")
                                       @PathVariable Long[] ossIds) {
        List<SysFileVo> list = sysSssService.listByIds(Arrays.asList(ossIds));
        return Result.success(list);
    }

    /**
     * 上传文件
     * @param file
     * @return
     */
    @SaCheckPermission("system:file:upload")
    @Log(title = "OSS对象存储", businessType = BusinessType.INSERT)
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Result<UploadFileVO> upload(@RequestPart("file") MultipartFile file) {
        if (ObjectUtil.isNull(file)) {
            throw new BusinessException("上传文件不能为空");
        }
        return Result.success(sysSssService.upload(file));
    }

    /**
     * 保存文件信息
     * @param dto
     * @return
     */
    @SaCheckPermission("system:file:save")
    @Log(title = "OSS对象存储", businessType = BusinessType.INSERT)
    @PostMapping(value = "/save")
    public Result<Boolean> saveFile(@RequestBody SysFileDTO dto) {
        sysSssService.saveFile(dto);
        return Result.success(true);
    }

    /**
     * 下载文件
     * @param ossId
     * @param response
     * @throws IOException
     */
    @SaCheckPermission("system:file:download")
    @GetMapping("/download/{ossId}")
    public void download(@PathVariable Long ossId, HttpServletResponse response) throws IOException {
        sysSssService.download(ossId,response);
    }

    /**
     * 删除文件
     * @param ossIds
     * @return
     */
    @SaCheckPermission("system:file:remove")
    @Log(title = "OSS对象存储", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ossIds}")
    public Result<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ossIds) {
        return toAjax(sysSssService.deleteFileByIds(List.of(ossIds), true));
    }

}
