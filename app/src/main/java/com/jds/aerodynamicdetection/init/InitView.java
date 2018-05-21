package com.jds.aerodynamicdetection.init;

import android.content.Intent;
import android.util.DisplayMetrics;
import android.widget.TextView;

import com.jds.aerodynamicdetection.R;
import com.jds.aerodynamicdetection.base.LogToFile;
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

        jumpToMainActivity();

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        LogToFile.d("TAG", "DPI:"+dm.densityDpi+" height:"+dm.heightPixels+" width:"+dm.widthPixels);
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
    }

    @Override
    protected void onDestroy()
    {
        stopService();
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
