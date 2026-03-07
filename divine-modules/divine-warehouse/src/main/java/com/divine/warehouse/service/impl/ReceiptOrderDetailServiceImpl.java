package com.divine.warehouse.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.divine.common.core.enums.FileBizTypeEnum;
import com.divine.system.domain.vo.SysFileVo;
import com.divine.system.service.SysFileService;
import com.divine.warehouse.domain.dto.ReceiptOrderDetailDto;
import com.divine.warehouse.domain.entity.ReceiptOrderDetail;
import com.divine.warehouse.domain.vo.ReceiptDetailVO;
import com.divine.warehouse.domain.vo.ReceiptOrderDetailVO;
import com.divine.warehouse.mapper.ReceiptOrderDetailMapper;
import com.divine.warehouse.service.ItemSkuService;
import com.divine.warehouse.service.ReceiptOrderDetailService;
import com.divine.common.core.utils.MapstructUtils;
import com.divine.common.mybatis.core.page.BasePage;
import com.divine.common.mybatis.core.page.PageInfoRes;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 入库单详情Service业务层处理
 *
 * @author yisl
 * @date 2024-07-19
 */
@RequiredArgsConstructor
@Service
public class ReceiptOrderDetailServiceImpl extends ServiceImpl<ReceiptOrderDetailMapper, ReceiptOrderDetail> implements ReceiptOrderDetailService {

    private final ReceiptOrderDetailMapper receiptOrderDetailMapper;
    private final ItemSkuService itemSkuService;
    private final SysFileService sysFileService;

    /**
     * 查询入库单详情
     */
    @Override
    public ReceiptOrderDetailVO queryById(Long id) {
        return receiptOrderDetailMapper.selectVoById(id);
    }

    /**
     * 查询入库单详情列表
     */
    @Override
    public PageInfoRes<ReceiptDetailVO> queryPageList(Long receiptId, BasePage basePage) {
        IPage<ReceiptDetailVO> result = receiptOrderDetailMapper.getReceiptDetailList(basePage.build(), receiptId);
        return PageInfoRes.build(result);
    }

    /**
     * 查询入库单详情列表
     */
    @Override
    public List<ReceiptOrderDetailVO> queryList(ReceiptOrderDetailDto dto) {
        LambdaQueryWrapper<ReceiptOrderDetail> lqw = buildQueryWrapper(dto);
        return receiptOrderDetailMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<ReceiptOrderDetail> buildQueryWrapper(ReceiptOrderDetailDto dto) {
        Map<String, Object> params = dto.getParams();
        LambdaQueryWrapper<ReceiptOrderDetail> lqw = Wrappers.lambdaQuery();
        lqw.eq(dto.getReceiptId() != null, ReceiptOrderDetail::getReceiptId, dto.getReceiptId());
        lqw.eq(dto.getSkuId() != null, ReceiptOrderDetail::getSkuId, dto.getSkuId());
        lqw.eq(dto.getQuantity() != null, ReceiptOrderDetail::getQuantity, dto.getQuantity());
        lqw.eq(dto.getUnitPrice() != null, ReceiptOrderDetail::getUnitPrice, dto.getUnitPrice());
        lqw.eq(dto.getWarehouseId() != null, ReceiptOrderDetail::getWarehouseId, dto.getWarehouseId());
        return lqw;
    }

    /**
     * 新增入库单详情
     */
    @Override
    public void insertByBo(ReceiptOrderDetailDto dto) {
        ReceiptOrderDetail add = MapstructUtils.convert(dto, ReceiptOrderDetail.class);
        receiptOrderDetailMapper.insert(add);
    }

    /**
     * 修改入库单详情
     */
    @Override
    public void updateByBo(ReceiptOrderDetailDto dto) {
        ReceiptOrderDetail update = MapstructUtils.convert(dto, ReceiptOrderDetail.class);
        receiptOrderDetailMapper.updateById(update);
    }

    /**
     * 批量删除入库单详情
     */
    @Override
    public void deleteByIds(Collection<Long> ids) {
        receiptOrderDetailMapper.deleteBatchIds(ids);
    }

    /**
     * 根据入库单id删除入库单详情
     */
    @Override
    public void deleteByReceiptOrderId(@NotNull Long receiptOrderId) {
        LambdaQueryWrapper<ReceiptOrderDetail> lqw = Wrappers.lambdaQuery();
        lqw.eq(ReceiptOrderDetail::getReceiptId, receiptOrderId);
        receiptOrderDetailMapper.delete(lqw);
    }

    /**
     * 保存入库明细
     *
     * @param list
     */
    @Override
    @Transactional
    public void saveDetails(List<ReceiptOrderDetailDto> list) {
        if (CollUtil.isEmpty(list)) {
            return;
        }
        List<ReceiptOrderDetail> detailList = MapstructUtils.convert(list, ReceiptOrderDetail.class);
        saveOrUpdateBatch(detailList);
        // 回填id
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setId(detailList.get(i).getId());
        }

    }

    /**
     * 查询入库单明细
     *
     * @param receiptOrderId
     * @return
     */
    @Override
    public List<ReceiptOrderDetailVO> queryByReceiptOrderId(Long receiptOrderId) {
        ReceiptOrderDetailDto dto = new ReceiptOrderDetailDto();
        dto.setReceiptId(receiptOrderId);
        List<ReceiptOrderDetailVO> details = queryList(dto);
        if (CollUtil.isEmpty(details)) {
            return Collections.emptyList();
        }
        // 封装文件信息
        List<Long> detailIds = details.stream().map(ReceiptOrderDetailVO::getId).toList();
        List<SysFileVo> sysFileVos = sysFileService.selectFileByBiz(FileBizTypeEnum.RECEIPT_DETAIL.getCode(), detailIds);
        Map<Long, List<String>> urlMap = sysFileVos.stream()
            .collect(Collectors.groupingBy(
                SysFileVo::getBizId,  // 按bizId分组
                Collectors.mapping(
                    SysFileVo::getFileUrl,  // 提取fileUrl
                    Collectors.toList()      // 收集到List
                )
            ));
        details.forEach(d -> d.setFileList(urlMap.get(d.getId())));
        itemSkuService.setItemSkuMap(details);
        return details;
    }
}
