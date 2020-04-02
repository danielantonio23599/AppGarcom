package com.daniel.appgarcom.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.daniel.appgarcom.R;
import com.daniel.appgarcom.modelo.beans.Produto;
import com.daniel.appgarcom.util.UtilImageTransmit;

import java.util.ArrayList;

public class AdapterProduto extends RecyclerView.Adapter<AdapterProduto.MyViewHolder> implements Filterable {

    private ArrayList<Produto> produtos;
    private ArrayList<Produto> filteredProdutos;
    private Context context;
    private CustomItemClickListener customItemClickListener;

    public AdapterProduto(Context context, ArrayList<Produto> userArrayList, CustomItemClickListener customItemClickListener) {
        this.context = context;
        this.produtos = userArrayList;
        this.filteredProdutos = userArrayList;
        this.customItemClickListener = customItemClickListener;
    }

    @Override
    public AdapterProduto.MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.produto_adapter, viewGroup, false);
        final MyViewHolder myViewHolder = new MyViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // for click item listener
                //Toast.makeText(context,"click " + myViewHolder.getAdapterPosition(),Toast.LENGTH_SHORT);
               customItemClickListener.onItemClick(filteredProdutos.get(myViewHolder.getAdapterPosition()), myViewHolder.getAdapterPosition());
            }
        });
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(AdapterProduto.MyViewHolder viewHolder, int position) {
        Bitmap bitmap = UtilImageTransmit.convertBytetoImage(filteredProdutos.get(position).getFoto());
        if (bitmap != null)
            viewHolder.foto.setImageBitmap(bitmap);
        viewHolder.nome.setText(filteredProdutos.get(position).getNome());
        viewHolder.descricao.setText(filteredProdutos.get(position).getDescricao());
        viewHolder.preco.setText("R$ " + filteredProdutos.get(position).getPreco());
        viewHolder.tempo.setText(filteredProdutos.get(position).getPreparo());
    }

    @Override
    public int getItemCount() {
        return filteredProdutos.size();
    }

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String searchString = charSequence.toString();

                if (searchString.isEmpty()) {

                    filteredProdutos = produtos;

                } else {

                    ArrayList<Produto> tempFilteredList = new ArrayList<>();

                    for (Produto user : produtos) {
                        // search for user name
                        if (user.getNome().toLowerCase().contains(searchString)) {

                            tempFilteredList.add(user);
                        } else if ((user.getCodigo() + "").toLowerCase().contains(searchString)) {
                            tempFilteredList.add(user);
                        }
                    }

                    filteredProdutos = tempFilteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredProdutos;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredProdutos = (ArrayList<Produto>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView foto;
        private TextView nome;
        private TextView descricao;
        private TextView preco;
        private TextView tempo;

        public MyViewHolder(View view) {
            super(view);
            foto = (ImageView) view.findViewById(R.id.ivFoto);
            nome = (TextView) view.findViewById(R.id.tvNome);
            descricao = (TextView) view.findViewById(R.id.tvDescricao);
            preco = (TextView) view.findViewById(R.id.tvPreco);
            tempo = (TextView) view.findViewById(R.id.tvTempo);

        }
    }
}
