package com.daniel.appgarcom.modelo.beans;

public class ProdutosGravados {
    private int codProduto;
    private int codPedidVenda;
    private String nome;
    private Float quantidade;
    private String time;
    private int mesa;
    private Float valor;

    public int getCodProduto() {
        return codProduto;
    }

    public void setCodProduto(int codProduto) {
        this.codProduto = codProduto;
    }

    public int getCodPedidVenda() {
        return codPedidVenda;
    }

    public void setCodPedidVenda(int codPedidVenda) {
        this.codPedidVenda = codPedidVenda;
    }



    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Float getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Float quantidade) {
        this.quantidade = quantidade;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getMesa() {
        return mesa;
    }

    public void setMesa(int mesa) {
        this.mesa = mesa;
    }

    public Float getValor() {
        return valor;
    }

    public void setValor(Float valor) {
        this.valor = valor;
    }

}
