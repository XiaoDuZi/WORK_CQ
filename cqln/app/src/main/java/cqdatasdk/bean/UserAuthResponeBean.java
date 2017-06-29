package cqdatasdk.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Godfather on 16/4/15.
 * 用户鉴权返回结果
 */
public class UserAuthResponeBean implements Serializable{


    /**
     * recordId : 11963
     * code : 0
     * msg : 添加播放记录成功
     * data : {"name":"什么是感觉统合教育","id":16251,"mp3Url":"","flacUrl":"","vodId":"4663439","duration":"","cmboIds":[{"cmboId":"1404","id":22,"imgUrl":"/product/has_order/MotherClassroom.png",
     * "offerId":"800520160017","orderBg":"/product/order_bg/mon.png","orderCycle":"M","pName":"孕婴教育29元/月按月扣费套餐","price":29,"status":0,"type":"0"},{"cmboId":"1499","id":28,
     * "imgUrl":"/product/has_order/MotherClassroom.png","offerId":"800520150492","orderBg":"/product/order_bg/mon.png","orderCycle":"Y","pName":"孕婴教育299元/年","price":299,"status":0,"type":"0"}],
     * "fileType":"1","bigImg":"/utvgo_big/cp0001010301_S.jpg","smallImg":"/utvgo_small/cp0001010301_K.jpg","artistNames":"","ifCollection":1,"collectionId":0,"isfree":0,"userCode":"200"}
     */

    private int recordId;
    private String code;
    private String msg;
    /**
     * name : 什么是感觉统合教育
     * id : 16251
     * mp3Url :
     * flacUrl :
     * vodId : 4663439
     * duration :
     * cmboIds : [{"cmboId":"1404","id":22,"imgUrl":"/product/has_order/MotherClassroom.png","offerId":"800520160017","orderBg":"/product/order_bg/mon.png","orderCycle":"M",
     * "pName":"孕婴教育29元/月按月扣费套餐","price":29,"status":0,"type":"0"},{"cmboId":"1499","id":28,"imgUrl":"/product/has_order/MotherClassroom.png","offerId":"800520150492",
     * "orderBg":"/product/order_bg/mon.png","orderCycle":"Y","pName":"孕婴教育299元/年","price":299,"status":0,"type":"0"}]
     * fileType : 1
     * bigImg : /utvgo_big/cp0001010301_S.jpg
     * smallImg : /utvgo_small/cp0001010301_K.jpg
     * artistNames :
     * ifCollection : 1
     * collectionId : 0
     * isfree : 0
     * userCode : 200
     */

    private DataBean data;

    public int getRecordId() {
        return recordId;
    }

    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private String name;
        private int id;
        private String mp3Url;
        private String flacUrl;
        private String vodId;
        private String duration;
        private String fileType;
        private String bigImg;
        private String smallImg;
        private String artistNames;
        private int ifCollection;
        private int collectionId;
        private int isfree;
        private String userCode;
        /**
         * cmboId : 1404
         * id : 22
         * imgUrl : /product/has_order/MotherClassroom.png
         * offerId : 800520160017
         * orderBg : /product/order_bg/mon.png
         * orderCycle : M
         * pName : 孕婴教育29元/月按月扣费套餐
         * price : 29.0
         * status : 0
         * type : 0
         */

        private List<CmboIdsBean> cmboIds;

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

        public int getIsfree() {
            return isfree;
        }

        public void setIsfree(int isfree) {
            this.isfree = isfree;
        }

        public String getUserCode() {
            return userCode;
        }

        public void setUserCode(String userCode) {
            this.userCode = userCode;
        }

        public List<CmboIdsBean> getCmboIds() {
            return cmboIds;
        }

        public void setCmboIds(List<CmboIdsBean> cmboIds) {
            this.cmboIds = cmboIds;
        }

        public static class CmboIdsBean {
            private String cmboId;
            private int id;
            private String imgUrl;
            private String offerId;
            private String orderBg;
            private String orderCycle;
            private String pName;
            private double price;
            private int status;
            private String type;

            public String getCmboId() {
                return cmboId;
            }

            public void setCmboId(String cmboId) {
                this.cmboId = cmboId;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getImgUrl() {
                return imgUrl;
            }

            public void setImgUrl(String imgUrl) {
                this.imgUrl = imgUrl;
            }

            public String getOfferId() {
                return offerId;
            }

            public void setOfferId(String offerId) {
                this.offerId = offerId;
            }

            public String getOrderBg() {
                return orderBg;
            }

            public void setOrderBg(String orderBg) {
                this.orderBg = orderBg;
            }

            public String getOrderCycle() {
                return orderCycle;
            }

            public void setOrderCycle(String orderCycle) {
                this.orderCycle = orderCycle;
            }

            public String getPName() {
                return pName;
            }

            public void setPName(String pName) {
                this.pName = pName;
            }

            public double getPrice() {
                return price;
            }

            public void setPrice(double price) {
                this.price = price;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }
        }
    }
}
