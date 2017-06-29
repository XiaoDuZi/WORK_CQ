package cqdatasdk.bean;

import java.io.Serializable;

/**
 * Created by godfather on 2016/3/23.
 */
public class DeletedCollectionSucceedBean implements Serializable {


    /**
     * code : 0
     * msg : 取消收藏成功
     */

    private String code;
    private String msg;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
