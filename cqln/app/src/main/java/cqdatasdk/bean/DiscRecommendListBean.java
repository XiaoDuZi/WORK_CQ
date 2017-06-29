package cqdatasdk.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by godfather on 2016/3/26.
 * 唱片推荐按分类刷选的列表数据模型
 */
public class DiscRecommendListBean implements Serializable{

    /**
     * code : 0
     * data : {"page":1,"size":10,"totalPage":2,"count":20,"list":[{"id":1,"name":"测试","bigImg":"/a/b/3.jpg","smallImg":"/a/b/3.jpg"}]}
     * msg :
     */

    private String code;
    /**
     * page : 1
     * size : 10
     * totalPage : 2
     * count : 20
     * list : [{"id":1,"name":"测试","bigImg":"/a/b/3.jpg","smallImg":"/a/b/3.jpg"}]
     */

    private DataBean data;
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

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static class DataBean {
        private int page;
        private int size;
        private int totalPage;
        private int count;
        /**
         * id : 1
         * name : 测试
         * bigImg : /a/b/3.jpg
         * smallImg : /a/b/3.jpg
         */

        private List<ListBean> list;

        public int getPage() {
            return page;
        }

        public void setPage(int page) {
            this.page = page;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public int getTotalPage() {
            return totalPage;
        }

        public void setTotalPage(int totalPage) {
            this.totalPage = totalPage;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
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
            private int fileType;

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

            public int getFileType() {
                return fileType;
            }

            public void setFileType(int fileType) {
                this.fileType = fileType;
            }
        }
    }
}
