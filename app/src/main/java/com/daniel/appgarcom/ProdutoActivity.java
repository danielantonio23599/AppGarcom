package com.daniel.appgarcom;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.app.SearchManager;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.daniel.appgarcom.adapter.AdapterProduto;
import com.daniel.appgarcom.adapter.CustomItemClickListener;
import com.daniel.appgarcom.adapter.holder.DialogHelper;
import com.daniel.appgarcom.modelo.beans.PreferencesSettings;
import com.daniel.appgarcom.modelo.beans.Produto;
import com.daniel.appgarcom.modelo.beans.SharedPreferences;
import com.daniel.appgarcom.sync.RestauranteAPI;
import com.daniel.appgarcom.sync.SyncDefaut;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProdutoActivity extends AppCompatActivity {
    private int venda;
    private RecyclerView recyclerView;
    private AdapterProduto adapter;
    private ArrayList<Produto> produtos;
    private AlertDialog alerta;

    public void setVenda(int venda) {
        this.venda = venda;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produto);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        venda = intent.getIntExtra("venda", 0);
        recyclerView = (RecyclerView) findViewById(R.id.rvProduto);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        getUserListFromRestApi();
    }

    private void getUserListFromRestApi() {
        mostraDialog();
        SharedPreferences sh = PreferencesSettings.getAllPreferences(getApplicationContext());
        RestauranteAPI api = SyncDefaut.RETROFIT_RESTAURANTE(getApplicationContext()).create(RestauranteAPI.class);
        final Call<ArrayList<Produto>> call = api.listarProdutos(sh.getEmail(), sh.getSenha());
        call.enqueue(new Callback<ArrayList<Produto>>() {
            @Override
            public void onResponse(Call<ArrayList<Produto>> call, Response<ArrayList<Produto>> response) {
                if (response.isSuccessful()) {
                    String auth = response.headers().get("auth");
                    if (auth.equals("1")) {
                        escondeDialog();
                        produtos = new ArrayList<>(response.body());
                        onItemClick(produtos);

                    } else {
                        Log.i("[IFMG]", "login incorreto");
                        escondeDialog();
                        // senha ou usuario incorreto

                    }
                } else {
                    Log.i("[IFMG]", "servidor fora do ar");
                    escondeDialog();
                    //servidor fora do ar
                }


            }

            @Override
            public void onFailure(Call<ArrayList<Produto>> call, Throwable t) {
                escondeDialog();
                DialogHelper.getAlertWithMessage("Hata", t.getMessage(), ProdutoActivity.this);
            }
        });


    }

    private void onItemClick(ArrayList<Produto> produtos) {
        adapter = new AdapterProduto(ProdutoActivity.this, produtos, new CustomItemClickListener() {
            @Override
            public void onItemClick(Produto user, int position) {

                Toast.makeText(getApplicationContext(), "" + user.getNome(), Toast.LENGTH_SHORT).show();

            }
        });
        recyclerView.setAdapter(adapter);
    }


    private void mostraDialog() {

        final LayoutInflater li = getLayoutInflater();
        //inflamos o layout alerta.xml na view
        View view = li.inflate(R.layout.alert_progress, null);
        TextView tvDesc = (TextView) view.findViewById(R.id.tvDesc);    //definimos para o botão do layout um clickListener
        tvDesc.setText("Buscando pedios Pendentes...");
        AlertDialog.Builder builder = new AlertDialog.Builder(ProdutoActivity.this);
        builder.setTitle("Aguarde...");
        builder.setView(view);
        builder.setCancelable(false);
        alerta = builder.create();
        alerta.show();


    }

    private void escondeDialog() {

        if (alerta.isShowing()) {
            alerta.dismiss();
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_item, menu);
        MenuItem searchItem = menu.findItem(R.id.menu_search);

        SearchView searchView = null;
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

}
