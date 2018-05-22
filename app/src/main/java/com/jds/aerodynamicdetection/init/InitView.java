package com.jds.aerodynamicdetection.init;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import com.jds.aerodynamicdetection.R;
import com.jds.aerodynamicdetection.base.MyActivity;
import com.jds.aerodynamicdetection.main.MainView;
import com.jds.aerodynamicdetection.service.CoreService;
import com.kongqw.serialportlibrary.Device;
import com.kongqw.serialportlibrary.SerialPortFinder;
import com.kongqw.serialportlibrary.listener.OnOpenSerialPortListener;

import java.io.File;
import java.util.ArrayList;

/**
 * 欢迎页，加载模块
 */
public class InitView extends MyActivity implements Contract.View, OnOpenSerialPortListener
{

    private Contract.Presenter mPresent;

    private TextView txtDebug;


    public static final int HANDLER_SERIAL_OK = 10;
    public static final int HANDLER_SERIAL_FAIL = 11;

    public static final int HANDLER_DATABASE_OK = 20;
    public static final int HANDLER_DATABASE_FAIL = 21;

    public static final int HANDLER_ALL_OK = 50;



    public Handler mHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            switch (msg.what){
                case HANDLER_SERIAL_OK:
                    showDebugInfo("串口打开成功");
                    break;
                case HANDLER_DATABASE_OK:
                    showDebugInfo("数据库连接成功");
                    break;
                case HANDLER_ALL_OK:
                    showDebugInfo("加载完成");
                    jumpToMainActivity();
                    break;
            }
        }
    };

    @Override
    protected int getLayoutId()
    {
        //return R.layout.act_main;
        return R.layout.act_init;
    }

    @Override
    public void setPresenter(Contract.Presenter presenter)
    {
        this.mPresent = presenter;
    }

    @Override
    protected void initView()
    {
        super.initView();

        txtDebug = (TextView) findViewById(R.id.txt_debug);

        setPresenter(new InitPresenter(this));
        mPresent.start();

        startService();

        Message msg = new Message();
        msg.what = HANDLER_SERIAL_OK;
        mHandler.sendMessageDelayed(msg, 500);
        msg = new Message();
        msg.what = HANDLER_DATABASE_OK;
        mHandler.sendMessageDelayed(msg, 1000);
        msg = new Message();
        msg.what = HANDLER_ALL_OK;
        mHandler.sendMessageDelayed(msg, 1500);

    }

    private String TAG = "InitView";

    private void startService()
    {
        SerialPortFinder serialPortFinder = new SerialPortFinder();
        ArrayList<Device> devices = serialPortFinder.getDevices();

        if (devices.size() < 6)
            return;

        Intent intent = new Intent(this, CoreService.class);
        intent.putExtra(CoreService.DEVICE, devices.get(5));
        startService(intent);

    }

    private void stopService()
    {
        stopService(new Intent(this, CoreService.class));
    }

    @Override
    public void showDebugInfo(String info)
    {
        txtDebug.append(info+"\r\n");
    }

    @Override
    public void jumpToMainActivity()
    {
        this.jumpActivity(MainView.class);
        finish();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }

    @Override
    public void onSuccess(File file)
    {

    }

    @Override
    public void onFail(File file, Status status)
    {

    }
}
