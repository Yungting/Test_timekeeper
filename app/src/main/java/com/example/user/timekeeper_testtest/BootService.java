package com.example.user.timekeeper_testtest;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import java.util.Calendar;

public class BootService extends Service {
    Handler handler = new Handler();

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
//        final Runnable runnable = new Runnable() {
//            @Override
//            public void run() {
        Log.d("service","!!!!");
        DB_normal_alarm db = new DB_normal_alarm(BootService.this);

        //cursor
        if (db != null) {
            Cursor cursor = db.select();
            if (cursor.getCount() > 0) {
                int i = 0;
                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                    Calendar cal = Calendar.getInstance();
                    Long t = Long.parseLong(cursor.getString(6));
                    cal.setTimeInMillis(t);
                    Boolean ifrepeat;
                    if (cursor.getString(4).equals("0")){
                        ifrepeat = false;
                    }else {
                        ifrepeat = true;
                    }
                    int status = cursor.getInt(8);
                    AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                    Intent intent1 = new Intent(BootService.this, normal_alarmalert.class);
                    intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent1.putExtra("requestcode", cursor.getInt(3));
                    PendingIntent pi1 = PendingIntent.getActivity(BootService.this, cursor.getInt(3), intent1, PendingIntent.FLAG_UPDATE_CURRENT);

                    String rday = cursor.getString(0);
                    if (rday != null && !rday.equals("")) {
                        String[] arrays = rday.trim().split("\\s+");
                        int a = 0;
                        int[] d = new int[7];

                        Calendar calendar = Calendar.getInstance();
                        calendar.setTimeInMillis(System.currentTimeMillis());

                        for (String s : arrays) {
                            if (s.equals("Su")) { d[a] = 1; }
                            if (s.equals("M")) { d[a] = 2; }
                            if (s.equals("T")) { d[a] = 3; }
                            if (s.equals("W")) { d[a] = 4; }
                            if (s.equals("Th")) { d[a] = 5; }
                            if (s.equals("F")) { d[a] = 6; }
                            if (s.equals("S")) { d[a] = 7; }
                            a++;
                        }
                        for (int j = 0; j < 7; j++) {
                            if (d[j] == calendar.get(Calendar.DAY_OF_WEEK)) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    Log.d("build", "23up");
                                    Log.d("ifrepeat",":"+ifrepeat);
                                    Log.d("status",":"+status);
                                    if (ifrepeat && status == 1) {
                                        if (System.currentTimeMillis()+5 > Long.parseLong(cursor.getString(6))) {
                                            Log.d("case", ":settmr");
                                            alarm.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, Long.parseLong(cursor.getString(6)) + 24 * 60 * 60 * 1000, pi1);
                                        } else {
                                            Log.d("case", ":settoday");
                                            alarm.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, Long.parseLong(cursor.getString(6)), pi1);
                                        }
                                    }
                                }else {
                                    if (ifrepeat && status == 1) {
                                        if (System.currentTimeMillis()+5 > Long.parseLong(cursor.getString(6))) {
                                            Log.d("case", ":settmr");
                                            alarm.setExact(AlarmManager.RTC_WAKEUP, Long.parseLong(cursor.getString(6)) + 24 * 60 * 60 * 1000, pi1);
                                        } else {
                                            Log.d("case", ":settoday");
                                            alarm.setExact(AlarmManager.RTC_WAKEUP, Long.parseLong(cursor.getString(6)), pi1);
                                            }
                                    }
                                }
                                break;
                            }
                        }
                    }else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            Log.d("build", "23up");
                            if (!ifrepeat && status == 1) {
                                if (System.currentTimeMillis()+5 > Long.parseLong(cursor.getString(6))) {
                                    Log.d("case", ":settmr");
                                    alarm.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, Long.parseLong(cursor.getString(6)) + 24 * 60 * 60 * 1000, pi1);
                                } else {
                                    Log.d("case", ":settoday");
                                    alarm.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, Long.parseLong(cursor.getString(6)), pi1);
                                }
                            }
                        }else {
                            if (!ifrepeat && status == 1) {
                                if (System.currentTimeMillis()+5 > Long.parseLong(cursor.getString(6))) {
                                    Log.d("case", ":settmr");
                                    alarm.setExact(AlarmManager.RTC_WAKEUP, Long.parseLong(cursor.getString(6)) + 24 * 60 * 60 * 1000, pi1);
                                } else {
                                    Log.d("case", ":settoday");
                                    alarm.setExact(AlarmManager.RTC_WAKEUP, Long.parseLong(cursor.getString(6)), pi1);
                                }
                            }
                        }
                    }
                    i++;
                }
            }
        }
        db.close();
        stopService(intent);
////                handler.postDelayed(this, 300000);
////            }
//        };

//        runnable.run();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
