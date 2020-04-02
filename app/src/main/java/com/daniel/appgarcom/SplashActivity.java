package com.daniel.appgarcom;

import android.Manifest;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.daniel.appgarcom.modelo.beans.Empresa;
import com.daniel.appgarcom.modelo.beans.Servidor;
import com.daniel.appgarcom.modelo.beans.Usuario;
import com.daniel.appgarcom.modelo.persistencia.BdEmpresa;
import com.daniel.appgarcom.modelo.persistencia.BdServidor;
import com.daniel.appgarcom.modelo.persistencia.BdUsuario;
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
        String[] permissoes = new String[]{
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE
        };
        PermissionUtils.validate(this, 0, permissoes);

        //cria delay para entrar na proxima activity

        Log.i("[IFMG]", "backgraund");
        BdServidor bd = new BdServidor(getApplication());
        if (bd.listar().getCodigo() == 0) {
            Servidor s = new Servidor();
            s.setIp("192.168.0.4");
            bd.insert(s);
            bd.close();
        }
        BdEmpresa bdEmpresa = new BdEmpresa(getApplication());
        Empresa e = bdEmpresa.listar();
        BdUsuario u = new BdUsuario(getApplication());
        Usuario usuario = u.listar();
        u.close();
        bdEmpresa.close();
        if (e.getEmpCodigo() == 0) {
            mudaActivity(LoginActivity.class);
        } else if (usuario.getCodigo() == 0) {
            mudaActivity(LoginGarcomActivity.class);
        } else {
            mudaActivity(PrincipalActivity.class);
        }
    }

/* -------------------------------------------------------
SUBCLASSE RESPONSÁVEL POR CRIAR A SEGUNDA THREAD, OBJETIVANDO PROCESSAMENTO
PARALELO AO DA THREAD DA INTERFACE GRÁFICA
 ----------------------------------------------------------*/
class InsertAsync extends AsyncTask<String, String, String> {
    //método executado antes do método da segunda thread doInBackground
    @Override
    protected void onPreExecute() {
        Log.i("[IFMG]", "preExecute");
        String[] permissoes = new String[]{
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE
        };
        PermissionUtils.validate(SplashActivity.this, 0, permissoes);
    }

    //método que será executado em outra thread
    @Override
    protected String doInBackground(String... args) {
        Log.i("[IFMG]", "backgraund");
        BdServidor bd = new BdServidor(getApplication());
        if (bd.listar().getCodigo() == 0) {
            Servidor s = new Servidor();
            s.setIp("192.168.0.4");
            bd.insert(s);
        }
        BdEmpresa bdEmpresa = new BdEmpresa(getApplication());
        Empresa e = bdEmpresa.listar();
        BdUsuario u = new BdUsuario(getApplication());
        Usuario usuario = u.listar();
        if (e.getEmpCodigo() == 0) {
            return "empresa";
        } else if (usuario.getCodigo() == 0) {
            return "usuario";
        }
        return "principal";
    }

    //método executado depois da thread do doInBackground
    @Override
    protected void onPostExecute(String retorno) {
        //manda mensagem na tela para dizer que já executou a segunda thread
        Log.i("[IFMG]", "posExecute");
        if (retorno.equals("principal")) {
            mudaActivity(PrincipalActivity.class);
        } else if (retorno.equals("empresa")) {
            mudaActivity(LoginActivity.class);
        } else if (retorno.equals("usuario")) {
            mudaActivity(LoginGarcomActivity.class);

        }
    }

}

    private void mudaActivity(final Class classe) {
        new Handler().postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        Log.i("[IFMG]", "passou no muda actyvity" + classe.getName());
                        final Intent intent = new Intent(SplashActivity.this, classe);
                        startActivity(intent);
                    }
                }, 3000);
    }
}

