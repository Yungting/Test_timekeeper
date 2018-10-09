package com.example.user.timekeeper_testtest;

import android.media.MediaRecorder;
import android.util.Log;

import java.io.File;
import java.io.IOException;

public class Get_Sound {
    public static MediaRecorder mMediaRecorder;
    //public static long time;
    private  final String TAG = "MediaRecord" ;
    public  static  final  int MAX_LENGTH = 1000 * 60 * 10; // 最大錄音時長1000*60*10;
    private String filePath;
    public static double db;
    public Get_Sound(){
        this .filePath = "/dev/null" ;
    }

    public Get_Sound(File file) {
        this .filePath = file.getAbsolutePath();
    }

    //public static long startTime;
    //private static long endTime;
    public void startRecord() {

        if (mMediaRecorder == null) {
            mMediaRecorder = new MediaRecorder();
        }
        try {
            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC); // 設置麥克風
            mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
            mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mMediaRecorder.setOutputFile(filePath);
            mMediaRecorder.setMaxDuration(MAX_LENGTH);
            if(android.os.Build.VERSION. SDK_INT > android.os.Build.VERSION_CODES. LOLLIPOP_MR1 ){
                mMediaRecorder.setAudioSamplingRate(11025);
            }
            mMediaRecorder.prepare ();
            mMediaRecorder.start();

            //startTime = System.currentTimeMillis();
            Log.d("錄音", "開始");
            //updateMicStatus();
        } catch (IllegalStateException e) {
            Log.i(TAG,
                    "call startAmr(File mRecAudioFile) failed!"+e.getMessage());
        } catch (IOException e) {
            Log.i(TAG,"call startAmr(File mRecAudioFile) failed!"+e.getMessage());
        }
    }
    public void stopRecord() {
        //time = 0;
        if (mMediaRecorder == null) {
            //time = 0;
        }else{
            // endTime = System.currentTimeMillis();
            //Log.i("ACTION_END", "endTime" + endTime);
            mMediaRecorder.stop();
            mMediaRecorder.reset();
            mMediaRecorder.release();
            mMediaRecorder = null;
            //Log.i("ACTION_LENGTH", "Time" + (endTime - startTime));
            //time = (endTime - startTime)/1000;
        }
    }
}
