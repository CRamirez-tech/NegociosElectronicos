package com.crm.negocios.sql;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class BDHelper extends SQLiteOpenHelper {
    private static final String NOMBRE_BASE_DE_DATOS = "negocios";
    final String INIT_SQL_0 = "PRAGMA foreign_keys = YES;";
    final String INIT_SQL_1 = "CREATE TABLE IF NOT EXISTS UnidadMedida (UmeCod INTEGER PRIMARY KEY AUTOINCREMENT, UmeNom TEXT NOT NULL, UmeEst TEXT NOT NULL);";
    final String INIT_SQL_2 = "CREATE TABLE IF NOT EXISTS Marca (MarCod INTEGER PRIMARY KEY AUTOINCREMENT, MarNom TEXT NOT NULL, MarEst TEXT NOT NULL);";
    final String INIT_SQL_3 = "CREATE TABLE IF NOT EXISTS Articulos (ArtCod INTEGER PRIMARY KEY AUTOINCREMENT, ArtNom TEXT NOT NULL, UmeCod INTEGER NOT NULL, ArtPre REAL NOT NULL, MarCod INTEGER NOT NULL, ArtEst TEXT NOT NULL,CONSTRAINT fk_unidad_medida FOREIGN KEY(UmeCod) REFERENCES UnidadMedida (UmeCod),CONSTRAINT fk_marca  FOREIGN KEY(MarCod) REFERENCES Marca (MarCod) );";

    public BDHelper(@Nullable Context context, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, NOMBRE_BASE_DE_DATOS, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(INIT_SQL_1);
        db.execSQL(INIT_SQL_2);
        db.execSQL(INIT_SQL_3);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        db.execSQL(INIT_SQL_0);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
