package com.daniel.appgarcom.adapter.holder;

public class Pedido {
    private int codigo;
    private int mesa;
    private String produto;
    private float quantidade;
    //hora que foi feito o pedido
    private String hora_pedido;
    //tempo cadastrado do produto em tempo de preparo
    private String tempo_preparo;
    //se é cozinha ou se é bar
    private String tipo;
    //se está atrazado, finalizado, ou preparando
    private String status;
    private String observacao;
    private String hora_final;

    public int getMesa() {
        return mesa;
    }

    public void setMesa(int mesa) {
        this.mesa = mesa;
    }

    public String getHora_final() {
        return hora_final;
    }

    public void setHora_final(String hora_final) {
        this.hora_final = hora_final;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getProduto() {
        return produto;
    }

    public void setProduto(String produto) {
        this.produto = produto;
    }

    public float getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(float quantidade) {
        this.quantidade = quantidade;
    }

    public String getHora_pedido() {
        return hora_pedido;
    }

    public void setHora_pedido(String hora_pedido) {
        this.hora_pedido = hora_pedido;
    }

    public String getTempo_preparo() {
        return tempo_preparo;
    }

    public void setTempo_preparo(String tempo_preparo) {
        this.tempo_preparo = tempo_preparo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }
}
