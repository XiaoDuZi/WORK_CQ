package cqdatasdk.bean;

import java.util.List;

/**
 * Created by fute on 16/9/5.
 */
public class TopicsBean {


    /**
     * errors : []
     * extra : {}
     * result : [{"leftCorner":0,"img":"2016/06/14/1e198860-d4a3-4a81-8f5f-66550233cecf.jpg","smallImg":"2016/06/14/f0e51940-6567-4973-b933-56b52f1d4b93.png","tvgo_img":"http://172.16.146
     * .6:81/snet/uploadFile/2016/06/14/1e198860-d4a3-4a81-8f5f-66550233cecf.jpg","type":4,"colorTemplates":"#000000","id":84678,"veveUrl":"album.html?albumId=1312","title":"第一期","imgType":"cms",
     * "powerId":79,"showStatus":1,"href":"album.html?albumId=1312"},{"leftCorner":0,"img":"2016/06/14/9023116d-f287-45db-a8f9-ffd514496e2e.jpg",
     * "smallImg":"2016/06/14/f7f1ef81-6ba2-43d6-bbcd-718db8830685.png","tvgo_img":"http://172.16.146.6:81/snet/uploadFile/2016/06/14/9023116d-f287-45db-a8f9-ffd514496e2e.jpg","type":4,
     * "colorTemplates":"#000000","id":84679,"veveUrl":"album.html?albumId=1314","title":"第二期","imgType":"cms","powerId":78,"showStatus":1,"href":"album.html?albumId=1314"},{"leftCorner":0,
     * "img":"2016/06/14/ffe496b8-ad80-4db6-8a1d-668d48ff656f.jpg","smallImg":"2016/06/14/9f055e13-3ea4-45cb-9989-07839258a5a9.png","tvgo_img":"http://172.16.146
     * .6:81/snet/uploadFile/2016/06/14/ffe496b8-ad80-4db6-8a1d-668d48ff656f.jpg","type":4,"colorTemplates":"#000000","id":84680,"veveUrl":"album.html?albumId=1316","title":"第三期","imgType":"cms",
     * "powerId":77,"showStatus":1,"href":"album.html?albumId=1316"},{"leftCorner":0,"img":"2016/06/14/d77f9ead-866f-4b24-8372-aeb8a38b14f9.jpg",
     * "smallImg":"2016/06/28/c4468236-02b6-4f24-be12-c97548d043a8.png","tvgo_img":"http://172.16.146.6:81/snet/uploadFile/2016/06/14/d77f9ead-866f-4b24-8372-aeb8a38b14f9.jpg","type":4,
     * "colorTemplates":"#000000","id":84681,"veveUrl":"album.html?albumId=1317","title":"第四期","imgType":"cms","powerId":76,"showStatus":1,"href":"album.html?albumId=1317"},{"leftCorner":0,
     * "img":"2016/06/14/8c631cbc-5f66-44ec-8583-b33f7013a367.jpg","smallImg":"2016/06/28/bbad3d06-fbd3-4468-b857-d24fbd3d9158.png","tvgo_img":"http://172.16.146
     * .6:81/snet/uploadFile/2016/06/14/8c631cbc-5f66-44ec-8583-b33f7013a367.jpg","type":4,"colorTemplates":"#000000","id":84682,"veveUrl":"album.html?albumId=1321","title":"第五期","imgType":"cms",
     * "powerId":75,"showStatus":1,"href":"album.html?albumId=1321"},{"leftCorner":0,"img":"2016/06/14/36956c1a-9741-4d61-9de1-212885368c2a.jpg",
     * "smallImg":"2016/06/28/fe6c1860-8916-4396-8651-73a82c353d90.png","tvgo_img":"http://172.16.146.6:81/snet/uploadFile/2016/06/14/36956c1a-9741-4d61-9de1-212885368c2a.jpg","type":4,
     * "colorTemplates":"#000000","id":84683,"veveUrl":"album.html?albumId=1323","title":"第六期","imgType":"cms","powerId":74,"showStatus":1,"href":"album.html?albumId=1323"},{"leftCorner":0,
     * "img":"2016/06/14/1e4f29c0-2e82-4dc0-943d-06492bd40136.jpg","smallImg":"2016/06/28/d750c624-0739-4b84-b9b5-f214862c4cab.png","tvgo_img":"http://172.16.146
     * .6:81/snet/uploadFile/2016/06/14/1e4f29c0-2e82-4dc0-943d-06492bd40136.jpg","type":4,"colorTemplates":"#000000","id":84684,"veveUrl":"album.html?albumId=1322","title":"第七期","imgType":"cms",
     * "powerId":73,"showStatus":1,"href":"album.html?albumId=1322"},{"leftCorner":0,"img":"2016/06/14/cd13f657-d368-401e-a084-8f5f31fec30f.jpg",
     * "smallImg":"2016/06/28/e9b6f246-e8af-42b3-8e74-ef4473ae8003.png","tvgo_img":"http://172.16.146.6:81/snet/uploadFile/2016/06/14/cd13f657-d368-401e-a084-8f5f31fec30f.jpg","type":4,
     * "colorTemplates":"#000000","id":84685,"veveUrl":"album.html?albumId=1320","title":"第八期","imgType":"cms","powerId":72,"showStatus":1,"href":"album.html?albumId=1320"},{"leftCorner":0,
     * "img":"2016/06/14/c6bf5353-bff1-49d7-a754-630855b5bb57.jpg","smallImg":"2016/07/27/1c8d5f8f-f486-4efc-9697-6c498971b0eb.png","tvgo_img":"http://172.16.146
     * .6:81/snet/uploadFile/2016/06/14/c6bf5353-bff1-49d7-a754-630855b5bb57.jpg","type":4,"colorTemplates":"#000000","id":84686,"veveUrl":"album.html?albumId=1319","title":"第九期","imgType":"cms",
     * "powerId":71,"showStatus":1,"href":"album.html?albumId=1319"},{"leftCorner":0,"img":"2016/06/14/875a4c87-1817-49b2-90bf-54c9cbfc83fe.jpg",
     * "smallImg":"2016/07/27/a02a397a-b818-4283-a953-1575441331b2.png","tvgo_img":"http://172.16.146.6:81/snet/uploadFile/2016/06/14/875a4c87-1817-49b2-90bf-54c9cbfc83fe.jpg","type":4,
     * "colorTemplates":"#000000","id":84687,"veveUrl":"album.html?albumId=1318","title":"第十期","imgType":"cms","powerId":70,"showStatus":1,"href":"album.html?albumId=1318"},{"leftCorner":0,
     * "img":"2016/06/14/b2ccb36c-9d55-4889-ba14-9733599f26f9.jpg","smallImg":"2016/07/27/a092ec1f-e376-4f6a-a027-1e5d5c092717.png","tvgo_img":"http://172.16.146
     * .6:81/snet/uploadFile/2016/06/14/b2ccb36c-9d55-4889-ba14-9733599f26f9.jpg","type":4,"colorTemplates":"#000000","id":84688,"veveUrl":"album.html?albumId=1315","title":"第十一期","imgType":"cms",
     * "powerId":69,"showStatus":1,"href":"album.html?albumId=1315"},{"leftCorner":0,"img":"2016/06/14/996705dd-ed66-4a7c-81e4-6def0f27cd9c.jpg",
     * "smallImg":"2016/07/27/5bdcbeec-35fa-47a6-baad-0bae4a4842e6.png","tvgo_img":"http://172.16.146.6:81/snet/uploadFile/2016/06/14/996705dd-ed66-4a7c-81e4-6def0f27cd9c.jpg","type":4,
     * "colorTemplates":"#000000","id":84689,"veveUrl":"album.html?albumId=1313","title":"第十二期","imgType":"cms","powerId":68,"showStatus":1,"href":"album.html?albumId=1313"}]
     * status : 0
     */

