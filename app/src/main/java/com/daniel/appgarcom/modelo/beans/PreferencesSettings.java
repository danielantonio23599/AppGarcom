package com.daniel.appgarcom.modelo.beans;

import android.content.Context;

import com.daniel.appgarcom.R;
import com.daniel.appgarcom.util.PrefsUtils;


/**
 * Created by Arthur on 15/04/2018.
 */

public class PreferencesSettings {

    private static final String TAG = "PreferencesSettings";

    public static void setUserCodigo(int codigo, Context context) {
        PrefsUtils.setInteger(context, context.getString(R.string.CODIGO), codigo);
    }

    public static void setUserLogin(String login, Context context){
        PrefsUtils.setString(context, context.getString(R.string.EMAIL), login);
    }

    public static void  setUserSenha(String senha, Context context){
        PrefsUtils.setString(context, context.getString(R.string.SENHA), senha);
    }

    public static void  clearUser(String senha, Context context){
        PrefsUtils.setString(context, context.getString(R.string.SENHA), senha);
    }
    public static void setUserLogOff(Context context){
        PrefsUtils.deletUser(context);
    }

     /*
    public static void setUserEmail(String email, Context context){
        PrefsUtils.setString(context, PreferencesSettings.TAG_EMAIL_USUARIO, email);
    }
    */

    public static void updateAllPreferences(Context context, SharedPreferences preferences) {

        PrefsUtils.setInteger(context, context.getString(R.string.CODIGO), preferences.getCodigo());
        PrefsUtils.setString(context, context.getString(R.string.EMAIL), preferences.getEmail());
        PrefsUtils.setString(context, context.getString(R.string.SENHA), preferences.getSenha());
    }

    public static SharedPreferences getAllPreferences(Context context) {
        SharedPreferences u = new SharedPreferences();
        u.setCodigo(PrefsUtils.getInteger(context, context.getString(R.string.CODIGO)));
        u.setEmail(PrefsUtils.getString(context, context.getString(R.string.EMAIL)));
        u.setSenha(PrefsUtils.getString(context,  context.getString(R.string.SENHA)));
        return u;
    }
}
