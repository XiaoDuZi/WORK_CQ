package cqdatasdk.bean;

import java.io.Serializable;

/**
 *
 * 新版家长锁接口返回数据
 *
 *http://192.168.34.81/utvgoWeb/hifi/hifiUser/userLock.action?keyNo=9950000002272843&dealType=1


 {
 "code": "500",
 "msg": "请求参数中custId为空"

 }



 {
 "code": "200",
 "passwd": "123456"
 }




 * Created by Godfather on 16/4/15.
 */
public class ParentLockBean_ implements Serializable {


    /**
     * code : 500
     * msg : 请求参数中custId为空
     * passwd : 123456
     */

    private String code;
    private String msg;
    private String passwd;

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

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }
}
