package com.example.android.news;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by Jason on 02/07/2016.
 */
public class NewsArticleAdapter extends ArrayAdapter<NewsArticle>{

    public NewsArticleAdapter(Context context, ArrayList<NewsArticle> newsArticles) {
        super(context, 0, newsArticles);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        // Get the object located at this position in the list
        final NewsArticle newsArticle = getItem(position);

        // Find the ImageView in the list_item.xml layout
        ImageView imageView = (ImageView) listItemView.findViewById(R.id.image);
        // Set the image on the ImageView
        new DownloadImageTask(imageView).execute(newsArticle.getmImageResourceId());

        // Find the TextView in the list_item.xml layout
        TextView headlineTextView = (TextView) listItemView.findViewById(R.id.headline);
        // Set the text on the TextView
        headlineTextView.setText(newsArticle.getmHeadline());

        // Find the TextView in the list_item.xml layout
        TextView trailTextTextView = (TextView) listItemView.findViewById(R.id.trail_text);
        // Set the text on the TextView
        trailTextTextView.setText(newsArticle.getmTrailText());


        LinearLayout listItemContainerView = (LinearLayout) listItemView.findViewById(R.id
                .list_item_container_view);
        listItemContainerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse
                        (newsArticle
                        .getmShortUrl())));
            }
        });



        return listItemView;
    }

    /**
     * Source: http://www.wingnity.com/blog/android-json-parsing-and-image-loading-tutorial/
     */
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }
        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
