package com.jds.aerodynamicdetection.base;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Date;

/**
 * Created by tao on 2018-04-04.
 */

public class Util
{
    public static void d(String infomation)
    {
        Log.d("BRK", infomation);
    }

    /**
     * 操作成功标志
     */
    public static final int SUCCESS = 1;

    /**
     * 操作失败标志
     */
    public static final int FAIL = -1;

    /**
     * 检查字符串对象是否为空
     * @param input
     * @return
     */
    public static boolean isEmpty(String input){
        if(null==input || "".equals(input) || input.equals("null") || input.equals("NULL"))
            return true;
        return false;
    }

    /**
     * 检查字符串对象是否不为空
     * @param input
     * @return
     */
    public static boolean isNotEmpty(String input)
    {
        return !isEmpty(input);
    }


    private static String logPath = null;

    public static void exportToFile(Context context, String msg) {

        Date date = new Date();
        long ms = date.getTime();

        if (null == logPath) {
            if (Environment.MEDIA_MOUNTED.equals(Environment.MEDIA_MOUNTED)
                    || !Environment.isExternalStorageRemovable()) {//如果外部储存可用
                logPath = context.getExternalFilesDir(null).getPath();//获得外部存储路径,默认路径为 /storage/emulated/0/Android/data/com.waka.workspace.logtofile/files/Logs/log_2016-03-14_16-15-09.log
            } else {
                logPath = context.getFilesDir().getPath();//直接存在/data/data里，非root手机是看不到的
            }
        }

        String fileName = logPath + "/export_" + ms + ".txt";//log日志名，使用时间命名，保证不重复
        //String log = dateFormat.format(date) + " " + type + " " + tag + " " + msg + "\n";//log日志内容，可以自行定制

        //如果父路径不存在
        File file = new File(logPath);
        if (!file.exists()) {
            file.mkdirs();//创建父路径
        }

        FileOutputStream fos = null;//FileOutputStream会自动调用底层的close()方法，不用关闭
        BufferedWriter bw = null;
        try {

            fos = new FileOutputStream(fileName, true);//这里的第二个参数代表追加还是覆盖，true为追加，flase为覆盖
            bw = new BufferedWriter(new OutputStreamWriter(fos));
            bw.write(msg);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bw != null) {
                    bw.close();//关闭缓冲流
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }



}
