package com.cq.ln.utils;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.URLUtil;
import android.widget.Toast;

import com.cq.ln.R;
import com.cq.ln.activity.ArtistSpecialListActivity;
import com.cq.ln.activity.ChildTypeActivity;
import com.cq.ln.activity.CustomWebViewActivity;
import com.cq.ln.activity.OrderGiftActivity;
import com.cq.ln.activity.PlayUtilActivity;
import com.cq.ln.activity.RechargeGiftActivity;
import com.cq.ln.activity.SearchActivity;
import com.cq.ln.activity.SpecialDetailActivity;
import com.cq.ln.activity.TopicsActivity;
import com.cq.ln.activity.UserCenterActivity;
import com.cq.ln.activity.UserInfoActivity;
import com.cq.ln.constant.ConstantEnum;

import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cqdatasdk.bean.HiFiMainPageBean;
import cqdatasdk.network.HostConfig;
import cqdatasdk.network.URLVerifyTools;

/**
 * Activity帮助类 这里存放的是activity有关的操作
 *
 * @author eric
 */
public final class ActivityUtility {

    private static final String TAG = "ActivityUtility";

    /**
     * 切换全屏状态。
     *
     * @param activity Activity
     * @param isFull   设置为true则全屏，否则非全屏
     */
    public static void toggleFullScreen(Activity activity, boolean isFull) {
        hideTitleBar(activity);
        Window window = activity.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        if (isFull) {
            params.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
            window.setAttributes(params);
            window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        } else {
            params.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
            window.setAttributes(params);
            window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
    }

    /**
     * 设置为全屏
     *
     * @param activity Activity
     */
    public static void setFullScreen(Activity activity) {
        toggleFullScreen(activity, true);
    }

    /**
     * 获取系统状态栏高度
     *
     * @param activity Activity
     * @return 状态栏高度
     */
    public static int getStatusBarHeight(Activity activity) {
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            Field field = clazz.getField("status_bar_height");
            int dpHeight = Integer.parseInt(field.get(object).toString());
            return activity.getResources().getDimensionPixelSize(dpHeight);
        } catch (Exception e1) {
            e1.printStackTrace();
            return 0;
        }
    }

