package org.fsftn.discuss;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.Calendar;


public class ActivityCheckerService extends Service {
    Calendar LastNotified;
    Integer NotificationID;
    public ActivityCheckerService() {
        NotificationID=123456;
        LastNotified=Calendar.getInstance();
    }

    public class MyBinder extends Binder
    {
        public ActivityCheckerService getService()
        {
            return ActivityCheckerService.this;
        }
    }
    @Override
    public IBinder onBind(Intent intent) {
        IBinder mb=new MyBinder();
        return (mb);
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent,flags,startId);
        try {
            JSONObject reader = (JSONObject) new JSONObjectRetriever().execute("http://discuss.fsftn.org/latest.json").get();
            JSONArray j = reader.getJSONObject("topic_list").getJSONArray("topics");
            for (int i = j.length() - 1; i > 0; i--) {
                String lpdate = j.getJSONObject(i).getString("last_posted_at");
                Calendar date = ParseDate.parse(lpdate);
                if (date.after(LastNotified)) {
                    notifyNewPost(j.getJSONObject(i));
                    LastNotified = date;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return START_STICKY;
    }

    public void notifyNewPost(JSONObject j)
    {
        try {
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(this)
                            .setSmallIcon(R.drawable.ic_launcher)
                            .setContentTitle(j.getString("title"))
                            .setContentText("Click to check");
            String slug = j.getString("slug");
            Integer id = j.getInt("id");
            String url="http://discuss.fsftn.org/t/"+slug+"/"+id.toString();
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
            mBuilder.setAutoCancel(true);
            mBuilder.setContentIntent(resultPendingIntent);
            NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            nm.notify(NotificationID, mBuilder.build());
            NotificationID++;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
