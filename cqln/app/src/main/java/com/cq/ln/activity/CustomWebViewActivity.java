package com.cq.ln.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.cq.ln.R;
import com.cq.ln.appConfig.Appconfig;
import com.cq.ln.constant.ConstantEnum;
import com.cq.ln.interfaces.MyDialogEnterListener;
import com.cq.ln.utils.XLog;
import com.cq.ln.views.HTML5WebView;

import butterknife.Bind;
import butterknife.ButterKnife;
import special.DiffHostConfig;

/**
 * Created by Administrator on 2016/4/7.
 * 自定义的webview HTML5WebView
 * 乐视网页有时加载失败 神州行手机播放网页视频有声音没画面
 */
public class CustomWebViewActivity extends BaseActivity {

    private static final String TAG = "CustomWebViewActivity";
    private static final int PLAY_VIDEO = 1000;
    @Bind(R.id.frameLayout_loading_html5)
    FrameLayout mFrameLayoutLoadingHtml5;
    @Bind(R.id.iv_netError)
    ImageView iv_netError;
    private String webviewUrl;
    private HTML5WebView mWebView;
    private String jsCloseLayerMethod;
    private boolean isKeyBackClose = false;
    private BroadcastReceiver infoReceive;

    @SuppressLint("JavascriptInterface")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        showPlaying = false;
        showHomeBg = false;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_webview_layout);
        ButterKnife.bind(this);
        webviewUrl = getIntent().getStringExtra("URL");
        if (TextUtils.isEmpty(webviewUrl)) {
            webviewUrl = DiffHostConfig.homeHost;
        } else if (!webviewUrl.startsWith(DiffHostConfig.ott_host)){
            webviewUrl = DiffHostConfig.ott_host + webviewUrl;
        }
        Log.d("webviewUrl onCreate", "webviewUrl" + webviewUrl);
        webviewUrl = webviewUrl.replace("#", "?");
        initView(savedInstanceState);
        mWebView.addJavascriptInterface(new JavascriptPlugin(), "android");
        initBroadcast();

    }

    protected void onResume() {
        super.onResume();
        mWebView.onResume();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        webviewUrl = intent.getStringExtra("URL");
        if (TextUtils.isEmpty(webviewUrl)) {
            webviewUrl = DiffHostConfig.homeHost;
        } else if (!webviewUrl.startsWith(DiffHostConfig.ott_host)) {
            webviewUrl = DiffHostConfig.ott_host + webviewUrl;
        }
        Log.d("webviewUrl onNewIntent", "webviewUrl" + webviewUrl);
        webviewUrl = webviewUrl.replace("#", "?");
        mWebView.loadUrl(webviewUrl);
    }

    private void initView(Bundle savedInstanceState) {
        mWebView = new HTML5WebView(this);
        mWebView.setBackgroundColor(this.getResources().getColor(android.R.color.transparent));
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);

        XLog.d("webView url = " + webviewUrl);

        mWebView.loadUrl(webviewUrl);
        mFrameLayoutLoadingHtml5.addView(mWebView.getLayout());
        if (savedInstanceState != null) {
            mWebView.restoreState(savedInstanceState);
        }

        mWebView.setWebNetStateistener(new HTML5WebView.WebNetStateistener() {
            @Override
            public void networkError() {
                iv_netError.setVisibility(View.VISIBLE);
            }
        });

    }

    private void initBroadcast() {
        infoReceive = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String cookie = intent.getExtras().getString("CookieString").toString();
                String epg = intent.getExtras().getString("EPG");// epg地址
                String serviceGroupId = "" + intent.getExtras().getLong("ServiceGroupId");//分组号
                String smartcard = intent.getExtras().getString("smartcard");

                String str = "cookie :" + cookie + "\n";
                str += "serviceGroupId :" + serviceGroupId + "\n";
                str += "epg :" + epg + "\n";
                str += "smartcard :" + smartcard + "\n";

                Appconfig.setEpg_url(context, epg);

                Appconfig.setServiceGroupId(serviceGroupId);
                Appconfig.setCookie(context, cookie);

                //TODO 酒店测试屏蔽
                Appconfig.setKeyNo(context, smartcard);

                XLog.d(TAG, "已经接收到更新广播 = " + str);

            }
        };
        this.registerReceiver(infoReceive, new IntentFilter(ConstantEnum.broadcast_getSmartCard));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLAY_VIDEO && resultCode == RESULT_OK) {
            final String cmboIdsArrayStr = data.getStringExtra("cmboIdsArrayStr");
            final int currentIndex = data.getIntExtra("currentIndex", 0);
            final long currentPoint = data.getLongExtra("currentPoint", 0);
            final boolean playEnd = data.getBooleanExtra("playEnd", false);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //调用JS中的 函数，当然也可以不传参
                    if (playEnd) {
                        mWebView.loadUrl("javascript:androidCallBack()");//播放完成回调js
                    } else {
                        mWebView.loadUrl("javascript:androidCallShowOrder(" + cmboIdsArrayStr + "," + currentIndex + "," + currentPoint + ")"); //订购回调js
                    }
                }
            });
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //机顶盒的返回键监听是KEYCODE_ESCAPE
        if (keyCode == KeyEvent.KEYCODE_ESCAPE || keyCode == KeyEvent.KEYCODE_BACK) {
            if (jsCloseLayerMethod != null && !jsCloseLayerMethod.equals("0")) {
                String jsString = "javascript:" + jsCloseLayerMethod;
                mWebView.loadUrl(jsString);
                return true;
            } else if (isKeyBackClose) {
                finish();
            } else if (mWebView.canGoBack()) {
                mWebView.goBack(); // goBack()表示返回WebView的上一页面
                return true;
            } else {
                finish();
                return true;
            }
        }
