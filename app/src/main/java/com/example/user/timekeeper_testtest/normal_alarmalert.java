package com.example.user.timekeeper_testtest;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import java.io.IOException;
import java.util.Calendar;

public class normal_alarmalert extends AppCompatActivity {
    int requestcode;
    MediaPlayer mp;
    String musicpath;
    Handler h = new Handler();
    AlertDialog dialog;
    private MyReceiver receiver;
    int state = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alert);
        DB_normal_alarm db = new DB_normal_alarm(this);
        Intent intent = getIntent();
        requestcode = intent.getIntExtra("requestcode", 0);
        Log.d("request",":"+requestcode);
        AudioManager audioManager =(AudioManager)this.getSystemService(Context.AUDIO_SERVICE);
        // Set the volume of played media to your choice.
        audioManager.setStreamVolume (AudioManager.STREAM_MUSIC,10,0);
        Cursor cursor = db.selectbycode(requestcode);
        if (cursor != null && cursor.moveToFirst()){
            musicpath = cursor.getString(1);
            state = cursor.getInt(8);
        }

        receiver = new MyReceiver();
        IntentFilter homeFilter = new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);

        registerReceiver(receiver, homeFilter);

        Window win = getWindow();
        win.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        win.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        detectrepeat(requestcode, cursor);
        db.updatestate(requestcode, state);
        db.close();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
        if (mp!=null){
            if (mp.isPlaying()){
                mp.stop();
            }
            mp.release();
        }
        if (dialog != null && dialog.isShowing()){
            Log.d("destory",":set");
            dialog.dismiss();
            dialog.cancel();
        }
        Intent service = new Intent(this, BootService.class);
        service.putExtra("req",requestcode);
        startService(service);
    }

    public void send(){
        Intent intent3 = new Intent(this, AlertService.class);
        this.startService(intent3);
    }

    public void alarmDialog(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("WAKE UP NOW!!");
        builder.setPositiveButton("LATER", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alarm();
                h.removeCallbacksAndMessages(null);
                finish();
            }
        });
        builder.setNegativeButton("CLOSED", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mp.stop();
                h.removeCallbacksAndMessages(null);
                Calendar cd = Calendar.getInstance();
                cd.setTimeInMillis(System.currentTimeMillis());
                long time = cd.getTimeInMillis();
                Log.d("alert", "time"+time);
                ai_count.clock_count++;//1017
                send();
                finish();
            }
        });

        dialog = builder.show();
        builder.show();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
    }

    private void alarm(){
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Log.d("code",":"+requestcode);
        long triggertime = System.currentTimeMillis()+300000;
        Intent intent = new Intent(this, normal_alarmalert.class);
        intent.putExtra("requestcode", requestcode);
        PendingIntent op = PendingIntent.getActivity(this, 1, intent ,PendingIntent.FLAG_UPDATE_CURRENT);

        am.setExact(AlarmManager.RTC, triggertime, op);
    }

    public void oneminute(){
        Runnable stopPlaybackRun = new Runnable() {
            public void run(){
                try {
                    alarm();
                    mp.stop();
                    System.exit(0);
                }catch(Exception e) {
                    e.printStackTrace();
                }
            }
        };
        h.postDelayed(stopPlaybackRun, 60 * 1000);
    }

    public void detectrepeat(int requestcode, Cursor cursor) {
        Boolean ifrepeat;
        if (cursor.getString(4).equals("0")) {
            ifrepeat = false;
        } else {
            ifrepeat = true;
        }
        if (ifrepeat) {
            ring(musicpath);
        } else {
            Log.d("ring", "else");
            ring(musicpath);
            state = 0;
        }
    }

    public void ring(String musicpath){
        mp = new MediaPlayer();
        if (musicpath == null){
            mp = MediaPlayer.create(this, R.raw.test);
            mp.setLooping(true);
            mp.setVolume(1.0f, 1.0f);
            mp.start();
            oneminute();
            alarmDialog();
        }else {
            try {
                mp.setDataSource(musicpath);
                mp.prepare();
                mp.setVolume(1.0f, 1.0f);
                mp.start();
                oneminute();
                alarmDialog();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class MyReceiver extends BroadcastReceiver {

        private final String SYSTEM_DIALOG_REASON_KEY = "reason";
        private final String SYSTEM_DIALOG_REASON_HOME_KEY = "homekey";
        private final String SYSTEM_DIALOG_REASON_RECENT_APPS = "recentapps";

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
                String reason = intent.getStringExtra(SYSTEM_DIALOG_REASON_KEY);

                if (reason == null)
                    return;

                // Home键
                if (reason.equals(SYSTEM_DIALOG_REASON_HOME_KEY)) {
                    Toast.makeText(getApplicationContext(), "關閉鬧鐘", Toast.LENGTH_SHORT).show();
                    mp.stop();
                    h.removeCallbacksAndMessages(null);
                    Calendar cd = Calendar.getInstance();
                    cd.setTimeInMillis(System.currentTimeMillis());
                    long time = cd.getTimeInMillis();
                    Log.d("alert", "time"+time);
                    send();
                    finish();
                }

                // 最近任务列表键
                if (reason.equals(SYSTEM_DIALOG_REASON_RECENT_APPS)) {

                }
            }
        }
    }

}
