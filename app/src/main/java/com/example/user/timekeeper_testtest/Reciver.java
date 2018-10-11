package com.example.user.timekeeper_testtest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class Reciver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)){
            Intent serviceintent = new Intent(context, BootService.class);
            context.startService(serviceintent);
        }
    }
}
