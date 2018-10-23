package com.asana.hbl.fragment;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListPopupWindow;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.asana.hbl.R;
import com.asana.hbl.utils.HttpClient;
import com.asana.hbl.utils.LoadPerferenceDataClient;
import com.asana.hbl.utils.RestApi;
import com.asana.hbl.utils.TestData;
import com.asana.hbl.utils.Utils;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Gary on 2018/10/1.
 */
public class GameFragment extends Fragment {
    public static final String TAG = "HBL-GameFragment";
    public static final String EXTRA_TEXT = "arg";
    private RelativeLayout mGamesSpinner;
    private TextView mTitle;
    private ListPopupWindow mListPop;
    private List<String > mList = new ArrayList<String>();
    private HashMap<String, String> mStageListData = new HashMap<String, String>();
    private List<JSONArray > mDataList = new ArrayList<JSONArray>();
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private ImageView mDreoDownIndicator;
    public static GameFragment newInsTance(){
        GameFragment fragment = new GameFragment();
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game, container, false);
        mGamesSpinner = (RelativeLayout)view.findViewById(R.id.spinner_games);
        mTitle = (TextView)view.findViewById(R.id.titles);
        mDreoDownIndicator = (ImageView)view.findViewById(R.id.indicator);
        mList.add(getResources().getString(R.string.all_games));

