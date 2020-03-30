package com.daniel.appgarcom.util;

import android.app.Activity;
import android.widget.TextView;

/**
 * Created by Daniel on 16/06/2018.
 */

public class Cronometro {
    private TextView textView;
    private long inicio = 0;
    private long mil = 0;
    private long seg = 0;
    private long min = 0;
    private long hra = 0;
    private String s = "";
    private String m = "";
    private String h = "";
    private boolean start = true;
    private String tempo = "00:00:00";
    private boolean pause = false;
    private Activity context;

    public Cronometro(Activity applicationContext) {
        context = applicationContext;
        //this.textView = textView;
    }

    public TextView getTextView() {
        return textView;
    }

   /* public void setTextView(TextView textView) {
        this.textView = textView;
    }*/

    public String getTempo() {
        return tempo;
    }

    public void setTempo(String tempo) {
        this.tempo = tempo;
    }

    public boolean isStart() {
        return start;
    }

    public void setStart(boolean start) {
        this.start = start;
    }

    public long getMil() {
        return mil;
    }

    public void setMil(long mil) {
        this.mil = mil;
    }

    public long getSeg() {
        return seg;
    }

    public void setSeg(long seg) {
        this.seg = seg;
    }

    public long getMin() {
        return min;
    }

    public void setMin(long min) {
        this.min = min;
    }

    public long getHra() {
        return hra;
    }

    public void setHra(long hra) {
        this.hra = hra;
    }

    public boolean isPause() {
        return pause;
    }

    public void setPause(boolean pause) {
        this.pause = pause;
    }


    public void iniciar() {
        //start eu recebo da minha frame
        while (start == true) { //se start for true faça, inicio meu laço ate start ficar falso
            inicio = System.currentTimeMillis(); //inicio a contagem
            while (pause == false) {
                mil = System.currentTimeMillis();
                mil = ((mil - inicio)); //tira a diferença do tempo inicial com o tempo atual
                //contador dos segundos
                if (mil >= 990) {
                    seg++;
                    inicio = System.currentTimeMillis();
                }
                if (seg >= 60) {
                    min++;
                    seg = 0;
                }
                if (min >= 60) {
                    hra++;
                    min = 0;
                }
                if (seg < 10) {
                    s = "0" + seg;
                } else {
                    s = seg + "";
                }
                if (min < 10) {
                    m = "0" + min;
                } else {
                    m = "" + min;
                }
                if (hra < 10) {
                    h = "0" + hra;
                } else {
                    h = "" + hra;
                }
                tempo = h + ":" + m ;
               if(!tempo.equals(textView.getText())) {
                    context.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            textView.setText(tempo);
                        }
                    });
                }
            }
        }
    }
}

