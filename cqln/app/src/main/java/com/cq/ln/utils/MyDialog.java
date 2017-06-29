package com.cq.ln.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cq.ln.R;
import com.cq.ln.interfaces.MyDialogEnterListener;


/**
 * 自定义Dialog------->引起的内存泄露，暂无使用
 */
public class MyDialog extends Dialog {
    private static Dialog dialog, netLoadingDialog;
    private static ListView dataList;
    private static Dialog DoloadDialog;
    private boolean canCancel = true;
    private static MyDialog mMyDialog;
    private MyDialogEnterListener listener;

    public MyDialog(Context context) {
        super(context);

    }

    public static MyDialog getInstance(Context context) {
        if (mMyDialog == null)
            mMyDialog = new MyDialog(context);
        return mMyDialog;
    }


    public MyDialog(Context ctx, int res, boolean cancelable) {
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
     * @param abstruct
     */
    public void showAbstructView(Context context, String abstruct) {
        if (dialog != null)
            dialog.dismiss();

        dialog = new MyDialog(context, R.layout.dialog_special_abstruct, true);
        TextView txtAbstruct = (TextView) dialog.findViewById(R.id.txtAbstruct);
        if (txtAbstruct != null && !TextUtils.isEmpty(abstruct))
            txtAbstruct.setText("\u3000\u3000" + abstruct);//首行缩进
        LinearLayout warp = (LinearLayout) dialog.findViewById(R.id.parentWarp);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(Tools.getDisplayWidth((Activity) context), context.getResources().getDimensionPixelOffset(R.dimen.dp450));
        warp.setLayoutParams(lp);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);

        safeShowDialog(context, dialog);

    }


