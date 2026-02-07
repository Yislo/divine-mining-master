package com.divine.weighing.service.impl;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.style.column.SimpleColumnWidthStyleStrategy;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.divine.common.core.domain.Result;
import com.divine.common.core.exception.base.BusinessException;
import com.divine.weighing.domain.dto.delivery.DeparturePageDTO;
import com.divine.weighing.domain.entity.Delivery;
import com.divine.weighing.export.DeliveryExportField;
import com.divine.weighing.export.ExportContext;
import com.divine.weighing.domain.dto.export.DeliveryExportDTO;
import com.divine.weighing.export.ExportField;
import com.divine.weighing.export.ExportFieldRegistry;
import com.divine.weighing.mapper.DeliveryMapper;
import com.divine.weighing.service.DeliveryService;
import com.divine.weighing.service.ExportService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * @Description: 送货记录业务层
 * @Author: yisl
 * @Date: 2025-11-21 11:05:10
 */
@Service
public class ExportServiceImpl extends ServiceImpl<DeliveryMapper, Delivery> implements ExportService {

    @Autowired
    private DeliveryService deliveryService;


    private static final int PAGE_SIZE = 5000;

    /**
     * 导出送货记录
     *
     * @param dto
     * @return
     */
    @SneakyThrows
    @Override
    public void exportDelivery(DeliveryExportDTO dto, HttpServletResponse response) {
        List<ExportField> fields = dto.getFields().stream()
                .map(ExportFieldRegistry::get)
                .filter(Objects::nonNull)
                .toList();
        if (fields.isEmpty()) {
            throw new BusinessException("未选择导出字段");
        }
        try {
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            String fileName = URLEncoder.encode("送货记录", StandardCharsets.UTF_8);
            response.setHeader(
                    "Content-Disposition",
                    "attachment; filename=" + fileName + ".xlsx"
            );
            List<List<String>> head = fields.stream().map(f -> List.of(f.header())).toList();

            ExcelWriter writer = EasyExcel
                    .write(response.getOutputStream())
                    .head(head)
                    .registerWriteHandler(new SimpleColumnWidthStyleStrategy(20))
                    .build();

            WriteSheet sheet = EasyExcel.writerSheet("送货记录").build();

            AtomicInteger rowNo = new AtomicInteger(1);

            int page = 1;
            DeparturePageDTO r = BeanUtil.copyProperties(dto, DeparturePageDTO.class);
            while (true) {
                r.setPageNum(page);
                r.setPageSize(PAGE_SIZE);
                // 查询所有数据
                IPage<DeliveryExportField> pageExp = deliveryService.selectExportList(r);
                List<List<Object>> data = new ArrayList<>();
                for (DeliveryExportField exportField : pageExp.getRecords()) {
                    ExportContext ctx = BeanUtil.copyProperties(exportField, ExportContext.class);
                    List<Object> row = new ArrayList<>();
                    for (ExportField field : fields) {
                        row.add(field.value(ctx));
                    }
                    data.add(row);
                }
                writer.write(data, sheet);
                // 如果没有数据则结束
                if (pageExp.getPages() <= page) break;
                page++;
            }
            writer.finish();
        } catch (Exception e) {
            log.error("导出失败", e);
            response.reset();
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(
                    JSONUtil.toJsonStr(Result.fail("导出失败"))
            );
        }
    }

}
