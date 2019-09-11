package com.example.joyeeta.easyHealth;

import android.content.Context;
import android.location.Location;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Joyeeta on 9/9/2019.
 */

public class OrganAdapter extends ArrayAdapter<Organ> {
    private int mColorResourceId;

    public OrganAdapter(Context context, int resource, List<Organ> objects, int colorResourceId) {
        super(context, 0, objects);
        mColorResourceId = colorResourceId;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View listitemView = convertView;
        if (listitemView == null) {
            listitemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        Organ currentOrgan = getItem(position);
        TextView organnNameTextView = (TextView) listitemView.findViewById(R.id.organ_name);
        organnNameTextView.setText(currentOrgan.getOrgan_name());

        ImageView imageView = (ImageView) listitemView.findViewById(R.id.image);
        if (currentOrgan.hasImage()) {
            imageView.setImageResource(currentOrgan.getImageResourceId());
            imageView.setVisibility(View.VISIBLE);
        } else {
            imageView.setVisibility(View.GONE);
        }

        View textContainer = listitemView.findViewById(R.id.text_container);
        int color = ContextCompat.getColor(getContext(), mColorResourceId);
        textContainer.setBackgroundColor(color);
        return listitemView;

    }
}
