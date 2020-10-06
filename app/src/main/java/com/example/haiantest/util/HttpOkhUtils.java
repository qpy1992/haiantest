package com.example.haiantest.util;


import com.google.gson.Gson;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;


/**
 * @创建者 AndyYan
 * @创建时间 2018/4/26 8:42
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */

public class HttpOkhUtils {
    // 网络请求超时时间值(s)
    private static final int DEFAULT_TIMEOUT = 30;
    private static HttpOkhUtils okhUtils;
    private OkHttpClient client;

    private HttpOkhUtils() {
        Interceptor logInterceptor = new HttpLoggingInterceptor(new HttpLog()).setLevel(HttpLoggingInterceptor.Level.BODY);
        TokenHeaderInterceptor tokenHeaderInterceptor = new TokenHeaderInterceptor();
        client = new OkHttpClient.Builder()
                .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .addInterceptor(tokenHeaderInterceptor)
                .addInterceptor(logInterceptor)
                .build();
    }

    public static HttpOkhUtils getInstance() {
        if (okhUtils == null) {
            synchronized (HttpOkhUtils.class) {
                if (okhUtils == null)
                    okhUtils = new HttpOkhUtils();
            }
        }
        return okhUtils;
    }

    public void doGet(String url, HttpCallBack httpCallBack) {
        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new StringCallBack(request, httpCallBack));
    }

    public void doGetWithOnlyHeader(String url, RequestParamsFM headbean, HttpCallBack httpCallBack) {
        Request.Builder builder1 = new Request.Builder();
        if (null != headbean) {
            Set<String> set1 = headbean.keySet();
            for (String key : set1) {
                builder1.addHeader(key, headbean.get(key).toString());
            }
        }
        Request request = builder1.url(url).build();
        client.newCall(request).enqueue(new StringCallBack(request, httpCallBack));
    }

    public Response doGetWithOnlyHeaderSyn(String url, RequestParamsFM headbean) throws IOException {
        Request.Builder builder1 = new Request.Builder();
        if (null != headbean) {
            Set<String> set1 = headbean.keySet();
            for (String key : set1) {
                builder1.addHeader(key, headbean.get(key).toString());
            }
        }
        Request request = builder1.url(url).build();
        return client.newCall(request).execute();
    }

    public void download(String url,Callback callback){
        Request request=new Request.Builder().url(url).get().build();
        Call call=client.newCall(request);
        call.enqueue(callback);
    }
    public void doGetWithParams(String url, RequestParamsFM bean, HttpCallBack httpCallBack) {
        url = url + "?";
        Iterator iter = bean.entrySet().iterator();
        while (iter.hasNext()) {
            Object next = iter.next();
            if (null != next) {
                RequestParamsFM.Entry entry = (RequestParamsFM.Entry) next;
                Object key = entry.getKey();
                Object value = entry.getValue();
                url = url + key + "=" + value + "&";
            } else {
                RequestParamsFM.Entry entry = (RequestParamsFM.Entry) next;
                Object key = entry.getKey();
                Object value = entry.getValue();
                url = url + key + "=" + value;
            }
        }
        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new StringCallBack(request, httpCallBack));
    }

    public void doGetWithHeadParams(String url, RequestParamsFM headbean, RequestParamsFM bean, HttpCallBack httpCallBack) {
        Request.Builder builder1 = new Request.Builder();
        if (null != headbean) {
            Set<String> set1 = headbean.keySet();
            for (String key : set1) {
                builder1.addHeader(key, headbean.get(key).toString());
            }
        }
        url = url + "?";
        Iterator iter = bean.entrySet().iterator();
        while (iter.hasNext()) {
            Object next = iter.next();
            if (null != next) {
                RequestParamsFM.Entry entry = (RequestParamsFM.Entry) next;
                Object key = entry.getKey();
                Object value = entry.getValue();
                url = url + key + "=" + value + "&";
            } else {
                RequestParamsFM.Entry entry = (RequestParamsFM.Entry) next;
                Object key = entry.getKey();
                Object value = entry.getValue();
                url = url + key + "=" + value;
            }
        }
        Request request = builder1.url(url).build();
        client.newCall(request).enqueue(new StringCallBack(request, httpCallBack));
    }

    public void doPost(String url, RequestParamsFM bean, HttpCallBack httpCallBack) {
        RequestBody requestBody;
        boolean toJson = bean.getIsUseJsonStreamer();
        if (toJson) {
            String json;
            JSONObject jsonObject = new JSONObject();
            Set<String> set1 = bean.keySet();
            for (Iterator<String> it = set1.iterator(); it.hasNext(); ) {
                String key = it.next();
                try {
                    jsonObject.put(key, bean.get(key));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            json = jsonObject.toString();
            //MediaType  设置Content-Type 标头中包含的媒体类型值
            //requestBody = FormBody.create(MediaType.parse("application/json; charset=utf-8"), json);
            requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        } else {
            FormBody.Builder builder = new FormBody.Builder();
            Set<String> set = bean.keySet();
            for (String key : set) {
                String value = bean.get(key).toString();
                builder.add(key, value);
            }
            requestBody = builder.build();
        }

        Request request = new Request.Builder().url(url).post(requestBody).build();
        client.newCall(request).enqueue(new StringCallBack(request, httpCallBack));
    }

    public void doPostBean(String url, RequestParamsFM bean, HttpCallBack httpCallBack) {
        RequestBody requestBody;
        boolean toJson = bean.getIsUseJsonStreamer();
        if (toJson) {
            String json;
            JSONObject jsonObject = new JSONObject();
            Set<String> set1 = bean.keySet();
            for (Iterator<String> it = set1.iterator(); it.hasNext(); ) {
                String key = it.next();
                try {
                    jsonObject.put(key, bean.get(key));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            json = jsonObject.toString();
            //MediaType  设置Content-Type 标头中包含的媒体类型值
            //requestBody = FormBody.create(MediaType.parse("application/json; charset=utf-8"), json);
            requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        } else {
            FormBody.Builder builder = new FormBody.Builder();
            Set<String> set = bean.keySet();
            for (String key : set) {
                String value = bean.get(key).toString();
                builder.add(key, value);
            }
            requestBody = builder.build();
        }
        Request request = new Request.Builder().url(url).post(requestBody).build();
        client.newCall(request).enqueue(new StringCallBack(request, httpCallBack));
    }


    public void doPostWithHeader(String url, RequestParamsFM headeBean, RequestParamsFM bean, HttpCallBack httpCallBack) {
        Request.Builder builder1 = new Request.Builder();
        if (null != headeBean) {
            Set<String> set1 = headeBean.keySet();
            for (String key : set1) {
                builder1.addHeader(key, headeBean.get(key).toString());
            }
        }
        RequestBody requestBody;
        boolean toJson = bean.getIsUseJsonStreamer();
        if (toJson) {
            String json;
            JSONObject jsonObject = new JSONObject();
            Set<String> set1 = bean.keySet();
            for (Iterator<String> it = set1.iterator(); it.hasNext(); ) {
                String key = it.next();
                try {
                    jsonObject.put(key, bean.get(key));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            json = jsonObject.toString();
            //MediaType  设置Content-Type 标头中包含的媒体类型值
            //requestBody = FormBody.create(MediaType.parse("application/json; charset=utf-8"), json);
            requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        } else {
            FormBody.Builder builder = new FormBody.Builder();
            Set<String> set2 = bean.keySet();
            for (String key : set2) {
                String value = bean.get(key).toString();
                builder.add(key, value);
            }
            requestBody = builder.build();
        }
        Request request = builder1.url(url).post(requestBody).build();
        client.newCall(request).enqueue(new StringCallBack(request, httpCallBack));
    }

    public void doPostByUrl(String url, RequestParamsFM headeBean, String key, RequestParamsFM bean, HttpCallBack httpCallBack) {
        Request.Builder builder1 = new Request.Builder();
        if (null != headeBean) {
            Set<String> set1 = headeBean.keySet();
            for (String keyHead : set1) {
                builder1.addHeader(keyHead, headeBean.get(keyHead).toString());
            }
        }
        JSONObject jsonObject = new JSONObject();
        Set<String> set1 = bean.keySet();
        for (Iterator<String> it = set1.iterator(); it.hasNext(); ) {
            String paramKey = it.next();
            try {
                jsonObject.put(paramKey, bean.get(paramKey));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        String json = jsonObject.toString();
        FormBody.Builder builder = new FormBody.Builder();
        builder.add(key, json);
        RequestBody requestBody = builder.build();
        Request request = builder1.url(url).post(requestBody).build();
        client.newCall(request).enqueue(new StringCallBack(request, httpCallBack));
    }

    public void doPut(String url, RequestParamsFM bean, HttpCallBack httpCallBack) {
        RequestBody requestBody;
        boolean toJson = bean.getIsUseJsonStreamer();
        if (toJson) {
            Gson gson = new Gson();
            //使用Gson将对象转换为json字符串
            String json = gson.toJson(bean);
            //MediaType  设置Content-Type 标头中包含的媒体类型值
            requestBody = FormBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        } else {
            FormBody.Builder builder = new FormBody.Builder();
            Set<String> set = bean.keySet();
            for (String key : set) {
                String value = bean.get(key).toString();
                builder.add(key, value);
            }
            requestBody = builder.build();
        }

        Request request = new Request.Builder().url(url).put(requestBody).build();
        client.newCall(request).enqueue(new StringCallBack(request, httpCallBack));
    }

    public void doPutWithHeader(String url, RequestParamsFM headeBean, RequestParamsFM bean, HttpCallBack httpCallBack) {
        Request.Builder builder1 = new Request.Builder();
        if (null != headeBean) {
            Set<String> set1 = headeBean.keySet();
            for (String key : set1) {
                builder1.addHeader(key, headeBean.get(key).toString());
            }
        }

        RequestBody requestBody;
        boolean toJson = bean.getIsUseJsonStreamer();
        if (toJson) {
            String json;
            JSONObject jsonObject = new JSONObject();
            Set<String> set1 = bean.keySet();
            for (Iterator<String> it = set1.iterator(); it.hasNext(); ) {
                String key = it.next();
                try {
                    jsonObject.put(key, bean.get(key));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            json = jsonObject.toString();
            //MediaType  设置Content-Type 标头中包含的媒体类型值
            //requestBody = FormBody.create(MediaType.parse("application/json; charset=utf-8"), json);
            requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        } else {
            FormBody.Builder builder = new FormBody.Builder();
            Set<String> set = bean.keySet();
            for (String key : set) {
                String value = bean.get(key).toString();
                builder.add(key, value);
            }
            requestBody = builder.build();
        }

        Request request = builder1.url(url).put(requestBody).build();
        client.newCall(request).enqueue(new StringCallBack(request, httpCallBack));
    }

    public void doDelete(String url, RequestParamsFM bean, HttpCallBack httpCallBack) {
        url = url + "?";
        Iterator iter = bean.entrySet().iterator();
        while (iter.hasNext()) {
            Object next = iter.next();
            if (null != next) {
                RequestParamsFM.Entry entry = (RequestParamsFM.Entry) next;
                Object key = entry.getKey();
                Object value = entry.getValue();
                url = url + key + "=" + value + "&";
            } else {
                RequestParamsFM.Entry entry = (RequestParamsFM.Entry) next;
                Object key = entry.getKey();
                Object value = entry.getValue();
                url = url + key + "=" + value;
            }
        }
        Request request = new Request.Builder().url(url).delete().build();
        client.newCall(request).enqueue(new StringCallBack(request, httpCallBack));
    }

    public void doDeleteOnlyWithHead(String url, RequestParamsFM headBean, HttpCallBack httpCallBack) {
        Request.Builder builder1 = new Request.Builder();
        if (null != headBean) {
            Set<String> set1 = headBean.keySet();
            for (String key : set1) {
                builder1.addHeader(key, headBean.get(key).toString());
            }
        }
        //        url = url + "?";
        //        Iterator iter = bean.entrySet().iterator();
        //        while (iter.hasNext()) {
        //            Object next = iter.next();
        //            if (null != next) {
        //                RequestParamsFM.Entry entry = (RequestParamsFM.Entry) next;
        //                Object key = entry.getKey();
        //                Object value = entry.getValue();
        //                url = url + key + "=" + value + "&";
        //            } else {
        //                RequestParamsFM.Entry entry = (RequestParamsFM.Entry) next;
        //                Object key = entry.getKey();
        //                Object value = entry.getValue();
        //                url = url + key + "=" + value;
        //            }
        //        }
        Request request = builder1.url(url).delete().build();
        client.newCall(request).enqueue(new StringCallBack(request, httpCallBack));
    }

    public void upDateFile(String url, RequestParamsFM headbean, RequestParamsFM bean, String fileKey, File file, HttpCallBack httpCallBack) {
        Request.Builder builder1 = new Request.Builder();
        if (null != headbean) {
            Set<String> set1 = headbean.keySet();
            for (String key : set1) {
                builder1.addHeader(key, headbean.get(key).toString());
            }
        }
        RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        if (null != bean) {
            Set<String> set = bean.keySet();
            for (String key : set) {
                String value = bean.get(key).toString();
                builder.addFormDataPart(key, value);
            }
        }
        builder.addFormDataPart(fileKey, file.getName(), fileBody);
        RequestBody requestBody = builder.build();

        Request request = builder1.url(url).post(requestBody).build();
        client.newCall(request).enqueue(new StringCallBack(request, httpCallBack));
    }

    private static class StringCallBack implements Callback {
        private HttpCallBack httpCallBack;
        private Request request;

        public StringCallBack(Request request, HttpCallBack httpCallBack) {
            this.request = request;
            this.httpCallBack = httpCallBack;
        }

        @Override
        public void onFailure(okhttp3.Call call, final IOException e) {
            ThreadUtils.runOnMainThread(new Runnable() {
                @Override
                public void run() {
                    httpCallBack.onError(request, e);
                }
            });
        }

        @Override
        public void onResponse(okhttp3.Call call, Response response) throws IOException {
            if (httpCallBack != null) {
                try {
                    final int code = response.code();
                    final String buffer = response.body().string();
                    ThreadUtils.runOnMainThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                httpCallBack.onSuccess(code, buffer);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public interface HttpCallBack {

        void onError(Request request, IOException e);

        void onSuccess(int code, String resbody);
    }
}
