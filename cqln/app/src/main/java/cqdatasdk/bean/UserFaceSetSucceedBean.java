package cqdatasdk.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/5/17.
 *
 * 设置用户头像成功
 * http://192.168.34.81/utvgoWeb/hifi/hifiUser/updateUserImg.action?keyNo=9950000000321800&userImgIndex=0
 */
public class UserFaceSetSucceedBean implements Serializable {


    /**
     * code : 0
     * msg : 修改成功
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
