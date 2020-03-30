package com.daniel.appgarcom.util;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;

public class Time {
    public static String getTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        Date data = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(data);
        Date data_atual = cal.getTime();
        String hora_atual = dateFormat.format(data_atual);
        Log.i("hora_atual", hora_atual); // Esse é o que você que
        return hora_atual;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String subtrairHoras(String horaIni) {
        return subtrairHoras(getTime(), horaIni);
    }

    public static String getCronometro(String horaInicial) {

        return "";
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String subtrairHoras(String hora1, String hora2) {
        int h1 = convertMinu(hora1);
        int h2 = convertMinu(hora2);
        int h3;
        if (h1 >= h2) {
            h3 = h1 - h2;
        } else {
            h3 = 1440 - h2;
            h3 += h1;
        }
        return convertHora(h3);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static int convertMinu(String hora) {
        LocalTime time = LocalTime.parse(hora);
        int h1 = time.getHour();
        int m1 = time.getMinute();
        return (h1 * 60 + m1);
    }

    public static String convertHora(int min) {
        int hora = (min / 60);
        int minuto = (min % 60);
        return hora + ":" + minuto;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String formataHora(String hora) {
        LocalTime time = LocalTime.parse(hora);
        int h1 = time.getHour();
        int m1 = time.getMinute();
        return h1 + ":" + m1;
    }
}
