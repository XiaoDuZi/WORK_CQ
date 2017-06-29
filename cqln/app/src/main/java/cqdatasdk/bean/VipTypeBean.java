package cqdatasdk.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/5/30.
 * 新歌抢先听列表
 * http://192.168.34.81/utvgoWeb/hifi/hifiUser/getVipType.action?keyNo=9950000002384049
 */
public class VipTypeBean {


    /**
     * code : 0
     * data : [{"id":8,"styleName":"全球经典","creatDate":"2016-05-24"},{"id":7,"styleName":"发烧唱片","creatDate":"2016-05-24"},{"id":6,"styleName":"榜中榜","creatDate":"2016-05-24"}]
     */

    private String code;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    private String msg;

    /**
     * id : 8
     * styleName : 全球经典
     * creatDate : 2016-05-24
     */

    private List<DataBean> data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        private int id;
        private String styleName;
        private String creatDate;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getStyleName() {
            return styleName;
        }

        public void setStyleName(String styleName) {
            this.styleName = styleName;
        }

        public String getCreatDate() {
            return creatDate;
        }

        public void setCreatDate(String creatDate) {
            this.creatDate = creatDate;
        }
    }
}
