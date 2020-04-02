package com.daniel.appgarcom.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Data {


    public static String getDataUS() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Data date = new Data();
        return dateFormat.format(date);
    }

    public static String formataDataBR(String dataUS) {
        String dataF = "";
        if (!dataUS.equals("")) {
            if (!dataUS.equals("  -  -    ")) {
                try {
                    SimpleDateFormat formatoDataBanco = new SimpleDateFormat("yyyy-MM-dd");
                    Date dataBanco = formatoDataBanco.parse(dataUS);
                    SimpleDateFormat formatoRetorno = new SimpleDateFormat("dd-MM-yyyy");
                    dataF = formatoRetorno.format(dataBanco);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            return "";
        }
        return dataF;
    }

    public static String formataDataUS(String dataBR) {
        String dataF = "";
        if (!dataBR.equals("")) {
            if (!dataBR.equals("  -  -    ")) {
                try {
                    SimpleDateFormat formatoRetorno = new SimpleDateFormat("dd-MM-yyyy");
                    Date dataBanco = formatoRetorno.parse(dataBR);
                    SimpleDateFormat formatoDataBanco = new SimpleDateFormat("yyyy-MM-dd");

                    dataF = formatoDataBanco.format(dataBanco);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println(dataF);
        return dataF;
    }

    public static String adicionarMeses(int quantidadeMeses) {
        Date d = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(d);

        // c.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH) + 10);
        c.set(Calendar.MONTH, c.get(Calendar.MONTH) + quantidadeMeses);
        //c.set(Calendar.YEAR, c.get(Calendar.YEAR) + 1);

        String a = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
        return a;
    }

}
