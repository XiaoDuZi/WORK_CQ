package com.cq.ln.adapter;

import android.content.Context;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by YS on 2016/2/26.
 * 类说明：所有的Adapter请继承此基类,以实现精简代码风格
 */
public abstract class HiFiBaseAdapter<T> extends BaseAdapter {

    public ArrayList<T> mList = new ArrayList<>();
    public Context mContext;

    public HiFiBaseAdapter(Context context) {
        this.mContext = context;
    }


    @Override
    public int getCount() {
        return mList.size();
    }

    public List<T> getlist() {
        return mList;
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    //向list中添加数据
    public void refreshAdapter(List<T> arrayList) {
        mList.addAll(arrayList);
        notifyDataSetChanged();
    }

    /**
     * 清空list集合,避免使用此方法，请用clearButMustUsedWithRefresh 和refreshAdapter 配合使用替代，有效减少Adapter的刷新次数
     */
    public void clear() {
        mList.clear();
        notifyDataSetChanged();
    }

    /**
     * 清空，但是请与refreshAdapter（）方法一起使用，才能生效，否则无效！！非常重要
     * <br>mList.clear();并没有调用notifyDataSetChanged();</br>
     */
    public void clearButMustUsedWithRefresh() {
        mList.clear();
    }

    //移除指定位置的对象
    public void remove(int index) {
        mList.remove(index);
        notifyDataSetChanged();
    }

    //移除对象
    public void remove(Object obj) {
        mList.remove(obj);
        notifyDataSetChanged();
    }

}
