package cqdatasdk.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by godfather on 2016/3/24.
 * 输入关键词，搜索结果模型
 */
public class SearchResultBean implements Serializable{

    /**
     * code : 0
     * data : {"page":1,"size":5,"count":2,"totalPage":1,"list":[{"name":"刘德华","bigImg":"","smallImg":"","contentId":"868","contentType":"0"},{"name":"帘动荷风","bigImg":"","smallImg":"","contentId":"529","contentType":"2"}]}
     */

    private int code;
    /**
     * page : 1
     * size : 5
     * count : 2
     * totalPage : 1
     * list : [{"name":"刘德华","bigImg":"","smallImg":"","contentId":"868","contentType":"0"},{"name":"帘动荷风","bigImg":"","smallImg":"","contentId":"529","contentType":"2"}]
     */

    private DataBean data;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    private String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private int page;
        private int size;
        private int count;
        private int totalPage;
        /**
         * name : 刘德华
         * bigImg :
         * smallImg :
         * contentId : 868
         * contentType : 0
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

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public int getTotalPage() {
            return totalPage;
        }

        public void setTotalPage(int totalPage) {
            this.totalPage = totalPage;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {



            private String name;

            @Override
            public String toString() {
                return "ListBean{" +
                        "name='" + name + '\'' +
                        ", bigImg='" + bigImg + '\'' +
                        ", smallImg='" + smallImg + '\'' +
                        ", contentId='" + contentId + '\'' +
                        ", contentType='" + contentType + '\'' +
                        '}';
            }

            private String bigImg;
            private String smallImg;
            private String contentId;
            private String contentType;
            private String fileType;

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

            public String getContentId() {
                return contentId;
            }

            public void setContentId(String contentId) {
                this.contentId = contentId;
            }

            public String getContentType() {
                return contentType;
            }

            public void setContentType(String contentType) {
                this.contentType = contentType;
            }

            public String getFileType() {
                return fileType;
            }

            public void setFileType(String fileType) {
                this.fileType = fileType;
            }
        }
    }
}
