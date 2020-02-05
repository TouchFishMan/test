package com.telek.hemsipc.util;

import lombok.Data;

import java.io.Serializable;

/**
 * @author wangxb
 * @date 19-8-8 上午9:37
 */
@Data
public class JsonResult implements Serializable {

    private static final long serialVersionUID = 5334140199765492567L;

    /**
     * 操作是否成功标志
     */
    protected boolean success;

    /**
     * 返回状态码
     */
    protected int code;

    /**
     * 返回数据
     */
    protected Object data;


    public JsonResult(boolean _success, int _code, Object _data) {
        this.success = _success;
        this.code = _code;
        this.data = _data;
    }

    public JsonResult(boolean _success) {
        this(_success, 0, null);
    }

    public JsonResult(boolean _success, int _code) {
        this(_success, _code, null);
    }

}

