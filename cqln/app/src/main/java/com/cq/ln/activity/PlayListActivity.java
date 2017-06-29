package com.cq.ln.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.cq.ln.R;

/**
 * Created by fute on 17/1/11.
 */

public class PlayListActivity extends BaseActivity {

    private ListView lv_play;
    private String[] items;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        showPlaying = false;
        showHomeBg = false;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_list);

//        WindowManager.LayoutParams lp = getWindow().getAttributes();
//        lp.width = (int)getResources().getDimension(R.dimen.dp360);
//        lp.gravity = Gravity.LEFT;
//        getWindow().setAttributes(lp);

        items = getIntent().getStringArrayExtra("titleArr");
        lv_play = (ListView) findViewById(R.id.lv_play);
        lv_play.setAdapter(new ItemAdapter());
        lv_play.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent dataIntent = new Intent();
                dataIntent.putExtra("playIndex", i);
                setResult(RESULT_OK, dataIntent);
                finish();
            }
        });
    }

    class ItemAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return items.length;
        }

        @Override
        public Object getItem(int i) {
            return items[i];
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View contentView = View.inflate(PlayListActivity.this, R.layout.item_play_list, null);
            TextView tv_name = (TextView) contentView;
            tv_name.setText(items[i]);
            return contentView;
        }
    }


}
