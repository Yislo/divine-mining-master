package com.divine.system.service;

/**
 * 数据权限 实现
 * <p>
 * 注意: 此Service内不允许调用标注`数据权限`注解的方法
 * 例如: deptMapper.selectList 此 selectList 方法标注了`数据权限`注解 会出现循环解析的问题
 *
 * @author Lion Li
 */
public interface SysDataScopeService {


    String getRoleCustom(Long roleId);

    String getDeptAndChild(Long deptId);

}
