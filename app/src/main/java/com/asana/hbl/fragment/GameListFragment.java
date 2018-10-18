package com.asana.hbl.fragment;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.asana.hbl.R;
import com.asana.hbl.activity.SingleGameActivity;
import com.asana.hbl.model.HblGames;
import com.asana.hbl.utils.HBLImageLoader;
import com.asana.hbl.views.CircularImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gary on 2018/10/3.
 */

public class GameListFragment extends Fragment {
    public static final String EXTRA_TEXT = "arg";
    public static final String TAG = "HBL-GameListFragment";
    public static String mTag;
    ListView mListView;
    List<HblGames> mDataList = new ArrayList<HblGames>();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_game_list, container, false);
    }

    public static GameListFragment newInsTance(){
        GameListFragment fragment = new GameListFragment();
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mListView = (ListView) view.findViewById(R.id.listview);
        Bundle bundle = getArguments();
        if (bundle != null) {
            try {
                JSONArray jArray = new JSONArray(bundle.getString(EXTRA_TEXT));
                for(int i=0; i<jArray.length(); i++){
                    HblGames gameInfo = new HblGames((JSONObject)jArray.get(i));
                    mDataList.add(gameInfo);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        initListView();
    }

    private void initListView(){
        MyAdapter adapter =  new MyAdapter(getActivity(), mDataList);
        mListView.setAdapter(adapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MyAdapter.ViewHolder holder = (MyAdapter.ViewHolder) view.getTag();
                HblGames gameData = holder.gameData;
                if(gameData != null){
                    Intent i = new Intent();
                    i.setClass(getActivity(), SingleGameActivity.class);
                    i.putExtra("game_data", gameData.getDataInString());
                    startActivity(i);
                }
            }
        });
    }

    public class MyAdapter extends BaseAdapter {
        private List<HblGames> mDataList;
        private LayoutInflater mInflater;
        private class ViewHolder {
            TextView txt1,txt2, txt3, txt4, txtHome, txtGuest;
            CircularImageView imgHome, imgGuest;
            HblGames gameData;
            public ViewHolder(TextView txt1, TextView txt2, TextView txt3, TextView txt4, TextView txtHome, TextView txtGuest, CircularImageView imgHome, CircularImageView imgGuest){
                this.txt1 = txt1;
                this.txt2 = txt2;
                this.txt3 = txt3;
                this.txt4 = txt4;
                this.txtHome = txtHome;
                this.txtGuest = txtGuest;
                this.imgHome = imgHome;
                this.imgGuest = imgGuest;
            }
        }
        public MyAdapter(Context context, List<HblGames> dataList){
            mInflater = LayoutInflater.from(context);
            this.mDataList = dataList;
        }

        @Override
        public int getCount() {
            return mDataList.size();
        }

        @Override
        public Object getItem(int position) {
            return mDataList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return mDataList.lastIndexOf(getItem(position));
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if(convertView==null){
                convertView = mInflater.inflate(R.layout.game_item_list, null);
                holder = new ViewHolder(
                        (TextView) convertView.findViewById(R.id.tv1),
                        (TextView) convertView.findViewById(R.id.tv2),
                        (TextView) convertView.findViewById(R.id.tv3),
                        (TextView) convertView.findViewById(R.id.tv4),
                        (TextView) convertView.findViewById(R.id.tv_home),
                        (TextView) convertView.findViewById(R.id.tv_guest),
                        (CircularImageView) convertView.findViewById(R.id.image_home),
                        (CircularImageView) convertView.findViewById(R.id.image_guest)
                );
                convertView.setTag(holder);
            }else{
                holder = (ViewHolder) convertView.getTag();
            }
            HblGames hblGabe = (HblGames)getItem(position);
            holder.gameData = hblGabe;
            SimpleDateFormat formater = new SimpleDateFormat("MM/dd/yyyy h:mma");
            holder.txt1.setText(formater.format(hblGabe.playDate).toString());
            holder.txt2.setText(hblGabe.location);
            holder.txt3.setText(hblGabe.currentQuarter);
            holder.txt4.setText(hblGabe.playTime);
            JSONObject jobject;
            try {
                jobject =  hblGabe.teamHomeRecord;
                holder.txtHome.setText(jobject.getString("score"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                jobject =  hblGabe.teamGuestRecord;
                holder.txtGuest.setText(jobject.getString("score"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            HBLImageLoader.loadTeamImage(getActivity().getApplicationContext(), hblGabe.teamHomeLogo, holder.imgHome);

            HBLImageLoader.loadTeamImage(getActivity().getApplicationContext(), hblGabe.teamGuestLogo, holder.imgGuest);

            return convertView;
        }
    }
}
