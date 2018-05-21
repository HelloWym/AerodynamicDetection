package com.jds.aerodynamicdetection.main;

import com.jds.aerodynamicdetection.base.MyPresenterINF;
import com.jds.aerodynamicdetection.base.MyViewINF;
import com.jds.aerodynamicdetection.model.DetectionData;

/**
 * Created by tao on 2018-05-15.
 */

public interface Contract
{

    interface Presenter extends MyPresenterINF
    {
        void getLatestDetectionData();
    }

    interface View extends MyViewINF<Presenter>
    {
        void updateNewDetectionData(DetectionData data);
    }
}
