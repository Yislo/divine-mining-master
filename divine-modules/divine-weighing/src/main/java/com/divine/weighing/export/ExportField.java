package com.divine.weighing.export;

public interface ExportField {

    /** 前端字段 key */
    String key();

    /** Excel 表头 */
    String header();

    /** 从上下文中取值 */
    Object value(ExportContext ctx);
}
