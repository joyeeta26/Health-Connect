package com.example.joyeeta.easyHealth;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.api.model.StringList;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import android.support.v4.app.Fragment;

import java.util.Map;

import static com.example.joyeeta.easyHealth.MainActivity._Age;
import static com.example.joyeeta.easyHealth.MainActivity._PhnNo;
import static com.example.joyeeta.easyHealth.MainActivity._bloodChoice;
import static com.example.joyeeta.easyHealth.MainActivity._email;
import static com.example.joyeeta.easyHealth.MainActivity._name;
import static com.example.joyeeta.easyHealth.MainActivity._rBool;
import static com.example.joyeeta.easyHealth.MainActivity._sex;
import static com.example.joyeeta.easyHealth.MainActivity._state;

public class UserAreaActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;

    //private Firebase mRef;
    private DatabaseReference mMessagesDatabaseReference;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mFirebaseAuth;
    private ChildEventListener mChildEventListener;
    String UID;

    View blood;
    View locate;
    View organ;
    View logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_area);

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
                            startActivity(new Intent(UserAreaActivity.this,BookingActivity.class));
                        } else if (id == R.id.Chat) {
                            startActivity(new Intent(UserAreaActivity.this,BookingActivity.class));
                        } else if (id == R.id.DoctorConnect) {
                            startActivity(new Intent(UserAreaActivity.this,BookingActivity.class));
                        } else if (id == R.id.Update) {
                            startActivity(new Intent(UserAreaActivity.this,BookingActivity.class));
                        } else if (id == R.id.Review) {
                            startActivity(new Intent(UserAreaActivity.this,ReviewActivity.class));
                        }


                        return true;
                    }

                });

        mFirebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mFirebaseAuth.getCurrentUser();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mMessagesDatabaseReference = mFirebaseDatabase.getReference();

        locate=(View)findViewById(R.id.bLocate);
        blood=(View)findViewById(R.id.bBlood);
        organ=(View)findViewById(R.id.bOrgan);
        logout=(View)findViewById(R.id.bLogout2) ;

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(UserAreaActivity.this,LoginActivity.class));
            }
        });

        locate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserAreaActivity.this,MapActivity.class));
            }
        });

        blood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserAreaActivity.this,BloodRequest.class));
            }
        });

        organ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTitle("Donate/Receive Organs");
                Fragment fragment = null;
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragment = new OrgansDonateFragment();
                fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
            }
        });

        if(_rBool){
            if (user != null) {
                //code to store stuff in db and use values here
                UID=user.getUid();
                //addChild("Users", UID);
                mFirebaseDatabase.getReference().child("Users");
                mFirebaseDatabase.getReference("Users").child(UID);
               // mMessagesDatabaseReference = mFirebaseDatabase.getReference().child("Users/"+UID);
                //mRef=new Firebase("https://friendly-chat-4fda9.firebaseio.com/Users/"+UID);
                addChild("Name",_name);
                addChild("Mail",_email);
                addChild("Age",_Age);
                addChild("Phone",_PhnNo);
                addChild("Sex",_sex);
                addChild("Blood",_bloodChoice);
                addChild("State",_state);
                //_rBool = false;
            } else {
                // No user is signed in
            }
        }else{
            //stuff already exists, read global variables
            UID=user.getUid();
            mMessagesDatabaseReference = mFirebaseDatabase.getReference("Users/"+UID);
            //mRef= new Firebase("https://friendly-chat-4fda9.firebaseio.com//Users/"+UID);
            mMessagesDatabaseReference.addValueEventListener(new com.google.firebase.database.ValueEventListener() {
                @Override
                public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                    GenericTypeIndicator<Map<String, String>> genericTypeIndicator = new GenericTypeIndicator<Map<String, String>>() {};
                    Map<String, String> map = dataSnapshot.getValue(genericTypeIndicator );
                       //Map<String, String> map = (Map<String, String>) dataSnapshot.getValue();
                            _Age=map.get("Age");
                            _bloodChoice=map.get("Blood");
                            _email=map.get("Mail");
                            _state=map.get("State");
                            _PhnNo=map.get("Phone");
                            _sex=map.get("Sex");
                            _name=map.get("Name");

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
//            mMessagesDatabaseReference.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    Map<String,String> map = dataSnapshot.getValue(Map.class);
//
//                    _Age=map.get("Age");
//                    _bloodChoice=map.get("Blood");
//                    _email=map.get("Mail");
//                    _state=map.get("State");
//                    _PhnNo=map.get("Phone");
//                    _sex=map.get("Sex");
//                    _name=map.get("Name");
//                }
//
//                @Override
//                public void onCancelled(FirebaseError firebaseError) {
//
//                }
//            });

        }
    }

    public void addChild(String key,String value){
       mFirebaseDatabase.getReference("Users/"+UID).child(key).setValue(value);
        //DatabaseReference mRefChild = mFirebaseDatabase.getReference().child(key);
       // Firebase mRefChild = mRef.child(key);
        //mRefChild.setValue(value);
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
}
