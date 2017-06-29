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
import com.cq.ln.constant.ConstantEnum;
import com.cq.ln.utils.ImageTool;
import com.cq.ln.utils.StrTool;

import butterknife.Bind;
import butterknife.ButterKnife;
import cqdatasdk.bean.SearchResultBean;
import cqdatasdk.network.URLVerifyTools;

/**
 * Created by godfather on 2016/3/24.
 * 关键词搜索结果的Adapter
 */
public class SearchKeyWordResultListAdapter extends HiFiBaseAdapter {

    private Context context;
    private DisplayImageOptions musicOptions;
    private DisplayImageOptions artistOptions;

    public SearchKeyWordResultListAdapter(Context context) {
        super(context);
        this.context = context;

        musicOptions = new DisplayImageOptions.Builder().showImageOnLoading(R.mipmap.music_def) // 默认
                .showImageForEmptyUri(R.mipmap.music_def) // URI不存在
                .showImageOnFail(R.mipmap.music_def) // 加载失败
                .displayer(new RoundedBitmapDisplayer(0)) // 图片圆角
                .cacheInMemory(true).cacheOnDisk(true).considerExifParams(true).imageScaleType(ImageScaleType.NONE_SAFE).bitmapConfig(Bitmap.Config.RGB_565).build();

        artistOptions = new DisplayImageOptions.Builder().showImageOnLoading(R.mipmap.artist_def) // 默认
                .showImageForEmptyUri(R.mipmap.artist_def) // URI不存在
                .showImageOnFail(R.mipmap.artist_def) // 加载失败
                .displayer(new RoundedBitmapDisplayer(0)) // 图片圆角
                .cacheInMemory(true).cacheOnDisk(true).considerExifParams(true).imageScaleType(ImageScaleType.NONE_SAFE).bitmapConfig(Bitmap.Config.RGB_565).build();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        viewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_search_result, null);
            holder = new viewHolder(view);
            view.setTag(holder);
        } else
            holder = (viewHolder) view.getTag();

        SearchResultBean.DataBean.ListBean bean = (SearchResultBean.DataBean.ListBean) mList.get(i);

        int conentType = StrTool.parsetNum(bean.getContentType());
        switch (conentType) {
            case ConstantEnum.contentType_Artist:
                ImageTool.loadImageWithUrl(context, URLVerifyTools.formatPicListImageUrl(bean.getSmallImg()), holder.mItemImageView, artistOptions);

                break;
            default:
                ImageTool.loadImageWithUrl(context, URLVerifyTools.formatPicListImageUrl(bean.getSmallImg()), holder.mItemImageView, musicOptions);

                break;
        }


        holder.mItemNameOne.setText(bean.getName());
        holder.mItemNameTwo.setText("");//TODO 暂无，需要设置,待完善处理
        return view;
    }

    static class viewHolder {

        @Bind(R.id.item_ImageView)
        ImageView mItemImageView;
        @Bind(R.id.item_nameOne)
        TextView mItemNameOne;
        @Bind(R.id.item_nameTwo)
        TextView mItemNameTwo;

        public viewHolder(View view) {
            ButterKnife.bind(this, view);
        }

    }


}
