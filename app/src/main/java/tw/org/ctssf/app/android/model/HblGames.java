package tw.org.ctssf.app.android.model;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Gary on 2018/10/3.
 */

public class HblGames {
    private final String TAG = "HBL-HblGames";
    public long playDate;
    public String playTime;
    public String location;
    public String currentQuarter;
    public String status;
    public JSONObject teamHome;
    public String teamHomeLogo;
    public JSONObject teamHomeRecord;
    public String teamHomeRecordScore;
    public JSONObject teamGuest;
    public String teamGuestLogo;
    public JSONObject teamGuestRecord;
    public String teamGuestRecordScore;
    public String mGameSN;
    private String mJSONObjectString;
    public HblGames(JSONObject jsonObject){
        try {
            mJSONObjectString = jsonObject.toString();
            playDate = jsonObject.getLong("playDate");
            playTime = jsonObject.getString("playTime");
            location = jsonObject.getString("location");
            currentQuarter = jsonObject.getString("currentQuarter");
            status = jsonObject.getString("status");
            teamHome = jsonObject.getJSONObject("teamHome");
            teamHomeLogo = teamHome.getString("logo");
            teamHomeRecord = jsonObject.getJSONObject("teamHomeRecord");
            teamHomeRecordScore = teamHomeRecord.getString("score");
            teamGuest = jsonObject.getJSONObject("teamGuest");
            teamGuestLogo = teamGuest.getString("logo");
            teamGuestRecord = jsonObject.getJSONObject("teamGuestRecord");
            teamGuestRecordScore = teamGuestRecord.getString("score");
            mGameSN = jsonObject.getString("sn");
        } catch (JSONException e){
            Log.v(TAG, "e: " + e.toString());
            e.printStackTrace();
        }
    }

    public String getDataInString(){
        return mJSONObjectString;
    }
}
