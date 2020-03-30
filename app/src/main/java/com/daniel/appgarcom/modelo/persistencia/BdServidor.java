package com.daniel.appgarcom.modelo.persistencia;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.daniel.appgarcom.modelo.beans.Servidor;


/**
 * Created by Daniel on 23/03/2018.
 */

public class BdServidor {
    public SQLiteDatabase db, dbr;

    public BdServidor(Context context) {

        //objeto obrigatório para todas as classes
        BdCore auxBd = new BdCore(context);

        //acesso para escrita no bd
        db = auxBd.getWritableDatabase();
        //acesso para leitura do bd
        dbr = auxBd.getReadableDatabase();
    }

    public long insert(Servidor linha) {

        ContentValues values = new ContentValues();
        if (linha.getCodigo() != 0)
            values.put("serCodigo", linha.getCodigo());
        values.put("serIp", linha.getIp());
        //inserindo diretamente na tabela sem a necessidade de script sql
        long r = db.insert("servidor", null, values);
        db.close();
        dbr.close();
        return r;

    }


    public void deleteAll() {

        // deleta todas informações da tabela usando script sql
        db.execSQL("DELETE FROM acesslog;");
        db.close();
        dbr.close();
    }


    public Servidor listar() {
        Servidor linha = new Servidor();
        linha.setCodigo(0);
        // Query do banco
        String query = "SELECT * FROM servidor ;";
        // Cria o cursor e executa a query
        Cursor cursor = db.rawQuery(query, null);
        // Percorre os resultados
        // Se o cursor pode ir ao primeiro
        if (cursor.moveToFirst()) do {
            // Cria novo , cada vez que entrar aqui

            // Define os campos da configuracao, pegando do cursor pelo id da coluna
            linha.setCodigo(cursor.getInt(0));
            linha.setIp(cursor.getString(1));
        }
        while (cursor.moveToNext()); // Enquanto o usuario pode mover para o proximo ele executa esse metodo

        db.close();
        dbr.close();
        // Retorna a lista
        return linha;
    }
}
