package com.example.android.news;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {

    ArrayList<NewsArticle> newsArticles = new ArrayList<>();

    /**
     * Source Code Reference: http://web.archive.org/web/20140531042945/https://weblogs.java.net/blog/pat/archive/2004/10/stupid_scanner_1.html
     */

    static String convertStreamToString(InputStream inputStream, String encoding) {
        Scanner scanner = new Scanner(inputStream, encoding).useDelimiter("\\A");
        return scanner.next();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    //TODO: Find a method to refresh the content. Preferably a "pull down gesture".

    // Before attempting to fetch the URL, makes sure that there is a network connection.
        ConnectivityManager connMgr = (ConnectivityManager)
        getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            String GUARDIAN_API_URL = "http://content.guardianapis.com/us/technology?api-key=test&show-fields=headline,trailText,thumbnail,shortUrl";
            new DownloadWebpageTask().execute(GUARDIAN_API_URL);
        } else {
            Log.v("mClickHandler", "No network connection available.");
        }
    }

    // Given a URL, establishes an HttpUrlConnection and retrieves
// the web page content as a InputStream, which it returns as
// a string.
    private String downloadUrl(String myUrl) throws IOException {
        InputStream is = null;

        try {
            URL url = new URL(myUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            is = conn.getInputStream();

            // Convert the InputStream into a string
            return convertStreamToString(is, "UTF-8");

            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    // Uses AsyncTask to create a task away from the main UI thread. This task takes a
    // URL string and uses it to create an HttpUrlConnection. Once the connection
    // has been established, the AsyncTask downloads the contents of the webpage as
    // an InputStream. Finally, the InputStream is converted into a string, which is
    // displayed in the UI by the AsyncTask's onPostExecute method.
    private class DownloadWebpageTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            // params comes from the execute() call: params[0] is the url.
            try {
                return downloadUrl(urls[0]);
            } catch (IOException e) {
                return "Unable to retrieve web page. URL may be invalid.";
            }
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            //Custom Adapter
            ArrayAdapter<NewsArticle> arrayAdapter = new NewsArticleAdapter(MainActivity.this, newsArticles);
            ListView rootView = (ListView) findViewById(R.id.rootview_list_view);
            try {
                JSONObject jsonRootObject = new JSONObject(result);
                JSONObject jsonResults = jsonRootObject.getJSONObject("response");
                JSONArray jsonArray = jsonResults.optJSONArray("results");

                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    JSONObject info = jsonObject.getJSONObject("fields");

                    String headline = info.getString("headline");
                    String trailText = info.getString("trailText");
                    String thumbnail = info.getString("thumbnail");
                    String shortUrl = info.getString("shortUrl");

                    newsArticles.add(new NewsArticle(headline, trailText, shortUrl, thumbnail));
                }
                rootView.setAdapter(arrayAdapter);

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }
}
