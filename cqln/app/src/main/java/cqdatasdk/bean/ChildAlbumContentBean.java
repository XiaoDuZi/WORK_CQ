package cqdatasdk.bean;

import java.util.List;

/**
 * Created by fute on 16/9/24.
 */

public class ChildAlbumContentBean {


    /**
     * code : 0
     * data : {"albumId":1609,"albumName":"有趣的动物儿歌","albumIntroduction":"有趣的动物儿歌","albumSmallImg":"/utvgo_small/20160920032733888.jpg","albumBigImg":"/utvgo_big/20160920032733428.jpg",
     * "artistNames":"","ifCollection":1,"collectionId":0,"fileType":"1","tjList":[{"albumId":1586,"albumName":"3D儿歌欢乐颂","albumBigImg":"/utvgo_big/20160920033600129.jpg",
     * "albumSmallImg":"/utvgo_small/20160920033600664.jpg","fileType":"1"},{"albumId":1597,"albumName":"童趣无限的3D儿歌","albumBigImg":"/utvgo_big/20160920033534821.jpg",
     * "albumSmallImg":"/utvgo_small/20160920033534363.jpg","fileType":"1"},{"albumId":1600,"albumName":"洋娃娃的舞会儿歌","albumBigImg":"/utvgo_big/2016092003350452.jpg",
     * "albumSmallImg":"/utvgo_small/2016092003350428.jpg","fileType":"1"}],"trackList":[{"trackId":33494,"trackName":"狗","mp3Url":"1","flacUrl":"","vodId":"4572998","trackSmallImg":"",
     * "trackBigImg":"","trackDuration":"","fileType":"1"},{"trackId":33497,"trackName":"狐狸","mp3Url":"1","flacUrl":"","vodId":"4573034","trackSmallImg":"","trackBigImg":"","trackDuration":"",
     * "fileType":"1"},{"trackId":42772,"trackName":"骄傲的大公鸡","mp3Url":"1","flacUrl":"","vodId":"4572966","trackSmallImg":"","trackBigImg":"","trackDuration":"","fileType":"1"},{"trackId":42777,
     * "trackName":"狼来了","mp3Url":"1","flacUrl":"","vodId":"4573146","trackSmallImg":"","trackBigImg":"","trackDuration":"","fileType":"1"},{"trackId":42779,"trackName":"猫儿追老鼠逃","mp3Url":"1",
     * "flacUrl":"","vodId":"4572965","trackSmallImg":"","trackBigImg":"","trackDuration":"","fileType":"1"},{"trackId":42780,"trackName":"青蛙大嘴巴","mp3Url":"1","flacUrl":"","vodId":"4572836",
     * "trackSmallImg":"","trackBigImg":"","trackDuration":"","fileType":"1"},{"trackId":42793,"trackName":"鱼儿水中游","mp3Url":"1","flacUrl":"","vodId":"4573313","trackSmallImg":"","trackBigImg":"",
     * "trackDuration":"","fileType":"1"},{"trackId":42798,"trackName":"我爱小青蛙","mp3Url":"1","flacUrl":"","vodId":"4572859","trackSmallImg":"","trackBigImg":"","trackDuration":"","fileType":"1"}]}
     */

    private String code;
    /**
     * albumId : 1609
     * albumName : 有趣的动物儿歌
     * albumIntroduction : 有趣的动物儿歌
     * albumSmallImg : /utvgo_small/20160920032733888.jpg
     * albumBigImg : /utvgo_big/20160920032733428.jpg
     * artistNames :
     * ifCollection : 1
     * collectionId : 0
     * fileType : 1
     * tjList : [{"albumId":1586,"albumName":"3D儿歌欢乐颂","albumBigImg":"/utvgo_big/20160920033600129.jpg","albumSmallImg":"/utvgo_small/20160920033600664.jpg","fileType":"1"},{"albumId":1597,
     * "albumName":"童趣无限的3D儿歌","albumBigImg":"/utvgo_big/20160920033534821.jpg","albumSmallImg":"/utvgo_small/20160920033534363.jpg","fileType":"1"},{"albumId":1600,"albumName":"洋娃娃的舞会儿歌",
     * "albumBigImg":"/utvgo_big/2016092003350452.jpg","albumSmallImg":"/utvgo_small/2016092003350428.jpg","fileType":"1"}]
     * trackList : [{"trackId":33494,"trackName":"狗","mp3Url":"1","flacUrl":"","vodId":"4572998","trackSmallImg":"","trackBigImg":"","trackDuration":"","fileType":"1"},{"trackId":33497,
     * "trackName":"狐狸","mp3Url":"1","flacUrl":"","vodId":"4573034","trackSmallImg":"","trackBigImg":"","trackDuration":"","fileType":"1"},{"trackId":42772,"trackName":"骄傲的z大公鸡","mp3Url":"1",
     * "flacUrl":"","vodId":"4572966","trackSmallImg":"","trackBigImg":"","trackDuration":"","fileType":"1"},{"trackId":42777,"trackName":"狼来了","mp3Url":"1","flacUrl":"","vodId":"4573146",
     * "trackSmallImg":"","trackBigImg":"","trackDuration":"","fileType":"1"},{"trackId":42779,"trackName":"猫儿追老鼠逃","mp3Url":"1","flacUrl":"","vodId":"4572965","trackSmallImg":"","trackBigImg":"",
     * "trackDuration":"","fileType":"1"},{"trackId":42780,"trackName":"青蛙大嘴巴","mp3Url":"1","flacUrl":"","vodId":"4572836","trackSmallImg":"","trackBigImg":"","trackDuration":"","fileType":"1"},
     * {"trackId":42793,"trackName":"鱼儿水中游","mp3Url":"1","flacUrl":"","vodId":"4573313","trackSmallImg":"","trackBigImg":"","trackDuration":"","fileType":"1"},{"trackId":42798,"trackName":"我爱小青蛙",
     * "mp3Url":"1","flacUrl":"","vodId":"4572859","trackSmallImg":"","trackBigImg":"","trackDuration":"","fileType":"1"}]
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
        private int albumId;
        private String albumName;
        private String albumIntroduction;
        private String albumSmallImg;
        private String albumBigImg;
        private String artistNames;
        private int ifCollection;
        private int collectionId;
        private String fileType;
        /**
         * albumId : 1586
         * albumName : 3D儿歌欢乐颂
         * albumBigImg : /utvgo_big/20160920033600129.jpg
         * albumSmallImg : /utvgo_small/20160920033600664.jpg
         * fileType : 1
         */

