package com.jds.aerodynamicdetection.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.jds.aerodynamicdetection.base.LogToFile;
import com.jds.aerodynamicdetection.base.Util;
import com.jds.aerodynamicdetection.model.DetectionData;
import com.jds.aerodynamicdetection.model.source.DataRepository;
import com.jds.aerodynamicdetection.model.source.DataSource;
import com.kongqw.serialportlibrary.Device;
import com.kongqw.serialportlibrary.SerialPortManager;
import com.kongqw.serialportlibrary.listener.OnOpenSerialPortListener;
import com.kongqw.serialportlibrary.listener.OnSerialPortDataListener;

import java.io.File;
import java.util.Arrays;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static com.jds.aerodynamicdetection.Params.BAUD_RATE;
import static com.jds.aerodynamicdetection.Params.heartPeriod;
import static com.jds.aerodynamicdetection.Params.receivePeriod;


/**核心服务
 *  接收串口数据
 * Created by tao on 2018-05-15.
 */

public class CoreService extends Service implements OnOpenSerialPortListener
{

    private String TAG = "CoreService";

    private SerialPortManager mSerialPortManager;

    public static final String DEVICE = "device";

    private Device device;

    private DataSource mModel;

    //调试文件内容
    StringBuilder mSb = new StringBuilder("调试信息头部\r\n");

    private Queue<byte[]> msgs= new ArrayBlockingQueue<byte[]>(100);

    private boolean heart = true;

    private byte[] HEART_BREAK = {(byte)0x01, (byte)0x02};

    private Queue<byte[]> tempData = new LinkedBlockingQueue<>();

    @Nullable
    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();

        if(mSerialPortManager==null)
            mSerialPortManager = new SerialPortManager();

        if(mModel == null)
            mModel = DataRepository.getInstance(this);

        mSb.append("onCreate 完成 \r\n");

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        Util.d("onStart");

        //尝试清理原读写线程，如果是系统重启服务
        mSerialPortManager.closeSerialPort();
        heart = true;

        device = (Device) intent.getSerializableExtra(DEVICE);

        if (null != device)
        {
            // 打开串口
            boolean openSerialPort = mSerialPortManager.setOnOpenSerialPortListener(this)
            .setOnSerialPortDataListener(new OnSerialPortDataListener() {
                @Override
                public void onDataReceived(byte[] bytes) {
                    mSb.append("\r\n onDataReceived [ byte[] ]: " + Arrays.toString(bytes));
                    //mSb.append("\r\n onDataReceived [ String ]: " + new String(bytes));

                    tempData.add(bytes);
                }

                @Override
                public void onDataSent(byte[] bytes) {
                    mSb.append("\r\n onDataSent [ byte[] ]: " + Arrays.toString(bytes));
                    //mSb.append("\r\n onDataSent [ String ]: " + new String(bytes));
                }
            })
            .openSerialPort(device.getFile(), BAUD_RATE);



            if(openSerialPort)
            {
                //开启心跳线程
                new Thread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        while (heart)
                        {
                            try
                            {
                                Thread.sleep(heartPeriod);
                            } catch (InterruptedException e)
                            {
                                e.printStackTrace();
                            }

                            mSerialPortManager.sendBytes(HEART_BREAK);
                        }

                    }
                }).start();


                //处理线程
                new Thread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        while (heart)
                        {
                            if(tempData.size()>0)
                            {
                                handleMsg(tempData.poll());
                            }
                            else
                            {
                                try
                                {
                                    Thread.sleep(receivePeriod);
                                } catch (InterruptedException e)
                                {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }).start();

                mSb.append("onStart 完成 "+openSerialPort+"\r\n");
            }
            else
            {
                //串口开启失败 //TODO 提示手动重新打开
            }

        }
        else
        {
            //无设备信息
        }

        return START_REDELIVER_INTENT;
    }



    @Override
    public void onDestroy()
    {
        Util.d("onDestroy"+mSb.toString());

        heart = false;

        mSerialPortManager.closeSerialPort();

        mSb.append("\r\n destroy \r\n");
        LogToFile.d(TAG, mSb.toString());

        super.onDestroy();
    }

    @Override
    public void onSuccess(File file)
    {

    }

    @Override
    public void onFail(File file, Status status)
    {

    }

    private void handleMsg(byte[] msg)
    {
        if(msg.length==29
                && msg[0]==(byte)0xAB && msg[1]==(byte)0xEF
                && msg[25]==(byte)0xFF && msg[26]==(byte)0xFC
                && msg[27]==(byte)0xFF && msg[28]==(byte)0xFF)
        {
            int a = msg[9]*256+msg[10];
            int b = msg[11]*256+msg[12];
            int c = msg[13]*256+msg[14];
            int d = msg[15]*256+msg[16];
            int e = msg[17]*256+msg[18];
            int f = msg[19]*256+msg[20];
            int g = msg[21]*256+msg[22];
            int h = msg[23]*256+msg[24];

            DetectionData data = new DetectionData();
            data.setaTorque(a);
            data.setbFrequent(b);
            data.setcRevolution(c);
            data.setdFlowrate(d);
            data.seteGasPressure(e);
            data.setfTemp(f);
            data.setgTemp(g);
            data.sethTemp(h);

            String info = mModel.insertData(data);
            data = null;

            mSb.append(info+"/r/n");
        }

    }

}
