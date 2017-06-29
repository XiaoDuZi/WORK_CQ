package cqdatasdk.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cq.ln.R;


/**
 * 自定义动画
 *
 * @author Administrator
 */
public class CusProgress extends Dialog {
    private static CusProgress dialog;
    private boolean outsideCancel = false;
    private boolean keybackCancel = false;
    private static ImageView imaView;
    private static AnimationDrawable aniDra;
    private static TextView txt;
    private static Context context;


    public interface OnMyDismissListener {
        void dismiss();
    }


    public static CusProgress getInstance(Context ctx) {
        context = ctx;
        if (dialog == null)
            dialog = new CusProgress(ctx, true, true);
        return dialog;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setCancelable(this.keybackCancel);
        super.setCanceledOnTouchOutside(outsideCancel);
    }

    public CusProgress(Context context, boolean outsideCancel, boolean keybackCancel) {
        super(context, R.style.MyDialog);

        this.outsideCancel = outsideCancel;
        this.keybackCancel = keybackCancel;
        setContentView(R.layout.dialog_net);


    }


    /**
     * @param info
     * @param callback
     * @throws Exception
     */
    public void show(String info, final OnMyDismissListener callback) throws Exception {
        if (txt == null)
            txt = (TextView) dialog.findViewById(R.id.textviewinfo);
        if (imaView == null)
            imaView = (ImageView) dialog.findViewById(R.id.image);
        if (aniDra == null)
            aniDra = (AnimationDrawable) imaView.getDrawable();

        if (txt != null)
            if (info == null) {
                txt.setVisibility(View.INVISIBLE);
                ((View) txt.getParent()).setBackgroundColor(0);
            } else if (info.length() > 0) {
                txt.setText(info);
            }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            if (context != null && !((Activity) context).isFinishing() && dialog != null && !dialog.isShowing() && !(((Activity) context).isDestroyed())) {
                dialog.show();
                if (aniDra != null)
                    aniDra.start();
            }
        } else {
            if (context != null && !((Activity) context).isFinishing() && dialog != null && !dialog.isShowing()) {
                dialog.show();
                if (aniDra != null)
                    aniDra.start();
            }
        }

        dialog.setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss(DialogInterface dialog) {
                close();
                if (callback != null)
                    callback.dismiss();
            }
        });
    }

    public void close() {
        if (dialog != null)
            dialog.dismiss();
    }


}