        mListPop = new ListPopupWindow(getActivity());
        mListPop.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.expandable_list_item, mList));
        mListPop.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        mListPop.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        mListPop.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(getActivity(), android.R.color.white)));
        mListPop.setAnchorView(mTitle);
        mListPop.setModal(true);
        mListPop.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mTitle.setText(mList.get(position));
                //mDataList.clear();
                //mTabLayout.removeAllTabs();
               //mViewPager.setAdapter(null);
                if(position == 0){
                    getAllGames(RestApi.SEASON_SN);
                }else{
                    String sn = mStageListData.get(mList.get(position));
                    getGameListBystage(sn);
                }

                mListPop.dismiss();
            }
        });

        mListPop.setOnDismissListener(new PopupWindow.OnDismissListener(){
            @Override
            public void onDismiss() {
                mDreoDownIndicator.animate()
                        .setDuration(Utils.ROTATION_ANIM_DURATION)
                        .rotationBy(180)
                        .start();
            }
        });

        getStageListbySn(RestApi.SEASON_SN);
        mGamesSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDreoDownIndicator.animate()
                        .setDuration(Utils.ROTATION_ANIM_DURATION)
                        .rotationBy(180).start();
                mListPop.show();
            }
        });
        mViewPager =  (ViewPager) view.findViewById(R.id.view_pager);
        mTabLayout = (TabLayout) view.findViewById(R.id.simpleTabLayout);
        mTitle.setText(mList.get(0));
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        getAllGames(RestApi.SEASON_SN);
    }

    private void getAllGames(String seasenSn){
        LoadPerferenceDataClient perferenceClient = new LoadPerferenceDataClient();
        LoadPerferenceDataClient.PerferenceDataTaskCallback pCallback= new LoadPerferenceDataClient.PerferenceDataTaskCallback(){
            @Override
            public void onResponse(String result) {
                if (result != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        initViewPager(jsonObject);
                        mViewPager.setCurrentItem((int)mTabLayout.getTag());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else{
                    if(TestData.useTestData){
                        try{
                            JSONObject jsonObject = new JSONObject(TestData.TEST_DATA_1);
                            initViewPager(jsonObject);
                            mViewPager.setCurrentItem((int)mTabLayout.getTag());
                        } catch(JSONException e) {
                            e.printStackTrace();
                        }
                    }else{
                        HttpClient httpClient = new HttpClient();
                        HttpClient.HttpResponseCallback callback = new HttpClient.HttpResponseCallback() {
                            @Override
                            public void onResponse(Bundle result) {
                                try {
                                    JSONObject jsonObject = null;
                                    jsonObject = new JSONObject(result.getString("response"));
                                    if(getActivity() != null) {
                                        initViewPager(jsonObject);
                                        mViewPager.setCurrentItem((int)mTabLayout.getTag());
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        };
                        if(TestData.useTestData){
                            try{
                                JSONObject jsonObject = new JSONObject(TestData.TEST_DATA_1);
                                initViewPager(jsonObject);
                            } catch(JSONException e) {
                                e.printStackTrace();
                            }
                        }else{
                            httpClient.async_query_GET(RestApi.getGameList(RestApi.SEASON_SN), null, callback);
                        }
                    }
                }
            }
        };
        perferenceClient.GetSharedPreferences(getActivity(), "game_data", getActivity().MODE_PRIVATE, pCallback);
    }

    private void getStageListbySn(String sn){
        HttpClient httpClient = new HttpClient();
        HttpClient.HttpResponseCallback callback = new HttpClient.HttpResponseCallback() {
            @Override
            public void onResponse(Bundle result) {
                try {
                    JSONArray jsonArray = null;
                    jsonArray = new JSONArray(result.getString("response"));
                    if(getActivity() != null && jsonArray != null) {
                         for(int i=0; i<jsonArray.length(); i++){
                             JSONObject jb = (JSONObject)jsonArray.get(i);
                             mList.add(jb.getString("name"));
                             mStageListData.put(jb.getString("name"), jb.getString("sn"));
                         }
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.expandable_list_item, mList);
                        mListPop.setAdapter(adapter);
                        mListPop.setWidth(Utils.measureContentWidth(getContext(), adapter));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };
        httpClient.async_query_GET(RestApi.getGameStageList(sn), null, callback);
    }

    private void getGameListBystage(String stageSN){
        HttpClient httpClient = new HttpClient();
        HttpClient.HttpResponseCallback callback = new HttpClient.HttpResponseCallback() {
            @Override
            public void onResponse(Bundle result) {
                try {
                    JSONObject jsonObject = null;
                    jsonObject = new JSONObject(result.getString("response"));
                    if(getActivity() != null && jsonObject != null) {
                        initViewPager(jsonObject);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };
        httpClient.async_query_GET(RestApi.getGameListByStage(stageSN), null, callback);
    }

    private void initViewPager(JSONObject jsonObject){
        Iterator<String> keys =  jsonObject.keys();
        mDataList.clear();
        mTabLayout.removeAllTabs();
        mViewPager.setAdapter(null);
        long currentTime= System.currentTimeMillis();
        int nearestGame = 0;
        long  timeDiff = 0;
        int i = 0;
        while(keys.hasNext()){
            String key = keys.next();
            TabLayout.Tab tab = mTabLayout.newTab();
            SimpleDateFormat formater = new SimpleDateFormat("MM月dd日 E");
            //SimpleDateFormat formater = new SimpleDateFormat("yyyyMMdd hh:mm:ss");
            tab.setText(formater.format(new Date(Long.parseLong(key))).toString());
            mTabLayout.addTab(tab);
            if(i == 0){
                nearestGame = i;
                timeDiff = Long.parseLong(key) - currentTime;
            }else{
                if(timeDiff<0 && (Long.parseLong(key) - currentTime) > timeDiff){
                    nearestGame = i;
                    timeDiff = Long.parseLong(key) - currentTime;
                }else if(timeDiff>0 && (Long.parseLong(key) - currentTime) < timeDiff){
                    nearestGame = i;
                    timeDiff = Long.parseLong(key) - currentTime;
                }

            }
            i++;
            JSONArray jArray;
            try {
                jArray = (JSONArray)jsonObject.get(key);
                mDataList.add(jArray);
                /*
                for(int i=0; i<jArray.length(); i++){
                    HblGames gameInfo = new HblGames((JSONObject)jArray.get(i));
                    mDataList.add(gameInfo);
                    //Log.v("Gary", "jsonObject : " + (JSONObject)jArray.get(i));jArray
                }*/
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        mTabLayout.setTag(nearestGame);
        mTabLayout.setTabTextColors(ContextCompat.getColor(getActivity(), R.color.colorCustomTabTextUnSelected),ContextCompat.getColor(getActivity(), R.color.colorCustomTabTextSelected));
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
                (getActivity().getSupportFragmentManager(), mTabLayout.getTabCount(), mDataList);
        mViewPager.setAdapter(adapter);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
    }

    class PagerAdapter extends FragmentStatePagerAdapter {
        int mNumOfTabs;
        List<JSONArray > mData;
        public PagerAdapter(FragmentManager fm, int NumOfTabs, List<JSONArray > dataList) {
            super(fm);
            this.mNumOfTabs = NumOfTabs;
            this.mData = dataList;
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = GameListFragment.newInsTance();
            Bundle args = new Bundle();
            args.putString("arg", mDataList.get(position).toString());
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getCount() {
            return mNumOfTabs;
        }
    }
}

