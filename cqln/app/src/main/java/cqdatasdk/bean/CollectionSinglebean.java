package cqdatasdk.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Godfather on 16/4/22.
 * 收藏的单曲
 * http://192.168.34.81/utvgoWeb/hifi/hifiUser/userCollection.action?keyNo=8002003646694252&pageno=1&size=8&collectionType=0
 */
public class CollectionSinglebean implements Serializable {


    /**
     * code : 0
     * data : {"page":1,"size":8,"count":40,"totalPage":5,"list":{"cacheKey":"-1-8","count":40,"offset":0,"pageNo":1,"pageSize":8,"result":[{"collectionTime":"2016-04-22 00:22:20.0","id":"255","vodId":"4401434","duration":"0:02:38","trackId":"8926","albumName":"邓丽君15周年XRCD","artistId":"1245","trackName":"小城故事","artistName":"邓丽君","flacUrl":"","mp3Url":"1"},{"collectionTime":"2016-04-21 23:53:38.0","id":"253","vodId":"4394594","duration":"0:04:55","trackId":"14625","albumName":"那些和我们打过招呼的爱情","artistId":"1246","trackName":"伤痕","artistName":"张杰","flacUrl":"","mp3Url":"1"},{"collectionTime":"2016-04-21 22:30:44.0","id":"252","vodId":"4396665","duration":"0:04:36","trackId":"14150","albumName":"经典重现","artistId":"868","trackName":"谢谢你的爱","artistName":"刘德华","flacUrl":"","mp3Url":"1"},{"collectionTime":"2016-04-21 12:21:54.0","id":"233","vodId":"4410127","duration":"0:05:17","trackId":"2732","albumName":"完美声音.VOL.2","artistId":"1238","trackName":"蓝月亮","artistName":"李克勤","flacUrl":"","mp3Url":"1"},{"collectionTime":"2016-04-21 10:02:34.0","id":"232","vodId":"4418856","duration":"0:03:14","trackId":"10998","albumName":"2009左麟右李演唱会","artistId":"1223","trackName":"高妹","artistName":"谭咏麟","flacUrl":"","mp3Url":"1"},{"collectionTime":"2016-04-20 17:10:28.0","id":"230","vodId":"4395147","duration":"0:04:07","trackId":"15045","albumName":"中国好歌曲 第7期","artistId":"1262","trackName":"喝酒Blues","artistName":"张岭","flacUrl":"","mp3Url":"1"},{"collectionTime":"2016-04-20 16:31:34.0","id":"227","vodId":"4393155","duration":"0:05:49","trackId":"15634","albumName":"苏打绿同名专辑","artistId":"1098","trackName":"频率","artistName":"苏打绿","flacUrl":"","mp3Url":"1"},{"collectionTime":"2016-04-20 16:15:37.0","id":"225","vodId":"4422799","duration":"0:02:18","trackId":"13952","albumName":"李克勤&香港小交响乐团 演奏厅2011","artistId":"1238","trackName":"高妹","artistName":"李克勤","flacUrl":"","mp3Url":"1"}],"totalPage":5}}
     * msg
     */

