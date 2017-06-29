package cqdatasdk.bean;

import java.io.Serializable;
import java.util.List;


    /*{
        code: 0,
                data: {
        page: 1,
                size: 8,
                count: 1,
                totalPage: 1,
                list: [
        {
            id: "845",
                    albumId: "845",
                albumName: "极乐颂歌",
                bigImg: "6bccEDd18FCA66E9fa1dCBEE29BFED4F.jpg",
                smallImg: "DaB63203EB1B5e5FcD3C8Ccdf403fCFa.jpg"
        }
        ]
    }
    }*/

/**
 * 艺术家专辑列表
 * Created by Administrator on 2016/3/31.
 * http://172.16.146.56:8083/utvgoWeb/hifi/hifiData/albumListByArtis.action?artisId=18&pageno=1&size=8
 */
public class ArtistAlbumListBean implements Serializable{
    @Override
    public String toString() {
        return "ArtistAlbumListBean{" +
                "code=" + code +
                ", data=" + data +
                '}';
    }

    /**
     * code : 0
     * data : {"page":1,"size":8,"count":1,"totalPage":1,"list":[{"id":"845","albumId":"845","albumName":"极乐颂歌","bigImg":"6bccEDd18FCA66E9fa1dCBEE29BFED4F.jpg","smallImg":"DaB63203EB1B5e5FcD3C8Ccdf403fCFa.jpg"}]}
     */

    private int code;
    /**
     * page : 1
     * size : 8
     * count : 1
     * totalPage : 1
     * list : [{"id":"845","albumId":"845","albumName":"极乐颂歌","bigImg":"6bccEDd18FCA66E9fa1dCBEE29BFED4F.jpg","smallImg":"DaB63203EB1B5e5FcD3C8Ccdf403fCFa.jpg"}]
     */

    private DataBean data;

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

    public static class DataBean implements Serializable{
        @Override
        public String toString() {
            return "DataBean{" +
                    "page=" + page +
                    ", size=" + size +
                    ", count=" + count +
                    ", totalPage=" + totalPage +
                    ", list=" + list +
                    '}';
        }

        private int page;
        private int size;
        private int count;
        private int totalPage;
        /**
         * id : 845
         * albumId : 845
         * albumName : 极乐颂歌
         * bigImg : 6bccEDd18FCA66E9fa1dCBEE29BFED4F.jpg
         * smallImg : DaB63203EB1B5e5FcD3C8Ccdf403fCFa.jpg
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
            @Override
            public String toString() {
                return "ListBean{" +
                        "id='" + id + '\'' +
                        ", albumId='" + albumId + '\'' +
                        ", albumName='" + albumName + '\'' +
                        ", bigImg='" + bigImg + '\'' +
                        ", smallImg='" + smallImg + '\'' +
                        '}';
            }

            private String id;
            private String albumId;
            private String albumName;
            private String bigImg;
            private String smallImg;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getAlbumId() {
                return albumId;
            }

            public void setAlbumId(String albumId) {
                this.albumId = albumId;
            }

            public String getAlbumName() {
                return albumName;
            }

            public void setAlbumName(String albumName) {
                this.albumName = albumName;
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
