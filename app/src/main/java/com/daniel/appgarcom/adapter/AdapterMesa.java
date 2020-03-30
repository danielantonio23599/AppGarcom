package com.daniel.appgarcom.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;


import com.daniel.appgarcom.R;
import com.daniel.appgarcom.adapter.holder.Mesa;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Daniel on 30/05/2018.
 */

public class AdapterMesa extends BaseAdapter {
    private List<Mesa> lin = new ArrayList<Mesa>();

    public List<Mesa> getLin() {
        return lin;
    }

    public void setLin(List<Mesa> lin) {
        this.lin = lin;
    }

    private Context context;

    public AdapterMesa(Context context) {
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

    @SuppressLint("ResourceAsColor")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.i("[IFMG]", "view: " + lin.get(position).getMesa());
        View view = LayoutInflater.from(context).inflate(R.layout.mesa_adapter, parent, false);
        TextView mesa = (TextView) view.findViewById(R.id.tvMesa);
        CardView back = (CardView) view.findViewById(R.id.back);
        mesa.setText(lin.get(position).getMesa() + "");
        Log.i("[IFMG]", "status : " + lin.get(position).getStatus());
        switch (lin.get(position).getStatus()) {
            case "aberta":
                Log.i("[IFMG]", " aberta ");
                back.setCardBackgroundColor(0xFF07935B);
                break;
            case "fechada":
                back.setCardBackgroundColor(Color.RED);
                break;
            case "pendente":
                back.setBackgroundColor(Color.YELLOW);
                break;
            default:
                back.setCardBackgroundColor(0xFF07935B);
                break;
        }
        return view;
    }
}
