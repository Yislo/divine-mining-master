package com.divine.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.divine.common.core.constant.CacheNames;
import com.divine.common.core.exception.base.BusinessException;
import com.divine.common.core.service.OssService;
import com.divine.common.core.utils.SpringUtils;
import com.divine.common.core.utils.StreamUtils;
import com.divine.common.core.utils.StringUtils;
import com.divine.common.core.utils.file.FileUtils;
import com.divine.common.mybatis.core.page.BasePage;
import com.divine.common.mybatis.core.page.PageInfoRes;
import com.divine.common.oss.constant.OssConstant;
import com.divine.common.oss.core.OssClient;
import com.divine.common.oss.entity.UploadResult;
import com.divine.common.oss.enumd.AccessPolicyType;
import com.divine.common.oss.factory.OssFactory;
import com.divine.common.redis.utils.RedisUtils;
import com.divine.system.domain.dto.SysFileDTO;
import com.divine.system.domain.dto.SysQueryFileDto;
import com.divine.system.domain.entity.SysFile;
import com.divine.system.domain.vo.SysFileVo;
import com.divine.system.domain.vo.UploadFileVO;
import com.divine.system.mapper.SysFileMapper;
import com.divine.system.service.SysFileService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 文件上传 服务层实现
 *
 * @author Lion Li
 */
@RequiredArgsConstructor
@Service
public class SysFileServiceImpl implements SysFileService, OssService {

    private final SysFileMapper fileMapper;

    /**
     * 分页查询文件信息
     * @param dto
     * @param basePage
     * @return
     */
    @Override
    public PageInfoRes<SysFileVo> queryPageList(SysQueryFileDto dto, BasePage basePage) {
        LambdaQueryWrapper<SysFile> lqw = buildQueryWrapper(dto);
        Page<SysFileVo> result = fileMapper.selectVoPage(basePage.build(), lqw);
        List<SysFileVo> filterResult = StreamUtils.toList(result.getRecords(), this::matchingUrl);
        result.setRecords(filterResult);
        return PageInfoRes.build(result);
    }

    @Override
    public List<SysFileVo> listByIds(Collection<Long> ossIds) {
        List<SysFileVo> list = new ArrayList<>();
        for (Long id : ossIds) {
            SysFileVo vo = SpringUtils.getAopProxy(this).getById(id);
            if (ObjectUtil.isNotNull(vo)) {
                try {
                    list.add(this.matchingUrl(vo));
                } catch (Exception ignored) {
                    // 如果oss异常无法连接则将数据直接返回
                    list.add(vo);
                }
            }
        }
        return list;
    }

    @Override
    public String selectUrlByIds(String ossIds) {
        List<String> list = new ArrayList<>();
        for (Long id : StringUtils.splitTo(ossIds, Convert::toLong)) {
            SysFileVo vo = SpringUtils.getAopProxy(this).getById(id);
            if (ObjectUtil.isNotNull(vo)) {
                try {
                    list.add(this.matchingUrl(vo).getFileUrl());
                } catch (Exception ignored) {
                    // 如果oss异常无法连接则将数据直接返回
                    list.add(vo.getFileUrl());
                }
            }
        }
        return String.join(StringUtils.SEPARATOR, list);
    }

