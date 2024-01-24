package com.crm.negocios;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.crm.negocios.databinding.FragmentArticuloAddBinding;
import com.crm.negocios.sql.controllers.ArticuloController;
import com.crm.negocios.sql.model.Articulo;

public class ArticuloEditActivity extends AppCompatActivity {
    private EditText etNombre, etPrecioUnitario, etEstado;
    private Spinner spinMedida, spinMarca;
    private ArticuloController articuloController;
    private Articulo articulo;
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
        // Instanciar el controlador de las mascotas
        articuloController = new ArticuloController(ArticuloEditActivity.this);
        // Rearmar la mascota
        // Nota: igualmente solamente podríamos mandar el id y recuperar la mascota de la BD
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
                volverCasa();
            }
        });
    }
    void volverCasa(){
        Intent intent = new Intent(ArticuloEditActivity.this, MainActivity.class);
        startActivity(intent);
    }
}