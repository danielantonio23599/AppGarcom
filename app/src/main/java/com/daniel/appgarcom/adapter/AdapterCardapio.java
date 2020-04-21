package com.daniel.appgarcom.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.daniel.appgarcom.R;
import com.daniel.appgarcom.adapter.interfaces.CardapioItemClickListener;
import com.daniel.appgarcom.modelo.beans.Produto;
import com.daniel.appgarcom.util.UtilImageTransmit;

import java.util.ArrayList;

public class AdapterCardapio extends RecyclerView.Adapter<AdapterCardapio.MyViewHolder> implements Filterable {

    private ArrayList<Produto> cardapio;
    private ArrayList<Produto> filteredCardapio;
    private Context context;
    private CardapioItemClickListener customItemClickListener;

    public AdapterCardapio(Context context, ArrayList<Produto> userArrayList, CardapioItemClickListener customItemClickListener) {
        this.context = context;
        this.cardapio = userArrayList;
        this.filteredCardapio = userArrayList;
        this.customItemClickListener = customItemClickListener;
    }

    @Override
    public AdapterCardapio.MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardapio_adapter, viewGroup, false);
        final MyViewHolder myViewHolder = new MyViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // for click item listener
                //Toast.makeText(context,"click " + myViewHolder.getAdapterPosition(),Toast.LENGTH_SHORT);
                customItemClickListener.onItemClick(filteredCardapio.get(myViewHolder.getAdapterPosition()), myViewHolder.getAdapterPosition());
            }
        });
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(AdapterCardapio.MyViewHolder viewHolder, int position) {
        Log.i("[IFMG]", " mesa : " + filteredCardapio.get(position).getNome() + "");
        viewHolder.produto.setText(filteredCardapio.get(position).getNome() + "");
        viewHolder.preco.setText("R$ " + filteredCardapio.get(position).getPreco() + "");
        viewHolder.tempo.setText(filteredCardapio.get(position).getPreparo() + "");
        viewHolder.descricao.setText(filteredCardapio.get(position).getDescricao() + "");
        Bitmap bitmap = UtilImageTransmit.convertBytetoImage(filteredCardapio.get(position).getFoto());
        if (bitmap != null)
            viewHolder.foto.setImageBitmap(bitmap);
        
    }

    @Override
    public int getItemCount() {
        return filteredCardapio.size();
    }

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String searchString = charSequence.toString();

                if (searchString.isEmpty()) {

                    filteredCardapio = cardapio;

                } else {

                    ArrayList<Produto> tempFilteredList = new ArrayList<>();

                    for (Produto user : cardapio) {
                        // search for user name
                        if ((user.getNome() + "").toLowerCase().contains(searchString)) {
                            tempFilteredList.add(user);
                        }
                    }

                    filteredCardapio = tempFilteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredCardapio;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredCardapio = (ArrayList<Produto>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView produto;
        private TextView descricao;
        private TextView preco;
        private TextView tempo;
        private ImageView foto;

        public MyViewHolder(View view) {
            super(view);
            produto = (TextView) view.findViewById(R.id.tvNome);
            descricao = (TextView) view.findViewById(R.id.tvDescricao);
            preco = (TextView) view.findViewById(R.id.tvPreco);
            tempo = (TextView) view.findViewById(R.id.tvTempo);
            foto = (ImageView) view.findViewById(R.id.ivFoto);


        }
    }
}
