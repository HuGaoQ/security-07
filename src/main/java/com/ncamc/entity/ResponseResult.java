package com.ncamc.entity;


import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ResponseResult {

    /**
     * 状态码
     */
    private Integer code;

    /**
     * 错误信息
     */
    private String message;

    /**
     * 数据
     */
    private Object data;

    public ResponseResult() {
    }

    public ResponseResult(Integer code, Object data) {
        this.code = code;
        this.data = data;
    }

    public ResponseResult(Integer code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static ResponseResult error(String msg) {
        return ResponseResult.error(msg, null);
    }

    public static ResponseResult error(String msg, Object data) {
        return new ResponseResult(HttpStatus.INTERNAL_SERVER_ERROR.value(), msg, data);
    }

    public static ResponseResult error(int code, String msg) {
        return new ResponseResult(code, msg, null);
    }

}