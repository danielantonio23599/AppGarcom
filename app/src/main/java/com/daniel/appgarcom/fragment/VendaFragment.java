package com.daniel.appgarcom.fragment;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;


import com.daniel.appgarcom.PrincipalActivity;
import com.daniel.appgarcom.R;
import com.daniel.appgarcom.adapter.AdapterItemPedido;
import com.daniel.appgarcom.adapter.holder.Item;
import com.daniel.appgarcom.adapter.holder.Mesa;
import com.daniel.appgarcom.adapter.holder.PedidoBEAN;
import com.daniel.appgarcom.modelo.beans.Empresa;
import com.daniel.appgarcom.modelo.beans.PreferencesSettings;
import com.daniel.appgarcom.modelo.beans.Servidor;
import com.daniel.appgarcom.modelo.beans.SharedPreferences;
import com.daniel.appgarcom.modelo.persistencia.BdEmpresa;
import com.daniel.appgarcom.modelo.persistencia.BdServidor;
import com.daniel.appgarcom.sync.RestauranteAPI;
import com.daniel.appgarcom.sync.SyncDefaut;
import com.daniel.appgarcom.util.TecladoUtil;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VendaFragment extends Fragment implements AdapterView.OnItemClickListener {

    private ArrayList<PedidoBEAN> pedidos = new ArrayList<>();
    private PedidoBEAN item = new PedidoBEAN();
    private AlertDialog alerta;
    private TextView tvMessage;

    private int venda;
    private AlertDialog alerta2;

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
                if (item != null) {
                    item.setObservacao(etObs.getText() + "");
                    item.setQuantidade(Float.parseFloat(etQTD.getText() + ""));
                    pedidos.add(item);
                    atualizaTabela();
                    onCreateOptionsMenu(menu, menuInflater);
                    limparDados();
                } else {
                    Toast.makeText(getActivity(), "Nenhum produto selecionado!!", Toast.LENGTH_LONG).show();
                }
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
        if (item != null) {
            mesa.setTitle("Mesa ( " + item.getVenda() + " )");
        }
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
        mostraDialog();
        BdEmpresa bd = new BdEmpresa(getActivity());
        Empresa sh = bd.listar();
        bd.close();
        RestauranteAPI api = SyncDefaut.RETROFIT_RESTAURANTE(getContext()).create(RestauranteAPI.class);
        final Call<Void> call = api.enviarPedidos(new Gson().toJson(pedidos), sh.getEmpEmail(), sh.getEmpSenha());
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    String auth = response.headers().get("auth");
                    String sucesso = response.headers().get("sucesso");
                    if (auth.equals("1")) {
                        if (sucesso.equals("Sucesso")) {
                            escondeDialog();
                            Toast.makeText(getActivity(), "Pedidos enviados com sucesso!!", Toast.LENGTH_LONG).show();
                            getActivity().finish();
                        } else {
                            Log.i("[IFMG]", "Algo errado");
                            escondeDialog();
                            Toast.makeText(getActivity(), sucesso, Toast.LENGTH_LONG).show();
                            getActivity().finish();
                        }

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
            public void onFailure(Call<Void> call, Throwable t) {
                Log.i("[IFMG]", "servidor com erro " + t.getMessage());
                escondeDialog();
            }
        });


    }

    private void limparDados() {
        etNome.setText("");
        item = null;
    }

    private void mostraDialog() {

        final LayoutInflater li = getLayoutInflater();
        //inflamos o layout alerta.xml na view
        View view = li.inflate(R.layout.alert_progress, null);
        TextView tvDesc = (TextView) view.findViewById(R.id.tvDesc);    //definimos para o botão do layout um clickListener
        tvDesc.setText("Enviando pedidos...");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Aguarde...");
        builder.setView(view);
        builder.setCancelable(false);
        alerta = builder.create();
        alerta.show();
    }

    public void showDelet(final PedidoBEAN p) {
        //LayoutInflater é utilizado para inflar nosso layout em uma view.
        //-pegamos nossa instancia da classe
        final LayoutInflater li = getLayoutInflater();

        //inflamos o layout alerta.xml na view
        final View view = li.inflate(R.layout.alert_remover_pedido, null);
        tvMessage = (TextView) view.findViewById(R.id.tvMessage);
        tvMessage.setText("Tem serteza que deseja remover o produto selecionado ?");
        //definimos para o botão do layout um clickListener
        Button ok = (Button) view.findViewById(R.id.btnOk);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pedidos.remove(p);
                alerta2.dismiss();
                atualizaTabela();
                onCreateOptionsMenu(menu, menuInflater);
            }
        });
        Button cancelar = (Button) view.findViewById(R.id.btnCancelar);
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alerta2.dismiss();
            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        alerta2 = builder.create();
        alerta2.show();
    }

    private void escondeDialog() {
        if (alerta.isShowing()) {
            alerta.dismiss();
        }
    }

    public void atualizaTabela() {
        Log.i("[IFMG]", "mesas : " + pedidos.size());
        AdapterItemPedido s = new AdapterItemPedido(getActivity());
        if (pedidos.size() > 0) {
            s.setLin(pedidos);
            listView.setAdapter(s);
            listView.setOnItemClickListener(VendaFragment.this);
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


    private void mudaActivity(final Class classe) {
        Log.i("[IFMG]", "passou no muda actyvity" + classe.getName());
        final Intent intent = new Intent(getActivity(), classe);
        startActivity(intent);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        AdapterItemPedido p = (AdapterItemPedido) parent.getAdapter();
        PedidoBEAN pedido = p.getLin().get(position);
        showDelet(pedido);

    }
}
