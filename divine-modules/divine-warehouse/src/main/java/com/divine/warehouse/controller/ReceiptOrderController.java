package com.divine.warehouse.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.divine.common.core.domain.Result;
import com.divine.common.core.utils.DateUtils;
import com.divine.common.core.validate.AddGroup;
import com.divine.common.core.validate.EditGroup;
import com.divine.common.excel.core.ExcelResult;
import com.divine.common.excel.utils.ExcelUtil;
import com.divine.common.idempotent.annotation.RepeatSubmit;
import com.divine.common.log.annotation.Log;
import com.divine.common.log.enums.BusinessType;
import com.divine.common.mybatis.core.page.BasePage;
import com.divine.common.mybatis.core.page.PageInfoRes;
import com.divine.common.web.core.BaseController;
import com.divine.warehouse.domain.dto.ReceiptOrderDto;
import com.divine.warehouse.domain.vo.ReceiptImportVO;
import com.divine.warehouse.domain.vo.ReceiptOrderVo;
import com.divine.warehouse.listener.ReceiptImportListener;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.divine.warehouse.service.ReceiptOrderService;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

/**
 * 入库单
 *
 * @author yisl
 * @date 2024-07-19
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/receiptOrder")
public class ReceiptOrderController extends BaseController {

    private final ReceiptOrderService receiptOrderService;

    /**
     * 查询入库单列表
     *
     * @param dto
     * @param basePage
     * @return
     */
    @SaCheckPermission("wms:receipt:all")
    @GetMapping("/list")
    public PageInfoRes<ReceiptOrderVo> list(ReceiptOrderDto dto, BasePage basePage) {
        return receiptOrderService.queryPageList(dto, basePage);
    }

    /**
     * 导出入库单列表
     *
     * @param dto
     * @param response
     */
    @SaCheckPermission("wms:receipt:all")
    @Log(title = "入库单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(ReceiptOrderDto dto, HttpServletResponse response) {
        List<ReceiptOrderVo> list = receiptOrderService.queryList(dto);
        ExcelUtil.exportExcel(list, "入库单", ReceiptOrderVo.class, response);
    }

    /**
     * 获取入库单详细信息
     *
     * @param id
     * @return
     */
    @SaCheckPermission("wms:receipt:all")
    @GetMapping("/{id}")
    public Result<ReceiptOrderVo> getInfo(@NotNull(message = "id不能为空")
                                          @PathVariable Long id) {
        return Result.success(receiptOrderService.queryById(id));
    }

    /**
     * 获取id
     *
     * @param orderNo
     * @return
     */
    @SaCheckPermission("wms:receipt:all")
    @GetMapping("/getIdByNo")
    public Result<Long> getId(@RequestParam String orderNo) {
        return Result.success(receiptOrderService.queryIdByOrderNo(orderNo));
    }

    /**
     * 导入入库单
     *
     * @param file        导入文件
     * @param warehouseId 仓库id
     */
    @Log(title = "入库", businessType = BusinessType.IMPORT)
    @SaCheckPermission("wms:receipt:all")
    @PostMapping(value = "/importData", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Result<String> importData(@RequestPart("file") MultipartFile file, Long warehouseId) throws Exception {
        ExcelResult<ReceiptImportVO> result = ExcelUtil.importExcel(file.getInputStream(),
            ReceiptImportVO.class, new ReceiptImportListener(warehouseId));
        return Result.success(result.getAnalysis());
    }

    /**
     * 下载入库单导入模板
     *
     * @param response
     */
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response) {
        ExcelUtil.exportExcel(new ArrayList<>(), "入库单导入模板" + "-" + DateUtils.getDate(), ReceiptImportVO.class, response);
    }

    /**
     * 暂存入库单
     *
     * @param dto
     * @return
     */
    @SaCheckPermission("wms:receipt:all")
    @Log(title = "入库单", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public Result<Long> add(@Validated(AddGroup.class) @RequestBody ReceiptOrderDto dto) {
        return Result.success(receiptOrderService.insertByBo(dto));
    }

    /**
     * 入库
     *
     * @param dto
     * @return
     */
    @SaCheckPermission("wms:receipt:all")
    @Log(title = "入库单", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PostMapping("/warehousing")
    public Result<Void> warehousing(@Validated(AddGroup.class) @RequestBody ReceiptOrderDto dto) {
        receiptOrderService.warehousing(dto);
        return Result.success();
    }

    /**
     * 修改入库单
     *
     * @param dto
     * @return
     */
    @SaCheckPermission("wms:receipt:all")
    @Log(title = "入库单", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public Result<Void> edit(@Validated(EditGroup.class) @RequestBody ReceiptOrderDto dto) {
        receiptOrderService.updateByBo(dto);
        return Result.success();
    }

    /**
     * 删除入库单
     *
     * @param id
     * @return
     */
    @SaCheckPermission("wms:receipt:all")
    @Log(title = "入库单", businessType = BusinessType.DELETE)
    @DeleteMapping("/{id}")
    public Result<Void> remove(@NotNull(message = "主键不能为空")
                               @PathVariable Long id) {
        receiptOrderService.deleteById(id);
        return Result.success();
    }
}
