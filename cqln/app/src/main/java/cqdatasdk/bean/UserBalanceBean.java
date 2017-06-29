package cqdatasdk.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Godfather on 16/4/15.
 * 查询用户金额
 */
public class UserBalanceBean implements Serializable {


    /**
     * status : 1
     * errors : []
     * result : {"custId":"7784881","totalFee":0,"totalBalance":44.12,"msg":"没有订购"}
     * extra : {}
     */

    private int status;
    /**
     * custId : 7784881
     * totalFee : 0.0
     * totalBalance : 44.12
     * msg : 没有订购
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
        private String custId;
        private double totalFee;
        private double totalBalance;
        private String msg;

        public String getCustId() {
            return custId;
        }

        public void setCustId(String custId) {
            this.custId = custId;
        }

        public double getTotalFee() {
            return totalFee;
        }

        public void setTotalFee(double totalFee) {
            this.totalFee = totalFee;
        }

        public double getTotalBalance() {
            return totalBalance;
        }

        public void setTotalBalance(double totalBalance) {
            this.totalBalance = totalBalance;
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
