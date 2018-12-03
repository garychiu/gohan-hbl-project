package tw.org.ctssf.app.android.fragment;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
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
import java.util.HashMap;
import java.util.List;
/**
 * Created by Gary on 2018/10/1.
 */

public class DashboardFragment extends Fragment {
    public static final String TAG = "HBL-DashboardFragment";
    ListView mListView;
    JSONArray mData;
    JSONArray mStageList, mGroupList;
    RelativeLayout mDropMenu1, mDropMenu2, mDropMenu3;
    private ListPopupWindow mListPop1, mListPop2, mListPop3;
    TextView mTv1, mTv2, mTv3;
    int mSelectedStage, mSelectedGroup;
    private ImageView mDreoDownIndicator1, mDreoDownIndicator2, mDreoDownIndicator3;
    private List<String > mListGender = new ArrayList<String>();
    HashMap<String, String> mListGenderF = new HashMap<String, String>();
    HashMap<String, String> mListGenderM = new HashMap<String, String>();
    public static DashboardFragment newInsTance(){
        DashboardFragment fragment = new DashboardFragment();
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        Log.v(TAG, "onCreateView");
        View view = inflater.inflate(tw.org.ctssf.app.android.R.layout.fragment_dashboard, container, false);
        mDropMenu1 = (RelativeLayout)view.findViewById(tw.org.ctssf.app.android.R.id.drop_menu1);
        mDropMenu2 = (RelativeLayout)view.findViewById(tw.org.ctssf.app.android.R.id.drop_menu2);
        mDropMenu3 = (RelativeLayout)view.findViewById(tw.org.ctssf.app.android.R.id.drop_menu3);
        mListView = (ListView)view.findViewById(tw.org.ctssf.app.android.R.id.lv);

        mDreoDownIndicator1 = (ImageView) view.findViewById(tw.org.ctssf.app.android.R.id.iv1);
        mDreoDownIndicator2 = (ImageView) view.findViewById(tw.org.ctssf.app.android.R.id.iv2);
        mDreoDownIndicator3 = (ImageView) view.findViewById(tw.org.ctssf.app.android.R.id.iv3);
        mTv1 = (TextView)view.findViewById(tw.org.ctssf.app.android.R.id.tv1);
        mTv2 = (TextView)view.findViewById(tw.org.ctssf.app.android.R.id.tv2);
        mTv3 = (TextView)view.findViewById(tw.org.ctssf.app.android.R.id.tv3);

        mListGender.add(getResources().getString(tw.org.ctssf.app.android.R.string.team_man));
        mListGender.add(getResources().getString(tw.org.ctssf.app.android.R.string.team_woman));

        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getActivity(), tw.org.ctssf.app.android.R.layout.expandable_list_item, mListGender);
        mListPop1 = new ListPopupWindow(getActivity());
        mListPop1.setAdapter(adapter1);
        mListPop1.setWidth(Utils.measureContentWidth(getContext(), adapter1));
        mListPop1.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

