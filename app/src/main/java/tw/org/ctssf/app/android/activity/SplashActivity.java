package tw.org.ctssf.app.android.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import tw.org.ctssf.app.android.MainActivity;
import tw.org.ctssf.app.android.R;
import tw.org.ctssf.app.android.utils.HttpClient;
import tw.org.ctssf.app.android.utils.NetworkUtil;
import tw.org.ctssf.app.android.utils.RestApi;
import tw.org.ctssf.app.android.utils.Utils;

/**
 * Created by Gary on 2018/10/9.
 */

public class SplashActivity extends BaseActivity{
    String mDataGame, mDataDashboard, mDataAnalysis;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getSupportActionBar().hide();
        setContentView(R.layout.activity_splash);

        HttpClient httpClient1 = new HttpClient();
        HttpClient.HttpResponseCallback callback1 = new HttpClient.HttpResponseCallback() {
            @Override
            public void onResponse(Bundle result) {
                try {
                    mDataGame = result.getString("response");
                    SharedPreferences spref = getSharedPreferences(Utils.SHAREED_PREFERENCE_KEY, MODE_PRIVATE);
                    SharedPreferences.Editor editor = spref.edit();
                    editor.clear();
                    editor.putString("game_data", mDataGame);
                    editor.apply();
                    editor.commit();

                    Intent i = new Intent();
                    i.setClass(SplashActivity.this, MainActivity.class);
                    SplashActivity.this.startActivity(i);
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        };

        HttpClient httpClient2 = new HttpClient();
        HttpClient.HttpResponseCallback callback2 = new HttpClient.HttpResponseCallback() {
            @Override
            public void onResponse(Bundle result) {
                try {
                    mDataDashboard = result.getString("response");
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        };

        HttpClient httpClient3 = new HttpClient();
        HttpClient.HttpResponseCallback callback3 = new HttpClient.HttpResponseCallback() {
            @Override
            public void onResponse(Bundle result) {
                try {
                    mDataAnalysis = result.getString("response");
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        };
        if(NetworkUtil.getConnectivityStatus(this) == NetworkUtil.NETWORK_STATUS_NOT_CONNECTED){
            showNoNetWorkDialog();
        }else{
            httpClient1.async_query_GET(RestApi.getGameList(RestApi.SEASON_SN), null, callback1);
        }
    }

    @Override
    public void onResume(){
        super.onResume();
    }
}
