package com.cq.ln.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cq.ln.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import cqdatasdk.bean.RecentPlayBean;

/**
 * Created by Godfather on 16/4/22.
 * 最近播放
 */
public class CollectionRecenPlayAdapter extends HiFiBaseAdapter {
    private Context context;

    public CollectionRecenPlayAdapter(Context context) {
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


        RecentPlayBean.DataBean.ListBean bean = (RecentPlayBean.DataBean.ListBean) mList.get(i);


        holder.mTxtMusicName.setText(bean.getTrackName());
        holder.mTxtOne.setText("专辑:" + bean.getAlbumName());
        holder.mTxttwo.setText("艺术家:" + bean.getArtistName());
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
