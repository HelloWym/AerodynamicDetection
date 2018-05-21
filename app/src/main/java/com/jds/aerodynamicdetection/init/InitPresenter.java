package com.jds.aerodynamicdetection.init;

import com.jds.aerodynamicdetection.base.MyApp;
import com.jds.aerodynamicdetection.model.source.DataRepository;
import com.jds.aerodynamicdetection.model.source.DataSource;

/**
 * 逻辑
 * Created by tao on 2018-05-15.
 */

public class InitPresenter implements Contract.Presenter
{
    private Contract.View mView;
    private DataSource mModel;

    @Override
    public void start()
    {
        //绑定Model
        mModel = DataRepository.getInstance(MyApp.getContext());
        if(mView==null || mModel==null);//结束程序

//        try {
//            String command = "chmod 777 " + "/dev";
//            Log.i("d", "command = " + command);
//            Runtime runtime = Runtime.getRuntime();
//
//            Process proc = runtime.exec(command);
//        } catch (IOException e) {
//            Log.i("d","chmod fail!!!!");
//            e.printStackTrace();
//        }


//        try
//        {
//            Thread.sleep(800);
//        } catch (InterruptedException e)
//        {
//            e.printStackTrace();
//        }

        //跳转
        //mView.jumpToMainActivity();

    }

    //构造器
    public InitPresenter()
    {

    }

    //构造器2
    public InitPresenter(Contract.View view)
    {
        this.mView = view;
    }

}
