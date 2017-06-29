package cqdatasdk.bean;

import java.io.Serializable;

/**
 * Created by Godfather on 16/4/15.
 * <p/>
 * 新版订购接口   http://192.168.34.81/utvgoWeb/hifi/hifiUser/comboValid.action?keyNo=9950000002272843
 * <p/>
 * {
 * "code": 500,
 * "msg": "套餐校验发生异常：null"
 * }
 * <p/>
 * 订购开通10元包月HIFI音乐
 */
public class OrderMusicServerBean_ implements Serializable {


    /**
     * code : 500
     * msg : 套餐校验发生异常：null
     */

    private int code;
    private String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
