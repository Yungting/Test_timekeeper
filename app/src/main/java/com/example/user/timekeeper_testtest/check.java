package com.example.user.timekeeper_testtest;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.example.user.timekeeper_testtest.mainpage.KEY;

public class check extends AppCompatActivity {
    private MyAdapter mAdapter;
    private RecyclerView mRecyclerView;
    Connect_To_Server connecting = new Connect_To_Server();
    String u_id;
    private DB_usage dbUsage;
    DB_usage db;
    ArrayList<String> dbdate = new ArrayList<>();
    View timekeeper_logo;
    TextView nothing_text;
    ArrayList<Integer> period = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check);

        u_id = getSharedPreferences(KEY, MODE_PRIVATE).getString("u_id", null);
        nothing_text = findViewById(R.id.nothing_text);

        //回首頁
        timekeeper_logo = findViewById(R.id.timekeeper_logo);
        timekeeper_logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.close();
                Intent mIntent = new Intent(check.this, mainpage.class);
                mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                finish();
                startActivity(mIntent);
            }
        });

        // 設定資料內容
        ArrayList<String> myDateset = new ArrayList<>();
        ArrayList<String> myTimeset = new ArrayList<>();
        db = new DB_usage(this);
        if (db != null) {
            Cursor cursor = db.select();
            if (cursor.getCount() > 0) {
                int i = 0;
                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                    if (cursor.getString(3) == null) {
                        Calendar cal = Calendar.getInstance();
                        Long t = Long.parseLong(cursor.getString(1));
                        cal.setTimeInMillis(t);
                        int hour = cal.get(Calendar.HOUR_OF_DAY);
                        int min = cal.get(Calendar.MINUTE);
                        int day = cal.get(Calendar.DAY_OF_MONTH);
                        int month = cal.get(Calendar.MONTH) + 1;
                        String time = "";
                        String date = month + "/" + day;
                        if (hour < 10 && min < 10) {
                            time = "0" + hour + ":0" + min;
                        } else if (hour < 10 && min >= 10) {
                            time = "0" + hour + ":" + min;
                        } else if (hour >= 10 && min < 10) {
                            time = hour + ":0" + min;
                        } else if (hour >= 10 && min >= 10) {
                            time = hour + ":" + min;
                        }
                        i++;
                        myDateset.add(date);
                        myTimeset.add(time);
                        dbdate.add(cursor.getString(1));
                        period.add(cursor.getInt(2));
                    }
                }
            }
        }
        if (myDateset.size() == 0 && myTimeset.size() == 0){
            nothing_text.setVisibility(View.VISIBLE);
            nothing_text.setText("您還沒設置過鬧鐘\n" +
                    "或是您已經都點擊完畢了~\n" +
                    "所以目前沒有資訊喔！");
        }

        mAdapter = new MyAdapter(myDateset, myTimeset);
        mRecyclerView = findViewById(R.id.list);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);

    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
        private List<String> mTime;
        private List<String> mDate;

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView date;
            public TextView time;

            public ViewHolder(View v) {
                super(v);
                date = v.findViewById(R.id.date);
                time = v.findViewById(R.id.time);
            }
        }

        public MyAdapter(List<String> time, List<String> date) {
            mTime = time;
            mDate = date;
        }


        @Override
        public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.check_item, parent, false);
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        //點擊效果
        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            // 資料寫入date與time
            holder.date.setText(mDate.get(position));
            holder.time.setText(mTime.get(position));

            Button btn_sleep = holder.itemView.findViewById(R.id.btn_sleep);
            Button btn_awake = holder.itemView.findViewById(R.id.btn_wake);
            final TextView showtext = holder.itemView.findViewById(R.id.showtext);
            showtext.setText("");

            btn_awake.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("u_id", ":" + u_id);
                    Log.d("date", ":" + dbdate.get(position));
                    String dbdb = dbdate.get(position);
                    db.ifawake(u_id, dbdb, true);
                    showtext.setText("成功叫醒，感謝您的確認!!");
                    if (isNetworkConnected(getBaseContext())) {
                        if (db != null) {
                            Cursor cursor = db.select();
                            if (cursor.getCount() > 0) {
                                int i = 0;
                                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                                    if (cursor.getString(3) != null) {
                                        String sql_u = "UPDATE `screen_record` SET `r_ifawake`="
                                                + true + " WHERE `User_id`='" + u_id + "' AND `Date`='" + cursor.getString(1) + "'";
                                        connecting.connect("insert_sql", sql_u);
                                        String sql_i = "INSERT INTO  `screen_record`(User_id, Date, Period, r_ifawake) VALUES ('" + u_id + "', '" + cursor.getString(1) + "', " + cursor.getInt(2) + ", " + true + ")";
                                        connecting.connect("insert_sql", sql_i);
                                        db.delete(u_id, cursor.getString(1));
                                    }
                                }
                            }
                        }
                    }
                }
            });

            btn_sleep.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    db.ifawake(u_id, dbdate.get(position), false);
                    showtext.setText("沒有叫醒，感謝您的確認!!");
                    if (isNetworkConnected(getBaseContext())) {
                        if (db != null) {
                            Cursor cursor = db.select();
                            if (cursor.getCount() > 0) {
                                int i = 0;
                                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                                    if (cursor.getString(3) != null) {
                                        String sql_u = "UPDATE `screen_record` SET `r_ifawake`="
                                                + false + " WHERE `User_id`='" + u_id + "' AND `Date`='" + cursor.getString(1) + "'";
                                        connecting.connect("insert_sql", sql_u);
                                        String sql_i = "INSERT INTO  `screen_record`(User_id, Date, Period, r_ifawake) VALUES ('" + u_id + "', '" + cursor.getString(1) + "', " + cursor.getInt(2) + ", " + false + ")";
                                        connecting.connect("insert_sql", sql_i);
                                        db.delete(u_id, cursor.getString(1));
                                    }
                                }
                            }
                        }
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mDate.size();
        }
    }


        public boolean isNetworkConnected(Context context) {
            if (context != null) {
                ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
                if (mNetworkInfo != null) {
                    return mNetworkInfo.isAvailable();
                }
            }
            return false;
        }
}
