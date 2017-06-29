package com.cq.ln.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.widget.FrameLayout;

import com.cq.ln.R;
import com.cq.ln.utils.XLog;
import com.cq.ln.views.HTML5WebView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 用于首页弹层活动的解决方案，可以是透明，透视首页数据,符合需求
 * 自定义的webview HTML5WebView
 */
public class CustomWebViewFragment extends BaseFragment {


    private static final String TAG = "CustomWebViewActivity";
    @Bind(R.id.frameLayout_loading_html5)
    FrameLayout mFrameLayoutLoadingHtml5;
    private HTML5WebView mWebView;
    private String jsCloseLayerMethod;
    private boolean isKeyBackClose = false;


    private static final String WEB_URL = "WEB_URL";
    private String webUrl;

    public CustomWebViewFragment() {
    }

    public static CustomWebViewFragment newInstance(String webUrl) {
        CustomWebViewFragment fragment = new CustomWebViewFragment();
        Bundle args = new Bundle();
        args.putString(WEB_URL, webUrl);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            webUrl = getArguments().getString(WEB_URL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.custom_webview_layout, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView(savedInstanceState);
        mWebView.addJavascriptInterface(new JavascriptPlugin(), "android");

    }

    private void initView(Bundle savedInstanceState) {
        mWebView = new HTML5WebView(getActivity());
        mWebView.setBackgroundColor(this.getResources().getColor(android.R.color.transparent));

//        webviewUrl = "http://192.168.49.147/HiFi_cq/123.html";//测试
        XLog.d("webView url = " + webUrl);


        mWebView.loadUrl(webUrl);
        mFrameLayoutLoadingHtml5.addView(mWebView.getLayout());


        if (savedInstanceState != null) {
            mWebView.restoreState(savedInstanceState);
        }

    }


    private final class JavascriptPlugin {
        //关闭弹层
        @JavascriptInterface
        public void onClickBack(final String strCallback) {
            jsCloseLayerMethod = strCallback;

        }

        //充值页面逻辑处理方法
        @JavascriptInterface
        public void onResultCallback(String isSucceed, String msg) {
            XLog.d(TAG, "onResultCallback " + "isSucceed = " + isSucceed + " msg = " + msg);
            if (isSucceed.equals("1")) {//
                //TODO 充值成功逻辑


            } else if (isSucceed.equals("0")) {
                //TODO 充值失败，msg携带失败原因
            }
        }

        @JavascriptInterface
        public void onCloseWebView(String isClose) {
            XLog.d(TAG, "onCloseWebView " + "isClose = " + isClose);
            if (isClose.equals("1")) {//测试 通过
                getActivity().onBackPressed();//TODO 触发关闭WebView Fragment
            }
        }

        //按返回键关闭WebView
        @JavascriptInterface
        public void onClickKeyBack() {
            XLog.d(TAG, "onClickKeyBack ");
            isKeyBackClose = true;
        }

    }


    /**
     * 自定义回退事件
     *
     * @param manager
     */
    public void onCustomerKayBack(FragmentManager manager) {
        if (jsCloseLayerMethod != null && !jsCloseLayerMethod.equals("0")) {
            String jsString = "javascript:" + jsCloseLayerMethod;
            mWebView.loadUrl(jsString);

        } else if (isKeyBackClose) {//Fragment 默认是Activity 管理，回退通过popBackStack

            if (manager != null) {
                manager.popBackStack();
                getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        } else if (mWebView.canGoBack()) {
            mWebView.goBack(); // goBack()表示返回WebView的上一页面.

        } else {//Fragment 默认是Activity 管理，回退通过popBackStack
            if (manager != null) {
                manager.popBackStack();
                getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        }
    }


    @Override
    public void onDestroy() {
        mWebView.destroy();
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    public void onPause() {
        super.onPause();
        mWebView.onPause();
    }

    public void onResume() {
        super.onResume();
        mWebView.onResume();
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


}
