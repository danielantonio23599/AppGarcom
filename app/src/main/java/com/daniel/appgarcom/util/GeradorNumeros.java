package com.daniel.appgarcom.util;

import java.util.Random;

public class GeradorNumeros {
    public static int geraNumeroInterio(int algarismos) {
        //instância um objeto da classe Random usando o construtor padrão
        Random gerador = new Random();
        String numero = "";
        //imprime sequência de 10 números inteiros aleatórios
        for (int i = 0; i < algarismos; i++) {
            numero += gerador.nextInt(10) + "";
            System.out.println(numero);
        }
        int retorno = Integer.parseInt(numero);
        return retorno;
    }
}
