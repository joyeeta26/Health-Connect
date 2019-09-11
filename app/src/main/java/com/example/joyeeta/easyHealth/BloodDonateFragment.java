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

public class BloodDonateFragment extends Fragment {

    public BloodDonateFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.organ_list, container, false);
        final ArrayList<Organ> organs = new ArrayList<>();
        organs.add(new Organ("Blood", R.drawable.blood, "The most common type of donation."));
        organs.add(new Organ("Power Red", R.drawable.powerred, "Best if you're type O or RH- negative."));
        organs.add(new Organ("Platelets", R.drawable.platelets, "Only available at select centers."));
        organs.add(new Organ("AB Plasma", R.drawable.plasma, "Only available at select centers. Donor must be AB Blood type."));
        organs.add(new Organ("About", R.drawable.about, "Eligibility, the process and more"));

        OrganAdapter adapter = new OrganAdapter(getActivity(), R.layout.list_item, organs, R.color.red_background);

        final ListView listView = (ListView) rootView.findViewById(R.id.list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Organ organ = organs.get(position);
                //building an alert dialog box
                LayoutInflater inflater = getActivity().getLayoutInflater();

                //setting up a view for dialog box
                View dialogView = inflater.inflate(R.layout.dialog_blood, null);

                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                // builder.setTitle(location.getLocation_name());

                TextView Title = (TextView) dialogView.findViewById(R.id.title);
                Title.setText(organ.getOrgan_name());

                ImageView mImage = (ImageView) dialogView.findViewById(R.id.dialog_imageview);
                mImage.setImageResource(organ.getImageResourceId());


                TextView Desc = (TextView) dialogView.findViewById(R.id.description);
                Desc.setText(organ.getmDescription());


                builder.setView(dialogView);
                //intent for google maps on click of positive button on dialog box
                builder.setPositiveButton("Locate nearest center", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + Uri.encode("nearest "+organ.getOrgan_name()+" bank"));
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                        mapIntent.setPackage("com.google.android.apps.maps");
                        if (mapIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                            startActivity(mapIntent);
                        }
                    }
                });

                final AlertDialog alertDialog = builder.create();

                alertDialog.show();
            }
        });

        return rootView;
    }
}
