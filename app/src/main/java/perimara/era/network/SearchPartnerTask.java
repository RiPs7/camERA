package perimara.era.network;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by periklismaravelias on 25/09/16.
 */
public class SearchPartnerTask extends AsyncTask<Void, Void, String> {

    double mLatitude, mLongitude;
    String mGender;
    String mAndroidId;

    final String mServerProt = "https";
    final String mServerAddr = "localhost";
    final String mServerUrl = mServerProt + "://" + mServerAddr;

    public SearchPartnerTask(double latitude, double longitude, String gender, String androidId) {
        mLatitude = latitude;
        mLongitude = longitude;
        mGender = gender;
        mAndroidId = androidId;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Void... params) {
        StringBuilder sb=null;
        BufferedReader reader=null;
        try {
            URL url = new URL(mServerUrl + "/" + mLatitude + "/" + mLongitude + "/" + mGender);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setConnectTimeout(5000);
            connection.setRequestMethod("GET");
            connection.connect();
            int statusCode = connection.getResponseCode();
            if (statusCode == 200) {
                sb = new StringBuilder();
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
            }
            connection.disconnect();
            if (sb != null) {
                return sb.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
    }

}
