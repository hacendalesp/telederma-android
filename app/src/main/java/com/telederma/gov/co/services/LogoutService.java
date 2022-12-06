package com.telederma.gov.co.services;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.telederma.gov.co.BaseActivity;
import com.telederma.gov.co.MenuActivity;
import com.telederma.gov.co.SplashActivity;
import com.telederma.gov.co.utils.Session;

public class LogoutService extends Service {
    public static CountDownTimer timer;
    String tag = "LogoutService";
    protected Session session;
    @Override
    public void onCreate(){
        super.onCreate();
        session = Session.getInstance(this);


        //timer = new CountDownTimer(4*60 *60 * 1000, 1000) {
        timer = new CountDownTimer(1*40 *60 * 1000, 1000) {
        //timer = new CountDownTimer(15000, 1000) {
            public void onTick(long millisUntilFinished) {
                //Some code
                //Log.v(tag, "Service Started seconds: "+String.valueOf(millisUntilFinished/1000));
            }

            public void onFinish() {
                Log.v(tag, "Call Logout by Service");
                stopSelf();
                //Toast.makeText(getApplicationContext(), "Logout", Toast.LENGTH_SHORT).show();
                session.invalidate();
                startActivity(new Intent(getApplicationContext(), SplashActivity.class));

            }
        };
        //timer.start();
    }

    @Override
    public void onDestroy() {

        timer.cancel();
        Log.i(tag, "Timer cancelled");
        super.onDestroy();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }


    //@Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
