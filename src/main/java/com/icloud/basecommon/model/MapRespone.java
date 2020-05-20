package com.icloud.basecommon.model;

import java.util.HashMap;
import java.util.Map;

public class MapRespone extends HashMap<String, Object> {
    private static final long serialVersionUID = 1L;


    public MapRespone() {
        put("code", "0000");
        put("message", "success");
    }

    public static MapRespone error() {
    return error("500", "未知异常，请联系管理员");
}

    public static MapRespone error(String message) {
        return error("1000", message);
    }

    public static MapRespone error(String code, String message) {
        MapRespone r = new MapRespone();
        r.put("code", code);
        r.put("message", message);
        return r;
    }

    public static MapRespone ok(String message) {
        MapRespone r = new MapRespone();
        r.put("message", message);
        return r;
    }

    public static MapRespone ok(Map<String, Object> map) {
        MapRespone r = new MapRespone();
        r.putAll(map);
        return r;
    }

    public static MapRespone ok() {
        return new MapRespone();
    }

    @Override
    public MapRespone put(String key, Object value) {
        super.put(key, value);
        return this;
    }

}