    private LambdaQueryWrapper<SysFile> buildQueryWrapper(SysQueryFileDto dto) {
        Map<String, Object> params = dto.getParams();
        LambdaQueryWrapper<SysFile> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(dto.getFileName()), SysFile::getFileName, dto.getFileName());
        lqw.eq(StringUtils.isNotBlank(dto.getFileSuffix()), SysFile::getFileSuffix, dto.getFileSuffix());
        lqw.eq(StringUtils.isNotBlank(dto.getBizType()), SysFile::getBizType, dto.getBizType());
        lqw.eq(ObjUtil.isNotNull(dto.getBizId()), SysFile::getBizId, dto.getBizId());
        lqw.eq(StringUtils.isNotBlank(dto.getFileSuffix()), SysFile::getFileSuffix, dto.getFileSuffix());
        lqw.between(params.get("beginCreateTime") != null && params.get("endCreateTime") != null,
            SysFile::getCreateTime, params.get("beginCreateTime"), params.get("endCreateTime"));
        lqw.eq(StringUtils.isNotBlank(dto.getCreateBy()), SysFile::getCreateBy, dto.getCreateBy());
        return lqw;
    }

    @Override
    @Cacheable(cacheNames = CacheNames.SYS_OSS, key = "#ossId")
    public SysFileVo getById(Long ossId) {
        return fileMapper.selectVoById(ossId);
    }

    @Override
    public void download(Long ossId, HttpServletResponse response) {
        SysFileVo sysOss = SpringUtils.getAopProxy(this).getById(ossId);
        if (ObjectUtil.isNull(sysOss)) {
            throw new BusinessException("文件数据不存在!");
        }
        FileUtils.setAttachmentResponseHeader(response, sysOss.getFileName());
        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE + "; charset=UTF-8");
        OssClient storage = getOssClient();
        try (InputStream inputStream = storage.getObjectContent(sysOss.getFileUrl())) {
            int available = inputStream.available();
            IoUtil.copy(inputStream, response.getOutputStream(), available);
            response.setContentLength(available);
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
    }

    @Override
    public UploadFileVO upload(MultipartFile file) {
        String originalfileName = file.getOriginalFilename();
        String suffix = StringUtils.substring(originalfileName, originalfileName.lastIndexOf("."), originalfileName.length());
        OssClient storage = OssFactory.instance();
        UploadResult uploadResult;
        try {
            uploadResult = storage.uploadSuffix(file.getBytes(), suffix, file.getContentType());
        } catch (IOException e) {
            throw new BusinessException(e.getMessage());
        }
        // 返回上传成功文件信息
        return UploadFileVO.builder()
            .url(uploadResult.getUrl())
            .fileName(originalfileName)
            .build();
    }

    /**
     * 文件信息持久化
     *
     * @param dto
     */
    @Override
    public void saveFile(SysFileDTO dto) {
        String fileName = dto.getFileName();
        SysFile sysFile = BeanUtil.copyProperties(dto, SysFile.class);
        String suffix = StringUtils.substring(fileName, fileName.lastIndexOf("."), fileName.length());
        sysFile.setFileSuffix(suffix);
        fileMapper.insert(sysFile);
    }

    @Override
    public UploadFileVO upload(File file) {
        String originalfileName = file.getName();
        String suffix = StringUtils.substring(originalfileName, originalfileName.lastIndexOf("."), originalfileName.length());
        OssClient storage = OssFactory.instance();
        UploadResult uploadResult = storage.uploadSuffix(file, suffix);
        // 返回上传成功文件信息
        return UploadFileVO.builder()
            .url(uploadResult.getUrl())
            .fileName(uploadResult.getFilename())
            .build();
    }

//    private SysOssVo buildResultEntity(String originalfileName, String suffix, String configKey, UploadResult uploadResult) {
//        SysFile oss = new SysFile();
//        oss.setFileUrl(uploadResult.getUrl());
//        oss.setFileSuffix(suffix);
//        oss.setFileName(uploadResult.getFilename());
//        oss.setOriginalName(originalfileName);
//        oss.setService(configKey);
//        ossMapper.insert(oss);
//        SysOssVo sysOssVo = MapstructUtils.convert(oss, SysOssVo.class);
//        return this.matchingUrl(sysOssVo);
//    }

    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if (isValid) {
            // 做一些业务上的校验,判断是否需要校验
        }
        List<SysFile> list = fileMapper.selectBatchIds(ids);
        for (SysFile sysFile : list) {
            OssClient storage = getOssClient();
            storage.delete(sysFile.getFileUrl());
        }
        return fileMapper.deleteBatchIds(ids) > 0;
    }

    /**
     * 匹配Url
     *
     * @param oss OSS对象
     * @return oss 匹配Url的OSS对象
     */
    private SysFileVo matchingUrl(SysFileVo oss) {
        OssClient storage = getOssClient();
        // 仅修改桶类型为 private 的URL，临时URL时长为120s
        if (AccessPolicyType.PRIVATE == storage.getAccessPolicy()) {
            oss.setFileUrl(storage.getPrivateUrl(oss.getFileName(), 120));
        }
        return oss;
    }

    private OssClient getOssClient(){
        String defaultConfigKey = RedisUtils.getCacheObject(OssConstant.DEFAULT_CONFIG_KEY).toString();
        if (StringUtils.isBlank(defaultConfigKey)) {
            throw new BusinessException("文件服务初始化失败");
        }
        return  OssFactory.instance(defaultConfigKey);
    }
}
