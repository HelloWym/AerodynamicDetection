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

        void getMaxData();

        void exportData(String start, String end);
    }

    interface View extends MyViewINF<Presenter>
    {
        //更新数值显示
        void updateNewDetectionData(DetectionData data);

        void updateMaxData(DetectionData data);

        void showExportSuccess();

        void showExportFail();

    }
}
