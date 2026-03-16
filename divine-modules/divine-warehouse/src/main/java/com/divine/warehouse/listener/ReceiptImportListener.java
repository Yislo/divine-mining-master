package com.divine.warehouse.listener;

import cn.dev33.satoken.secure.BCrypt;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.divine.common.core.exception.base.BusinessException;
import com.divine.common.core.utils.SpringUtils;
import com.divine.common.core.utils.ValidatorUtils;
import com.divine.common.excel.core.ExcelListener;
import com.divine.common.excel.core.ExcelResult;
import com.divine.common.satoken.utils.LoginHelper;
import com.divine.system.domain.dto.SysUserDto;
import com.divine.system.domain.vo.SysUserImportVo;
import com.divine.system.domain.vo.SysUserVo;
import com.divine.system.service.SysConfigService;
import com.divine.system.service.SysUserService;
import com.divine.warehouse.domain.dto.ReceiptImportDTO;
import com.divine.warehouse.domain.vo.ReceiptImportVO;
import com.divine.warehouse.service.ReceiptOrderService;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * 入库单导入
 *
 * @author Lion Li
 */
@Slf4j
public class ReceiptImportListener extends AnalysisEventListener<ReceiptImportVO> implements ExcelListener<ReceiptImportVO> {

    private final List<ReceiptImportDTO> cachedDataList = new ArrayList<>();

    private final ReceiptOrderService receiptOrderService;

    private final Long warehouseId;

    private int successNum = 0;
    private int failureNum = 0;
    private final StringBuilder successMsg = new StringBuilder();
    private final StringBuilder failureMsg = new StringBuilder();

    /**
     * 仓库信息初始化
     *
     * @param warehouseId
     */
    public ReceiptImportListener(Long warehouseId) {
        this.receiptOrderService = SpringUtils.getBean(ReceiptOrderService.class);
        this.warehouseId = warehouseId;
    }

    @Override
    public void invoke(ReceiptImportVO receiptImportVO, AnalysisContext context) {
        try {
            ReceiptImportDTO receiptImportDTO = BeanUtil.copyProperties(receiptImportVO, ReceiptImportDTO.class);
            receiptImportDTO.setWarehouseId(warehouseId);
            cachedDataList.add(receiptImportDTO);
            if (cachedDataList.size() >= 100) {
                saveData();
                cachedDataList.clear();
            }
        } catch (Exception e) {
            failureNum++;
            String msg = "<br/>" + failureNum + "、物品 " + receiptImportVO.getItemName() + receiptImportVO.getSkuName() + " 导入失败：";
            failureMsg.append(msg).append(e.getMessage());
            log.error(msg, e);
        }
    }

    /**
     * 处理剩余的数据
     *
     * @param context
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        saveData();
    }

    /**
     * 保存数据
     */
    private void saveData() {
        receiptOrderService.importBatch(cachedDataList);
        successNum += cachedDataList.size();
    }

    @Override
    public ExcelResult<ReceiptImportVO> getExcelResult() {
        return new ExcelResult<ReceiptImportVO>() {

            @Override
            public String getAnalysis() {
                if (failureNum > 0) {
                    failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
                    throw new BusinessException(failureMsg.toString());
                } else {
                    successMsg.insert(0, "导入完成！已成功导入 " + successNum + " 条数据");
                }
                return successMsg.toString();
            }

            @Override
            public List<ReceiptImportVO> getList() {
                return null;
            }

            @Override
            public List<String> getErrorList() {
                return null;
            }
        };
    }
}
