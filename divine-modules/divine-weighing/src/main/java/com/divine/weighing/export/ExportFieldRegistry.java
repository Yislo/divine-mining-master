package com.divine.weighing.export;

import java.util.HashMap;
import java.util.Map;

public class ExportFieldRegistry {

    private static final Map<String, ExportField> FIELD_MAP = new HashMap<>();

    static {
        register(DeliveryFields.values());
    }

    public static void register(ExportField[] fields) {
        for (ExportField field : fields) {
            FIELD_MAP.put(field.key(), field);
        }
    }

    public static ExportField get(String key) {
        return FIELD_MAP.get(key);
    }
}
