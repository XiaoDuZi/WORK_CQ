package com.cq.ln.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.cq.ln.R;
import com.cq.ln.appConfig.Appconfig;
import com.cq.ln.utils.HiFiDialogTools;
import com.cq.ln.utils.ImageTool;
import com.cq.ln.utils.Tools;

import butterknife.Bind;
import butterknife.ButterKnife;
import cqdatasdk.network.URLVerifyTools;

/**
 * 订购有礼
 */
public class OrderGiftActivity extends BaseActivity {

    @Bind(R.id.order_gift_img)
    ImageView mOrderGiftImg;
    @Bind(R.id.show_balance_text)
    TextView mShowBalanceText;
    @Bind(R.id.old_price_text)
    TextView mOldPriceText;
    @Bind(R.id.order_gift_button)
    Button mOrderGiftButton;

    private String imgUrl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_gift_layout);
        ButterKnife.bind(this);
        imgUrl = getIntent().getStringExtra("imgUrl");
        imgUrl = "http://www.pp3.cn/uploads/allimg/111110/114J0L31-5.jpg";

        setDataToView();

    }

    private void setDataToView() {
        mOrderGiftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // ActivityUtility.goCustomWebView(OrderGiftActivity.this, "");
                goOrder();
            }
        });
        ImageTool.loadImage(getApplicationContext(), mOrderGiftImg, URLVerifyTools.formatPicListImageUrl(imgUrl), 0, R.mipmap.middle, null, null);
        String balance = "我的广电账户余额是：" + Appconfig.getbalance(getApplicationContext());
        // mShowBalanceText.setText(balance);
    }

    private void goOrder() {
        if (Appconfig.isOrderMusicServer(getApplicationContext())) {
            Tools.showTip(OrderGiftActivity.this, "您已经订购了");
            return;
        } else {

            HiFiDialogTools hiFiDialogTools = new HiFiDialogTools();
            hiFiDialogTools.showPlaceOrder(this, null);

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }


}
