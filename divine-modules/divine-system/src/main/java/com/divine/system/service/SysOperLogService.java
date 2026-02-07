package com.divine.system.service;

import com.divine.common.mybatis.core.page.BasePage;
import com.divine.common.mybatis.core.page.PageInfoRes;
import com.divine.system.domain.dto.SysOperLogDto;
import com.divine.system.domain.vo.SysOperLogVo;
import com.divine.common.log.event.OperLogEvent;

import java.util.List;

/**
 * 操作日志 服务层处理
 *
 * @author Lion Li
 */
public interface SysOperLogService {

    /**
     * 操作日志记录
     *
     * @param operLogEvent 操作日志事件
     */
    void recordOper(OperLogEvent operLogEvent);

    PageInfoRes<SysOperLogVo> selectPageOperLogList(SysOperLogDto operLog, BasePage basePage);

    /**
     * 新增操作日志
     *
     * @param bo 操作日志对象
     */
    void insertOperlog(SysOperLogDto bo);

    /**
     * 查询系统操作日志集合
     *
     * @param operLog 操作日志对象
     * @return 操作日志集合
     */
    List<SysOperLogVo> selectOperLogList(SysOperLogDto operLog);

    /**
     * 批量删除系统操作日志
     *
     * @param operIds 需要删除的操作日志ID
     * @return 结果
     */
    int deleteOperLogByIds(Long[] operIds);

    /**
     * 查询操作日志详细
     *
     * @param operId 操作ID
     * @return 操作日志对象
     */
    SysOperLogVo selectOperLogById(Long operId);

    /**
     * 清空操作日志
     */
    void cleanOperLog();
}
