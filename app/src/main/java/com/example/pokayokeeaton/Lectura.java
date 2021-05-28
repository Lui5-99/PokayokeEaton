package com.example.pokayokeeaton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pokayokeeaton.Modelos.RSA;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.security.Key;
import java.util.ArrayList;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class Lectura extends AppCompatActivity {
    ArrayList<String> DatosRec;
    EditText edCodigo2D, edAIAG, edLineSet;
    TextView tvCantidad;
    LinearLayout layoutSetLine;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lectura);
        int PERMISSION = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if(PERMISSION != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
        }
        Bundle extras = getIntent().getExtras();
        DatosRec = extras.getStringArrayList("datos");
        edCodigo2D = findViewById(R.id.edCodigo2D);
        edAIAG = findViewById(R.id.edAIAG);
        edLineSet = findViewById(R.id.edLineSet);
        tvCantidad = findViewById(R.id.tvCantidad);
        layoutSetLine = findViewById(R.id.LayoutLineSetPrincipal);
        crearArchivo();
        if(IsTwo(DatosRec)){
            tvCantidad.setText(" 0 / " + DatosRec.get(2));
            layoutSetLine.setVisibility(View.INVISIBLE);
        }
        else{
            tvCantidad.setText(" 0 /  " + DatosRec.get(2));
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

    private void crearArchivo(){
        try{
            File file = new File(Environment.getExternalStorageDirectory() + "/kmf" , "Test.txt");
            OutputStreamWriter archivo = new OutputStreamWriter(new FileOutputStream(file));
            String texto = "Hola";
            archivo.write(encriptar(texto));
            archivo.flush();
            archivo.close();
           }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }
    private String encriptar(String texto)
    {
        try {

            //Obtenemos el texto desde el cuadro de texto
            String original = texto;

            RSA rsa = new RSA();

            //le asignamos el Contexto
            rsa.setContext(getBaseContext());

            //Generamos un juego de claves
            rsa.genKeyPair(1024);

            //Guardamos en la memoria las claves
            rsa.saveToDiskPrivateKey("rsa.pri");
            rsa.saveToDiskPublicKey("rsa.pub");

            //Ciframos
            String encode_text = rsa.Encrypt(original);

            //Mostramos el texto cifrado

            //Creamos otro objeto de nuestra clase RSA
            RSA rsa2 = new RSA();

            //Le pasamos el contexto
            rsa2.setContext(getBaseContext());

            //Cargamos las claves que creamos anteriormente
            rsa2.openFromDiskPrivateKey("rsa.pri");
            rsa2.openFromDiskPublicKey("rsa.pub");

            //Desciframos
            String decode_text = rsa2.Decrypt(encode_text);

            //Mostramos el texto ya descifrado

            return encode_text;
        }
        catch (Exception e) {
            return "";
        }

    }

}