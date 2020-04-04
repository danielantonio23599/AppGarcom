package com.daniel.appgarcom.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;


import com.daniel.appgarcom.R;
import com.daniel.appgarcom.adapter.AdapterItemPedido;
import com.daniel.appgarcom.adapter.holder.Item;
import com.daniel.appgarcom.adapter.holder.PedidoBEAN;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class VendaFragment extends Fragment {

    private ArrayList<PedidoBEAN> pedidos = new ArrayList<>();
    private PedidoBEAN item = new PedidoBEAN();

    private int venda;

    public void addItem(PedidoBEAN i) {
        item = i;
        // item.add(i);
    }

    public void setVenda(int venda) {
        this.venda = venda;
    }

    private ListView listView;
    private EditText etNome;
    private EditText etObs;
    private EditText etQTD;
    private Button add;
    private Menu menu;
    private MenuInflater menuInflater;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_venda, container, false);
        setHasOptionsMenu(true);
        listView = (ListView) view.findViewById(R.id.lvPedidos);
        etNome = (EditText) view.findViewById(R.id.input_produto);
        etObs = (EditText) view.findViewById(R.id.input_obs);
        etQTD = (EditText) view.findViewById(R.id.input_quantidade);
        add = (Button) view.findViewById(R.id.btnAdd);
        //etNome.setText(item.getProNome());
        atualizaTabela();

        FloatingActionButton fab = view.findViewById(R.id.fab_venda);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProdutoFragment p = new ProdutoFragment();
                p.setVenda(venda);
                p.setVendaFragment(VendaFragment.this);
                replaceFragment(p);
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item.setObservacao(etObs.getText() + "");
                item.setQuantidade(Float.parseFloat(etQTD.getText() + ""));
                pedidos.add(item);
                atualizaTabela();
                onCreateOptionsMenu(menu, menuInflater);
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("[IFMG]", "OnResume : ");
        etNome.setText(item.getProNome());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_venda, menu);
        MenuItem mesa = menu.findItem(R.id.menu_mesa);
        MenuItem valor = menu.findItem(R.id.menu_valor);
        mesa.setTitle("Mesa ( " + item.getVenda() + " )");
        float total = 0;
        for (PedidoBEAN p : pedidos) {
            total += (p.getValor() * Float.parseFloat(etQTD.getText() + ""));
        }
        valor.setTitle("R$ " + total + "");
        super.onCreateOptionsMenu(menu, inflater);
        this.menu = menu;
        this.menuInflater = inflater;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.menu_salvar:
                encaminharPedido();
                return true;

        }
        return false;
    }

    private void encaminharPedido() {

        //Todo selvet para cadastrar pedido


    }

    public void atualizaTabela() {
        Log.i("[IFMG]", "mesas : " + pedidos.size());
        AdapterItemPedido s = new AdapterItemPedido(getActivity());
        if (pedidos.size() > 0) {
            s.setLin(pedidos);
            listView.setAdapter(s);
        } else {
            s.setLin(pedidos);
            listView.setAdapter(s);
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getActivity(), "nenhuma mesa nova!! ", Toast.LENGTH_SHORT);
                }
            });

        }
    }

    public void replaceFragment(Fragment fragment) {
        getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_produto_fragment, fragment, "produtos").addToBackStack(null).commit();
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }
}
