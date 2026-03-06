package com.divine.system.service;

import com.divine.common.mybatis.core.page.BasePage;
import com.divine.system.domain.dto.SysFileDTO;
import com.divine.system.domain.dto.SysQueryFileDto;
import com.divine.system.domain.vo.SysFileVo;
import com.divine.common.mybatis.core.page.PageInfoRes;
import com.divine.system.domain.vo.UploadFileVO;
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
public interface SysFileService {

    /**
     * 分页查询文件列表
     *
     * @param dto
     * @param basePage
     * @return
     */
    PageInfoRes<SysFileVo> queryPageList(SysQueryFileDto dto, BasePage basePage);

    /**
     * 根据id盘库查询
     *
     * @param ossIds
     * @return
     */
    List<SysFileVo> listByIds(Collection<Long> ossIds);

    /**
     * 根据业务查询文件信息
     *
     * @param bizType 业务类型
     * @param bizIds 业务ids
     * @return
     */
    List<SysFileVo> selectFileByBiz(String bizType, List<Long> bizIds);

    /**
     * 根据id查询文件信息
     *
     * @param ossId
     * @return
     */
    SysFileVo getById(Long ossId);

    /**
     * 下载
     *
     * @param ossId
     * @param response
     */
    void download(Long ossId, HttpServletResponse response);

    /**
     * 上传
     *
     * @param file
     * @return
     */
    UploadFileVO upload(MultipartFile file);

    /**
     * 保存文件信息
     *
     * @param dto
     */
    void saveFile(SysFileDTO dto);

    /**
     * 批量保存文件信息
     *
     * @param dto
     */
    void batchSaveFile(List<SysFileDTO> dto);

    /**
     * 文件上传
     *
     * @param file
     * @return
     */
    UploadFileVO upload(File file);

    /**
     * 根据id删除文件
     *
     * @param ids
     * @param isValid
     * @return
     */
    Boolean deleteFileByIds(Collection<Long> ids, Boolean isValid);
}
