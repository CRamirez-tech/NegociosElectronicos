package com.crm.negocios.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.crm.negocios.R;
import com.crm.negocios.sql.model.Marca;

import java.util.List;

public class MarcaAdapter extends RecyclerView.Adapter<MarcaAdapter.MyViewHolder> {
    private List<Marca> marcasList;

    public void setMarcasList(List<Marca> marcasList) {
        this.marcasList = marcasList;
    }

    public MarcaAdapter(List<Marca> marcas) {
        this.marcasList = marcas;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View rootHolder = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_marcas, viewGroup, false);
        return new MyViewHolder(rootHolder);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        // Obtener la mascota de nuestra lista gracias al índice i
        Marca articulo = marcasList.get(i);

        // Obtener los datos de la lista
        String nombre = articulo.getNombre();
        String estado = articulo.getEstadoRegistro();

        // Y poner a los TextView los datos con setText
        myViewHolder.nombre.setText(nombre);
        myViewHolder.estado.setText(estado);
    }

    @Override
    public int getItemCount() {
        return marcasList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView nombre, estado;

        MyViewHolder(View itemView) {
            super(itemView);
            this.nombre = itemView.findViewById(R.id.tvNombre);
            this.estado = itemView.findViewById(R.id.tvEstado);
        }
    }
}
