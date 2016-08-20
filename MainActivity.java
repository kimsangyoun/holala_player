package com.kang.hola.vidio_test;

import android.app.Activity;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

public class MainActivity extends Activity {
    VideoView view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        view = (FullView) findViewById(R.id.videoView);
        MediaController mediaController = new MediaController(this);

        //비디오 뷰에 연결
        mediaController.setAnchorView(view);

        Uri uri = Uri.parse("android.resource://"+getPackageName()+"/raw/"+"hologram_video_test");

        view.setMediaController(mediaController);//뷰를 미디어 컨트롤러 사용
        view.setVideoURI(uri);
        view.requestFocus();
        view.start();//재생한다.

    }
}
