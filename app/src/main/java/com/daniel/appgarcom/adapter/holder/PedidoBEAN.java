package com.daniel.appgarcom.adapter.holder;

public class PedidoBEAN {
    private int codigo;
    private String time;
    private Float quantidade;
    private String observacao;
    private String timeF;
    private String status;
    private String proNome;
    private float valor;
    private int excluzao;
    private int produto;
    private int venda;

    public float getValor() {
        return valor;
    }

    public void setValor(float valor) {
        this.valor = valor;
    }

    public String getProNome() {
        return proNome;
    }

    public void setProNome(String proNome) {
        this.proNome = proNome;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Float getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Float quantidade) {
        this.quantidade = quantidade;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public String getTimeF() {
        return timeF;
    }

    public void setTimeF(String timeF) {
        this.timeF = timeF;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getExcluzao() {
        return excluzao;
    }

    public void setExcluzao(int excluzao) {
        this.excluzao = excluzao;
    }

    public int getProduto() {
        return produto;
    }

    public void setProduto(int produto) {
        this.produto = produto;
    }

    public int getVenda() {
        return venda;
    }

    public void setVenda(int venda) {
        this.venda = venda;
    }
}
