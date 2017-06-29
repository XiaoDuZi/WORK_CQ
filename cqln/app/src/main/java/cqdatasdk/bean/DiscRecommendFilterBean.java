package cqdatasdk.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by godfather on 2016/3/23.
 * 唱片推荐刷选分类列表
 */
public class DiscRecommendFilterBean implements Serializable {


    /**
     * code : 0
     * data : [{"name":"流行","code":"liuxing_2618","id":2618},{"name":"古典","code":"gudian_2620","id":2620},{"name":"爵士","code":"jueshi_2621","id":2621},{"name":"民族","code":"minzu_2622","id":2622},{"name":"Hi-Fi","code":"hi-fi_2687","id":2687},{"name":"免费区","code":"mianfeiqu_2697","id":2697}]
     */

    private String code;
    /**
     * name : 流行
     * code : liuxing_2618
     * id : 2618
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
        private String code;
        private int id;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }
}
