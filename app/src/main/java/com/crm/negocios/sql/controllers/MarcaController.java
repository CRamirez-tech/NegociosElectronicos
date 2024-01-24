package com.crm.negocios.sql.controllers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.crm.negocios.sql.BDHelper;
import com.crm.negocios.sql.model.Marca;

import java.util.ArrayList;

public class MarcaController {
    private final BDHelper ayudanteBaseDeDatos;
    private final String NOMBRE_TABLA = "Marca";

    public MarcaController(Context context) {
        this.ayudanteBaseDeDatos = new BDHelper(context,null,1);
    }

    public int eliminarMarcaFisico(Marca marca) {
        SQLiteDatabase baseDeDatos = ayudanteBaseDeDatos.getWritableDatabase();
        String[] argumentos = {String.valueOf(marca.getCod())};
        return baseDeDatos.delete(NOMBRE_TABLA, "MarCod = ?", argumentos);
    }
    public int eliminarMarca(Marca marca) {
        SQLiteDatabase baseDeDatos = ayudanteBaseDeDatos.getWritableDatabase();
        ContentValues valoresParaActualizar = new ContentValues();

        valoresParaActualizar.put("MarEst","*");

        String campoParaActualizar = "MarCod = ?";
        String[] argumentosParaActualizar = {String.valueOf(marca.getCod())};
        return baseDeDatos.update(NOMBRE_TABLA, valoresParaActualizar, campoParaActualizar, argumentosParaActualizar);
    }
    public int desactivarMarca(Marca marca) {
        String estado = "I";
        if (marca.getEstadoRegistro().equals(estado)){
            estado = "A";
        }
        SQLiteDatabase baseDeDatos = ayudanteBaseDeDatos.getWritableDatabase();
        ContentValues valoresParaActualizar = new ContentValues();

        valoresParaActualizar.put("MarEst",estado);

        String campoParaActualizar = "MarCod = ?";
        String[] argumentosParaActualizar = {String.valueOf(marca.getCod())};
        return baseDeDatos.update(NOMBRE_TABLA, valoresParaActualizar, campoParaActualizar, argumentosParaActualizar);
    }
    public long nuevaMarca(Marca marca) {
        // writable porque vamos a insertar
        SQLiteDatabase baseDeDatos = ayudanteBaseDeDatos.getWritableDatabase();
        ContentValues valoresParaInsertar = new ContentValues();

        valoresParaInsertar.put("MarNom",marca.getNombre());
        valoresParaInsertar.put("MarEst","A");

        return baseDeDatos.insert(NOMBRE_TABLA, null, valoresParaInsertar);
    }

    public int guardarCambios(Marca marca) {
        SQLiteDatabase baseDeDatos = ayudanteBaseDeDatos.getWritableDatabase();
        ContentValues valoresParaActualizar = new ContentValues();

        valoresParaActualizar.put("MarNom",marca.getNombre());
        valoresParaActualizar.put("MarEst",marca.getEstadoRegistro());

        String campoParaActualizar = "MarCod = ?";
        String[] argumentosParaActualizar = {String.valueOf(marca.getCod())};
        return baseDeDatos.update(NOMBRE_TABLA, valoresParaActualizar, campoParaActualizar, argumentosParaActualizar);
    }

    public ArrayList<Marca> obtenerMarcas() {
        ArrayList<Marca> marcas = new ArrayList<>();

        // readable porque no vamos a modificar, solamente leer
        SQLiteDatabase baseDeDatos = ayudanteBaseDeDatos.getReadableDatabase();

        // SELECT nombre, unidadMedida, etc
        String[] columnasAConsultar = {"MarCod","MarNom", "MarEst"};
        Cursor cursor = baseDeDatos.query(
                NOMBRE_TABLA,
                columnasAConsultar,
                "NOT MarEst = '*'",
                null,
                null,
                null,
                null
        );

        if (cursor == null) {
            return marcas;
        }

        if (!cursor.moveToFirst()){
            return marcas;
        }

        do {
            long CodFromDB = cursor.getLong(0);
            String NombreFromDB = cursor.getString(1);
            String EstadoRegistroFromDB = cursor.getString(2);

            Marca marca = new Marca(CodFromDB, NombreFromDB, EstadoRegistroFromDB);
            marcas.add(marca);
        } while (cursor.moveToNext());
        cursor.close();
        return marcas;
    }
}
