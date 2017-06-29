package cqdatasdk.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;

public class JsonUtil {

    /**
     * Mode转Json字符串
     *
     * @param <T>
     * @param t
     * @return
     */
    //@Deprecated
    public static synchronized <T> String getJsonStr(T t) {
        Gson gson = new Gson();
        return gson.toJson(t);
    }

    /**
     * Json转对象，可以是mode,也可以是mode集合
     *
     * @param <T>
     * @param gsonStr
     * @param type
     * @return
     */
    @SuppressWarnings("unchecked")
    public static synchronized <T> T getMode(String gsonStr, Type type) {
        Gson gson = new Gson();
        return (T) gson.fromJson(gsonStr, type);
    }

    //@Deprecated
    @SuppressWarnings("unchecked")
    public static synchronized <T> T getMode2(String gsonStr, Type type) {
        Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping()
                .create();
        return (T) gson.fromJson(gsonStr, type);
    }


    /**
     * @param code 网络请求，是否逻辑成功，返回的code等于0，则是逻辑成功
     * @return
     */
    public static boolean isLogicSucceed(String code) {
        if (code.equals("0"))
            return true;
        else
            return false;
    }

}
