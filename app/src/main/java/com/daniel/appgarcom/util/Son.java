package com.daniel.appgarcom.util;

import android.content.Context;
import android.media.MediaPlayer;

import com.daniel.appgarcom.R;


public class Son {
    public static void start(Context context) {
        MediaPlayer mp = MediaPlayer.create(context, R.raw.bell);
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {

                mp.release();
            }

        });
        mp.start();
    }
}
