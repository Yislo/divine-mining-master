package com.divine.system.domain.vo.home;

import lombok.Data;

/**
 * @Author: Yisl
 * @Description:
 * @Date: 2026/3/7 14:29
 */
@Data
public class AccessRecordInfoVO {

    /**
     * 出厂数量
     */
    private Long exitNum;

    /**
     * 滞留数量
     */
    private Long remainNum;

    @Data
    static class AccessRecord{

        /**
         * 进厂数量
         */
        private Long entryNum;

        /**
         * 进厂类型(0:其他,1:送货)
         */
        private Long entryType;

    }





}
