package com.example.joyeeta.easyHealth;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.app.Dialog;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.example.joyeeta.easyHealth.MainActivity._state;
import static com.example.joyeeta.easyHealth.MainActivity._tBlood;

public class BloodRequest extends AppCompatActivity {

    LocationManager locationManager;
    //protected LocationListener locationListener;
    Context context;
    String lat;
    String provider;
    protected String latitude, longitude;
    protected boolean gps_enabled, network_enabled;

    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;

    Button submit;
    Button bBlood;
    Button pBlood;
    EditText etUnits;
    RadioGroup platelets;
    int plateletsChoice;
    String plateChoice;
    Spinner bloodGroupSpinner;

    String units;
    String bGrp;

    //Firebase mRef;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mMessagesDatabaseReference, cMessagesDatabaseReference;

    String UID;
    FirebaseUser user;
    String value;
    double lati=-1,lngi;
    int ct =0;
    boolean done = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood);
        context = this;

        mDrawerLayout =(DrawerLayout) findViewById(R.id.drawer_layout);
        mNavigationView = (NavigationView) findViewById(R.id.nav_view);

        mNavigationView.setItemIconTintList(null);

        //Set the toolbar as the action bar
        final Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Add the nav drawer button
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_navicon);

        mNavigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        //set action bar title
                        //setTitle(menuItem.getTitle());
                        // close drawer when item is tapped
                        mDrawerLayout.closeDrawers();
                        int id = menuItem.getItemId();
                        if (id == R.id.Book) {
                            startActivity(new Intent(BloodRequest.this,BookingActivity.class));
                        } else if (id == R.id.Chat) {
                            startActivity(new Intent(BloodRequest.this,BookingActivity.class));
                        } else if (id == R.id.DoctorConnect) {
                            startActivity(new Intent(BloodRequest.this,BookingActivity.class));
                        } else if (id == R.id.Update) {
                            startActivity(new Intent(BloodRequest.this,BookingActivity.class));
                        } else if (id == R.id.Review) {
                            startActivity(new Intent(BloodRequest.this,ReviewActivity.class));
                        }


                        return true;
                    }

                });

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mMessagesDatabaseReference = mFirebaseDatabase.getReference();
        cMessagesDatabaseReference = mFirebaseDatabase.getReference();
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 1200, locationListenerGPS);

       // locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

        mFirebaseDatabase.getReference("Users").child(_state);
        mFirebaseDatabase.getReference("Users/"+_state).child("Blood");

        cMessagesDatabaseReference = mFirebaseDatabase.getReference("Users/"+_state+"/Blood").child("Count");
        cMessagesDatabaseReference.setValue(Integer.toString(ct));

        pBlood = (Button)findViewById(R.id.prBlood);
        pBlood.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(BloodRequest.this);
                dialog.setTitle("Request for Blood");
                dialog.setContentView(R.layout.r_blood);
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialog.getWindow().getAttributes());
                lp.width=WindowManager.LayoutParams.MATCH_PARENT;
                dialog.show();

                etUnits = (EditText) dialog.findViewById(R.id.etUnits);
                platelets = (RadioGroup) dialog.findViewById(R.id.plateletsRadioGroup);
                plateletsChoice = platelets.getCheckedRadioButtonId();
                switch (plateletsChoice) {
                    case R.id.yes:
                        //some code
                        plateChoice="True";
                        break;
                    case R.id.no:
                        //some code
                        plateChoice="False";
                        break;
                }


//***********************************************************************************************
//                mMessagesDatabaseReference.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        value = dataSnapshot.getValue(String.class);
//                        ct=Integer.parseInt(value);
//                    }
//
//                    @Override
//                    public void onCancelled(FirebaseError firebaseError) {
//
//                    }
//                });
//************************************************************************************************


                submit = (Button)dialog.findViewById(R.id.submit);
                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //update db
                        _tBlood=true;
                        bloodGroupSpinner = (Spinner) dialog.findViewById(R.id.bloodGroupSpinner);
                        units=etUnits.getText().toString();
                        user = FirebaseAuth.getInstance().getCurrentUser();
                        UID=user.getUid();
                        bGrp=String.valueOf(bloodGroupSpinner.getSelectedItem());

                        mFirebaseDatabase.getReference("Users/"+_state+"/Blood").child("Donors");
                        //mFirebaseDatabase.getReference("Users/"+_state+"/Blood/Donors");
                        //mRef=new Firebase("https://friendly-chat-4fda9.firebaseio.com/Users/"+_state+"/Blood/Donors");
                        if(lati!=-1){
                            dialog.dismiss();
                            String s=Double.toString(lati)+" "+ Double.toString(lngi);
                            ct++;
                            value=Integer.toString(ct);
                            addChild(value,s);
                            mFirebaseDatabase.getReference("Users/"+_state+"/Blood/Count").setValue(value);
                            //Firebase f=new Firebase("https://friendly-chat-4fda9.firebaseio.com/Users/"+_state+"/Blood/Count");
                            //f.setValue(value);
                        }else{
                            //Toast.makeText(Request.this,"Please turn on GPS to add your request",Toast.LENGTH_LONG).show();
                            isLocationEnabled();
                        }

                    }
                });

            }

        });

        bBlood = (Button)findViewById(R.id.dBlood);
        bBlood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTitle("Donate Blood");
                Fragment fragment = null;
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragment = new BloodDonateFragment();
                fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
            }
        });

        cMessagesDatabaseReference.addValueEventListener(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                value = dataSnapshot.getValue(String.class);
                ct=Integer.parseInt(value);
                cMessagesDatabaseReference.setValue(Integer.toString(ct));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    LocationListener locationListenerGPS = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            lati = location.getLatitude();
            lngi = location.getLongitude();
            done = true;
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}

        @Override
        public void onProviderEnabled(String provider) {}

        @Override
        public void onProviderDisabled(String provider) {}
    };

    public void addChild(String key,String value){
        mFirebaseDatabase.getReference("Users/"+_state+"/Blood/Donors").child(key).setValue(value);
//        DatabaseReference mRefChild = mFirebaseDatabase.getReference().child(key);
//        //Firebase mRefChild = mRef.child(key);
//        mRefChild.setValue(value);
    }

    private void isLocationEnabled() {
        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
            alertDialog.setTitle("Enable Location");
            alertDialog.setMessage("Your locations setting is not enabled. Please enabled it in settings menu.");
            alertDialog.setPositiveButton("Location Settings", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }
            });
            alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            AlertDialog alert = alertDialog.create();
            alert.show();
        }
//        else {
//            final AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
//            alertDialog.setTitle("Confirm Location");
//            alertDialog.setMessage("Your location is enabled. Please enjoy!!");
//            alertDialog.setNegativeButton("Back to interface", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    dialog.cancel();
//                }
//            });
//            AlertDialog alert = alertDialog.create();
//            alert.show();
//        }
    }

    //open the drawer when the button is tapped
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
//    @Override
//    public void onLocationChanged(Location location) {
//        lati=location.getLatitude();
//        lngi=location.getLongitude();
//        done=true;
//    }
//
//    @Override
//    public void onProviderDisabled(String provider) {
//    }
//
//    @Override
//    public void onProviderEnabled(String provider) {
//    }
//
//    @Override
//    public void onStatusChanged(String provider, int status, Bundle extras) {
//    }
}
