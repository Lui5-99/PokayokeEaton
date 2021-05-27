package com.example.pokayokeeaton;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class Lectura extends AppCompatActivity {
    ArrayList<String> DatosRec;
    EditText edCodigo2D, edAIAG, edLineSet;
    TextView tvCantidad;
    LinearLayout layoutSetLine;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lectura);
        Bundle extras = getIntent().getExtras();
        DatosRec = extras.getStringArrayList("datos");
        edCodigo2D = findViewById(R.id.edCodigo2D);
        edAIAG = findViewById(R.id.edAIAG);
        edLineSet = findViewById(R.id.edLineSet);
        tvCantidad = findViewById(R.id.tvCantidad);
        layoutSetLine = findViewById(R.id.LayoutLineSetPrincipal);
        if(IsTwo(DatosRec)){
            tvCantidad.setText(" 0 / 2");
            layoutSetLine.setVisibility(View.INVISIBLE);
        }
        else{
            tvCantidad.setText(" 0 / 3 ");
            layoutSetLine.setVisibility(View.VISIBLE);
        }
    }
    private boolean IsTwo(ArrayList<String> datos){
        boolean estado = false;
        String[] Destinos = new String[]{"Escobedo", "Blue Diamond", "Springfield", "Spring 3PL"};
        for(int i = 0; i < Destinos.length; i++){
            if(datos.get(0).equals(Destinos[i])){
                estado = false;
                break;
            }
            else{
                estado = true;
            }
        }
        return estado;
    }
}