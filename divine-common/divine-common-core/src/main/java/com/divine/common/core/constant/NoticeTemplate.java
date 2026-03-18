package com.divine.common.core.constant;

/**
 * @Author: Yisl
 * @Description:
 * @Date: 2026/3/9 10:21
 */
public class NoticeTemplate {
    public static final String SYSTEMATIC_NOTIFICATION= "系统通知";

    /**
     * 构建库存预警通知内容
     */
    public static String buildStockWarning(String itemName, String skuNo, String warehouse,
                                           Long currentStock, Long safeStock, String unit, String time) {
        return String.format(
            STOCK_WARNING_TPL,
            itemName,
            skuNo,
            warehouse,
            currentStock, unit,
            safeStock, unit,
            time
        );
    }

    /**
     * 库存预警 HTML 模板
     * 使用 %s 占位符，方便 String.format 调用
     */
    private static final String STOCK_WARNING_TPL =
        "<p>⚠\uFE0F 【库存预警通知】</p>" +
            "<p><br></p>" +
            "<p>系统检测到以下商品库存已低于安全库存线，请及时安排补货：</p>" +
            "<p><br></p>" +
            "<p>\uD83D\uDCE6 物品名称：%s</p>" +
            "<p><strong style=\"background-color: rgba(0, 0, 0, 0);\">\uD83C\uDFF7\uFE0F" +
            "</strong> SKU编码：%s</p>" +
            "<p>\uD83D\uDCCD 所在仓库：%s</p>" +
            "<p>\uD83D\uDCCA 当前库存：%d %s</p>" +
            "<p>⚠\uFE0F 安全库存：%d %s</p>" +
            "<p>⏰ 预警时间：%s</p>" +
            "<p><br></p>" +
            "<p>请尽快处理！</p>";


}
