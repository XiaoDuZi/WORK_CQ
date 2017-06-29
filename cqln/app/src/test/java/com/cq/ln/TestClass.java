package com.cq.ln;

import android.content.Context;
import android.test.InstrumentationTestCase;

import com.cq.ln.utils.StorageUtils;
import com.cq.ln.utils.XLog;

/**
 * Created by Administrator on 2016/2/26.
 */
public class TestClass extends InstrumentationTestCase {

    public void test() throws Exception {
        Context mContext = this.getInstrumentation().getTargetContext().getApplicationContext();

        String cache = StorageUtils.getAppCacheDir(mContext);
        XLog.d(cache);
        String crash = StorageUtils.getAppCrashDir(mContext);
        XLog.d(crash);
    }
}
