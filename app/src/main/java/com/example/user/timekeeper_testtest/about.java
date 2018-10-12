package com.example.user.timekeeper_testtest;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class about extends AppCompatActivity {

    TextView about_content;
    View timekeeper_logo;
    LinearLayout line, mail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);

        //回首頁
        timekeeper_logo = findViewById(R.id.timekeeper_logo);
        timekeeper_logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        about_content = findViewById(R.id.about_content);
        about_content.setText(
                "我們是一群來自高雄大學資訊管理學系的大四生\n " +
                        "這是我們的畢業專題，目前是測試的版本\n " +
                        "我們會蒐集手機使用習慣\n" +
                        "請您一定要打開權限\n" +
                        "時間管家尊重並保護您的資料\n " +
                        "您的所有資料僅在本專題中使用\n" +
                        "不會做於其他用途\n " +
                        "時間管家感謝您的合作：) ");

        line = findViewById(R.id.line);
        line.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri=Uri.parse("https://line.me/R/ti/p/%40xzc4051g");
                Intent i=new Intent(Intent.ACTION_VIEW,uri);
                startActivity(i);
            }
        });


        mail = findViewById(R.id.mail);
        mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri1 = Uri.parse("mailto:timekeepernukim@gmail.com");
                Intent intent2 = new Intent(Intent.ACTION_SENDTO, uri1);
                startActivity(intent2);
            }
        });

    }
}
