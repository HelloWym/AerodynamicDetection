package com.jds.aerodynamicdetection.main;

import android.graphics.Color;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.jds.aerodynamicdetection.R;
import com.jds.aerodynamicdetection.base.MyActivity;
import com.jds.aerodynamicdetection.model.DetectionData;

import java.util.ArrayList;
import java.util.List;

import static com.jds.aerodynamicdetection.Params.max_point_of_chart;

/**
 * Created by tao on 2018-05-15.
 */

public class MainView extends MyActivity implements Contract.View
{
    private Contract.Presenter mPresenter;

    private TextView txtTorque, txtFrequent, txtRevolution, txtFlowrate, txtGasPressure;

    //图
    private LineChart mLineChart;
    //图数据
    private LineData mLineData;
    //线
    private LineDataSet mDataSet;
    //线数据
    private List<Entry> series;
    //图表更新的总数
    private long totalDot = 0;

    public static final int FLAG_TORQUE = 0;
    public static final int FLAG_FREQUENT = 1;
    public static final int FLAG_REVOLUTION = 2;
    public static final int FLAG_FLOWRATE = 3;
    public static final int FLAG_GASPRESSURE = 4;
    private int flag = 0;

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
    protected void onDestroy()
    {
        mHandler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }

    @Override
    protected void initView()
    {
        super.initView();
        setPresenter(new MainPresenter(this));
        txtTorque = (TextView) findViewById(R.id.txt_torque);
        txtTorque.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                switchAData();
            }
        });
        txtFrequent = (TextView) findViewById(R.id.txt_frequent);
        txtFrequent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchBData();
            }
        });
        txtRevolution = (TextView) findViewById(R.id.txt_revolution);
        txtRevolution.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchCData();
            }
        });
        txtFlowrate = (TextView) findViewById(R.id.txt_flowrate);
        txtFlowrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchDData();
            }
        });
        txtGasPressure = (TextView) findViewById(R.id.txt_gasPressure);
        txtGasPressure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchEData();
            }
        });
        mLineChart = (LineChart) findViewById(R.id.chart_main);
        initChart();

        mPresenter.start();

    }

    /**
     * 初始化图表参数
     */
    private void initChart()
    {
        //X轴
        XAxis xAxis =mLineChart.getXAxis();
        xAxis.setAxisMinimum(0);
        xAxis.setAxisMaximum(max_point_of_chart-1);
        xAxis.setAvoidFirstLastClipping(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        xAxis.setValueFormatter(new IAxisValueFormatter()
        {
            @Override
            public String getFormattedValue(float value, AxisBase axis)
            {
                int t = (int) value;
                if(totalDot < max_point_of_chart)
                    return String.valueOf(t);
                else
                    return String.valueOf(totalDot-max_point_of_chart + 1 + t);
            }
        });

        //左Y
        YAxis leftAxis = mLineChart.getAxisLeft();
        leftAxis.setAxisMinimum(-55);
        leftAxis.setAxisMaximum(55);

        //右Y
        mLineChart.getAxisRight().setEnabled(false);

        series = new ArrayList<>();     //线数据
        series.add(new Entry(0,0));     //预置一个0

        mDataSet = new LineDataSet(series, "Label");        //线
        mDataSet.setColor(Color.GREEN);
        mDataSet.setValueTextColor(Color.BLUE);

        mLineData = new LineData(mDataSet);                 //图数据
        mLineChart.setData(mLineData);                      //图

    }

    @Override
    public void updateNewDetectionData(DetectionData data)
    {
        if(data==null)
            return;

        txtTorque.setText(String.valueOf(data.getaTorque()));
        txtFrequent.setText(String.valueOf(data.getbFrequent()));
        txtRevolution.setText(String.valueOf(data.getcRevolution()));
        txtFlowrate.setText(String.valueOf(data.getdFlowrate()));
        txtGasPressure.setText(String.valueOf(data.geteGasPressure()));

    }

    @Override
    public void updateChart(DetectionData data) {
        int newValue = 0;
        switch(flag)
        {
            case FLAG_TORQUE:
                newValue = data.getaTorque();
                break;
            case FLAG_FREQUENT:
                newValue = data.getbFrequent();
                break;
            case FLAG_REVOLUTION:
                newValue = data.getcRevolution();
                break;
            case FLAG_FLOWRATE:
                newValue = data.getdFlowrate();
                break;
            case FLAG_GASPRESSURE:
                newValue = data.geteGasPressure();
                break;
            default:
                break;
        }

        totalDot++;

        Entry entry = new Entry(series.size(), newValue);
        series.add(entry);

        if(series.size() > max_point_of_chart)
        {
            series.remove(0);
            for(Entry en : series)
            {
                en.setX(en.getX() - 1);
            }
        }

        mDataSet.notifyDataSetChanged();
        mLineData.notifyDataChanged();
        mLineChart.notifyDataSetChanged();
        mLineChart.invalidate();
    }

    @Override
    public void updateChartAndLatestValue(DetectionData data) {
        updateChart(data);
        updateNewDetectionData(data);
    }

    @Override
    public void switchAData() {
        flag = FLAG_TORQUE;
        totalDot = 0;
        series.clear();
    }

    @Override
    public void switchBData() {
        flag = FLAG_FREQUENT;
        totalDot = 0;
        series.clear();
    }

    @Override
    public void switchCData() {
        flag = FLAG_REVOLUTION;
        totalDot = 0;
        series.clear();
    }

    @Override
    public void switchDData() {
        flag = FLAG_FLOWRATE;
        totalDot = 0;
        series.clear();
    }

    @Override
    public void switchEData() {
        flag = FLAG_GASPRESSURE;
        totalDot = 0;
        series.clear();
    }

}