        mListPop1.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(getActivity(), android.R.color.white)));
        mListPop1.setAnchorView(mDropMenu1);
        mListPop1.setModal(true);
        mListPop1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mTv1.setText(mListGender.get(position));
                if(mListGender.get(position).equals(getResources().getString(tw.org.ctssf.app.android.R.string.team_man))){
                    updateStageList(mListGenderM);
                }else if(mListGender.get(position).equals(getResources().getString(tw.org.ctssf.app.android.R.string.team_woman))){
                    updateStageList(mListGenderF);
                }
                mListPop1.dismiss();
            }
        });
        mListPop1.setOnDismissListener(new PopupWindow.OnDismissListener(){
            @Override
            public void onDismiss() {
                mDreoDownIndicator1.animate()
                        .setDuration(Utils.ROTATION_ANIM_DURATION)
                        .rotationBy(180)
                        .start();
            }
        });

        mDropMenu1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDreoDownIndicator1.animate()
                        .setDuration(Utils.ROTATION_ANIM_DURATION)
                        .rotationBy(180)
                        .start();
                mListPop1.show();
            }
        });


        mTv1.setText(mListGender.get(0));
        getStageList();
        return view;
    }

    private void getDataList(String stageSN, final String division){
        HttpClient httpClient = new HttpClient();
        HttpClient.HttpResponseCallback callback = new HttpClient.HttpResponseCallback() {
            @Override
            public void onResponse(Bundle result) {
                try {
                    mData =  new JSONArray();
                    JSONArray data = new JSONArray(result.getString("response"));
                    for(int i=0; i< data.length(); i++){
                        try {
                            JSONObject jsonObject = (JSONObject)data.get(i);
                            if(jsonObject.getString("division").equals(division)){
                                mData.put(jsonObject);
                            };
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    if(getActivity() != null) {
                        initListView(mData);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };
        httpClient.async_query_GET(RestApi.getTeamListByStage(stageSN), null, callback);
    }

    private void initListView(JSONArray data){
        MyAdapter adapter =  new MyAdapter(getActivity(), data);
        mListView.setAdapter(adapter);
    }


    public class MyAdapter extends BaseAdapter {
        private LayoutInflater mInflater;
        private List<JSONObject> mListData= new ArrayList<JSONObject>();
        private class ViewHolder {
            TextView tv_team_name,tv_win, tv_lose, tv_raking;
            CircularImageView iv_team;
            public ViewHolder(TextView txt1, TextView txt2, TextView txt3, TextView txt4, CircularImageView iv){
                this.tv_team_name = txt1;
                this.tv_win = txt2;
                this.tv_lose = txt3;
                this.tv_raking = txt4;
                this.iv_team = iv;
            }
        }
        public MyAdapter(Context context, JSONArray data){
            initDataList(data);
            mInflater = LayoutInflater.from(context);
        }

        private void initDataList(JSONArray data){
            JSONArray ja = data;
            int rank = 1;
            int errorCount = 0;
            do{
                if(errorCount >= ja.length()){
                    break;
                }
                for(int i=0; i<ja.length(); i++){
                    try {
                        JSONObject jsonObject = (JSONObject)data.get(i);
                        if(Integer.valueOf(jsonObject.getString("rank")) == rank){
                            mListData.add(jsonObject);
                            rank ++;
                        }
                    } catch (Exception e) {
                        errorCount ++;
                        e.printStackTrace();
                    }

                }

            }while(rank < ja.length());
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
                convertView = mInflater.inflate(tw.org.ctssf.app.android.R.layout.dashboard_list_item, null);
                holder = new ViewHolder(
                        (TextView) convertView.findViewById(tw.org.ctssf.app.android.R.id.tv_team_name),
                        (TextView) convertView.findViewById(tw.org.ctssf.app.android.R.id.tv_win),
                        (TextView) convertView.findViewById(tw.org.ctssf.app.android.R.id.tv_lose),
                        (TextView) convertView.findViewById(tw.org.ctssf.app.android.R.id.tv_raking),
                        (CircularImageView) convertView.findViewById(tw.org.ctssf.app.android.R.id.iv_team)
                );
                convertView.setTag(holder);
            }else{
                holder = (ViewHolder) convertView.getTag();
            }

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
                HBLImageLoader.loadTeamImage(getActivity().getApplicationContext(), mListData.get(position).getString("logo"), holder.iv_team);
            } catch (JSONException e) {
                e.printStackTrace();
            }

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
                        for(int i=0; i<mStageList.length(); i++){
                            try {
                                JSONObject jsonObject = (JSONObject)mStageList.get(i);
                                String gender = jsonObject.getString("gender");
                                if(gender.equals("M")){
                                    mListGenderM.put(jsonObject.getString("name").split("-")[0], jsonObject.getString("sn"));
                                }else if(gender.equals("F")) {
                                    mListGenderF.put(jsonObject.getString("name").split("-")[0], jsonObject.getString("sn"));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        if(mTv1.getText().toString().equals(getResources().getString(tw.org.ctssf.app.android.R.string.team_man))){
                            updateStageList(mListGenderM);
                        }else if(mTv1.getText().toString().equals(getResources().getString(tw.org.ctssf.app.android.R.string.team_woman))){
                            updateStageList(mListGenderF);
                        }
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
                        mListPop3 = new ListPopupWindow(getActivity());
                        mListPop3.setAdapter(adapter);
                        mListPop3.setWidth(Utils.measureContentWidth(getContext(), adapter));
                        mListPop3.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
                        mListPop3.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(getActivity(), android.R.color.white)));
                        mListPop3.setAnchorView(mDropMenu3);
                        mListPop3.setModal(true);
                        mListPop3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                mTv3.setText((String)parent.getAdapter().getItem(position));
                                mSelectedGroup = position;
                                try {
                                    JSONObject jsonObject = (JSONObject)mGroupList.get(position);
                                    String groupSn = jsonObject.getJSONObject("stage").getString("sn");
                                    getDataList(groupSn, (String)parent.getAdapter().getItem(position));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
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

                        if(adapter.isEmpty()){
                            mTv3.setText(" ");
                            mSelectedGroup = -1;
                        }else {
                            mTv3.setText((String) adapter.getItem(0));
                            mSelectedGroup = 0;
                            try {
                                JSONObject jsonObject = (JSONObject)mGroupList.get(0);
                                String groupSn = jsonObject.getJSONObject("stage").getString("sn");
                                getDataList(groupSn, (String) adapter.getItem(0) );
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };
        httpClient.async_query_GET(RestApi.getGroupListByStage(stageSn), null, callback);
    }

    private void updateStageList(final HashMap<String, String> map){
        final List<String> list = new ArrayList<String>();
        for ( String key : map.keySet() ) {
            list.add(key);
        }
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getActivity(), tw.org.ctssf.app.android.R.layout.expandable_list_item, list);
        mListPop2 = new ListPopupWindow(getActivity());
        mListPop2.setAdapter(adapter2);
        mListPop2.setWidth(Utils.measureContentWidth(getContext(), adapter2));
        mListPop2.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        mListPop2.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(getActivity(), android.R.color.white)));
        mListPop2.setAnchorView(mDropMenu2);
        mListPop2.setModal(true);
        mListPop2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mTv2.setText(list.get(position));
                mSelectedStage = position;
                getGroupListByStage(map.get(list.get(position)));
                //getDataList(map.get(mTv2.getText().toString()));
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
        if(list.size() > 0) {
            mTv2.setText(list.get(0));
            getGroupListByStage(map.get(list.get(0)));
        }
        //getDataList(map.get(mTv2.getText().toString()));
    }
    /*
    private void getGroupListByStage(String stageSn){
        HttpClient httpClient = new HttpClient();
        HttpClient.HttpResponseCallback callback = new HttpClient.HttpResponseCallback() {
            @Override
            public void onResponse(Bundle result) {
                try {
                    mGroupList = new JSONArray(result.getString("response"));
                    if(getActivity() != null) {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };
        httpClient.async_query_GET(RestApi.getGroupListByStage(stageSn), null, callback);
    }*/
}