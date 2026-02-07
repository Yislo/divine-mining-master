package com.divine.generator.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.io.IoUtil;
import com.divine.common.core.domain.Result;
import com.divine.common.mybatis.core.page.BasePage;
import com.divine.common.mybatis.core.page.PageInfoRes;
import com.divine.common.log.annotation.Log;
import com.divine.common.log.enums.BusinessType;
import com.divine.common.web.core.BaseController;
import com.divine.generator.domain.GenTable;
import com.divine.generator.domain.GenTableColumn;
import com.divine.generator.service.IGenTableService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 代码生成 操作处理
 *
 * @author Lion Li
 */
@Tag(name = "代码生成")
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/tool/gen")
public class GenController extends BaseController {

    private final IGenTableService genTableService;

    /**
     * 查询代码生成列表
     */
    @Operation(summary = "查询代码生成列表")
    @SaCheckPermission("tool:gen:list")
    @GetMapping("/list")
    public PageInfoRes<GenTable> genList(GenTable genTable, BasePage basePage) {
        return genTableService.selectPageGenTableList(genTable, basePage);
    }

    /**
     * 修改代码生成业务
     *
     * @param tableId 表ID
     */
    @Operation(summary = "修改代码生成业务")
    @SaCheckPermission("tool:gen:query")
    @GetMapping(value = "/{tableId}")
    public Result<Map<String, Object>> getInfo(@PathVariable Long tableId) {
        GenTable table = genTableService.selectGenTableById(tableId);
        List<GenTable> tables = genTableService.selectGenTableAll();
        List<GenTableColumn> list = genTableService.selectGenTableColumnListByTableId(tableId);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("info", table);
        map.put("rows", list);
        map.put("tables", tables);
        return Result.success(map);
    }

    /**
     * 查询数据库列表
     */
    @Operation(summary = "查询数据库列表")
    @SaCheckPermission("tool:gen:list")
    @GetMapping("/db/list")
    public PageInfoRes<GenTable> dataList(GenTable genTable, BasePage basePage) {
        return genTableService.selectPageDbTableList(genTable, basePage);
    }

    /**
     * 查询数据表字段列表
     *
     * @param tableId 表ID
     */
    @Operation(summary = "查询数据表字段列表")
    @SaCheckPermission("tool:gen:list")
    @GetMapping(value = "/column/{tableId}")
    public PageInfoRes<GenTableColumn> columnList(@PathVariable("tableId") Long tableId) {
        PageInfoRes<GenTableColumn> dataInfo = new PageInfoRes<>();
        List<GenTableColumn> list = genTableService.selectGenTableColumnListByTableId(tableId);
        dataInfo.setRows(list);
        dataInfo.setTotal(list.size());
        return dataInfo;
    }

    /**
     * 导入表结构（保存）
     *
     * @param tables 表名串
     */
    @Operation(summary = "导入表结构（保存）")
    @SaCheckPermission("tool:gen:import")
    @Log(title = "代码生成", businessType = BusinessType.IMPORT)
    @PostMapping("/importTable")
    public Result<Void> importTableSave(String tables) {
        String[] tableNames = Convert.toStrArray(tables);
        // 查询表信息
        List<GenTable> tableList = genTableService.selectDbTableListByNames(tableNames);
        genTableService.importGenTable(tableList);
        return Result.success();
    }

    /**
     * 修改保存代码生成业务
     */
    @Operation(summary = "修改保存代码生成业务")
    @SaCheckPermission("tool:gen:edit")
    @Log(title = "代码生成", businessType = BusinessType.UPDATE)
    @PutMapping
    public Result<Void> editSave(@Validated @RequestBody GenTable genTable) {
        genTableService.validateEdit(genTable);
        genTableService.updateGenTable(genTable);
        return Result.success();
    }

    /**
     * 删除代码生成
     *
     * @param tableIds 表ID串
     */
    @Operation(summary = "删除代码生成")
    @SaCheckPermission("tool:gen:remove")
    @Log(title = "代码生成", businessType = BusinessType.DELETE)
    @DeleteMapping("/{tableIds}")
    public Result<Void> remove(@PathVariable Long[] tableIds) {
        genTableService.deleteGenTableByIds(tableIds);
        return Result.success();
    }

    /**
     * 预览代码
     *
     * @param tableId 表ID
     */
    @Operation(summary = "预览代码")
    @SaCheckPermission("tool:gen:preview")
    @GetMapping("/preview/{tableId}")
    public Result<Map<String, String>> preview(@PathVariable("tableId") Long tableId) throws IOException {
        Map<String, String> dataMap = genTableService.previewCode(tableId);
        return Result.success(dataMap);
    }

    /**
     * 生成代码（下载方式）
     *
     * @param tableName 表名
     */
    @Operation(summary = "生成代码（下载方式）")
    @SaCheckPermission("tool:gen:code")
    @Log(title = "代码生成", businessType = BusinessType.GENCODE)
    @GetMapping("/download/{tableName}")
    public void download(HttpServletResponse response, @PathVariable("tableName") String tableName) throws IOException {
        byte[] data = genTableService.downloadCode(tableName);
        genCode(response, data);
    }

    /**
     * 生成代码（自定义路径）
     *
     * @param tableName 表名
     */
    @Operation(summary = "生成代码（自定义路径）")
    @SaCheckPermission("tool:gen:code")
    @Log(title = "代码生成", businessType = BusinessType.GENCODE)
    @GetMapping("/genCode/{tableName}")
    public Result<Void> genCode(@PathVariable("tableName") String tableName) {
        genTableService.generatorCode(tableName);
        return Result.success();
    }

    /**
     * 同步数据库
     *
     * @param tableName 表名
     */
    @Operation(summary = "同步数据库")
    @SaCheckPermission("tool:gen:edit")
    @Log(title = "代码生成", businessType = BusinessType.UPDATE)
    @GetMapping("/synchDb/{tableName}")
    public Result<Void> synchDb(@PathVariable("tableName") String tableName) {
        genTableService.synchDb(tableName);
        return Result.success();
    }

    /**
     * 批量生成代码
     *
     * @param tables 表名串
     */
    @Operation(summary = "批量生成代码")
    @SaCheckPermission("tool:gen:code")
    @Log(title = "代码生成", businessType = BusinessType.GENCODE)
    @GetMapping("/batchGenCode")
    public void batchGenCode(HttpServletResponse response, String tables) throws IOException {
        String[] tableNames = Convert.toStrArray(tables);
        byte[] data = genTableService.downloadCode(tableNames);
        genCode(response, data);
    }

    /**
     * 生成zip文件
     */
    private void genCode(HttpServletResponse response, byte[] data) throws IOException {
        response.reset();
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Expose-Headers", "Content-Disposition");
        response.setHeader("Content-Disposition", "attachment; filename=\"divine.zip\"");
        response.addHeader("Content-Length", "" + data.length);
        response.setContentType("application/octet-stream; charset=UTF-8");
        IoUtil.write(response.getOutputStream(), false, data);
    }
}
