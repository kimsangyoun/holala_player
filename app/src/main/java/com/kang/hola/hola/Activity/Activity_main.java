package com.kang.hola.hola.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.kang.hola.hola.Adapter.adapter_music;
import com.kang.hola.hola.R;
import com.kang.hola.hola.Vo.MusicVo;

import java.util.ArrayList;

/**
 * Created by AppCreater01 on 2016-08-08.
 */
public class Activity_main extends AppCompatActivity {
    ImageView circleimg;
    ListView listview;
    adapter_music _adapter;
    ArrayList<MusicVo> mv = new ArrayList<MusicVo>();
    final int MY_PERMISSION_REQUEST_STORAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listview = (ListView) findViewById(R.id.main_listview01);
       // circleimg = (ImageView) findViewById(R.id.main_circleimg01);
        _adapter = new adapter_music(this,mv);
        checkPermission();

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String decoding = "ISO-8859-1";
                String encoding = "EUC-KR";

                Toast.makeText(Activity_main.this, "왜안뜸?", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Activity_main.this,Activity_music.class);
                intent.putExtra("position",position);
                intent.putExtra("playlist",mv);
                startActivity(intent);
            }
        });

       // Picasso.with(this).load(R.drawable.beenzinotest)
         //       .transform(new CircleTransform()).into(circleimg);
    }

    private void checkPermission() {
        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
                || checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // Explain to the user why we need to write the permission.
                Toast.makeText(this, "Read/Write external storage", Toast.LENGTH_SHORT).show();
            }

            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSION_REQUEST_STORAGE);


        } else {
            // 퍼미션이 항상 허용일 경우 실행.
            GetMusicList();
        }
    }

    public void GetMusicList(){
        Log.i("뮤직","리스트");
        String path = Environment.getExternalStorageDirectory().getAbsolutePath();


        Cursor cursor=this.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                null,null,null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        if (cursor==null){
            return;
        }
        while (cursor.moveToNext()){
            MusicVo music=new MusicVo();
            music.sid=cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
            music.stitle=cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
            music.sartist=cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
            music.suri=cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
            //music.size=cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE));
            music.slength=ConvertToDate(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION)));
            music.salbumid=cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
            mv.add(music);
        }

        listview.setAdapter(_adapter);
        _adapter.notifyDataSetChanged();
        cursor.close();
    }
    public static String ConvertToDate(String str) {
        int sum=Integer.parseInt(str);
        sum/=1000;
        int minute=sum/60;
        int second=sum%60;
        if (second<10){
            return minute+":0"+second;
        }
        return minute+":"+second;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_REQUEST_STORAGE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    GetMusicList();
                } else {
                    Log.d("TAG", "Permission always deny");
                }
                break;
        }
    }

}
