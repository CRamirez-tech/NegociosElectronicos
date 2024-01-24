package com.crm.negocios.ui.fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.crm.negocios.sql.model.Articulo;
import com.crm.negocios.sql.controllers.ArticuloController;
import com.crm.negocios.ui.RecyclerTouchListener;
import com.crm.negocios.ui.adapters.ArticuloAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class ArticuloFragment extends Fragment {
    private List<Articulo> listaArticulos;
    private ArticuloAdapter articuloAdapter;
    private ArticuloController articuloController;
    private FragmentArticuloBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        articuloController = new ArticuloController(this.getContext());
        Articulo test = new Articulo("Fanta",1,2.23,1);
        long id = articuloController.nuevoArticulo(test);
        if (id == -1) {
            // De alguna manera ocurrió un error
            Toast.makeText(this.getActivity(), "Error al guardar. Intenta de nuevo", Toast.LENGTH_SHORT).show();
        } else {
            // Terminar
            Toast.makeText(this.getActivity(), "Si guardo", Toast.LENGTH_SHORT).show();
        }

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
                Intent intent = new Intent(getContext(), ArticuloEditActivity.class);
                intent.putExtra("nombreArticulo",articulo.getNombre());
                intent.putExtra("unidadMedidaArticulo",articulo.getUnidadMedida());
                intent.putExtra("precioUnitarioArticulo",articulo.getPrecioUnitario());
                intent.putExtra("marcaArticulo",articulo.getMarca());
                intent.putExtra("estadoRegistroArticulo",articulo.getEstadoRegistro());
                intent.putExtra("codArticulo",articulo.getCod());
                startActivity(intent);
            }

            @Override // Un toque largo
            public void onLongClick(View view, int position) {
                final Articulo articuloParaEliminar = listaArticulos.get(position);
                AlertDialog dialog = new AlertDialog
                        .Builder(getContext())
                        .setPositiveButton("Sí, eliminar", (dialog1, which) -> {
                            articuloController.eliminarArticulo(articuloParaEliminar);
                            refrescarLista();
                        })
                        .setNegativeButton("Cancelar", (dialog2, which) -> dialog2.dismiss())
                        .setTitle("Confirmar")
                        .setMessage("¿Eliminar al articulo " + articuloParaEliminar.getNombre() + "?")
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