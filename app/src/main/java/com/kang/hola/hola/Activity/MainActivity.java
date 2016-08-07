package com.kang.hola.hola.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kang.hola.hola.Play;
import com.kang.hola.hola.R;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends Activity {
    ListView listview;
    MusicLIstAdapter adapter;
    ArrayList<String> item;
    File[] filelist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        item = new ArrayList<>();

        String path = Environment.getExternalStorageDirectory().getAbsolutePath();
        File root = new File(path);
        filelist = root.listFiles();
        Toast.makeText(MainActivity.this,path+" and " + root.listFiles().length,Toast.LENGTH_LONG).show();
        for(int i = 0; i < filelist.length; i++){
            if(filelist[i].toString().contains(".mp3"))
            {
              item.add(filelist[i].toString());
            }
        }
        listview = (ListView) findViewById(R.id.listView);



        adapter = new MusicLIstAdapter(this,R.layout.item,item);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent startPlay = new Intent(getApplicationContext(), Play.class);
                startActivity(startPlay);
            }
        });

    }


    class MusicLIstAdapter extends BaseAdapter { //BaseAdapter를 상속받은 어댑터이다. 추상메소드는 반드시 구현한다.
        Context maincon;
        LayoutInflater Inflater;
        ArrayList<String> musicdata;
        int layout;

        public MusicLIstAdapter(Context context, int alayout, ArrayList<String> aarSrc){//생성자  콘텍스트와 설정할 단위레이아웃, 정보를 받아온다.
            maincon = context;
            Inflater = (LayoutInflater)context.getSystemService(// 시스템 레벨의 서비스를 제어할 수 있는 핸들을// 리턴
                    Context.LAYOUT_INFLATER_SERVICE);//레이아웃 리소스를 인플레트한다.
            musicdata = aarSrc;
            layout = alayout;
        }

        public int getCount(){//어댑터가 관리할 데이터의 개수를 설정
            return musicdata.size();
        }

        public String getItem(int position){//어댑터가 관리하는 데이터의 Item의 position을 <객체>형태(여기서는 string)형태로 얻어온다.
            return musicdata.get(position);
        }

        public long getItemId(int position){//어댑터가 관리하는 데이터의 ITem의 포지션 값의 ID를 가져온다.
            return position;
        }

        //--------------------------------------------[LISTVIEW에 뿌려질 한 줄의 ROW를 설정한다.][시작]------------------------------------------------
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final int pos = position;

            if(convertView == null){
                convertView = Inflater.inflate(layout, parent, false);//아이템 단위 레이아웃을 부모레이아웃에다가 참조함 /false:레이아웃 파라미터 값만 참조
            }
            TextView musicinfo = (TextView) convertView.findViewById(R.id.musictext);
            musicinfo.setText(musicdata.get(position));

            return convertView;//컨버트 뷰 리턴
        }

        @Override
        public void notifyDataSetChanged() {//새로고침함
            super.notifyDataSetChanged();
        }

    }
    //--------------------------------------[LISTVIEW에 뿌려질 한 줄의 ROW를 설정한다.][끝]---------------------------------------------

}
