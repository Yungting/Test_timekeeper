package com.example.user.timekeeper_testtest;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

import java.io.IOException;
import java.util.Calendar;

public class normal_alarmalert extends AppCompatActivity {
    int requestcode;
    MediaPlayer mp;
    String musicpath;
    Handler h = new Handler();
    AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DB_normal_alarm db = new DB_normal_alarm(this);
        Intent intent = getIntent();
        requestcode = intent.getIntExtra("requestcode", 0);
        AudioManager audioManager =(AudioManager)this.getSystemService(Context.AUDIO_SERVICE);
        // Set the volume of played media to your choice.
        audioManager.setStreamVolume (AudioManager.STREAM_MUSIC,10,0);
        Cursor cursor = db.selectbycode(requestcode);
        if (cursor != null && cursor.moveToFirst()){
            musicpath = cursor.getString(1);
        }

        Window win = getWindow();
        win.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        win.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        detectrepeat(requestcode, cursor);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
                //Intent intent1 = new Intent(normal_alarmalert.this, ai_count.class);
                Log.d("alert", "time"+time);
                ai_count data_record = new ai_count();
                data_record.record(normal_alarmalert.this);
                //intent1.putExtra("time", time);
                //normal_alarmalert.this.startActivity(intent1);
                finish();
            }
        });
        builder.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                // Prevent dialog close on back press button
                return keyCode == KeyEvent.KEYCODE_BACK;
            }
        });
        dialog = builder.show();
        builder.show();

    }

    private void alarm(){
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Log.d("code",":"+requestcode);
        long triggertime = System.currentTimeMillis()+300000;
        Intent intent = new Intent(this, normal_alarmalert.class);
        intent.putExtra("requestcode", requestcode);
        PendingIntent op = PendingIntent.getActivity(this, requestcode, intent ,PendingIntent.FLAG_UPDATE_CURRENT);

        am.set(AlarmManager.RTC, triggertime,op);
    }

    public void oneminute(){
        Runnable stopPlaybackRun = new Runnable() {
            public void run(){
                try {
                    alarm();
                    finish();
                    mp.stop();
                }catch(Exception e) {
                    e.printStackTrace();
                }
            }
        };
        h.postDelayed(stopPlaybackRun, 60 * 1000);
    }

    public void detectrepeat(int requestcode, Cursor cursor){
        String rday = cursor.getString(0);
        if (rday != null && !rday.equals("")){
            String[] arrays = rday.trim().split("\\s+");
            int i = 0;
            int[] d = new int[7];

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());

            for(String s : arrays){
                if (s.equals("Su")){ d[i] = 1;}
                if (s.equals("M")){ d[i] = 2;}
                if (s.equals("T")){ d[i] = 3;}
                if (s.equals("W")){ d[i] = 4;}
                if (s.equals("Th")){ d[i] = 5;}
                if (s.equals("F")){ d[i] = 6;}
                if (s.equals("S")){ d[i] = 7;}
                i++;
            }
            for (int j = 0; j<7; j++){
                if (d[j] == calendar.get(Calendar.DAY_OF_WEEK)){
                    Log.d("ring","for if");
                    Log.d("day",":"+d[j]);
                    ring(musicpath);
                    break;
                }
            }
        }else {
            Log.d("ring","else");
            ring(musicpath);
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
}
