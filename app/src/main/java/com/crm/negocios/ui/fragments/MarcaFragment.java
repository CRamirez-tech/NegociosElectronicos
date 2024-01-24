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

import com.crm.negocios.MarcaAddActivity;
import com.crm.negocios.MarcaEditActivity;
import com.crm.negocios.R;
import com.crm.negocios.databinding.FragmentMarcaBinding;
import com.crm.negocios.sql.controllers.MarcaController;
import com.crm.negocios.sql.model.Marca;
import com.crm.negocios.ui.RecyclerTouchListener;
import com.crm.negocios.ui.adapters.MarcaAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MarcaFragment extends Fragment {

    private List<Marca> listaMarcas;
    private MarcaAdapter articuloAdapter;
    private MarcaController marcaController;
    private FragmentMarcaBinding binding;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        marcaController = new MarcaController(this.getContext());
        /*
        Marca test = new Marca("Ind. San Miguel");
        long id = marcaController.nuevaMarca(test);
        if (id == -1) {
            // De alguna manera ocurrió un error
            Toast.makeText(this.getActivity(), "Error al guardar. Intenta de nuevo", Toast.LENGTH_SHORT).show();
        } else {
            // Terminar
            Toast.makeText(this.getActivity(), "Si guardo", Toast.LENGTH_SHORT).show();
        }
         */
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentMarcaBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        RecyclerView recyclerView = root.findViewById(R.id.recyclerViewMarcas);
        FloatingActionButton fabAgregar = root.findViewById(R.id.fabAgregar);

        listaMarcas = new ArrayList<>();
        articuloAdapter = new MarcaAdapter(listaMarcas);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(articuloAdapter);
        refrescarLista();
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this.getContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override // Un toque sencillo
            public void onClick(View view, int position) {
                // Pasar a la actividad EditarMascotaActivity.java
                Marca marca = listaMarcas.get(position);
                if(marca.getEstadoRegistro().equals("A")){
                    Intent intent = new Intent(getContext(), MarcaEditActivity.class);
                    intent.putExtra("cod",marca.getCod());
                    intent.putExtra("nombre",marca.getNombre());
                    intent.putExtra("estadoRegistro",marca.getEstadoRegistro());
                    startActivity(intent);
                }else{
                    Toast.makeText(getActivity(), "Primero necesita activar el item", Toast.LENGTH_SHORT).show();
                }
            }

            @Override // Un toque largo
            public void onLongClick(View view, int position) {
                final Marca marca = listaMarcas.get(position);
                AlertDialog dialog = new AlertDialog
                        .Builder(getContext())
                        .setPositiveButton("Sí, eliminar", (dialog1, which) -> {
                            marcaController.eliminarMarca(marca);
                            refrescarLista();
                        })
                        .setNeutralButton("Solo Desactivar/Activar", (dialog1, which) -> {
                            marcaController.desactivarMarca(marca);
                            refrescarLista();
                        })
                        .setNegativeButton("Cancelar", (dialog2, which) -> dialog2.dismiss())
                        .setTitle("Confirmar")
                        .setMessage("¿Eliminar la marca" + marca.getNombre() + "?")
                        .create();
                dialog.show();
            }
        }));

        fabAgregar.setOnClickListener(v -> {
            Intent intent = new Intent(this.getContext(), MarcaAddActivity.class);
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
        listaMarcas = marcaController.obtenerMarcas();
        articuloAdapter.setMarcasList(listaMarcas);

        articuloAdapter.notifyDataSetChanged();
    }
}