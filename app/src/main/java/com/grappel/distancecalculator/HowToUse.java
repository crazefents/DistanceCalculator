package com.grappel.distancecalculator;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.google.analytics.tracking.android.EasyTracker;

public class HowToUse extends Activity implements View.OnClickListener {

    @Override
    public void onStart() {
        super.onStart();
        EasyTracker.getInstance(this).activityStart(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EasyTracker.getInstance(this).activityStop(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.howto_layout);
    }

    @Override
    public void onClick(View v) {

    }

}
