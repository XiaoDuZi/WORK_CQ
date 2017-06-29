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
import cqdatasdk.bean.CollectionSpecailBean;
import cqdatasdk.network.URLVerifyTools;

/**
 * Created by Godfather on 16/4/22.
 * 收藏的专辑
 */
public class CollectionSpecialAdapter extends HiFiBaseAdapter {

    private Context context;
    private DisplayImageOptions options;

    public CollectionSpecialAdapter(Context context) {
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


        /**
         * collectionTime : 2016-04-14 10:31:21.0
         * id : 210
         * albumName : 献给你的歌，卡伦
         * albumId : 510
         */

        CollectionSpecailBean.DataBean.ListBean.ResultBean bean = (CollectionSpecailBean.DataBean.ListBean.ResultBean) mList.get(i);

        holder.mItemGridTxt.setText(bean.getAlbumName());
        ImageTool.loadImageWithUrl(context, URLVerifyTools.formatPicListImageUrl(bean.getBigImg()), holder.mItemGridImageView, options);


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
