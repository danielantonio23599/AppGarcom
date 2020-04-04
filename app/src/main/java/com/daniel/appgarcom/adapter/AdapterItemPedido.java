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
import com.daniel.appgarcom.adapter.holder.Item;
import com.daniel.appgarcom.adapter.holder.Pedido;
import com.daniel.appgarcom.adapter.holder.PedidoBEAN;
import com.daniel.appgarcom.util.Time;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Daniel on 30/05/2018.
 */

public class AdapterItemPedido extends BaseAdapter {
    private List<PedidoBEAN> lin = new ArrayList<PedidoBEAN>();

    public List<PedidoBEAN> getLin() {
        return lin;
    }

    public void setLin(List<PedidoBEAN> lin) {
        this.lin = lin;
    }

    private Context context;

    public AdapterItemPedido(Context context) {
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
        View view = LayoutInflater.from(context).inflate(R.layout.produto_pedido_adapter, parent, false);
        TextView quantidade = (TextView) view.findViewById(R.id.tvQTD);
        TextView valor = (TextView) view.findViewById(R.id.tvValor);
        TextView item = (TextView) view.findViewById(R.id.tvItemPedido);
        item.setText(lin.get(position).getProNome());
        float qtd = lin.get(position).getQuantidade();
        quantidade.setText(qtd + "");
        float val = (qtd * lin.get(position).getValor());
        valor.setText("R$ " + val);
        return view;
    }
}