    /**
     * 隐藏Activity的系统默认标题栏
     *
     * @param activity Activity
     */
    public static void hideTitleBar(Activity activity) {
        activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    /**
     * 强制设置Actiity的显示方向为垂直方向。
     *
     * @param activity Activity
     */
    public static void setScreenVertical(Activity activity) {
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    /**
     * 强制设置Activity的显示方向为横向。
     *
     * @param activity Activity
     */
    public static void setScreenHorizontal(Activity activity) {
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    /**
     * 隐藏软件输入法
     *
     * @param activity Activity
     */
    public static void hideSoftInput(Activity activity) {
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    /**
     * 关闭已经显示的输入法窗口。
     *
     * @param context      上下文对象，一般为Activity
     * @param focusingView 输入法所在焦点的View
     */
    public static void closeSoftInput(Context context, View focusingView) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(focusingView.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
    }

    /**
     * 打开输入法窗口。
     *
     * @param context        上下文对象，一般为Activity
     * @param //focusingView 输入法所在焦点的View
     */
    public static void openSoftInput(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.RESULT_UNCHANGED_SHOWN);
    }

    /**
     * 使UI适配输入法
     *
     * @param activity Activity
     */
    public static void adjustSoftInput(Activity activity) {
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    /**
     * 返回一个空的intent
     *
     * @param mContext
     * @return
     */
    public static Intent getIntent(Context mContext) {
        return new Intent();
    }

    /**
     * 跳转到某个Activity
     *
     * @param activity       当前Activity
     * @param targetActivity 目标Activity
     */
    public static void startActivity(Activity activity, Class<? extends Activity> targetActivity) {
        try {
            startActivity(activity, new Intent(activity, targetActivity));
        } catch (ActivityNotFoundException e) {
            toastLong(activity, "请现在AndroidManifest.xml中声明" + targetActivity.getClass().getSimpleName());
        }
    }

    /**
     * 根据给定的Intent进行Activity跳转
     *
     * @param activity 当前Activity
     * @param intent   目标Activity的Intent
     */
    public static void startActivity(Activity activity, Intent intent) {
        try {
            activity.startActivity(intent);
            activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        } catch (ActivityNotFoundException e) {
            toastLong(activity, "请现在AndroidManifest.xml中声明" + intent.getClass().getSimpleName());
        }
    }

    /**
     * 带返回请求进行Activity跳转
     *
     * @param activity       当前Activity
     * @param targetActivity 目标Activity
     * @param requestCode    Activity请求码
     */
    public static void startActivityForResult(Activity activity, Class<? extends Activity> targetActivity, int requestCode) {
        try {
            Intent intent = new Intent(activity, targetActivity);
            activity.startActivityForResult(intent, requestCode);

        } catch (ActivityNotFoundException e) {
            toastLong(activity, "请现在AndroidManifest.xml中声明" + targetActivity.getClass().getSimpleName());
        }
    }

    /**
     * 带返回请求进行Activity跳转
     *
     * @param activity    当前Activity
     * @param intent      目标intent
     * @param requestCode Activity请求码
     */
    public static void startActivityForResult(Activity activity, Intent intent, int requestCode) {
        try {
            activity.startActivityForResult(intent, requestCode);
        } catch (ActivityNotFoundException e) {
            toastLong(activity, "请现在AndroidManifest.xml中声明" + intent.getClass().getSimpleName());
        }
    }

    /**
     * 以较短时间显示Toast消息
     *
     * @param activity Activity
     * @param msgResID 消息资源ID
     */
    public static void toastShort(Activity activity, int msgResID) {
        //toastShort(activity, activity.getResources().getString(msgResID));
        showTip(activity, activity.getResources().getString(msgResID), Toast.LENGTH_SHORT);
    }

    /**
     * 以较长时间显示Toast消息
     *
     * @param activity Activity
     * @param msgResID 消息资源ID
     */
    public static void toastLong(Activity activity, int msgResID) {
        //toastLong(activity, activity.getResources().getString(msgResID));
        showTip(activity, activity.getResources().getString(msgResID), Toast.LENGTH_LONG);
    }

    /**
     * 以短时间时间显示Toast消息
     *
     * @param activity Activity
     * @param message  消息内容
     */
    public static void toastShort(Context activity, String message) {
        //Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
        showTip(activity, message, Toast.LENGTH_SHORT);
    }

    /**
     * 以较长时间显示Toast消息
     *
     * @param activity Activity
     * @param message  消息内容
     */
    public static void toastLong(Context activity, String message) {
        //Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
        showTip(activity, message, Toast.LENGTH_LONG);
    }

    private static Toast mToast;

    // 显示提示信息（Toast）
    private static void showTip(Context context, String tip, int duration) {
        if (mToast == null) {
            mToast = Toast.makeText(context, tip, duration);
        } else {
            mToast.setText(tip);
        }
        mToast.show();
    }

    /**
     * 新版 首页跳转事件处理方法(测试通过)
     *
     * @param context
     * @param view
     * @throws Exception
     */
    public static void MainFragmentHandleClickEvent_(Context context, View view, int fragmentNextUpId) throws Exception {
        Intent intent;
        HiFiMainPageBean.ClassifyBean.ClassifyItemBean itemBean = (HiFiMainPageBean.ClassifyBean.ClassifyItemBean) view.getTag(R.id.tag_for_MainPage_Bean);
        if (itemBean == null)
            return;
        switch (itemBean.linkType) {
            case ConstantEnum.linkType_default://默认容错，不做处理
                break;
            case ConstantEnum.linkType_album://专辑
                int albumId = 0;
                try {
                    albumId = getNumbers(itemBean.href.trim().toString());
                } catch (Exception e) {
                    XLog.d("e=" + e);
                }
                intent = new Intent(context, SpecialDetailActivity.class);
                intent.putExtra("albumId", albumId);
                context.startActivity(intent);
                break;
            case ConstantEnum.linkType_single://单曲
                int singleId = 0;
                try {
                    singleId = getNumbers(itemBean.href.trim().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //单曲播放
                intent = new Intent(context, PlayUtilActivity.class);
                intent.putExtra("isSingle", true);
                intent.putExtra("ablumId", singleId + "");//曲目ID
                intent.putExtra("ablumName", itemBean.title);//曲目名
                context.startActivity(intent);
                break;
            case ConstantEnum.linkType_special://专题
                String topicsId = Uri.parse(itemBean.href).getQueryParameter("id");
                int linkType = itemBean.linkType;
                intent = new Intent(context, TopicsActivity.class);
                intent.putExtra("topicsId", topicsId);
                intent.putExtra("linkType", linkType);
                intent.putExtra("fileType", 0);
                context.startActivity(intent);
                break;
            case ConstantEnum.linkType_childSonglinkType_special://儿歌专题
                String topicsIds = Uri.parse(itemBean.href).getQueryParameter("id");
                int linkTypes = itemBean.linkType;
                intent = new Intent(context, TopicsActivity.class);
                intent.putExtra("topicsId", topicsIds);
                intent.putExtra("linkType", linkTypes);
                intent.putExtra("fileType", 1);
                context.startActivity(intent);
                break;
            case ConstantEnum.linkType_act://TODO webView 1 活动,需要完整url链接
                gotoWebView((Activity) context, itemBean);
                break;
            case ConstantEnum.linkType_more://更多
                gotoMore(context, fragmentNextUpId);
                break;
            case ConstantEnum.linkType_myCollect://我的收藏
                ActivityUtility.goUserCenter((Activity) context, ConstantEnum.Main_Collection_);
                break;
            case ConstantEnum.linkType_rechange://TODO webView 2 充值有礼
//                gotoWebView((Activity) context, itemBean);
                goCustomWebView((Activity) context,HostConfig.rechangeUrl);
                break;
            case ConstantEnum.linkType_order://TODO webView 3 订购有礼，此处已经在会员专区页面做了是否订购的判断，所以这里只要提示一下已经订购即可
                gotoWebView((Activity) context, itemBean);
                break;
            case ConstantEnum.linkType_userInfo://个人信息
                ActivityUtility.goUserInfo((Activity) context, "");
                break;
            case ConstantEnum.linkType_qrCode://二维码，不需要处理
                break;
            case ConstantEnum.linkType_signIn://TODO webView 4 签到
                gotoWebView((Activity) context, itemBean);
                break;
            case ConstantEnum.linkType_newsDvd://新碟抢先听
                //逻辑在外部处理，此处无需处理
                break;
            case ConstantEnum.linkType_radio://电台
                //TODO 后续需求添加
                break;
            case ConstantEnum.linkType_childSong://儿歌类型
                int columnId = 0;
                try {
                    columnId = Integer.parseInt(Uri.parse(itemBean.href).getQueryParameter("columnId"));
                } catch (Exception e) {
                    XLog.d("e=" + e);
                }
                int column = ConstantEnum.Search_DiscRecommend;
                intent = new Intent(context, ChildTypeActivity.class);
                intent.putExtra("column", column);
                intent.putExtra("ColumnChild", -1);
                intent.putExtra("columnId", columnId);
                context.startActivity(intent);
                break;
            case ConstantEnum.linkType_childSongContent://儿歌专辑
                int childSongAlbumId = 0;
                try {
                    childSongAlbumId = Integer.parseInt(Uri.parse(itemBean.href).getQueryParameter("albumId"));
                } catch (Exception e) {
                    XLog.d("e=" + e);
                }
                intent = new Intent(context, SpecialDetailActivity.class);
                intent.putExtra("albumId", childSongAlbumId);
                intent.putExtra("fileType", 1);
                context.startActivity(intent);
                break;
        }
    }

    private static void gotoWebView(Activity context, HiFiMainPageBean.ClassifyBean.ClassifyItemBean itemBean) {
        if (itemBean == null)
            return;
        String url = URLVerifyTools.formatWebViewUrl(itemBean.href);
        if (!URLUtil.isNetworkUrl(url))
            return;
        goCustomWebView(context, url);

    }

    private static void gotoMore(Context context, int fragmentNextUpId) {
        int ColumnChild;
        switch (fragmentNextUpId) {//更多
            case R.id.RBtnHiFi://2
                ColumnChild = ConstantEnum.Search_ColumnChild_Zero;
                goSearchActivity(context, ColumnChild);
                break;
            case R.id.RBtnPopular://3
                ColumnChild = ConstantEnum.Search_ColumnChild_One;
                goSearchActivity(context, ColumnChild);
                break;
            case R.id.RBtnClass://4
                ColumnChild = ConstantEnum.Search_ColumnChild_TWo;
                goSearchActivity(context, ColumnChild);
                break;
            case R.id.RBtnJazz://5
                ColumnChild = ConstantEnum.Search_ColumnChild_Three;
                goSearchActivity(context, ColumnChild);
                break;
            case R.id.RBtnNation://6
                ColumnChild = ConstantEnum.Search_ColumnChild_Four;
                goSearchActivity(context, ColumnChild);
                break;
            default: //默认是流行的更多
                ColumnChild = ConstantEnum.Search_ColumnChild_One;
                goSearchActivity(context, ColumnChild);
                break;
        }
    }

    private static void goSearchActivity(Context context, int columnChild) {
        int column = ConstantEnum.Search_DiscRecommend;
        Intent intent;
        intent = new Intent(context, SearchActivity.class);
        intent.putExtra("column", column);
        intent.putExtra("ColumnChild", columnChild);
        context.startActivity(intent);
    }

    /**
     * @param context  首页入口逻辑
     * @param itemBean
     */
    private static void defaultOther(Context context, HiFiMainPageBean.ClassifyBean.ClassifyItemBean itemBean) {
        Intent intent;
        String str = itemBean.href.toLowerCase().trim();
        XLog.d("str = " + str);
        if (str.contains("album.html") && str.contains("albumid")) {//本地布局页面，专辑详情 //album.html?albumid=33
            intent = new Intent(context, SpecialDetailActivity.class);
            intent.putExtra(Enumer.intent_specailBean, itemBean);
            context.startActivity(intent);
            return;

        } else if (str.contains("album.html") && str.contains("id")) {//本地布局页面，专辑详情 //album.html?id=33
            int albumId = 0;
            try {
                albumId = getNumbers(itemBean.href.trim().toString());
            } catch (Exception e) {
                XLog.d("e=" + e);
            }
            intent = new Intent(context, SpecialDetailActivity.class);
            intent.putExtra("albumId", albumId);
            context.startActivity(intent);
            return;

        } else if (str.contains("play.html") && str.contains("singleid")) {//单曲播放 href: "play.html?singleId=6050",
            int singleId = 0;
            try {
                singleId = getNumbers(itemBean.href.trim().toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            //单曲播放
            intent = new Intent(context, PlayUtilActivity.class);
            intent.putExtra("isSingle", true);
            intent.putExtra("ablumId", singleId + "");//曲目ID
            intent.putExtra("ablumName", itemBean.title);//曲目名
            context.startActivity(intent);
            return;

        } else {
            goCustomWebView((Activity) context, itemBean.href);
            return;
        }
    }

    //截取数字
    public static int getNumbers(String content) throws Exception {
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(content);
        XLog.d("content = " + content);
        int number = -1;
        int index = 0;
        while (matcher.find()) {
            String num = matcher.group(index);
            XLog.d("num = " + num);
            number = Integer.parseInt(num);
            index++;
        }
        return number;
    }

    /**
     * @param activity 专辑详情
     * @param albumId
     */

    public static void goSpecialDetailActivity(Activity activity, int albumId) {
        Intent intent = new Intent();
        intent.setClass(activity, SpecialDetailActivity.class);
        intent.putExtra("albumId", albumId);
        startActivity(activity, intent);
    }

    /**
     * @param activity      艺术家专辑列表
     * @param artistId
     * @param artistName
     * @param artistIconUrl
     */
    public static void goArtistSpecialList(Activity activity, int artistId, String artistName, String artistIconUrl) {
        Intent intent = new Intent();
        intent.setClass(activity, ArtistSpecialListActivity.class);
        intent.putExtra("artistId", artistId);
        intent.putExtra("artistName", artistName);
        intent.putExtra("artistIconUrl", artistIconUrl);

        startActivity(activity, intent);
    }


    public static void goVipArea(Activity activity, String imgUrl, Class<? extends Activity> cls) {
        Intent intent = new Intent();
        intent.setClass(activity, cls);
        intent.putExtra("imgUrl", imgUrl);
        startActivity(activity, intent);
    }

    /**
     * @param activity
     * @param showWhich alt + f 注释快捷键
     */
    public static void goUserCenter(Activity activity, int showWhich) {
        Intent intent = new Intent();
        intent.setClass(activity, UserCenterActivity.class);
        intent.putExtra("showWhich", showWhich);
        startActivity(activity, intent);
    }

    /**
     * 使用自定义webView页面
     *
     * @param activity
     * @param webviewUrl
     */
    public static void goCustomWebView(Activity activity, String webviewUrl) {
        Intent intent = new Intent();
        intent.setClass(activity, CustomWebViewActivity.class);
        intent.putExtra("webviewUrl", webviewUrl);
        startActivity(activity, intent);
    }

    // 充值有礼
    public static void goRechargGift(Activity activity, String imgUrl) {
        Intent intent = new Intent();
        intent.setClass(activity, RechargeGiftActivity.class);
        intent.putExtra("imgUrl", imgUrl);
        startActivity(activity, intent);
    }

    //订购有礼
    public static void goOrderGift(Activity activity, String imgUrl) {
        Intent intent = new Intent();
        intent.setClass(activity, OrderGiftActivity.class);
        intent.putExtra("imgUrl", imgUrl);
        startActivity(activity, intent);
    }

    //用户信息
    public static void goUserInfo(Activity activity, String str) {
        Intent intent = new Intent();
        intent.setClass(activity, UserInfoActivity.class);
        startActivity(activity, intent);
    }


}