//        else if (keyCode==KeyEvent.KEYCODE_MENU){
//            hiFiDialogTools.showLeftRightTip(CustomWebViewActivity.this, "温馨提示", "确认退出" + getString(R.string.app_name) + "？", "再玩一会", "退出", new MyDialogEnterListener() {
//                @Override
//                public void onClickEnter(Dialog dialog, Object object) {
//                    if ((int) object == 1) {
//                        finish();
//                    }
//                }
//            });
//            return true;
//        }
//        else if (keyCode==KeyEvent.KEYCODE_MENU){
//            Intent intent = new Intent(CustomWebViewActivity.this, PlayVideoActivity.class);
//            intent.putExtra("playIndex", 0);
//            intent.putExtra("playListJsonString", "[{\"trackId\":\"4585385\",\"initPoint\":\"14\",\"name\":\"测    试1\",\"vodId\":\"4585385\",\"fileType\":\"0\"},{\"trackId\":\"4585385\",
// \"initPoint\":\"14\",\"name\":\"测   试2\",\"vodId\":\"4585385\",\"fileType\":\"0\"},{\"trackId\":\"4585385\",\"initPoint\":\"14\",\"name\":\"测   试3\",\"vodId\":\"4585385\",\"fileType\":\"0\"}]");
//            CustomWebViewActivity.this.startActivityForResult(intent, PLAY_VIDEO);
//        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        if (mWebView != null) {
            ViewGroup parent = (ViewGroup) mWebView.getParent();
            if (parent != null) {
                parent.removeView(mWebView);
            }
            mWebView.removeAllViews();
            mWebView.destroy();
        }
        if (infoReceive != null) {
            this.unregisterReceiver(infoReceive);
        }
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    protected void onPause() {
        super.onPause();
        mWebView.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mWebView.saveState(outState);
    }

    @Override
    public void onStop() {
        super.onStop();
        mWebView.stopLoading();
    }

    private final class JavascriptPlugin {
        //关闭弹层
        @JavascriptInterface
        public void onClickBack(final String strCallback) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    jsCloseLayerMethod = strCallback;
                }
            });
        }

        //充值页面逻辑处理方法
        @JavascriptInterface
        public void onResultCallback(String isSucceed, String msg) {
            XLog.d(TAG, "onResultCallback " + "isSucceed = " + isSucceed + " msg = " + msg);
            if (isSucceed.equals("1")) {//
                //TODO 充值成功逻辑，


            } else if (isSucceed.equals("0")) {
                //TODO 充值失败，msg携带失败原因
            }
        }

        @JavascriptInterface
        public void onCloseWebView(String isClose) {
            XLog.d(TAG, "onCloseWebView " + "isClose = " + isClose);
            if (isClose.equals("1")) {//测试 通过
                finish();//TODO 关闭WebView
            }
        }

        @JavascriptInterface
        public String getKeyNo() {
            //web页面通过这个方法，获取智能卡号
            return Appconfig.getKeyNo(getApplicationContext());
        }

        //按返回键关闭WebView
        @JavascriptInterface
        public void onClickKeyBack() {
            XLog.d(TAG, "onClickKeyBack ");
            Log.e(TAG, "onClickKeyBack: ");
            isKeyBackClose = true;
        }

        //退出app
        @JavascriptInterface
        public void exitApp() {
            Log.e(TAG, "exitApp: ");
            hiFiDialogTools.showLeftRightTip(CustomWebViewActivity.this, "温馨提示", "确认退出" + getString(R.string.app_name) + "？", "再玩一会", "退出", new MyDialogEnterListener() {
                @Override
                public void onClickEnter(Dialog dialog, Object object) {
                    if ((int) object == 1) {
                        finish();
                    }
                }
            });
        }

        //吊起本地播放视频
        @JavascriptInterface
        public void startPlayVideo(int playIndex, String playListJsonString) {
            Intent intent = new Intent(CustomWebViewActivity.this, PlayVideoActivity.class);
            intent.putExtra("playIndex", playIndex);
            intent.putExtra("playListJsonString", playListJsonString);
            CustomWebViewActivity.this.startActivityForResult(intent, PLAY_VIDEO);
        }
    }

}
