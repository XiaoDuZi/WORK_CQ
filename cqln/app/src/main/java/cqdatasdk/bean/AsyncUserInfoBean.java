package cqdatasdk.bean;

import java.util.List;

/**
 * Created by Godfather on 16/4/25.
 */
public class AsyncUserInfoBean {

    /**
     * status : 1
     * errors : []
     * result : {"custId":"3816815","totalFee":59.05,"totalBalance":0,"msg":"没有订购","userImg":1}
     * extra : {}
     */

    private int status;
    /**
     * custId : 3816815
     * totalFee : 59.05
     * totalBalance : 0
     * msg : 没有订购
     * userImg : 1
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
        private int totalBalance;
        private String msg;
        private int userImg;

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

        public int getTotalBalance() {
            return totalBalance;
        }

        public void setTotalBalance(int totalBalance) {
            this.totalBalance = totalBalance;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public int getUserImg() {
            return userImg;
        }

        public void setUserImg(int userImg) {
            this.userImg = userImg;
        }
    }

    public static class ExtraBean {
    }
}
