package com.lannasoftware.somehelp.Activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.TextView;

import com.lannasoftware.somehelp.Helper.HelperApp;
import com.lannasoftware.somehelp.R;

public class SplashScreenGoodbye extends AppCompatActivity {

    Context mContext;

    private static int SPLASH_TIME_OUT = 2000;

    TextView txt_message;

    String sUserName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen_goodbye);

        mContext = this;

        Bundle extras = getIntent().getExtras();

        if(extras != null) {
            sUserName = extras.getString("user_name", "");
        }

        txt_message = (TextView) findViewById(R.id.txt_message);

        HelperApp.SetFont(mContext, txt_message,"Roboto-Light.ttf");

        txt_message.setText(getResources().getString(R.string.aurevoir) + " " + sUserName);
        HelperApp.RunAnimation(this, txt_message);

        CloseApp();
    }

    private void CloseApp()
    {
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                finishAffinity();
            }
        }, SPLASH_TIME_OUT);
    }
}
