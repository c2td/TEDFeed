package com.application.pm.tedfeed;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/* Fragment class for displaying details view */
public class DetailFragment extends Fragment {

    final static String KEY_POSITION = "position";
    final static String KEY_ITEM = "currentItem";
    final static String KEY_VIDEO_URL = "videoUrl";
    int mCurrentPosition = -1;

    TextView mTitleView;
    TextView mAuthorView;
    TextView mDescriptionView;
    ImageView mImageView;
    Button mButtonPlay;
    View mSeparator;

    List<CatalogItem> mItems;
    CatalogItem mCurrentItem;

    public DetailFragment() {}

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // get the savedInstanceState data
        if (savedInstanceState != null){
            mCurrentPosition = savedInstanceState.getInt(KEY_POSITION);
            mCurrentItem = savedInstanceState.getParcelable(KEY_ITEM);
        }

        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        mTitleView = (TextView) view.findViewById(R.id.details_title);
        mAuthorView = (TextView) view.findViewById(R.id.details_author);
        mDescriptionView = (TextView) view.findViewById(R.id.details_description);
        mButtonPlay = (Button) view.findViewById(R.id.details_button_play);
        mImageView = (ImageView) view.findViewById(R.id.details_image);
        mSeparator = view.findViewById(R.id.separator);

        mItems = new ArrayList<>();
        return view;

    }

    public void setItems(ArrayList<CatalogItem> itemsList) {
        mItems = itemsList;
    }

    /* A method for setting all the details data */
    public void setData(int currentIndex) {

        if (mItems.size() != 0) {
            mCurrentItem = mItems.get(currentIndex);
        }

        mCurrentPosition = currentIndex;

        // Get bitmap from cache (it has already been cashed on catalog display)
        Bitmap bitmap = (Bitmap) Cache.getInstance().getLru().get(currentIndex);
        // Check if bitmap has already been cashed, otherwise run a loading task
        if (bitmap != null) {
            mImageView.setImageBitmap(bitmap);
        } else {
            new LoadImageTask(mCurrentPosition, mCurrentItem.getImageUrl(), mImageView).execute();
        }

        String[] titleParts = mCurrentItem.getTitle().split("\\|");
        mTitleView.setText(titleParts[0]);
        mAuthorView.setText(titleParts[1].trim());
        mSeparator.setVisibility(View.VISIBLE);
        mDescriptionView.setText(mCurrentItem.getDescription());

        mButtonPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent videoIntent = new Intent(getActivity(), VideoActivity.class);
                videoIntent.putExtra(KEY_VIDEO_URL, mCurrentItem.getVideoUrl());
                startActivity(videoIntent);
            }
        });
        mButtonPlay.setVisibility(View.VISIBLE);

    }

    @Override
    public void onStart() {
        super.onStart();

        // Arguments passed to the fragment
        Bundle args = getArguments();

        if (args != null) {

            // get the current item object from parcel
            mCurrentItem = args.getParcelable(KEY_ITEM);

            // set the current list index
            setData(args.getInt(KEY_POSITION));

        } else if (mCurrentPosition != -1) {
            // Set the saved item details on activity reorientation
            setData(mCurrentPosition);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Save the current item data in case the fragment is recreated
        outState.putInt(KEY_POSITION, mCurrentPosition);
        outState.putParcelable(KEY_ITEM, mCurrentItem);
    }
}