    private int status;
    private List<?> errors;
    /**
     * leftCorner : 0
     * img : 2016/06/14/1e198860-d4a3-4a81-8f5f-66550233cecf.jpg
     * smallImg : 2016/06/14/f0e51940-6567-4973-b933-56b52f1d4b93.png
     * tvgo_img : http://172.16.146.6:81/snet/uploadFile/2016/06/14/1e198860-d4a3-4a81-8f5f-66550233cecf.jpg
     * type : 4
     * colorTemplates : #000000
     * id : 84678
     * veveUrl : album.html?albumId=1312
     * title : 第一期
     * imgType : cms
     * powerId : 79
     * showStatus : 1
     * href : album.html?albumId=1312
     */

    private List<ResultBean> result;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<?> getErrors() {
        return errors;
    }

    public void setErrors(List<?> errors) {
        this.errors = errors;
    }

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public static class ResultBean {
        private int leftCorner;
        private String img;
        private String smallImg;
        private String tvgo_img;
        private int type;
        private String colorTemplates;
        private int id;
        private String veveUrl;
        private String title;
        private String imgType;
        private int powerId;
        private int showStatus;
        private String href;

        public int getLeftCorner() {
            return leftCorner;
        }

        public void setLeftCorner(int leftCorner) {
            this.leftCorner = leftCorner;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getSmallImg() {
            return smallImg;
        }

        public void setSmallImg(String smallImg) {
            this.smallImg = smallImg;
        }

        public String getTvgo_img() {
            return tvgo_img;
        }

        public void setTvgo_img(String tvgo_img) {
            this.tvgo_img = tvgo_img;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getColorTemplates() {
            return colorTemplates;
        }

        public void setColorTemplates(String colorTemplates) {
            this.colorTemplates = colorTemplates;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getVeveUrl() {
            return veveUrl;
        }

        public void setVeveUrl(String veveUrl) {
            this.veveUrl = veveUrl;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getImgType() {
            return imgType;
        }

        public void setImgType(String imgType) {
            this.imgType = imgType;
        }

        public int getPowerId() {
            return powerId;
        }

        public void setPowerId(int powerId) {
            this.powerId = powerId;
        }

        public int getShowStatus() {
            return showStatus;
        }

        public void setShowStatus(int showStatus) {
            this.showStatus = showStatus;
        }

        public String getHref() {
            return href;
        }

        public void setHref(String href) {
            this.href = href;
        }
    }
}
