package tw.org.ctssf.app.android.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import tw.org.ctssf.app.android.utils.NetworkUtil;


/**
 * Created by Gary on 2018/10/16.
 */

public class NetworkChangeReceiver extends BroadcastReceiver {
    OnNetWorkChangeLinster mListener;
    @Override
    public void onReceive(final Context context, final Intent intent) {

        int status = NetworkUtil.getConnectivityStatusString(context);
        if ("tw.org.ctssf.app.android.net.conn.CONNECTIVITY_CHANGE".equals(intent.getAction())) {
            if(mListener != null){
                mListener.onChange(status);
            }
        }
    }

    public void setonNetWorkChangeLinster(OnNetWorkChangeLinster linster){
        mListener = linster;
    }

    public interface OnNetWorkChangeLinster {
        void onChange(int status);
    }
}