    private int code;
    /**
     * page : 1
     * size : 8
     * count : 40
     * totalPage : 5
     * list : {"cacheKey":"-1-8","count":40,"offset":0,"pageNo":1,"pageSize":8,"result":[{"collectionTime":"2016-04-22 00:22:20.0","id":"255","vodId":"4401434","duration":"0:02:38","trackId":"8926","albumName":"邓丽君15周年XRCD","artistId":"1245","trackName":"小城故事","artistName":"邓丽君","flacUrl":"","mp3Url":"1"},{"collectionTime":"2016-04-21 23:53:38.0","id":"253","vodId":"4394594","duration":"0:04:55","trackId":"14625","albumName":"那些和我们打过招呼的爱情","artistId":"1246","trackName":"伤痕","artistName":"张杰","flacUrl":"","mp3Url":"1"},{"collectionTime":"2016-04-21 22:30:44.0","id":"252","vodId":"4396665","duration":"0:04:36","trackId":"14150","albumName":"经典重现","artistId":"868","trackName":"谢谢你的爱","artistName":"刘德华","flacUrl":"","mp3Url":"1"},{"collectionTime":"2016-04-21 12:21:54.0","id":"233","vodId":"4410127","duration":"0:05:17","trackId":"2732","albumName":"完美声音.VOL.2","artistId":"1238","trackName":"蓝月亮","artistName":"李克勤","flacUrl":"","mp3Url":"1"},{"collectionTime":"2016-04-21 10:02:34.0","id":"232","vodId":"4418856","duration":"0:03:14","trackId":"10998","albumName":"2009左麟右李演唱会","artistId":"1223","trackName":"高妹","artistName":"谭咏麟","flacUrl":"","mp3Url":"1"},{"collectionTime":"2016-04-20 17:10:28.0","id":"230","vodId":"4395147","duration":"0:04:07","trackId":"15045","albumName":"中国好歌曲 第7期","artistId":"1262","trackName":"喝酒Blues","artistName":"张岭","flacUrl":"","mp3Url":"1"},{"collectionTime":"2016-04-20 16:31:34.0","id":"227","vodId":"4393155","duration":"0:05:49","trackId":"15634","albumName":"苏打绿同名专辑","artistId":"1098","trackName":"频率","artistName":"苏打绿","flacUrl":"","mp3Url":"1"},{"collectionTime":"2016-04-20 16:15:37.0","id":"225","vodId":"4422799","duration":"0:02:18","trackId":"13952","albumName":"李克勤&香港小交响乐团 演奏厅2011","artistId":"1238","trackName":"高妹","artistName":"李克勤","flacUrl":"","mp3Url":"1"}],"totalPage":5}
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
         * count : 40
         * offset : 0
         * pageNo : 1
         * pageSize : 8
         * result : [{"collectionTime":"2016-04-22 00:22:20.0","id":"255","vodId":"4401434","duration":"0:02:38","trackId":"8926","albumName":"邓丽君15周年XRCD","artistId":"1245","trackName":"小城故事","artistName":"邓丽君","flacUrl":"","mp3Url":"1"},{"collectionTime":"2016-04-21 23:53:38.0","id":"253","vodId":"4394594","duration":"0:04:55","trackId":"14625","albumName":"那些和我们打过招呼的爱情","artistId":"1246","trackName":"伤痕","artistName":"张杰","flacUrl":"","mp3Url":"1"},{"collectionTime":"2016-04-21 22:30:44.0","id":"252","vodId":"4396665","duration":"0:04:36","trackId":"14150","albumName":"经典重现","artistId":"868","trackName":"谢谢你的爱","artistName":"刘德华","flacUrl":"","mp3Url":"1"},{"collectionTime":"2016-04-21 12:21:54.0","id":"233","vodId":"4410127","duration":"0:05:17","trackId":"2732","albumName":"完美声音.VOL.2","artistId":"1238","trackName":"蓝月亮","artistName":"李克勤","flacUrl":"","mp3Url":"1"},{"collectionTime":"2016-04-21 10:02:34.0","id":"232","vodId":"4418856","duration":"0:03:14","trackId":"10998","albumName":"2009左麟右李演唱会","artistId":"1223","trackName":"高妹","artistName":"谭咏麟","flacUrl":"","mp3Url":"1"},{"collectionTime":"2016-04-20 17:10:28.0","id":"230","vodId":"4395147","duration":"0:04:07","trackId":"15045","albumName":"中国好歌曲 第7期","artistId":"1262","trackName":"喝酒Blues","artistName":"张岭","flacUrl":"","mp3Url":"1"},{"collectionTime":"2016-04-20 16:31:34.0","id":"227","vodId":"4393155","duration":"0:05:49","trackId":"15634","albumName":"苏打绿同名专辑","artistId":"1098","trackName":"频率","artistName":"苏打绿","flacUrl":"","mp3Url":"1"},{"collectionTime":"2016-04-20 16:15:37.0","id":"225","vodId":"4422799","duration":"0:02:18","trackId":"13952","albumName":"李克勤&香港小交响乐团 演奏厅2011","artistId":"1238","trackName":"高妹","artistName":"李克勤","flacUrl":"","mp3Url":"1"}]
         * totalPage : 5
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
             * collectionTime : 2016-04-22 00:22:20.0
             * id : 255
             * vodId : 4401434
             * duration : 0:02:38
             * trackId : 8926
             * albumName : 邓丽君15周年XRCD
             * artistId : 1245
             * trackName : 小城故事
             * artistName : 邓丽君
             * flacUrl :
             * mp3Url : 1
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
                private String vodId;
                private String duration;
                private String trackId;
                private String albumName;
                private String artistId;
                private String trackName;
                private String artistName;
                private String flacUrl;
                private String mp3Url;

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

                public String getVodId() {
                    return vodId;
                }

                public void setVodId(String vodId) {
                    this.vodId = vodId;
                }

                public String getDuration() {
                    return duration;
                }

                public void setDuration(String duration) {
                    this.duration = duration;
                }

                public String getTrackId() {
                    return trackId;
                }

                public void setTrackId(String trackId) {
                    this.trackId = trackId;
                }

                public String getAlbumName() {
                    return albumName;
                }

                public void setAlbumName(String albumName) {
                    this.albumName = albumName;
                }

                public String getArtistId() {
                    return artistId;
                }

                public void setArtistId(String artistId) {
                    this.artistId = artistId;
                }

                public String getTrackName() {
                    return trackName;
                }

                public void setTrackName(String trackName) {
                    this.trackName = trackName;
                }

                public String getArtistName() {
                    return artistName;
                }

                public void setArtistName(String artistName) {
                    this.artistName = artistName;
                }

                public String getFlacUrl() {
                    return flacUrl;
                }

                public void setFlacUrl(String flacUrl) {
                    this.flacUrl = flacUrl;
                }

                public String getMp3Url() {
                    return mp3Url;
                }

                public void setMp3Url(String mp3Url) {
                    this.mp3Url = mp3Url;
                }
            }
        }
    }
}
