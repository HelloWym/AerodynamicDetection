package com.jds.aerodynamicdetection.base;

import android.app.Application;
import android.content.Context;

/** 自定义Application
 * Created by tao on 2018/2/20.
 */

public class MyApp extends Application {

    private static Context mContext = null;

    @Override
    public void onCreate() {
        super.onCreate();

        if(mContext==null)
           mContext = getApplicationContext();

        LogToFile.init(this);

        //TODO 修复
//        //TODO APPID
//        Bugly.init(getApplicationContext(), "c259e2c23f", false);
//
//        //XG
//        XGPushConfig.enableDebug(this,true);
//        XGPushManager.registerPush(this, new XGIOperateCallback() {
//            @Override
//            public void onSuccess(Object data, int flag) {
//                //token在设备卸载重装的时候有可能会变
//                Log.d("TPush", "注册成功，设备token为：" + data);
//            }
//            @Override
//            public void onFail(Object data, int errCode, String msg) {
//                Log.d("TPush", "注册失败，错误码：" + errCode + ",错误信息：" + msg);
//            }
//        });
//        XGPushManager.bindAccount(getApplicationContext(), "XINGE");


    }

    /**
     * 流程
     *
     * - 引入mybase
     * - 引入标准样式styles dimens colors anim文件夹
     * - 修改application，修改主题style，string.xml-appname
     *
     * 规范
     *
     * res/layout
     * 1. act frag view item
     * 2. 业务 start menu
     * 3. 权限 admin manager
     *
     * 1. txt_username txt_password input_username image_icon
     * 2. txtUsername txtPassword
     *
     * app/java/com.example.activity
     * 1. 变量 - 界面控件变量 共享变量
     *
     * 问题：
     *  线程周期性更新界面，定时器放在presenter还是view？
     *      view是activity，runOnUIThread比较方便。
     *      view的init初始化变量，present.start();然后开启其他线程。
     *
     */

    /**
     * 全局获取Application实例
     * @return
     */
    public static Context getContext()
    {
        return mContext;
    }

}
