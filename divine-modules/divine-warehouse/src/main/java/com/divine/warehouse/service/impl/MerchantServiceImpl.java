package com.divine.warehouse.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.divine.common.core.exception.base.BusinessException;
import com.divine.warehouse.domain.dto.MerchantDto;
import com.divine.warehouse.domain.entity.Merchant;
import com.divine.warehouse.domain.entity.ReceiptOrder;
import com.divine.warehouse.domain.vo.MerchantVo;
import com.divine.warehouse.mapper.MerchantMapper;
import com.divine.warehouse.mapper.ReceiptOrderMapper;
import com.divine.warehouse.service.MerchantService;
import com.divine.common.core.utils.MapstructUtils;
import com.divine.common.core.utils.StringUtils;
import com.divine.common.mybatis.core.page.BasePage;
import com.divine.common.mybatis.core.page.PageInfoRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 往来单位Service业务层处理
 *
 * @author yisl
 * @date 2024-07-16
 */
@RequiredArgsConstructor
@Service
public class MerchantServiceImpl implements MerchantService {

    private final MerchantMapper merchantMapper;
    private final ReceiptOrderMapper receiptOrderMapper;

    /**
     * 查询往来单位
     */
    @Override
    public MerchantVo queryById(Long id){
        return merchantMapper.selectVoById(id);
    }

    /**
     * 查询往来单位列表
     */
    @Override
    public PageInfoRes<MerchantVo> queryPageList(MerchantDto dto, BasePage basePage) {
        LambdaQueryWrapper<Merchant> lqw = buildQueryWrapper(dto);
        Page<MerchantVo> result = merchantMapper.selectVoPage(basePage.build(), lqw);
        return PageInfoRes.build(result);
    }

    /**
     * 查询往来单位列表
     */
    @Override
    public List<MerchantVo> queryList(MerchantDto dto) {
        LambdaQueryWrapper<Merchant> lqw = buildQueryWrapper(dto);
        return merchantMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<Merchant> buildQueryWrapper(MerchantDto dto) {
        Map<String, Object> params = dto.getParams();
        LambdaQueryWrapper<Merchant> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(dto.getMerchantNo()), Merchant::getMerchantNo, dto.getMerchantNo());
        lqw.like(StringUtils.isNotBlank(dto.getMerchantName()), Merchant::getMerchantName, dto.getMerchantName());
        lqw.eq(dto.getMerchantType() != null, Merchant::getMerchantType, dto.getMerchantType());
        return lqw;
    }

    /**
     * 新增往来单位
     */
    @Override
    public void insertByBo(MerchantDto dto) {
        Merchant add = MapstructUtils.convert(dto, Merchant.class);
        merchantMapper.insert(add);
    }

    /**
     * 修改往来单位
     */
    @Override
    public void updateByBo(MerchantDto dto) {
        Merchant update = MapstructUtils.convert(dto, Merchant.class);
        merchantMapper.updateById(update);
    }

    /**
     * 删除往来单位
     */
    @Override
    public void deleteById(Long id) {
        validateIdBeforeDelete(id);
        merchantMapper.deleteById(id);
    }

    private void validateIdBeforeDelete(Long id) {
        LambdaQueryWrapper<ReceiptOrder> receiptOrderLqw = Wrappers.lambdaQuery();
        receiptOrderLqw.eq(ReceiptOrder::getMerchantId, id);
        Long receiptOrderCount = receiptOrderMapper.selectCount(receiptOrderLqw);
        if (receiptOrderCount != null && receiptOrderCount > 0) {
            throw new BusinessException("该企业已有业务关联，无法删除！");
        }
    }

    /**
     * 批量删除往来单位
     */
    @Override
    public void deleteByIds(Collection<Long> ids) {
        merchantMapper.deleteBatchIds(ids);
    }
}
