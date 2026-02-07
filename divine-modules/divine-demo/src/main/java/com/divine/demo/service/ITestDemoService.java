package com.divine.demo.service;

import com.divine.common.mybatis.core.page.BasePage;
import com.divine.demo.domain.dto.TestDemoDto;
import com.divine.demo.domain.entity.TestDemo;
import com.divine.demo.domain.vo.TestDemoVo;
import com.divine.common.mybatis.core.page.PageInfoRes;

import java.util.Collection;
import java.util.List;

/**
 * 测试单表Service接口
 *
 * @author Lion Li
 * @date 2021-07-26
 */
public interface ITestDemoService {

    /**
     * 查询单个
     *
     * @return
     */
    TestDemoVo queryById(Long id);

    /**
     * 查询列表
     */
    PageInfoRes<TestDemoVo> queryPageList(TestDemoDto bo, BasePage basePage);

    /**
     * 自定义分页查询
     */
    PageInfoRes<TestDemoVo> customPageList(TestDemoDto bo, BasePage basePage);

    /**
     * 查询列表
     */
    List<TestDemoVo> queryList(TestDemoDto bo);

    /**
     * 根据新增业务对象插入测试单表
     *
     * @param bo 测试单表新增业务对象
     * @return
     */
    Boolean insertByBo(TestDemoDto bo);

    /**
     * 根据编辑业务对象修改测试单表
     *
     * @param bo 测试单表编辑业务对象
     * @return
     */
    Boolean updateByBo(TestDemoDto bo);

    /**
     * 校验并删除数据
     *
     * @param ids     主键集合
     * @param isValid 是否校验,true-删除前校验,false-不校验
     * @return
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    /**
     * 批量保存
     */
    Boolean saveBatch(List<TestDemo> list);
}