        private List<TjListBean> tjList;
        /**
         * trackId : 33494
         * trackName : 狗
         * mp3Url : 1
         * flacUrl :
         * vodId : 4572998
         * trackSmallImg :
         * trackBigImg :
         * trackDuration :
         * fileType : 1
         */

        private List<TrackListBean> trackList;

        public int getAlbumId() {
            return albumId;
        }

        public void setAlbumId(int albumId) {
            this.albumId = albumId;
        }

        public String getAlbumName() {
            return albumName;
        }

        public void setAlbumName(String albumName) {
            this.albumName = albumName;
        }

        public String getAlbumIntroduction() {
            return albumIntroduction;
        }

        public void setAlbumIntroduction(String albumIntroduction) {
            this.albumIntroduction = albumIntroduction;
        }

        public String getAlbumSmallImg() {
            return albumSmallImg;
        }

        public void setAlbumSmallImg(String albumSmallImg) {
            this.albumSmallImg = albumSmallImg;
        }

        public String getAlbumBigImg() {
            return albumBigImg;
        }

        public void setAlbumBigImg(String albumBigImg) {
            this.albumBigImg = albumBigImg;
        }

        public String getArtistNames() {
            return artistNames;
        }

        public void setArtistNames(String artistNames) {
            this.artistNames = artistNames;
        }

        public int getIfCollection() {
            return ifCollection;
        }

        public void setIfCollection(int ifCollection) {
            this.ifCollection = ifCollection;
        }

        public int getCollectionId() {
            return collectionId;
        }

        public void setCollectionId(int collectionId) {
            this.collectionId = collectionId;
        }

        public String getFileType() {
            return fileType;
        }

        public void setFileType(String fileType) {
            this.fileType = fileType;
        }

        public List<TjListBean> getTjList() {
            return tjList;
        }

        public void setTjList(List<TjListBean> tjList) {
            this.tjList = tjList;
        }

        public List<TrackListBean> getTrackList() {
            return trackList;
        }

        public void setTrackList(List<TrackListBean> trackList) {
            this.trackList = trackList;
        }

        public static class TjListBean {
            private int albumId;
            private String albumName;
            private String albumBigImg;
            private String albumSmallImg;
            private String fileType;

            public int getAlbumId() {
                return albumId;
            }

            public void setAlbumId(int albumId) {
                this.albumId = albumId;
            }

            public String getAlbumName() {
                return albumName;
            }

            public void setAlbumName(String albumName) {
                this.albumName = albumName;
            }

            public String getAlbumBigImg() {
                return albumBigImg;
            }

            public void setAlbumBigImg(String albumBigImg) {
                this.albumBigImg = albumBigImg;
            }

            public String getAlbumSmallImg() {
                return albumSmallImg;
            }

            public void setAlbumSmallImg(String albumSmallImg) {
                this.albumSmallImg = albumSmallImg;
            }

            public String getFileType() {
                return fileType;
            }

            public void setFileType(String fileType) {
                this.fileType = fileType;
            }
        }

        public static class TrackListBean {
            private int trackId;
            private String trackName;
            private String mp3Url;
            private String flacUrl;
            private String vodId;
            private String trackSmallImg;
            private String trackBigImg;
            private String trackDuration;
            private String fileType;

            public int getTrackId() {
                return trackId;
            }

            public void setTrackId(int trackId) {
                this.trackId = trackId;
            }

            public String getTrackName() {
                return trackName;
            }

            public void setTrackName(String trackName) {
                this.trackName = trackName;
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

            public String getTrackSmallImg() {
                return trackSmallImg;
            }

            public void setTrackSmallImg(String trackSmallImg) {
                this.trackSmallImg = trackSmallImg;
            }

            public String getTrackBigImg() {
                return trackBigImg;
            }

            public void setTrackBigImg(String trackBigImg) {
                this.trackBigImg = trackBigImg;
            }

            public String getTrackDuration() {
                return trackDuration;
            }

            public void setTrackDuration(String trackDuration) {
                this.trackDuration = trackDuration;
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
