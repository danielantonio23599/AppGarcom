package com.daniel.appgarcom.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.daniel.appgarcom.R;
import com.daniel.appgarcom.adapter.holder.Mesa;

import java.util.ArrayList;

public class AdapterMesaR extends RecyclerView.Adapter<AdapterMesaR.MyViewHolder> implements Filterable {

    private ArrayList<Mesa> mesas;
    private ArrayList<Mesa> filteredMesas;
    private Context context;
    private MesaItemClickListener customItemClickListener;

    public AdapterMesaR(Context context, ArrayList<Mesa> userArrayList, MesaItemClickListener customItemClickListener) {
        this.context = context;
        this.mesas = userArrayList;
        this.filteredMesas = userArrayList;
        this.customItemClickListener = customItemClickListener;
    }

    @Override
    public AdapterMesaR.MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.mesa_adapter, viewGroup, false);
        final MyViewHolder myViewHolder = new MyViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // for click item listener
                //Toast.makeText(context,"click " + myViewHolder.getAdapterPosition(),Toast.LENGTH_SHORT);
                customItemClickListener.onItemClick(filteredMesas.get(myViewHolder.getAdapterPosition()), myViewHolder.getAdapterPosition());
            }
        });
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(AdapterMesaR.MyViewHolder viewHolder, int position) {
        Log.i("[IFMG]", " mesa : " + filteredMesas.get(position).getMesa() + "");
        viewHolder.mesa.setText(filteredMesas.get(position).getMesa() + "");
        switch (filteredMesas.get(position).getStatus()) {
            case "aberta":
                Log.i("[IFMG]", " aberta ");
                viewHolder.back.setCardBackgroundColor(0xFF07935B);
                break;
            case "fechada":
                viewHolder.back.setCardBackgroundColor(Color.RED);
                break;
            case "pendente":
                viewHolder.back.setBackgroundColor(Color.YELLOW);
                break;
            default:
                viewHolder.back.setCardBackgroundColor(0xFF07935B);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return filteredMesas.size();
    }

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String searchString = charSequence.toString();

                if (searchString.isEmpty()) {

                    filteredMesas = mesas;

                } else {

                    ArrayList<Mesa> tempFilteredList = new ArrayList<>();

                    for (Mesa user : mesas) {
                        // search for user name
                        if ((user.getMesa() + "").toLowerCase().contains(searchString)) {
                            tempFilteredList.add(user);
                        }
                    }

                    filteredMesas = tempFilteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredMesas;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredMesas = (ArrayList<Mesa>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView mesa;
        private CardView back;

        public MyViewHolder(View view) {
            super(view);
            mesa = (TextView) view.findViewById(R.id.tvMesaNum);
            back = (CardView) view.findViewById(R.id.back);


        }
    }
}
