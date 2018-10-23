package com.asana.hbl.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.asana.hbl.R;
import com.asana.hbl.fragment.DashboardFragment;
import com.asana.hbl.fragment.PlayByPlayFragment;
import com.asana.hbl.fragment.SingleGameDataFregment;
import com.asana.hbl.fragment.SingleGameFragment;
import com.asana.hbl.model.HBLSingleGameData;
import com.asana.hbl.model.HblGames;
import com.asana.hbl.utils.HBLImageLoader;
import com.asana.hbl.utils.HttpClient;
import com.asana.hbl.utils.RestApi;
import com.asana.hbl.utils.TestData;
import com.asana.hbl.views.CircularImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gary on 2018/10/4.
 */

public class SingleGameActivity extends BaseActivity {
    final private String TAG = "HBL-SingleGameActivity";
    private TextView mTvActionBar;
    private HblGames mGamedata;
    private HBLSingleGameData mHBLSingleGameData;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private List<String > mList = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sigle_game);
        initActionBar();
        mTvActionBar = (TextView)findViewById(R.id.title);
        Intent i = getIntent();
        String stGamedata = i.getStringExtra("game_data");
        if(stGamedata != null){
            try {
                JSONObject jb = new JSONObject(stGamedata);
                mGamedata = new HblGames(jb);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            SimpleDateFormat formater = new SimpleDateFormat("MM/dd/yyyy EEE");
            mTvActionBar.setText(formater.format(mGamedata.playDate).toString());
            RelativeLayout rl = (RelativeLayout)findViewById(R.id.game_item_list);
            TextView txt1,txt2, txt3, txt4, txtHome, txtGuest;
            CircularImageView imgHome, imgGuest;
            txt1 = (TextView) findViewById(R.id.tv1);
            txt3 = (TextView) findViewById(R.id.tv3);
            txt4 = (TextView) findViewById(R.id.tv4);
            txtHome = (TextView) findViewById(R.id.tv_home);
            txtGuest = (TextView) findViewById(R.id.tv_guest);
            imgHome = (CircularImageView) findViewById(R.id.image_home);
            imgGuest = (CircularImageView) findViewById(R.id.image_guest);
            txt1.setText(mGamedata.location);
            txt3.setText(mGamedata.currentQuarter);
            txt4.setText(mGamedata.playTime);
            JSONObject jobject;
            try {
                jobject =  mGamedata.teamHomeRecord;
                txtHome.setText(jobject.getString("score"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                jobject =  mGamedata.teamGuestRecord;
                txtGuest.setText(jobject.getString("score"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            HBLImageLoader.loadTeamImage(getApplicationContext(), mGamedata.teamHomeLogo, imgHome);

            HBLImageLoader.loadTeamImage(getApplicationContext(), mGamedata.teamGuestLogo, imgGuest);
        }

        mViewPager =  (ViewPager) findViewById(R.id.view_pager);
        mTabLayout = (TabLayout) findViewById(R.id.simpleTabLayout);

        HttpClient httpClient = new HttpClient();
        HttpClient.HttpResponseCallback callback = new HttpClient.HttpResponseCallback() {
            @Override
            public void onResponse(Bundle result) {
                try {
                    JSONObject jsonObject = null;
                    jsonObject = new JSONObject(result.getString("response"));
                    try{
                       initViewPager(jsonObject);
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };

        if(TestData.useTestData){
            try{
                JSONObject jsonObject = new JSONObject(TestData.TEST_DATA_2);
                initViewPager(jsonObject);
            } catch(JSONException e) {
                e.printStackTrace();
            }
        }else{
            httpClient.async_query_GET(RestApi.getSingleGameData(mGamedata.mGameSN), null, callback);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void initActionBar(){
        ActionBar ab = getSupportActionBar();
        ab.setDisplayShowCustomEnabled(true);
        LayoutInflater inflater = (LayoutInflater)ab.getThemedContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View customActionBar = inflater.inflate( R.layout.activity_sigle_game_titlebar, null);
        ab.setCustomView(customActionBar);
        ab.setDisplayHomeAsUpEnabled(true);

        Toolbar toolbar = (Toolbar)ab.getCustomView().getParent();
        toolbar.setContentInsetsAbsolute(0, 0);
        toolbar.getContentInsetEnd();
        toolbar.setPadding(0, 0, 0, 0);
    }

    private void initViewPager(JSONObject jsonObject){
        mHBLSingleGameData = new HBLSingleGameData(jsonObject);
        TabLayout.Tab tab1 = mTabLayout.newTab();
        tab1.setText(R.string.title_games);
        mTabLayout.addTab(tab1);

        TabLayout.Tab tab2 = mTabLayout.newTab();
        tab2.setText(R.string.game_data);
        mTabLayout.addTab(tab2);

        TabLayout.Tab tab3 = mTabLayout.newTab();
        tab3.setText(R.string.play_by_play);
        mTabLayout.addTab(tab3);


        mTabLayout.setTabTextColors(ContextCompat.getColor(this, R.color.colorCustomTabTextUnSelected),ContextCompat.getColor(this, R.color.colorCustomTabTextSelected));
        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        PagerAdapter adapter = new PagerAdapter
                (getSupportFragmentManager(), mTabLayout.getTabCount(), mGamedata);
        mViewPager.setAdapter(adapter);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
    }

    class PagerAdapter extends FragmentStatePagerAdapter {
        int mNumOfTabs;
        HblGames mData;
        public PagerAdapter(FragmentManager fm, int NumOfTabs, HblGames gameData) {
            super(fm);
            this.mNumOfTabs = NumOfTabs;
            this.mData = gameData;
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment;
            switch(position){
                case 0:
                    fragment = SingleGameFragment.newInsTance();
                    break;
                case 1:
                    fragment = SingleGameDataFregment.newInsTance();
                    break;
                case 2:
                    fragment = PlayByPlayFragment.newInsTance();
                    break;
                default:
                    fragment = DashboardFragment.newInsTance();
                    break;
            }
            Bundle args = new Bundle();
            args.putString("arg", mHBLSingleGameData.getDataInString());
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getCount() {
            return mNumOfTabs;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
