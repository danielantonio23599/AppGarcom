package com.daniel.appgarcom.util;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

/**
 * Created by fernando on 08/12/17.
 */

public class UtilImageTransmit {

    /**
     * Método usado para converter stream da imagem em Base64. Método usado na transmissão de
     * imagens em webservices Rest
     *
     * @return String retornada é a própria imagem na Base64
     */

    public static String convertImageToBase64(String pathName) {
        //Codificar uma imagem
        Bitmap bitmap = BitmapFactory.decodeFile(pathName);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        //reduzindo imagem
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] bitMapData = stream.toByteArray();
        return Base64.encodeToString(bitMapData, Base64.DEFAULT);
    }

    public static Bitmap convertBase64ToBitmap(String encodedImage) {
        String base64Image = encodedImage.split(",")[1];
        byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
       /* byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);*/

        return decodedByte;

    }

    public static Bitmap getConvertString64Bitmap(String Base64String) {
        byte[] decodedString = Base64.decode(Base64String.getBytes(), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return decodedByte;
    }

    public static String getConvert64ImageStringFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteFormat = stream.toByteArray();
        // get the base 64 string
        String imgString = Base64.encodeToString(byteFormat, Base64.NO_CLOSE);
        return imgString;
    }

    public static String convertImageToBase64(Bitmap bitmap) {
        //Codificar uma imagem
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        //reduzindo imagem
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] bitMapData = stream.toByteArray();
        String url = Base64.encodeToString(bitMapData, Base64.DEFAULT);
        return url;
    }
    public static byte[]  convertImageToByte(Bitmap image) {
        Bitmap immagex = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.PNG, 90, baos);
        byte[] b = baos.toByteArray();
        return b;
    }

    /**
     * Método usado para converter o stream de uma imagem da Base64 para Bitmap, Método usado na
     * recebimento de imagens enviadas via webservice Rest
     *
     * @param img String contendo o código da imagem em formato Base64
     * @return Retorna um Bitmap com a imagem.
     */
    public static Bitmap convertBase64toImage(String img) {
        Bitmap bitmap = null;
        try {
            byte[] bitMapData = Base64.decode(img, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(bitMapData, Base64.DEFAULT, bitMapData.length);
        } catch (Exception x) {
            x.printStackTrace();
        }
        return bitmap;
    }
    public static Bitmap convertBytetoImage(byte[] bitMapData) {
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeByteArray(bitMapData, Base64.DEFAULT, bitMapData.length);
        } catch (Exception x) {
            x.printStackTrace();
        }
        return bitmap;
    }
}
