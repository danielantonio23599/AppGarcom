package com.daniel.appgarcom.modelo.persistencia;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;

/**
 * Created by Daniel on 23/03/2018.
 */

public class BdCore extends SQLiteOpenHelper {

    private static final String Name_BD = "Banco.db";
    private static final int Versao_BD = 4;

    public BdCore(Context context) {
        super(context, Name_BD, null, Versao_BD);
    }
    public boolean checkDataBase(Context context) {
        File database = context.getDatabasePath(Name_BD);
        if (!database.exists()) {
            Log.i("IFMG", "BD não existente");
            return false;
        } else {
            Log.i("IFMG", "BD não existente");
            return true;
        }
    }
    @Override
    public void onCreate(SQLiteDatabase bd) {
        createTableIp(bd);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public void createTableIp(SQLiteDatabase bd) {
        String slqCreateTabela = "CREATE TABLE IF NOT EXISTS servidor(\n" +
                "  serCodigo INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "  serIp text);";
        // Executa a query passada como parametro
        bd.execSQL(slqCreateTabela);
    }

}
