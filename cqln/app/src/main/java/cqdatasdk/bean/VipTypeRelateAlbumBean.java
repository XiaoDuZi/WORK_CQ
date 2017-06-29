package cqdatasdk.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/5/30.
 * 新歌抢先听 列表关联的专辑列表
 * http://192.168.34.81/utvgoWeb/hifi/hifiData/albumList.action?styleId=8&pageno=1&size=8
 */
public class VipTypeRelateAlbumBean {


    /**
     * code : 0
     * data : {"list":[{"name":"芭芭拉·史翠珊精选","id":6,"smallImg":"/utvgo_small/6600b47dbbc023c16fe86fc1b053c309.jpg","bigImg":"/utvgo_big/27e6b168f706511b4573cb72402bd43d.jpg","createTime":"2016-03-23"},{"name":"悲情城市 电影原声大碟","id":7,"smallImg":"/utvgo_small/8f099606812d2070a84bea06b55a7745.jpg","bigImg":"/utvgo_big/050ce69004f66c75f2e56e08a0d03d7c.jpg","createTime":"2016-03-23"},{"name":"贝拉芬堤在卡耐基大厅","id":18,"smallImg":"/utvgo_small/5afa5ddd324fa191bf10a5ff58798f33.jpg","bigImg":"/utvgo_big/5afa5ddd324fa191bf10a5ff58798f33.jpg","createTime":"2016-03-23"}],"totalPage":1,"page":1,"total":3}
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
     * list : [{"name":"芭芭拉·史翠珊精选","id":6,"smallImg":"/utvgo_small/6600b47dbbc023c16fe86fc1b053c309.jpg","bigImg":"/utvgo_big/27e6b168f706511b4573cb72402bd43d.jpg","createTime":"2016-03-23"},{"name":"悲情城市 电影原声大碟","id":7,"smallImg":"/utvgo_small/8f099606812d2070a84bea06b55a7745.jpg","bigImg":"/utvgo_big/050ce69004f66c75f2e56e08a0d03d7c.jpg","createTime":"2016-03-23"},{"name":"贝拉芬堤在卡耐基大厅","id":18,"smallImg":"/utvgo_small/5afa5ddd324fa191bf10a5ff58798f33.jpg","bigImg":"/utvgo_big/5afa5ddd324fa191bf10a5ff58798f33.jpg","createTime":"2016-03-23"}]
     * totalPage : 1
     * page : 1
     * total : 3
     */

    private DataBean data;

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
         * name : 芭芭拉·史翠珊精选
         * id : 6
         * smallImg : /utvgo_small/6600b47dbbc023c16fe86fc1b053c309.jpg
         * bigImg : /utvgo_big/27e6b168f706511b4573cb72402bd43d.jpg
         * createTime : 2016-03-23
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
            private String createTime;

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

            public String getCreateTime() {
                return createTime;
            }

            public void setCreateTime(String createTime) {
                this.createTime = createTime;
            }
        }
    }
}
