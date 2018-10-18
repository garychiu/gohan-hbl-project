package com.asana.hbl.activity;

import android.content.DialogInterface;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.graphics.ColorUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;;
import android.util.TypedValue;
import android.view.Window;
import android.view.WindowManager;


import com.asana.hbl.R;
import com.asana.hbl.Receiver.NetworkChangeReceiver;
import com.asana.hbl.utils.NetworkUtil;

/**
 * Created by Gary on 2018/10/2.
 */

public class BaseActivity extends AppCompatActivity {
    NetworkChangeReceiver mReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int customStatusBarColor =  ColorUtils.blendARGB(Color.parseColor("#a72429"), Color.parseColor("#ffffff"), 0.08F);
        changeStatusBarColor(customStatusBarColor);
    }


    @Override
    public void setContentView(final int layoutResID){
        super.setContentView(layoutResID);
        ActionBar ab = getSupportActionBar();
        if(ab != null) {
            ab.setBackgroundDrawable(new ColorDrawable(ColorUtils.blendARGB(Color.parseColor("#a72429"), Color.parseColor("#ffffff"), 0.08F)));
        }
    }


    public int getActionBarHeight() {
        int actionBarHeight = 0;
        TypedValue tv = new TypedValue();
        if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true))
        {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data,getResources().getDisplayMetrics());
        }
        return actionBarHeight;
    }

    @Override
    protected void onResume(){
        super.onResume();
        if(mReceiver == null) {
            mReceiver = new NetworkChangeReceiver();
        }
        NetworkChangeReceiver.OnNetWorkChangeLinster onNetWorkChangeLinster = new NetworkChangeReceiver.OnNetWorkChangeLinster(){
            @Override
            public void onChange(int status) {
                if(status == NetworkUtil.NETWORK_STATUS_NOT_CONNECTED) {
                    showNoNetWorkDialog();
                }
            }
        };
        mReceiver.setonNetWorkChangeLinster(onNetWorkChangeLinster);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        intentFilter.setPriority(100);
        registerReceiver(mReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(this.mReceiver!=null) {
            unregisterReceiver(this.mReceiver);
        }
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    protected void showNoNetWorkDialog(){
        new AlertDialog.Builder(this)
                .setTitle("No network connection!!!")
                .setIcon(R.mipmap.ic_launcher)
                .setMessage("Pleasee check your network connection")
                .setPositiveButton("OK",new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .show();
    }
    protected void changeStatusBarColor(int color){
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(color);
        }
    }

}
