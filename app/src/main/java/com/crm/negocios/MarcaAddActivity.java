package com.crm.negocios;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.crm.negocios.sql.controllers.MarcaController;
import com.crm.negocios.sql.model.Marca;

public class MarcaAddActivity extends AppCompatActivity {
    private EditText etNombre;
    private MarcaController marcaController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marca_add);

        etNombre = findViewById(R.id.etNombre);
        Button btnAgregar = findViewById(R.id.btnAgregar);
        Button btnCancelar = findViewById(R.id.btnCancelar);

        marcaController = new MarcaController(MarcaAddActivity.this);


        btnAgregar.setOnClickListener(v -> {

            etNombre.setError(null);

            String nombre = etNombre.getText().toString();
            if ("".equals(nombre)) {
                etNombre.setError("Escribe el nombre de la mascota");
                etNombre.requestFocus();
                return;
            }

            // Ya pasó la validación
            Marca nuevaMarca = new Marca(nombre);
            long id = marcaController.nuevaMarca(nuevaMarca);
            if (id == -1) {
                // De alguna manera ocurrió un error
                Toast.makeText(MarcaAddActivity.this, "Error al guardar. Intenta de nuevo", Toast.LENGTH_SHORT).show();
            } else {
                // Terminar
                finish();
            }
        });

        // El de cancelar simplemente cierra la actividad
        btnCancelar.setOnClickListener(v -> finish());
    }
}