package com.example.user.timekeeper_testtest;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.IBinder;
import android.util.Log;

import java.util.Calendar;

public class BootService extends Service {
    public BootService() {
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
        DB_normal_alarm db = new DB_normal_alarm(this);
        //cursor
        if (db != null) {
            Cursor cursor = db.select();
            if (cursor.getCount() > 0) {
                int i = 0;
                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                    Calendar cal = Calendar.getInstance();
                    Long t = Long.parseLong(cursor.getString(6));
                    cal.setTimeInMillis(t);
                    Boolean ifrepeat = Boolean.parseBoolean(cursor.getString(4));
                    AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                    Intent intent1 = new Intent(this, normal_alarmalert.class);
                    intent1.putExtra("requestcode", cursor.getInt(3));
                    PendingIntent pi1 = PendingIntent.getActivity(this, cursor.getInt(3), intent1, PendingIntent.FLAG_UPDATE_CURRENT);
                    if (ifrepeat) {
                        Log.d("case", ":repear");
                        alarm.setRepeating(AlarmManager.RTC_WAKEUP, Long.parseLong(cursor.getString(6)), 24 * 60 * 60 * 1000, pi1);
                    } else {
                        if (System.currentTimeMillis() > Long.parseLong(cursor.getString(6))) {
                            Log.d("case", ":settmr");
                            alarm.setExact(AlarmManager.RTC_WAKEUP, Long.parseLong(cursor.getString(6)) + 24 * 60 * 60 * 1000, pi1);
                        } else {
                            Log.d("case", ":settoday");
                            alarm.setExact(AlarmManager.RTC_WAKEUP, Long.parseLong(cursor.getString(6)), pi1);
                        }
                    }
                    i++;
                }
            }
        }
        db.close();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
