package tw.org.ctssf.app.android.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import tw.org.ctssf.app.android.R;
import tw.org.ctssf.app.android.model.HBLSingleGameData;
import tw.org.ctssf.app.android.utils.HBLImageLoader;
import tw.org.ctssf.app.android.views.CircularImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Gary on 2018/10/4.
 */

public class SingleGameFragment extends Fragment {
    public static final String EXTRA_TEXT = "arg";
    public static final String TAG = "HBL-SingleGameFragment";
    private HBLSingleGameData mGameData;
    public static SingleGameFragment newInsTance(){
        SingleGameFragment fragment = new SingleGameFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        Log.v(TAG, "onCreateView");
        View view = inflater.inflate(R.layout.fragment_single_game, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if (bundle != null) {
            try {
                JSONObject jObject = new JSONObject(bundle.getString(EXTRA_TEXT));
                mGameData = new HBLSingleGameData(jObject);
                initBillBord(view, mGameData);
            } catch (JSONException e) {
                Log.v(TAG, " e: " +  e.toString());
                e.printStackTrace();
            }
        }

    }

    private void initBillBord(View view, HBLSingleGameData data){
        if(data != null){
            //CircularImageView iv11;
            CircularImageView iv21;
            TextView tv15, tv16;
            TextView tv22, tv23, tv24, tv25, tv26, tv27;
            CircularImageView iv31;
            TextView tv32, tv33, tv34, tv35, tv36, tv37;
            //TextView team_home, team_away;

            ListView lv;
            tv15 = (TextView)view.findViewById(R.id.tv15);
            tv16 = (TextView)view.findViewById(R.id.tv16);
            //iv11 = (CircularImageView)view.findViewById(R.id.iv11);

            iv21 = (CircularImageView)view.findViewById(R.id.iv21);
            tv22 = (TextView)view.findViewById(R.id.tv22);
            tv23 = (TextView)view.findViewById(R.id.tv23);
            tv24 = (TextView)view.findViewById(R.id.tv24);
            tv25 = (TextView)view.findViewById(R.id.tv25);
            tv26 = (TextView)view.findViewById(R.id.tv26);
            tv27= (TextView)view.findViewById(R.id.tv27);

            iv31 = (CircularImageView)view.findViewById(R.id.iv31);
            tv32 = (TextView)view.findViewById(R.id.tv32);
            tv33 = (TextView)view.findViewById(R.id.tv33);
            tv34 = (TextView)view.findViewById(R.id.tv34);
            tv35 = (TextView)view.findViewById(R.id.tv35);
            tv36 = (TextView)view.findViewById(R.id.tv36);
            tv37 = (TextView)view.findViewById(R.id.tv37);

            //team_home = (TextView)view.findViewById(R.id.team_home);
            //team_away = (TextView)view.findViewById(R.id.team_away);

            HBLImageLoader.loadTeamImage(getActivity().getApplicationContext(), data.home_logo, iv21);
            HBLImageLoader.loadTeamImage(getActivity().getApplicationContext(), data.away_logo, iv31);

            JSONArray jsonArray;
            jsonArray = data.score_home_p2p;
            try {
                tv22.setText(jsonArray.getString(0));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                tv23.setText(jsonArray.getString(1));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                tv24.setText(jsonArray.getString(2));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                tv25.setText(jsonArray.getString(3));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                tv26.setText(jsonArray.getString(4));
            } catch (JSONException e) {
                tv16.setVisibility(View.GONE);
                tv26.setVisibility(View.GONE);
                e.printStackTrace();
            }

            tv27.setText(data.score_home);

            jsonArray = data.score_away_p2p;
            try {
                tv32.setText(jsonArray.getString(0));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                tv33.setText(jsonArray.getString(1));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                tv34.setText(jsonArray.getString(2));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                tv35.setText(jsonArray.getString(3));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                tv36.setText(jsonArray.getString(4));
            } catch (JSONException e) {
                tv36.setText(" ");
                tv16.setVisibility(View.GONE);
                tv36.setVisibility(View.GONE);
                e.printStackTrace();
            }

            tv37.setText(data.score_away);
            //team_home.setText(data.home_name);
            //team_away.setText(data.away_name);


            lv = (ListView)view.findViewById(R.id.listview);
            MyAdapter adapter =  new MyAdapter(getActivity(), mGameData);
            lv.setAdapter(adapter);
        }
    }

    public class MyAdapter extends BaseAdapter {
        private LayoutInflater mInflater;
        HBLSingleGameData mData;
        private class ViewHolder {
            TextView txt11,txt12, txt13, txt21, txt22;
            CircularImageView imgHome, imgGuest;
            View scoreBbrLeft, scoreBarRight;
            LinearLayout scoreBar;

            public ViewHolder(TextView txt11, TextView txt12, TextView txt13, TextView txt21, TextView txt22, CircularImageView imgHome, CircularImageView imgGuest, View scoreBarL, View scoreBarR, LinearLayout scoreBar){
                this.txt11 = txt11;
                this.txt12 = txt12;
                this.txt13 = txt13;
                this.txt21 = txt21;
                this.txt22 = txt22;
                this.imgHome = imgHome;
                this.imgGuest = imgGuest;
                this.scoreBbrLeft = scoreBarL;
                this.scoreBarRight = scoreBarR;
                this.scoreBar = scoreBar;
            }
        }
        public MyAdapter(Context context, HBLSingleGameData data){
            mInflater = LayoutInflater.from(context);
            mData = data;
        }

        @Override
        public int getCount() {
            return mData.mTeamHomeRecordLeaders.length();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
           ViewHolder holder = null;
            if(convertView==null){
                convertView = mInflater.inflate(R.layout.single_game_fragment_list_item, null);
                holder = new ViewHolder(
                        (TextView) convertView.findViewById(R.id.tv11),
                        (TextView) convertView.findViewById(R.id.tv12),
                        (TextView) convertView.findViewById(R.id.tv13),
                        (TextView) convertView.findViewById(R.id.tv21),
                        (TextView) convertView.findViewById(R.id.tv22),
                        (CircularImageView) convertView.findViewById(R.id.iv1),
                        (CircularImageView) convertView.findViewById(R.id.iv2),
                        (View) convertView.findViewById(R.id.score_bar_left),
                        (View) convertView.findViewById(R.id.score_bar_right),
                        (LinearLayout)convertView.findViewById(R.id.score_bar)
                );
                convertView.setTag(holder);
            }else{
                holder = (ViewHolder) convertView.getTag();
            }


            int scoreBarWith = holder.scoreBar.getLayoutParams().width;
            int eventValueLeft = 1;
            int eventValueRight= 1;
            try {
                JSONObject teamHomeRecordLeader = (JSONObject)mData.mTeamHomeRecordLeaders.get(position);
                String playerHomeLogoUrl = teamHomeRecordLeader.getJSONObject("roster").getString("profilePicture");
                HBLImageLoader.loadPlayerImage(getActivity().getApplicationContext(), playerHomeLogoUrl, holder.imgHome);
                holder.txt11.setText(teamHomeRecordLeader.getString("eventValue"));
                String eventText = "";
                switch(teamHomeRecordLeader.getString("eventName")){
                    case "pts":
                        eventText = "得分王";
                        break;
                    case "rebounds":
                        eventText = "籃板王";
                        break;
                    case "assists":
                        eventText = "助攻王";
                        break;
                }
                holder.txt12.setText(eventText);
                holder.txt21.setText(teamHomeRecordLeader.getJSONObject("roster").getString("name"));
                eventValueLeft = Integer.valueOf(teamHomeRecordLeader.getString("eventValue"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                JSONObject teamGuestRecordLeader = (JSONObject)mData.mTeamGuestRecordLeaders.get(position);
                String playerAwayLogoUrl = teamGuestRecordLeader.getJSONObject("roster").getString("profilePicture");;
                HBLImageLoader.loadPlayerImage(getActivity().getApplicationContext(), playerAwayLogoUrl, holder.imgGuest);
                holder.txt13.setText(teamGuestRecordLeader.getString("eventValue"));
                holder.txt22.setText(teamGuestRecordLeader.getJSONObject("roster").getString("name"));
                eventValueRight = Integer.valueOf(teamGuestRecordLeader.getString("eventValue"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            float scoreBarLeftWidth = (scoreBarWith*eventValueLeft/(eventValueLeft + eventValueRight));
            float scoreBarRightWidth = (scoreBarWith*eventValueRight/(eventValueLeft + eventValueRight));
            holder.scoreBbrLeft.getLayoutParams().width  = (int)scoreBarLeftWidth;;
            holder.scoreBarRight.getLayoutParams().width = (int)scoreBarRightWidth;

            return convertView;
        }
    }

}
