package org.fsftn.discuss;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ParseDate {
    public static Calendar parse(String date)
    {
        String d[]=date.split("-");
        Calendar cal = Calendar.getInstance();
        Calendar TimeZoneChange=Calendar.getInstance();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        DateFormat df1 = new SimpleDateFormat("HH:mm");
        try {cal.setTime(df.parse(d[0]+'-'+d[1]+'-'+d[2]));TimeZoneChange.setTime(df1.parse(d[3]));}
        catch (Exception e)
        {e.printStackTrace();}
        cal.add(Calendar.HOUR,TimeZoneChange.get(Calendar.HOUR)+5);         //adding time change in time
        cal.add(Calendar.MINUTE,TimeZoneChange.get(Calendar.MINUTE)+30);    //due to different time zones
        return cal;
    }
}
