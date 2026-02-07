package com.divine.weighing.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.divine.weighing.domain.entity.Delivery;
import com.divine.weighing.domain.dto.export.DeliveryExportDTO;
import jakarta.servlet.http.HttpServletResponse;

/**
 * @Description: 送货记录业务层
 * @Author: yisl
 * @Date: 2025-11-21 11:05:10
 */
public interface ExportService extends IService<Delivery> {

    /**
     * 导出送货记录
     *
     * @param dto
     * @return
     */
    void exportDelivery(DeliveryExportDTO dto, HttpServletResponse response);

}
