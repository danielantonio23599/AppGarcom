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

    public static void setUserisAdm(String fantazia, Context context){
        PrefsUtils.setString(context, context.getString(R.string.FANTAZIA), fantazia);
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

    public static void updateAllPreferences(Context context, SharedPreferencesEmpresa preferences) {

        PrefsUtils.setInteger(context, context.getString(R.string.CODIGO), preferences.getEmpCodigo());
        PrefsUtils.setString(context, context.getString(R.string.EMAIL), preferences.getEmpEmail());
        PrefsUtils.setString(context, context.getString(R.string.SENHA), preferences.getEmpSenha());
        PrefsUtils.setString(context, context.getString(R.string.FANTAZIA), preferences.getEmpFantazia());
    }

    public static SharedPreferencesEmpresa getAllPreferences(Context context) {
        SharedPreferencesEmpresa u = new SharedPreferencesEmpresa();
        u.setEmpCodigo(PrefsUtils.getInteger(context, context.getString(R.string.CODIGO)));
        u.setEmpEmail(PrefsUtils.getString(context, context.getString(R.string.EMAIL)));
        u.setEmpSenha(PrefsUtils.getString(context,  context.getString(R.string.SENHA)));
        u.setEmpFantazia(PrefsUtils.getString(context, context.getString(R.string.FANTAZIA)));


        return u;
    }
}
