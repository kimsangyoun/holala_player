package com.kang.hola.hola.Activity;


import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.kang.hola.hola.R;
import com.kang.hola.hola.Vo.MusicVo;
import com.kang.hola.hola.utils.CircleTransform;
import com.squareup.picasso.Picasso;

import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.id3.AbstractID3v2Tag;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by AppCreater01 on 2016-08-09.
 */
public class Activity_music extends Activity implements View.OnClickListener {

    private ArrayList<MusicVo> list;
    private MediaPlayer mediaPlayer;
    TextView title;

    ImageView album,previous,play,pause,next;
    TextView lyrics;
    SeekBar seekBar;
    boolean isPlaying = true;
    private ContentResolver res;
    private ProgressUpdate progressUpdate;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);
        Intent intent = getIntent();
        mediaPlayer = new MediaPlayer();
        title = (TextView)findViewById(R.id.title);
        album = (ImageView)findViewById(R.id.album);
        lyrics =(TextView)findViewById(R.id.lyrics);
        seekBar = (SeekBar)findViewById(R.id.seekbar);

        position = intent.getIntExtra("position",0);
        list = (ArrayList<MusicVo>) intent.getSerializableExtra("playlist");
        res = getContentResolver();

        previous = (ImageView)findViewById(R.id.pre);
        play = (ImageView)findViewById(R.id.play);
        pause = (ImageView)findViewById(R.id.pause);
        next = (ImageView)findViewById(R.id.next);

        album.setOnClickListener(this);
        lyrics.setOnClickListener(this);
        previous.setOnClickListener(this);
        play.setOnClickListener(this);
        pause.setOnClickListener(this);
        next.setOnClickListener(this);


        File file;
        file = new File( list.get(position).suri);
        Log.i("제목",  list.get(position).suri);
        try {
            MP3File mp3 = (MP3File) AudioFileIO.read(file);
            AbstractID3v2Tag tag2 = mp3.getID3v2Tag();
            list.get(position).slyrics = tag2.getFirst(FieldKey.LYRICS);

        }catch (Exception e){
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
        playMusic(list.get(position));
        progressUpdate = new ProgressUpdate();
        progressUpdate.start();

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                mediaPlayer.pause();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
                if(seekBar.getProgress()>0 && play.getVisibility()==View.GONE){
                    mediaPlayer.start();
                }
            }
        });
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if(position+1<list.size()) {
                    position++;
                    playMusic(list.get(position));
                }
            }
        });
    }
    public void playMusic(MusicVo musicDto) {
        try {
            seekBar.setProgress(0);
            title.setText(musicDto.sartist + " - " + musicDto.stitle);
            Uri musicURI = Uri.withAppendedPath(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, ""+musicDto.sid);
            mediaPlayer.reset();
            mediaPlayer.setDataSource(this, musicURI);
            mediaPlayer.prepare();
            mediaPlayer.start();
            lyrics.setText(list.get(position).slyrics);
            seekBar.setMax(mediaPlayer.getDuration());
            if(mediaPlayer.isPlaying()){
                play.setVisibility(View.GONE);
                pause.setVisibility(View.VISIBLE);
            }else{
                play.setVisibility(View.VISIBLE);
                pause.setVisibility(View.GONE);
            }
            Uri uri = Uri.parse("content://media/external/audio/albumart/" + musicDto.salbumid);
            Picasso.with(this).load(uri).error(R.drawable.music_default).transform(new CircleTransform()).into(album);
        }
        catch (Exception e) {
            Log.e("SimplePlayer", e.getMessage());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.play:
                pause.setVisibility(View.VISIBLE);
                play.setVisibility(View.GONE);
                mediaPlayer.seekTo(mediaPlayer.getCurrentPosition());
                mediaPlayer.start();

                break;
            case R.id.pause:
                pause.setVisibility(View.GONE);
                play.setVisibility(View.VISIBLE);
                mediaPlayer.pause();
                break;
            case R.id.pre:
                if(position-1>=0 ){
                    position--;
                    playMusic(list.get(position));
                    seekBar.setProgress(0);
                }
                break;
            case R.id.next:
                if(position+1<list.size()){
                    position++;
                    playMusic(list.get(position));
                    seekBar.setProgress(0);
                }
                break;

            case R.id.album:
                album.setVisibility(View.INVISIBLE);
                lyrics.setVisibility(View.VISIBLE);
                break;
            case R.id.lyrics:
                album.setVisibility(View.VISIBLE);
                lyrics.setVisibility(View.INVISIBLE);
                break;

        }
    }


    class ProgressUpdate extends Thread{
        @Override
        public void run() {
            while(isPlaying){
                try {
                    Thread.sleep(500);
                    if(mediaPlayer!=null){
                        seekBar.setProgress(mediaPlayer.getCurrentPosition());
                    }
                } catch (Exception e) {
                  //  Log.e("ProgressUpdate",e.getMessage());
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        Log.i("파괴","onDestroy");
        super.onDestroy();
        isPlaying = false;
        if(mediaPlayer!=null){
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
    @Override
    protected void onStop() {
        Log.i("스탑","ONSTOP");
        super.onStop();
    }

    @Override
    protected void onPause() {
        Log.i("멈춤","ONPAUSE");
        super.onPause();

    }
}

