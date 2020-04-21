package com.daniel.appgarcom;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.daniel.appgarcom.adapter.AdapterProduto;
import com.daniel.appgarcom.fragment.ProdutoFragment;
import com.daniel.appgarcom.modelo.beans.Produto;

import java.util.ArrayList;

public class ProdutoActivity extends AppCompatActivity {
    private int venda;
    private RecyclerView recyclerView;
    private AdapterProduto adapter;
    private ArrayList<Produto> produtos;
    private AlertDialog alerta;
    private Toast toast;
    private long lastBackPressTime = 0;
    private ProdutoFragment produtoFragment = new ProdutoFragment();

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
        produtoFragment.setVenda(venda);
        replaceFragment(produtoFragment);
    }

    public void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        getSupportFragmentManager().beginTransaction().replace(R.id.nav_produto_fragment, fragment, "IFMG").addToBackStack(null).commit();
    }

    @Override
    public void onBackPressed() {
        if (this.lastBackPressTime < System.currentTimeMillis() - 2000) {
            toast = Toast.makeText(this, "Pressione o Botão Voltar novamente para finalizar a viagem", Toast.LENGTH_SHORT);
            toast.show();
            this.lastBackPressTime = System.currentTimeMillis();
        } else {
            if (toast != null) {
                toast.cancel();
                super.onBackPressed();
            }

        }
    }

    @Override
    public void finish() {
        super.finish();
    }
}
