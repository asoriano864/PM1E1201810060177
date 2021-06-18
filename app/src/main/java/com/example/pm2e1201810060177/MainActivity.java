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

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    EditText bxNombre;
    EditText bxTelefono;
    EditText bxNota;
    Spinner cmbPais;

    String PaisSeleccionado = "";

    ArrayList<String> paises = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        paises.add("");
        paises.add("Honduras 504");
        paises.add("Guatemala 502");
        paises.add("Costa Rica 506");
        paises.add("El Salvador 503");

        bxNombre = (EditText) findViewById(R.id.txtNombre);
        bxTelefono = (EditText) findViewById(R.id.txtTelefono);
        bxNota = (EditText) findViewById(R.id.txtNota);
        cmbPais = (Spinner) findViewById(R.id.cmbPaises);
        cmbPais.setOnItemSelectedListener(this);

        Button btnAgregar = (Button) findViewById(R.id.btnGuardarContacto);

        ArrayAdapter adp = new ArrayAdapter(this,android.R.layout.simple_spinner_item, paises);

        adp.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);

        cmbPais.setAdapter(adp);

        cmbPais.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                PaisSeleccionado = paises.get(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AgregarContacto();
            }
        });

        Button btnListaContactos = (Button) findViewById(R.id.btnListaContactos);
        btnListaContactos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent lsViewContactos = new Intent(v.getContext(), ListaContactos.class);
                startActivity(lsViewContactos);
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void AgregarContacto(){
        SQLiteConexion conexion = new SQLiteConexion(this, Transacciones.NameDatabase, null, 1);
        SQLiteDatabase db = conexion.getWritableDatabase();

        ContentValues datos = new ContentValues();
        datos.put(Transacciones.Nombre, bxNombre.getText().toString());
        datos.put(Transacciones.Telefono, bxTelefono.getText().toString());
        datos.put(Transacciones.Nota, bxNota.getText().toString());
        datos.put(Transacciones.Pais, PaisSeleccionado);

        if(PaisSeleccionado.equals("")){
            Toast.makeText(getApplicationContext(), "Debe Seleccionar un pais", Toast.LENGTH_LONG).show();
        }else if(bxNombre.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(), "Debe Ingresar un Nombre", Toast.LENGTH_LONG).show();
        }else if(bxTelefono.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(), "Debe Ingresar un Telefono", Toast.LENGTH_LONG).show();
        }
        else{
            try{
                Long Resultado = db.insert(Transacciones.TablaContactos, Transacciones.Id, datos);

                Toast.makeText(getApplicationContext(), "Registro guardado", Toast.LENGTH_LONG).show();

                ClearForm();
            }catch(Exception e){
                Toast.makeText(getApplicationContext(), "Error al guardar el registro", Toast.LENGTH_LONG).show();
            }

            db.close();
        }



    }

    private void ClearForm(){
        bxNombre.setText("");
        bxTelefono.setText("");
        bxNota.setText("");
        PaisSeleccionado = "";
        cmbPais.setSelection(0);
    }

}