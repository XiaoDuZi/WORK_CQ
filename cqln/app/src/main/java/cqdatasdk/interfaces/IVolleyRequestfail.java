package cqdatasdk.interfaces;

/**
 * Created by YS on 2016/2/26.
 * 类说明：
 */
public interface IVolleyRequestfail {

    void onFailed(String method, String key, int errorTipId) throws Exception;
}
