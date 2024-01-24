package com.crm.negocios.ui.fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.crm.negocios.ArticuloAddActivity;
import com.crm.negocios.ArticuloEditActivity;
import com.crm.negocios.R;
import com.crm.negocios.databinding.FragmentArticuloBinding;
import com.crm.negocios.sql.controllers.MarcaController;
import com.crm.negocios.sql.controllers.UnidadMedidaController;
import com.crm.negocios.sql.model.Articulo;
import com.crm.negocios.sql.controllers.ArticuloController;
import com.crm.negocios.sql.model.Marca;
import com.crm.negocios.sql.model.UnidadMedida;
import com.crm.negocios.ui.RecyclerTouchListener;
import com.crm.negocios.ui.adapters.ArticuloAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class ArticuloFragment extends Fragment {
    private List<Articulo> listaArticulos;
    private ArticuloAdapter articuloAdapter;
    private ArticuloController articuloController;
    private List<Marca> marcaList;
    private List<UnidadMedida> unidadList;
    private FragmentArticuloBinding binding;
    private long marcaSeleccionadaCod;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        articuloController = new ArticuloController(this.getContext());
        marcaList = new MarcaController(this.getContext()).obtenerMarcas();
        unidadList = new UnidadMedidaController(this.getContext()).obtenerUnidadMedida();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentArticuloBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        RecyclerView recyclerView = root.findViewById(R.id.recyclerViewArticulos);
        FloatingActionButton fabAgregar = root.findViewById(R.id.fabAgregar);

        listaArticulos = new ArrayList<>();
        articuloAdapter = new ArticuloAdapter(listaArticulos);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(articuloAdapter);
        refrescarLista();
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this.getContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override // Un toque sencillo
            public void onClick(View view, int position) {
                // Pasar a la actividad EditarMascotaActivity.java
                Articulo articulo = listaArticulos.get(position);
                if(articulo.getEstadoRegistro().equals("A")){
                    Intent intent = new Intent(getContext(), ArticuloEditActivity.class);
                    intent.putExtra("nombreArticulo",articulo.getNombre());
                    intent.putExtra("unidadMedidaArticulo",articulo.getUnidadMedida());
                    intent.putExtra("precioUnitarioArticulo",articulo.getPrecioUnitario());
                    intent.putExtra("marcaArticulo",articulo.getMarca());
                    intent.putExtra("estadoRegistroArticulo",articulo.getEstadoRegistro());
                    intent.putExtra("codArticulo",articulo.getCod());
                    startActivity(intent);
                }else{
                    Toast.makeText(getActivity(), "Primero necesita activar el item", Toast.LENGTH_SHORT).show();
                }
            }

            @Override // Un toque largo
            public void onLongClick(View view, int position) {
                final Articulo articulo = listaArticulos.get(position);
                AlertDialog dialog = new AlertDialog
                        .Builder(getContext())
                        .setPositiveButton("Sí, eliminar", (dialog1, which) -> {
                            articuloController.eliminarArticulo(articulo);
                            refrescarLista();
                        })
                        .setNeutralButton("Solo Desactivar/Activar", (dialog1, which) -> {
                            articuloController.desactivarArticulo(articulo);
                            refrescarLista();
                        })
                        .setNegativeButton("Cancelar", (dialog2, which) -> dialog2.dismiss())
                        .setTitle("Confirmar")
                        .setMessage("¿Eliminar al articulo " + articulo.getNombre() + "?")
                        .create();
                dialog.show();
            }
        }));

        fabAgregar.setOnClickListener(v -> {
            Intent intent = new Intent(this.getContext(), ArticuloAddActivity.class);
            startActivity(intent);
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void refrescarLista() {
        if (articuloAdapter == null) return;
        listaArticulos = articuloController.obtenerArticulos();
        articuloAdapter.setArticuloList(listaArticulos);

        articuloAdapter.notifyDataSetChanged();
    }
}