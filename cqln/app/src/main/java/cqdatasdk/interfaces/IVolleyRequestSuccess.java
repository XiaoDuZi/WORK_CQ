package cqdatasdk.interfaces;

/**
 * Created by YS on 2016/2/26.
 * 类说明：
 */
public interface IVolleyRequestSuccess {
    void onSucceeded(String method, String key, Object object) throws Exception;//添加一个key，为缓存数据使用标志
}
