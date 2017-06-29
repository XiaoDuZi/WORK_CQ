package com.cq.ln.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.cq.ln.R;

import java.net.URL;

/**
 * Created by fute on 17/1/22.
 */

public class WebLauncherActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_launcher);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Uri uri = getIntent().getData();
        String targetUrl = uri.getQueryParameter("targetUrl");
        targetUrl = Uri.decode(targetUrl);
        Intent intent = new Intent(this, CustomWebViewActivity.class);
        intent.putExtra("targetUrl", targetUrl);
        startActivity(intent);

        finish();

    }
}
