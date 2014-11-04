package org.fsftn.discuss;

import android.os.AsyncTask;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

public class JSONObjectRetriever extends AsyncTask {
    JSONObject JObj;
    protected Object doInBackground(Object[] objects) {
        try {
            String url = objects[0].toString();
            HttpClient httpClient = new DefaultHttpClient();
            HttpResponse httpResponse = null;
            HttpEntity httpEntity = null;
            String response = "";
            HttpGet httpGet = new HttpGet(url);
            httpResponse = httpClient.execute(httpGet);
            httpEntity = httpResponse.getEntity();
            response = EntityUtils.toString(httpEntity);
            JObj = new JSONObject(response);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return JObj;
    }
}
