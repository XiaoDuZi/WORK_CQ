package com.cq.ln;

import android.content.Context;
import android.test.InstrumentationTestCase;

import com.cq.ln.utils.Tools;

import org.junit.Test;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest extends InstrumentationTestCase {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);

    }

    public void test() throws Exception {

        Context context = this.getInstrumentation().getTargetContext();


        Tools.getAppCacheDir(context);

        Tools.getAppCrashDir(context);
    }

}