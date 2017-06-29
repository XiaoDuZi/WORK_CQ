package com.cq.ln.activity;

/**
 * Created by fute on 16/9/25.
 */

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Window;

import com.cq.ln.CqApplication;
import com.cq.ln.R;
import com.cq.ln.appConfig.Appconfig;
import com.cq.ln.constant.ConstantEnum;
import com.cq.ln.interfaces.MyDialogEnterListener;
import com.cq.ln.utils.ActivityUtility;
import com.cq.ln.utils.HiFiDialogTools;
import com.cq.ln.utils.Tools;
import com.cq.ln.utils.XLog;

import cqdatasdk.bean.OrderMusicServerBean_;
import cqdatasdk.bean.ParentLockBean_;
import cqdatasdk.bean.UserAuthResponeBean;
import cqdatasdk.bean.UserBalanceBean;
import cqdatasdk.interfaces.IVolleyRequestSuccess;
import cqdatasdk.interfaces.IVolleyRequestfail;
import cqdatasdk.network.URLVerifyTools;
import cqdatasdk.network.VolleyRequest;

/**
 * 获取播放地址
 * 1,首先鉴权
 * 2,如果鉴权通过获取播放地址
 * 3,如果鉴权不通过则需要用户订购
 * 4,订购时先获取用户是否有家长锁
 * 5,如果有则需要用户先输入家长所,
 * 6,如果没有则直接订购
 * 7,如果用户余额不足则需要用户充值
 * 7,充值成功继续订购
 * */

public class GetPlayUrlActivity extends BaseActivity implements IVolleyRequestSuccess, IVolleyRequestfail{

    private HiFiDialogTools hiFiDialogTools = new HiFiDialogTools();
    private int trackId;
    public static final String GetPlayUrlResultKey = "VodPlayerUri";
    public static final int GetPlayUrlKey = 1002;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(null);

        trackId = getIntent().getIntExtra("trackId", 0);
        Auth(trackId);

    }

    /**
     * //鉴权
     *
     * @param progId 曲目Id
     */
    private void Auth(int progId) {
        asyncHttpRequest.getUserAuth(this, progId+"", 0, Appconfig.getKeyNo(getApplicationContext()), this, this, "请稍后...");
    }

    private void getPlayerRtsp(String progId) {
        final String url = Appconfig.getEpg_url(getApplicationContext()) + "/defaultHD/en/go_authorization.jsp?playType=1&progId=" + progId + "&contentType=0&business=1&baseFlag=0&idType=FSN";
        XLog.d("GetPlayUrlActivity", "获取RTSP连接:" + url);
        VolleyRequest.getRTSPUrl(progId, url, Appconfig.getCookie(getApplicationContext()), new IVolleyRequestSuccess() {
            @Override
            public void onSucceeded(String method, final String vodId, final Object object) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String vodPlayerUri = "";
                        if (null == object) {
                            hiFiDialogTools.showtips(GetPlayUrlActivity.this, "获取播放连接失败,请稍后再试.", myDialogEnterListener);
                            return;
                        }
                        vodPlayerUri = object.toString(); //播放地址
//                        vodPlayerUri = "http://file.goodweb.cn/music/musicdownload_all/musicdownload32/%E8%93%9D%E8%8E%B2%E8%8A%B1.mp3"; // TODO: 16/10/13 测试播放记得去掉
                        Intent intent = new Intent();
                        intent.putExtra(GetPlayUrlResultKey, vodPlayerUri);
                        setResult(RESULT_OK, intent); //intent为A传来的带有Bundle的intent，当然也可以自己定义新的Bundle
                        finish();//此处一定要调用finish()方法
                    }
                });
            }
        });
    }

    @Override
    public void onSucceeded(String method, String key, Object object) {
        if ("single.action".equals(method)) {//第一个接口,查询订购状态返回
            if (object == null)
                return;
            UserAuthResponeBean bean = (UserAuthResponeBean) object;
            UserAuthResponeBean.DataBean result = bean.getData();
            if (result == null) {
                hiFiDialogTools.showtips(GetPlayUrlActivity.this, bean.getMsg(), myDialogEnterListener);

                return;
            }
            switch (result.getIsfree()) {
                case ConstantEnum.Player_Auth_charge://收费
                    chargePlay(result);
                    break;
                case ConstantEnum.Player_Auth_Free://免费,
                    //可播放
                    getPlayerRtsp(result.getVodId());
                default:
                    break;
            }
            return;

        } else if ("userLock.action".equals(method)) {//查询家长锁返回
            if (object == null)
                return;
            final ParentLockBean_ bean = (ParentLockBean_) object;
            if (bean == null)
                return;


            if (bean.getCode().equals("200")) {
                //弹层2 家长锁
                Intent intent = new Intent();
                intent.putExtra("PASSWORD", bean.getPasswd());
                intent.setClass(GetPlayUrlActivity.this, ParentsLockActivity.class);
                startActivityForResult(intent, PlayUtilActivity.CodeParentsLock);
                return;
            } else if (bean.getCode().equals("201")) {//没有家长锁
                getUserBalance();
                return;
            } else {
                hiFiDialogTools.showtips(GetPlayUrlActivity.this, bean.getMsg(), myDialogEnterListener);
            }
            return;

        } else if ("getUserInfo.action".equals(method)) {//查询余额成功返回
            if (object == null)
                return;
            UserBalanceBean bean = (UserBalanceBean) object;
            UserBalanceBean.ResultBean result = bean.getResult();
            if (result == null)
                return;
            //{"status":1,"errors":[],"result":{"custId":"3816815","totalFee":90.54,"totalBalance":0.0,"msg":"没有订购"},"extra":{}}

            if (result.getTotalBalance() > ConstantEnum.USER_ORDER_BALANCE) {//用户金额大于0,开通
                OrderMusicServer();
            } else {
                //弹层3 充值,后期才接入充值
                /**
                 MyDialog.getInstance(this).showPlaceOrderOption(this, result.getTotalBalance(), new onMyDialogEnterListener() {
                @Override public void onClickEnter(Dialog dialog, Object object) {//支付宝充值
                //弹层4,充值金额选择

                //TODO
                }
                });*/

                hiFiDialogTools.showLeftRightTip(GetPlayUrlActivity.this, "温馨提示", getString(R.string.string_96868), "马上充值", "取消", new MyDialogEnterListener() {
                    @Override
                    public void onClickEnter(Dialog dialog, Object object) {
                        if ((int)object==0){
                            ActivityUtility.goCustomWebView(GetPlayUrlActivity.this, URLVerifyTools.formatWebViewUrl(CqApplication.mRechangeBean.href));
                        }
                    }
                });

            }

            return;
        } else if ("comboValid.action".equals(method)) {//订购包月套餐成功!!

            if (object == null)
                return;
            OrderMusicServerBean_ bean = (OrderMusicServerBean_) object;

            if (bean.getCode() == 200) {//订购成功!
                //TODO 弹层提示!
                hiFiDialogTools.showtips(this, R.string.string_order_succeed, new MyDialogEnterListener() {
                    @Override
                    public void onClickEnter(Dialog dialog, Object object) {
                        //刷新当前播放,调用单曲循环刷新方法即可!
                        Auth(trackId);
                    }
                });
            } else {//订购失败!

                if (bean == null) {
                    Tools.showTip(GetPlayUrlActivity.this, "订购失败.");
                    return;
                }
                Tools.showTip(GetPlayUrlActivity.this, bean.getMsg());
            }
            return;
        }

    }


    /**
     * @param result 付费曲目处理
     */
    private void chargePlay(UserAuthResponeBean.DataBean result) {
        switch (result.getUserCode()) {
            case ConstantEnum.Music_Auth_status_IsOrder://已订购,不处理
                //可播放
                getPlayerRtsp(result.getVodId());
                break;
            case ConstantEnum.Music_Auth_status_NoOrder://未订购
                //可播放0秒
//                getPlayerRtsp(result.getVodId());
                //计时
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        playThirdSecond(false);
                    }
                }).start();
                break;
            case ConstantEnum.Music_Auth_status_blacklist:
                //黑名单,弹层提示
                //TODO
