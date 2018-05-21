package com.jds.aerodynamicdetection.chart;

import com.jds.aerodynamicdetection.base.MyApp;
import com.jds.aerodynamicdetection.model.source.DataRepository;
import com.jds.aerodynamicdetection.model.source.DataSource;

/**
 * Created by tao on 2018-05-16.
 */

public class ChartPresenter implements Contract.Presenter
{

    private Contract.View mView;
    private DataSource mModel;

    private Thread insertThread = null;
    private boolean run = true;

    public ChartPresenter(Contract.View view)
    {
        this.mView = view;
    }


    @Override
    public void start()
    {
        mModel = DataRepository.getInstance(MyApp.getContext());
        startThread();
    }

    /**
     * 开启定时线程
     */
    private void startThread()
    {
//        insertThread = new Thread(new Runnable()
//        {
//            @Override
//            public void run()
//            {
//                int i = 1;
//                while(run && i<50)
//                {
//                    DetectionData data = new DetectionData();
//                    data.setaTorque(i++);
//
//                    mModel.insertData(data);
//                    data = null;
//
//                    try
//                    {
//                        Thread.sleep(1000);
//                    } catch (InterruptedException e)
//                    {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        });
//        insertThread.start();
    }

    @Override
    public void getLatestDetectionData()
    {
        mView.drawDynamicChart(mModel.getLatestData());
    }

    @Override
    public void stopAllThread()
    {
        run = false;
    }
}
