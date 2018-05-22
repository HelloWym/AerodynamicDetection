package com.jds.aerodynamicdetection.model.source;

import com.jds.aerodynamicdetection.model.DetectionData;

import java.util.List;

/**
 * 数据
 * Created by tao on 2018-05-15.
 */

public interface DataSource
{

    /**
     * 获取数据库数据
     *  耗时回调
     * @param start 开始日期，格式yyyy-MM-dd HH:mm:ss
     * @param end   结束日期，格式yyyy-MM-dd HH:mm:ss
     * @param callback 反馈
     * @return
     */
    void getData(String start, String end, LoadDataCallback callback);

    /**
     * 插入新数据到数据库，更新船新数据
     * @param data
     * @return
     */
    String insertData(DetectionData data);

    void updateNewData(DetectionData data);

    void updateMaxData(DetectionData data);

    void resetMaxData();

    DetectionData getMaxData();

    /**
     * 删除单条数据
     * @param id
     * @return
     */
    String delData(long id);

    /**
     * 获取船新的数据
     * @return
     */
    DetectionData getLatestData();


    interface LoadDataCallback
    {
        void onDataLoaded(List<DetectionData> datas);

        void onDataNotAvailable();
    }

}


