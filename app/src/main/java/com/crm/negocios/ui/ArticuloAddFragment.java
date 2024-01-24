package com.crm.negocios.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.crm.negocios.MainActivity;
import com.crm.negocios.R;
import com.crm.negocios.UnidadMedidaAddActivity;
import com.crm.negocios.databinding.FragmentArticuloAddBinding;
import com.crm.negocios.sql.controllers.ArticuloController;
import com.crm.negocios.sql.model.Articulo;

public class ArticuloAddFragment extends Fragment {
    private Button btnAgregar, btnCancelar;
    private EditText etNombre, etPrecioUnitario;
    private ArticuloController articuloController;
    private FragmentArticuloAddBinding binding;
    public ArticuloAddFragment() {
    }

    public static ArticuloAddFragment newInstance(String param1, String param2) {
        return new ArticuloAddFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                Articulo nuevoArticulo = new Articulo(nombre,1,precioUnitario,1);
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

        // El de cancelar simplemente cierra la actividad
        btnCancelar.setOnClickListener(v -> volverCasa());
        // Inflate the layout for this fragment
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
}