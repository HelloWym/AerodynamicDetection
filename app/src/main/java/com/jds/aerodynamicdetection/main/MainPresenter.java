package com.jds.aerodynamicdetection.main;

import com.jds.aerodynamicdetection.base.MyApp;
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
    }

    @Override
    public void getLatestDetectionData()
    {
        mView.updateNewDetectionData(mDataSource.getLatestData());
    }
}
