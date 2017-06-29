package cqdatasdk.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Godfather on 16/4/22.
 * 收藏的专辑
 * http://192.168.34.81/utvgoWeb/hifi/hifiUser/userCollection.action?keyNo=8002003646694252&pageno=1&size=8&collectionType=1
 */
public class CollectionSpecailBean implements Serializable {


    /**
     * code : 0
     * data : {"page":1,"size":8,"count":31,"totalPage":4,"list":{"cacheKey":"-1-8","count":31,"offset":0,"pageNo":1,"pageSize":8,"result":[{"collectionTime":"2016-04-14 10:31:21.0","id":"210","albumName":"献给你的歌，卡伦","albumId":"510","smallImg":"/utvgo_small/0eEeBcdF4A75CdEd96dfaEC7520FAf33.jpg","bigImg":"/utvgo_big/3B355E98B46680202216Cadfcc36F76b.jpg"},{"collectionTime":"2016-04-14 10:29:34.0","id":"209","albumName":"莫文蔚 X","albumId":"258","smallImg":"/utvgo_small/2cabd8403dd9d4bbabe5911ecd380360.jpg","bigImg":"/utvgo_big/48b64681234db49824e17090c9aaa614.jpg"},{"collectionTime":"2016-04-01 10:27:34.0","id":"115","albumName":"The Other Shore","albumId":"47","smallImg":"/utvgo_small/cb4d507035f7623d4f1c4542e8bbf2a6.jpg","bigImg":"/utvgo_big/ff1510c0830608bbc322c2b7f47e34b1.jpg"},{"collectionTime":"2016-03-29 15:23:19.0","id":"103","albumName":"绿袖子","albumId":"778","smallImg":"/utvgo_small/2BEB6adbcdEF57d0e8399bf3A53B6F9D.jpg","bigImg":"/utvgo_big/311a33FFf0dBeA57FF6fb36AB446AACD.jpg"},{"collectionTime":"2016-03-28 15:30:02.0","id":"102","albumName":"机遇","albumId":"1","smallImg":"/utvgo_small/f3f4c96c656733f9f55da25c4c6ac175.jpg","bigImg":"/utvgo_big/153c2559cf22a62f1b2bc235c2752f2c.jpg"},{"collectionTime":"2016-03-25 16:01:36.0","id":"99","albumName":"1987-1997雨果CD大全","albumId":"1000","smallImg":"/utvgo_small/3FAaAD01Ea039c76648773E7Fab9aAdA.jpg","bigImg":"/utvgo_big/d97a29bd6FB1e579dE8d7e914ccA49a9.jpg"},{"collectionTime":"2016-03-25 15:39:48.0","id":"98","albumName":"王妃","albumId":"256","smallImg":"/utvgo_small/5ec064afbc98eccda1807528ff8adc78.jpg","bigImg":"/utvgo_big/0b5bfc68d7333c5d9c5cff8878b74307.jpg"},{"collectionTime":"2016-03-23 16:49:42.0","id":"78","albumName":"到金星一游","albumId":"123","smallImg":"/utvgo_small/86656c82195265048e49de2114a0ebc2.jpg","bigImg":"/utvgo_big/6384a92f77023178ee93ba033fb0ba60.jpg"}],"totalPage":4}}
     * msg
     */

