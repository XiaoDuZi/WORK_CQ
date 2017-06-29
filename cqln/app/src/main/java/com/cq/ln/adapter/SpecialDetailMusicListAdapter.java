package com.cq.ln.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cq.ln.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import cqdatasdk.bean.SpecialDetailBean;

/**
 * Created by godfather on 2016/3/21.
 * 专辑详情List
 */
public class SpecialDetailMusicListAdapter extends HiFiBaseAdapter {
    private Context context;

    public SpecialDetailMusicListAdapter(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        viewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_specaildetail_list, null);
            holder = new viewHolder(view);
            view.setTag(holder);
        } else
            holder = (viewHolder) view.getTag();
        SpecialDetailBean.DataBean.TrackListBean bean = (SpecialDetailBean.DataBean.TrackListBean) mList.get(i);
        holder.mTxtView.setText((i + 1) + "." + bean.getTrackName());

        return view;
    }

    static class viewHolder {
        @Bind(R.id.txtName)
        TextView mTxtView;

        public viewHolder(View view) {
            ButterKnife.bind(this, view);
        }

    }
}
