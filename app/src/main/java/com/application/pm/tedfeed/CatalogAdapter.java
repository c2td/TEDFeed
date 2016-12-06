package com.application.pm.tedfeed;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/* Adapter class for Catalog list data */
class CatalogAdapter extends ArrayAdapter<CatalogItem> {

    CatalogAdapter(Context context, List<CatalogItem> catalogItems) {
        super(context, 0, catalogItems);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;

        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.catalog_item, parent, false);
        }

        // Get the current item located at that position in the list
        CatalogItem currentItem = getItem(position);

        ImageView imageView = (ImageView) listItemView.findViewById(R.id.list_item_image);
        TextView titleView = (TextView) listItemView.findViewById(R.id.list_item_title);
        TextView authorView = (TextView) listItemView.findViewById(R.id.list_item_author);

        Bitmap bitmap = (Bitmap) Cache.getInstance().getLru().get(position);

        // Check if bitmap has already been cashed, otherwise run a loading task
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        } else {
            new LoadImageTask(position, currentItem.getImageUrl(), imageView).execute();
        }

        // Display the title in two parts
        String[] titleParts = currentItem.getTitle().split("\\|");
        titleView.setText(titleParts[0]);
        authorView.setText(titleParts[1].trim());

        return listItemView;

    }
}
