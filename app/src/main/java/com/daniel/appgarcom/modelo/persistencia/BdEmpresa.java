package com.daniel.appgarcom.modelo.persistencia;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.daniel.appgarcom.modelo.beans.Empresa;
import com.daniel.appgarcom.modelo.beans.Servidor;

public class BdEmpresa {
    public SQLiteDatabase db, dbr;

    public BdEmpresa(Context context) {

        //objeto obrigatório para todas as classes
        BdCore auxBd = new BdCore(context);

        //acesso para escrita no bd
        db = auxBd.getWritableDatabase();
        //acesso para leitura do bd
        dbr = auxBd.getReadableDatabase();
    }

    public long insert(Empresa linha) {
        ContentValues values = new ContentValues();
        if (linha.getEmpCodigo() != 0)
            values.put("empCodigo", linha.getEmpCodigo());
        values.put("empEmail", linha.getEmpEmail());
        values.put("empSenha", linha.getEmpSenha());
        values.put("empFantazia", linha.getEmpFantazia());
        values.put("empLogo", linha.getEmpLogo());
        //inserindo diretamente na tabela sem a necessidade de script sql
        long r = db.insert("empresa", null, values);
        return r;

    }

    public void close() {
        db.close();
        dbr.close();
    }


    public void deleteAll() {

        // deleta todas informações da tabela usando script sql
        db.execSQL("DELETE FROM empresa;");
    }


    public Empresa listar() {
        Empresa linha = new Empresa();
        linha.setEmpCodigo(0);
        // Query do banco
        String query = "SELECT * FROM empresa ;";
        // Cria o cursor e executa a query
        Cursor cursor = db.rawQuery(query, null);
        // Percorre os resultados
        // Se o cursor pode ir ao primeiro
        if (cursor.moveToFirst()) do {
            // Cria novo , cada vez que entrar aqui
            // Define os campos da configuracao, pegando do cursor pelo id da coluna
            linha.setEmpCodigo(cursor.getInt(0));
            linha.setEmpEmail(cursor.getString(1));
            linha.setEmpSenha(cursor.getString(2));
            linha.setEmpFantazia(cursor.getString(3));
            linha.setEmpLogo(cursor.getBlob(4));
        }
        while (cursor.moveToNext()); // Enquanto o usuario pode mover para o proximo ele executa esse metodo
        // Retorna a lista
        return linha;
    }
}
