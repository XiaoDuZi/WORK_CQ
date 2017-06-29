package com.cq.ln.views;


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.cq.ln.R;

public class HTML5WebView extends WebView {

    //	private boolean mLoading;
    private static final String LOGTAG = "HTML5WebView";
    private static final FrameLayout.LayoutParams COVER_SCREEN_PARAMS = new FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT);
    private Context mContext;
    private MyWebChromeClient mWebChromeClient;
    private View mCustomView;
    private FrameLayout mCustomViewContainer;
    private WebChromeClient.CustomViewCallback mCustomViewCallback;
    private FrameLayout mContentView;
    private FrameLayout mBrowserFrameLayout;
    private FrameLayout mLayout;
    //private Bitmap mDefaultVideoPoster;
//	private View mVideoProgressView;
    private ProgressBar mProgressBar;
    private WebNetStateistener webNetStateistener;

    public HTML5WebView(Context context) {
        super(context);
        init(context);
    }

    public HTML5WebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public HTML5WebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        Activity activity = (Activity) mContext;

        mLayout = new FrameLayout(context);
        mLayout.setBackgroundColor(this.getResources().getColor(android.R.color.transparent));

        mBrowserFrameLayout = (FrameLayout) LayoutInflater.from(activity).inflate(R.layout.custom_screen, null);
        mContentView = (FrameLayout) mBrowserFrameLayout.findViewById(R.id.main_content);
        mCustomViewContainer = (FrameLayout) mBrowserFrameLayout.findViewById(R.id.fullscreen_custom_content);

        mLayout.addView(mBrowserFrameLayout, COVER_SCREEN_PARAMS);

        // if (mVideoProgressView == null) {
        // LayoutInflater inflater = LayoutInflater.from(mContext);
        // mVideoProgressView =
        // inflater.inflate(R.layout.video_loading_progress, null);
        // }
        mProgressBar = (ProgressBar) mBrowserFrameLayout
                .findViewById(R.id.webview_progressbar2);

        mWebChromeClient = new MyWebChromeClient();
        setWebChromeClient(mWebChromeClient);
        setWebViewClient(new MyWebViewClient());

        // Configure the webview
        WebSettings mWebSettings = getSettings();
        mWebSettings.setBuiltInZoomControls(true);

        mWebSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        mWebSettings.setUseWideViewPort(true);
        mWebSettings.setLoadWithOverviewMode(true);

        mWebSettings.setSavePassword(true);
        mWebSettings.setSaveFormData(true);
        mWebSettings.setJavaScriptEnabled(true);

        // enable navigator.geolocation
        mWebSettings.setGeolocationEnabled(true);
        mWebSettings.setGeolocationDatabasePath("/data/data/org.itri.html5webview/databases/");

        // enable Web Storage: localStorage, sessionStorage
        mWebSettings.setDomStorageEnabled(true);

        mContentView.addView(this);
    }

    public FrameLayout getLayout() {
        return mLayout;
    }

    public boolean inCustomView() {
        return (mCustomView != null);
    }

    public void hideCustomView() {
        mWebChromeClient.onHideCustomView();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((mCustomView == null) && canGoBack()) {
                goBack();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private void showOrDismissLoading(boolean isShowing, View dialog) {
        if (dialog != null)
            dialog.setVisibility(isShowing ? View.VISIBLE : View.GONE);
    }

    private class MyWebChromeClient extends WebChromeClient {
        // private Bitmap mDefaultVideoPoster;
        // private View mVideoProgressView;

        @Override
        public void onShowCustomView(View view, CustomViewCallback callback) {
            // Log.i(LOGTAG, "here in on ShowCustomView");
            HTML5WebView.this.setVisibility(View.GONE);

            // if a view already exists then immediately terminate the new one
            if (mCustomView != null) {
                callback.onCustomViewHidden();
                return;
            }

            mCustomViewContainer.addView(view);
            mCustomView = view;
            mCustomViewCallback = callback;
            mCustomViewContainer.setVisibility(View.VISIBLE);
        }

        @Override
        public void onHideCustomView() {

            if (mCustomView == null)
                return;

            // Hide the custom view.
            mCustomView.setVisibility(View.GONE);

            // Remove the custom view from its container.
            mCustomViewContainer.removeView(mCustomView);
            mCustomView = null;
            mCustomViewContainer.setVisibility(View.GONE);
            mCustomViewCallback.onCustomViewHidden();

            HTML5WebView.this.setVisibility(View.VISIBLE);

            // Log.i(LOGTAG, "set it to webVew");
        }

//		@Override
//		public Bitmap getDefaultVideoPoster() {
//			// Log.i(LOGTAG, "here in on getDefaultVideoPoster");
//			if (mDefaultVideoPoster == null) {
//				mDefaultVideoPoster = BitmapFactory.decodeResource(
//						getResources(), R.drawable.default_video_poster);
//			}
//			return mDefaultVideoPoster;
//		}

        @Override
        public View getVideoLoadingProgressView() {
            // Log.i(LOGTAG, "here in on getVideoLoadingPregressView");
            // if (mVideoProgressView == null) {
            // LayoutInflater inflater = LayoutInflater.from(mContext);
            // mVideoProgressView =
            // inflater.inflate(R.layout.video_loading_progress, null);
            // }
            // return mVideoProgressView;

            return mProgressBar;
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            ((Activity) mContext).setTitle(title);
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            // ((Activity)
            // mContext).getWindow().setFeatureInt(Window.FEATURE_PROGRESS,
            // newProgress*100);
            if (newProgress != 100) {
                mProgressBar.setVisibility(View.VISIBLE);
                // mProgressBar.setProgress(newProgress);
            } else {
                mProgressBar.setVisibility(View.GONE);
            }
            // mProgressBar.setProgress(newProgress);
        }

        @Override
        public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
            callback.invoke(origin, true, false);
        }

    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.i(LOGTAG, "shouldOverrideUrlLoading: " + url);
            // don't override URL so that stuff within iframe can work properly
             view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            Log.d("log", "onPageStarted " + url);
            showOrDismissLoading(true, mProgressBar);
            //		mLoading = true;
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            Log.d("log", "onPageFinished " + url);
            showOrDismissLoading(false, mProgressBar);
//			mLoading = false;
            super.onPageFinished(view, url);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode,
                                    String description, String failingUrl) {
            // Toast.makeText(mContext, "加载失败，请检查网络", 0).show();
            super.onReceivedError(view, errorCode, description, failingUrl);

            if (webNetStateistener!=null){
                webNetStateistener.networkError();
            }

        }
    }

    public interface WebNetStateistener{
        void networkError();
    }

    public void setWebNetStateistener(WebNetStateistener webNetStateistener) {
        this.webNetStateistener = webNetStateistener;
    }
}