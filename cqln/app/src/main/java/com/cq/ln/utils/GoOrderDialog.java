package com.cq.ln.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.cq.ln.R;
import com.cq.ln.interfaces.OnSubmintClickListener;


/**
 * 自定义Dialog 去订购
 */
public class GoOrderDialog extends Dialog {
    private static Dialog dialog, listDialog;
    private static ListView dataList;
    private static Dialog DoloadDialog;
    private boolean canCancel = true;

    public GoOrderDialog(Context ctx, int res, boolean cancelable) {
        super(ctx, R.style.MyDialog);
        this.canCancel = cancelable;
        setContentView(res);
    }

    public static void Close() {
        if (dialog != null)
            dialog.dismiss();
    }

    /**
     * @param context  弹出简介提示信息
     */
    public static void showAbstructView(Context context, final OnSubmintClickListener listener) {
        if (dialog != null)
            dialog.dismiss();

        dialog = new GoOrderDialog(context, R.layout.dialog_go_order, true);
       // TextView txtAbstruct = (TextView) dialog.findViewById(R.id.txtAbstruct);
      //  if (txtAbstruct != null && !TextUtils.isEmpty(abstruct))
      //      txtAbstruct.setText("\u3000\u3000" + abstruct);//首行缩进
        LinearLayout warp = (LinearLayout) dialog.findViewById(R.id.parentWarp);
        //context.getResources().getDimensionPixelOffset(R.dimen.dp388)
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(Tools.getDisplayWidth((Activity) context), FrameLayout.LayoutParams.WRAP_CONTENT);
        warp.setLayoutParams(lp);


        TextView titleView =  (TextView) dialog.findViewById(R.id.title);
        //titleView.setText(DialogUtil.parse(context, titleText));

        Button leftButton = (Button) dialog.findViewById(R.id.left);
      //  leftButton.setText(DialogUtil.parse(context, cancelText));
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClick(dialog, 0, new String(""));
                }
            }
        });
        Button rightButton = (Button) dialog.findViewById(R.id.right);
      //  rightButton.setText(DialogUtil.parse(context, submitText));
        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClick(dialog, 1,null);
                }
            }
        });

        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.show();

    }

}
