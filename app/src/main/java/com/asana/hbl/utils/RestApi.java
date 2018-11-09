package com.asana.hbl.utils;

/**
 * Created by Gary on 2018/10/2.
 */

public class RestApi {
    public static String SEASON_SN = "1";

    private static String BASE_URI = "http://api.endpoints.gohoops-hbl.cloud.goog";

    public static String getGameStageList(String season){
        return BASE_URI + "/rest/stage/list/" + season + "?key=AIzaSyC_oI-W4XFOxqYu33e3Yg773ZAjt-NY5rk";
    }

    public static String getGameList(String season){
        return BASE_URI + "/rest/game/listbyseason/" + season + "?key=AIzaSyC_oI-W4XFOxqYu33e3Yg773ZAjt-NY5rk";
    }

    public static String getSingleGameData(String sn){
        return BASE_URI + "/rest/game/" + sn + "?key=AIzaSyC_oI-W4XFOxqYu33e3Yg773ZAjt-NY5rk";
    }

    public static String getGameListByStage(String sn){
        return BASE_URI + "/rest/game/listbystage/" + sn + "?key=AIzaSyC_oI-W4XFOxqYu33e3Yg773ZAjt-NY5rk";
    }

    public static String getTeamListByStage(String sn){
        return BASE_URI + "/rest/team/listbystage/" + sn + "?key=AIzaSyC_oI-W4XFOxqYu33e3Yg773ZAjt-NY5rk";
    }

    public static String getStageList(String season){
        return BASE_URI + "/rest/stage/list/" + season + "?key=AIzaSyC_oI-W4XFOxqYu33e3Yg773ZAjt-NY5rk";
    }

    public static String getGroupListByStage(String sn){
        return BASE_URI + "/rest/group/listbystage/" + sn + "?key=AIzaSyC_oI-W4XFOxqYu33e3Yg773ZAjt-NY5rk";
    }

    public static String getRosterAverageListByStageAndGroup(String stageSn, String groupSn, String eventName, int returnCount){
        return BASE_URI + "/rest/roster/averagelistbystageandgroup/" + stageSn + "/" + groupSn +  "/" + eventName + "/" + returnCount +"?key=AIzaSyC_oI-W4XFOxqYu33e3Yg773ZAjt-NY5rk";
    }

    public static String getTeamAverageListByStageAndGroup(String stageSn, String groupSn, String eventName, int returnCount){
        return BASE_URI + "/rest/roster/averagelistbystageandgroup/" + stageSn + "/" + groupSn +  "/" + eventName + "/" + returnCount +"?key=AIzaSyC_oI-W4XFOxqYu33e3Yg773ZAjt-NY5rk";
    }
}
