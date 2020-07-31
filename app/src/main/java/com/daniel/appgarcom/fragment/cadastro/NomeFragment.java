package com.daniel.appgarcom.fragment.cadastro;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.daniel.appgarcom.R;
import com.google.android.material.textfield.TextInputLayout;

public class NomeFragment extends Fragment {

    private EditText nome;
    private TextInputLayout layNome;

    public String getNome() {
        return nome.getText() + "";
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cadastro_nome, container, false);
        nome = (EditText) view.findViewById(R.id.input_name);
        layNome = (TextInputLayout) view.findViewById(R.id.lay_nome);
        nome.requestFocus();
        nome.setSelection(nome.length());
        return view;
    }
    public boolean verificaNome() {
        if (nome.getText().length() > 5) {
            layNome.setErrorEnabled(false);
            return true;
        } else {
            layNome.setError("Nome muito curto");
            return false;
        }
    }


}
