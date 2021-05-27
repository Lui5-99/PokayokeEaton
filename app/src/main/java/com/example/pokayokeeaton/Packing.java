package com.example.pokayokeeaton;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.pokayokeeaton.Modelos.ModeloBD;

import java.util.ArrayList;

public class Packing extends AppCompatActivity {
    EditText edtCodigo, edNoDelivery, edNoPzas;
    Spinner cbCliente, cbDestino;
    ArrayList<String> lClientes, lDestinos;
    ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_packing);
        edtCodigo = findViewById(R.id.edtCodigo);
        edNoDelivery = findViewById(R.id.edNoDelivery);
        edNoPzas = findViewById(R.id.edNoPzas);
        cbCliente = findViewById(R.id.cbCliente);
        cbDestino = findViewById(R.id.cbDestino);
        lClientes = new ArrayList<String>();
        lDestinos = new ArrayList<String>();
        llenarCB(cbCliente, lClientes, "clientes", "nombre");
        llenarCB(cbDestino, lDestinos, "destinos", "nombre");
    }
    private void llenarCB(Spinner combo, ArrayList<String> lista, String tabla, String columna){
        try{
            Clientes();
            Destinos();
            ModeloBD adminBD = new ModeloBD(this, "Eaton", null, 1);
            SQLiteDatabase BD = adminBD.getWritableDatabase();
            Cursor fila = BD.rawQuery("Select " + columna + " from " + tabla, null);
            if(fila.moveToFirst()){
                do{
                    lista.add(fila.getString(0));
                }
                while(fila.moveToNext());
            }
            adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, lista);
            combo.setAdapter(adapter);
        }
        catch (Exception ex){
            Toast.makeText(this, "" + ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    private void Clientes(){
        ModeloBD adminBD = new ModeloBD(this, "Eaton", null, 1);
        SQLiteDatabase BD = adminBD.getWritableDatabase();
        Cursor fila = BD.rawQuery("Select * from clientes", null);
        if(!fila.moveToFirst()){
            ContentValues add = new ContentValues();
            add.put("Nombre", "International");
            BD.insert("clientes", null, add);
            add.put("Nombre", "Kenworth");
            BD.insert("clientes", null, add);
            add.put("Nombre", "Paccar Peterbilt");
            BD.insert("clientes", null, add);
            add.put("Nombre", "DTNA /Freightliner");
            BD.insert("clientes", null, add);
            add.put("Nombre", "Intercompanias");
            BD.insert("clientes", null, add);
            add.put("Nombre", "Mack");
            BD.insert("clientes", null, add);
            add.put("Nombre", "Volvo");
            BD.insert("clientes", null, add);
            add.put("Nombre", "Blue Bird");
            BD.insert("clientes", null, add);
        }
        
    }
    private void Destinos(){
        ModeloBD adminBD = new ModeloBD(this, "Eaton", null, 1);
        SQLiteDatabase BD = adminBD.getWritableDatabase();
        Cursor fila = BD.rawQuery("Select * from Destinos", null);
        if(!fila.moveToFirst()) {
            ContentValues add = new ContentValues();
            add.put("Nombre", "Escobedo");
            BD.insert("destinos", null, add);
            add.put("Nombre", "Blue Diamond");
            BD.insert("destinos", null, add);
            add.put("Nombre", "Springfield");
            BD.insert("destinos", null, add);
            add.put("Nombre", "Mexicali");
            BD.insert("destinos", null, add);
            add.put("Nombre", "MD St. Therese");
            BD.insert("destinos", null, add);
            add.put("Nombre", "Chillicothe");
            BD.insert("destinos", null, add);
            add.put("Nombre", "Renton");
            BD.insert("destinos", null, add);
            add.put("Nombre", "Denton");
            BD.insert("destinos", null, add);
            add.put("Nombre", "Cleveland");
            BD.insert("destinos", null, add);
            add.put("Nombre", "Portland");
            BD.insert("destinos", null, add);
            add.put("Nombre", "Santiago");
            BD.insert("destinos", null, add);
            add.put("Nombre", "Saltillo");
            BD.insert("destinos", null, add);
            add.put("Nombre", "Mount Holly");
            BD.insert("destinos", null, add);
            add.put("Nombre", "Gaffney");
            BD.insert("destinos", null, add);
            add.put("Nombre", "Servicios  /AFM");
            BD.insert("destinos", null, add);
            add.put("Nombre", "Greenfield");
            BD.insert("destinos", null, add);
            add.put("Nombre", "Brasil");
            BD.insert("destinos", null, add);
            add.put("Nombre", "Australia");
            BD.insert("destinos", null, add);
            add.put("Nombre", "Galesburg");
            BD.insert("destinos", null, add);
            add.put("Nombre", "Galesburg");
            BD.insert("destinos", null, add);
            add.put("Nombre", "Salem");
            BD.insert("destinos", null, add);
            add.put("Nombre", "Blue Bird");
            BD.insert("destinos", null, add);
            add.put("Nombre", "Escobedo");
        }
    }
}