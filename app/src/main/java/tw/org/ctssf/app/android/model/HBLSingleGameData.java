package tw.org.ctssf.app.android.model;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Gary on 2018/10/4.
 */

public class HBLSingleGameData {
    private final String TAG = "HBL-HBLSingleGameData";
    public static final int TEAM_HOME = 0;
    public static final int TEAM_AWAY = 1;
    public String date;
    public String id;
    public String location;
    public String status;
    public String time;
    public String current_period;
    public String away_id;
    public String away_logo;
    public String away_name;
    public String score_away;
    public String home_name_alt;
    public String home_id;
    public String home_logo;
    public String home_name;
    public String score_home;
    public JSONArray player_stats_away;
    public JSONArray score_away_p2p;
    public JSONArray player_stats_home;
    public JSONArray score_home_p2p;
    public JSONArray mTeamGuestRecordLeaders, mTeamHomeRecordLeaders;
    public JSONObject team_stats_away;
    public JSONObject team_stats_home;
    private int mSlectedTeam;
    private String mJSONObjectString;
    public HBLSingleGameData(JSONObject jsonObject){
        try {
            mJSONObjectString = jsonObject.toString();
            away_name = jsonObject.getString("away_name");
            home_name = jsonObject.getString("home_name");

            date = jsonObject.getString("date");
            id = jsonObject.getString("id");
            location = jsonObject.getString("location");
            location = jsonObject.getString("status");
            time = jsonObject.getString("time");
            current_period = jsonObject.getString("current_period");
            away_id = jsonObject.getString("away_id");
            away_logo = jsonObject.getString("away_logo");
            score_away = jsonObject.getString("score_away");
            home_name_alt = jsonObject.getString("home_name_alt");
            home_id = jsonObject.getString("home_id");
            home_logo = jsonObject.getString("home_logo");
            score_home = jsonObject.getString("score_home");

            player_stats_away = jsonObject.getJSONArray("player_stats_away");
            score_away_p2p = jsonObject.getJSONArray("score_away_p2p");
            player_stats_home =  jsonObject.getJSONArray("player_stats_home");
            score_home_p2p = jsonObject.getJSONArray("score_home_p2p");
            team_stats_away = jsonObject.getJSONObject("team_stats_away");
            team_stats_home = jsonObject.getJSONObject("team_stats_home");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            mTeamGuestRecordLeaders = jsonObject.getJSONArray("teamGuestRecordLeaders");
        } catch (JSONException e){
            Log.v(TAG, "e: " + e.toString());
            e.printStackTrace();
        }
        try {
            mTeamHomeRecordLeaders = jsonObject.getJSONArray("teamHomeRecordLeaders");
        } catch (JSONException e){
            Log.v(TAG, "e: " + e.toString());
            e.printStackTrace();
        }
    }

    public String getDataInString(){
        return mJSONObjectString;
    }
    public void setSelectedTeam(int team){
        mSlectedTeam = team;
    }
    public int getSelectedTeam(){
        return mSlectedTeam;
    }
}
