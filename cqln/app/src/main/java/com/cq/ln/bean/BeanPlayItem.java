package com.cq.ln.bean;

/**
 * Created by fute on 17/1/12.
 */

public class BeanPlayItem {
    private int trackId;
    private int initPoint;
    private String name;
    private String vodId;
    private String fileType;

    public int getTrackId() {
        return trackId;
    }

    public void setTrackId(int trackId) {
        this.trackId = trackId;
    }

    public int getInitPoint() {
        return initPoint;
    }

    public void setInitPoint(int initPoint) {
        this.initPoint = initPoint;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVodId() {
        return vodId;
    }

    public void setVodId(String vodId) {
        this.vodId = vodId;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFileType() {
        return fileType;
    }
}
