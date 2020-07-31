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


public class EmailFragment extends Fragment {
    private EditText email;
    private TextInputLayout layEmail;

    public String getEmail() {
        return email.getText() + "";
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cadastro_email, container, false);
        email = (EditText) view.findViewById(R.id.input_email);
        layEmail = (TextInputLayout) view.findViewById(R.id.lay_email);
        email.requestFocus();
        email.setSelection(email.length());
        return view;
    }

    public boolean verificaEmail() {
        if (email.getText().length() > 5) {
            layEmail.setErrorEnabled(false);
            return true;
        } else {
            layEmail.setError("Email muito curto");
            return false;
        }
    }

    public void setEnableErroEmail() {
        layEmail.setError("Email já cadastrado");
    }
    public void setDisableErroEmail() {
        layEmail.setErrorEnabled(false);
    }

}
