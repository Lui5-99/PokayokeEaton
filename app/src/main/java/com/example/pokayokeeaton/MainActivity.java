package com.example.pokayokeeaton;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pokayokeeaton.Modelos.ModeloBD;

import java.sql.ResultSet;

public class MainActivity extends AppCompatActivity implements config.DatosCuadroDialogo{
    EditText edUser, edPass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edUser = findViewById(R.id.edtUSER);
        edPass = findViewById(R.id.edtPass);
        crearUserDefault();
    }
    public void Log(View v){
        String usuario = "";
        String pass = "";
        String query = "";
        try{
            ModeloBD adminBD = new ModeloBD(this, "Eaton", null, 1);
            SQLiteDatabase BD = adminBD.getWritableDatabase();
            if(!(edUser.getText().toString() == "") || !(edPass.getText().toString() == "")){
                query = "Select * from usuarios where nombre = '" + edUser.getText().toString() + "' and Pass = '" + edPass.getText().toString() + "'";
            }
            else{
                query = "Select * from usuarios where nombre = '' and Pass = ''";
            }
            Cursor fila = BD.rawQuery(query, null);
            if(fila.moveToNext()){
                usuario = fila.getString(1);
                pass = fila.getString(2);
            }
            BD.close();
            if(!(usuario.isEmpty()) || !(pass.isEmpty())){
                Intent intent = new Intent(this, Packing.class);
                startActivity(intent);
            }
            else
                Toast.makeText(this, "Sin acceso", Toast.LENGTH_SHORT).show();
        }
        catch (Exception ex){
            Toast.makeText(this, "" + ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    public void config(View v){
        config con = new config();
        con.CuadroDialogo(this, MainActivity.this);
    }
    @Override
    public void resultadoCuadroDialogo(String user, String pass) {
        ModeloBD adminBD = new ModeloBD(this, "Eaton", null, 1);
        SQLiteDatabase BD = adminBD.getWritableDatabase();
        ContentValues add = new ContentValues();
        add.put("nombre", user);
        add.put("Pass", pass);
        BD.insert("usuarios", null, add);
        BD.close();
    }
    private void crearUserDefault(){
        ModeloBD adminBD = new ModeloBD(this, "Eaton", null, 1);
        SQLiteDatabase BD = adminBD.getWritableDatabase();
        ContentValues add = new ContentValues();
        Cursor fila = BD.rawQuery("Select * from usuarios", null);
        if(!fila.moveToFirst()){
            add.put("nombre", "Test");
            add.put("Pass", "Test");
            BD.insert("usuarios", null, add);
        }
        BD.close();
    }
}