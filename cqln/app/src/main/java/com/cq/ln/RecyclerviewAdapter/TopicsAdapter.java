package com.cq.ln.RecyclerviewAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import com.cq.ln.CqApplication;
import com.cq.ln.R;
import com.cq.ln.interfaces.onRecyclerViewItemSelectedListener;
import com.cq.ln.utils.ImageTool;
import java.util.ArrayList;
import java.util.List;
import cqdatasdk.bean.TopicsBean;
import cqdatasdk.network.URLVerifyTools;

/**
 * Created by Godfather on 16/5/16.
 */
public class TopicsAdapter extends RecyclerView.Adapter<MyViewHolder> {

    private Context context;
    private List<TopicsBean.ResultBean> mlist = new ArrayList<>();
    private onRecyclerViewItemSelectedListener listener;

    public TopicsAdapter(Context context) {
        this.context = context;
    }

    public List<TopicsBean.ResultBean> getMlist() {
        return mlist;
    }

    public void setMlist(List<TopicsBean.ResultBean> mlist) {
        this.mlist = mlist;
    }

    public onRecyclerViewItemSelectedListener getListener() {
        return listener;
    }

    public void setListener(onRecyclerViewItemSelectedListener listener) {
        this.listener = listener;
    }

    public void refresh(List<TopicsBean.ResultBean> mlist) {
        this.mlist = mlist;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View inflate = LayoutInflater.from(context).inflate(R.layout.item_topics, null);
        inflate.findViewById(R.id.item_Topics_ImageButton).setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (listener != null) {
                    listener.onItemFocus(hasFocus, v);
                }
            }
        });
        inflate.findViewById(R.id.item_Topics_ImageButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = (int)view.getTag();
                if (listener != null) {
                    listener.onItemClick(position);
                }
            }
        });
        return new MyViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        TopicsBean.ResultBean bean = mlist.get(position);
        holder.getItemView().findViewById(R.id.item_Topics_ImageButton).setTag(position);
        ImageTool.loadImageAndUserHandleOption(context, URLVerifyTools.formatMainImageUrl(bean.getSmallImg()), holder.mItemTopicsImageButton, CqApplication.options, -1);
    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }
}


class MyViewHolder extends RecyclerView.ViewHolder {
    ImageButton mItemTopicsImageButton;
    View itemView;
    public MyViewHolder(View itemView) {
        super(itemView);
        this.itemView = itemView;
        mItemTopicsImageButton = (ImageButton) itemView.findViewById(R.id.item_Topics_ImageButton);
    }

    public View getItemView() {
        return itemView;
    }
}