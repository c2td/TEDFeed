package com.application.pm.tedfeed;

import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class CatalogFragment extends ListFragment implements LoaderManager.LoaderCallbacks<List<CatalogItem>> {

    // A custom adapter for displaying catalog mItems in the list view
    private CatalogAdapter mCatalogAdapter;

    // A list for storing all the retrieved item objects
    static ArrayList<CatalogItem> mItems;

    // Url for making the RSS feed query
    public static final String URL = "http://feeds.feedburner.com/tedtalks_video";

    // A constant value for the loader ID
    public static final int CATALOG_LOADER_ID = 1;

    // An empty text view for error messages
    private TextView mEmptyTextView;

    public interface OnSelectionChangeListener {
        void OnSelectionChanged(int versionNameIndex);
    }

    // Empty constructor required by the fragment manager
    public CatalogFragment() {}

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mItems = new ArrayList<>();

        // Prepare an empty view just in case the data cannot be displayed
        mEmptyTextView = (TextView) getActivity().findViewById(R.id.empty_view);

        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {

            // Initialize and prepare the loader
            final LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(CATALOG_LOADER_ID, null, this);
        } else {

            // Update the text view with no connection error message
            mEmptyTextView.setVisibility(View.VISIBLE);
            mEmptyTextView.setText(R.string.no_internet_connection);
        }

        mCatalogAdapter = new CatalogAdapter(getActivity(), mItems);
        setListAdapter(mCatalogAdapter);

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        OnSelectionChangeListener listener = (OnSelectionChangeListener) getActivity();
        listener.OnSelectionChanged(position);
    }

    @Override
    public Loader<List<CatalogItem>> onCreateLoader(int id, Bundle args) {

        // Create a new loader for the given URL
        return new CatalogLoader(getActivity(), URL);
    }

    @Override
    public void onLoadFinished(Loader<List<CatalogItem>> loader, List<CatalogItem> items) {

        // Clear the adapter of previous data
        mCatalogAdapter.clear();

        // Add the data to adapter's data set
        if (items != null && !items.isEmpty()) {
            mCatalogAdapter.clear();
            mCatalogAdapter.addAll(items);

        } else {
            // Display a message when there is no data
            mEmptyTextView.setVisibility(View.VISIBLE);
            mEmptyTextView.setText(R.string.no_data);
        }
    }

    @Override
    public void onLoaderReset(Loader loader) {

        // Clear the existing data
        mCatalogAdapter.clear();
    }

    public static ArrayList<CatalogItem> getItems() {
        return mItems;
    }


}
