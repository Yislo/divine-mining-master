package com.divine.system.service;

import com.divine.common.mybatis.core.page.BasePage;
import com.divine.system.domain.dto.SysOssDto;
import com.divine.system.domain.vo.SysOssVo;
import com.divine.common.mybatis.core.page.PageInfoRes;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Collection;
import java.util.List;

/**
 * 文件上传 服务层实现
 *
 * @author Lion Li
 */
public interface SysOssService {

    PageInfoRes<SysOssVo> queryPageList(SysOssDto bo, BasePage basePage);

    List<SysOssVo> listByIds(Collection<Long> ossIds);

    String selectUrlByIds(String ossIds);

    SysOssVo getById(Long ossId);

    void download(Long ossId, HttpServletResponse response);

    SysOssVo upload(MultipartFile file);

    SysOssVo upload(File file);

    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

}
