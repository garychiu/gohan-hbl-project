package com.asana.hbl.fragment;

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

/**
 * Created by Gary on 2018/10/1.
 */

public class DashboardFragment extends Fragment {
    public static final String TAG = "HBL-DashboardFragment";
    ListView mListView;
    JSONArray mData;
    RelativeLayout mDropMenu1, mDropMenu2;
    private ListPopupWindow mListPop1, mListPop2;
    TextView mTv1, mTv2;
    private ImageView mDreoDownIndicator1, mDreoDownIndicator2;
    private List<String > mListGender = new ArrayList<String>();
    private List<String > mListName= new ArrayList<String>();
    public static DashboardFragment newInsTance(){
        DashboardFragment fragment = new DashboardFragment();
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        mDropMenu1 = (RelativeLayout)view.findViewById(R.id.drop_menu1);
        mDropMenu2 = (RelativeLayout)view.findViewById(R.id.drop_menu2);
        mListView = (ListView)view.findViewById(R.id.lv);

        mDreoDownIndicator1 = (ImageView) view.findViewById(R.id.iv1);
        mDreoDownIndicator2 = (ImageView) view.findViewById(R.id.iv2);
        mTv1 = (TextView)view.findViewById(R.id.tv1);
        mTv2 = (TextView)view.findViewById(R.id.tv2);

        mListGender.add(getResources().getString(R.string.team_man));
        mListGender.add(getResources().getString(R.string.team_woman));

        mListName.add(getResources().getString(R.string.qualifying));
        mListName.add(getResources().getString(R.string.preliminaries));
        mListName.add(getResources().getString(R.string.remach));
        mListName.add(getResources().getString(R.string.semi_final));
        mListName.add(getResources().getString(R.string.finals));
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getActivity(), R.layout.expandable_list_item, mListGender);
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
                getDataList(mTv1.getText().toString(), mTv2.getText().toString());
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
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getActivity(), R.layout.expandable_list_item, mListName);
        mListPop2 = new ListPopupWindow(getActivity());
        mListPop2.setAdapter(adapter2);
        mListPop1.setWidth(Utils.measureContentWidth(getContext(), adapter2));
        mListPop2.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        mListPop2.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(getActivity(), android.R.color.white)));
        mListPop2.setAnchorView(mDropMenu2);
        mListPop2.setModal(true);
        mListPop2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mTv2.setText(mListName.get(position));
                getDataList(mTv1.getText().toString(), mTv2.getText().toString());
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

        mTv1.setText(mListGender.get(0));
        mTv2.setText(mListName.get(0));

        getDataList(mTv1.getText().toString(), mTv2.getText().toString());

        return view;
    }

    private void getDataList(String selectedGender, String selectedName){
        String stageSN = "";
        if(selectedGender.equals(getResources().getString(R.string.team_man))
                && selectedName.equals(getResources().getString(R.string.qualifying))){
            stageSN = "1";
        }else if(selectedGender.equals(getResources().getString(R.string.team_woman))
                && selectedName.equals(getResources().getString(R.string.qualifying))){
            stageSN = "6";
        }else if(selectedGender.equals(getResources().getString(R.string.team_man))
                && selectedName.equals(getResources().getString(R.string.preliminaries))){
            stageSN = "2";
        }else if(selectedGender.equals(getResources().getString(R.string.team_woman))
                && selectedName.equals(getResources().getString(R.string.preliminaries))){
            stageSN = "7";
        }else if(selectedGender.equals(getResources().getString(R.string.team_man))
                && selectedName.equals(getResources().getString(R.string.remach))){
            stageSN = "3";
        }else if(selectedGender.equals(getResources().getString(R.string.team_woman))
                && selectedName.equals(getResources().getString(R.string.remach))){
            stageSN = "8";
        }else if(selectedGender.equals(getResources().getString(R.string.team_man))
                && selectedName.equals(getResources().getString(R.string.semi_final))){
            stageSN = "4";
        }else if(selectedGender.equals(getResources().getString(R.string.team_woman))
                && selectedName.equals(getResources().getString(R.string.semi_final))){
            stageSN = "9";
        }else if(selectedGender.equals(getResources().getString(R.string.team_man))
                && selectedName.equals(getResources().getString(R.string.finals))){
            stageSN = "5";
        }else if(selectedGender.equals(getResources().getString(R.string.team_woman))
                && selectedName.equals(getResources().getString(R.string.finals))){
            stageSN = "10";
        }
        HttpClient httpClient = new HttpClient();
        HttpClient.HttpResponseCallback callback = new HttpClient.HttpResponseCallback() {
            @Override
            public void onResponse(Bundle result) {
                try {
                    mData = new JSONArray(result.getString("response"));
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
                convertView = mInflater.inflate(R.layout.dashboard_list_item, null);
                holder = new ViewHolder(
                        (TextView) convertView.findViewById(R.id.tv_team_name),
                        (TextView) convertView.findViewById(R.id.tv_win),
                        (TextView) convertView.findViewById(R.id.tv_lose),
                        (TextView) convertView.findViewById(R.id.tv_raking),
                        (CircularImageView) convertView.findViewById(R.id.iv_team)
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
}