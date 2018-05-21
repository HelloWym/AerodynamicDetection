package com.jds.aerodynamicdetection.init;

import com.jds.aerodynamicdetection.base.MyPresenterINF;
import com.jds.aerodynamicdetection.base.MyViewINF;

/**
 * 约束
 * Created by tao on 2018-05-15.
 */

public interface Contract
{
    interface Presenter extends MyPresenterINF
    {

    }

    interface View extends MyViewINF<Presenter>
    {
        void showDebugInfo(String info);

        void jumpToMainActivity();

    }

}
