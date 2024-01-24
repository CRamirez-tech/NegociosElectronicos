package com.crm.negocios;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.crm.negocios.sql.controllers.ArticuloController;
import com.crm.negocios.sql.controllers.MarcaController;
import com.crm.negocios.sql.controllers.UnidadMedidaController;
import com.crm.negocios.sql.model.Articulo;
import com.crm.negocios.sql.model.Marca;
import com.crm.negocios.sql.model.UnidadMedida;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ArticuloEditActivity extends AppCompatActivity {
    private EditText etNombre, etPrecioUnitario, etEstado;
    private Spinner spinMedida, spinMarca;
    private ArticuloController articuloController;
    private Articulo articulo;
    private List<Marca> marcaList;
    private List<UnidadMedida> unidadList;
    private long marcaSeleccionadaCod;
    private long unidadSeleccionadaCod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_articulo_edit);
        // Recuperar datos que enviaron
        Bundle extras = getIntent().getExtras();
        // Si no hay datos (cosa rara) salimos
        if (extras == null) {
            finish();
            return;
        }

        articuloController = new ArticuloController(ArticuloEditActivity.this);
        marcaList = new MarcaController(ArticuloEditActivity.this).obtenerMarcas();
        unidadList = new UnidadMedidaController(ArticuloEditActivity.this).obtenerUnidadMedida();


        long codArticulo = extras.getLong("codArticulo");
        String nombreArticulo = extras.getString("nombreArticulo");
        int unidadMedidaArticulo = extras.getInt("unidadMedidaArticulo");
        double precioUnitarioArticulo = extras.getDouble("precioUnitarioArticulo");
        int marcaArticulo = extras.getInt("marcaArticulo");
        String estadoRegistroArticulo = extras.getString("estadoRegistroArticulo");

        articulo = new Articulo(codArticulo, nombreArticulo, unidadMedidaArticulo, precioUnitarioArticulo, marcaArticulo, estadoRegistroArticulo);

        // Ahora declaramos las vistas
        etNombre = findViewById(R.id.etNombre);
        spinMarca = findViewById(R.id.spinMarca);
        etPrecioUnitario = findViewById(R.id.etPrecioUnitario);
        spinMedida = findViewById(R.id.spinMedida);
        etEstado = findViewById(R.id.etEstado);

        Button btnCancelar = findViewById(R.id.btnCancelar);
        Button btnGuardar = findViewById(R.id.btnGuardar);

        etNombre.setText(articulo.getNombre());
        etPrecioUnitario.setText(String.valueOf(articulo.getPrecioUnitario()));
        etEstado.setText(articulo.getEstadoRegistro());

        btnCancelar.setOnClickListener(v -> volverCasa());

        btnGuardar.setOnClickListener(v -> {

            etNombre.setError(null);
            etPrecioUnitario.setError(null);
            etEstado.setError(null);

            String nuevoNombre = etNombre.getText().toString();
            String posibleNuevoPrecio = etPrecioUnitario.getText().toString();
            String nuevoEstado = etEstado.getText().toString();

            if (nuevoNombre.isEmpty()) {
                etNombre.setError("Escribe el nombre");
                etNombre.requestFocus();
                return;
            }
            if (posibleNuevoPrecio.isEmpty()) {
                etPrecioUnitario.setError("Escribe el precio");
                etPrecioUnitario.requestFocus();
                return;
            }
            if (nuevoEstado.isEmpty()) {
                etEstado.setError("Se modificado el estado");
                etEstado.requestFocus();
                return;
            }
            // Si no es entero, igualmente marcar error
            double nuevoPrecio;
            try {
                nuevoPrecio = Double.parseDouble(posibleNuevoPrecio);
            } catch (NumberFormatException e) {
                etPrecioUnitario.setError("Escribe un número");
                etPrecioUnitario.requestFocus();
                return;
            }

            Articulo mascotaConNuevosCambios = new Articulo(articulo.getCod(),nuevoNombre,1, nuevoPrecio,1,nuevoEstado);
            int filasModificadas = articuloController.guardarCambios(mascotaConNuevosCambios);
            if (filasModificadas != 1) {
                Toast.makeText(ArticuloEditActivity.this, "Error guardando cambios. Intente de nuevo.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(ArticuloEditActivity.this, "Se modifico el articulo correctamente", Toast.LENGTH_SHORT).show();
                volverCasa();
            }
        });
        Spinner spinMarca = findViewById(R.id.spinMarca);
        HashMap<Long, String> hashMapMarcas = generarHashMapMarca(marcaList);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                ArticuloEditActivity.this,
                android.R.layout.simple_spinner_item,
                new ArrayList<>(hashMapMarcas.values())
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinMarca.setAdapter(adapter);
        spinMarca.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Manejar la selección del Spinner aquí
                marcaSeleccionadaCod = marcaList.get(position).getCod();
                //String marcaSeleccionadaNombre = marcaList.get(position).getNombre();
                //String marcaSeleccionadaEstado = marcaList.get(position).getEstadoRegistro();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        Spinner spinMedida = findViewById(R.id.spinMedida);
        HashMap<Long, String> hashMapMedidas = generarHashMapUnidad(unidadList);

        ArrayAdapter<String> adapter_2 = new ArrayAdapter<>(
                ArticuloEditActivity.this,
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
    }
    void volverCasa(){
        Intent intent = new Intent(ArticuloEditActivity.this, MainActivity.class);
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