package cqdatasdk.network;

import android.content.Context;
import android.text.TextUtils;
import android.webkit.URLUtil;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.cq.ln.R;
import com.cq.ln.constant.ConstantEnum;
import com.cq.ln.utils.XLog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

import cqdatasdk.interfaces.IVolleyRequestSuccess;
import cqdatasdk.interfaces.IVolleyRequestfail;


/**
 * Created by YS on 2016/2/26.
 * 类说明：
 * 网络接口请求的统一方式，使用Volley框架
 */
public class VolleyRequest {

    private static final String TAG = "VolleyRequest";
    private final int SOCKET_TIMEOUT = 20 * 1000;// 设置连接20秒超时
    private final int RETRIES_MAXNUM = 1; // 重试次数
    private Context mContext;
    private RequestQueue mQueue;
    private String requestMethodName = "";
    private IVolleyRequestSuccess mDelegateSuccess = null;
    private IVolleyRequestfail mDelegateFail = null;

    public VolleyRequest(Context context, RequestQueue queue, IVolleyRequestSuccess succeed) {
        mContext = context;
        mQueue = queue;
        mDelegateSuccess = succeed;
    }

    public VolleyRequest(Context context, RequestQueue queue, IVolleyRequestSuccess succeed, IVolleyRequestfail failed) {
        mContext = context;
        mQueue = queue;
        mDelegateSuccess = succeed;
        mDelegateFail = failed;
    }


