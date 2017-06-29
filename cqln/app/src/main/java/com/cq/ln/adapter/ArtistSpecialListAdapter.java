package com.cq.ln.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.cq.ln.R;
import com.cq.ln.utils.ImageTool;

import java.util.ArrayList;
import java.util.List;

import cqdatasdk.bean.ArtistAlbumListBean;
import cqdatasdk.network.URLVerifyTools;

/**
 * Created by Administrator on 2016/3
 */
public class ArtistSpecialListAdapter extends BaseAdapter {

    private Context context;
    private List<ArtistAlbumListBean.DataBean.ListBean> mlist = new ArrayList<>();
    private DisplayImageOptions options;

    public ArtistSpecialListAdapter(Context context) {
        this.context = context;
        options = new DisplayImageOptions.Builder().showImageOnLoading(R.mipmap.artist_def) // 默认
                .showImageForEmptyUri(R.mipmap.artist_def) // URI不存在
                .showImageOnFail(R.mipmap.artist_def) // 加载失败
                .displayer(new RoundedBitmapDisplayer(0)) // 图片圆角
                .cacheInMemory(true).cacheOnDisk(true).considerExifParams(true).imageScaleType(ImageScaleType.NONE_SAFE).bitmapConfig(Bitmap.Config.RGB_565).build();
    }

    public void addDatas(List<ArtistAlbumListBean.DataBean.ListBean> list) {
        if (list == null) {
            return;
        }
        this.mlist.addAll(list);
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mlist.size();
    }

    @Override
    public Object getItem(int position) {
        return mlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_sear_result_gridview_item_list2, null);
            holder.title = (TextView) convertView.findViewById(R.id.item_grid_txt);
            holder.img = (ImageView) convertView.findViewById(R.id.item_grid_ImageView);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ArtistAlbumListBean.DataBean.ListBean bean = this.mlist.get(position);
        if (bean != null) {
            if (!TextUtils.isEmpty(bean.getAlbumName())) {
                holder.title.setText((position + 1) + "." + bean.getAlbumName());
            }
            ImageTool.loadImageWithUrl(context, URLVerifyTools.formatPicListImageUrl(bean.getBigImg()), holder.img, options);
        }

        return convertView;
    }

    private final class ViewHolder {
        public TextView title;
        public ImageView img;
    }
}
