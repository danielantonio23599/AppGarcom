package com.daniel.appgarcom;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.daniel.appgarcom.modelo.beans.Usuario;
import com.daniel.appgarcom.modelo.beans.PreferencesSettings;
import com.daniel.appgarcom.modelo.beans.SharedPreferences;
import com.daniel.appgarcom.modelo.beans.Usuario;
import com.daniel.appgarcom.modelo.persistencia.BdUsuario;
import com.daniel.appgarcom.sync.RestauranteAPI;
import com.daniel.appgarcom.sync.SyncDefaut;
import com.daniel.appgarcom.util.Criptografia;
import com.daniel.appgarcom.util.Data;
import com.daniel.appgarcom.util.MaskEditUtil;
import com.daniel.appgarcom.util.PermissionUtils;
import com.daniel.appgarcom.util.UtilImageTransmit;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PerfilActivity extends AppCompatActivity {
    private static final String TAG = "PerfilFragment";
    private static final String SELECT_PICTURE_TEXT_NO_PIC = "Selecionar foto";
    private static final String SELECT_PICTURE_TEXT_CHANGE_PIC = "Alterar foto";
    protected static final int RESULT_SPEECH = 1;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private static final int REQUEST_IMAGE_CAPTURE_CODE = 1;
    private AlertDialog alerta;
    private AlertDialog alerta2;
    private EditText nome, email, telefone, rg, cpf, pwd, logradouro, bairro, numero, uf, cidade, complemento, nascimento, cep;
    private Button buttonCadastro;
    private ImageButton map, view_pwd;
    private TextView tvDesc, tvAddImagem;
    private ProgressBar progresAndress;
    //private LinearLayout pic_selection_section;
    CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton fab;
    ImageView image;
    private Usuario u = new Usuario();

    private static byte[] fotoUsuario;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfi);
        image = (ImageView) findViewById(R.id.image);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                dispatchTakePictureIntent();
            }
        });

        String[] permissoes = new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.INTERNET,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
        };
        PermissionUtils.validate(PerfilActivity.this, 0, permissoes);
        email = (EditText) findViewById(R.id.input_email);
        nome = (EditText) findViewById(R.id.input_name);
        pwd = (EditText) findViewById(R.id.input_senha);
        nascimento = (EditText) findViewById(R.id.input_data_nascimento);
        nascimento.addTextChangedListener(MaskEditUtil.mask(nascimento, MaskEditUtil.FORMAT_DATE));
        telefone = (EditText) findViewById(R.id.input_telefone);
        telefone.addTextChangedListener(MaskEditUtil.mask(telefone, MaskEditUtil.FORMAT_FONE));
        cpf = (EditText) findViewById(R.id.input_cpf);
        cpf.addTextChangedListener(MaskEditUtil.mask(cpf, MaskEditUtil.FORMAT_CPF));
        rg = (EditText) findViewById(R.id.input_rg);
        logradouro = (EditText) findViewById(R.id.input_logradouro);
        bairro = (EditText) findViewById(R.id.input_bairro);
        numero = (EditText) findViewById(R.id.input_numero);
        cidade = (EditText) findViewById(R.id.input_cidade);
        complemento = (EditText) findViewById(R.id.input_complemento);
        uf = (EditText) findViewById(R.id.input_uf);
        uf.addTextChangedListener(MaskEditUtil.mask(uf, "##"));
        cep = (EditText) findViewById(R.id.input_cep);
        cep.addTextChangedListener(MaskEditUtil.mask(cep, MaskEditUtil.FORMAT_CEP));
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        buttonCadastro = (Button) findViewById(R.id.btnSalvarDados);

        buttonCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (validaCampos()) {
                    showAletProgress("Aguarde enquanto é cadastrado...");
                    cadastrar();
                } else {
                    Log.d(TAG, "onClick: Campos Vazios");
                    Toast.makeText(getApplicationContext(), "Preencha todos os campos.", Toast.LENGTH_SHORT).show();
                }

            }
        });
        BdUsuario bdUsuario = new BdUsuario(getApplicationContext());
        setDados(bdUsuario.listar());
        bdUsuario.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void cadastrar() {
        BdUsuario bdUsuario = new BdUsuario(getApplicationContext());
        u = bdUsuario.listar();
        bdUsuario.close();
        u.setDataNacimento(Data.formataDataUS(nascimento.getText() + ""));
        u.setNome(nome.getText() + "");
        u.setTelefone(telefone.getText() + "");
        u.setEmail(email.getText() + "");
        u.setSenha(Criptografia.criptografar(pwd.getText() + ""));
        u.setCPF(cpf.getText() + "");
        u.setRG(rg.getText() + "");
        u.setLogradouro(logradouro.getText() + "");
        u.setBairro(bairro.getText() + "");
        u.setCidade(cidade.getText() + "");
        u.setNumero(numero.getText() + "");
        u.setUf(uf.getText() + "");
        u.setCep(cep.getText() + "");
        u.setComplemento(complemento.getText() + "");
        u.setFoto(fotoUsuario);

        Log.i("PASSOU", "Passou 1");

        RestauranteAPI i = SyncDefaut.RETROFIT_RESTAURANTE(this).create(RestauranteAPI.class);
        SharedPreferences s = PreferencesSettings.getAllPreferences(getApplicationContext());
        Log.i("IFMG", "Senha: " + u.getSenha());
        final Call<Void> call = i.atualizarFuncionario(new Gson().toJson(u), s.getEmail(), u.getSenha());

        Log.i("USUARIO", "U: " + u.toString());

        //Log.i("PASSOU", "Passou 2: " + fotoUsuario);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() == 200) {

                    String auth = response.headers().get("auth");
                    if (auth.equals("1")) {
                        BdUsuario bdUsuario = new BdUsuario(getApplicationContext());
                        bdUsuario.deleteAll();
                        u.setSenha(pwd.getText() + "");
                        bdUsuario.insert(u);
                        bdUsuario.close();
                        Toast.makeText(getApplicationContext(), "Usuario Atualizado", Toast.LENGTH_SHORT).show();
                        alerta2.dismiss();

                    } else {
                        Toast.makeText(getApplicationContext(), "Algo falhou", Toast.LENGTH_SHORT).show();
                        alerta2.dismiss();
                        Log.i("[IFMG]", "Falhou");
                    }

                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Falha ao cadastrar usuario..", Toast.LENGTH_SHORT).show();
                alerta2.dismiss();
                Log.i("[IFMG]", "Falha ao baixar usuários: " + t.getMessage());
                t.printStackTrace();
            }
        });

    }

    private void setDados(Usuario u) {
        nascimento.setText(Data.formataDataBR(u.getDataNacimento()));
        nome.setText(u.getNome());
        //nome
        collapsingToolbarLayout.setTitle(u.getNome());
        telefone.setText(u.getTelefone());
        email.setText(u.getEmail());
        pwd.setText(u.getSenha());
        cpf.setText(u.getCPF());
        rg.setText(u.getRG());
        logradouro.setText(u.getLogradouro());
        bairro.setText(u.getBairro());
        cidade.setText(u.getCidade());
        numero.setText(u.getNumero());
        uf.setText(u.getUf());
        cep.setText(u.getCep());
        complemento.setText(u.getComplemento());
        UtilImageTransmit utilImageTransmit = new UtilImageTransmit();
        Bitmap bitmap = UtilImageTransmit.convertBytetoImage(u.getFoto());
        if (bitmap != null)
            image.setImageBitmap(bitmap);
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
                fotoUsuario != null) {
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
        AlertDialog.Builder builder = new AlertDialog.Builder(PerfilActivity.this);
        builder.setTitle("Aguarde...");
        builder.setView(view);
        builder.setCancelable(false);
        alerta2 = builder.create();
        alerta2.show();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE_CODE && resultCode == PerfilActivity.RESULT_OK) {
            Bundle bundle = data.getExtras();
            //Todo arrumar aq
            Bitmap bitmap = (Bitmap) bundle.get("data");

            if (bitmap != null) {
                image.setImageBitmap(bitmap);
                UtilImageTransmit utilImageTransmit = new UtilImageTransmit();
                fotoUsuario = utilImageTransmit.convertImageToByte(bitmap);

                Log.i("[IFMG]", "" + fotoUsuario);
            } else Log.i("imagem", "imagemNula");

            //imageView.setImageBitmap(imageBitmap);
        } // TODO:
        /*else if (requestCode == REQUEST_WIFI_CONNECTION && resultCode == getActivity().RESULT_OK) {

            cadastrar2();

        } */
    }


    private void dispatchTakePictureIntent() {
        //galeria
        //Intent.ACTION_GET_CONTENT
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE_CODE);
    }
}
