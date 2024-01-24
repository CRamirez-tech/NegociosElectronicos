package com.crm.negocios;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.crm.negocios.sql.controllers.UnidadMedidaController;
import com.crm.negocios.sql.model.Marca;
import com.crm.negocios.sql.model.UnidadMedida;

public class UnidadMedidaAddActivity extends AppCompatActivity {

    private EditText etNombre ;
    private UnidadMedidaController unidadMedidaController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unidad_medida_add);

        etNombre = findViewById(R.id.etNombre);
        Button btnAgregar = findViewById(R.id.btnAgregar);
        Button btnCancelar = findViewById(R.id.btnCancelar);

        unidadMedidaController = new UnidadMedidaController(UnidadMedidaAddActivity.this);

        btnAgregar.setOnClickListener(v -> {

            etNombre.setError(null);

            String nombre = etNombre.getText().toString();
            if ("".equals(nombre)) {
                etNombre.setError("Escribe el nombre de la mascota");
                etNombre.requestFocus();
                return;
            }

            // Ya pasó la validación
            UnidadMedida nuevaUnidad = new UnidadMedida(nombre);
            long id = unidadMedidaController.nuevaUnidad(nuevaUnidad);
            if (id == -1) {
                // De alguna manera ocurrió un error
                Toast.makeText(UnidadMedidaAddActivity.this, "Error al guardar la unidad. Intenta de nuevo", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(UnidadMedidaAddActivity.this, "Se modifico la unidad correctamente", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        // El de cancelar simplemente cierra la actividad
        btnCancelar.setOnClickListener(v -> finish());
    }
}