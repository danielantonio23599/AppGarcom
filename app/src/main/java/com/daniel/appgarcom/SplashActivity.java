package com.daniel.appgarcom;

import android.Manifest;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.daniel.appgarcom.modelo.beans.Servidor;
import com.daniel.appgarcom.modelo.persistencia.BdServidor;
import com.daniel.appgarcom.util.PermissionUtils;


public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        int tempoSplash = 3000;
        String[] permissoes = new String[]{
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE
        };
        PermissionUtils.validate(this, 0, permissoes);

        //cria delay para entrar na proxima activity
        new Handler().postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        // Seta intent para abrir nova Activity
                        Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                        startActivity(i);
                    }
                }, tempoSplash);

    }

    /* -------------------------------------------------------
    SUBCLASSE RESPONSÁVEL POR CRIAR A SEGUNDA THREAD, OBJETIVANDO PROCESSAMENTO
    PARALELO AO DA THREAD DA INTERFACE GRÁFICA
     ----------------------------------------------------------*/
    class InsertAsync extends AsyncTask<String, String, String> {
        //método executado antes do método da segunda thread doInBackground
        @Override
        protected void onPreExecute() {

        }

        //método que será executado em outra thread
        @Override
        protected String doInBackground(String... args) {
            BdServidor bd = new BdServidor(getApplication());
            if (bd.listar().getCodigo() == 0) {
                Servidor s = new Servidor();
                s.setIp("192.168.0.4");
                bd.insert(s);
            }
            return null;
        }

        //método executado depois da thread do doInBackground
        @Override
        protected void onPostExecute(String retorno) {
            //manda mensagem na tela para dizer que já executou a segunda thread


        }
    }
}

