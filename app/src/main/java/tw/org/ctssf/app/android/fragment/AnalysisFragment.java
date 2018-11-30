package tw.org.ctssf.app.android.fragment;

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

import tw.org.ctssf.app.android.R;
import tw.org.ctssf.app.android.utils.HBLImageLoader;
import tw.org.ctssf.app.android.utils.HttpClient;
import tw.org.ctssf.app.android.utils.RestApi;
import tw.org.ctssf.app.android.utils.Utils;
import tw.org.ctssf.app.android.views.CircularImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Vector;

/**
 * Created by Gary on 2018/10/1.
 */

public class AnalysisFragment extends Fragment {
    public static final String TAG = "HBL-AnalysisFragment";
    private  final String EVENT_NAME = "points";
    private ListPopupWindow mListPop, mListPop2, mListPop3;
    TextView mTv1, mTv2, mTv3;
    TextView mItemTitle1, mItemTitle2, mItemTitle3, mItemTitle4, mItemTitle5;
    JSONArray mStageList, mGroupList;
    JSONArray mData;
    int mSelectedStage, mSelectedGroup;
    RelativeLayout mDropMenu, mDropMenu2, mDropMenu3;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private ImageView mDreoDownIndicator, mDreoDownIndicator2, mDreoDownIndicator3;

    LinkedHashMap<String, String> mGroupItenmList = new LinkedHashMap<String, String>(){
        {
            put("進攻得分", "PPG");
            put("防守失分", "OPPG");
            put("兩分球", "2P%");
            put("三分球", "3P%");
            put("罰球", "FT%");
            put("進攻籃板", "OREB");
            put("防守籃板", "DREB");
            put("總籃板", "REB");
            put("助攻", "AST");
            put("阻攻", "BLK");
            put("抄截", "STL");
            put("失誤", "TOV");
            put("犯規", "PF");
        }
    };

    LinkedHashMap<String, String> mRosterItenmList = new LinkedHashMap<String, String>(){
        {
            put("場均得分", "PPG");
            put("場均籃板", "RPG");
            put("場均助攻", "APG");
            put("場均抄截", "SPG");
            put("場均阻攻", "BPG");
        }
    };

    public static AnalysisFragment newInsTance(){
        AnalysisFragment fragment = new AnalysisFragment();
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        Log.v(TAG, "onCreateView");
        View view = inflater.inflate(tw.org.ctssf.app.android.R.layout.fragment_analysis, container, false);
        initView(view);
        return view;
    }

    private void initView(View view){
        mTabLayout = (TabLayout) view.findViewById(tw.org.ctssf.app.android.R.id.simpleTabLayout);
        mViewPager = (ViewPager)view.findViewById(tw.org.ctssf.app.android.R.id.view_pager);
        mItemTitle1 = (TextView)view.findViewById(tw.org.ctssf.app.android.R.id.item_title1);
        mItemTitle2 = (TextView)view.findViewById(tw.org.ctssf.app.android.R.id.item_title2);
        mItemTitle3 = (TextView)view.findViewById(tw.org.ctssf.app.android.R.id.item_title3);
        mItemTitle4 = (TextView)view.findViewById(tw.org.ctssf.app.android.R.id.item_title4);
        mItemTitle5  = (TextView)view.findViewById(tw.org.ctssf.app.android.R.id.item_title5);

        //DropMenu1
        mDropMenu = (RelativeLayout)view.findViewById(tw.org.ctssf.app.android.R.id.drop_menu);
        mDreoDownIndicator = (ImageView) view.findViewById(tw.org.ctssf.app.android.R.id.iv1);
        mTv1 = (TextView)view.findViewById(tw.org.ctssf.app.android.R.id.tv1);


        //DropMenu2
        mDropMenu2 = (RelativeLayout)view.findViewById(tw.org.ctssf.app.android.R.id.drop_menu2);
        mDreoDownIndicator2 = (ImageView) view.findViewById(tw.org.ctssf.app.android.R.id.iv2);
        mTv2 = (TextView)view.findViewById(tw.org.ctssf.app.android.R.id.tv2);

        //DropMenu3
        mDropMenu3 = (RelativeLayout)view.findViewById(tw.org.ctssf.app.android.R.id.drop_menu3);
        mDreoDownIndicator3 = (ImageView) view.findViewById(tw.org.ctssf.app.android.R.id.iv3);
        mTv3 = (TextView)view.findViewById(tw.org.ctssf.app.android.R.id.tv3);

        initDropMenu();
        initTabLayout();
    }

