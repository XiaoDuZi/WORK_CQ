package cqdatasdk.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by godfather on 2016/3/27.
 * 音乐风格列表数据
 */
public class MusicStyleListBean implements Serializable{

    /**
     * code : 0
     * data : {"list":[{"name":"test","id":1,"smallImg":"/a/b/3.jpg","bigImg":"/a/b/3.jpg"}],"totalPage":2,"page":1,"total":20}
     */

    private String code;
    /**
     * list : [{"name":"test","id":1,"smallImg":"/a/b/3.jpg","bigImg":"/a/b/3.jpg"}]
     * totalPage : 2
     * page : 1
     * total : 20
     */

    private DataBean data;

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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private int totalPage;
        private int page;
        private int total;
        /**
         * name : test
         * id : 1
         * smallImg : /a/b/3.jpg
         * bigImg : /a/b/3.jpg
         */

        private List<ListBean> list;

        public int getTotalPage() {
            return totalPage;
        }

        public void setTotalPage(int totalPage) {
            this.totalPage = totalPage;
        }

        public int getPage() {
            return page;
        }

        public void setPage(int page) {
            this.page = page;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            private String name;
            private int id;
            private String smallImg;
            private String bigImg;

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

            public String getSmallImg() {
                return smallImg;
            }

            public void setSmallImg(String smallImg) {
                this.smallImg = smallImg;
            }

            public String getBigImg() {
                return bigImg;
            }

            public void setBigImg(String bigImg) {
                this.bigImg = bigImg;
            }
        }
    }
}
