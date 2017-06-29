package com.cq.ln.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.cq.ln.R;
import com.cq.ln.utils.ImageTool;

import butterknife.Bind;
import butterknife.ButterKnife;
import cqdatasdk.bean.ArtistReleaListbean;
import cqdatasdk.bean.DiscRecommendListBean;
import cqdatasdk.bean.MusicStyleListBean;
import cqdatasdk.bean.VipTypeRelateAlbumBean;
import cqdatasdk.network.URLVerifyTools;

/**
 * Created by godfather on 2016/3/26.
 * 搜索页面最右边网格布局列表的Adapter
 */
public class SearchResultGridViewAdapter extends HiFiBaseAdapter {
    private Context context;
    private DisplayImageOptions options;

    public SearchResultGridViewAdapter(Context context) {
        super(context);
        this.context = context;

        options = new DisplayImageOptions.Builder().showImageOnLoading(R.mipmap.artist_def) // 默认
                .showImageForEmptyUri(R.mipmap.artist_def) // URI不存在
                .showImageOnFail(R.mipmap.artist_def) // 加载失败
                .displayer(new RoundedBitmapDisplayer(0)) // 图片圆角
                .cacheInMemory(true).cacheOnDisk(true).considerExifParams(true).imageScaleType(ImageScaleType.NONE_SAFE).bitmapConfig(Bitmap.Config.RGB_565).build();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        viewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_sear_result_gridview_item_list, null);
            holder = new viewHolder(view);
            view.setTag(holder);
        } else
            holder = (viewHolder) view.getTag();

        Object object = mList.get(i);
        if (object instanceof DiscRecommendListBean.DataBean.ListBean) {//唱片推荐列表数据模型
            DiscRecommendListBean.DataBean.ListBean bean = (DiscRecommendListBean.DataBean.ListBean) object;
            holder.mItemGridTxt.setText(bean.getName());

            ImageTool.loadImageWithUrl(context, URLVerifyTools.formatPicListImageUrl(bean.getBigImg()), holder.mItemGridImageView,options);
        } else if (object instanceof MusicStyleListBean.DataBean.ListBean) {//音乐风格
            MusicStyleListBean.DataBean.ListBean bean = (MusicStyleListBean.DataBean.ListBean) object;
            holder.mItemGridTxt.setText(bean.getName());

            ImageTool.loadImageWithUrl(context, URLVerifyTools.formatPicListImageUrl(bean.getBigImg()), holder.mItemGridImageView, options);
        } else if (object instanceof ArtistReleaListbean.DataBean.ListBean) {//艺术家
            ArtistReleaListbean.DataBean.ListBean bean = (ArtistReleaListbean.DataBean.ListBean) object;
            holder.mItemGridTxt.setText(bean.getName());

            ImageTool.loadImageWithUrl(context, URLVerifyTools.formatPicListImageUrl(bean.getSmallImg()), holder.mItemGridImageView,options);
        }else if (object instanceof VipTypeRelateAlbumBean.DataBean.ListBean){//新碟抢先听
            VipTypeRelateAlbumBean.DataBean.ListBean bean = (VipTypeRelateAlbumBean.DataBean.ListBean) object;
            holder.mItemGridTxt.setText(bean.getName());

            ImageTool.loadImageWithUrl(context, URLVerifyTools.formatPicListImageUrl(bean.getBigImg()), holder.mItemGridImageView,options);
        }


        return view;
    }

    static class viewHolder {

        @Bind(R.id.item_grid_ImageView)
        ImageView mItemGridImageView;
        @Bind(R.id.item_grid_txt)
        TextView mItemGridTxt;

        public viewHolder(View view) {
            ButterKnife.bind(this, view);
        }

    }
}
