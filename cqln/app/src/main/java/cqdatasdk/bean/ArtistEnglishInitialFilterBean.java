package cqdatasdk.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by godfather on 2016/3/20.
 * 艺术家首字母，刷选使用
 */
public class ArtistEnglishInitialFilterBean implements Serializable {

    public String englishInitial;//A,B,C,....Z 字母
    private static String[] letter = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K",
            "L", "N", "M", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z"};


    public static ArrayList<ArtistEnglishInitialFilterBean> getList() {
        ArrayList<ArtistEnglishInitialFilterBean> mList = new ArrayList<>();
        ArtistEnglishInitialFilterBean bean;
        for (int i = 0; i < letter.length; i++) {
            bean = new ArtistEnglishInitialFilterBean();
            bean.englishInitial = letter[i];
            mList.add(bean);
        }

        return mList;

    }

    private static String[] letter2 = {"专辑收藏", "单曲收藏"};

    public static ArrayList<ArtistEnglishInitialFilterBean> getList2() {
        ArrayList<ArtistEnglishInitialFilterBean> mList = new ArrayList<>();
        ArtistEnglishInitialFilterBean bean;
        for (int i = 0; i < letter2.length; i++) {
            bean = new ArtistEnglishInitialFilterBean();
            bean.englishInitial = letter2[i];
            mList.add(bean);
        }
        return mList;
    }


}
