package com.jds.aerodynamicdetection.main;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.jds.aerodynamicdetection.R;
import com.jds.aerodynamicdetection.base.MyActivity;
import com.jds.aerodynamicdetection.chart.ChartView;
import com.jds.aerodynamicdetection.model.DetectionData;
import com.jds.aerodynamicdetection.service.CoreService;

import java.util.Calendar;

/**
 * Created by tao on 2018-05-15.
 */

public class MainView extends MyActivity implements Contract.View
{
    private Contract.Presenter mPresenter;

    private TextView txtTorque, txtFrequent, txtRevolution, txtFlowrate, txtGasPressure;
    private TextView txtTorqueMax, txtFrequentMax, txtRevolutionMax, txtFlowrateMax, txtGasPressureMax;

    private TextView txtStart, txtEnd;
    private Button btnExport;

    public static Handler mHandler = new Handler();

    @Override
    protected int getLayoutId()
    {
        return R.layout.act_main;
    }

    @Override
    public void setPresenter(Contract.Presenter presenter)
    {
        mPresenter = presenter;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);

        //停止服务
        CoreService.heart = false;
    }

    @Override
    protected void initView()
    {
        super.initView();
        setPresenter(new MainPresenter(this));

        final Calendar ca = Calendar.getInstance();
        final String[] text_start = new String[2];
        final String[] text_end = new String[2];

        final TimePickerDialog time_start_dia = new TimePickerDialog(
                MainView.this,
                // 绑定监听器
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        text_start[1] = (" "+(hourOfDay<10?"0"+hourOfDay:hourOfDay)+":"+(minute<10?"0"+minute:minute)+":00");
                        txtStart.setText(text_start[0] + text_start[1]);

                    }
                }
                // 设置初始时间
                , ca.get(Calendar.HOUR_OF_DAY)
                , ca.get(Calendar.MINUTE)
                // true表示采用24小时制
                ,true);

        final TimePickerDialog time_end_dia =  new TimePickerDialog(
                MainView.this,
                // 绑定监听器
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        text_end[1] = (" "+(hourOfDay<10?"0"+hourOfDay:hourOfDay)+":"+(minute<10?"0"+minute:minute)+":00");
                        txtEnd.setText(text_end[0]+text_end[1]);

                    }
                }
                // 设置初始时间
                , ca.get(Calendar.HOUR_OF_DAY)
                , ca.get(Calendar.MINUTE)
                // true表示采用24小时制
                ,true);

        txtStart = (TextView) findViewById(R.id.txt_start);
        txtStart.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                new DatePickerDialog(MainView.this
                        , new DatePickerDialog.OnDateSetListener()
                        {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth)
                            {
                                text_start[0] = year+"-"
                                        +(month+1<10?"0"+(month+1):(month+1))+"-"
                                        +(dayOfMonth<10?"0"+dayOfMonth:dayOfMonth);

                                time_start_dia.show();
                            }
                        }
                        , ca.get(Calendar.YEAR)
                        , ca.get(Calendar.MONTH)
                        , ca.get(Calendar.DAY_OF_MONTH))
                        .show();
            }

        });
        txtEnd = (TextView) findViewById(R.id.txt_end);
        txtEnd.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                new DatePickerDialog(
                        MainView.this
                        , new DatePickerDialog.OnDateSetListener()
                        {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth)
                            {
                                text_end[0] = year+"-"
                                        +(month+1<10?"0"+(month+1):(month+1))+"-"
                                        +(dayOfMonth<10?"0"+dayOfMonth:dayOfMonth);

                                time_end_dia.show();

                            }
                        }
                        , ca.get(Calendar.YEAR)
                        , ca.get(Calendar.MONTH)
                        , ca.get(Calendar.DAY_OF_MONTH))
                        .show();
            }
        });

        btnExport = (Button) findViewById(R.id.btn_export);
        btnExport.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mPresenter.exportData(txtStart.getText().toString(), txtEnd.getText().toString());
            }
        });


        txtTorque = (TextView) findViewById(R.id.txt_torque);
        txtTorqueMax = (TextView) findViewById(R.id.txt_torque_max);
        txtTorque.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ChartView.open(MainView.this, ChartView.FLAG_TORQUE);
            }
        });
        txtFrequent = (TextView) findViewById(R.id.txt_frequent);
        txtFrequentMax = (TextView) findViewById(R.id.txt_frequent_max);
        txtFrequent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChartView.open(MainView.this, ChartView.FLAG_FREQUENT);
            }
        });
        txtRevolution = (TextView) findViewById(R.id.txt_revolution);
        txtRevolutionMax = (TextView) findViewById(R.id.txt_revolution_max);
        txtRevolution.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChartView.open(MainView.this, ChartView.FLAG_REVOLUTION);
            }
        });
        txtFlowrate = (TextView) findViewById(R.id.txt_flowrate);
        txtFlowrateMax = (TextView) findViewById(R.id.txt_flowrate_max);
        txtFlowrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChartView.open(MainView.this, ChartView.FLAG_FLOWRATE);
            }
        });
        txtGasPressure = (TextView) findViewById(R.id.txt_gasPressure);
        txtGasPressureMax = (TextView) findViewById(R.id.txt_gasPressure_max);
        txtGasPressure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChartView.open(MainView.this, ChartView.FLAG_GASPRESSURE);
            }
        });

        mPresenter.start();




    }

    public static final String S_CUR = " ";
    public static final String S_MAX = " ";

    @Override
    public void updateNewDetectionData(DetectionData data)
    {
        if(data==null)
            return;

        txtTorque.setText(S_CUR + String.valueOf(data.getaTorque()));
        txtFrequent.setText(S_CUR + String.valueOf(data.getbFrequent()));
        txtRevolution.setText(S_CUR + String.valueOf(data.getcRevolution()));
        txtFlowrate.setText(S_CUR + String.valueOf(data.getdFlowrate()));
        txtGasPressure.setText(S_CUR + String.valueOf(data.geteGasPressure()));
    }

    @Override
    public void updateMaxData(DetectionData data)
    {
        if(data==null)
            return;

        txtTorqueMax.setText(S_MAX + String.valueOf(data.getaTorque()));
        txtFrequentMax.setText(S_MAX + String.valueOf(data.getbFrequent()));
        txtRevolutionMax.setText(S_MAX + String.valueOf(data.getcRevolution()));
        txtFlowrateMax.setText(S_MAX + "无");
        txtGasPressureMax.setText(S_MAX + "无");
    }

    @Override
    public void showExportSuccess()
    {
        Toast.makeText(this, "导出成功", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showExportFail()
    {
        Toast.makeText(this, "导出失败，数据为空", Toast.LENGTH_SHORT).show();
    }
}
