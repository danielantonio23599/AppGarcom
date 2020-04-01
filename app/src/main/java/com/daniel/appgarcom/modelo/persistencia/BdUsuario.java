package com.daniel.appgarcom.modelo.persistencia;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.daniel.appgarcom.modelo.beans.Servidor;
import com.daniel.appgarcom.modelo.beans.Usuario;

public class BdUsuario {
    public SQLiteDatabase db, dbr;

    public BdUsuario(Context context) {

        //objeto obrigatório para todas as classes
        BdCore auxBd = new BdCore(context);

        //acesso para escrita no bd
        db = auxBd.getWritableDatabase();
        //acesso para leitura do bd
        dbr = auxBd.getReadableDatabase();
    }

    public long insert(Usuario linha) {
        ContentValues values = new ContentValues();
        if (linha.getCodigo() != 0)
            values.put("usuCodigo", linha.getCodigo());
        values.put("usuNome", linha.getNome());
        values.put("usuDataNascimento", linha.getDataNacimento());
        values.put("usuTelefone", linha.getTelefone());
        values.put("usuFoto", linha.getFoto());
        values.put("usuEmail", linha.getEmail());
        values.put("usuCpf", linha.getCPF());
        values.put("usuRg", linha.getRG());
        values.put("usuSenha", linha.getSenha());
        values.put("usuLogradouro", linha.getLogradouro());
        values.put("usuBairro", linha.getBairro());
        values.put("usuComplemento", linha.getComplemento());
        values.put("usuNumero", linha.getNumero());
        values.put("usuCidade", linha.getCidade());
        values.put("usuUf", linha.getUf());
        values.put("usuCep", linha.getCep());

        //inserindo diretamente na tabela sem a necessidade de script sql
        long r = db.insert("usuario", null, values);
        db.close();
        dbr.close();
        return r;

    }


    public void deleteAll() {
        // deleta todas informações da tabela usando script sql
        db.execSQL("DELETE FROM usuario;");
        db.close();
        dbr.close();
    }


    public Usuario listar() {
        Usuario linha = new Usuario();
        linha.setCodigo(0);
        // Query do banco
        String query = "SELECT * FROM usuario ;";
        // Cria o cursor e executa a query
        Cursor cursor = db.rawQuery(query, null);
        // Percorre os resultados
        // Se o cursor pode ir ao primeiro
        if (cursor.moveToFirst()) do {
            // Cria novo , cada vez que entrar aqui

            // Define os campos da configuracao, pegando do cursor pelo id da coluna
            linha.setCodigo(cursor.getInt(0));

            linha.setNome(cursor.getString(1));
            linha.setDataNacimento(cursor.getString(2));
            linha.setTelefone(cursor.getString(3));
            linha.setFoto(cursor.getBlob(4));
            linha.setCPF(cursor.getString(5));
            linha.setRG(cursor.getString(6));
            linha.setSenha(cursor.getString(7));
            linha.setLogradouro(cursor.getString(8));
            linha.setBairro(cursor.getString(9));
            linha.setComplemento(cursor.getString(10));
            linha.setNumero(cursor.getString(11));
            linha.setCidade(cursor.getString(12));
            linha.setUf(cursor.getString(13));
            linha.setCep(cursor.getString(14));
        }
        while (cursor.moveToNext()); // Enquanto o usuario pode mover para o proximo ele executa esse metodo

        db.close();
        dbr.close();
        // Retorna a lista
        return linha;
    }
}
