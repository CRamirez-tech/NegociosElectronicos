package com.crm.negocios.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.crm.negocios.R;
import com.crm.negocios.sql.model.Articulo;

import java.text.DecimalFormat;
import java.util.List;

public class ArticuloAdapter extends RecyclerView.Adapter<ArticuloAdapter.MyViewHolder> {

    private List<Articulo> articuloList;

    public void setArticuloList(List<Articulo> articuloList) {
        this.articuloList = articuloList;
    }

    public ArticuloAdapter(List<Articulo> articulos) {
        this.articuloList = articulos;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View rootHolder = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_articulos, viewGroup, false);
        return new MyViewHolder(rootHolder);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        // Obtener la mascota de nuestra lista gracias al índice i
        Articulo articulo = articuloList.get(i);

        // Obtener los datos de la lista
        String nombre = articulo.getNombre();
        int codUnidadMedida = articulo.getUnidadMedida();
        double precioUnitario = articulo.getPrecioUnitario();
        int codMarca = articulo.getMarca();

        // Y poner a los TextView los datos con setText
        myViewHolder.nombre.setText(nombre);
        myViewHolder.unidadMedida.setText(String.valueOf(codUnidadMedida));
        myViewHolder.precioUnitario.setText(new DecimalFormat("0.00").format(precioUnitario));
        myViewHolder.marca.setText(String.valueOf(codMarca));
    }

    @Override
    public int getItemCount() {
        return articuloList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView nombre, unidadMedida, precioUnitario, marca;

        MyViewHolder(View itemView) {
            super(itemView);
            this.nombre = itemView.findViewById(R.id.tvNombre);
            this.unidadMedida = itemView.findViewById(R.id.tvUnidadMedida);
            this.precioUnitario = itemView.findViewById(R.id.tvPrecioUnitario);
            this.marca = itemView.findViewById(R.id.tvMarca);
        }
    }
}
