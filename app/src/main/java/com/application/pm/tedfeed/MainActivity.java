package com.application.pm.tedfeed;

import android.app.FragmentTransaction;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

/* Main activity class, basis for the catalog list view */
public class MainActivity extends AppCompatActivity implements CatalogFragment.OnSelectionChangeListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // If the activity is using the fragment_container, then a fragment is added
        if (findViewById(R.id.fragment_container) != null){

            // Create a new catalog fragment
            CatalogFragment catalogFragment = new CatalogFragment();
            getFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, catalogFragment).commit();
        }

    }

    /* This method runs when an item is clicked in the catalog list view */
    @Override
    public void OnSelectionChanged(int catalogItemIndex) {

        DetailFragment detailFragment = (DetailFragment) getFragmentManager()
                .findFragmentById(R.id.detail_fragment);

        ArrayList<CatalogItem> items = CatalogFragment.getItems();

        // If the detail fragment is not null, then two-pane layout exists
        // and its contents can be updated
        if (detailFragment != null) {

            detailFragment.setItems(items);
            detailFragment.setData(catalogItemIndex);

            // Otherwise the single screen contents must be updated
        } else {

            DetailFragment newDetailFragment = new DetailFragment();

            // Create a bundle for storing the selected item data
            Bundle bundle = new Bundle();
            bundle.putInt(DetailFragment.KEY_POSITION, catalogItemIndex);
            bundle.putParcelable(DetailFragment.KEY_ITEM, items.get(catalogItemIndex));
            newDetailFragment.setArguments(bundle);

            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();

            // Replace the fragment_container contents with the current fragment
            // and add the transaction to backStack
            fragmentTransaction.replace(R.id.fragment_container, newDetailFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

            // single screen details layout should only be in portrait
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

}
