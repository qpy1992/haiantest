package com.example.haiantest.util;

//import android.support.v7.app.AppCompatActivity;
//import android.view.View;
//
//import com.blankj.utilcode.util.ActivityUtils;
//import com.bt.smart.truck_broker.MyApplication;
//import com.bt.smart.truck_broker.moudle.login.LoginActivity;
//import com.bt.smart.truck_broker.utils.networkutil.entry.UserLoginBiz;
//import com.bt.smart.truck_broker.widget.localddata.LoginEventBean;
//import com.kongzue.dialog.interfaces.OnDialogButtonClickListener;
//import com.kongzue.dialog.interfaces.OnDismissListener;
//import com.kongzue.dialog.util.BaseDialog;
//import com.kongzue.dialog.v3.MessageDialog;
//
//import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Token 拦截器
 */
public class TokenHeaderInterceptor implements Interceptor {

    @Override public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);
//        if (response.code() == 401) {
//            if (MyApplication.logout) {
//                MyApplication.logout = false;
//                MessageDialog.show(MyApplication.appCompatContext, "提示",
//                        "已经在别的设备登录,需要重新登录上线", "重新登录", "退出").setOnOkButtonClickListener(new OnDialogButtonClickListener() {
//                    @Override
//                    public boolean onClick(BaseDialog baseDialog, View v) {
//                        MyApplication.appCompatContext.finish();
//                        MyApplication.logout = true;
//                        SpUtils.putString(MyApplication.appCompatContext,"psd","");
//                        UserLoginBiz.getInstance(MyApplication.getContext()).logout();
//                        EventBus.getDefault().post(new LoginEventBean(LoginEventBean.LOG_OUT));
//                        ActivityManager.getAppManager().finishAllActivity();
//                        ActivityUtils.startActivity(LoginActivity.class);
//                        return false;
//                    }
//                }).setOnCancelButtonClickListener(new OnDialogButtonClickListener() {
//                    @Override
//                    public boolean onClick(BaseDialog baseDialog, View v) {
//                        SpUtils.putString(MyApplication.appCompatContext,"psd","");
//                        System.exit(0);
//                        return false;
//                    }
//                }).setCancelable(false);
//
//            }
//        }
        return response;
    }
}