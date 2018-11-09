package com.asana.hbl.fragment;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListPopupWindow;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.asana.hbl.R;
import com.asana.hbl.utils.HBLImageLoader;
import com.asana.hbl.utils.HttpClient;
import com.asana.hbl.utils.RestApi;
import com.asana.hbl.utils.Utils;
import com.asana.hbl.views.CircularImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created by Gary on 2018/10/1.
 */

public class AnalysisFragment extends Fragment {
    public static final String TAG = "HBL-AnalysisFragment";
    private  final String EVENT_NAME = "point";
    private ListPopupWindow mListPop, mListPop2, mListPop3;
    TextView mTv1, mTv2, mTv3;
    JSONArray mStageList, mGroupList;
    JSONArray mData;
    int mSelectedStage, mSelectedGroup;
    RelativeLayout mDropMenu, mDropMenu2, mDropMenu3;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private ImageView mDreoDownIndicator, mDreoDownIndicator2, mDreoDownIndicator3;
    public static AnalysisFragment newInsTance(){
        AnalysisFragment fragment = new AnalysisFragment();
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_analysis, container, false);
        initDropMenu(view);
        initTabLayout(view);
        initViewPager(view);

        return view;
    }

    private void initDropMenu(View view){
        //DropMenu1
        mDropMenu = (RelativeLayout)view.findViewById(R.id.drop_menu);
        mDreoDownIndicator = (ImageView) view.findViewById(R.id.iv1);
        mTv1 = (TextView)view.findViewById(R.id.tv1);


        //DropMenu2
        mDropMenu2 = (RelativeLayout)view.findViewById(R.id.drop_menu2);
        mDreoDownIndicator2 = (ImageView) view.findViewById(R.id.iv2);
        mTv2 = (TextView)view.findViewById(R.id.tv2);

        getStageList();

        //DropMenu3
        mDropMenu3 = (RelativeLayout)view.findViewById(R.id.drop_menu3);
        mDreoDownIndicator3 = (ImageView) view.findViewById(R.id.iv3);
        mTv3 = (TextView)view.findViewById(R.id.tv3);

        List<String > list3 = new ArrayList<String>();
        list3.add(getResources().getString(R.string.group));
        list3.add(getResources().getString(R.string.personal));
        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(getActivity(), R.layout.expandable_list_item, list3);
        mListPop3 = new ListPopupWindow(getActivity());
        mListPop3.setAdapter(adapter3);
        mListPop3.setWidth(Utils.measureContentWidth(getContext(), adapter3));
        mListPop3.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        mListPop3.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(getActivity(), android.R.color.white)));
        mListPop3.setAnchorView(mDropMenu3);
        mListPop3.setModal(true);
        mListPop3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mTv3.setText((String)parent.getAdapter().getItem(position));
                mListPop3.dismiss();
            }
        });

        mListPop3.setOnDismissListener(new PopupWindow.OnDismissListener(){
            @Override
            public void onDismiss() {
                mDreoDownIndicator3.animate()
                        .setDuration(Utils.ROTATION_ANIM_DURATION)
                        .rotationBy(180)
                        .start();
            }
        });


        mDropMenu3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDreoDownIndicator3.animate()
                        .setDuration(Utils.ROTATION_ANIM_DURATION)
                        .rotationBy(180)
                        .start();
                mListPop3.show();
            }
        });
        mTv3.setText((String)adapter3.getItem(0));

    }

    private void initTabLayout(View view){
        mTabLayout = (TabLayout) view.findViewById(R.id.simpleTabLayout);
        mTabLayout.setTabTextColors(Color.parseColor("#999999"),
                Color.parseColor("#A8272C"));
        TabLayout.Tab tab1 = mTabLayout.newTab();
        tab1.setText(getResources().getString(R.string.offensive_score));
        mTabLayout.addTab(tab1);

        TabLayout.Tab tab2 = mTabLayout.newTab();
        tab2.setText(getResources().getString(R.string.defensive_score));
        mTabLayout.addTab(tab2);

        TabLayout.Tab tab3 = mTabLayout.newTab();
        tab3.setText(getResources().getString(R.string.two_pointer));
        mTabLayout.addTab(tab3);

        TabLayout.Tab tab4 = mTabLayout.newTab();
        tab4.setText(getResources().getString(R.string.three_pointer));
        mTabLayout.addTab(tab4);
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
    }

    private void initViewPager(View view){
        mViewPager = (ViewPager)view.findViewById(R.id.view_pager);
        ListView listview1 = new ListView(getContext());
        ListView listview2 = new ListView(getContext());
        ListView listview3 = new ListView(getContext());
        ListView listview4 = new ListView(getContext());

        Vector<View> pages = new Vector<View>();

        pages.add(listview1);
        pages.add(listview2);
        pages.add(listview3);
        pages.add(listview4);

        CustomPagerAdapter adapter = new CustomPagerAdapter(getContext(), pages);
        mViewPager.setAdapter(adapter);

        listview1.setAdapter(new CusomListAdapter(getContext(), null));
        listview2.setAdapter(new CusomListAdapter(getContext(), null));
        listview3.setAdapter(new CusomListAdapter(getContext(), null));
        listview4.setAdapter(new CusomListAdapter(getContext(), null));
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
    }

    private class CustomPagerAdapter extends PagerAdapter {

        private Context mContext;
        private Vector<View> pages;

        public CustomPagerAdapter(Context context, Vector<View> pages) {
            this.mContext=context;
            this.pages=pages;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View page = pages.get(position);
            container.addView(page);
            return page;
        }

        @Override
        public int getCount() {
            return pages.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view.equals(object);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

    }

    public class CusomListAdapter extends BaseAdapter {
        private LayoutInflater mInflater;
        private List<JSONObject> mListData= new ArrayList<JSONObject>();
        private class ViewHolder {
            TextView tv_team_name,tv2, tv3, tv4;
            CircularImageView iv_team;
            public ViewHolder(TextView txt1, TextView txt2, TextView txt3, TextView txt4, CircularImageView iv){
                this.tv_team_name = txt1;
                this.tv2 = txt2;
                this.tv3 = txt3;
                this.tv4 = txt4;
                this.iv_team = iv;
            }
        }

        public CusomListAdapter(Context context, JSONArray data){
            //initDataList(data);
            mInflater = LayoutInflater.from(context);
        }

        private void initDataList(JSONArray data){
            for(int i=0; i<data.length(); i++){
                try {
                    JSONObject jsonObject = (JSONObject)data.get(i);
                    mListData.add((JSONObject)data.get(i));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public int getCount() {
            return 10;
            //return mListData.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
            //return mListData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if(convertView==null){
                convertView = mInflater.inflate(R.layout.analysis_list_item, null);
                holder = new ViewHolder(
                        (TextView) convertView.findViewById(R.id.tv1),
                        (TextView) convertView.findViewById(R.id.tv2),
                        (TextView) convertView.findViewById(R.id.tv3),
                        (TextView) convertView.findViewById(R.id.tv4),
                        (CircularImageView) convertView.findViewById(R.id.iv)
                );
                convertView.setTag(holder);
            }else{
                holder = (ViewHolder) convertView.getTag();
            }
            HBLImageLoader.loadTeamImage(getActivity().getApplicationContext(), "", holder.iv_team);
            /*
            try {
                holder.tv_team_name.setText(mListData.get(position).getString("team_name"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                holder.tv_win.setText(mListData.get(position).getString("win_count"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                holder.tv_lose.setText(mListData.get(position).getString("loss_count"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                holder.tv_raking.setText(mListData.get(position).getString("rank"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                mImageLoader.get(mListData.get(position).getString("logo"), ImageLoader.getImageListener(holder.iv_team,
                        R.mipmap.team_default, R.mipmap.team_default));
                holder.iv_team.setImageUrl(mListData.get(position).getString("logo"), mImageLoader);
            } catch (JSONException e) {
                e.printStackTrace();
            }
           */
            return convertView;
        }
    }

    private void getStageList(){
        HttpClient httpClient = new HttpClient();
        HttpClient.HttpResponseCallback callback = new HttpClient.HttpResponseCallback() {
            @Override
            public void onResponse(Bundle result) {
                try {
                    mStageList = new JSONArray(result.getString("response"));
                    if(getActivity() != null) {
                        List<String > list1 = new ArrayList<String>();
                        for(int i=0; i<mStageList.length(); i++){
                            try {
                                JSONObject jsonObject = (JSONObject)mStageList.get(i);
                                list1.add(jsonObject.getString("name"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.expandable_list_item, list1);
                        mListPop = new ListPopupWindow(getActivity());
                        mListPop.setAdapter(adapter);
                        mListPop.setWidth(Utils.measureContentWidth(getContext(), adapter));
                        mListPop.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
                        mListPop.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(getActivity(), android.R.color.white)));
                        mListPop.setAnchorView(mDropMenu);
                        mListPop.setModal(true);
                        mListPop.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                mTv1.setText((String)parent.getAdapter().getItem(position));
                                mSelectedStage = position;
                                String selectedGender;
                                try {
                                    JSONObject jsonObject = (JSONObject)mStageList.get(position);
                                    getGroupListByStage(jsonObject.getString("sn"));
                                    selectedGender = jsonObject.getString("gender");
                                    Log.v("Gary", "selectedSN: " + jsonObject.getString("sn"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
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


                        mDropMenu.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mDreoDownIndicator.animate()
                                        .setDuration(Utils.ROTATION_ANIM_DURATION)
                                        .rotationBy(180)
                                        .start();
                                mListPop.show();
                            }
                        });
                        mTv1.setText((String)adapter.getItem(0));
                        JSONObject jsonObject = (JSONObject)mStageList.get(0);
                        getGroupListByStage(jsonObject.getString("sn"));
                        mSelectedStage = 0;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };
        httpClient.async_query_GET(RestApi.getStageList(RestApi.SEASON_SN), null, callback);
    }

    private void getGroupListByStage(String stageSn){
        HttpClient httpClient = new HttpClient();
        HttpClient.HttpResponseCallback callback = new HttpClient.HttpResponseCallback() {
            @Override
            public void onResponse(Bundle result) {
                try {
                    mGroupList = new JSONArray(result.getString("response"));
                    if(getActivity() != null) {
                        List<String > list2 = new ArrayList<String>();
                        for(int i=0; i<mGroupList.length(); i++){
                            try {
                                JSONObject jsonObject = (JSONObject)mGroupList.get(i);
                                list2.add(jsonObject.getString("name"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.expandable_list_item, list2);
                        mListPop2 = new ListPopupWindow(getActivity());
                        mListPop2.setAdapter(adapter);
                        mListPop2.setWidth(Utils.measureContentWidth(getContext(), adapter));
                        mListPop2.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
                        mListPop2.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(getActivity(), android.R.color.white)));
                        mListPop2.setAnchorView(mDropMenu2);
                        mListPop2.setModal(true);
                        mListPop2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                mTv2.setText((String)parent.getAdapter().getItem(position));
                                mSelectedGroup = position;
                                mListPop2.dismiss();
                            }
                        });

                        mListPop2.setOnDismissListener(new PopupWindow.OnDismissListener(){
                            @Override
                            public void onDismiss() {
                                mDreoDownIndicator2.animate()
                                        .setDuration(Utils.ROTATION_ANIM_DURATION)
                                        .rotationBy(180)
                                        .start();
                            }
                        });


                        mDropMenu2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mDreoDownIndicator2.animate()
                                        .setDuration(Utils.ROTATION_ANIM_DURATION)
                                        .rotationBy(180)
                                        .start();
                                mListPop2.show();
                            }
                        });
                        if(adapter.isEmpty()){
                            mTv2.setText(" ");
                            mSelectedGroup = -1;
                        }else {
                            mTv2.setText((String) adapter.getItem(0));
                            mSelectedGroup = 0;
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };
        httpClient.async_query_GET(RestApi.getGroupListByStage(stageSn), null, callback);
    }


    private void getDataList(String stageSn, String groupSn, int returnCount){
        String type = mTv3.getText().toString();
        HttpClient httpClient = new HttpClient();
        HttpClient.HttpResponseCallback callback = new HttpClient.HttpResponseCallback() {
            @Override
            public void onResponse(Bundle result) {
                try {
                    mData = new JSONArray(result.getString("response"));
                    if(getActivity() != null) {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        if(type != null && type.equals(getResources().getString(R.string.group))){
            httpClient.async_query_GET(RestApi.getTeamAverageListByStageAndGroup(stageSn, groupSn, EVENT_NAME, returnCount), null, callback);
        }else if(type != null && type.equals(getResources().getString(R.string.personal))){
            httpClient.async_query_GET(RestApi.getRosterAverageListByStageAndGroup(stageSn, groupSn, EVENT_NAME, returnCount), null, callback);
        }

    }
}
