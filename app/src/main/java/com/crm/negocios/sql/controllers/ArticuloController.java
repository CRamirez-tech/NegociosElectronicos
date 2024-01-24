package com.crm.negocios.sql.controllers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.crm.negocios.sql.model.Articulo;
import com.crm.negocios.sql.BDHelper;

import java.util.ArrayList;

public class ArticuloController {
    private final BDHelper ayudanteBaseDeDatos;
    private final String NOMBRE_TABLA = "Articulos";

    public ArticuloController(Context context) {
        ayudanteBaseDeDatos = new BDHelper(context,null,1);
    }

    public int eliminarArticulo(Articulo articulo) {
        SQLiteDatabase baseDeDatos = ayudanteBaseDeDatos.getWritableDatabase();
        String[] argumentos = {String.valueOf(articulo.getCod())};
        return baseDeDatos.delete(NOMBRE_TABLA, "ArtCod = ?", argumentos);
    }

    public long nuevoArticulo(Articulo articulo) {
        // writable porque vamos a insertar
        SQLiteDatabase baseDeDatos = ayudanteBaseDeDatos.getWritableDatabase();
        ContentValues valoresParaInsertar = new ContentValues();

        valoresParaInsertar.put("ArtNom",articulo.getNombre());
        valoresParaInsertar.put("UmeCod",articulo.getUnidadMedida());
        valoresParaInsertar.put("ArtPre",articulo.getPrecioUnitario());
        valoresParaInsertar.put("MarCod",articulo.getMarca());
        valoresParaInsertar.put("ArtEst","A");

        return baseDeDatos.insert(NOMBRE_TABLA, null, valoresParaInsertar);
    }

    public int guardarCambios(Articulo articulo) {
        SQLiteDatabase baseDeDatos = ayudanteBaseDeDatos.getWritableDatabase();
        ContentValues valoresParaActualizar = new ContentValues();

        valoresParaActualizar.put("ArtNom",articulo.getNombre());
        valoresParaActualizar.put("UmeCod",articulo.getUnidadMedida());
        valoresParaActualizar.put("ArtPre",articulo.getPrecioUnitario());
        valoresParaActualizar.put("MarCod",articulo.getMarca());
        valoresParaActualizar.put("ArtEst",articulo.getEstadoRegistro());

        String campoParaActualizar = "ArtCod = ?";
        String[] argumentosParaActualizar = {String.valueOf(articulo.getCod())};
        return baseDeDatos.update(NOMBRE_TABLA, valoresParaActualizar, campoParaActualizar, argumentosParaActualizar);
    }

    public ArrayList<Articulo> obtenerArticulos() {
        ArrayList<Articulo> articulos = new ArrayList<>();

        // readable porque no vamos a modificar, solamente leer
        SQLiteDatabase baseDeDatos = ayudanteBaseDeDatos.getReadableDatabase();

        // SELECT nombre, unidadMedida, etc
        String[] columnasAConsultar = {"ArtCod","ArtNom", "UmeCod", "ArtPre","MarCod","ArtEst"};
        Cursor cursor = baseDeDatos.query(
                NOMBRE_TABLA,
                columnasAConsultar,
                null,
                null,
                null,
                null,
                null
        );

        if (cursor == null) {
            return articulos;
        }

        if (!cursor.moveToFirst()){
            return articulos;
        }

        do {
            long CodFromDB = cursor.getLong(0);
            String NombreFromDB = cursor.getString(1);
            int UnidadMedidaFromDB = cursor.getInt(2);
            double PrecioUnitarioFromDB = cursor.getDouble(3);
            int MarcaFromDB = cursor.getInt(4);
            String EstadoRegistroFromDB = cursor.getString(5);

            Articulo articulo = new Articulo(CodFromDB, NombreFromDB, UnidadMedidaFromDB, PrecioUnitarioFromDB, MarcaFromDB, EstadoRegistroFromDB);
            articulos.add(articulo);
        } while (cursor.moveToNext());

        cursor.close();
        return articulos;
    }
}