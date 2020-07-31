package com.daniel.appgarcom.fragment.cadastro;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.daniel.appgarcom.R;
import com.google.android.material.textfield.TextInputLayout;


public class SenhaFragment extends Fragment {
    private EditText senha;
    private EditText senha2;
    private TextInputLayout laySenha;
    private TextInputLayout laySenha2;

    public String getSenha() {
        return senha.getText() + "";
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cadastro_senha, container, false);
        senha = (EditText) view.findViewById(R.id.input_senha1);
        senha2 = (EditText) view.findViewById(R.id.input_senha2);
        laySenha = (TextInputLayout) view.findViewById(R.id.lay_senha);
        laySenha2 = (TextInputLayout) view.findViewById(R.id.lay_senha2);
        senha.requestFocus();
        senha.setSelection(senha.length());
        return view;
    }
    public boolean verificaSenha() {
        Log.i("[IFMG]", "senha 1 :"+senha.getText());
        Log.i("[IFMG]", "senha 2 :" +senha2.getText());
        if (senha.getText().toString().equals(senha2.getText().toString())) {
            laySenha.setErrorEnabled(false);
            laySenha2.setErrorEnabled(false);
            return true;
        } else {
            laySenha.setError("Senhas não conferem");
            laySenha2.setError("Senhas não conferem");
            return false;
        }
    }

}
