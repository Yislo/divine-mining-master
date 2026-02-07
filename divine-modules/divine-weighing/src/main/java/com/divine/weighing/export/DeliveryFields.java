package com.divine.weighing.export;

import cn.hutool.core.util.ObjUtil;

import java.math.BigDecimal;
import java.math.RoundingMode;


public enum DeliveryFields implements ExportField {

    /* ===== 表字段 ===== */

    WEIGHT_NO {
        public String key() { return "weightNo"; }
        public String header() { return "过磅编号"; }
        public Object value(ExportContext ctx) {
            return ctx.getWeightNo();
        }
    },

    CAR_NUMBER {
        public String key() { return "carNumber"; }
        public String header() { return "车牌号"; }
        public Object value(ExportContext ctx) {
            return ctx.getCarNumber();
        }
    },

    COMPANY_NAME {
        public String key() { return "companyName"; }
        public String header() { return "工厂名称"; }
        public Object value(ExportContext ctx) {
            return ctx.getCompanyName();
        }
    },

    MOTORCADE_NAME {
        public String key() { return "motorcadeName"; }
        public String header() { return "车队名称"; }
        public Object value(ExportContext ctx) {
            return ctx.getMotorcadeName();
        }
    },

    STATUS_TEXT {
        public String key() { return "statusText"; }
        public String header() { return "送货状态"; }
        public Object value(ExportContext ctx) {
            return ctx.getStatusText();
        }
    },

    ANTI_FAKE_CODE {
        public String key() { return "antiFakeCode"; }
        public String header() { return "防伪码"; }
        public Object value(ExportContext ctx) {
            return ctx.getAntiFakeCode();
        }
    },

    SETTLE_PRICE {
        public String key() { return "settlePrice"; }
        public String header() { return "组货结算运费($)"; }
        public Object value(ExportContext ctx) {
            return ctx.getSettlePrice();
        }
    },

    SETTLE_TIME {
        public String key() { return "settleTime"; }
        public String header() { return "组货结算时间"; }
        public Object value(ExportContext ctx) {
            return ctx.getSettleTime();
        }
    },

    TICKET_TIME {
        public String key() { return "ticketTime"; }
        public String header() { return "开票时间"; }
        public Object value(ExportContext ctx) {
            return ctx.getTicketTime();
        }
    },

    ACCREDIT_TIME {
        public String key() { return "accreditTime"; }
        public String header() { return "授权日期"; }
        public Object value(ExportContext ctx) {
            return ctx.getAccreditTime();
        }
    },

    DEPARTURE_TIME {
        public String key() { return "departureTime"; }
        public String header() { return "发车时间"; }
        public Object value(ExportContext ctx) {
            return ctx.getDepartureTime();
        }
    },

    WEIGHING_TIME {
        public String key() { return "weighingTime"; }
        public String header() { return "过磅时间"; }
        public Object value(ExportContext ctx) {
            return ctx.getWeighingTime();
        }
    },

    TOTAL_WEIGHT {
        public String key() { return "totalWeight"; }
        public String header() { return "总重(kg)"; }
        public Object value(ExportContext ctx) {
            return ctx.getTotalWeight();
        }
    },

    EMPTY_WEIGHT {
        public String key() { return "emptyWeight"; }
        public String header() { return "空重(kg)"; }
        public Object value(ExportContext ctx) {
            return ctx.getEmptyWeight();
        }
    },

    NET_WEIGHT {
        public String key() { return "netWeight"; }
        public String header() { return "净重(kg)"; }
        public Object value(ExportContext ctx) {
            return ctx.getNetWeight();
        }
    },

    MOISTURE {
        public String key() { return "moisture"; }
        public String header() { return "水份"; }
        public Object value(ExportContext ctx) {
            BigDecimal moisture = ctx.getMoisture();
            if (ObjUtil.isNull(moisture)){
                return null;
            }
            return moisture+"%";
        }
    },

    CUO_RATIO {
        public String key() { return "cuoRatio"; }
        public String header() { return "氧化铜品位"; }
        public Object value(ExportContext ctx) {
            BigDecimal cuoRatio = ctx.getCuoRatio();
            if (ObjUtil.isNull(cuoRatio)){
                return null;
            }
            return cuoRatio+"%";
        }
    },

    ACID_DEMAND {
        public String key() { return "acidDemand"; }
        public String header() { return "酸耗"; }
        public Object value(ExportContext ctx) {
            return ctx.getAcidDemand();
        }
    },


    GOLD_TON {
        public String key() { return "goldTon"; }
        public String header() { return "金吨(kg)"; }
        public Object value(ExportContext ctx) {
            // 计算金吨 金吨=净重*（100-水）/100*氧化铜/100
            Integer netWeight =ctx.getNetWeight();
            BigDecimal moisture = ctx.getMoisture();
            BigDecimal cuoRatio = ctx.getCuoRatio();
            if (ObjUtil.isNotNull(netWeight)
                    && ObjUtil.isNotNull(moisture)
                    && ObjUtil.isNotNull(cuoRatio)) {
                return new BigDecimal(netWeight)
                        .multiply(BigDecimal.valueOf(100).subtract(moisture))
                        .multiply(cuoRatio)
                        .divide(BigDecimal.valueOf(10000), 2, RoundingMode.HALF_UP);
            }else {
                return null;
            }
        }
    }



}
