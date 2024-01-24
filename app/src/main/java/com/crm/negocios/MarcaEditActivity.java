package com.crm.negocios;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.crm.negocios.sql.controllers.MarcaController;
import com.crm.negocios.sql.model.Marca;

public class MarcaEditActivity extends AppCompatActivity {
    private EditText etNombre, etEstado;
    private MarcaController marcaController;
    private Marca marca;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marca_edit);

        Bundle extras = getIntent().getExtras();

        if (extras == null) {
            finish();
            return;
        }

        marcaController = new MarcaController(MarcaEditActivity.this);

        long cod = extras.getLong("cod");
        String nombre = extras.getString("nombre");
        String estadoRegistro = extras.getString("estadoRegistro");

        marca = new Marca(cod, nombre, estadoRegistro);

        // Ahora declaramos las vistas
        etNombre = findViewById(R.id.etNombre);
        etEstado = findViewById(R.id.etEstado);

        Button btnCancelar = findViewById(R.id.btnCancelar);
        Button btnGuardar = findViewById(R.id.btnGuardar);

        etNombre.setText(marca.getNombre());
        etEstado.setText(marca.getEstadoRegistro());

        btnCancelar.setOnClickListener(v -> volverCasa());

        btnGuardar.setOnClickListener(v -> {

            etNombre.setError(null);
            etEstado.setError(null);

            String nuevoNombre = etNombre.getText().toString();
            String nuevoEstado = etEstado.getText().toString();

            if (nuevoNombre.isEmpty()) {
                etNombre.setError("Escribe el nombre");
                etNombre.requestFocus();
                return;
            }
            if (nuevoEstado.isEmpty()) {
                etEstado.setError("Se modificado el estado");
                etEstado.requestFocus();
                return;
            }

            Marca modificado = new Marca(marca.getCod(),nuevoNombre,nuevoEstado);
            int filasModificadas = marcaController.guardarCambios(modificado);
            if (filasModificadas != 1) {
                Toast.makeText(MarcaEditActivity.this, "Error guardando cambios. Intente de nuevo.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MarcaEditActivity.this, "Se modifico la marca correctamente", Toast.LENGTH_SHORT).show();
                volverCasa();
            }
        });
    }
    void volverCasa(){
        Intent intent = new Intent(MarcaEditActivity.this, MainActivity.class);
        startActivity(intent);
    }
}