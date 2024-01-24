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

import com.crm.negocios.sql.controllers.UnidadMedidaController;
import com.crm.negocios.sql.model.Marca;
import com.crm.negocios.sql.model.UnidadMedida;

import java.util.ArrayList;
import java.util.HashMap;

public class UnidadMedidaEditActivity extends AppCompatActivity {
    private EditText etNombre, etEstado;
    private UnidadMedidaController marcaController;
    private UnidadMedida unidadMedida;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unidad_medida_edit);

        Bundle extras = getIntent().getExtras();

        if (extras == null) {
            finish();
            return;
        }

        marcaController = new UnidadMedidaController(UnidadMedidaEditActivity.this);

        long cod = extras.getLong("cod");
        String nombre = extras.getString("nombre");
        String estadoRegistro = extras.getString("estadoRegistro");

        unidadMedida = new UnidadMedida(cod, nombre, estadoRegistro);

        // Ahora declaramos las vistas
        etNombre = findViewById(R.id.etNombre);
        etEstado = findViewById(R.id.etEstado);

        Button btnCancelar = findViewById(R.id.btnCancelar);
        Button btnGuardar = findViewById(R.id.btnGuardar);

        etNombre.setText(unidadMedida.getNombre());
        etEstado.setText(unidadMedida.getEstadoRegistro());

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

            UnidadMedida modificado = new UnidadMedida(unidadMedida.getCod(),nuevoNombre,nuevoEstado);
            int filasModificadas = marcaController.guardarCambios(modificado);
            if (filasModificadas != 1) {
                Toast.makeText(UnidadMedidaEditActivity.this, "Error guardando cambios. Intente de nuevo.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(UnidadMedidaEditActivity.this, "Se modifico la unidad correctamente", Toast.LENGTH_SHORT).show();
                volverCasa();
            }
        });


    }
    void volverCasa(){
        Intent intent = new Intent(UnidadMedidaEditActivity.this, MainActivity.class);
        startActivity(intent);
    }
}