    public void showsingeTip(Context context, String content, final MyDialogEnterListener listener) {
        if (dialog != null)
            dialog.dismiss();
        dialog = new MyDialog(context, R.layout.dialog_sing_tip, false);
        TextView tv_content = (TextView) dialog.findViewById(R.id.dialog_content);
        if (!Tools.isNullOrEmpty(content))
            tv_content.setText(content);
        dialog.findViewById(R.id.dialog_enter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (listener != null) {
                    listener.onClickEnter(dialog, null);
                }
            }
        });

        safeShowDialog(context, dialog);

    }


    /**
     * @param context  弹出订购提示
     * @param listener
     */
    public void showPlaceOrder(Context context, final MyDialogEnterListener listener) {
        if (dialog != null)
            dialog.dismiss();

        dialog = new MyDialog(context, R.layout.dialog_show_placeorder_first, false);
        dialog.findViewById(R.id.left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (dialog != null)
                    dialog.dismiss();
                if (listener != null)
                    listener.onClickEnter(dialog, null);

            }
        });

        dialog.findViewById(R.id.right).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dialog != null)
                    dialog.dismiss();
            }
        });

        TextView textView = (TextView) dialog.findViewById(R.id.txtorign);
        textView.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);//中间划线

        LinearLayout warp = (LinearLayout) dialog.findViewById(R.id.parentWarp);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(Tools.getDisplayWidth((Activity) context), context.getResources().getDimensionPixelOffset(R.dimen.dp450));
        warp.setLayoutParams(lp);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);

        safeShowDialog(context, dialog);


    }


    /**
     * @param context  弹出订购提示,二次确认
     * @param listener
     */
    public void showPlaceOrderConfirm(Context context, final MyDialogEnterListener listener) {
        if (dialog != null)
            dialog.dismiss();

        dialog = new MyDialog(context, R.layout.dialog_show_placeorder_second_confirm, false);
        dialog.findViewById(R.id.left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dialog != null)
                    dialog.dismiss();
                if (listener != null)
                    listener.onClickEnter(dialog, null);
            }
        });

        Button right = (Button) dialog.findViewById(R.id.right);
        right.requestFocus();
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dialog != null)
                    dialog.dismiss();
            }
        });

        LinearLayout warp = (LinearLayout) dialog.findViewById(R.id.parentWarp);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(Tools.getDisplayWidth((Activity) context), context.getResources().getDimensionPixelOffset(R.dimen.dp450));
        warp.setLayoutParams(lp);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);

        safeShowDialog(context, dialog);


    }

    /**
     * @param context  弹出订购提示余额不足,支付宝充值,二期使用
     * @param listener
     */
    public void showPlaceOrderOption(Context context, Double balance, final MyDialogEnterListener listener) {
        if (dialog != null)
            dialog.dismiss();

        dialog = new MyDialog(context, R.layout.dialog_show_placeorder_lackofbalance, true);
        dialog.findViewById(R.id.left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dialog != null)
                    dialog.dismiss();
                if (listener != null)
                    listener.onClickEnter(dialog, null);//支付宝充值
            }
        });

        dialog.findViewById(R.id.btn_leftleft).setOnClickListener(new View.OnClickListener() {//营业厅充值
            @Override
            public void onClick(View view) {
                if (dialog != null)
                    dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.right).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dialog != null)
                    dialog.dismiss();
            }
        });

        TextView textView = (TextView) dialog.findViewById(R.id.myBalance);
        textView.setText("我的余额:" + Tools.FormatTwoDecimal(balance) + "元");

        LinearLayout warp = (LinearLayout) dialog.findViewById(R.id.parentWarp);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(Tools.getDisplayWidth((Activity) context), context.getResources().getDimensionPixelOffset(R.dimen.dp450));
        warp.setLayoutParams(lp);

        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);

        safeShowDialog(context, dialog);

    }

    /**
     * @param context   弹出简单提示
     * @param stringRes
     * @param listener
     */
    public void showtips(Context context, int stringRes, final MyDialogEnterListener listener) {
        if (dialog != null)
            dialog.dismiss();
        dialog = new MyDialog(context, R.layout.dialog_show_singer_tip, false);
        dialog.findViewById(R.id.left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dialog != null)
                    dialog.dismiss();
                if (listener != null)
                    listener.onClickEnter(dialog, null);

            }
        });

        if (stringRes != -1)
            ((TextView) dialog.findViewById(R.id.txtTip)).setText(context.getResources().getString(stringRes));

        LinearLayout warp = (LinearLayout) dialog.findViewById(R.id.parentWarp);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(Tools.getDisplayWidth((Activity) context), context.getResources().getDimensionPixelOffset(R.dimen.dp450));
        warp.setLayoutParams(lp);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);

        safeShowDialog(context, dialog);

    }


    public void showloadingDialog(Context context, String loadingString, final MyDialogEnterListener listener) {
        if (netLoadingDialog != null)
            netLoadingDialog.dismiss();
        netLoadingDialog = new MyDialog(context, R.layout.dialog_net, true);
        TextView txt = (TextView) netLoadingDialog.findViewById(R.id.textviewinfo);

        if (!TextUtils.isEmpty(loadingString))
            txt.setText(loadingString);

        ImageView imaView = (ImageView) netLoadingDialog.findViewById(R.id.image);
        AnimationDrawable aniDra = (AnimationDrawable) imaView.getDrawable();

        if (aniDra != null)
            aniDra.start();

        RelativeLayout warp = (RelativeLayout) netLoadingDialog.findViewById(R.id.parentWarp);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(Tools.getDisplayWidth((Activity) context), Tools.getDisplayHeight((Activity) context));
        warp.setLayoutParams(lp);
        netLoadingDialog.setCanceledOnTouchOutside(false);
        netLoadingDialog.setCancelable(true);//返回键可以取消

        safeShowDialog(context, netLoadingDialog);

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

    /**
     * 关闭通用dialog
     */
    public void closeUniversalDialog() {
        if (dialog != null)
            dialog.dismiss();
    }


    /**
     * 关闭LoadingDialog
     */
    public void closeLoadingDialog() {
        if (netLoadingDialog != null)
            netLoadingDialog.dismiss();
    }


}
