package com.example.user.timekeeper_testtest;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.io.File;

public class normal_alarm_music extends Activity {
    ListView music_list;
    TextView music_list1;
    View view2;
    String index = "Default";
    Cursor cursor, cursor1;
    int select_item = -1;
    int rcode;
    int[] pois = new int[300];
    int orignalpois = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.normal_alarm_music);

        String selection = MediaStore.Audio.Media.DURATION+">5000";
        cursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null,
                selection, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        String[] str = new String[]{MediaStore.Audio.Media.TITLE , MediaStore.Audio.Media.ARTIST};
        int[] displayViews = new int[]{R.id.music_name};
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.music_item, cursor, str, displayViews, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

        Intent intent1 = getIntent();
        if (intent1 != null){
            rcode = intent1.getIntExtra("rcode",0);
            String path = intent1.getStringExtra("audiopath");
            DB_normal_alarm db = new DB_normal_alarm(this);
            cursor1 = db.selectbycode(rcode);
            int i = 0;
            if (cursor1 != null && cursor1.moveToFirst()){
                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
                    pois[i] = i;
                    int file = cursor.getColumnIndex(MediaStore.Audio.Media.DATA);
                    String audiopath = cursor.getString(file);
                    if (audiopath.equals(cursor1.getString(1))){
                        orignalpois = i;
                    }
                    i++;
                }
            }else{
                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
                    pois[i] = i;
                    int file = cursor.getColumnIndex(MediaStore.Audio.Media.DATA);
                    String audiopath = cursor.getString(file);
                    if (audiopath.equals(path)){
                        orignalpois = i;
                    }
                    i++;
                }
            }
        }

        // listview
        music_list = findViewById(R.id.music_list);
        music_list1 = findViewById(R.id.music_list1);
        music_list.setAdapter(adapter);

        if (orignalpois >= 0){
            music_list.setSelection(orignalpois);
            select_item = orignalpois;
        }else{
            music_list1.setBackgroundColor(getResources().getColor(R.color.background_gray));
        }

        music_list1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setBackgroundColor(Color.GRAY);
                if (select_item != -1){
                    view2.setBackgroundColor(Color.WHITE);
                }
                select_item = -1;
            }
        });

        music_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, final View view, final int i, long l) {
                //點選某個item並呈現被選取的狀態
                select_item = i;
                index = cursor.getString(cursor.getColumnIndex("TITLE"));
                music_list1.setBackgroundColor(Color.WHITE);
                view2 = view;

            }
        });

        Button music_apply = findViewById(R.id.music_apply);
        music_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString("index", index);
                bundle.putString("from", "music");
                if (cursor.moveToPosition(select_item)) {

                    int fileColumn = cursor.getColumnIndex(MediaStore.Audio.Media.DATA);
                    int mimeTypeColumn = cursor.getColumnIndex(MediaStore.Audio.Media.MIME_TYPE);

                    String audioFilePath = cursor.getString(fileColumn);
                    String mimeType = cursor.getString(mimeTypeColumn);

                    File newFile = new File(audioFilePath);
                    bundle.putString("audioFilePath", audioFilePath);
                    bundle.putString("mimeType", mimeType);
                }
                intent.putExtras(bundle);
                setResult(2, intent); //requestCode需跟A.class的一樣
                finish();
            }
        });

    }
}
