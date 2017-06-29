package com.cq.ln.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.cq.ln.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import cqdatasdk.bean.CollectionSinglebean;

/**
 * Created by Godfather on 16/4/22.
 * 收藏的单曲
 */
public class CollectionSingleAdapter extends HiFiBaseAdapter {
    private Context context;
    private DisplayImageOptions options;

    public CollectionSingleAdapter(Context context) {
        super(context);
        this.context = context;

    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        viewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_collection_single_recent_play, null);
            holder = new viewHolder(view);
            view.setTag(holder);
        } else
            holder = (viewHolder) view.getTag();
        CollectionSinglebean.DataBean.ListBean.ResultBean bean = (CollectionSinglebean.DataBean.ListBean.ResultBean) mList.get(i);


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

        holder.mTxtMusicName.setText(bean.getTrackName());
        holder.mTxtOne.setText("艺术家:" + bean.getArtistName());
        holder.mTxttwo.setText("专辑:" + bean.getAlbumName());
        holder.mTxtDuration.setText(bean.getDuration());

        return view;
    }

    static class viewHolder {

        @Bind(R.id.txtMusicName)
        TextView mTxtMusicName;
        @Bind(R.id.txtOne)
        TextView mTxtOne;
        @Bind(R.id.txttwo)
        TextView mTxttwo;
        @Bind(R.id.txtDuration)
        TextView mTxtDuration;

        public viewHolder(View view) {
            ButterKnife.bind(this, view);
        }

    }
}
