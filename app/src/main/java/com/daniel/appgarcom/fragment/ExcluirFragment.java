package com.daniel.appgarcom.fragment;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
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

import com.daniel.appgarcom.R;
import com.daniel.appgarcom.adapter.AdapterPedidos;
import com.daniel.appgarcom.adapter.holder.Pedido;
import com.daniel.appgarcom.adapter.holder.PedidoBEAN;
import com.daniel.appgarcom.modelo.beans.Empresa;
import com.daniel.appgarcom.modelo.beans.PreferencesSettings;
import com.daniel.appgarcom.modelo.beans.SharedPreferences;
import com.daniel.appgarcom.modelo.persistencia.BdEmpresa;
import com.daniel.appgarcom.sync.RestauranteAPI;
import com.daniel.appgarcom.sync.SyncDefaut;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExcluirFragment extends Fragment implements AdapterView.OnItemClickListener {
    private ArrayList<Pedido> pp = new ArrayList<Pedido>();
    private AlertDialog alerta;
    private ListView listView;
    private TextView tvMessage;
    private AlertDialog alerta2;
    private AlertDialog alerta3;
    private EditText etMotivo;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_excluir, container, false);
        listView = (ListView) view.findViewById(R.id.lvExcluir);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        atualizarFragment();
    }

    public void atualizarFragment() {
        atualizaPedidos();
    }

    private void atualizaPedidos() {
        Log.i("[IFMG]", "faz selveti buscando pedidos pendentes");
        mostraDialog();
        RestauranteAPI api = SyncDefaut.RETROFIT_RESTAURANTE(getContext()).create(RestauranteAPI.class);
        BdEmpresa bd = new BdEmpresa(getActivity());
        Empresa e = bd.listar();
        bd.close();
        final Call<ArrayList<Pedido>> call = api.listarPedidosPendentes(e.getEmpEmail(), e.getEmpSenha() + "");

        call.enqueue(new Callback<ArrayList<Pedido>>() {
            @Override
            public void onResponse(Call<ArrayList<Pedido>> call, Response<ArrayList<Pedido>> response) {
                if (response.code() == 200) {
                    String auth = response.headers().get("auth");
                    if (auth.equals("1")) {
                        escondeDialog();
                        ArrayList<Pedido> u = response.body();
                        atualizaTabela(u);
                    } else {
                        escondeDialog();
                        Toast.makeText(getActivity().getBaseContext(), "Nome de usuário ou senha incorretos", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    escondeDialog();
                    Toast.makeText(getActivity().getBaseContext(), "Erro ao fazer login, erro servidor", Toast.LENGTH_SHORT).show();
                    Log.i("[IFMG]", "t1: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Pedido>> call, Throwable t) {
                escondeDialog();
                Toast.makeText(getActivity().getBaseContext(), "Erro ao fazer login, falhaaaaa", Toast.LENGTH_SHORT).show();
                Log.i("[IFMG]", "faz login");
                Log.i("Teste", "t2: " + t.getMessage());
                //mudaActivity(MainActivity.class);
            }
        });

    }

    private void mostraDialog() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final LayoutInflater li = getLayoutInflater();
                //inflamos o layout alerta.xml na view
                View view = li.inflate(R.layout.alert_progress, null);
                TextView tvDesc = (TextView) view.findViewById(R.id.tvDesc);    //definimos para o botão do layout um clickListener
                tvDesc.setText("Buscando pedios Pendentes...");
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Aguarde...");
                builder.setView(view);
                builder.setCancelable(false);
                alerta = builder.create();
                alerta.show();
            }
        });

    }


    private void escondeDialog() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (alerta.isShowing()) {
                    alerta.dismiss();
                }
            }
        });

    }

    public void atualizaTabela(ArrayList<Pedido> pedidos) {
        Log.i("[IFMG]", "pedidos: " + pedidos.size());
        AdapterPedidos s = new AdapterPedidos(getActivity());
        if (pedidos.size() > 0) {
            s.setLin(pedidos);
            listView.setAdapter(s);
            listView.setOnItemClickListener(ExcluirFragment.this);
        } else {
            s.setLin(pedidos);
            listView.setAdapter(s);
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getActivity(), "nenhum pedio Novo!! ", Toast.LENGTH_SHORT);
                }
            });

        }


    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //Todo excluir
        AdapterPedidos p = (AdapterPedidos) parent.getAdapter();
        Pedido pedido = p.getLin().get(position);
        showDelet(pedido);
    }


    public void replaceFragment(Fragment fragment) {
        getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, fragment, "IFMG").addToBackStack(null).commit();
    }

    public void showDelet(final Pedido p) {
        //LayoutInflater é utilizado para inflar nosso layout em uma view.
        //-pegamos nossa instancia da classe
        final LayoutInflater li = getLayoutInflater();

        //inflamos o layout alerta.xml na view
        final View view = li.inflate(R.layout.alert_remover_pedido, null);
        tvMessage = (TextView) view.findViewById(R.id.tvMessage);
        tvMessage.setText("Tem serteza que deseja excluir o pedido selecionado ?");
        //definimos para o botão do layout um clickListener
        Button ok = (Button) view.findViewById(R.id.btnOk);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMotivo(p);
                alerta2.dismiss();
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

    public void showMotivo(final Pedido p) {
        //LayoutInflater é utilizado para inflar nosso layout em uma view.
        //-pegamos nossa instancia da classe
        final LayoutInflater li = getLayoutInflater();

        //inflamos o layout alerta.xml na view
        final View view = li.inflate(R.layout.alert_motivo, null);
        etMotivo = (EditText) view.findViewById(R.id.etMotivo);
        //definimos para o botão do layout um clickListener
        Button ok = (Button) view.findViewById(R.id.btnOk);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                excluirPedido(p, etMotivo.getText() + "");
                alerta3.dismiss();
            }
        });
        Button cancelar = (Button) view.findViewById(R.id.btnCancelar);
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alerta3.dismiss();
            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        alerta3 = builder.create();
        alerta3.show();
    }

    private void excluirPedido(Pedido p, String motivo) {
        Log.i("[IFMG]", "faz selveti buscando pedidos pendentes");
        mostraDialog();
        RestauranteAPI api = SyncDefaut.RETROFIT_RESTAURANTE(getContext()).create(RestauranteAPI.class);
        SharedPreferences sh = PreferencesSettings.getAllPreferences(getContext());
               final Call<Void> call = api.cancelarPedido(p.getCodigo() + "", motivo + "", sh.getEmail(), sh.getSenha());

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() == 200) {
                    String auth = response.headers().get("auth");
                    if (auth.equals("1")) {
                        escondeDialog();
                        Toast.makeText(getActivity().getBaseContext(), "Pedido Cancelado !!!", Toast.LENGTH_SHORT).show();
                        atualizaPedidos();
                    } else {
                        escondeDialog();
                        Toast.makeText(getActivity().getBaseContext(), "Nome de usuário ou senha incorretos", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    escondeDialog();
                    Toast.makeText(getActivity().getBaseContext(), "Erro ao fazer login, erro servidor", Toast.LENGTH_SHORT).show();
                    Log.i("[IFMG]", "t1: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                escondeDialog();
                Toast.makeText(getActivity().getBaseContext(), "Erro ao fazer login, falhaaaaa", Toast.LENGTH_SHORT).show();
                Log.i("[IFMG]", "faz login");
                Log.i("Teste", "t2: " + t.getMessage());
                //mudaActivity(MainActivity.class);
            }
        });

    }
}
