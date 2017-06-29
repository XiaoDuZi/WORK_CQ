package cqdatasdk.bean;

/**
 * Created by apple on 16/11/3.
 */

public class GetBackGroundMusicBean {

    /**
     * id : 2
     * name : 尘封的记忆
     * mp3Url : 1
     * flacUrl :
     * vodId : 4397142
     * duration : 0:03:25
     * fileType : 0
     */

    private TrackBean track;
    /**
     * track : {"id":2,"name":"尘封的记忆","mp3Url":"1","flacUrl":"","vodId":"4397142","duration":"0:03:25","fileType":"0"}
     * code : 0
     * info : 获取背景音乐成功！keyNo=8230004001366670
     */

    private String code;
    private String info;

    public TrackBean getTrack() {
        return track;
    }

    public void setTrack(TrackBean track) {
        this.track = track;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public static class TrackBean {
        private int id;
        private String name;
        private String mp3Url;
        private String flacUrl;
        private String vodId;
        private String duration;
        private String fileType;

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

        public String getDuration() {
            return duration;
        }

        public void setDuration(String duration) {
            this.duration = duration;
        }

        public String getFileType() {
            return fileType;
        }

        public void setFileType(String fileType) {
            this.fileType = fileType;
        }
    }
}
