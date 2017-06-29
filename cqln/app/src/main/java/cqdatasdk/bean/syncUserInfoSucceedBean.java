package cqdatasdk.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 同步用户信息成功，返回数据，解析订购信息
 * <p>
 * status //0 已订购  1 未订购
 * <p>
 * http://192.168.34.81/utvgoWeb/hifi/hifiUser/getUserInfo.action?keyNo=9950000001855937
 * <p>
 * Created by Administrator on 2016/7/6.
 */
public class syncUserInfoSucceedBean implements Serializable {


    /**
     *
     * status : 1  //0 已订购  1 未订购
     * errors : []
     * result : {"custId":"1122336","totalFee":0.0,"totalBalance":24.07,"expireDate":"2099-12-31 23:59:59","validDate":"2016-07-11 10:43:22","msg":"已经订购","userImg":3}
     * extra : {}
     */

    private int status;
    /**
     * custId : 1122336
     * totalFee : 25.93
     * totalBalance : 0
     * msg : 没有订购
     * userImg : 4
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
        private int userImg;
        private String expireDate;
        private String validDate;

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

        public void setTotalBalance(double totalBalance) {
            this.totalBalance = totalBalance;
        }

        public double getTotalBalance() {
            return totalBalance;
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

        public String getExpireDate() {
            return expireDate;
        }

        public void setExpireDate(String expireDate) {
            this.expireDate = expireDate;
        }

        public String getValidDate() {
            return validDate;
        }

        public void setValidDate(String validDate) {
            this.validDate = validDate;
        }
    }

    public static class ExtraBean {
    }
}
