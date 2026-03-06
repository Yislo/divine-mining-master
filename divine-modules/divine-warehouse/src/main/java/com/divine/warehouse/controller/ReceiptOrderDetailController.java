package com.divine.warehouse.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.divine.warehouse.domain.dto.ReceiptOrderDetailDto;
import com.divine.common.core.domain.Result;
import com.divine.common.core.validate.AddGroup;
import com.divine.common.core.validate.EditGroup;
import com.divine.common.excel.utils.ExcelUtil;
import com.divine.common.idempotent.annotation.RepeatSubmit;
import com.divine.common.log.annotation.Log;
import com.divine.common.log.enums.BusinessType;
import com.divine.common.mybatis.core.page.BasePage;
import com.divine.common.mybatis.core.page.PageInfoRes;
import com.divine.common.web.core.BaseController;
import com.divine.warehouse.domain.vo.ReceiptDetailVO;
import com.divine.warehouse.domain.vo.ReceiptOrderDetailVO;
import com.divine.warehouse.service.ReceiptOrderDetailService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 入库单详情
 *
 * @author yisl
 * @date 2024-07-19
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/receiptOrderDetail")
public class ReceiptOrderDetailController extends BaseController {

    private final ReceiptOrderDetailService receiptOrderDetailService;

    /**
     * 查询入库单详情列表
     */
    @SaCheckPermission("wms:receipt:all")
    @GetMapping("/list")
    public PageInfoRes<ReceiptDetailVO> list(Long receiptId, BasePage basePage) {
        return receiptOrderDetailService.queryPageList(receiptId, basePage);
    }

    /**
     * 导出入库单详情列表
     */
    @SaCheckPermission("wms:receipt:all")
    @Log(title = "入库单详情", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(ReceiptOrderDetailDto dto, HttpServletResponse response) {
        List<ReceiptOrderDetailVO> list = receiptOrderDetailService.queryList(dto);
        ExcelUtil.exportExcel(list, "入库单详情", ReceiptOrderDetailVO.class, response);
    }

    /**
     * 获取入库单详情详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("wms:receipt:all")
    @GetMapping("/{id}")
    public Result<ReceiptOrderDetailVO> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return Result.success(receiptOrderDetailService.queryById(id));
    }

    /**
     * 新增入库单详情
     */
    @SaCheckPermission("wms:receipt:all")
    @Log(title = "入库单详情", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public Result<Void> add(@Validated(AddGroup.class) @RequestBody ReceiptOrderDetailDto dto) {
        receiptOrderDetailService.insertByBo(dto);
        return Result.success();
    }

    /**
     * 修改入库单详情
     */
    @SaCheckPermission("wms:receipt:all")
    @Log(title = "入库单详情", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public Result<Void> edit(@Validated(EditGroup.class) @RequestBody ReceiptOrderDetailDto dto) {
        receiptOrderDetailService.updateByBo(dto);
        return Result.success();
    }

    /**
     * 删除入库单详情
     *
     * @param ids 主键串
     */
    @SaCheckPermission("wms:receipt:all")
    @Log(title = "入库单详情", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public Result<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        receiptOrderDetailService.deleteByIds(List.of(ids));
        return Result.success();
    }

    /**
     * 根据入库单id查询入库单详情列表
     */
    @SaCheckPermission("wms:receipt:all")
    @GetMapping("/list/{receiptOrderId}")
    public Result<List<ReceiptOrderDetailVO>> listByReceiptOrderId(@NotNull @PathVariable Long receiptOrderId) {
        return Result.success(receiptOrderDetailService.queryByReceiptOrderId(receiptOrderId));
    }
}
