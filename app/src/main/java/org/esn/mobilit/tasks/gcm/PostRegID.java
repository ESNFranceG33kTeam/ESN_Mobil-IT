package org.esn.mobilit.tasks.gcm;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.esn.mobilit.activities.SplashActivity;
import org.esn.mobilit.utils.ApplicationConstants;
import org.esn.mobilit.utils.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PostRegID extends AsyncTask<Void, Void, Void> {

    SplashActivity activity;
    private static final String TAG = PostRegID.class.getSimpleName();

    public PostRegID(SplashActivity activity) {
        this.activity = activity;
    }

    @Override
    protected Void doInBackground(Void... params) {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(ApplicationConstants.APP_SERVER_URL);
        try {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("regId", activity.getGcmService().getRegId()));
            nameValuePairs.add(new BasicNameValuePair("CODE_SECTION", Utils.getDefaults(activity.getContext(), "CODE_SECTION")));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            HttpResponse response = httpclient.execute(httppost);
        } catch (ClientProtocolException e) {
            Log.d(TAG, "ClientProtocolException" + e.getMessage());
        } catch (IOException e) {
            Log.d(TAG,"IOException" + e.getMessage());
        }

        return null;
    }

    protected void onPostExecute(Void args) {
        this.activity.getGcmService().storeRegIdinSharedPref();
        this.activity.incrementCount();

        if (this.activity.count == this.activity.count_limit) {
            this.activity.launchHomeActivity();
            this.activity.finish();
        }

    }
}