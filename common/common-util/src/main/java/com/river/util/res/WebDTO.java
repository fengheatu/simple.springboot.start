package com.river.util.res;

import java.util.HashMap;

/**
 * create by river  2018/5/16
 * desc:
 */
public class WebDTO<T> extends HashMap<String,Object> {

    private String resCode;
    private String resMsg;
    /**
     * 总记录数
     */
    private Long total;
    private Integer totalPages;
    private T data;

    public String getResCode() {
        return resCode;

    }

    public void setResCode(String resCode) {
        this.resCode = resCode;
        put("resCode",resCode);
    }

    public String getResMsg() {
        return resMsg;
    }

    public void setResMsg(String resMsg) {
        this.resMsg = resMsg;
        put("resMsg",resMsg);
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
        put("total",total);
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
        put("totalPages",totalPages);
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
        put("data",data);
    }

    public void setResEnum(ResCodeEnum resCodeEnum) {
        setResCode(resCodeEnum.getResCode());
        setResMsg(resCodeEnum.getResMsg());
    }
}
