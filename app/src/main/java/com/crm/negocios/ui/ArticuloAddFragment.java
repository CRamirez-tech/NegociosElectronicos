package com.crm.negocios.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.crm.negocios.ArticuloEditActivity;
import com.crm.negocios.MainActivity;
import com.crm.negocios.R;
import com.crm.negocios.UnidadMedidaAddActivity;
import com.crm.negocios.databinding.FragmentArticuloAddBinding;
import com.crm.negocios.sql.controllers.ArticuloController;
import com.crm.negocios.sql.controllers.MarcaController;
import com.crm.negocios.sql.controllers.UnidadMedidaController;
import com.crm.negocios.sql.model.Articulo;
import com.crm.negocios.sql.model.Marca;
import com.crm.negocios.sql.model.UnidadMedida;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ArticuloAddFragment extends Fragment {
    private Button btnAgregar, btnCancelar;
    private EditText etNombre, etPrecioUnitario;
    private ArticuloController articuloController;
    private FragmentArticuloAddBinding binding;
    private List<Marca> marcaList;
    private List<UnidadMedida> unidadList;
    private long marcaSeleccionadaCod;
    private long unidadSeleccionadaCod;

    public ArticuloAddFragment() {
    }

    public static ArticuloAddFragment newInstance(String param1, String param2) {
        return new ArticuloAddFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        marcaList = new MarcaController(getContext()).obtenerMarcas();
        unidadList = new UnidadMedidaController(getContext()).obtenerUnidadMedida();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentArticuloAddBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Instanciar vistas
        etNombre = root.findViewById(R.id.etNombre);
        etPrecioUnitario = root.findViewById(R.id.etPrecioUnitario);
        btnAgregar = root.findViewById(R.id.btnAgregar);
        btnCancelar = root.findViewById(R.id.btnCancelar);
        // Crear el controlador
        articuloController = new ArticuloController(this.getContext());

        // Agregar listener del botón de guardar
        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Resetear errores a ambos
                etNombre.setError(null);
                etPrecioUnitario.setError(null);
                String nombre = etNombre.getText().toString();
                String precioUnitarioString = etPrecioUnitario.getText().toString();
                if ("".equals(nombre)) {
                    etNombre.setError("Escriba el nombre del articulo");
                    etNombre.requestFocus();
                    return;
                }
                if ("".equals(precioUnitarioString)) {
                    etPrecioUnitario.setError("Escribe el precio unitario");
                    etPrecioUnitario.requestFocus();
                    return;
                }

                double precioUnitario;
                try {
                    precioUnitario = Double.parseDouble(precioUnitarioString);
                } catch (NumberFormatException e) {
                    etPrecioUnitario.setError("Escribe un número");
                    etPrecioUnitario.requestFocus();
                    return;
                }
                // Ya pasó la validación
                Articulo nuevoArticulo = new Articulo(nombre,(int)unidadSeleccionadaCod ,precioUnitario, (int) marcaSeleccionadaCod);
                long id = articuloController.nuevoArticulo(nuevoArticulo);
                if (id == -1) {
                    // De alguna manera ocurrió un error
                    Toast.makeText(getActivity(), "Error al guardar el articulo. Intenta de nuevo", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Se guardo el articulo correctamente", Toast.LENGTH_SHORT).show();
                    volverCasa();
                }
            }
        });
        btnCancelar.setOnClickListener(v -> volverCasa());

        Spinner spinMarca = root.findViewById(R.id.spinMarca);
        HashMap<Long, String> hashMapMarcas = generarHashMapMarca(marcaList);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                new ArrayList<>(hashMapMarcas.values())
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinMarca.setAdapter(adapter);
        spinMarca.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                marcaSeleccionadaCod = marcaList.get(position).getCod();
                //String marcaSeleccionadaNombre = marcaList.get(position).getNombre();
                //String marcaSeleccionadaEstado = marcaList.get(position).getEstadoRegistro();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        Spinner spinMedida = root.findViewById(R.id.spinMedida);
        HashMap<Long, String> hashMapMedidas = generarHashMapUnidad(unidadList);

        ArrayAdapter<String> adapter_2 = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                new ArrayList<>(hashMapMedidas.values())
        );
        adapter_2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinMedida.setAdapter(adapter_2);
        spinMedida.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Manejar la selección del Spinner aquí
                unidadSeleccionadaCod = unidadList.get(position).getCod();
                //String marcaSeleccionadaNombre = marcaList.get(position).getNombre();
                //String marcaSeleccionadaEstado = marcaList.get(position).getEstadoRegistro();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return root;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    void volverCasa(){
        Intent intent = new Intent(this.getContext(), MainActivity.class);
        startActivity(intent);
    }
    private HashMap<Long, String> generarHashMapMarca(List<Marca> listaMarcas) {
        HashMap<Long, String> hashMapMarcas = new HashMap<>();
        for (Marca marca : listaMarcas) {
            hashMapMarcas.put(marca.getCod(), marca.getNombre());
        }
        return hashMapMarcas;
    }
    private HashMap<Long, String> generarHashMapUnidad(List<UnidadMedida> listaUnidades) {
        HashMap<Long, String> hashMapMarcas = new HashMap<>();
        for (UnidadMedida unidad : listaUnidades) {
            hashMapMarcas.put(unidad.getCod(), unidad.getNombre());
        }
        return hashMapMarcas;
    }
}