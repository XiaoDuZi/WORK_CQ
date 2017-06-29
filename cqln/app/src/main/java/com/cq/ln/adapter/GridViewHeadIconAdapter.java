package com.cq.ln.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;

import com.cq.ln.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/4/12.
 */
public class GridViewHeadIconAdapter extends BaseAdapter {

    private Context mContext;
    private List<Integer> list = new ArrayList<>();

    public GridViewHeadIconAdapter(Context cxt){
        this.mContext = cxt;
    }

    public void addListData(List<Integer> list){
        if(list==null || list.isEmpty()){
            return;
        }
        this.list.addAll(list);
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if(null == convertView){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.head_icon_gridview_item, null);
            holder.mImageView = (ImageButton) convertView.findViewById(R.id.head_icon_img);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }

        holder.mImageView.setImageResource(list.get(position));

        return convertView;
    }

    private final static class ViewHolder{
        //ImageButton ImageView
        private ImageButton mImageView;
    }

}
