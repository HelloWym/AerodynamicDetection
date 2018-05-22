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
        //更新数值显示
        void updateNewDetectionData(DetectionData data);
        //更新图表
        void updateChart(DetectionData data);
        //更新图表和数值
        void updateChartAndLatestValue(DetectionData data);
        //切换到A数据图标
        void switchAData();
        void switchBData();
        void switchCData();
        void switchDData();
        void switchEData();

    }
}
