package com.twinvaves.darshan.querto2;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by darshan on 15/09/15.
 */
public class fetchTimeline extends AsyncTask<String, Void, Boolean> {


    BufferedReader mBufferedInputStream;
    String Response = "";
    public static ArrayList<DataClass> dataList = new ArrayList<>();
    Context context;
    ProgressDialog progressDialog;


    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        progressDialog.dismiss();
        MainActivity.timelineAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(context);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMessage("Loading...");
        progressDialog.setIndeterminate(true);
        progressDialog.show();
    }

    fetchTimeline(Context context) {
        this.context = context;
    }

    @Override
    protected Boolean doInBackground(String... params) {
        Log.d("darshan", "fetching data");
        try {
            URL url = new URL("http://204.152.203.111/timeline.php");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

            httpURLConnection.setConnectTimeout(15000);
            httpURLConnection.setReadTimeout(10000);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);

            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("type", "tech")
                    .appendQueryParameter("page_no", "1");

            String query = builder.build().getEncodedQuery();

            OutputStream os = httpURLConnection.getOutputStream();

            BufferedWriter mBufferedWriter = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            mBufferedWriter.write(query);
            mBufferedWriter.flush();
            mBufferedWriter.close();
            os.close();

            httpURLConnection.connect();


            if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {

                mBufferedInputStream = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                String inline;
                while ((inline = mBufferedInputStream.readLine()) != null) {
                    Response += inline;
                }
                mBufferedInputStream.close();

                parseJson(Response);


            } else {
                Log.d("darshan", "something wrong");

            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    JSONArray jsonArray;
    static String img_url = "http://darshannn.hostei.com/question_pics/";


    private void parseJson(String response) {
        DataClass data1;
        Log.d("darshan", response);

        try {
            jsonArray = new JSONArray(response);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject sub = jsonArray.getJSONObject(i);

                data1 = new DataClass();

                data1.UserName = (sub.getString("user_name"));
                data1.UserImage = (sub.getString("user_img"));
                data1.QuestionId = (sub.getString("que_id"));
                data1.Question = (sub.getString("question"));
                data1.QuestionPicture = (img_url + sub.getString("question_img"));

                dataList.add(data1);

            }

            for (int i = 0; i < dataList.size(); i++) {
                Log.d("Darshan", dataList.get(i).Question);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
