package com.example.user.timekeeper_testtest;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class sign_up extends AppCompatActivity {
    Connect_To_Server db_select;
    Connect_To_Server db_insert;
    JSONArray get_result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        db_select = new Connect_To_Server();
        db_insert = new Connect_To_Server();

        Button signup_btn = findViewById(R.id.signup_btn);
        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText user_mail= (EditText)findViewById(R.id.user_mail);
                EditText user_pwd = (EditText)findViewById(R.id.user_pwd);
                EditText check_pwd = (EditText)findViewById(R.id.check_pwd);
                EditText user_name = (EditText)findViewById(R.id.user_name);
                final String u_id = user_mail.getText().toString();
                String u_pwd = check_pwd.getText().toString();
                String u_name = user_name.getText().toString();
                if(!android.util.Patterns.EMAIL_ADDRESS.matcher(u_id).matches()){
                    new AlertDialog.Builder(sign_up.this).setTitle("輸入錯誤").setMessage("帳號請填E-mail喔~~")
                            .setNegativeButton("OK",null)
                            .show();
                }else {
                    if (user_pwd.getText().toString().equals(check_pwd.getText().toString())) {

                        Thread get_data = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                db_select.connect("select_sql", "SELECT user_id,u_password,u_name FROM `user` WHERE user_id ='" + u_id + "'");
                                Log.d("連線", "安安SELECT user_id,u_password,u_name FROM `user` WHERE user_id ='" + u_id + "'");
                            }
                        });
                        get_data.start();
                        try {
                            Thread.sleep(400);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Log.d("連線", "安安" +db_select.get_data);
                        String db_u_id = null;
                        try{
                            get_result = new JSONArray(db_select.get_data);
                            int lenght = get_result.length();
                            for(int i = 0;i < lenght;i++){
                                JSONObject jsonObject = get_result.getJSONObject(i);
                                db_u_id = jsonObject.getString("user_id");
                            }
                        }
                        catch(JSONException e) {
                            e.printStackTrace();
                        }
                        if (db_u_id != null) {
                            new AlertDialog.Builder(sign_up.this).setTitle("輸入錯誤").setMessage("已有人註冊此帳號")
                                    .setNegativeButton("OK", null)
                                    .show();
                        } else {
                            db_insert.connect("insert_sql", "INSERT INTO `user` (`user_id`, `u_name`, `u_password`) VALUES('" + u_id + "', '" + u_name + "', '" + u_pwd + "');");
                            new AlertDialog.Builder(sign_up.this).setTitle("註冊成功").setMessage("快去登入吧!!")
                                    .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            finish();
                                        }
                                    })
                                    .show();
                        }
                    } else {
                        new AlertDialog.Builder(sign_up.this).setTitle("輸入錯誤").setMessage("兩組密碼不一樣")
                                .setNegativeButton("OK", null)
                                .show();
                    }
                }
            }
        });
    }

    //點擊空白處隱藏鍵盤
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {

            // 获得当前得到焦点的View，一般情况下就是EditText（特殊情况就是轨迹求或者实体案件会移动焦点）
            View v = getCurrentFocus();

            if (isShouldHideInput(v, ev)) {
                hideSoftInput(v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    private boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = { 0, 0 };
            v.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left
                    + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditView上，和用户用轨迹球选择其他的焦点
        return false;
    }

    private void hideSoftInput(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    // 按返回鍵取消delete狀態
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return false;
    }
}
