package com.example.joyeeta.easyHealth;

import android.app.Application;

import com.firebase.client.Firebase;

/**
 * Created by joyeeta on 1/9/2019.
 */

public class easyHealth extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Firebase.setAndroidContext(this);
    }

}
