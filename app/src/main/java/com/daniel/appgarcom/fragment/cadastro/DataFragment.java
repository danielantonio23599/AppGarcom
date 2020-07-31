package com.daniel.appgarcom.fragment.cadastro;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.daniel.appgarcom.R;
import com.daniel.appgarcom.util.Data;
import com.daniel.appgarcom.util.MaskEditUtil;
import com.google.android.material.textfield.TextInputLayout;

public class DataFragment extends Fragment {

    private EditText data;
    private TextInputLayout layData;

    public String getData() {
        return Data.formataDataUS(data.getText() + "");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cadastro_nascimento, container, false);
        data = (EditText) view.findViewById(R.id.input_data);
        data.addTextChangedListener(MaskEditUtil.mask(data, MaskEditUtil.FORMAT_DATE));
        layData = (TextInputLayout) view.findViewById(R.id.lay_data);
        data.requestFocus();
        data.setSelection(data.length());
        return view;
    }
    public boolean verificaData() {
        if (data.getText().length() == 10) {
            layData.setErrorEnabled(false);
            return true;
        } else {
            layData.setError("Data invalida");
            return false;
        }
    }


}
