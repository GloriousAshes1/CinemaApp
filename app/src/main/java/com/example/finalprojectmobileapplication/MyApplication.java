package com.example.finalprojectmobileapplication;

import android.app.Application;
import android.content.Context;

import com.example.finalprojectmobileapplication.prefs.DataStoreManager;
import com.example.finalprojectmobileapplication.prefs.DataStoreManager;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MyApplication extends Application {

    private static final String FIREBASE_URL = "https://cinema-c68f6-default-rtdb.firebaseio.com";

    public static MyApplication get(Context context) {
        return (MyApplication) context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        DataStoreManager.init(getApplicationContext());
        FirebaseApp.initializeApp(this);
    }
}
