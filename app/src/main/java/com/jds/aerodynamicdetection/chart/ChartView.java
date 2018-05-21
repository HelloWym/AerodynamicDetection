package com.jds.aerodynamicdetection.chart;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.jds.aerodynamicdetection.Params;
import com.jds.aerodynamicdetection.R;
import com.jds.aerodynamicdetection.base.MyActivity;
import com.jds.aerodynamicdetection.model.DetectionData;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.jds.aerodynamicdetection.Params.max_point_of_chart;

/**
 * Created by tao on 2018-05-16.
 */

public class ChartView extends MyActivity implements Contract.View
{
    private Contract.Presenter mPresenter;

    //图
    private LineChart mLineChart;
    //图数据
    private LineData mLineData;
    //线
    private LineDataSet mDataSet;
    //线数据
    private List<Entry> series;

    public static final String EXTRA_FLAG = "flag";
    public static final int FLAG_TORQUE = 0;
    public static final int FLAG_FREQUENT = 1;
    public static final int FLAG_REVOLUTION = 2;
    public static final int FLAG_FLOWRATE = 3;
    public static final int FLAG_GASPRESSURE = 4;

    private int flag = 0;

    private Timer timer;

    private long totalDot = 0;

    //打开该Activity
    public static void open(Activity from, int flag)
    {
        Intent intent = new Intent(from, ChartView.class);
        intent.putExtra(EXTRA_FLAG, flag);
        from.startActivity(intent);
    }

    @Override
    protected int getLayoutId()
    {
        return R.layout.act_chart;
    }

    @Override
    public void setPresenter(Contract.Presenter presenter)
    {
        this.mPresenter = presenter;
    }

    @Override
    protected void onDestroy()
    {
        timer.cancel();
        timer = null;
        mPresenter.stopAllThread();
        super.onDestroy();
    }

    @Override
    protected void initView()
    {
        super.initView();
        Intent intent = getIntent();
        flag = intent.getIntExtra(EXTRA_FLAG, 0);
        setPresenter(new ChartPresenter(this));

        mLineChart = (LineChart) findViewById(R.id.chart_main);
        initChart();

        mPresenter.start();
        startThread();
    }

    //开启UI线程
    private void startThread()
    {

        //定时请求更新界面，因为runOnUiThread所以在View启动
        TimerTask task = new TimerTask()
        {
            @Override
            public void run()
            {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //处理延时任务
                        mPresenter.getLatestDetectionData();
                    }
                });
            }
        };
        timer = new Timer();
        timer.schedule(task, 300, Params.updatePeriod);
    }

    @Override
    public void drawDynamicChart(DetectionData data)
    {//拿最新的数据更新图表
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
        //mDataSet.addEntry(entry);

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


}
