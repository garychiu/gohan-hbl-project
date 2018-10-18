package com.asana.hbl.model;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.asana.hbl.R;;
import com.asana.hbl.utils.HBLImageLoader;
import com.asana.hbl.views.CircularImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gary on 2018/10/5.
 */

public class ExpandableListAdapter extends BaseExpandableListAdapter {
    final String TAG = "HBL-ExpandableAdapter";
    private class ViewHolder {
        TextView txt1,txt2, txt3, txt4, txt5;
        CircularImageView image_player;
        public ViewHolder(TextView txt1, TextView txt2, TextView txt3, TextView txt4, TextView txt5, CircularImageView imagePlayer){
            this.txt1 = txt1;
            this.txt2 = txt2;
            this.txt3 = txt3;
            this.txt4 = txt4;
            this.txt5 = txt5;
            this.image_player = imagePlayer;
        }
    }

    private class ViewHolderChild {
        TextView txt31,txt32, txt33, txt34, txt35, txt36, txt37, txt38, txt39;
        TextView txt61,txt62, txt63, txt64, txt65, txt66, txt67;
        public ViewHolderChild(TextView txt31, TextView txt32, TextView txt33, TextView txt34, TextView txt35, TextView txt36, TextView txt37, TextView txt38, TextView txt39,
                          TextView txt61, TextView txt62, TextView txt63, TextView txt64, TextView txt65, TextView txt66, TextView txt67){
            this.txt31 = txt31;
            this.txt32 = txt32;
            this.txt33 = txt33;
            this.txt34 = txt34;
            this.txt35 = txt35;
            this.txt36 = txt36;
            this.txt37 = txt37;
            this.txt38 = txt38;
            this.txt39 = txt39;

            this.txt61 = txt61;
            this.txt62 = txt62;
            this.txt63 = txt63;
            this.txt64 = txt64;
            this.txt65 = txt65;
            this.txt66 = txt66;
            this.txt67 = txt67;
        }
    }


    private Context mContext;
    private HBLSingleGameData mGameData;
    private List<JSONObject> mPlayerList = new ArrayList<JSONObject>();
    public ExpandableListAdapter(Context context, HBLSingleGameData data) {
        this.mContext = context;
        this.mGameData = data;
        initPlayerList();
    }

