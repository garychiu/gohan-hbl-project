package tw.org.ctssf.app.android.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Gary on 2018/10/3.
 */

public class TimeUtils {

    public static long  compareToNowDate(Date date){
        Date nowdate = new Date();
        //format date pattern
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //convert to millions seconds
        long time = DateToLong(StringToDate(formatter.format(nowdate)));
        long serverTime=DateToLong(date);
        //convert to seconds
        long minTime = Math.abs(time-serverTime)/1000;
        return minTime;
        //Log.d(getLocalClassName(), "minTime= "+minTime);
    }

    private static long DateToLong(Date time){
        SimpleDateFormat st=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//yyyyMMddHHmmss
        return Long.parseLong(st.format(time));
    }

    private static Date StringToDate(String s){
        Date time=null;
        SimpleDateFormat sd=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            time=sd.parse(s);
        } catch (java.text.ParseException e) {
            System.out.println("输入的日期格式有误！");
            e.printStackTrace();
        }
        return time;
    }

    public long compareDataToNow(String date){

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date passDate,nowDate;
        long diff=-100l,days=-100l;

        try {
            passDate = sdf.parse(date);
            String nowStr=sdf.format(new Date());
            nowDate=sdf.parse(nowStr);

            diff = passDate.getTime() - nowDate.getTime();
            days = diff / (1000 * 60 * 60 * 24);
            System.out.println( "相隔："+days+"天");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return diff;
    }


}
