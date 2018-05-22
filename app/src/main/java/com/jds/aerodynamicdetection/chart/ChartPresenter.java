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



    public ChartPresenter(Contract.View view)
    {
        this.mView = view;
    }


    @Override
    public void start()
    {
        mModel = DataRepository.getInstance(MyApp.getContext());

    }


    @Override
    public void getLatestDetectionData()
    {
        mView.drawDynamicChart(mModel.getLatestData());
    }

}
