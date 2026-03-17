package com.divine.warehouse.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.divine.warehouse.domain.entity.Merchant;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 往来单位视图对象 wms_merchant
 *
 * @author yisl
 * @date 2024-07-16
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = Merchant.class)
public class MerchantVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;

    /**
     * 编号
     */
    private String merchantNo;

    /**
     * 名称
     */
    private String merchantName;

    /**
     * 企业类型
     */
    private Integer merchantType;

    /**
     * 级别
     */
    private String merchantLevel;

    /**
     * 开户行
     */
    private String bankName;

    /**
     * 银行账户
     */
    private String bankAccount;

    /**
     * 地址
     */
    private String address;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 座机号
     */
    private String tel;

    /**
     * 联系人
     */
    private String contactPerson;

    /**
     * Email
     */
    private String email;

    /**
     * 备注
     */
    private String remark;

}
