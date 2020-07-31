package com.daniel.appgarcom.fragment.cadastro;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import com.daniel.appgarcom.R;
import com.daniel.appgarcom.modelo.beans.Endereco;
import com.daniel.appgarcom.sync.CepAPI;
import com.daniel.appgarcom.sync.SyncCEP;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BuscaEnderecoFragment extends Fragment {
    private EditText cep;
    private Button btnCep;

    public String getCep() {
        return cep.getText() + "";
    }

    public void setCep(EditText cep) {
        this.cep = cep;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_buscar_endereco, container, false);
        cep = (EditText) view.findViewById(R.id.input_cep);
        btnCep = (Button) view.findViewById(R.id.btnCep);
        cep.requestFocus();
        cep.setSelection(cep.length());
        btnCep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               getEnderecoByCEP();

            }
        });
        return view;
    }

    public void getEnderecoByCEP() {
        CepAPI api = SyncCEP.RETROFIT_CEP(getContext()).create(CepAPI.class);

        final Call<Endereco> call = api.getEnderecoByCEP(cep.getText() + "");

        call.enqueue(new Callback<Endereco>() {
            @Override
            public void onResponse(Call<Endereco> call, Response<Endereco> response) {
                if (response.code() == 200) {


                    Log.i("[IFMG]", response.body() + "");

                    Endereco u = response.body();
                    if (u != null) {
                       // setDados(u);
                    }
                    //setEndereco(u);
                }
            }

            @Override
            public void onFailure(Call<Endereco> call, Throwable t) {
            }
        });
    }
}