//                MyDialog.getInstance(this)
                //FIXME
                hiFiDialogTools.showtips(this, R.string.string_black, myDialogEnterListener);

                break;
            case ConstantEnum.Music_Auth_status_disable://欠费停用
                //可播放0秒
//                getPlayerRtsp(result.getVodId());
                //计时
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        playThirdSecond(true);

                    }
                }).start();
                break;
        }
    }

    /**
     * 试播30秒
     *
     * @param isDisable 是否停用,放在线程中执行
     */
    private void playThirdSecond(final boolean isDisable) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (isDisable) {//欠费停用
                    //FIXME
                    hiFiDialogTools.showtips(GetPlayUrlActivity.this, R.string.string_disable, myDialogEnterListener);
                    return;
                } else {
                    //弹层1,未订购
                    hiFiDialogTools.showPlaceOrder(GetPlayUrlActivity.this, new MyDialogEnterListener() {
                        @Override
                        public void onClickEnter(Dialog dialog, Object object) {
                            if ("cancel".equals(object)){
                                finish();
                            }else{
                                getParentLock();
                            }
                        }
                    });
                }
            }
        });

    }

    @Override
    public void onFailed(String method, String key, int errorTipId) throws Exception {

    }

    /**
     * 开通音乐包月活动
     */
    private void OrderMusicServer() {
        asyncHttpRequest.OrderMusicServer_(this, Appconfig.getKeyNo(getApplicationContext()), this, this);
    }

    /**
     * 查询用户余额
     */
    private void getUserBalance() {
        asyncHttpRequest.getUserBalance(this, Appconfig.getKeyNo(getApplicationContext()), this, this, "请稍后...");
    }

    /**
     * 查询是否有家长锁
     */
    private void getParentLock() {
        asyncHttpRequest.getParentLock_(this, "1", Appconfig.getKeyNo(getApplicationContext()), this, this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode==PlayUtilActivity.CodeParentsLock){
            //验证家长锁成功
            getUserBalance();
        }else{
            finish();
        }
    }

    MyDialogEnterListener myDialogEnterListener = new MyDialogEnterListener() {
        @Override
        public void onClickEnter(Dialog dialog, Object object) {
            finish();
        }
    };

}
