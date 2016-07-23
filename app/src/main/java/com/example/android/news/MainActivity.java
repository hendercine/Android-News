package com.example.android.news;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.AbsListView;
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
        final SwipeRefreshLayout swipeView = (SwipeRefreshLayout) findViewById(R.id.swipe);

        swipeView.setEnabled(false);
        ListView lView = (ListView) findViewById(R.id.rootview_list_view);
        ArrayAdapter<String> adp = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, createItems(40,0 ));
        lView.setAdapter(adp);

        swipeView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeView.setRefreshing(true);
                ( new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeView.setRefreshing(false);

                    }
                }, 3000);
            }
        });

        lView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem == 0)
                    swipeView.setEnabled(true);
                else
                    swipeView.setEnabled(false);
            }
        });

        //TODO: Find a method to refresh the content. Preferably a "pull down gesture".

        // Before attempting to fetch the URL, makes sure that there is a network connection.
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            String GUARDIAN_API_URL = "http://content.guardianapis.com/search?show-fields=headline%2CtrailText%2CshortUrl%2Cthumbnail%2ClastModified&page-size=20&q=Canada%2CCanadian%2CCanadians%2CCanada%27s%2CToronto%2CMontreal%2CVancouver%2CCanuck&api-key=test";
            new DownloadWebpageTask().execute(GUARDIAN_API_URL);
        } else {
            Log.v("mClickHandler", "No network connection available.");
        }
    }

    private int createItems(int i, int i1) {
        return 0;
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
                    String headline;
                    String trailText;
                    String shortUrl;
                    String thumbnail;
                    String lastModified;
                    JSONObject resultsObject = jsonArray.getJSONObject(i);
                    JSONObject fields = resultsObject.getJSONObject("fields");

                    headline = fields.optString("headline");
                    trailText = fields.optString("trailText");
                    thumbnail = fields.optString("thumbnail");
                    shortUrl = fields.optString("shortUrl");
                    lastModified = fields.optString("lastModified");

                    newsArticles.add(new NewsArticle(headline, trailText, shortUrl, thumbnail, lastModified));
                }
                rootView.setAdapter(arrayAdapter);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("key", newsArticles);
        super.onSaveInstanceState(outState);
    }
}