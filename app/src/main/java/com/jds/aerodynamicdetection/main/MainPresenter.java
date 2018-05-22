package com.jds.aerodynamicdetection.main;

import com.jds.aerodynamicdetection.Params;
import com.jds.aerodynamicdetection.base.MyApp;
import com.jds.aerodynamicdetection.base.Util;
import com.jds.aerodynamicdetection.model.DetectionData;
import com.jds.aerodynamicdetection.model.source.DataRepository;
import com.jds.aerodynamicdetection.model.source.DataSource;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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

        //startThread2();
        updateViewThread();
    }

    /**
     * 界面更新线程
     */
    private void updateViewThread()
    {
        Runnable run = new Runnable() {
            @Override
            public void run() {
                getLatestDetectionData();
                getMaxData();
                MainView.mHandler.postDelayed(this, Params.updatePeriod);
            }
        };

        MainView.mHandler.postDelayed(run, 0);
    }

    @Override
    public void getLatestDetectionData()
    {
        mView.updateNewDetectionData(mDataSource.getLatestData());
    }

    @Override
    public void getMaxData()
    {
        mView.updateMaxData(mDataSource.getMaxData());
    }

    @Override
    public void exportData(final String start, final String end)
    {

        mDataSource.getData(start, end, new DataSource.LoadDataCallback()
        {
            @Override
            public void onDataLoaded(List<DetectionData> datas)
            {
                Date date = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                StringBuilder mSb = new StringBuilder(sdf.format(date)+"导出数据 从"+start+"到"+end+"\r\n");
                for(DetectionData en : datas)
                {
                    mSb.append(
                            en.getDateRecord()+" | "
                            + " 扭矩 : " + en.getaTorque()+" | "
                            + " 频率 : "+ en.getbFrequent()+" | "
                            + " 转速 : " + en.getcRevolution()+" | "
                            + " 流量 : " + en.getdFlowrate()+" | "
                            + " 气压 : " + en.geteGasPressure()+" | "
                            + " 温度1 : " + en.getfTemp()+" | "
                            + " 温度2 : " + en.getgTemp()+" | "
                            + " 温度3 : " + en.gethTemp()+" | "
                            + "\r\n");
                }

                Util.exportToFile(MyApp.getContext(), mSb.toString());

                mView.showExportSuccess();

            }

            @Override
            public void onDataNotAvailable()
            {
                mView.showExportFail();
            }
        });
    }


    /**
     * 测试用生成数据线程
     */
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
                        Thread.sleep(Params.updatePeriod);
                    } catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        });
        insertThread.start();
    }
}
