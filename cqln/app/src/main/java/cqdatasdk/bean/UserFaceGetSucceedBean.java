package cqdatasdk.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/5/17.
 * 获取用户头像成功
 *
 * http://192.168.34.81/utvgoWeb/hifi/hifiUser/getUserImg.action?keyNo=9950000000321800
 */
public class UserFaceGetSucceedBean implements Serializable {


    /**
     * code : 0
     * userImg : 5
     * balance : 0.0
     * userIntegral : 305
     * msg : 获取成功
     */

    private String code;
    private int userImg;
    private String balance;
    private int userIntegral;
    private String msg;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getUserImg() {
        return userImg;
    }

    public void setUserImg(int userImg) {
        this.userImg = userImg;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public int getUserIntegral() {
        return userIntegral;
    }

    public void setUserIntegral(int userIntegral) {
        this.userIntegral = userIntegral;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
