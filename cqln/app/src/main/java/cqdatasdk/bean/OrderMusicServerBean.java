package cqdatasdk.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Godfather on 16/4/15.
 * 订购开通10元包月HIFI音乐
 */
public class OrderMusicServerBean implements Serializable {


    /**
     * status : 0
     * errors : []
     * result : {"msg":"SUCCESS"}
     * extra : {}
     */

    private int status;
    /**
     * msg : SUCCESS
     */

    private ResultBean result;
    private ExtraBean extra;
    private List<?> errors;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public ExtraBean getExtra() {
        return extra;
    }

    public void setExtra(ExtraBean extra) {
        this.extra = extra;
    }

    public List<?> getErrors() {
        return errors;
    }

    public void setErrors(List<?> errors) {
        this.errors = errors;
    }

    public static class ResultBean {
        private String msg;

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }

    public static class ExtraBean {
    }
}
