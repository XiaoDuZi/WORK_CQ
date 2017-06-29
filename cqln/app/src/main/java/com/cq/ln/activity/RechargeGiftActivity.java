package com.cq.ln.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.cq.ln.R;
import com.cq.ln.utils.ActivityUtility;
import com.cq.ln.utils.ImageTool;

import butterknife.Bind;
import butterknife.ButterKnife;
import cqdatasdk.network.URLVerifyTools;

/**
 * 充值有礼
 */
public class RechargeGiftActivity extends BaseActivity {

    @Bind(R.id.recharge_gift_img)
    ImageView mRechargeGiftImg;
    @Bind(R.id.show_balance_text)
    TextView mShowBalanceText;
    @Bind(R.id.recharge_gift_button)
    Button mRechargeGiftButton;
    private String imgUrl;

    @SuppressLint("JavascriptInterface")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recharge_gift_layout);
        ButterKnife.bind(this);
        imgUrl = getIntent().getStringExtra("imgUrl");
        imgUrl = "http://www.pp3.cn/uploads/allimg/111110/114J0L31-5.jpg";

        setDataToView();
        // ActivityUtility.goPlay(this, HiFiApplication.getInstance().getUserInfo(), new MyObject());

    }

    private void setDataToView() {
        mRechargeGiftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityUtility.goCustomWebView(RechargeGiftActivity.this, "http://www.baidu.com");
            }
        });
        ImageTool.loadImage(getApplicationContext(), mRechargeGiftImg, URLVerifyTools.formatPicListImageUrl(imgUrl), 0, R.mipmap.middle, null, null);
        String balance = "我的广电账户余额是：xx元";
        mShowBalanceText.setText(balance);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }


    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            //Tools.showTip(CustomWebViewActivity.this, "event.getKeyCode()=="+event.getKeyCode());
            if (event.getKeyCode() == KeyEvent.FLAG_KEEP_TOUCH_MODE) {

            }
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
        }
        return super.onKeyDown(keyCode, event);
    }


}
