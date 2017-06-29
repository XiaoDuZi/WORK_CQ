package com.cq.ln.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.cq.ln.R;
import com.cq.ln.appConfig.Appconfig;
import com.cq.ln.utils.Tools;

import cqdatasdk.bean.SetBackgroundMusicBean;
import cqdatasdk.interfaces.IVolleyRequestSuccess;
import cqdatasdk.interfaces.IVolleyRequestfail;
import cqdatasdk.network.AsyncHttpRequest;

/**
 * Created by apple on 16/11/3.
 */

public class SetBackGroundMusicActivity extends BaseActivity implements IVolleyRequestSuccess, IVolleyRequestfail {
    public String trackId;
    private EditText inputServer;
    private String KeyNo;
    private Button ownerKeyNoBt;
    private AsyncHttpRequest asyncHttpRequest;
    private TextView tipView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        showPlaying = false;
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_go_setbackgroundmusic);
        getWindow().setBackgroundDrawable(null);
        trackId = getIntent().getStringExtra("TRACKID");
        asyncHttpRequest = new AsyncHttpRequest();
        ownerKeyNoBt = (Button) findViewById(R.id.setOnwer);
        if (trackId.equals(Appconfig.getTrackid(SetBackGroundMusicActivity.this))){
            ownerKeyNoBt.setText("已设置为自己背景音乐");
            ownerKeyNoBt.setClickable(false);
        }
        inputServer = (EditText) findViewById(R.id.setfriend);
        tipView = (TextView) findViewById(R.id.bottom);

        Button sureBt = (Button) findViewById(R.id.center);
        sureBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Tools.isFastClick(400))//防止快速点击
                    return;
                String inuptString = inputServer.getText().toString();
                if (inuptString.length() < 10 || inuptString.length() == 0) {
                    tipView.setText("对不起  您输入的卡号不正确，请确认后再输入!");
                    tipView.setVisibility(View.VISIBLE);
                    return;
                }

                KeyNo = inuptString;
                asyncHttpRequest.setBackGroundMusic(SetBackGroundMusicActivity.this, trackId,inuptString,SetBackGroundMusicActivity.this,SetBackGroundMusicActivity.this);

            }
        });

        ownerKeyNoBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Tools.isFastClick(400))//防止快速点击
                    return;
                KeyNo = Appconfig.getKeyNo(SetBackGroundMusicActivity.this);
                asyncHttpRequest.setBackGroundMusic(SetBackGroundMusicActivity.this, trackId,Appconfig.getKeyNo(getApplicationContext()),SetBackGroundMusicActivity.this,SetBackGroundMusicActivity.this);
            }
        });
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        WindowManager.LayoutParams p = getWindow().getAttributes();
        p.width = metric.widthPixels;
        getWindow().setAttributes(p);

    }

    @Override
    public void onSucceeded(String method, String key, Object object) throws Exception {

        if (KeyNo.equals(Appconfig.getKeyNo(SetBackGroundMusicActivity.this))){
            if (object == null)return;
            SetBackgroundMusicBean bean = ((SetBackgroundMusicBean) object);
            if (bean.getCode().equals("0")){
                Appconfig.setTrackid(SetBackGroundMusicActivity.this,trackId);
                ownerKeyNoBt.setText("已设置为自己背景音乐");
                tipView.setText("设置自己背景音乐成功!");
                tipView.setVisibility(View.VISIBLE);
            }else {
                tipView.setText("设置失败,请重试");
                tipView.setVisibility(View.VISIBLE);
            }
        }else {
            if (object == null)return;
            SetBackgroundMusicBean bean = ((SetBackgroundMusicBean) object);
            if (bean.getCode().equals("0")){
                tipView.setText("设置朋友背景音乐成功!");
                tipView.setVisibility(View.VISIBLE);
            }else {
                tipView.setText(bean.getMsg());
                tipView.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onFailed(String method, String key, int errorTipId) throws Exception {

    }
}
