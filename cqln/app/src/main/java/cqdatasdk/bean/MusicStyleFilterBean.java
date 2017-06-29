package cqdatasdk.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by godfather on 2016/3/23.
 */
public class MusicStyleFilterBean implements Serializable {

    /**
     * code : 0
     * data : [{"name":"爵士","id":5},{"name":"流行","id":4},{"name":"民族","id":3},{"name":"古典","id":2},{"name":"HIFI","id":1}]
     */

    private String code;
    /**
     * name : 爵士
     * id : 5
     */

    private List<DataBean> data;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    private String msg;

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

    public static class DataBean implements Serializable{
        private String name;
        private int id;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }
}