    /**
     * @param key 此key，在搜索页面做数据缓存关联非常重要！！
     * @param url
     */
    public void executeGetRequest(final String key, String url) {
        if (TextUtils.isEmpty(url)) {
            XLog.d("url is empty！");
            return;
        }
        if (url.indexOf("?") != -1) {
            requestMethodName = url.substring(url.lastIndexOf("/") + 1, url.indexOf("?"));
        } else
            requestMethodName = url.substring(url.lastIndexOf("/") + 1, url.length());
        XLog.d("volley 请求，请求URL：" + url + ",请求方法：" + requestMethodName);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Method.GET, url, null, new Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                if (mDelegateSuccess != null)
                    try {
                        mDelegateSuccess.onSucceeded(requestMethodName, key, jsonObject);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                XLog.d("请求接口：" + requestMethodName + " 成功，返回数据：" + jsonObject.toString());

            }
        }, new ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {


                if (mDelegateFail != null)
                    try {
                        mDelegateFail.onFailed(requestMethodName, key, parseErr(volleyError));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                XLog.d("volleyError.toString():" + volleyError.toString());
            }
        });
        jsonObjectRequest.setRetryPolicy(getRetryPolicy());
        mQueue.add(jsonObjectRequest);

    }

    public void executeGetRequest(String url) {
        executeGetRequest(null, url);
    }


    /**
     * @param context
     * @param url
     */
    public void executeGetStringRequest(final Context context, final String url) {
        if (TextUtils.isEmpty(url)) {
            XLog.d(TAG, "url is empty!");
            return;
        }
        if (!URLUtil.isNetworkUrl(url))
            return;
        StringRequest stringRequest = new StringRequest(Method.GET, url, new Listener<String>() {
            @Override
            public void onResponse(String s) {
                XLog.d(TAG, "请求成功返回:" + s);
                if (mDelegateSuccess != null)
                    try {
                        mDelegateSuccess.onSucceeded("", null, s);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
            }
        }, new ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (mDelegateFail != null)
                    try {
                        mDelegateFail.onFailed(requestMethodName, null, parseErr(volleyError));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                XLog.d(TAG, "volleyError.toString():" + volleyError.toString());
            }
        });

        stringRequest.setRetryPolicy(getRetryPolicy());
        mQueue.add(stringRequest);

    }


    public static void getRTSPUrl(final String progIdTag, final String url, final String cookies, final IVolleyRequestSuccess success) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                String tag = progIdTag;
                String respone = null;
                try {
                    XLog.d("cookies = " + cookies);
                    respone = sendHttpReuqest(url, cookies);
                } catch (Exception e) {
                    XLog.d(TAG, e.toString());
                }
                if (null != success)
                    try {
                        success.onSucceeded(ConstantEnum.Method_Get_RTSP, tag, parseRTSP(respone));
                    } catch (Exception e) {
                        XLog.d(TAG, e.toString());
                    }
            }
        }).start();

    }

    private static String parseRTSP(String response) {
        if (response != null && response != "") {
            try {
                JSONObject result = new JSONObject(response);
                String play = result.getString("playUrl");
                String res[] = play.split("\\^");
                String playUrl = res[4] + "";
                return playUrl;
            } catch (Exception e) {
                XLog.d(TAG, "parseRTSP Exception e:" + e);
            }
        }
        return null;
    }

    private static String sendHttpReuqest(String serviceURL, String myCookie) throws Exception {

        try {
            XLog.d(TAG, "serviceURL = " + serviceURL);

            URL url = new URL(serviceURL);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");// 设置请求的方式
            urlConnection.setReadTimeout(30000);// 设置超时的时间
            urlConnection.setConnectTimeout(30000);// 设置链接超时的时间
            urlConnection.setRequestProperty("Cookie", myCookie);
            urlConnection.setRequestProperty("Content-type", "text/html");
            urlConnection.setRequestProperty("Accept-Charset", "utf-8");
            urlConnection.setRequestProperty("contentType", "utf-8");
            // 获取响应的状态码 404 200 505 302
            if (urlConnection.getResponseCode() == 200) {
                // 获取响应的输入流对象
                InputStream is = urlConnection.getInputStream();
                BufferedReader in = new BufferedReader(new InputStreamReader(is, "GBK"));
                StringBuffer buffer = new StringBuffer();
                String line = "";
                while ((line = in.readLine()) != null) {
                    buffer.append(line);
                }
                String result = buffer.toString();
                XLog.d(TAG, "获取RTSP返回数据：" + result);
                is.close();
                in.close();
                return result;
            } else {
                return "requestError";
            }
        } catch (Exception e) {
            XLog.d(TAG, e.toString());
            return "requestError";
        }
    }


    /**
     * 中断（取消）网络请求
     */
    public void cancel(String method) {
        if (mQueue != null)
            mQueue.cancelAll(method);
    }

    /**
     * 解析错误信息
     *
     * @param error
     * @return
     */
    private int parseErr(VolleyError error) {
        if (error instanceof TimeoutError) {
            return R.string.string_connectServiceTimeOut;
        } else if (error instanceof NetworkError) {
            return R.string.string_NetWorderror;
        } else if (error instanceof NoConnectionError) {
            return R.string.string_noNetWord;
        } else if ((error instanceof ServerError)) {
            NetworkResponse response = error.networkResponse;
            if (response != null) {
                // handleError(error, response);
                XLog.d("服务器响应错误码" + String.valueOf(response.statusCode));
            }
            return R.string.string_innerServiceFailed;
        } else if ((error instanceof AuthFailureError)) {
            return R.string.string_authServiceFailed;
        }
        return R.string.string_networdException;

    }


    // 重试机制
    private RetryPolicy getRetryPolicy() {
        return new DefaultRetryPolicy(SOCKET_TIMEOUT, RETRIES_MAXNUM, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
    }


    //===========================================================以下暂无使用===========================================================
    class JsonUTF8Request extends JsonRequest<JSONObject> {
        public JsonUTF8Request(int method, String url, JSONObject jsonRequest, Listener<JSONObject> listener, ErrorListener errorListener) {
            super(method, url, (jsonRequest == null) ? null : jsonRequest.toString(), listener, errorListener);
        }

        @Override
        protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
            try {
                // solution 1:
                String jsonString = new String(response.data, "UTF-8");
                // solution 2:
                // response.headers.put(HTTP.CONTENT_TYPE,
                // response.headers.get("content-type"));
                // String jsonString = new String(response.data,
                // HttpHeaderParser.parseCharset(response.headers));
                //
                return Response.success(new JSONObject(jsonString), HttpHeaderParser.parseCacheHeaders(response));
            } catch (UnsupportedEncodingException e) {
                return Response.error(new ParseError(e));
            } catch (JSONException je) {
                return Response.error(new ParseError(je));
            }
        }
    }

}
