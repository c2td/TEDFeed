package com.application.pm.tedfeed;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/* Loader class for background loading of catalog data */
public class CatalogLoader extends AsyncTaskLoader<List<CatalogItem>> {

    // Query url
    private String mUrl;

    public CatalogLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    // load the list of catalog mItems immediately on loader start
    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<CatalogItem> loadInBackground() {

        if (mUrl == null) {
            return null;
        }

        // Perform the network request, parse the response and extract a list of mItems
        List<CatalogItem> items = new QueryUtils().fetchCatalogData(mUrl);
        return items;
    }
}
