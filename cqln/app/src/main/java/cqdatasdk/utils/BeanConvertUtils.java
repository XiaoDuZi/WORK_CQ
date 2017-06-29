package cqdatasdk.utils;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import cqdatasdk.bean.CollectionSinglebean;
import cqdatasdk.bean.RecentPlayBean;
import cqdatasdk.bean.SpecialDetailBean;

/**
 * Created by Godfather on 16/4/23.
 * java bean转换工具
 */
public class BeanConvertUtils {
    public static BeanConvertUtils mBeanConvertUtils;

    public static BeanConvertUtils getInstance() {
        if (mBeanConvertUtils == null)
            mBeanConvertUtils = new BeanConvertUtils();
        return mBeanConvertUtils;
    }


// CollectionSinglebean.DataBean.ListBean.ResultBean

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

//    SpecialDetailBean.DataBean.TrackListBean

    /**
     * trackId : 139
     * trackName : The Higher You Rise
     * mp3Url : 1
     * flacUrl :
     * vodId : 4417579
     * trackSmallImg :
     * trackBigImg :
     * trackDuration : 0:04:07
     */

    /**
     * 收藏的Bean转换为专辑详情的Bean
     *
     * @param orignBean
     * @return
     */
    public SpecialDetailBean.DataBean.TrackListBean collectionSingle2SpecialBean(CollectionSinglebean.DataBean.ListBean.ResultBean orignBean) {
        SpecialDetailBean.DataBean.TrackListBean result = new SpecialDetailBean.DataBean.TrackListBean();
        int trackid = 0;
        if (!TextUtils.isEmpty(orignBean.getTrackId()))
            trackid = Integer.parseInt(orignBean.getTrackId());
        result.setTrackId(trackid);
        result.setTrackName(orignBean.getTrackName());
        result.setMp3Url(orignBean.getMp3Url());
        result.setFlacUrl(orignBean.getFlacUrl());
        result.setVodId(orignBean.getVodId());
        result.setTrackDuration(orignBean.getDuration());

        return result;

    }

    /**
     * @param orignList 收藏的单曲list 转换为专辑详情的曲目list
     * @return
     */
    public List<SpecialDetailBean.DataBean.TrackListBean> collectionSingleList2SpecailBeanList(List<CollectionSinglebean.DataBean.ListBean.ResultBean> orignList) {
        List<SpecialDetailBean.DataBean.TrackListBean> resultList = new ArrayList<>(orignList.size());
        for (CollectionSinglebean.DataBean.ListBean.ResultBean bean : orignList) {
            resultList.add(collectionSingle2SpecialBean(bean));
        }
        return resultList;
    }


    /***
     *
     * //带列表带跳转
     *

     Intent intent = new Intent(this, MusicPlayActivity.class);
     intent.putExtra("isSingle", false);
     intent.putExtra("TrackListBean", bean);
     intent.putExtra("trackList", (Serializable) musicListAdapter.getlist());
     intent.putExtra("currentIndex", i);

     if (specialDetailBean != null && specialDetailBean.getData() != null) {
     intent.putExtra("albumName", specialDetailBean.getData().getAlbumName());//专辑名
     intent.putExtra("artistNames", specialDetailBean.getData().getArtistNames());//艺术家
     }
     startActivity(intent);
     * @param orignBean
     * @return


     */

//    RecentPlayBean.DataBean.ListBean  -->SpecialDetailBean.DataBean.TrackListBean
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

    //    SpecialDetailBean.DataBean.TrackListBean

    /**
     * trackId : 139
     * trackName : The Higher You Rise
     * mp3Url : 1
     * flacUrl :
     * vodId : 4417579
     * trackSmallImg :
     * trackBigImg :
     * trackDuration : 0:04:07
     */

    /**
     * 最近播放的bean 转换为专辑详情的bean
     *
     * @param orignBean
     * @return
     */
    public SpecialDetailBean.DataBean.TrackListBean Recentbean2Specailbean(RecentPlayBean.DataBean.ListBean orignBean) {
        SpecialDetailBean.DataBean.TrackListBean resultBean = new SpecialDetailBean.DataBean.TrackListBean();
        int trackid = 0;
        if (!TextUtils.isEmpty(orignBean.getTrackId()))
            trackid = Integer.parseInt(orignBean.getTrackId());
        resultBean.setTrackId(trackid);
        resultBean.setTrackName(orignBean.getTrackName());
        resultBean.setMp3Url(orignBean.getMp3Url());
        resultBean.setFlacUrl(orignBean.getFlacUrl());
        resultBean.setVodId(orignBean.getVodId());
        resultBean.setTrackDuration(orignBean.getDuration());


        return resultBean;

    }

    public List<SpecialDetailBean.DataBean.TrackListBean> RecentList2SpecailList(List<RecentPlayBean.DataBean.ListBean> orignList) {
        List<SpecialDetailBean.DataBean.TrackListBean> resultList = new ArrayList<>(orignList.size());
        for (RecentPlayBean.DataBean.ListBean bean : orignList) {
            resultList.add(Recentbean2Specailbean(bean));
        }
        return resultList;
    }


}
