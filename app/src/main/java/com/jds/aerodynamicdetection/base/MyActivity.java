package com.jds.aerodynamicdetection.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/** 父Activity
 * Created by tao on 2018/1/17.
 */

public abstract class MyActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        initView();

    }

    protected abstract int getLayoutId();

    protected void initView()
    {

    }

    public void jumpActivity(Class aim)
    {
        Intent intent = new Intent(this, aim);
        startActivity(intent);
    }

}

