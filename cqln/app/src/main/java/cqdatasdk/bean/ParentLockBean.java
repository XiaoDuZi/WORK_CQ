package cqdatasdk.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Godfather on 16/4/15.
 */
public class ParentLockBean implements Serializable {


    /**
     * status : 0
     * errors : []
     * result : {"lockerPass":"123456","code":"200","msg":"SUCCESS"}
     * extra : {}
     */

    private int status;
    /**
     * lockerPass : 123456
     * code : 200
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
        private String lockerPass;
        private String code;
        private String msg;

        public String getLockerPass() {
            return lockerPass;
        }

        public void setLockerPass(String lockerPass) {
            this.lockerPass = lockerPass;
        }

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

    public static class ExtraBean {
    }
}
