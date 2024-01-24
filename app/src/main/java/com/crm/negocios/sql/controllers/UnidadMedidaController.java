package com.crm.negocios.sql.controllers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.crm.negocios.sql.BDHelper;
import com.crm.negocios.sql.model.UnidadMedida;

import java.util.ArrayList;

public class UnidadMedidaController {
    private final BDHelper ayudanteBaseDeDatos;
    private final String NOMBRE_TABLA = "UnidadMedida";

    public UnidadMedidaController(Context context) {
        this.ayudanteBaseDeDatos = new BDHelper(context,null,1);
    }
    public int eliminarUnidadFisico(UnidadMedida unidad) {
        SQLiteDatabase baseDeDatos = ayudanteBaseDeDatos.getWritableDatabase();
        String[] argumentos = {String.valueOf(unidad.getCod())};
        return baseDeDatos.delete(NOMBRE_TABLA, "UmeCod = ?", argumentos);
    }
    public int eliminarUnidad(UnidadMedida unidad) {
        SQLiteDatabase baseDeDatos = ayudanteBaseDeDatos.getWritableDatabase();
        ContentValues valoresParaActualizar = new ContentValues();

        valoresParaActualizar.put("UmeEst","*");

        String campoParaActualizar = "UmeCod = ?";
        String[] argumentosParaActualizar = {String.valueOf(unidad.getCod())};
        return baseDeDatos.update(NOMBRE_TABLA, valoresParaActualizar, campoParaActualizar, argumentosParaActualizar);
    }
    public int desactivarUnidad(UnidadMedida unidad) {
        String estado = "I";
        if (unidad.getEstadoRegistro().equals(estado)){
            estado = "A";
        }
        SQLiteDatabase baseDeDatos = ayudanteBaseDeDatos.getWritableDatabase();
        ContentValues valoresParaActualizar = new ContentValues();

        valoresParaActualizar.put("UmeEst",estado);

        String campoParaActualizar = "UmeCod = ?";
        String[] argumentosParaActualizar = {String.valueOf(unidad.getCod())};
        return baseDeDatos.update(NOMBRE_TABLA, valoresParaActualizar, campoParaActualizar, argumentosParaActualizar);
    }
    public long nuevaUnidad(UnidadMedida unidad) {

        SQLiteDatabase baseDeDatos = ayudanteBaseDeDatos.getWritableDatabase();
        ContentValues valoresParaInsertar = new ContentValues();

        valoresParaInsertar.put("UmeNom",unidad.getNombre());
        valoresParaInsertar.put("UmeEst","A");

        return baseDeDatos.insert(NOMBRE_TABLA, null, valoresParaInsertar);
    }

    public int guardarCambios(UnidadMedida unidad) {
        SQLiteDatabase baseDeDatos = ayudanteBaseDeDatos.getWritableDatabase();
        ContentValues valoresParaActualizar = new ContentValues();

        valoresParaActualizar.put("UmeNom",unidad.getNombre());
        valoresParaActualizar.put("UmeEst",unidad.getEstadoRegistro());

        String campoParaActualizar = "UmeCod = ?";
        String[] argumentosParaActualizar = {String.valueOf(unidad.getCod())};
        return baseDeDatos.update(NOMBRE_TABLA, valoresParaActualizar, campoParaActualizar, argumentosParaActualizar);
    }

    public ArrayList<UnidadMedida> obtenerUnidadMedida() {
        ArrayList<UnidadMedida> unidades = new ArrayList<>();

        // readable porque no vamos a modificar, solamente leer
        SQLiteDatabase baseDeDatos = ayudanteBaseDeDatos.getReadableDatabase();

        // SELECT nombre, unidadMedida, etc
        String[] columnasAConsultar = {"UmeCod","UmeNom", "UmeEst"};
        Cursor cursor = baseDeDatos.query(
                NOMBRE_TABLA,
                columnasAConsultar,
                "NOT UmeEst = '*'",
                null,
                null,
                null,
                null
        );

        if (cursor == null) {
            return unidades;
        }

        if (!cursor.moveToFirst()){
            return unidades;
        }

        do {
            long CodFromDB = cursor.getLong(0);
            String NombreFromDB = cursor.getString(1);
            String EstadoRegistroFromDB = cursor.getString(2);

            UnidadMedida marca = new UnidadMedida(CodFromDB, NombreFromDB, EstadoRegistroFromDB);
            unidades.add(marca);
        } while (cursor.moveToNext());

        cursor.close();
        return unidades;
    }
}
