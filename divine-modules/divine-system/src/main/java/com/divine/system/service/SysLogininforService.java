package com.divine.system.service;

import com.divine.common.mybatis.core.page.BasePage;
import com.divine.system.domain.dto.SysLogininforDto;
import com.divine.system.domain.entity.SysLogininfor;
import com.divine.system.domain.vo.SysLogininforVo;
import com.divine.common.mybatis.core.page.PageInfoRes;
import com.divine.common.log.event.LogininforEvent;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 系统访问日志情况信息 服务层处理
 *
 * @author Lion Li
 */
@Service
public interface SysLogininforService {

    /**
     * 记录登录信息
     *
     * @param logininforEvent 登录事件
     */
    void recordLogininfor(LogininforEvent logininforEvent);


    PageInfoRes<SysLogininforVo> selectPageLogininforList(SysLogininforDto logininfor, BasePage basePage);

    /**
     * 新增系统登录日志
     *
     * @param bo 访问日志对象
     */
    void insertLogininfor(SysLogininforDto bo);

    /**
     * 查询系统登录日志集合
     *
     * @param logininfor 访问日志对象
     * @return 登录记录集合
     */
    List<SysLogininfor> selectLogininforList(SysLogininforDto logininfor);

    /**
     * 批量删除系统登录日志
     *
     * @param infoIds 需要删除的登录日志ID
     * @return 结果
     */
    int deleteLogininforByIds(Long[] infoIds);

    /**
     * 清空系统登录日志
     */
    void cleanLogininfor();
}
