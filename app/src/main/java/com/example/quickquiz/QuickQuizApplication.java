package com.example.quickquiz;

import android.app.Application;
import com.google.firebase.database.FirebaseDatabase;

public class QuickQuizApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}