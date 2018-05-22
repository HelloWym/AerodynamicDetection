package com.jds.aerodynamicdetection.main;

import com.jds.aerodynamicdetection.Params;
import com.jds.aerodynamicdetection.base.MyApp;
import com.jds.aerodynamicdetection.model.DetectionData;
import com.jds.aerodynamicdetection.model.source.DataRepository;
import com.jds.aerodynamicdetection.model.source.DataSource;

/**
 * Created by tao on 2018-05-15.
 */

public class MainPresenter implements Contract.Presenter
{

    private Contract.View mView;
    private DataSource mDataSource;

    MainPresenter(Contract.View view)
    {
        mView = view;
    }

    @Override
    public void start()
    {
        mDataSource = DataRepository.getInstance(MyApp.getContext());

        startThread2();
        startThread();
    }

    private void startThread()
    {
        Runnable run = new Runnable() {
            @Override
            public void run() {
                getLatestDetectionData();
                MainView.mHandler.postDelayed(this, Params.updatePeriod);
            }
        };

        MainView.mHandler.postDelayed(run, 0);
    }

    private void startThread2()
    {
        final boolean run = true;
        Thread insertThread = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                int i = 1;
                while(run && i<50)
                {
                    DetectionData data = new DetectionData();
                    data.setaTorque(++i);
                    data.setcRevolution(i);

                    mDataSource.insertData(data);
                    data = null;

                    try
                    {
                        Thread.sleep(1000);
                    } catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        });
        insertThread.start();
    }

    @Override
    public void getLatestDetectionData()
    {
        mView.updateChartAndLatestValue(mDataSource.getLatestData());
    }
}
