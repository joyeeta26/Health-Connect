package com.example.joyeeta.easyHealth;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class OrgansDonateFragment extends Fragment {

    public OrgansDonateFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.organ_list, container, false);
        final ArrayList<Organ> organs = new ArrayList<>();
        organs.add(new Organ("Lungs", R.drawable.lungs, "General Instructions"));
        organs.add(new Organ("Kidney", R.drawable.kidney, "General Instructions"));
        organs.add(new Organ("Eye", R.drawable.eye, "General Instructions"));
        organs.add(new Organ("Liver", R.drawable.liver, "General Instructions"));
        organs.add(new Organ("Pancreas", R.drawable.pancreas, "General Instructions"));
        organs.add(new Organ("Skin", R.drawable.skin, "General Instructions"));
        organs.add(new Organ("Bones", R.drawable.bone, "General Instructions"));

        OrganAdapter adapter = new OrganAdapter(getActivity(), R.layout.list_item, organs,  R.color.colorPrimaryDark);

        final ListView listView = (ListView) rootView.findViewById(R.id.list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Organ organ = organs.get(position);
                //building an alert dialog box
                LayoutInflater inflater = getActivity().getLayoutInflater();

                //setting up a view for dialog box
                View dialogView = inflater.inflate(R.layout.dialog, null);

                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                // builder.setTitle(location.getLocation_name());

                TextView Title = (TextView) dialogView.findViewById(R.id.title);
                Title.setText(organ.getOrgan_name());

                ImageView mImage = (ImageView) dialogView.findViewById(R.id.dialog_imageview);
                mImage.setImageResource(organ.getImageResourceId());


                TextView Desc = (TextView) dialogView.findViewById(R.id.description);
                Desc.setText(organ.getmDescription());


                builder.setView(dialogView);

                final AlertDialog alertDialog = builder.create();

                alertDialog.show();
            }
        });

        return rootView;
    }
}
