package com.cq.ln.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.cq.ln.R;
import com.cq.ln.interfaces.OnSubmintClickListener;

import java.util.ArrayList;
import java.util.List;


/**
 * 自定义Dialog
 */
public class SelectHeandIconDialog {
    private List<View> ImCheckViewList;
    private int flag = -1;
    private Dialog dialog;

    public void close() {
        if (dialog != null)
            dialog.dismiss();
    }

    /**
     * @param context 弹出头像选择
     */
    public void showSelectedUserFaceView(final Context context, int defaultFace, final OnSubmintClickListener listener) {
        flag = defaultFace;
        dialog = new Dialog(context, R.style.MyDialog);
        dialog.setContentView(R.layout.dialog_select_head_icon);
        LinearLayout warp = (LinearLayout) dialog.findViewById(R.id.parentWarp);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(Tools.getDisplayWidth((Activity) context), FrameLayout.LayoutParams.WRAP_CONTENT);
        warp.setLayoutParams(lp);
        View view1 = dialog.findViewById(R.id.ItemHead1).findViewById(R.id.face_Root);
        View view2 = dialog.findViewById(R.id.ItemHead2).findViewById(R.id.face_Root);
        View view3 = dialog.findViewById(R.id.ItemHead3).findViewById(R.id.face_Root);
        View view4 = dialog.findViewById(R.id.ItemHead4).findViewById(R.id.face_Root);
        View view5 = dialog.findViewById(R.id.ItemHead5).findViewById(R.id.face_Root);
        View view6 = dialog.findViewById(R.id.ItemHead6).findViewById(R.id.face_Root);
        View view7 = dialog.findViewById(R.id.ItemHead7).findViewById(R.id.face_Root);
        View view8 = dialog.findViewById(R.id.ItemHead8).findViewById(R.id.face_Root);

        if (ImCheckViewList == null)
            ImCheckViewList = new ArrayList<>();
        ImCheckViewList.clear();
        ImCheckViewList.add(view1.findViewById(R.id.Im_check));
        ImCheckViewList.add(view2.findViewById(R.id.Im_check));
        ImCheckViewList.add(view3.findViewById(R.id.Im_check));
        ImCheckViewList.add(view4.findViewById(R.id.Im_check));
        ImCheckViewList.add(view5.findViewById(R.id.Im_check));
        ImCheckViewList.add(view6.findViewById(R.id.Im_check));
        ImCheckViewList.add(view7.findViewById(R.id.Im_check));
        ImCheckViewList.add(view8.findViewById(R.id.Im_check));

        view1.setOnClickListener(new FaceSelecListener(context, view1, null));
        view2.setOnClickListener(new FaceSelecListener(context, view2, null));
        view3.setOnClickListener(new FaceSelecListener(context, view3, null));
        view4.setOnClickListener(new FaceSelecListener(context, view4, null));
        view5.setOnClickListener(new FaceSelecListener(context, view5, null));
        view6.setOnClickListener(new FaceSelecListener(context, view6, null));
        view7.setOnClickListener(new FaceSelecListener(context, view7, null));
        view8.setOnClickListener(new FaceSelecListener(context, view8, null));

        Button btnEnter = (Button) dialog.findViewById(R.id.btnEnter);
        btnEnter.setOnClickListener(new FaceSelecListener(context, btnEnter, listener));

        setSelectedFace(ImCheckViewList, defaultFace);

        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        safeShowDialog(context, dialog);
    }

    /**
     * @param dialog 安全方式showDialog
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
     * 设置已选择的头像
     *
     * @param imCheckViewList
     * @param selectedFace
     */
    private void setSelectedFace(List<View> imCheckViewList, int selectedFace) {
        if (selectedFace >= 0 && selectedFace < imCheckViewList.size()) {
            View view = imCheckViewList.get(selectedFace);
            FrameLayout parent = (FrameLayout) view.getParent();
            parent.requestLayout();
            parent.requestFocus();
            setCheckWithoutView(view.findViewById(R.id.Im_check));
        }
    }


    private class FaceSelecListener implements View.OnClickListener {
        View view;
        Context context;
        OnSubmintClickListener listener;

        public FaceSelecListener(Context context, View view, OnSubmintClickListener listener) {
            this.context = context;
            this.view = view;
            this.listener = listener;
        }

        @Override
        public void onClick(View v) {
            if (v instanceof Button) {
                if (flag == -1) {
                    Tools.showTip(context.getApplicationContext(), "请先选择头像！");
                    return;
                }
                close();
                if (listener != null) {
                    listener.onClick(null, flag, null);
                }
            } else {
                setCheckWithoutView(v.findViewById(R.id.Im_check));
                flag = Integer.parseInt(((View) v.getParent()).getTag().toString());
            }
        }
    }

    private void setCheckWithoutView(View view) {
        for (View v : ImCheckViewList) {
            if (v.equals(view)) {
                view.setVisibility(View.VISIBLE);
                continue;
            }
            v.setVisibility(View.INVISIBLE);
        }
    }
}
