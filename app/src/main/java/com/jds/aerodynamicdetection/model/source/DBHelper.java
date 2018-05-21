package com.jds.aerodynamicdetection.model.source;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by tao on 2018-05-15.
 */

public class DBHelper extends SQLiteOpenHelper
{
    public static final int DATABASE_VERSION = 5;

    public static final String DATABASE_NAME = "Data.db";

    public static final String TABLE_DETECTION_DATA = "DetectionData";

    private static final String SQL_CREATE_ENTRIES =
            "create table "+TABLE_DETECTION_DATA+"("
                    +"id integer primary key autoincrement,"
                    +"aTorque integer,"
                    +"bFrequent integer,"
                    +"cRevolution integer,"
                    +"dFlowrate integer,"
                    +"eGasPressure integer,"
                    +"fTemp integer,"
                    +"gTemp integer,"
                    +"hTemp integer,"
                    +"dateRecord text,"
                    +"ms integer"
                    +")";

    public DBHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("drop table if exists "+TABLE_DETECTION_DATA);
        onCreate(db);
    }
}
