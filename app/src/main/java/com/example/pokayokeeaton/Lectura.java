package com.example.pokayokeeaton;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pokayokeeaton.Modelos.Escaneos;
import com.example.pokayokeeaton.Modelos.ModeloBD;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;


import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Lectura extends AppCompatActivity {
    ArrayList<String> DatosRec;
    EditText edCodigo2D, edAIAG, edLineSet;
    TextView tvCantidad;
    Button btSiguiente;
    LinearLayout layoutSetLine;
    int PiezasEscaneadas;
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
        PiezasEscaneadas = extras.getInt("Piezas");
        if(PiezasEscaneadas == 0){
            PiezasEscaneadas = 0;
        }
        else{
            PiezasEscaneadas = extras.getInt("Piezas");
        }
        edCodigo2D = findViewById(R.id.edCodigo2D);
        edAIAG = findViewById(R.id.edAIAG);
        edLineSet = findViewById(R.id.edLineSet);
        tvCantidad = findViewById(R.id.tvCantidad);
        layoutSetLine = findViewById(R.id.LayoutLineSetPrincipal);
        btSiguiente = findViewById(R.id.btSiguiente);
        crearFolder("EatonFiles");
        if(IsTwo(DatosRec)){
            tvCantidad.setText(" " + PiezasEscaneadas + " / " + DatosRec.get(2));
            layoutSetLine.setVisibility(View.INVISIBLE);
        }
        else{
            tvCantidad.setText(" " + PiezasEscaneadas + " / " + DatosRec.get(2));
            layoutSetLine.setVisibility(View.VISIBLE);
        }
        if(PiezasEscaneadas == Integer.parseInt(DatosRec.get(2))){
            btSiguiente.setText("Generar Archivo");
        }
    }
    @Override
    public void onBackPressed(){

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
        Date date = new Date();
        DateFormat fecha = new SimpleDateFormat("yyyy-MM-dd_HH:MM:ss");
        String currentDate = fecha.format(date);
        Document docPDF = new Document();
        try{
            List<Escaneos> escaneos = new ArrayList<Escaneos>();
            ModeloBD adminBD = new ModeloBD(this, "Eaton", null, 1);
            SQLiteDatabase BD = adminBD.getWritableDatabase();
            Cursor fila = BD.rawQuery("Select * from Etiqueta", null);
            if(fila.moveToFirst()){
                do{
                    escaneos.add(new Escaneos(fila.getString(0),fila.getString(1),fila.getString(2),fila.getString(3)));
                }
                while (fila.moveToNext());
            }
            File file = new File(Environment.getExternalStorageDirectory() + "/Documents/EatonFiles" , currentDate + ".pdf");
            FileOutputStream ficheroPDF = new FileOutputStream(file.getAbsolutePath());
            PdfWriter writer = PdfWriter.getInstance(docPDF, ficheroPDF);
            docPDF.open();
            docPDF.add(new Paragraph("Work Order: " + DatosRec.get(1)));
            docPDF.add(new Paragraph("Gafete: " + DatosRec.get(3)));
            docPDF.add(new Paragraph("Cliente: " + DatosRec.get(4)));
            docPDF.add(new Paragraph("Destino: " + DatosRec.get(0)));
            docPDF.add(new Paragraph("Cantidad: " + DatosRec.get(2)));
            docPDF.add(new Paragraph(" "));
            for(int i = 0; i < Integer.parseInt(DatosRec.get(2)); i++){
                docPDF.add(new Paragraph("" + escaneos.get(i).getEtiqueta2D() + " | " + escaneos.get(i).getetiquetaAIAG() + " | " + escaneos.get(i).getetiquetaLineSet() + " | " + escaneos.get(i).getfecha()));
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        finally {
            docPDF.close();
        }
    }
    private boolean crearFolder(String nombreCarpeta){
        File carpeta = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), nombreCarpeta);
        if(!carpeta.mkdirs()){
            return false;
        }
        else{
            return true;
        }
    }
    public void siguiente(View v){
        if(btSiguiente.getText().toString().equals("Siguiente")){
            PiezasEscaneadas += 1;
            Intent intent = new Intent(this, Lectura.class);
            intent.putExtra("datos", DatosRec);
            intent.putExtra("Piezas", PiezasEscaneadas);
            Escaneos();
            startActivity(intent);
        }
        else{
            crearArchivo();
            Intent intent = new Intent(this, Packing.class);
            limpiarBD();
            startActivity(intent);
        }
    }
    private void Escaneos(){
        ModeloBD adminBD = new ModeloBD(this, "Eaton", null, 1);
        SQLiteDatabase BD = adminBD.getWritableDatabase();
        ContentValues add = new ContentValues();
        Date date = new Date();
        DateFormat fecha = new SimpleDateFormat("yyyy-MM-dd HH:MM:ss");
        String currentDate = fecha.format(date);
        if(IsTwo(DatosRec)){
            add.put("etiqueta2D", edCodigo2D.getText().toString());
            add.put("etiquetaAIAG", edAIAG.getText().toString());
            add.put("etiquetaLineSet", "");
            add.put("fecha", currentDate);
            BD.insert("Etiqueta", null, add);
        }
        else {
            add.put("etiqueta2D", edCodigo2D.getText().toString());
            add.put("etiquetaAIAG", edAIAG.getText().toString());
            add.put("etiquetaLineSet", edLineSet.getText().toString());
            add.put("fecha", currentDate);
            BD.insert("Etiqueta", null, add);
        }
        BD.close();
    }
    private void limpiarBD(){
        ModeloBD adminBD = new ModeloBD(this, "Eaton", null, 1);
        SQLiteDatabase BD = adminBD.getWritableDatabase();
        BD.execSQL("delete from Etiqueta");
    }
}