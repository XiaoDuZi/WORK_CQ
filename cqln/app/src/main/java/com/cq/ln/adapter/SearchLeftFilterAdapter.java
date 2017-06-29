package com.cq.ln.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cq.ln.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import cqdatasdk.bean.ArtistEnglishInitialFilterBean;
import cqdatasdk.bean.DiscRecommendFilterBean;
import cqdatasdk.bean.MusicStyleFilterBean;

/**
 * Created by godfather on 2016/3/20.
 * 搜索页面左边的过滤刷选的Adapter（唱片推荐，音乐风格，艺术家三个通用）
 */
public class SearchLeftFilterAdapter extends HiFiBaseAdapter {

    private Context context;

    public SearchLeftFilterAdapter(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        viewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_filter, null);
            holder = new viewHolder(view);
            view.setTag(holder);
        } else
            holder = (viewHolder) view.getTag();

        Object object = mList.get(i);

        if (object instanceof ArtistEnglishInitialFilterBean) {//艺术家
            ArtistEnglishInitialFilterBean bean = (ArtistEnglishInitialFilterBean) object;
            holder.mTxtView.setText(bean.englishInitial);
        } else if (object instanceof DiscRecommendFilterBean.DataBean) {//唱片推荐
            DiscRecommendFilterBean.DataBean bean = (DiscRecommendFilterBean.DataBean) object;
            holder.mTxtView.setText(bean.getName());
        } else if (object instanceof MusicStyleFilterBean.DataBean) {//音乐风格
            MusicStyleFilterBean.DataBean bean = (MusicStyleFilterBean.DataBean) object;
            holder.mTxtView.setText(bean.getName());
        }
        view.setTag(R.id.Tag_for_search_LeftFilterList_Databean, object);

        return view;
    }

    static class viewHolder {
        @Bind(R.id.txtView)
        TextView mTxtView;

        public viewHolder(View view) {
            ButterKnife.bind(this, view);
        }

    }


}
