package com.daniel.appgarcom.modelo.beans;

import com.google.gson.annotations.SerializedName;

public class Produto {
    @SerializedName("codigo")
    private int codigo;
    @SerializedName("nome")
    private String nome;
    @SerializedName("preco")
    private float preco;
    @SerializedName("foto")
    private byte[] foto;
    @SerializedName("preparo")
    private String preparo;
    @SerializedName("descricao")
    private String descricao;
    @SerializedName("tipo")
    private String tipo;



    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public float getPreco() {
        return preco;
    }

    public void setPreco(float preco) {
        this.preco = preco;
    }

    public byte[] getFoto() {
        return foto;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
    }

    public String getPreparo() {
        return preparo;
    }

    public void setPreparo(String preparo) {
        this.preparo = preparo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
