package com.cq.ln.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.text.TextUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cq.ln.R;
import com.cq.ln.interfaces.MyDialogEnterListener;


/**
 * loading loadingDialogTools
 */
public class LoadingDialogTools {
    private Dialog loadingDialog;

    public void showloadingDialog(Context context, String loadingString, final MyDialogEnterListener listener) {

        loadingDialog = new Dialog(context, R.style.MyDialog);
        loadingDialog.setContentView(R.layout.dialog_net);

        RelativeLayout warp = (RelativeLayout) loadingDialog.findViewById(R.id.parentWarp);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(Tools.getDisplayWidth((Activity) context), Tools.getDisplayHeight((Activity) context));
        warp.setLayoutParams(lp);
        loadingDialog.setCanceledOnTouchOutside(false);
        loadingDialog.setCancelable(true);//返回键可以取消
        ImageView imaView = (ImageView) loadingDialog.findViewById(R.id.image);
        TextView textView = (TextView) loadingDialog.findViewById(R.id.textviewinfo);
        AnimationDrawable aniDra = (AnimationDrawable) imaView.getDrawable();
        if (!TextUtils.isEmpty(loadingString) && textView != null)
            textView.setText(loadingString);
        if (aniDra != null)
            aniDra.start();
        safeShowDialog(context, loadingDialog);
    }

    /**
     * @param context 安全方式showDialog
     * @param dialog
     */
    private void safeShowDialog(Context context, Dialog dialog) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                if (context != null && !((Activity) context).isFinishing() && dialog != null && !dialog.isShowing() && !(((Activity) context).isDestroyed()))
                    dialog.show();
            } else {
                if (context != null && !((Activity) context).isFinishing() && dialog != null && !dialog.isShowing())
                    dialog.show();
            }
        } catch (Exception e) {
            XLog.d("e:" + e);
            e.printStackTrace();
        }
    }

    public void Close() {
        XLog.d("close Loading");
        if (loadingDialog != null)
            loadingDialog.dismiss();
    }

}
