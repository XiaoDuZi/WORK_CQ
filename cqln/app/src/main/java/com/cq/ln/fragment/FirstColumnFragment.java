package com.cq.ln.fragment;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.cq.ln.CqApplication;
import com.cq.ln.R;
import com.cq.ln.constant.ConstantEnum;
import com.cq.ln.utils.ActivityUtility;
import com.cq.ln.utils.ImageTool;
import com.cq.ln.utils.XLog;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cqdatasdk.bean.HiFiMainPageBean;
import cqdatasdk.network.URLVerifyTools;

/**
 * 第1个栏目
 */
@TargetApi(Build.VERSION_CODES.M)
public class FirstColumnFragment extends BaseFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @Bind(R.id.effHandpick_one)
    ImageButton mEffHandpickOne;
    @Bind(R.id.effHandpick_two)
    ImageButton mEffHandpickTwo;
    @Bind(R.id.effHandpick_three)
    ImageButton mEffHandpickThree;
    @Bind(R.id.effHandpick_four)
    ImageButton mEffHandpickFour;
    @Bind(R.id.effHandpick_Five)
    ImageButton mEffHandpickFive;
    @Bind(R.id.effHandpick_Six)
    ImageButton mEffHandpickSix;
    @Bind(R.id.effHandpick_Seven)
    ImageButton mEffHandpickSeven;
    @Bind(R.id.effHandpick_eight)
    ImageButton mEffHandpickEight;
    @Bind(R.id.effHandpick_Nine)
    ImageButton mEffHandpickNine;
    @Bind(R.id.Fragment_RootView)
    FrameLayout mMyFrameLayout;

    // TODO: Rename and change types of parameters
    private int nextUpId;
    private String mParam2;
    private List<ImageButton> imageButtonList;


    public FirstColumnFragment() {
        // Required empty public constructor
    }

    public static FirstColumnFragment newInstance(int param1, String param2) {
        FirstColumnFragment fragment = new FirstColumnFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            nextUpId = getArguments().getInt(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_handpick, container, false);
        ButterKnife.bind(this, view);

        traversalView((ViewGroup) view);
        createBorderView(view);

        XLog.d(this.getClass().getSimpleName() + "--->onCreateView");
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initImageButtonList();
    }

    private void initImageButtonList() {
        imageButtonList = new ArrayList<>();
        imageButtonList.add(mEffHandpickOne);
        imageButtonList.add(mEffHandpickTwo);
        imageButtonList.add(mEffHandpickThree);
        imageButtonList.add(mEffHandpickFour);
        imageButtonList.add(mEffHandpickFive);
        imageButtonList.add(mEffHandpickSix);
        imageButtonList.add(mEffHandpickSeven);
        imageButtonList.add(mEffHandpickEight);
        imageButtonList.add(mEffHandpickNine);


        mEffHandpickOne.setNextFocusUpId(nextUpId);
        mEffHandpickTwo.setNextFocusUpId(nextUpId);
        mEffHandpickThree.setNextFocusUpId(nextUpId);
        mEffHandpickFour.setNextFocusUpId(nextUpId);

        mEffHandpickFive.setNextFocusDownId(R.id.rl_image);
        mEffHandpickSix.setNextFocusDownId(R.id.rl_image);
        mEffHandpickSeven.setNextFocusDownId(R.id.rl_image);
        mEffHandpickEight.setNextFocusDownId(R.id.rl_image);
        mEffHandpickNine.setNextFocusDownId(R.id.rl_image);
    }

    public void bindData(HiFiMainPageBean.ClassifyBean bean, DisplayImageOptions options) {
        Map<String, HiFiMainPageBean.ClassifyBean.ClassifyItemBean> map;
        Set<String> keySet;
        HiFiMainPageBean.ClassifyBean.ClassifyItemBean itemBean;
        for (int i = 0; i < bean.tuList.size(); i++) {
            map = bean.tuList.get(i);
            keySet = map.keySet();
            itemBean = map.get(keySet.iterator().next());
            if (itemBean != null && imageButtonList != null) {
                imageButtonList.get(i).setTag(R.id.tag_for_MainPage_Bean, itemBean);
                ImageTool.loadImageAndUserHandleOption(getActivity(), URLVerifyTools.formatMainImageUrl(itemBean.img), imageButtonList.get(i), options, R.mipmap.default_small);
            }
            if (itemBean.linkType== ConstantEnum.linkType_rechange){
                CqApplication.mRechangeBean = itemBean;
            }
        }
    }


    @OnClick({R.id.effHandpick_one, R.id.effHandpick_two, R.id.effHandpick_three,
            R.id.effHandpick_four, R.id.effHandpick_Five, R.id.effHandpick_Six,
            R.id.effHandpick_Seven, R.id.effHandpick_eight, R.id.effHandpick_Nine})
    public void onClickView(View v) {
        try {
            ActivityUtility.MainFragmentHandleClickEvent_(getActivity(), v, nextUpId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        XLog.d(this.getClass().getSimpleName() + "===> onDestroyView");
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
