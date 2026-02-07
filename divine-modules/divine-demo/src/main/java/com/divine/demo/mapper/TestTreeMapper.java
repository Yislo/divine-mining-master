package com.divine.demo.mapper;

import com.divine.demo.domain.entity.TestTree;
import com.divine.demo.domain.vo.TestTreeVo;
import com.divine.common.mybatis.annotation.DataColumn;
import com.divine.common.mybatis.annotation.DataPermission;
import com.divine.common.mybatis.core.mapper.BaseMapperPlus;

/**
 * 测试树表Mapper接口
 *
 * @author Lion Li
 * @date 2021-07-26
 */
@DataPermission({
    @DataColumn(key = "deptName", value = "dept_id"),
    @DataColumn(key = "userName", value = "user_id")
})
public interface TestTreeMapper extends BaseMapperPlus<TestTree, TestTreeVo> {

}
