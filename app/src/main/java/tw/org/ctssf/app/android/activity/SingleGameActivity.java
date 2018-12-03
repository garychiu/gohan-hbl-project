package tw.org.ctssf.app.android.activity;

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

import tw.org.ctssf.app.android.R;
import tw.org.ctssf.app.android.fragment.PlayByPlayFragment;
import tw.org.ctssf.app.android.fragment.SingleGameDataFregment;
import tw.org.ctssf.app.android.fragment.SingleGameFragment;
import tw.org.ctssf.app.android.model.HBLSingleGameData;
import tw.org.ctssf.app.android.model.HblGames;
import tw.org.ctssf.app.android.utils.HBLImageLoader;
import tw.org.ctssf.app.android.utils.HttpClient;
import tw.org.ctssf.app.android.utils.RestApi;
import tw.org.ctssf.app.android.utils.TestData;
import tw.org.ctssf.app.android.views.CircularImageView;

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
        setContentView(tw.org.ctssf.app.android.R.layout.activity_sigle_game);
        initActionBar();
        mTvActionBar = (TextView)findViewById(tw.org.ctssf.app.android.R.id.title);
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
            RelativeLayout rl = (RelativeLayout)findViewById(tw.org.ctssf.app.android.R.id.game_item_list);
            TextView txt1,txt2, txt3, txt4, txtHome, txtGuest, txtHomeName, txtGuestName;
            CircularImageView imgHome, imgGuest;
            txt1 = (TextView) findViewById(tw.org.ctssf.app.android.R.id.tv1);
            txt3 = (TextView) findViewById(tw.org.ctssf.app.android.R.id.tv3);
            txt4 = (TextView) findViewById(tw.org.ctssf.app.android.R.id.tv4);
            txtHome = (TextView) findViewById(tw.org.ctssf.app.android.R.id.tv_home);
            txtHomeName = (TextView) findViewById(tw.org.ctssf.app.android.R.id.tv_home_name);
            txtGuest = (TextView) findViewById(tw.org.ctssf.app.android.R.id.tv_guest);
            txtGuestName = (TextView) findViewById(tw.org.ctssf.app.android.R.id.tv_guest_name);
            imgHome = (CircularImageView) findViewById(tw.org.ctssf.app.android.R.id.image_home);
            imgGuest = (CircularImageView) findViewById(tw.org.ctssf.app.android.R.id.image_guest);
            txt1.setText(mGamedata.location);
            if(mGamedata.status.equals("3")){
                txt3.setText("終場");
                txt4.setText("");
            }else{
                txt3.setText(mGamedata.currentQuarter);
                txt4.setText(mGamedata.playTime);
            }

            JSONObject jobject;
            try {
                jobject =  mGamedata.teamHomeRecord;
                txtHome.setText(jobject.getString("score"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                jobject =  mGamedata.teamHome;
                txtHomeName.setText(jobject.getString("name"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                jobject =  mGamedata.teamGuestRecord;
                txtGuest.setText(jobject.getString("score"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                jobject =  mGamedata.teamGuest;
                txtGuestName.setText(jobject.getString("name"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            HBLImageLoader.loadTeamImage(getApplicationContext(), mGamedata.teamHomeLogo, imgHome);

            HBLImageLoader.loadTeamImage(getApplicationContext(), mGamedata.teamGuestLogo, imgGuest);
        }

        mViewPager =  (ViewPager) findViewById(tw.org.ctssf.app.android.R.id.view_pager);
        mTabLayout = (TabLayout) findViewById(tw.org.ctssf.app.android.R.id.simpleTabLayout);

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
        View customActionBar = inflater.inflate( tw.org.ctssf.app.android.R.layout.activity_sigle_game_titlebar, null);
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
        tab1.setText(tw.org.ctssf.app.android.R.string.title_games);
        mTabLayout.addTab(tab1);

        TabLayout.Tab tab2 = mTabLayout.newTab();
        tab2.setText(tw.org.ctssf.app.android.R.string.game_data);
        mTabLayout.addTab(tab2);

        /*
        TabLayout.Tab tab3 = mTabLayout.newTab();
        tab3.setText(tw.org.ctssf.app.android.R.string.play_by_play);
        mTabLayout.addTab(tab3);
        */

        mTabLayout.setTabTextColors(ContextCompat.getColor(this, tw.org.ctssf.app.android.R.color.colorCustomTabTextUnSelected),ContextCompat.getColor(this, tw.org.ctssf.app.android.R.color.colorCustomTabTextSelected));
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
                (getSupportFragmentManager(), mTabLayout.getTabCount(), mHBLSingleGameData);
        mViewPager.setAdapter(adapter);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
    }

    class PagerAdapter extends FragmentStatePagerAdapter {
        int mNumOfTabs;
        HBLSingleGameData mData;

        public PagerAdapter(FragmentManager fm, int NumOfTabs, HBLSingleGameData gameData) {
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
                    fragment = SingleGameFragment.newInsTance();
            }
            Bundle args = new Bundle();
            args.putString("arg", mData.getDataInString());
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getCount() {
            return mNumOfTabs;
        }
    }

    @Override
    public void onBackPressed(){
        finish();
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
