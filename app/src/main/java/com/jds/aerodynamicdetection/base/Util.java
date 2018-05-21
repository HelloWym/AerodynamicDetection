package com.jds.aerodynamicdetection.base;

import android.util.Log;

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



}
