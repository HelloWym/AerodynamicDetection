package com.jds.aerodynamicdetection.chart;

import com.jds.aerodynamicdetection.base.MyPresenterINF;
import com.jds.aerodynamicdetection.base.MyViewINF;
import com.jds.aerodynamicdetection.model.DetectionData;

/**
 * Created by tao on 2018-05-16.
 */

public interface Contract
{
    interface Presenter extends MyPresenterINF
    {
        void getLatestDetectionData();

        void stopAllThread();
    }

    interface View extends MyViewINF<Presenter>
    {
        void drawDynamicChart(DetectionData data);
    }

}
