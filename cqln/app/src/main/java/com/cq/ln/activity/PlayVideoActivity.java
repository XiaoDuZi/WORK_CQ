package com.cq.ln.activity;

import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.cq.ln.CqApplication;
import com.cq.ln.R;
import com.cq.ln.appConfig.Appconfig;
import com.cq.ln.bean.BeanPlayItem;
import com.cq.ln.constant.ConstantEnum;
import com.cq.ln.interfaces.MyDialogEnterListener;
import com.cq.ln.utils.HiFiDialogTools;
import com.cq.ln.utils.StrTool;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cqdatasdk.bean.UserAuthResponeBean;
import cqdatasdk.interfaces.IVolleyRequestSuccess;
import cqdatasdk.interfaces.IVolleyRequestfail;
import cqdatasdk.network.VolleyRequest;
import ipaneltv.toolkit.fragment.PlayActivityInterface;
import ipaneltv.toolkit.fragment.VodPlayFragment;
import ipaneltv.uuids.ChongqingUUIDs;

/**
 * Created by fute on 17/1/9.
 */

public class PlayVideoActivity extends BaseActivity implements IVolleyRequestSuccess, IVolleyRequestfail, PlayActivityInterface.VodPlayBaseInterface.Callback {

    //快进快退
    private static final int QUICK_ADD_PROGRESS = 5;
    private static final int QUICK_CUT_PROGRESS = 6;
    private static final int SHOW_CONTROL = 7;
    private static final int HIDE_CONTROL = 8;
    private static final int PLAY_TIME = 9;
    private static final int QUICK_INTERVAL_ITEM = 60;  //快进的时间间隔

    @Bind(R.id.ll_play_quick)
    LinearLayout ll_play_quick;
    @Bind(R.id.tv_quick_info)
    TextView tv_quick_info;
    @Bind(R.id.iv_quick_icon)
    ImageView iv_quick_icon;
    @Bind(R.id.tv_dur_left)
    TextView mTxtPlayTime;
    @Bind(R.id.tv_dur_right)
    TextView mTxtDuration;
    @Bind(R.id.video_player_progress)
    SeekBar video_player_progress;
    @Bind(R.id.vedio_player_pause)
    ImageButton vedio_player_pause;
    @Bind(R.id.rl_control)
    RelativeLayout rl_control;
    @Bind(R.id.tv_title)
    TextView tv_title;
    @Bind(R.id.iv_video_bg)
    ImageView iv_video_bg;
    @Bind(R.id.rl_play_list)
    RelativeLayout rl_play_list;
    @Bind(R.id.lv_play)
    ListView lv_play;