    private int code;
    /**
     * page : 1
     * size : 8
     * count : 31
     * totalPage : 4
     * list : {"cacheKey":"-1-8","count":31,"offset":0,"pageNo":1,"pageSize":8,"result":[{"collectionTime":"2016-04-14 10:31:21.0","id":"210","albumName":"献给你的歌，卡伦","albumId":"510","smallImg":"/utvgo_small/0eEeBcdF4A75CdEd96dfaEC7520FAf33.jpg","bigImg":"/utvgo_big/3B355E98B46680202216Cadfcc36F76b.jpg"},{"collectionTime":"2016-04-14 10:29:34.0","id":"209","albumName":"莫文蔚 X","albumId":"258","smallImg":"/utvgo_small/2cabd8403dd9d4bbabe5911ecd380360.jpg","bigImg":"/utvgo_big/48b64681234db49824e17090c9aaa614.jpg"},{"collectionTime":"2016-04-01 10:27:34.0","id":"115","albumName":"The Other Shore","albumId":"47","smallImg":"/utvgo_small/cb4d507035f7623d4f1c4542e8bbf2a6.jpg","bigImg":"/utvgo_big/ff1510c0830608bbc322c2b7f47e34b1.jpg"},{"collectionTime":"2016-03-29 15:23:19.0","id":"103","albumName":"绿袖子","albumId":"778","smallImg":"/utvgo_small/2BEB6adbcdEF57d0e8399bf3A53B6F9D.jpg","bigImg":"/utvgo_big/311a33FFf0dBeA57FF6fb36AB446AACD.jpg"},{"collectionTime":"2016-03-28 15:30:02.0","id":"102","albumName":"机遇","albumId":"1","smallImg":"/utvgo_small/f3f4c96c656733f9f55da25c4c6ac175.jpg","bigImg":"/utvgo_big/153c2559cf22a62f1b2bc235c2752f2c.jpg"},{"collectionTime":"2016-03-25 16:01:36.0","id":"99","albumName":"1987-1997雨果CD大全","albumId":"1000","smallImg":"/utvgo_small/3FAaAD01Ea039c76648773E7Fab9aAdA.jpg","bigImg":"/utvgo_big/d97a29bd6FB1e579dE8d7e914ccA49a9.jpg"},{"collectionTime":"2016-03-25 15:39:48.0","id":"98","albumName":"王妃","albumId":"256","smallImg":"/utvgo_small/5ec064afbc98eccda1807528ff8adc78.jpg","bigImg":"/utvgo_big/0b5bfc68d7333c5d9c5cff8878b74307.jpg"},{"collectionTime":"2016-03-23 16:49:42.0","id":"78","albumName":"到金星一游","albumId":"123","smallImg":"/utvgo_small/86656c82195265048e49de2114a0ebc2.jpg","bigImg":"/utvgo_big/6384a92f77023178ee93ba033fb0ba60.jpg"}],"totalPage":4}
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
         * cacheKey : -1-8
         * count : 31
         * offset : 0
         * pageNo : 1
         * pageSize : 8
         * result : [{"collectionTime":"2016-04-14 10:31:21.0","id":"210","albumName":"献给你的歌，卡伦","albumId":"510","smallImg":"/utvgo_small/0eEeBcdF4A75CdEd96dfaEC7520FAf33.jpg","bigImg":"/utvgo_big/3B355E98B46680202216Cadfcc36F76b.jpg"},{"collectionTime":"2016-04-14 10:29:34.0","id":"209","albumName":"莫文蔚 X","albumId":"258","smallImg":"/utvgo_small/2cabd8403dd9d4bbabe5911ecd380360.jpg","bigImg":"/utvgo_big/48b64681234db49824e17090c9aaa614.jpg"},{"collectionTime":"2016-04-01 10:27:34.0","id":"115","albumName":"The Other Shore","albumId":"47","smallImg":"/utvgo_small/cb4d507035f7623d4f1c4542e8bbf2a6.jpg","bigImg":"/utvgo_big/ff1510c0830608bbc322c2b7f47e34b1.jpg"},{"collectionTime":"2016-03-29 15:23:19.0","id":"103","albumName":"绿袖子","albumId":"778","smallImg":"/utvgo_small/2BEB6adbcdEF57d0e8399bf3A53B6F9D.jpg","bigImg":"/utvgo_big/311a33FFf0dBeA57FF6fb36AB446AACD.jpg"},{"collectionTime":"2016-03-28 15:30:02.0","id":"102","albumName":"机遇","albumId":"1","smallImg":"/utvgo_small/f3f4c96c656733f9f55da25c4c6ac175.jpg","bigImg":"/utvgo_big/153c2559cf22a62f1b2bc235c2752f2c.jpg"},{"collectionTime":"2016-03-25 16:01:36.0","id":"99","albumName":"1987-1997雨果CD大全","albumId":"1000","smallImg":"/utvgo_small/3FAaAD01Ea039c76648773E7Fab9aAdA.jpg","bigImg":"/utvgo_big/d97a29bd6FB1e579dE8d7e914ccA49a9.jpg"},{"collectionTime":"2016-03-25 15:39:48.0","id":"98","albumName":"王妃","albumId":"256","smallImg":"/utvgo_small/5ec064afbc98eccda1807528ff8adc78.jpg","bigImg":"/utvgo_big/0b5bfc68d7333c5d9c5cff8878b74307.jpg"},{"collectionTime":"2016-03-23 16:49:42.0","id":"78","albumName":"到金星一游","albumId":"123","smallImg":"/utvgo_small/86656c82195265048e49de2114a0ebc2.jpg","bigImg":"/utvgo_big/6384a92f77023178ee93ba033fb0ba60.jpg"}]
         * totalPage : 4
         */

        private ListBean list;

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

        public ListBean getList() {
            return list;
        }

        public void setList(ListBean list) {
            this.list = list;
        }

        public static class ListBean {
            private String cacheKey;
            private int count;
            private int offset;
            private int pageNo;
            private int pageSize;
            private int totalPage;
            /**
             * collectionTime : 2016-04-14 10:31:21.0
             * id : 210
             * albumName : 献给你的歌，卡伦
             * albumId : 510
             * smallImg : /utvgo_small/0eEeBcdF4A75CdEd96dfaEC7520FAf33.jpg
             * bigImg : /utvgo_big/3B355E98B46680202216Cadfcc36F76b.jpg
             */

            private List<ResultBean> result;

            public String getCacheKey() {
                return cacheKey;
            }

            public void setCacheKey(String cacheKey) {
                this.cacheKey = cacheKey;
            }

            public int getCount() {
                return count;
            }

            public void setCount(int count) {
                this.count = count;
            }

            public int getOffset() {
                return offset;
            }

            public void setOffset(int offset) {
                this.offset = offset;
            }

            public int getPageNo() {
                return pageNo;
            }

            public void setPageNo(int pageNo) {
                this.pageNo = pageNo;
            }

            public int getPageSize() {
                return pageSize;
            }

            public void setPageSize(int pageSize) {
                this.pageSize = pageSize;
            }

            public int getTotalPage() {
                return totalPage;
            }

            public void setTotalPage(int totalPage) {
                this.totalPage = totalPage;
            }

            public List<ResultBean> getResult() {
                return result;
            }

            public void setResult(List<ResultBean> result) {
                this.result = result;
            }

            public static class ResultBean {
                private String collectionTime;
                private String id;
                private String albumName;
                private String albumId;
                private String smallImg;
                private String bigImg;

                public String getCollectionTime() {
                    return collectionTime;
                }

                public void setCollectionTime(String collectionTime) {
                    this.collectionTime = collectionTime;
                }

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public String getAlbumName() {
                    return albumName;
                }

                public void setAlbumName(String albumName) {
                    this.albumName = albumName;
                }

                public String getAlbumId() {
                    return albumId;
                }

                public void setAlbumId(String albumId) {
                    this.albumId = albumId;
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
}
