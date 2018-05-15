package com.river.util.res;

/**
 * create by river  2018/5/15
 * desc:
 */
public enum  ResCodeEnum {
    SYS_SUCCESS("000000","成功"),
    sys_error("0000001","系统异常");
    private String resCode;
    private String resMsg;

   ResCodeEnum(String resCode,String resMsg) {
        this.resCode = resCode;
        this.resMsg = resMsg;
    }

    public String getResCode() {
        return resCode;
    }

    public void setResCode(String resCode) {
        this.resCode = resCode;
    }

    public String getResMsg() {
        return resMsg;
    }

    public void setResMsg(String resMsg) {
        this.resMsg = resMsg;
    }
}
