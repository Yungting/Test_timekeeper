package com.example.user.timekeeper_testtest.guide;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;

import com.example.user.timekeeper_testtest.R;
import com.example.user.timekeeper_testtest.mainpage;

public class mainpage_splash extends Activity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 判断是否是第一次开启应用
        boolean isFirstOpen = SpUtils.getBoolean(this, AppConstants.FIRST_OPEN);
            // 如果是第一次启动，则先进入功能引导页
            if (!isFirstOpen) {
                Intent intent = new Intent(this, guide_page.class);
                startActivity(intent);
            finish();
            return;
        }

        // 如果不是第一次启动app，则正常显示启动屏
        setContentView(R.layout.mainpage_splash);

        new Handler().postDelayed(new Runnable() {

        @Override
        public void run() {
            enterHomeActivity();
        }
    }, 500);
}

    private void enterHomeActivity() {
        Intent intent = new Intent(this, mainpage.class);
        startActivity(intent);
        finish();
    }
}
