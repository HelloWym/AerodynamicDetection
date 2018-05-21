package com.jds.aerodynamicdetection.model;

/**
 * 检测采集数据类
 */
public class DetectionData
{
    //ID索引
    private long id;

    //扭矩
    private int aTorque;

    //频率
    private int bFrequent;

    //转速
    private int cRevolution;

    //气流量
    private int dFlowrate;

    //气流压力
    private int eGasPressure;

    //温度1
    private int fTemp;

    //温度2
    private int gTemp;

    //温度3
    private int hTemp;

    //日期，格式 "yyyy-MM-dd HH:mm:ss"
    private String dateRecord;

    private long ms;

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public int getaTorque()
    {
        return aTorque;
    }

    public void setaTorque(int aTorque)
    {
        this.aTorque = aTorque;
    }

    public int getbFrequent()
    {
        return bFrequent;
    }

    public void setbFrequent(int bFrequent)
    {
        this.bFrequent = bFrequent;
    }

    public int getcRevolution()
    {
        return cRevolution;
    }

    public void setcRevolution(int cRevolution)
    {
        this.cRevolution = cRevolution;
    }

    public int getdFlowrate()
    {
        return dFlowrate;
    }

    public void setdFlowrate(int dFlowrate)
    {
        this.dFlowrate = dFlowrate;
    }

    public int geteGasPressure()
    {
        return eGasPressure;
    }

    public void seteGasPressure(int eGasPressure)
    {
        this.eGasPressure = eGasPressure;
    }

    public int getfTemp()
    {
        return fTemp;
    }

    public void setfTemp(int fTemp)
    {
        this.fTemp = fTemp;
    }

    public int getgTemp()
    {
        return gTemp;
    }

    public void setgTemp(int gTemp)
    {
        this.gTemp = gTemp;
    }

    public int gethTemp()
    {
        return hTemp;
    }

    public void sethTemp(int hTemp)
    {
        this.hTemp = hTemp;
    }

    public String getDateRecord()
    {
        return dateRecord;
    }

    public void setDateRecord(String dateRecord)
    {
        this.dateRecord = dateRecord;
    }

    public long getMs()
    {
        return ms;
    }

    public void setMs(long ms)
    {
        this.ms = ms;
    }
}
