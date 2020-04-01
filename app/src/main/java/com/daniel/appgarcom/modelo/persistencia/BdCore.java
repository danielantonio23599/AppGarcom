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

    private static final String Name_BD = "BancoGarcom.db";
    private static final int Versao_BD = 1;

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
        createTableUsuario(bd);
        createTableEmpresa(bd);


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

    public void createTableUsuario(SQLiteDatabase bd) {
        String slqCreateTabela = "CREATE TABLE IF NOT EXISTS usuario(\n" +
                "   usuCodigo INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "   usuNome text,\n" +
                "   usuDataNascimento text,\n" +
                "   usuTelefone text,\n" +
                "   usuFoto longblob,\n" +
                "   usuEmail text,\n" +
                "   usuCpf text,\n" +
                "   usuRg text,\n" +
                "   usuSenha text,\n" +
                "   usuLogradouro text,\n" +
                "   usuBairro text,\n" +
                "   usuComplemento text,\n" +
                "   usuNumero text,\n" +
                "   usuCidade text,\n" +
                "   usuUf text,\n" +
                "   usuCep text);";
        // Executa a query passada como parametro
        bd.execSQL(slqCreateTabela);
    }

    public void createTableEmpresa(SQLiteDatabase bd) {
        String slqCreateTabela = "CREATE TABLE IF NOT EXISTS empresa(\n" +
                "  empCodigo INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "  empEmail text,\n" +
                "  empSenha text,\n" +
                "  empFantazia text,\n" +
                "  empLogo longblob);";
        // Executa a query passada como parametro
        bd.execSQL(slqCreateTabela);
    }
}
