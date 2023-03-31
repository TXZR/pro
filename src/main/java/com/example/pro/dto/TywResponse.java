package com.example.pro.dto;

import lombok.Data;

@Data
public class TywResponse {
    private String code;
    private String message;
    private Object data;

    public TywResponse(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public TywResponse(Object data) {
        this("20000", "success");
        this.data = data;
    }

    public static TywResponse success(Object data) {
        return new TywResponse(data);
    }
    
    public static TywResponse fail(Exception e) {
        return new TywResponse("20001", "系统异常！");
    }
}
