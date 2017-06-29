package com.cq.ln.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cq.ln.R;

import java.util.ArrayList;
import java.util.List;

import cqdatasdk.bean.RecentPlayBean;

/**
 * Created by Administrator on 2016/3/24.
 */
public class UserCenterRecentPlayAdapter extends BaseAdapter {

    Context context;
    //private int fromWhere;
    private List<RecentPlayBean.DataBean.ListBean> mlist = new ArrayList<>();
    public UserCenterRecentPlayAdapter(Context context){
        this.context = context;
    }

    public void addDatas(List<RecentPlayBean.DataBean.ListBean> list){
        if(list==null){
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
         //   convertView = mInflater.inflate(R.layout.item_listview, null);
            convertView = LayoutInflater.from(context).inflate(R.layout.item_listview, null);
            holder.title = (TextView) convertView.findViewById(R.id.textview_title);
            holder.name = (TextView) convertView.findViewById(R.id.textview_describe);
            holder.time = (TextView) convertView.findViewById(R.id.textview_time);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
//            if(fromWhere== ConstantEnum.LATELY_PLAY2){
//        //        holder.title.setText("来自最近播放"+data.get(position));
//            }else if(fromWhere == ConstantEnum.collectionType_single){
//         //       holder.title.setText("来自单曲收藏"+data.get(position));
//            }else if(fromWhere == ConstantEnum.collectionType_Specail){
//        //        holder.title.setText("来自专辑收藏"+data.get(position));
//            }
        RecentPlayBean.DataBean.ListBean  mResultBean = this.mlist.get(position);
        if(mResultBean != null){
            holder.title.setText((position+1)+"."+mResultBean.getAlbumName());
            if(!TextUtils.isEmpty(mResultBean.getTrackName())){
                holder.name.setText(mResultBean.getTrackName());
            }
            if(!TextUtils.isEmpty(mResultBean.getDuration())){
                holder.time.setText(mResultBean.getDuration());
            }
        }
        return convertView;
    }

    private final class ViewHolder {
        public TextView title;
        public TextView name;
        public TextView time;
    }
}
