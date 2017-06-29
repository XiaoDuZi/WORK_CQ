package cqdatasdk.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/3/24.
 * 播放记录列表/最近播放
 * http://172.16.146.56:8083/utvgoWeb/hifi/hifiUser/userPlayList.action?keyNo=8002003646694252&pageno=1&size=8
 */
public class RecentPlayBean {


    /**
     * code : 0
     * data : {"page":1,"size":8,"count":143,"totalPage":18,"list":[{"id":"1829","playTime":"2016-04-22 00:49:06.0","trackName":"爱你一万年","trackId":"14128","duration":"0:04:49","mp3Url":"1","flacUrl":"","vodId":"4420712","artistId":"868","artistName":"刘德华","albumName":"经典重现"},{"id":"1828","playTime":"2016-04-22 00:43:51.0","trackName":"男人哭吧不是罪","trackId":"14127","duration":"0:05:14","mp3Url":"1","flacUrl":"","vodId":"4420816","artistId":"868","artistName":"刘德华","albumName":"经典重现"},{"id":"1827","playTime":"2016-04-22 00:41:33.0","trackName":"你是我一生心最大的骄傲","trackId":"14126","duration":"0:05:12","mp3Url":"1","flacUrl":"","vodId":"4421013","artistId":"868","artistName":"刘德华","albumName":"经典重现"},{"id":"1826","playTime":"2016-04-22 00:40:42.0","trackName":"山沟沟 - 金志文","trackId":"15141","duration":"0:02:35","mp3Url":"1","flacUrl":"","vodId":"4395210","artistId":"315","artistName":"金志文","albumName":"中国好声音精选"},{"id":"1825","playTime":"2016-04-22 00:39:55.0","trackName":"降落练习存在孪生基因","trackId":"15632","duration":"0:04:45","mp3Url":"1","flacUrl":"","vodId":"4393096","artistId":"1098","artistName":"苏打绿","albumName":"苏打绿同名专辑"},{"id":"1824","playTime":"2016-04-22 00:39:49.0","trackName":"漂浮","trackId":"15635","duration":"0:06:03","mp3Url":"1","flacUrl":"","vodId":"4394412","artistId":"1098","artistName":"苏打绿","albumName":"苏打绿同名专辑"},{"id":"1823","playTime":"2016-04-22 00:39:34.0","trackName":"Homeland (Main Title)","trackId":"12376","duration":"0:03:41","mp3Url":"1","flacUrl":"","vodId":"4421327","artistId":"1251","artistName":"Hans Zimmer","albumName":"小马王电影动画原声大碟"},{"id":"1820","playTime":"2016-04-22 00:39:17.0","trackName":"心有千千结","trackId":"14466","duration":"0:02:43","mp3Url":"1","flacUrl":"","vodId":"4393359","artistId":"561","artistName":"石修","albumName":"顾嘉辉大师经典"}]}
     */

    private int code;
    /**
     * page : 1
     * size : 8
     * count : 143
     * totalPage : 18
     * list : [{"id":"1829","playTime":"2016-04-22 00:49:06.0","trackName":"爱你一万年","trackId":"14128","duration":"0:04:49","mp3Url":"1","flacUrl":"","vodId":"4420712","artistId":"868","artistName":"刘德华","albumName":"经典重现"},{"id":"1828","playTime":"2016-04-22 00:43:51.0","trackName":"男人哭吧不是罪","trackId":"14127","duration":"0:05:14","mp3Url":"1","flacUrl":"","vodId":"4420816","artistId":"868","artistName":"刘德华","albumName":"经典重现"},{"id":"1827","playTime":"2016-04-22 00:41:33.0","trackName":"你是我一生心最大的骄傲","trackId":"14126","duration":"0:05:12","mp3Url":"1","flacUrl":"","vodId":"4421013","artistId":"868","artistName":"刘德华","albumName":"经典重现"},{"id":"1826","playTime":"2016-04-22 00:40:42.0","trackName":"山沟沟 - 金志文","trackId":"15141","duration":"0:02:35","mp3Url":"1","flacUrl":"","vodId":"4395210","artistId":"315","artistName":"金志文","albumName":"中国好声音精选"},{"id":"1825","playTime":"2016-04-22 00:39:55.0","trackName":"降落练习存在孪生基因","trackId":"15632","duration":"0:04:45","mp3Url":"1","flacUrl":"","vodId":"4393096","artistId":"1098","artistName":"苏打绿","albumName":"苏打绿同名专辑"},{"id":"1824","playTime":"2016-04-22 00:39:49.0","trackName":"漂浮","trackId":"15635","duration":"0:06:03","mp3Url":"1","flacUrl":"","vodId":"4394412","artistId":"1098","artistName":"苏打绿","albumName":"苏打绿同名专辑"},{"id":"1823","playTime":"2016-04-22 00:39:34.0","trackName":"Homeland (Main Title)","trackId":"12376","duration":"0:03:41","mp3Url":"1","flacUrl":"","vodId":"4421327","artistId":"1251","artistName":"Hans Zimmer","albumName":"小马王电影动画原声大碟"},{"id":"1820","playTime":"2016-04-22 00:39:17.0","trackName":"心有千千结","trackId":"14466","duration":"0:02:43","mp3Url":"1","flacUrl":"","vodId":"4393359","artistId":"561","artistName":"石修","albumName":"顾嘉辉大师经典"}]
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
         * id : 1829
         * playTime : 2016-04-22 00:49:06.0
         * trackName : 爱你一万年
         * trackId : 14128
         * duration : 0:04:49
         * mp3Url : 1
         * flacUrl :
         * vodId : 4420712
         * artistId : 868
         * artistName : 刘德华
         * albumName : 经典重现
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
            private String id;
            private String playTime;
            private String trackName;
            private String trackId;
            private String duration;
            private String mp3Url;
            private String flacUrl;
            private String vodId;
            private String artistId;
            private String artistName;
            private String albumName;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getPlayTime() {
                return playTime;
            }

            public void setPlayTime(String playTime) {
                this.playTime = playTime;
            }

            public String getTrackName() {
                return trackName;
            }

            public void setTrackName(String trackName) {
                this.trackName = trackName;
            }

            public String getTrackId() {
                return trackId;
            }

            public void setTrackId(String trackId) {
                this.trackId = trackId;
            }

            public String getDuration() {
                return duration;
            }

            public void setDuration(String duration) {
                this.duration = duration;
            }

            public String getMp3Url() {
                return mp3Url;
            }

            public void setMp3Url(String mp3Url) {
                this.mp3Url = mp3Url;
            }

            public String getFlacUrl() {
                return flacUrl;
            }

            public void setFlacUrl(String flacUrl) {
                this.flacUrl = flacUrl;
            }

            public String getVodId() {
                return vodId;
            }

            public void setVodId(String vodId) {
                this.vodId = vodId;
            }

            public String getArtistId() {
                return artistId;
            }

            public void setArtistId(String artistId) {
                this.artistId = artistId;
            }

            public String getArtistName() {
                return artistName;
            }

            public void setArtistName(String artistName) {
                this.artistName = artistName;
            }

            public String getAlbumName() {
                return albumName;
            }

            public void setAlbumName(String albumName) {
                this.albumName = albumName;
            }
        }
    }
}
