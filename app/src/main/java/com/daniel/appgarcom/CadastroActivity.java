package com.daniel.appgarcom;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.daniel.appgarcom.modelo.beans.Empresa;
import com.daniel.appgarcom.modelo.beans.Usuario;
import com.daniel.appgarcom.modelo.persistencia.BdEmpresa;
import com.daniel.appgarcom.sync.RestauranteAPI;
import com.daniel.appgarcom.sync.SyncDefaut;
import com.daniel.appgarcom.util.PermissionUtils;
import com.daniel.appgarcom.util.UtilImageTransmit;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import okhttp3.internal.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by polo on 30/06/2018.
 */

public class CadastroActivity extends AppCompatActivity {

    private static final String TAG = "CadastroActivity";

    private static final String SELECT_PICTURE_TEXT_NO_PIC = "Selecionar foto";
    private static final String SELECT_PICTURE_TEXT_CHANGE_PIC = "Alterar foto";
    protected static final int RESULT_SPEECH = 1;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private static final int REQUEST_IMAGE_CAPTURE_CODE = 1;
    private AlertDialog alerta;
    private AlertDialog alerta2;
    private EditText nome, email, telefone,rg,cpf,pwd,logradouro,bairro,numero,uf,cidade,complemento,nascimento,cep;
    private Button buttonCadastro;
    private ImageButton map, view_pwd;
    private TextView tvDesc, tvAddImagem;
    private ProgressBar progresAndress;
    //private LinearLayout pic_selection_section;
    ImageView imagemSignin;
    ImageButton addImagemBtn;

    private Usuario u = new Usuario();
    private boolean aux;
    private View view = null;

    private static byte[] fotoUsuario ;
    private TextView andres;
    private AutoCompleteTextView pesquisar;
    private ImageButton btnVoz;
    private ImageButton btnPesquisar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
        String[] permissoes = new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.INTERNET,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
        PermissionUtils.validate(CadastroActivity.this, 0, permissoes);
        imagemSignin = (ImageView) findViewById(R.id.imagem_signin);

        addImagemBtn = (ImageButton) findViewById(R.id.add_image_button_signin);

        tvAddImagem = (TextView) findViewById(R.id.add_image_text_signin);

        addImagemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent();
            }
        });


        email = (EditText) findViewById(R.id.input_email);
        nome = (EditText) findViewById(R.id.input_name);
        nascimento = (EditText) findViewById(R.id.input_data_nascimento);
        telefone = (EditText) findViewById(R.id.input_telefone);
        cpf = (EditText) findViewById(R.id.input_cpf);
        rg = (EditText) findViewById(R.id.input_rg);
        logradouro = (EditText) findViewById(R.id.input_logradouro);
        bairro = (EditText) findViewById(R.id.input_bairro);
        numero = (EditText) findViewById(R.id.input_numero);
        cidade = (EditText) findViewById(R.id.input_cidade);
        complemento = (EditText) findViewById(R.id.input_complemento);
        uf = (EditText) findViewById(R.id.input_uf);
        cep = (EditText) findViewById(R.id.input_cep);


        //view_pwd = (ImageButton) findViewById(R.id.input_password_view_button);




        buttonCadastro = (Button) findViewById(R.id.buttonSignUp);

        buttonCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (validaCampos()) {
                    showAletProgress("Aguarde enquanto é cadastrado...");
                    cadastrar2();
                } else {
                    Log.d(TAG, "onClick: Campos Vazios");
                    Toast.makeText(CadastroActivity.this, "Preencha todos os campos.", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    private void cadastrar2() {
        u = new Usuario();

        u.setNome(nome.getText() + "");
        u.setTelefone(telefone.getText()+"");
        u.setEmail(email.getText()+"");
        u.setSenha(pwd.getText()+"");
        u.setCPF(cpf.getText()+"");
        u.setRG(rg.getText()+"");
        u.setLogradouro(logradouro.getText()+"");
        u.setBairro(bairro.getText()+"");
        u.setCidade(cidade.getText()+"");
        u.setNumero(numero.getText()+"");
        u.setUf(uf.getText()+"");
        u.setCep(cep.getText()+"");
        u.setComplemento(complemento.getText()+"");

        u.setFoto(fotoUsuario);

        Log.i("PASSOU", "Passou 1");

        RestauranteAPI i = SyncDefaut.RETROFIT_RESTAURANTE(getApplication()).create(RestauranteAPI.class);
        BdEmpresa bd = new BdEmpresa(getApplication());
        Empresa e = bd.listar();
        final Call<Void> call = i.insereFuncionario(new Gson().toJson(u),e.getEmpEmail(),e.getEmpSenha());

        Log.i("USUARIO", "U: " + u.toString());

        //Log.i("PASSOU", "Passou 2: " + fotoUsuario);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

                if (response.code() == 200) {

                    String nUsuExiste = response.headers().get("nUsuExiste");
                    if (nUsuExiste.equals("1")) {
                        Toast.makeText(CadastroActivity.this, "Este nome de usuário já existe", Toast.LENGTH_SHORT).show();
                        alerta2.dismiss();
                        Log.i("[IFMG]", "Usuario ja existe");
                    } else if (nUsuExiste.equals("0")) {
                        String auth = response.headers().get("sucesso");
                        if (auth.equals("1")) {
                            Toast.makeText(CadastroActivity.this, "Cadastro efetuado com Sucesso", Toast.LENGTH_SHORT).show();
                            alerta2.dismiss();

                            Log.i("[IFMG]", "Sucesso");
                        } else {
                            Toast.makeText(CadastroActivity.this, "Algo falhou", Toast.LENGTH_SHORT).show();
                            alerta2.dismiss();
                            Log.i("[IFMG]", "Falhou");
                        }
                    }
                } else {
                    Toast.makeText(CadastroActivity.this, "Algo falhou", Toast.LENGTH_SHORT).show();
                    alerta2.dismiss();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(CadastroActivity.this, "Falha ao cadastrar usuario..", Toast.LENGTH_SHORT).show();
                alerta2.dismiss();
                Log.i("[IFMG]", "Falha ao baixar usuários: " + t.getMessage());
                t.printStackTrace();
            }
        });

    }

    private boolean validaCampos() {
        if (!nome.getText().toString().equals("") &&
                !pwd.getText().toString().equals("") &&
                !email.getText().toString().equals("") &&
                !nascimento.getText().toString().equals("") &&
                !telefone.getText().toString().equals("") &&
                !cpf.getText().toString().equals("") &&
                !rg.getText().toString().equals("") &&
                !logradouro.getText().toString().equals("") &&
                !bairro.getText().toString().equals("") &&
                !cidade.getText().toString().equals("") &&
                !numero.getText().toString().equals("") &&
                !uf.getText().toString().equals("") &&
                !cep.getText().toString().equals("") &&
                !complemento.getText().toString().equals("") &&
                !fotoUsuario.equals("")) {
            return true;
        } else {
            return false;
        }
    }


    public void showAletProgress(final String descricao) {
        final LayoutInflater li = getLayoutInflater();
        //inflamos o layout alerta.xml na view
        View view = li.inflate(R.layout.alert_progress, null);
        tvDesc = (TextView) view.findViewById(R.id.tvDesc);    //definimos para o botão do layout um clickListener
        tvDesc.setText(descricao);
        AlertDialog.Builder builder = new AlertDialog.Builder(CadastroActivity.this);
        builder.setTitle("Aguarde...");
        builder.setView(view);
        builder.setCancelable(false);
        alerta2 = builder.create();
        alerta2.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE_CODE && resultCode == RESULT_OK) {

            Bundle bundle = data.getExtras();

            //Todo arrumar aq
            Bitmap bitmap = (Bitmap) bundle.get("data");

            if (bitmap != null) {
                imagemSignin.setImageBitmap(bitmap);
                imagemSignin.setBackgroundColor(Color.WHITE);
                tvAddImagem.setText("Alterar imagem");
                tvAddImagem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dispatchTakePictureIntent();
                    }
                });
                addImagemBtn.setVisibility(View.GONE);

                UtilImageTransmit utilImageTransmit = new UtilImageTransmit();
                fotoUsuario = utilImageTransmit.convertImageToByte(bitmap);

                Log.i("FOTO", "" + fotoUsuario);
            } else Log.i("imagem", "imagemNula");

            //imageView.setImageBitmap(imageBitmap);
        } // TODO:
        /*else if (requestCode == REQUEST_WIFI_CONNECTION && resultCode == getActivity().RESULT_OK) {

            cadastrar2();

        } */
    }


    private void dispatchTakePictureIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE_CODE);
    }
}

