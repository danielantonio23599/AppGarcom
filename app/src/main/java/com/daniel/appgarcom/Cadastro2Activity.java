package com.daniel.appgarcom;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;


import com.daniel.appgarcom.fragment.cadastro.BuscaEnderecoFragment;
import com.daniel.appgarcom.fragment.cadastro.DataFragment;
import com.daniel.appgarcom.fragment.cadastro.EmailFragment;
import com.daniel.appgarcom.fragment.cadastro.EnderecoFragment;
import com.daniel.appgarcom.fragment.cadastro.NomeFragment;
import com.daniel.appgarcom.fragment.cadastro.SenhaFragment;
import com.daniel.appgarcom.fragment.cadastro.TelefoneFragment;
import com.daniel.appgarcom.fragment.cadastro.confirma.ConfirmaTelefoneFragment;
import com.daniel.appgarcom.modelo.beans.Empresa;
import com.daniel.appgarcom.modelo.beans.Endereco;
import com.daniel.appgarcom.modelo.beans.Funcionario;
import com.daniel.appgarcom.modelo.persistencia.BdEmpresa;
import com.daniel.appgarcom.sync.CepAPI;
import com.daniel.appgarcom.sync.RestauranteAPI;
import com.daniel.appgarcom.sync.SyncCEP;
import com.daniel.appgarcom.sync.SyncDefaut;
import com.daniel.appgarcom.util.Criptografia;
import com.daniel.appgarcom.util.PermissionUtils;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Cadastro2Activity extends AppCompatActivity {
    private Button next;
    private ImageButton back;
    private Toolbar toolbar;
    private FragmentContainerView fragment;
    private SenhaFragment senha = new SenhaFragment();
    private NomeFragment nome = new NomeFragment();
    private DataFragment data = new DataFragment();
    private EmailFragment email = new EmailFragment();
    private TelefoneFragment telefone = new TelefoneFragment();
    private EnderecoFragment enderec = new EnderecoFragment();
    private BuscaEnderecoFragment busca_end = new BuscaEnderecoFragment();
    private ConfirmaTelefoneFragment conf_tele = new ConfirmaTelefoneFragment();
    private Endereco endereco;
    private AlertDialog alerta;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_cadastro2);
        String[] permissoes = new String[]{
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                Manifest.permission.SEND_SMS,
                Manifest.permission.BROADCAST_SMS
        };
        PermissionUtils.validate(this, 0, permissoes);
        //toolbar = (Toolbar) findViewById(R.id.toolbar);
        //botão voltar na actionBar
        // setSupportActionBar(toolbar);
        // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setDisplayShowHomeEnabled(true);
        back = (ImageButton) findViewById(R.id.imBack);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        next = (Button) findViewById(R.id.btnNext);
        fragment = (FragmentContainerView) findViewById(R.id.cadastro_fragment);

        replaceFragment(nome);
        next.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View v) {
                Log.i("IFMG", getSupportFragmentManager().findFragmentById(R.id.cadastro_fragment) + "");
                if (nome == (getSupportFragmentManager().findFragmentById(R.id.cadastro_fragment))) {
                    if (nome.verificaNome()) {
                        replaceFragment(data);
                    }

                } else if (data == (getSupportFragmentManager().findFragmentById(R.id.cadastro_fragment))) {
                    if (data.verificaData()) {
                        replaceFragment(email);
                    }
                } else if ((getSupportFragmentManager().findFragmentById(R.id.cadastro_fragment)) == email) {
                    if (email.verificaEmail()) {
                        replaceFragment(telefone);
                    }
                } else if ((getSupportFragmentManager().findFragmentById(R.id.cadastro_fragment)) == telefone) {
                    if (telefone.verificaTelefone()) {
                        telefone.enviarSMS();
                        replaceFragment(conf_tele);
                    }
                } else if ((getSupportFragmentManager().findFragmentById(R.id.cadastro_fragment)) == conf_tele) {
                    if (conf_tele.verificaCodigo(telefone.getCodigo())) {
                        replaceFragment(enderec);
                    }
                } else if ((getSupportFragmentManager().findFragmentById(R.id.cadastro_fragment)) == enderec) {
                    replaceFragment(senha);
                } else if ((getSupportFragmentManager().findFragmentById(R.id.cadastro_fragment)) == senha) {
                    if (senha.verificaSenha()) {
                        cadastrar();
                    }
                }
            }
        });
    }

    public void replaceFragment(Fragment fragment) {
        //nao deixa adicionar a fila de fragments
        //getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        getSupportFragmentManager().beginTransaction().replace(R.id.cadastro_fragment, fragment, "cadastro").addToBackStack(null).commit();
    }


    @Override
    public void onBackPressed() {
        if (nome != (getSupportFragmentManager().findFragmentById(R.id.cadastro_fragment))) {
            getSupportFragmentManager().popBackStack();
        }
    }

    public Endereco getEnderecoByCEP(String cep) {
        CepAPI api = SyncCEP.RETROFIT_CEP(this).create(CepAPI.class);

        final Call<Endereco> call = api.getEnderecoByCEP(cep);

        call.enqueue(new Callback<Endereco>() {
            @Override
            public void onResponse(Call<Endereco> call, Response<Endereco> response) {
                if (response.code() == 200) {


                    Log.i("[IFMG]", response.body() + "");
                    endereco = response.body();

                }
            }

            @Override
            public void onFailure(Call<Endereco> call, Throwable t) {
            }
        });
        return endereco;
    }

    public void cadastrar() {
        mostraDialog();
        BdEmpresa bd = new BdEmpresa(this);
        final Empresa sh = bd.listar();
        bd.close();
        Funcionario f = getDados();
        RestauranteAPI api = SyncDefaut.RETROFIT_RESTAURANTE(getApplicationContext()).create(RestauranteAPI.class);
        Log.i("[IFMG]", sh.getEmpEmail() + sh.getEmpSenha() + "");
        final Call<Void> call = api.insereFuncionario(new Gson().toJson(f), sh.getEmpEmail(), sh.getEmpSenha());
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.i("[IFMG]", response + "");
                if (response.isSuccessful()) {
                    String auth = response.headers().get("auth");
                    if (auth.equals("1")) {
                        escondeDialog();
                        String res = response.headers().get("sucesso");
                        if (res.equals("Funcionario já CADASTRADO!!")) {
                            email.setEnableErroEmail();
                            replaceFragment(email);
                        } else {
                            showToast(res);
                            mudaActivity(LoginGarcomActivity.class);
                        }
                    } else {
                        escondeDialog();
                        showToast("Erro login");
                    }
                } else {
                    escondeDialog();
                    Log.i("[IFMG]", "erro servidor");
                    showToast("Erro servidor");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                escondeDialog();
                Log.i("[IFMG]", "erro servidor onFalure " + t.getMessage());
                showToast("Erro servidor " + t.getMessage());
            }
        });
    }

    private void mudaActivity(final Class classe) {
        Log.i("[IFMG]", "passou no muda actyvity" + classe.getName());
        final Intent intent = new Intent(this, classe);
        startActivity(intent);
        finish();

    }

    public void showToast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    private Funcionario getDados() {
        Funcionario f = new Funcionario();
        f.setDataNacimento(data.getData());
        f.setNome(nome.getNome());
        f.setEmail(email.getEmail());
        f.setTelefone(telefone.getTelefone());
        f.setBairro(enderec.getBairro());
        f.setLogradouro(enderec.getLogradouro());
        f.setCep(enderec.getCep());
        f.setCidade(enderec.getCidade());
        f.setUf(enderec.getUf());
        f.setNumero(enderec.getNumero());
        f.setSenha(Criptografia.criptografar(senha.getSenha()));
        return f;
    }

    private void escondeDialog() {
        if (alerta.isShowing()) {
            alerta.dismiss();
        }
    }

    private void mostraDialog() {

        final LayoutInflater li = getLayoutInflater();
        //inflamos o layout alerta.xml na view
        View view = li.inflate(R.layout.alert_progress, null);
        TextView tvDesc = (TextView) view.findViewById(R.id.tvDesc);    //definimos para o botão do layout um clickListener
        tvDesc.setText("Cadastro de Funcionario...");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Aguarde...");
        builder.setView(view);
        builder.setCancelable(false);
        alerta = builder.create();
        alerta.show();
    }
}
