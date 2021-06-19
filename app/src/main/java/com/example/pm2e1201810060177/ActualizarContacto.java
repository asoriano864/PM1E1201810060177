package com.example.pm2e1201810060177;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.pm2e1201810060177.Transacciones.Transacciones;

import java.util.ArrayList;

public class ActualizarContacto extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    SQLiteConexion conexion;

    private Integer Id;
    private String Nombre;
    private String Telefono;
    private String Nota;
    private String Pais;

    EditText bxNombre;
    EditText bxTelefono;
    EditText bxNota;
    Spinner cmbPais;
    String PaisSelected = "";

    ArrayList<String> paises = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar_contacto);

        conexion = new SQLiteConexion(this, Transacciones.NameDatabase, null, 1);
        Bundle extras = getIntent().getExtras();
        Nombre = extras.getString("NOMBRE");
        Telefono = extras.getString("TELEFONO");
        Nota = extras.getString("NOTA");
        Pais = extras.getString("PAIS");
        Id = Integer.parseInt(extras.getString("ID"));

        paises.add("");
        paises.add("Honduras 504");
        paises.add("Guatemala 502");
        paises.add("Costa Rica 506");
        paises.add("El Salvador 503");

        cmbPais = (Spinner) findViewById(R.id.cmbPaisUpd);
        cmbPais.setOnItemSelectedListener(this);
        ArrayAdapter adp = new ArrayAdapter(this,android.R.layout.simple_spinner_item, paises);
        adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cmbPais.setAdapter(adp);

        cmbPais.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                PaisSelected = paises.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        bxNombre = findViewById(R.id.txtNombreUpd);
        bxTelefono = findViewById(R.id.txtTelefonoUpd);
        bxNota = findViewById(R.id.txtNotaUpd);

        bxNombre.setText(Nombre);
        bxTelefono.setText(Telefono);
        bxNota.setText(Nota);

        for(Integer i = 0; i < paises.size(); i++){
            if(Pais.equals(paises.get(i))){
                cmbPais.setSelection(i);
            }
        }

        Button btnActualizar = findViewById(R.id.btnActualizarContacto);
        btnActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!bxNombre.getText().toString().equals("") || !bxTelefono.getText().toString().equals("") || !bxTelefono.getText().toString().equals("") || !PaisSelected.equals("")){
                    Actualizar(Id.toString(),bxNombre.getText().toString(),bxTelefono.getText().toString(),bxNota.getText().toString(),PaisSelected);
                    Intent ListaContactos = new Intent(v.getContext(), ListaContactos.class);
                    startActivity(ListaContactos);
                }else{
                    Toast.makeText(getApplicationContext(), "Debe llenar todos los campos del contacto", Toast.LENGTH_LONG);
                }
            }
        });

        Button btnCancelar = findViewById(R.id.btnCancelar);
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ListaContactos = new Intent(v.getContext(), ListaContactos.class);
                startActivity(ListaContactos);
            }
        });


    }

    private void Actualizar(String id, String Nombre, String Telefono, String Nota, String Pais){
        SQLiteDatabase db = conexion.getWritableDatabase();
        String[] params = {id};
        ContentValues datos = new ContentValues();
        datos.put(Transacciones.Nombre, Nombre);
        datos.put(Transacciones.Telefono, Telefono);
        datos.put(Transacciones.Nota, Nota);
        datos.put(Transacciones.Pais, Pais);
        try{
            db.update(Transacciones.TablaContactos,datos,Transacciones.Id + "=?", params);
            Toast.makeText(getApplicationContext(), "Datos actualizados con exito",Toast.LENGTH_LONG).show();
        }catch(Exception e){
            Toast.makeText(getApplicationContext(), "Error al actuaizar los datos",Toast.LENGTH_LONG).show();
        }


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}