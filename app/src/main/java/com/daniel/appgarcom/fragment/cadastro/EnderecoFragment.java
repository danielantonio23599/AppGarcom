package com.daniel.appgarcom.fragment.cadastro;

import android.content.IntentSender;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.daniel.appgarcom.R;
import com.daniel.appgarcom.modelo.beans.Endereco;
import com.daniel.appgarcom.sync.CepAPI;
import com.daniel.appgarcom.sync.SyncCEP;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EnderecoFragment extends Fragment {
    private LinearLayout lyEndereco;
    private EditText logradouro;
    private EditText bairro;
    private EditText cidade;
    private EditText numero;
    private EditText uf;
    private EditText complemento;
    private EditText cep;
    private TextView end;
    private Endereco endereco;
    private TextInputLayout layCEP;
    public int REQUEST_CHECK_SETTINGS = 1;
    private Button btnCep;

    public String getLogradouro() {
        return logradouro.getText().toString();
    }

    public void setLogradouro(String logradouro) {
        this.logradouro.setText(logradouro);
    }

    public String getBairro() {
        return bairro.getText().toString();
    }

    public void setBairro(String bairro) {
        this.bairro.setText(bairro);
    }

    public String getCidade() {
        return cidade.getText().toString();
    }

    public void setCidade(String cidade) {
        this.cidade.setText(cidade);
    }

    public String getNumero() {
        return numero.getText().toString();
    }

    public void setNumero(String numero) {
        this.numero.setText(numero);
    }

    public String getUf() {
        return uf.getText().toString();
    }

    public void setUf(String uf) {
        this.uf.setText(uf);
    }

    public void setCep(String cep) {
        this.cep.setText(cep);
    }

    public String getCep() {
        return cep.getText().toString();
    }

    public LinearLayout getLyEndereco() {
        return lyEndereco;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_buscar_endereco, container, false);
        lyEndereco = (LinearLayout) view.findViewById(R.id.lyEndereco);
       // end = (TextView) view.findViewById(R.id.tvEndereco);
        // edit text
        logradouro = (EditText) view.findViewById(R.id.input_logradouro);
        bairro = (EditText) view.findViewById(R.id.input_bairro);
        cidade = (EditText) view.findViewById(R.id.input_cidade);
        cep = (EditText) view.findViewById(R.id.input_cep);
        cep.requestFocus();
        cep.setSelection(cep.length());
        layCEP = (TextInputLayout) view.findViewById(R.id.lay_cep);
        numero = (EditText) view.findViewById(R.id.input_numero);
        uf = (EditText) view.findViewById(R.id.input_uf);
        complemento = (EditText) view.findViewById(R.id.input_complemento);
        btnCep = (Button) view.findViewById(R.id.btnCep);
        btnCep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cep.getText().length() < 8) {
                    layCEP.setError("Cep incorreto, favor digite novamente!");
                } else {
                    layCEP.setErrorEnabled(false);
                    //layCEP.setHelperText("O CEP digitado sera usado como busca para pesquisa de endereço");
                    getEnderecoByCEP();
                }

            }
        });
        lyEndereco.setVisibility(view.GONE);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public Endereco getEndereco() {
        Endereco e = new Endereco();
        e.setLogradouro(logradouro.getText() + "");
        e.setBairro(bairro.getText() + "");
        e.setLocalidade(cidade.getText() + "");
        e.setUf(uf.getText() + "");
        e.setNumero(numero.getText() + "");
        e.setCep(cep.getText() + "");
        e.setComplemento(complemento.getText() + "");
        return e;
    }

    public void setEndereco(Endereco e) {
        endereco = e;
    }

    public void setDados(Endereco e) {
        logradouro.setText(e.getLogradouro() + "");
        bairro.setText(e.getBairro());
        cidade.setText(e.getLocalidade());
        uf.setText(e.getUf());
        numero.setText(e.getNumero());
        cep.setText(e.getCep());
        complemento.setText(e.getComplemento());
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
                        setDados(u);
                        lyEndereco.setVisibility(View.VISIBLE);
                    } else {
                        layCEP.setError("CEP INCORRETO");
                        Toast.makeText(getActivity(), "CEP incorreto, tente novamento", Toast.LENGTH_LONG).show();
                    }
                } else {
                }
            }

            @Override
            public void onFailure(Call<Endereco> call, Throwable t) {
            }
        });
    }
}