    private void initDropMenu(){
        getStageList();
        List<String > list3 = new ArrayList<String>();
        list3.add(getResources().getString(tw.org.ctssf.app.android.R.string.group));
        list3.add(getResources().getString(tw.org.ctssf.app.android.R.string.personal));
        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(getActivity(), tw.org.ctssf.app.android.R.layout.expandable_list_item, list3);
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
                getDataList(mSelectedStage, mSelectedGroup, 10);
                initTabLayout();

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

    private void initTabLayout(){
        mTabLayout.removeAllTabs();
        String type = mTv3.getText().toString();
        mTabLayout.setTabTextColors(Color.parseColor("#999999"),
                Color.parseColor("#A8272C"));
        List<String> list = new ArrayList<String>();

        if(isRoster()){
            for ( String key : mRosterItenmList.keySet() ) {
                list.add(key);
            }
            mItemTitle1.setText("姓名");
            mItemTitle3.setVisibility(View.VISIBLE);
        }else{
            for ( String key : mGroupItenmList.keySet() ) {
                list.add(key);
            }
            mItemTitle1.setText("隊伍");
            mItemTitle3.setVisibility(View.GONE);
        }
        for(String item : list){
            TabLayout.Tab tab = mTabLayout.newTab();
            tab.setText(item);
            mTabLayout.addTab(tab);
        }
        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
                if(isRoster()){
                    mItemTitle4.setText(mRosterItenmList.get(tab.getText().toString()));
                }else{
                    mItemTitle4.setText(mGroupItenmList.get(tab.getText().toString()));
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        initViewPager();
    }

    private void initViewPager(){
        Vector<View> pages = new Vector<View>();
        if(isRoster()){
            for ( String key : mRosterItenmList.keySet() ) {
                ListView listview = new ListView(getContext());
                listview.setAdapter(new CusomListAdapter(getContext(), null, mRosterItenmList.get(key)));
                pages.add(listview);
            }
        }else{
            for ( String key : mGroupItenmList.keySet() ) {
                ListView listview = new ListView(getContext());
                listview.setAdapter(new CusomListAdapter(getContext(), null, mGroupItenmList.get(key)));
                pages.add(listview);
            }
        }

        CustomPagerAdapter adapter = new CustomPagerAdapter(getContext(), pages);
        mViewPager.setAdapter(adapter);
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
        private String mDataID;


        private final LinkedHashMap<String, String> mGroupDataMap = new LinkedHashMap<String, String>(){
            {
                put("PPG", "points");
                put("OPPG", "points");
                put("2P%", "two_pct");
                put("3P%", "trey_pct");
                put("FT%", "ft_pct");
                put("OREB", "reb_o");
                put("DREB", "reb_d");
                put("REB", "reb");
                put("AST", "ast");
                put("BLK", "blk");
                put("STL", "stl");
                put("TOV", "turnover");
                put("PF", "pfoul");
            }
        };

        LinkedHashMap<String, String> mRosterDataMap = new LinkedHashMap<String, String>(){
            {
                put("PPG", "points");
                put("RPG", "reb");
                put("APG", "ast");
                put("SPG", "stl");
                put( "BPG", "blk");
            }
        };


        private class ViewHolder {
            TextView tv_11, tv_team_name,tv2, tv3, tv4, tv5;
            CircularImageView iv_team;
            public ViewHolder(TextView txt1, TextView txt1_1, TextView txt2, TextView txt3, TextView txt4, TextView txt5, CircularImageView iv){
                this.tv_team_name = txt1;
                this.tv_11 = txt1_1;
                this.tv2 = txt2;
                this.tv3 = txt3;
                this.tv4 = txt4;
                this.tv5 = txt5;
                this.iv_team = iv;
            }
        }

        public CusomListAdapter(Context context, JSONArray data, String dataID){
            this.mDataID = dataID;
            initDataList(data);
            mInflater = LayoutInflater.from(context);
        }

        public void updateDataList(JSONArray data){
            initDataList(data);
            notifyDataSetChanged();
        }

        private void initDataList(JSONArray data){
            if(data == null) return;
            mListData.clear();
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
            return mListData.size();
        }

        @Override
        public Object getItem(int position) {
            return mListData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if(convertView==null){
                convertView = mInflater.inflate(tw.org.ctssf.app.android.R.layout.analysis_list_item, null);
                holder = new ViewHolder(
                        (TextView) convertView.findViewById(tw.org.ctssf.app.android.R.id.tv1),
                        (TextView) convertView.findViewById(tw.org.ctssf.app.android.R.id.tv1_1),
                        (TextView) convertView.findViewById(tw.org.ctssf.app.android.R.id.tv2),
                        (TextView) convertView.findViewById(tw.org.ctssf.app.android.R.id.tv3),
                        (TextView) convertView.findViewById(tw.org.ctssf.app.android.R.id.tv4),
                        (TextView) convertView.findViewById(tw.org.ctssf.app.android.R.id.tv5),
                        (CircularImageView) convertView.findViewById(tw.org.ctssf.app.android.R.id.iv)
                );
                convertView.setTag(holder);
            }else{
                holder = (ViewHolder) convertView.getTag();
            }

            if(isRoster()){
                HBLImageLoader.loadPlayerImage(getActivity().getApplicationContext(), "", holder.iv_team);

                try {
                    holder.tv_team_name.setText(mListData.get(position).getString("name_alt"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                holder.tv_11.setVisibility(View.VISIBLE);
                try {
                    holder.tv_11.setText(mListData.get(position).getString("team_name_alt"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else{
                HBLImageLoader.loadTeamImage(getActivity().getApplicationContext(), "", holder.iv_team);

                try {
                    holder.tv_team_name.setText(mListData.get(position).getString("team_name_alt"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                holder.tv_11.setVisibility(View.GONE);
            }


            String gp;
            try {
                gp = mListData.get(position).getString("gp");
                holder.tv2.setText(gp);
            } catch (JSONException e) {
                e.printStackTrace();
                gp = null;
            }
            if(isRoster()) {
                String second;
                try {
                    second = mListData.get(position).getString("seconds");
                    int i_second = (int)((Float.valueOf(second)) /(Integer.valueOf(gp)) % 60);
                    int i_munite = (int)((Float.valueOf(second)) /(Integer.valueOf(gp)) / 60);
                    String MPG = String.valueOf(i_munite) + "\"" + String.valueOf(i_second);
                    holder.tv3.setText(MPG);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else{
                holder.tv3.setVisibility(View.GONE);
            }

            try {
                String texToShow = "";
                if(isRoster()) {
                    texToShow = mListData.get(position).getString((mRosterDataMap.get(mDataID)));
                    if(texToShow.length() > 5){
                        texToShow = texToShow.substring(0, 4);
                    }
                    holder.tv4.setText(texToShow);

                }else{
                    texToShow = mListData.get(position).getString((mGroupDataMap.get(mDataID)));
                    if(texToShow.length() > 4){
                        texToShow = texToShow.substring(0, 4);
                    }
                    holder.tv4.setText(texToShow);
                    holder.tv4.setText(texToShow);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            holder.tv5.setText("" + (position + 1));

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
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), tw.org.ctssf.app.android.R.layout.expandable_list_item, list1);
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
                                try {
                                    JSONObject jsonObject = (JSONObject)mStageList.get(position);
                                    getGroupListByStage(jsonObject.getString("sn"));
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
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), tw.org.ctssf.app.android.R.layout.expandable_list_item, list2);
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
                                getDataList(mSelectedStage, mSelectedGroup, 10);
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
                        getDataList(mSelectedStage, mSelectedGroup, 10);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };
        httpClient.async_query_GET(RestApi.getGroupListByStage(stageSn), null, callback);
    }


    private void getDataList(int selectedStage, int seletedGroup, int returnCount){
        if(selectedStage < 0 || seletedGroup < 0 || mStageList == null || mGroupList == null){
            return;
        }

        String stageSn = null;
        String groupSn = null;

        JSONObject jsonObject = null;
        try {
            jsonObject = (JSONObject)mStageList.get(selectedStage);
            stageSn = jsonObject.getString("sn");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            jsonObject = (JSONObject)mGroupList.get(seletedGroup);
            groupSn = jsonObject.getString("sn");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(stageSn == null || groupSn == null) return;

        HttpClient httpClient = new HttpClient();
        HttpClient.HttpResponseCallback callback = new HttpClient.HttpResponseCallback() {
            @Override
            public void onResponse(Bundle result) {
                try {
                    mData = new JSONArray(result.getString("response"));
                    if(getActivity() != null) {
                        Vector<View> pages = ((CustomPagerAdapter)mViewPager.getAdapter()).pages;
                        for(View view : pages){
                            CusomListAdapter adapter = (CusomListAdapter)((ListView)view).getAdapter();
                            adapter.updateDataList(mData);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        if(isRoster()){
            Log.v("Gary", "get Roster URI: " + RestApi.getRosterAverageListByStageAndGroup(stageSn, groupSn, EVENT_NAME, returnCount));
            httpClient.async_query_GET(RestApi.getRosterAverageListByStageAndGroup(stageSn, groupSn, EVENT_NAME, returnCount), null, callback);
        }else{
            Log.v("Gary", "get Team URI: " + RestApi.getTeamAverageListByStageAndGroup(stageSn, groupSn, EVENT_NAME, returnCount));
            httpClient.async_query_GET(RestApi.getTeamAverageListByStageAndGroup(stageSn, groupSn, EVENT_NAME, returnCount), null, callback);
        }

    }

    private boolean isRoster(){
        boolean result = true;
        String type = mTv3.getText().toString();
        if(type != null && type.equals(getResources().getString(tw.org.ctssf.app.android.R.string.group))){
            result = false;
        }else if(type != null && type.equals(getResources().getString(tw.org.ctssf.app.android.R.string.personal))){
            result = true;
        }
        return result;
    }
}