    @Override
    public int getGroupCount() {
        return mPlayerList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mPlayerList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mPlayerList.get(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.expended_item_header, null);
            holder = new ViewHolder(
                    (TextView) convertView.findViewById(R.id.tv1),
                    (TextView) convertView.findViewById(R.id.tv2),
                    (TextView) convertView.findViewById(R.id.tv3),
                    (TextView) convertView.findViewById(R.id.tv4),
                    (TextView) convertView.findViewById(R.id.tv5),
                    (CircularImageView) convertView.findViewById(R.id.image_player)
            );
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        try {
            holder.txt1.setText(((JSONObject)getGroup(groupPosition)).getString("name_alt"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            Log.v(TAG, ((JSONObject)getGroup(groupPosition)).toString());
            int secondsec = Integer.parseInt(((JSONObject)getGroup(groupPosition)).getString("seconds"))/60;
            String stMin = String.valueOf(secondsec);
            holder.txt2.setText(stMin);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            holder.txt3.setText(((JSONObject)getGroup(groupPosition)).getString("points"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            holder.txt4.setText(((JSONObject)getGroup(groupPosition)).getString("reb"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            holder.txt5.setText(((JSONObject)getGroup(groupPosition)).getString("ast"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String playerImgUrl = "";
        HBLImageLoader.loadPlayerImage(mContext, playerImgUrl, holder.image_player);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ViewHolderChild holder = null;
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.expended_item, null);
            holder = new ViewHolderChild(
                    (TextView) convertView.findViewById(R.id.tv31),
                    (TextView) convertView.findViewById(R.id.tv32),
                    (TextView) convertView.findViewById(R.id.tv33),
                    (TextView) convertView.findViewById(R.id.tv34),
                    (TextView) convertView.findViewById(R.id.tv35),
                    (TextView) convertView.findViewById(R.id.tv36),
                    (TextView) convertView.findViewById(R.id.tv37),
                    (TextView) convertView.findViewById(R.id.tv38),
                    (TextView) convertView.findViewById(R.id.tv39),
                    (TextView) convertView.findViewById(R.id.tv61),
                    (TextView) convertView.findViewById(R.id.tv62),
                    (TextView) convertView.findViewById(R.id.tv63),
                    (TextView) convertView.findViewById(R.id.tv64),
                    (TextView) convertView.findViewById(R.id.tv65),
                    (TextView) convertView.findViewById(R.id.tv66),
                    (TextView) convertView.findViewById(R.id.tv67)
            );
            convertView.setTag(holder);
        }else{
            holder = (ViewHolderChild) convertView.getTag();
        }
        try {
            holder.txt31.setText(((JSONObject)getGroup(groupPosition)).getString("position"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            holder.txt32.setText(((JSONObject)getGroup(groupPosition)).getString("points"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            holder.txt33.setText(((JSONObject)getGroup(groupPosition)).getString("reb_o"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            holder.txt34.setText(((JSONObject)getGroup(groupPosition)).getString("reb_d"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            holder.txt35.setText(((JSONObject)getGroup(groupPosition)).getString("reb"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            holder.txt36.setText(((JSONObject)getGroup(groupPosition)).getString("ast"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            holder.txt37.setText(((JSONObject)getGroup(groupPosition)).getString("stl"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            holder.txt38.setText(((JSONObject)getGroup(groupPosition)).getString("blk"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            holder.txt39.setText(((JSONObject)getGroup(groupPosition)).getString("pfoul"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            String textMA = ((JSONObject)getGroup(groupPosition)).getString("two_m") + "/" +
                    ((JSONObject)getGroup(groupPosition)).getString("two_a");
            holder.txt61.setText(textMA);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            if(Integer.parseInt(((JSONObject)getGroup(groupPosition)).getString("two_a")) + Integer.parseInt(((JSONObject)getGroup(groupPosition)).getString("two_m")) != 0) {
                float fPersontage = Float.parseFloat(((JSONObject) getGroup(groupPosition)).getString("two_m")) /
                        (Float.parseFloat(((JSONObject) getGroup(groupPosition)).getString("two_a")) + Float.parseFloat(((JSONObject) getGroup(groupPosition)).getString("two_m")));
                fPersontage = fPersontage*100;
                Log.v("Gary", "fPersontage: " + fPersontage);
                String sPersontage = new DecimalFormat("#.##").format(fPersontage);
                holder.txt62.setText(sPersontage);
            }else{
                holder.txt62.setText("0");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        /*
        try {

            holder.txt62.setText(((JSONObject)getGroup(groupPosition)).getString("textMA"));
        } catch (JSONException e) {
            e.printStackTrace();
        }*/



        try {
            String textMA = ((JSONObject)getGroup(groupPosition)).getString("trey_m") + "/" +
                    ((JSONObject)getGroup(groupPosition)).getString("trey_a");
            holder.txt63.setText(textMA);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            if(Integer.parseInt(((JSONObject)getGroup(groupPosition)).getString("trey_m")) + Integer.parseInt(((JSONObject)getGroup(groupPosition)).getString("trey_a")) != 0) {
                float fPersontage = Float.parseFloat(((JSONObject) getGroup(groupPosition)).getString("trey_m")) /
                        (Float.parseFloat(((JSONObject) getGroup(groupPosition)).getString("trey_m")) + Float.parseFloat(((JSONObject) getGroup(groupPosition)).getString("trey_a")));
                fPersontage = fPersontage*100;
                String sPersontage = new DecimalFormat("#.##").format(fPersontage);
                holder.txt64.setText(sPersontage);
            }else{
                holder.txt64.setText("0");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        /*
        try {

            holder.txt64.setText(((JSONObject)getGroup(groupPosition)).getString("name_alt"));
        } catch (JSONException e) {
            e.printStackTrace();
        }*/

        try {
            String textMA = ((JSONObject)getGroup(groupPosition)).getString("ft_m") + "/" +
                    ((JSONObject)getGroup(groupPosition)).getString("ft_a");
            holder.txt65.setText(textMA);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            if(Integer.parseInt(((JSONObject)getGroup(groupPosition)).getString("ft_m")) + Integer.parseInt(((JSONObject)getGroup(groupPosition)).getString("ft_a")) != 0) {
                float fPersontage = Float.parseFloat(((JSONObject) getGroup(groupPosition)).getString("ft_m")) /
                        (Float.parseFloat(((JSONObject) getGroup(groupPosition)).getString("ft_m")) + Float.parseFloat(((JSONObject) getGroup(groupPosition)).getString("ft_a")));
                fPersontage = fPersontage*100;
                String sPersontage = new DecimalFormat("#.##").format(fPersontage);
                holder.txt66.setText(sPersontage);
            }else{
                holder.txt66.setText("0");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        /*
        try {
            holder.txt66.setText(((JSONObject)getGroup(groupPosition)).getString("name_alt"));
        } catch (JSONException e) {
            e.printStackTrace();
        }*/

        try {
            holder.txt67.setText(((JSONObject)getGroup(groupPosition)).getString("turnover"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    private void initPlayerList(){
        JSONArray jArray = null;
        switch(mGameData.getSelectedTeam()){
            case HBLSingleGameData.TEAM_HOME:
                jArray = mGameData.player_stats_home;
                break;
            case HBLSingleGameData.TEAM_AWAY:
                jArray = mGameData.player_stats_away;
                break;
        }
        if(jArray != null) {
            try {
                for (int i = 0; i < jArray.length(); i++) {
                    mPlayerList.add((JSONObject) jArray.get(i));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
