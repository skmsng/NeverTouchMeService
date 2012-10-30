package com.example.nevertouchmeservice;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class NeverTouchActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
    
    //サービスの起動
    public void onClickStartButton(View view) {
    	startService(new Intent(this, NeverTouchService.class));
    }
    
    //サービスの停止
    public void onClickStopButton(View view) {
    	stopService(new Intent(this, NeverTouchService.class));
    }
}