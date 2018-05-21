package com.jds.aerodynamicdetection.main;

import android.view.View;
import android.widget.TextView;

import com.jds.aerodynamicdetection.Params;
import com.jds.aerodynamicdetection.R;
import com.jds.aerodynamicdetection.base.MyActivity;
import com.jds.aerodynamicdetection.chart.ChartView;
import com.jds.aerodynamicdetection.model.DetectionData;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by tao on 2018-05-15.
 */

public class MainView extends MyActivity implements Contract.View
{

    private Contract.Presenter mPresenter;

    private TextView txtTorque, txtFrequent, txtRevolution, txtFlowrate, txtGasPressure;

    private Timer timer;

    @Override
    protected int getLayoutId()
    {
        return R.layout.act_main;
    }

    @Override
    public void setPresenter(Contract.Presenter presenter)
    {
        mPresenter = presenter;
    }

    @Override
    protected void onDestroy()
    {
        timer.cancel();
        timer = null;
        super.onDestroy();
    }

    @Override
    protected void initView()
    {
        super.initView();
        setPresenter(new MainPresenter(this));
        txtTorque = (TextView) findViewById(R.id.txt_torque);
        txtTorque.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ChartView.open(MainView.this, ChartView.FLAG_TORQUE);
            }
        });

        txtFrequent = (TextView) findViewById(R.id.txt_frequent);
        txtRevolution = (TextView) findViewById(R.id.txt_revolution);
        txtFlowrate = (TextView) findViewById(R.id.txt_flowrate);
        txtGasPressure = (TextView) findViewById(R.id.txt_gasPressure);

        mPresenter.start();

        //定时请求更新界面，因为runOnUiThread所以在View启动
        TimerTask task = new TimerTask()
        {
            @Override
            public void run()
            {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //处理延时任务
                        mPresenter.getLatestDetectionData();
                    }
                });
            }
        };
        timer = new Timer();
        timer.schedule(task, 300, Params.updatePeriod);

    }

    @Override
    public void updateNewDetectionData(DetectionData data)
    {
        if(data==null)
            return;

        txtTorque.setText(String.valueOf(data.getaTorque()));
        txtFrequent.setText(String.valueOf(data.getbFrequent()));
        txtRevolution.setText(String.valueOf(data.getcRevolution()));
        txtFlowrate.setText(String.valueOf(data.getdFlowrate()));
        txtGasPressure.setText(String.valueOf(data.geteGasPressure()));

    }


}
