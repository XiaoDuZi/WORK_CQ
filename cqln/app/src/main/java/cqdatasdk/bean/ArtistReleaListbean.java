package cqdatasdk.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by godfather on 2016/3/27.
 * 艺术家关联的列表数据
 */
public class ArtistReleaListbean implements Serializable {


    /**
     * code : 0
     * data : {"list":[{"id":1,"name":"test","bigImg":"/a/b/3.jpg","smallImg":"/a/b/3.jpg"}],"totalPage":2,"page":10,"total":20}
     */

    private String code;
    /**
     * list : [{"id":1,"name":"test","bigImg":"/a/b/3.jpg","smallImg":"/a/b/3.jpg"}]
     * totalPage : 2
     * page : 10
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

    public static class DataBean implements Serializable{
        private int totalPage;
        private int page;
        private int total;
        /**
         * id : 1
         * name : test
         * bigImg : /a/b/3.jpg
         * smallImg : /a/b/3.jpg
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
            private int id;
            private String name;
            private String bigImg;
            private String smallImg;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getBigImg() {
                return bigImg;
            }

            public void setBigImg(String bigImg) {
                this.bigImg = bigImg;
            }

            public String getSmallImg() {
                return smallImg;
            }

            public void setSmallImg(String smallImg) {
                this.smallImg = smallImg;
            }
        }
    }
}
