package org.fsftn.discuss;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class LatestPostsActivity extends ActionBarActivity {
    ListView lv;
    ArrayList<String> posts;
    JSONObject reader;
    JSONArray j;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_latest_posts);


        //set an alarm to start service every 1 minute
        AlarmManager am=(AlarmManager)getSystemService(ALARM_SERVICE);
        Intent srvce=new Intent(getApplicationContext(),ActivityCheckerService.class);
        PendingIntent pi =PendingIntent.getService(this,0,srvce,0);
        am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,60000,60000,pi);

        posts = new ArrayList<String>();
        lv=(ListView)findViewById(R.id.listView);
        ListAdapter adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,posts);
        try {
            reader = (JSONObject)new JSONObjectRetriever().execute("http://discuss.fsftn.org/latest.json").get();
            j=reader.getJSONObject("topic_list").getJSONArray("topics");
            for(int i=0;i<j.length();i++) {
                posts.add(j.getJSONObject(i).getString("title"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    String slug = j.getJSONObject(i).getString("slug");
                    Integer id = j.getJSONObject(i).getInt("id");
                    String url="http://discuss.fsftn.org/t/"+slug+"/"+id.toString();
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(Intent.createChooser(intent, "Choose browser"));
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.latest_posts, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
