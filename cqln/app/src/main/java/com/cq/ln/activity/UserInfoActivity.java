package com.cq.ln.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cq.ln.CqApplication;
import com.cq.ln.R;
import com.cq.ln.appConfig.Appconfig;
import com.cq.ln.constant.ConstantEnum;
import com.cq.ln.helper.UserFaceViewHelper;
import com.cq.ln.interfaces.OnSubmintClickListener;
import com.cq.ln.utils.ActivityUtility;
import com.cq.ln.utils.SelectHeandIconDialog;
import com.cq.ln.utils.Tools;
import com.cq.ln.views.FixGridLayout;

import butterknife.Bind;
import butterknife.ButterKnife;
import cqdatasdk.bean.UserFaceGetSucceedBean;
import cqdatasdk.bean.UserFaceSetSucceedBean;
import cqdatasdk.interfaces.IVolleyRequestSuccess;
import cqdatasdk.interfaces.IVolleyRequestfail;
import cqdatasdk.network.HostConfig;
import cqdatasdk.network.URLVerifyTools;

/**
 * 用户信息
 * http://pic.nipic.com/2008-07-04/200874122649997_2.jpg icon
 */
public class UserInfoActivity extends BaseActivity implements IVolleyRequestSuccess, IVolleyRequestfail {
    private static final String TAG = "UserInfoActivity";
    private final float scale = 1.0f;
    @Bind(R.id.user_info_head_icon)
    ImageButton mUserInfoHeadIcon;
    @Bind(R.id.key_no_text)
    TextView mKeyNoText;
    @Bind(R.id.user_balance_text)
    TextView mUserBalanceText;
    @Bind(R.id.register_integral_text)
    TextView mRegisterIntegralText;
    @Bind(R.id.user_recharge_button)
    Button mUserRechargeButton;
    @Bind(R.id.user_register_button)
    Button mUserRegisterButton;
    @Bind(R.id.img_layout)
    LinearLayout mImgLayout;
    @Bind(R.id.linearlayout_buttons)
    FixGridLayout mLinearlayoutButtons;
    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_info_layout);
        ButterKnife.bind(this);
        context = this;

        initView();

        setListeners();

        int originIndex = Tools.getIntegerPreference(this, ConstantEnum.UserFace_Index, ConstantEnum.UserFace_Index_NO_Setting);
        setFace(originIndex);

        getAndUpdateInfo();

    }

    private void getAndUpdateInfo() {

        setinfo();
        asyncHttpRequest.getUserFace(this, Appconfig.getKeyNo(getApplicationContext()), this, this, null);

    }

    private void setinfo() {

        mKeyNoText.setText("智能卡号：" + Appconfig.getKeyNo(getApplicationContext()));
        mUserBalanceText.setText("账号余额：" + Appconfig.getbalance(getApplicationContext()));
        mRegisterIntegralText.setText("签到积分：" + Appconfig.getUserIntegral(getApplicationContext()));

    }


    private void initView() {
        FixGridLayout fixGridLayout = (FixGridLayout) findViewById(R.id.linearlayout_buttons);
        fixGridLayout.setmCellHeight(30);
        fixGridLayout.setmCellWidth(100);
        for (int i = 0; i < 7; i++) {
            Button box = new Button(this);
            box.setText("第" + i + "个");
            fixGridLayout.addView(box);
        }
    }

    private void setListeners() {
        mUserRechargeButton.setOnClickListener(this);
        mUserRegisterButton.setOnClickListener(this);
        mUserInfoHeadIcon.setOnClickListener(this);

        mUserRechargeButton.requestFocus();
        mUserRechargeButton.setFocusableInTouchMode(true);
        mUserRechargeButton.setFocusable(true);

    }

    @Override
    protected void onResume() {
        super.onResume();
        getAndUpdateInfo();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.user_recharge_button://充值
//                ActivityUtility.goCustomWebView(this, HostConfig.rechangeUrl);
                if (CqApplication.mRechangeBean!=null) {
                    ActivityUtility.goCustomWebView(this, URLVerifyTools.formatWebViewUrl(CqApplication.mRechangeBean.href));
                }else{
                    finish();//数据为空，返回上一界面去获取数据
                }
                break;
            case R.id.user_register_button://签到
                ActivityUtility.goCustomWebView(this, HostConfig.signInUrl);
                break;
            case R.id.user_info_head_icon:
                int originIndex = Tools.getIntegerPreference(this, ConstantEnum.UserFace_Index, ConstantEnum.UserFace_Index_NO_Setting);
                SelectHeandIconDialog selectHeandIconDialog = new SelectHeandIconDialog();
                selectHeandIconDialog.showSelectedUserFaceView(this, originIndex, new OnSubmintClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int index, String inputStr) {
                        if (index != -1) {
                            setFace(index);
                            updateUserFace(index);
                        }

                    }
                });
                break;
        }
    }


    private void updateUserFace(int index) {
        int orignIndex = Tools.getIntegerPreference(context, ConstantEnum.UserFace_Index, ConstantEnum.UserFace_Index_NO_Setting);
        if (index == orignIndex)
            return;
        asyncHttpRequest.setUserFace(this, Appconfig.getKeyNo(getApplicationContext()), index, this, this, "正在保存中...");
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (event.getKeyCode()) {
                case KeyEvent.KEYCODE_DPAD_RIGHT:
                    break;
                case KeyEvent.KEYCODE_DPAD_LEFT:
                    break;
                case KeyEvent.KEYCODE_DPAD_UP:
                    break;
                case KeyEvent.KEYCODE_DPAD_DOWN:
                    break;
            }
        }
        return super.dispatchKeyEvent(event);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    public void onSucceeded(String method, String key, Object object) throws Exception {
        if ("updateUserImg.action".equals(method)) {
            UserFaceSetSucceedBean bean = (UserFaceSetSucceedBean) object;
            if (bean != null) {
                Tools.showTip(UserInfoActivity.this, bean.getMsg());
                if (bean.getCode().equals("0")) {//逻辑成功
                    int index = Integer.parseInt(key);
                    //存储
                    Tools.setIntegerPreference(context, ConstantEnum.UserFace_Index, index);
                } else {//逻辑失败
                    int originIndex = Tools.getIntegerPreference(this, ConstantEnum.UserFace_Index, ConstantEnum.UserFace_Index_NO_Setting);
                    setFace(originIndex);
                }

            }


            return;
        } else if ("getUserImg.action".equals(method)) {
            UserFaceGetSucceedBean bean = (UserFaceGetSucceedBean) object;
            if (bean == null)
                return;
            if (bean.getCode().equals("0")) {
                int faceIndex = bean.getUserImg();
                Tools.setIntegerPreference(this, ConstantEnum.UserFace_Index, faceIndex);
                setFace(faceIndex);

                //余额积分
                Appconfig.setbalance(getApplicationContext(), bean.getBalance());
                Appconfig.setUserIntegral(getApplicationContext(), bean.getUserIntegral());

                //reload
                setinfo();
            }
            return;
        }
    }

    @Override
    public void onFailed(String method, String key, int errorTipId) throws Exception {
        if ("updateUserImg.action".equals(method)) {//请求失败
            Tools.showTip(UserInfoActivity.this, "设置头像失败，请重试！");
            int originIndex = Tools.getIntegerPreference(this, ConstantEnum.UserFace_Index, ConstantEnum.UserFace_Index_NO_Setting);
            setFace(originIndex);
            return;
        }
    }


    private void setFace(int index) {
        UserFaceViewHelper.setUserFaceImage(index, mUserInfoHeadIcon);
    }


}
