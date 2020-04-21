package com.daniel.appgarcom.fragment;

import android.app.AlertDialog;
import android.content.Intent;
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
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.daniel.appgarcom.ProdutoActivity;
import com.daniel.appgarcom.R;
import com.daniel.appgarcom.adapter.AdapterMesaR;

import com.daniel.appgarcom.adapter.interfaces.MesaItemClickListener;

import com.daniel.appgarcom.adapter.holder.Mesa;
import com.daniel.appgarcom.modelo.beans.Empresa;
import com.daniel.appgarcom.modelo.beans.PreferencesSettings;
import com.daniel.appgarcom.modelo.beans.SharedPreferences;
import com.daniel.appgarcom.modelo.persistencia.BdEmpresa;
import com.daniel.appgarcom.sync.RestauranteAPI;
import com.daniel.appgarcom.sync.SyncDefaut;
import com.daniel.appgarcom.util.TecladoUtil;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MesaFragment extends Fragment {
    private RecyclerView recyclerView;
    private AdapterMesaR adapter;
    private ArrayList<Mesa> mesa;
    private AlertDialog alerta;
    private EditText numMesa;
    private AlertDialog alerta2;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmente_mesas, container, false);
        setHasOptionsMenu(true);
        recyclerView = (RecyclerView) view.findViewById(R.id.rvMesa);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 3);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        getUserListFromRestApi();
        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAbrirMesa();
            }
        });
        return view;
    }

    private void showAbrirMesa() {
        final LayoutInflater li = getLayoutInflater();
        //inflamos o layout alerta.xml na view
        final View view = li.inflate(R.layout.alert_add_mesa, null);
        numMesa = (EditText) view.findViewById(R.id.etNumeroMesa);
        //definimos para o botão do layout um clickListener
        Button ok = (Button) view.findViewById(R.id.btnOk);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!numMesa.getText().equals("")) {
                    TecladoUtil.hideKeyboard(getActivity(), view);
                    abrirMesa(Integer.parseInt(numMesa.getText() + ""));
                    alerta2.dismiss();
                } else {
                    Toast.makeText(getActivity(), "Campo vasio", Toast.LENGTH_SHORT);
                }
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

    private void abrirMesa(int mesa) {
        mostraDialog();
        SharedPreferences sh = PreferencesSettings.getAllPreferences(getContext());
        RestauranteAPI api = SyncDefaut.RETROFIT_RESTAURANTE(getContext()).create(RestauranteAPI.class);
        final Call<Void> call = api.abrirMesa(sh.getEmail(), sh.getSenha(), mesa + "");
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    String auth = response.headers().get("auth");
                    final String sucesso = response.headers().get("sucesso");
                    if (auth.equals("1")) {
                        escondeDialog();

                        if (sucesso.equals("0")) {
                            Log.i("[IFMG]", "sucesso : " + sucesso);
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    Toast.makeText(getContext(), "Mesa já Aberta", Toast.LENGTH_LONG).show();
                                }
                            });

                        } else if (sucesso.equals("-1")) {
                            Toast.makeText(getContext(), "Caixa está fechado", Toast.LENGTH_LONG).show();
                        } else {
                            getUserListFromRestApi();
                        }
                    } else {
                        escondeDialog();
                        // senha ou usuario incorreto

                    }
                } else {
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

    private void getUserListFromRestApi() {
        mostraDialog();
        BdEmpresa bd = new BdEmpresa(getActivity());
        Empresa sh = bd.listar();
        bd.close();
        RestauranteAPI api = SyncDefaut.RETROFIT_RESTAURANTE(getContext()).create(RestauranteAPI.class);
        final Call<ArrayList<Mesa>> call = api.getMesasAbertas(sh.getEmpEmail(), sh.getEmpSenha());
        call.enqueue(new Callback<ArrayList<Mesa>>() {
            @Override
            public void onResponse(Call<ArrayList<Mesa>> call, Response<ArrayList<Mesa>> response) {
                if (response.isSuccessful()) {
                    String auth = response.headers().get("auth");
                    String sucesso = response.headers().get("sucesso");
                    if (auth.equals("1")) {
                        if (sucesso.equals("0")) {
                            escondeDialog();
                            Toast.makeText(getActivity(), "Caixa está fechado", Toast.LENGTH_LONG).show();
                        } else {
                            Log.i("[IFMG]", "login correto");
                            mesa = new ArrayList<>(response.body());
                            escondeDialog();
                            onItemClick(mesa);
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
            public void onFailure(Call<ArrayList<Mesa>> call, Throwable t) {
                Log.i("[IFMG]", "servidor com erro " + t.getMessage());
                escondeDialog();
            }
        });


    }

    private void onItemClick(ArrayList<Mesa> mesa) {
        adapter = new AdapterMesaR(getActivity(), mesa, new MesaItemClickListener() {
            @Override
            public void onItemClick(Mesa user, int position) {
                Intent intent = new Intent(getActivity(), ProdutoActivity.class);
                intent.putExtra("venda", user.getMesa());
                startActivity(intent);
                // Toast.makeText(getContext(), "" + user.getStatus(), Toast.LENGTH_SHORT).show();

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
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_item, menu);
        MenuItem item = menu.findItem(R.id.menu_search);
        //item.setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItem.SHOW_AS_ACTION_IF_ROOM);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                // Here is where we are going to implement the filter logic
                return true;
            }

        });
        super.onCreateOptionsMenu(menu, inflater);
    }

}
