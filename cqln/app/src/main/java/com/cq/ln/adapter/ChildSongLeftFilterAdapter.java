package com.cq.ln.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cq.ln.CqApplication;
import com.cq.ln.R;
import com.cq.ln.utils.ImageTool;

import cqdatasdk.bean.ChildAlbumContentBean;
import cqdatasdk.network.URLVerifyTools;

/**
 * Created by fute on 16/9/24.
 */

public class ChildSongLeftFilterAdapter extends HiFiBaseAdapter {

    private Context context;

    public ChildSongLeftFilterAdapter(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_left_filter, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        Object object = mList.get(i);
        ChildAlbumContentBean.DataBean.TrackListBean trackBean = (ChildAlbumContentBean.DataBean.TrackListBean) object;
        holder.tv_name.setText(trackBean.getTrackName() + "");
        ImageTool.loadImageWithUrl(context, URLVerifyTools.formatPicListImageUrl(trackBean.getTrackSmallImg()), holder.iv_img, CqApplication.options);

        view.setTag(R.id.Tag_for_search_LeftFilterList_Databean, object);

        return view;
    }

    static class ViewHolder {
        TextView tv_name;
        ImageView iv_img;

        public ViewHolder(View view) {
            tv_name = (TextView) view.findViewById(R.id.tv_name);
            iv_img = (ImageView) view.findViewById(R.id.iv_img);
        }

    }

}
