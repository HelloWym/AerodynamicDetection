package com.jds.aerodynamicdetection.model.source;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import com.jds.aerodynamicdetection.model.DetectionData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tao on 2018-05-15.
 */

public class DataRepository implements DataSource
{

    private static DataRepository instance = null;
    private DBHelper mDbHelper;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    //船新的数据
    private DetectionData timelyData = new DetectionData();

    //保留最大值
    private DetectionData maxData = new DetectionData();


    private DataRepository(@NonNull Context context)
    {
        mDbHelper = new DBHelper(context);
    }

    public static DataRepository getInstance(@NonNull Context context)
    {
        if(instance==null)
            instance = new DataRepository(context);

        return instance;
    }

    @Override
    public void getData(String start, String end, LoadDataCallback callback)
    {
        List<DetectionData> datas = new ArrayList<DetectionData>();
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {
                "id",
                "dateRecord",
                "aTorque",
                "bFrequent",
                "cRevolution",
                "dFlowrate",
                "eGasPressure",
                "fTemp",
                "gTemp",
                "hTemp"
        };

        String selection = "dateRecord > ? AND dateRecord < ? limit 500";
        String[] selectionArgs = {start, end};

        Cursor c = db.query(
                DBHelper.TABLE_DETECTION_DATA, projection, selection, selectionArgs, null, null, null);

        if (c != null && c.getCount() > 0) {
            while (c.moveToNext()) {
                long id = c.getLong(c.getColumnIndexOrThrow("id"));
                int a = c.getInt(c.getColumnIndexOrThrow("aTorque"));
                int b = c.getInt(c.getColumnIndexOrThrow("bFrequent"));
                int c1 = c.getInt(c.getColumnIndexOrThrow("cRevolution"));
                int d = c.getInt(c.getColumnIndexOrThrow("dFlowrate"));
                int e = c.getInt(c.getColumnIndexOrThrow("eGasPressure"));
                int f = c.getInt(c.getColumnIndexOrThrow("fTemp"));
                int g = c.getInt(c.getColumnIndexOrThrow("gTemp"));
                int h = c.getInt(c.getColumnIndexOrThrow("hTemp"));
                String dateRecord = c.getString(c.getColumnIndexOrThrow("dateRecord"));

                DetectionData data = new DetectionData();
                data.setId(id);
                data.setaTorque(a);
                data.setbFrequent(b);
                data.setcRevolution(c1);
                data.setdFlowrate(d);
                data.seteGasPressure(e);
                data.setfTemp(f);
                data.setgTemp(g);
                data.sethTemp(h);
                data.setDateRecord(dateRecord);
                //不用显示ms，未取出
                datas.add(data);
            }
        }
        if (c != null) {
            c.close();
        }

        db.close();

        if (datas.isEmpty()) {
            // This will be called if the table is new or just empty.
            callback.onDataNotAvailable();
        } else {
            callback.onDataLoaded(datas);
        }

    }



    @Override
    public String insertData(DetectionData data)
    {
        if(data==null)
            return "插入数据不合法";

        //插入数据库
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues entity = new ContentValues();
        entity.put("aTorque", data.getaTorque());
        entity.put("bFrequent", data.getbFrequent());
        entity.put("cRevolution", data.getcRevolution());
        entity.put("dFlowrate", data.getdFlowrate());
        entity.put("eGasPressure", data.geteGasPressure());
        entity.put("fTemp", data.getfTemp());
        entity.put("gTemp", data.getgTemp());
        entity.put("hTemp", data.gethTemp());
        entity.put("dateRecord", data.getDateRecord());
        entity.put("ms", data.getMs());

        db.insert(DBHelper.TABLE_DETECTION_DATA, null, entity);
        db.close();

        return "";
    }

    @Override
    public void updateNewData(DetectionData data)
    {
        timelyData = data;
    }

    @Override
    public void updateMaxData(DetectionData data)
    {
//        if(data.getaTorque()>maxData.getaTorque())
//            maxData.setaTorque(data.getaTorque());
//        if(data.getbFrequent()>maxData.getbFrequent())
//            if(data.getcRevolution()>maxData.getcRevolution())
//                maxData.setbFrequent(data.getbFrequent());
//        maxData.setcRevolution(data.getcRevolution());
//        if(data.getdFlowrate()>maxData.getdFlowrate())
//            maxData.setdFlowrate(data.getdFlowrate());
//        if(data.geteGasPressure()>maxData.geteGasPressure())
//            maxData.seteGasPressure(data.geteGasPressure());

        maxData = data;

    }

    @Override
    public DetectionData getMaxData()
    {
        return maxData;
    }

    @Override
    public void resetMaxData()
    {
        maxData = new DetectionData();
    }

    @Override
    public String delData(long id)
    {
        return "暂未实现";
    }

    @Override
    public DetectionData getLatestData()
    {
        if (System.currentTimeMillis()-timelyData.getMs()>1500) //1.5s没更新数据，置零
            timelyData = new DetectionData();

        return timelyData;
    }



}
