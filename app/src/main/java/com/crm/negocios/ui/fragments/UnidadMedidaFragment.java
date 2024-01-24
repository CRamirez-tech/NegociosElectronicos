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

import com.crm.negocios.R;
import com.crm.negocios.UnidadMedidaAddActivity;
import com.crm.negocios.UnidadMedidaEditActivity;
import com.crm.negocios.databinding.FragmentMetricUnitBinding;
import com.crm.negocios.sql.controllers.UnidadMedidaController;
import com.crm.negocios.sql.model.UnidadMedida;
import com.crm.negocios.ui.RecyclerTouchListener;
import com.crm.negocios.ui.adapters.UnidadMedidaAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class UnidadMedidaFragment extends Fragment {

    private List<UnidadMedida> listaMedidas;
    private UnidadMedidaAdapter unidadMedidaAdapter;
    private UnidadMedidaController marcaController;
    private FragmentMetricUnitBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        marcaController = new UnidadMedidaController(this.getContext());
        /*
        UnidadMedida test = new UnidadMedida("L.");
        long id = marcaController.nuevaUnidad(test);
        if (id == -1) {
            Toast.makeText(this.getActivity(), "Error al guardar. Intenta de nuevo", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this.getActivity(), "Si guardo", Toast.LENGTH_SHORT).show();
        }
        */
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMetricUnitBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        RecyclerView recyclerView = root.findViewById(R.id.recyclerViewMetricUnits);
        FloatingActionButton fabAgregar = root.findViewById(R.id.fabAgregar);

        listaMedidas = new ArrayList<>();
        unidadMedidaAdapter = new UnidadMedidaAdapter(listaMedidas);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(unidadMedidaAdapter);
        refrescarLista();
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this.getContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override // Un toque sencillo
            public void onClick(View view, int position) {
                // Pasar a la actividad EditarMascotaActivity.java
                UnidadMedida unidadMedida = listaMedidas.get(position);
                if(unidadMedida.getEstadoRegistro().equals("A")){
                    Intent intent = new Intent(getContext(), UnidadMedidaEditActivity.class);
                    intent.putExtra("cod",unidadMedida.getCod());
                    intent.putExtra("nombre",unidadMedida.getNombre());
                    intent.putExtra("estadoRegistro",unidadMedida.getEstadoRegistro());
                    startActivity(intent);
                }else{
                    Toast.makeText(getActivity(), "Primero necesita activar el item", Toast.LENGTH_SHORT).show();
                }
            }

            @Override // Un toque largo
            public void onLongClick(View view, int position) {
                final UnidadMedida unidadMedida = listaMedidas.get(position);
                AlertDialog dialog = new AlertDialog
                        .Builder(getContext())
                        .setPositiveButton("Si, Eliminar", (dialog1, which) -> {
                            marcaController.eliminarUnidad(unidadMedida);
                            refrescarLista();
                        })
                        .setNeutralButton("Solo Desactivar/Activar", (dialog1, which) -> {
                            marcaController.desactivarUnidad(unidadMedida);
                            refrescarLista();
                        })
                        .setNegativeButton("Cancelar", (dialog2, which) -> dialog2.dismiss())
                        .setTitle("Confirmar")
                        .setMessage("¿Eliminar la unidad de medida " + unidadMedida.getNombre() + "?")
                        .create();
                dialog.show();
            }
        }));

        fabAgregar.setOnClickListener(v -> {
            Intent intent = new Intent(this.getContext(), UnidadMedidaAddActivity.class);
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
        if (unidadMedidaAdapter == null) return;
        listaMedidas = marcaController.obtenerUnidadMedida();
        unidadMedidaAdapter.setUnidadMedidaList(listaMedidas);

        unidadMedidaAdapter.notifyDataSetChanged();
    }
}