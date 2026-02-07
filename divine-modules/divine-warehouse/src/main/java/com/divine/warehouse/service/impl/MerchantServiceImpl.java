package com.divine.warehouse.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.divine.warehouse.domain.dto.MerchantDto;
import com.divine.warehouse.domain.entity.Merchant;
import com.divine.warehouse.domain.entity.ReceiptOrder;
import com.divine.warehouse.domain.vo.MerchantVo;
import com.divine.warehouse.mapper.MerchantMapper;
import com.divine.warehouse.mapper.ReceiptOrderMapper;
import com.divine.warehouse.service.MerchantService;
import com.divine.common.core.constant.HttpStatus;
import com.divine.common.core.exception.ServiceException;
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
 * @author zcc
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
    public PageInfoRes<MerchantVo> queryPageList(MerchantDto bo, BasePage basePage) {
        LambdaQueryWrapper<Merchant> lqw = buildQueryWrapper(bo);
        Page<MerchantVo> result = merchantMapper.selectVoPage(basePage.build(), lqw);
        return PageInfoRes.build(result);
    }

    /**
     * 查询往来单位列表
     */
    @Override
    public List<MerchantVo> queryList(MerchantDto bo) {
        LambdaQueryWrapper<Merchant> lqw = buildQueryWrapper(bo);
        return merchantMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<Merchant> buildQueryWrapper(MerchantDto bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<Merchant> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getMerchantCode()), Merchant::getMerchantCode, bo.getMerchantCode());
        lqw.like(StringUtils.isNotBlank(bo.getMerchantName()), Merchant::getMerchantName, bo.getMerchantName());
        lqw.eq(bo.getMerchantType() != null, Merchant::getMerchantType, bo.getMerchantType());
        return lqw;
    }

    /**
     * 新增往来单位
     */
    @Override
    public void insertByBo(MerchantDto bo) {
        Merchant add = MapstructUtils.convert(bo, Merchant.class);
        merchantMapper.insert(add);
    }

    /**
     * 修改往来单位
     */
    @Override
    public void updateByBo(MerchantDto bo) {
        Merchant update = MapstructUtils.convert(bo, Merchant.class);
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
            throw new ServiceException("删除失败", HttpStatus.CONFLICT,"该企业已有业务关联，无法删除！");
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
