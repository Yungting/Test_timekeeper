package com.example.user.timekeeper_testtest;


import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class AlertService extends Service {

    public AlertService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        Log.i("brad", "Service:onBind()");
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("service","!!!!");
        ai_count data_record = new ai_count();
        data_record.record(this);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
