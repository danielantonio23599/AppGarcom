package com.daniel.appgarcom.util;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by Daniel on 14/05/2018.
 */

public class TecladoUtil {
    public static void hideKeyboard(Context context, View editText) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);

    }

    public static void showKeyboard(Context context, View editText) {
        //editText.findFocus();
        ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE))
                .showSoftInput(editText, 0);
        //edittext.clearFocus();


    }
}