    private int quickCount = 0;
    private long vodDur = 0, vodPayingTime = 0;
    private boolean startQuick = false;
    private boolean isPlaying = false;
    private boolean isBuyBack = false;   //如果为true  则播放单集的时候  返回web页面不会退出播放列表页
    private String vodPlayerUri = "";
    private VodPlayFragment playFragment;
    private HiFiDialogTools hiFiDialogTools = new HiFiDialogTools();
    private int streamType = CqApplication.StreamTypeIpqam;    //数据流类型
    private PlayActivityInterface.VodPlayBaseInterface baseInterface;
    private int playIndex = 0;
    private ArrayList<String> playTitleList = new ArrayList();
    private ArrayList<String> playVodIdList = new ArrayList();
    private ArrayList<String> playTrackIdList = new ArrayList();
    private ArrayList<String> fileTypeList = new ArrayList();
    private ArrayList<Integer> pointList = new ArrayList();
    private int freeTime = 30; //可播放时长，默认为30秒，如果为-1  则整个免费
    private UserAuthResponeBean authBean;
    Handler uiHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            if (message.what == QUICK_ADD_PROGRESS) { //快进
                quickCount = quickCount + 1;
                uiHandler.sendEmptyMessageDelayed(QUICK_ADD_PROGRESS, QUICK_INTERVAL_ITEM);
                setPlayInfo();
            } else if (message.what == QUICK_CUT_PROGRESS) { //快退
                quickCount = quickCount - 1;
                uiHandler.sendEmptyMessageDelayed(QUICK_CUT_PROGRESS, QUICK_INTERVAL_ITEM);
                setPlayInfo();
            } else if (message.what == SHOW_CONTROL) {
                rl_control.setVisibility(View.VISIBLE);
            } else if (message.what == HIDE_CONTROL) {
                rl_control.setVisibility(View.GONE);
            } else if (message.what == PLAY_TIME) {
                uiHandler.sendEmptyMessageDelayed(PLAY_TIME, 1000);
                vodPayingTime++;
                if (vodPayingTime * 1000 > vodDur) {
//                    playNext();//// TODO: 17/1/18 要去掉
                    return false;
                }
                mTxtPlayTime.setText(StrTool.generateTime(vodPayingTime * 1000));
                video_player_progress.setProgress((int) (vodPayingTime * 1000 * video_player_progress.getMax() / vodDur));
                if (freeTime != -1 && vodPayingTime > freeTime) {//免费播放时长结束，返回webview界面提示需要订购
                    Gson gson = new Gson();
                    String cmboId = "";
                    if (authBean != null && authBean.getData() != null) {
                        cmboId = gson.toJson(authBean.getData().getCmboIds());
                    }
                    Intent intent = new Intent();
                    intent.putExtra("cmboIdsArrayStr", cmboId);
                    intent.putExtra("currentIndex", playIndex);
                    intent.putExtra("currentPoint", vodPayingTime);
                    setResult(RESULT_OK, intent);
                    isBuyBack = true;
                    finish(); // TODO: 17/1/18
                }
            }
            return false;
        }
    });

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_video);
        ButterKnife.bind(this);

        String playListJsonString = getIntent().getStringExtra("playListJsonString");
        playIndex = getIntent().getIntExtra("playIndex", 0);

        List<BeanPlayItem> playItemList = new ArrayList<BeanPlayItem>();
        Gson gson = new Gson();
        playItemList = gson.fromJson(playListJsonString, new TypeToken<List<BeanPlayItem>>() {
        }.getType());
        if (playItemList.isEmpty()) {
            return;
        }
        for (int i = 0; i < playItemList.size(); i++) {
            BeanPlayItem playItem = playItemList.get(i);
            playTitleList.add(playItem.getName());
            playVodIdList.add(playItem.getVodId());
            fileTypeList.add(playItem.getFileType());
            pointList.add(playItem.getInitPoint());
            playTrackIdList.add(playItem.getTrackId() + "");
        }
        if (playIndex >= playItemList.size()) {
            playIndex = 0;
        }
        Auth(playTrackIdList.get(playIndex));
        tv_title.setText(playTitleList.get(playIndex));
        showControl();
    }

    /**
     * //鉴权
     *
     * @param progId 曲目Id
     */
    private void Auth(String progId) {
        asyncHttpRequest.getUserAuth(this, progId, 2, Appconfig.getKeyNo(getApplicationContext()), this, this, null);
    }

    private void getPlayerRtsp(String progId) {

        String fileType = fileTypeList.get(playIndex);
        if (TextUtils.equals(fileType, "1")) { //视频播放
            iv_video_bg.setImageResource(R.mipmap.home_bg);
        } else { //音乐
            iv_video_bg.setImageResource(R.mipmap.play_music_bg);
        }
//        progId = "4585385";   // TODO: 17/1/17
        final String url = Appconfig.getEpg_url(getApplicationContext()) + "/defaultHD/en/go_authorization.jsp?playType=1&progId=" + progId + "&contentType=0&business=1&baseFlag=0&idType=FSN";
        VolleyRequest.getRTSPUrl(progId, url, Appconfig.getCookie(getApplicationContext()), new IVolleyRequestSuccess() {
            @Override
            public void onSucceeded(String method, final String vodId, final Object object) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        onVodDuration(1803000);// TODO: 17/1/17
                        if (null == object) {
                            hiFiDialogTools.showtips(PlayVideoActivity.this, "获取播放连接失败,请稍后再试.", new MyDialogEnterListener() {
                                @Override
                                public void onClickEnter(Dialog dialog, Object object) {
                                    isBuyBack = true;
                                    finish();
                                }
                            });
                            return;
                        }
                        vodPlayerUri = object.toString();
                        Log.d("PlayVideoActivity", "播放链接  " + vodPlayerUri);
                        String netType = Uri.parse(vodPlayerUri).getQueryParameter("NetType");
                        if (!TextUtils.isEmpty(netType) && TextUtils.equals(netType.toLowerCase(), "ip")) {
                            streamType = CqApplication.StreamTypeINet;
                        } else {
                            streamType = CqApplication.StreamTypeIpqam;
                        }
                        playUrl(vodPlayerUri);
                    }
                });
            }
        });
    }

    private void setPlayInfo() {
        if ((quickCount + video_player_progress.getProgress()) < 0 || (quickCount + video_player_progress.getProgress()) > video_player_progress.getMax()) {
            return;
        }
        vodPayingTime = vodPayingTime + quickCount / QUICK_INTERVAL_ITEM / 1000;
        Log.d("PlayVideoActivity", "setPlayInfo: video_player_progress.getProgress() + quickCount   " + (video_player_progress.getProgress() + quickCount));
        Log.d("PlayVideoActivity", "setPlayInfo: vodPayingTime  " + vodPayingTime);
        String playingSeek = StrTool.generateTime((video_player_progress.getProgress() + quickCount) * vodDur / video_player_progress.getMax());
        tv_quick_info.setText(playingSeek + "/" + mTxtDuration.getText().toString());
    }

    //设置播放或者暂停
    private void setPauseOrPlay() {
        if (baseInterface == null) {
            return;
        }
        if (isPlaying) {
            vedio_player_pause.setImageResource(R.mipmap.player_play);
            vedio_player_pause.setVisibility(View.VISIBLE);
            baseInterface.pause();
            isPlaying = false;
            uiHandler.removeMessages(PLAY_TIME);
        } else {
            vedio_player_pause.setImageResource(R.mipmap.player_pause);
            vedio_player_pause.setVisibility(View.GONE);
            baseInterface.resume();
            isPlaying = true;
            uiHandler.sendEmptyMessage(PLAY_TIME);
        }
    }

    private void showControl() {
        uiHandler.removeMessages(HIDE_CONTROL);
        uiHandler.sendEmptyMessageDelayed(HIDE_CONTROL, 5000);
        uiHandler.sendEmptyMessage(SHOW_CONTROL);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        showControl();
        if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
            iv_quick_icon.setImageResource(R.mipmap.quick_back);
            ll_play_quick.setVisibility(View.VISIBLE);
            startQuickThread(QUICK_CUT_PROGRESS);
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
            iv_quick_icon.setImageResource(R.mipmap.quick_forward);
            ll_play_quick.setVisibility(View.VISIBLE);
            startQuickThread(QUICK_ADD_PROGRESS);
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (rl_play_list.getVisibility() == View.VISIBLE) {
                rl_play_list.setVisibility(View.GONE);
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER || keyCode == KeyEvent.KEYCODE_ENTER) {
            setPauseOrPlay();
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT || keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
            uiHandler.removeMessages(QUICK_CUT_PROGRESS);
            uiHandler.removeMessages(QUICK_ADD_PROGRESS);
            ll_play_quick.setVisibility(View.GONE);
            stopQuickThread();
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_UP || keyCode == KeyEvent.KEYCODE_DPAD_DOWN || keyCode == KeyEvent.KEYCODE_MENU) {
            if (rl_play_list.getVisibility() == View.GONE) {
                showPlayList();
                return true;
            }
        }
        return super.onKeyUp(keyCode, event);
    }


    private void playVodByIndex() {
        if (playIndex >= playVodIdList.size()) {
            finish();
            return;
        }
        isPlaying = false;
        vodPayingTime = 0;
        video_player_progress.setProgress(0);
        iv_video_bg.setVisibility(View.VISIBLE);
        uiHandler.removeMessages(PLAY_TIME);
        mTxtPlayTime.setText("00:00");
        if (baseInterface != null) {
            baseInterface.stop();
        }
        tv_title.setText(playTitleList.get(playIndex));
        Auth(playTrackIdList.get(playIndex));
        showControl();
    }

    private void playNext() {
        playIndex++;
        playVodByIndex();
    }

    private void stopQuickThread() {
        startQuick = false;
        quickSeek();
    }

    private void startQuickThread(int what) {
        if (startQuick) {
            return;
        }
        Log.d("quickCount", "开始计时线程" + what);
        startQuick = true;
        uiHandler.sendEmptyMessage(what);
    }

    private void quickSeek() {
        long progress = (video_player_progress.getProgress() + quickCount) * vodDur / video_player_progress.getMax();
        vodPayingTime = progress / 1000;
        if (vodPayingTime <= 0) {
            vodPayingTime = 0;
        }
        if (baseInterface != null) {
            baseInterface.seek(progress);
        }
        quickCount = 0;
    }

    private void playUrl(String playUrl) {
        showControl();
        this.vodPlayerUri = playUrl;
        if (baseInterface == null) {
            playFragment = VodPlayFragment.createInstance(ChongqingUUIDs.ID, VodPlayFragment.HUAWEI);
            baseInterface = playFragment.getPlayInterface(PlayVideoActivity.this);
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.add(playFragment, null);
            transaction.commitAllowingStateLoss();
        } else {
            baseInterface.start(vodPlayerUri, 3, streamType, 0);
        }
    }

    @Override
    public void onContextReady(String s) {
        if (baseInterface != null) {
            String fileType = fileTypeList.get(playIndex);
            baseInterface.start(vodPlayerUri, 3, streamType, 0);
            baseInterface.setVolume(1);//-1.0~1.0
            if (TextUtils.equals(fileType, "1")) { //视频播放
                DisplayMetrics dm = new DisplayMetrics();
                //取得窗口属性
                getWindowManager().getDefaultDisplay().getMetrics(dm);
                //窗口的宽度
                int screenWidth = dm.widthPixels;
                int screenHeight = dm.heightPixels;
                baseInterface.setDisplay(0, 0, screenWidth, screenHeight);//播放视频区域 x,y,w,h
            } else {
                baseInterface.setDisplay(0, 0, 10, 10);//播放视频区域 x,y,w,h
            }
        }
    }

    @Override
    public void onVodDuration(long l) {
        Log.d("PlayVideoActivity", "播放时长  " + l);
        vodDur = l;
        mTxtDuration.setText(StrTool.generateTime(l));

    }

    @Override
    public void onSeeBackPeriod(long l, long l1) {

    }

    @Override
    public void onPlayStart(boolean b) {
        isPlaying = true;
        vodPayingTime = 0;
        video_player_progress.setProgress(0);
        uiHandler.removeMessages(PLAY_TIME);
        uiHandler.sendEmptyMessage(PLAY_TIME);

        String fileType = fileTypeList.get(playIndex);
        if (TextUtils.equals(fileType, "1")) { //视频播放
            iv_video_bg.setVisibility(View.GONE); // TODO: 17/1/17
            int pointTime = pointList.get(playIndex);
            if (pointTime < (vodDur / 1000 - 30)&&pointTime>0) { //历史播放位置小于播放时长前30秒
                quickCount = (int) (pointTime * video_player_progress.getMax() * 1000 / vodDur);
                quickSeek();
            }
        } else {
            iv_video_bg.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onSourceRate(float v) {

    }

    @Override
    public void onSourceSeek(long l) {
        hiFiDialogTools.showtips(PlayVideoActivity.this, "onSourceSeek: " + l, null);
    }

    @Override
    public void onSyncMediaTime(long l) {
        hiFiDialogTools.showtips(PlayVideoActivity.this, "onSyncMediaTime: " + l, null);
    }

    @Override
    public void onPlayEnd(float v) {
        addPlayRecord();//播放完成后添加到历史播放记录
        isPlaying = false;
        iv_video_bg.setVisibility(View.VISIBLE);
        playNext();
    }

    @Override
    public void onPlayMsg(String s) {

    }

    @Override
    public void onPlayError(String s) {
        Log.d("PlayVideoActivity", "播放出错  " + s);
    }


    @Override
    public void onSucceeded(String method, String key, Object object) throws Exception {
        if ("single.action".equals(method)) {//第一个接口,查询订购状态返回
            if (object == null)
                return;

            authBean = (UserAuthResponeBean) object;
            UserAuthResponeBean.DataBean result = authBean.getData();
            if (result == null) {
                hiFiDialogTools.showtips(PlayVideoActivity.this, "订购状态: " + authBean.getMsg(), null);
                return;
            }

            if (result.getIsfree() == ConstantEnum.Player_Auth_Free || TextUtils.equals(result.getUserCode(), ConstantEnum.Music_Auth_status_IsOrder)) {//免费或者已订购不做处理
                freeTime = -1; // 用户可以光看完视频
            } else if (TextUtils.equals(result.getUserCode(), ConstantEnum.Music_Auth_status_blacklist)) { //黑名单  不让播放  不让订购
                hiFiDialogTools.showtips(this, R.string.string_black, null);
                return;
            } else {//否则设置为免费播放前30秒钟
                freeTime = 30;//// TODO: 17/1/18
            }
            getPlayerRtsp(playVodIdList.get(playIndex));//鉴权完成，获取播放链接，进行播放
            return;
        }
    }

    @Override
    public void onFailed(String method, String key, int errorTipId) throws Exception {

    }

    @Override
    public void finish() {
        if (playVodIdList.size() == 1 && !isBuyBack) {
            Intent intent = new Intent();
            intent.putExtra("playEnd", true);
            setResult(RESULT_OK, intent);
        }
        super.finish();
        addPlayRecord();
    }

    private void addPlayRecord() {
        if (authBean != null && authBean.getData() != null) {
            if (playIndex >= playVodIdList.size()) {
                playIndex = playVodIdList.size() - 1;
            }
            asyncHttpRequest.addPlayRecord(PlayVideoActivity.this, authBean.getRecordId() + "", playVodIdList.get(playIndex), playTitleList.get(playIndex),
                    vodPayingTime + "", vodPayingTime + "", Appconfig.getKeyNo(getApplicationContext()), null, null, null);
        }
    }

    private void showPlayList() {
        rl_play_list.setVisibility(View.VISIBLE);
        lv_play.setAdapter(new ItemAdapter());
        lv_play.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                addPlayRecord(); //选择视频后添加到历史播放记录
                playIndex = i;
                playVodByIndex();
                rl_play_list.setVisibility(View.GONE);
            }
        });
    }

    class ItemAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return playTitleList.size();
        }

        @Override
        public Object getItem(int i) {
            return playTitleList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View contentView = View.inflate(PlayVideoActivity.this, R.layout.item_play_list, null);
            TextView tv_name = (TextView) contentView;
            tv_name.setText(playTitleList.get(i));
            return contentView;
        }
    }


}
