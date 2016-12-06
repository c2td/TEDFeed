package com.application.pm.tedfeed;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/* AsyncTask class for bitmap loading */
class LoadImageTask extends AsyncTask<Void, Void, Bitmap> {

    private String mUrl;
    private ImageView mImageView;
    private int mIndex;

    LoadImageTask(int index, String url, ImageView imageView) {
        this.mUrl = url;
        mImageView = imageView;
        mIndex = index;
    }

        @Override
        protected Bitmap doInBackground(Void... params) {
            try {
                return getBitmapFromURL(mUrl);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);

            // Saving bitmap to cache for later use
            Cache.getInstance().getLru().put(mIndex, result);
            if (mImageView != null) {
                mImageView.setImageBitmap(result);
            }
        }


     /* Loads and returns bitmap from the specified URL*/
    private static Bitmap getBitmapFromURL(String src) {

        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
