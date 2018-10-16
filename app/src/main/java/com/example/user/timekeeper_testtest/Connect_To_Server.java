package com.example.user.timekeeper_testtest;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.List;

public class Connect_To_Server {
    //private final static String mUrl = "http://192.168.43.42:80/TimeKeeper/index.php";
    private final static String mUrl = "http://140.127.218.207:80/Connecter.php";
    public String get_data = "";
    public static Boolean internet_connect = false;
    public void connect(final String sqltype, final String sql){
        Thread connecting = new Thread(new Runnable() {
            @Override
            public void run() {
                Message msg = new Message();
                Bundle data = new Bundle();
                msg.setData(data);
                //
                // TODO: http request.
                //
                try
                {
                    //連線到 url網址
                    HttpClient httpclient = new DefaultHttpClient();
                    HttpPost method = new HttpPost(mUrl);

                    //傳值給PHP
                    List<NameValuePair> vars=new ArrayList< NameValuePair>();
//                    vars.add(new BasicNameValuePair(sqltype,sql));
                    vars.add(new BasicNameValuePair(sqltype,sql));
                    method.setEntity(new UrlEncodedFormEntity(vars, org.apache.http.protocol.HTTP.UTF_8));

                    //接收PHP回傳的資料
                    HttpResponse response = httpclient.execute(method);
                    HttpEntity entity = response.getEntity();

                    if(entity != null){
                        data.putString("key", EntityUtils.toString(entity,"utf-8"));//如果成功將網頁內容存入key
                        get_data = data.getString("key");
                        handler_Success.sendMessage(msg);
                    }
                    else{
                        data.putString("key","無資料");
                        handler_Nodata.sendMessage(msg);
                    }
                }
                catch(Exception e){
                    data.putString("key","連線失敗");
                    handler_Error.sendMessage(msg);
                }

            }
        });
        connecting.start();
        try {
            connecting.join();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    Handler handler_Success = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String val = data.getString("key");//取出key中的字串存入val
            Log.d("上傳Server","成功"+val);
            internet_connect = true;
            //data_txt.setText(val);
        }
    };

    Handler handler_Error = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String val = data.getString("key");
            Log.d("上傳Server","失敗"+val);
            internet_connect = false;

        }
    };

    Handler handler_Nodata = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String val = data.getString("key");
            Log.d("上傳Server","失敗"+val);
            internet_connect = false;

        }
    };
}
