package com.daniel.appgarcom.adapter;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;


import com.daniel.appgarcom.R;
import com.daniel.appgarcom.adapter.holder.Pedido;
import com.daniel.appgarcom.util.Time;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Daniel on 30/05/2018.
 */

public class AdapterPedidos extends BaseAdapter {
    private List<Pedido> lin = new ArrayList<Pedido>();

    public List<Pedido> getLin() {
        return lin;
    }

    public void setLin(List<Pedido> lin) {
        this.lin = lin;
    }

    private Context context;

    public AdapterPedidos(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return lin.size();
    }

    @Override
    public Object getItem(int position) {
        return lin.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.i("[IFMG]", "view: " + lin.get(position).getProduto());
        View view = LayoutInflater.from(context).inflate(R.layout.pedidos_adapter, parent, false);
        TextView produto = (TextView) view.findViewById(R.id.tvProduto);
        TextView status = (TextView) view.findViewById(R.id.tvStatus);
        TextView observacao = (TextView) view.findViewById(R.id.tvObservacao);
        TextView mesa = (TextView) view.findViewById(R.id.tvNunMesa);
        TextView quantidade = (TextView) view.findViewById(R.id.tvQuantidade);
        TextView tempoEspera = (TextView) view.findViewById(R.id.tvTempoEspera);
        FrameLayout layout = (FrameLayout) view.findViewById(R.id.layFundo);
        produto.setText(lin.get(position).getProduto() + "");
        status.setText(lin.get(position).getStatus());
        observacao.setText(lin.get(position).getObservacao());
        quantidade.setText(lin.get(position).getQuantidade() + "");
        tempoEspera.setText(Time.subtrairHoras(lin.get(position).getHora_pedido()));
        mesa.setText(lin.get(position).getMesa() + "");
        return view;
    }
}
