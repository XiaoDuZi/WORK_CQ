package cqdatasdk.bean;

import java.io.Serializable;

/**
 * Created by godfather on 2016/3/23.
 */
public class CollectionSucceedBean implements Serializable{


    /**
     * code : 0
     * collectionId : 1462272
     * msg : 添加收藏成功
     */

    private String code;
    private int collectionId;
    private String msg;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getCollectionId() {
        return collectionId;
    }

    public void setCollectionId(int collectionId) {
        this.collectionId = collectionId;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
