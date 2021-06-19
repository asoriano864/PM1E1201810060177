package com.example.pm2e1201810060177;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.pm2e1201810060177.Tablas.Contactos;
import com.example.pm2e1201810060177.Transacciones.Transacciones;
import static android.Manifest.permission.CALL_PHONE;

import java.util.ArrayList;

public class ListaContactos extends AppCompatActivity {

    SQLiteConexion conexion;
    ListView lsViewContactos;
    ArrayList<Contactos> Contactos;
    ArrayList<String> ArregloContactos;
    EditText txtFiltro;
    Integer ContactoSelected = -1;
    ArrayAdapter adp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_contactos);
        //-----------------------------------------------------------------------------------------------------------------------------------//
        /* Establecemos la conexion a la base de datos y asociamos el objeto de lista */
        conexion = new SQLiteConexion(this, Transacciones.NameDatabase, null, 1);
        lsViewContactos = (ListView) findViewById(R.id.listViewContactos);
        //-----------------------------------------------------------------------------------------------------------------------------------//

        //-----------------------------------------------------------------------------------------------------------------------------------//
        /*Ejecutamos el metodo para traer los datos de la base de datos y asociarlos a un arreglo*/
        ObtenerContactos();

        /* Creamos un objeto Adapter para asociar los datos del arreglo a el ListView */
        adp = new ArrayAdapter<String>(ListaContactos.this, android.R.layout.simple_list_item_1, ArregloContactos);
        lsViewContactos.setAdapter(adp);
        lsViewContactos.setTextFilterEnabled(true);

        txtFiltro = (EditText) findViewById(R.id.txtFiltro);
        txtFiltro.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                ListaContactos.this.adp.getFilter().filter(arg0);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //-----------------------------------------------------------------------------------------------------------------------------------//

        //-----------------------------------------------------------------------------------------------------------------------------------//
        /* Creamos un evento on click para los elementos del ListView */
        lsViewContactos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /*SELECCIONAMOS EL ID CORRESPONDIENTE AL CONTACTO SELECCIONADO */
                ContactoSelected = position;


            }
        });
        //-----------------------------------------------------------------------------------------------------------------------------------//

        //-----------------------------------------------------------------------------------------------------------------------------------//
        /* Creamos un evento para el boton de llamar a contacto*/
        Button btnLlamarContacto = (Button) findViewById(R.id.btnLlamar);
        btnLlamarContacto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Se valida que exista un contacto seleccionado*/
                if(!ContactoSelected.equals(null)){
                    /*Creamos el objeto de dialogo para la ventana emergente*/
                    AlertDialog.Builder builder;
                    builder = new AlertDialog.Builder(v.getContext());
                    builder.setMessage("Desea llamar a "+Contactos.get(ContactoSelected).getNombre()).setTitle("Llamar Contacto");

                    /*Boton de dialogo ACEPTAR*/
                    builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent i = new Intent(Intent.ACTION_CALL);
                            i.setData(Uri.parse("tel:"+Contactos.get(ContactoSelected).getTelefono()));

                            if (ContextCompat.checkSelfPermission(getApplicationContext(), CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                                startActivity(i);
                            } else {
                                requestPermissions(new String[]{CALL_PHONE}, 1);
                            }
                        }
                    });

                    /*Boton de Dialogo Cancelar*/
                    builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }else{
                    /*No hay contacto Seleccionado*/
                    Toast.makeText(getApplicationContext(),"No hay un contacto seleccionado", Toast.LENGTH_LONG).show();
                }

            }
        });

        //-----------------------------------------------------------------------------------------------------------------------------------//


        //-----------------------------------------------------------------------------------------------------------------------------------//
        /*Mandar datos a la actividad de actualizar contactos*/
        Button btnActualizarContacto = (Button) findViewById(R.id.btnActualizar);
        btnActualizarContacto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if(ContactoSelected>=0){
                        Integer idS = ContactoSelected;

                        Intent ActualizarContacto = new Intent(v.getContext(), com.example.pm2e1201810060177.ActualizarContacto.class);
                        ActualizarContacto.putExtra("NOMBRE",Contactos.get(idS).getNombre().toString());
                        ActualizarContacto.putExtra("TELEFONO",Contactos.get(idS).getTelefono().toString());
                        ActualizarContacto.putExtra("NOTA",Contactos.get(idS).getNota().toString());
                        ActualizarContacto.putExtra("PAIS",Contactos.get(idS).getPais().toString());
                        ActualizarContacto.putExtra("ID",Contactos.get(idS).getId().toString());
                        startActivity(ActualizarContacto);
                    }else{
                        Toast.makeText(getApplicationContext(), "Debe Seleccionar un contacto",Toast.LENGTH_LONG).show();
                    }


            }
        });
        //-----------------------------------------------------------------------------------------------------------------------------------//

        //-----------------------------------------------------------------------------------------------------------------------------------//
        /*Eliminar Contacto*/
        Button btnEliminar = findViewById(R.id.btnEliminar);
        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContactoSelected>=0){
                    try{
                        String IDEliminar = Contactos.get(ContactoSelected).getId().toString();
                        Eliminar(IDEliminar);
                        Intent ListaContactos = new Intent(v.getContext(), ListaContactos.class);
                        startActivity(ListaContactos);
                    }catch(Exception e){
                        Toast.makeText(getApplicationContext(), "Ocurrio un error al eliminar el contacto",Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "Debe Seleccionar un contacto",Toast.LENGTH_LONG).show();
                }
            }
        });
        //-----------------------------------------------------------------------------------------------------------------------------------//

    }

    private void ObtenerContactos(){
        SQLiteDatabase db = conexion.getReadableDatabase();
        Contactos contacto = null;

        Contactos = new ArrayList<Contactos>();

        Cursor cursor = db.rawQuery("SELECT * FROM "+Transacciones.TablaContactos, null);

        while(cursor.moveToNext()){
            contacto = new Contactos();
            contacto.setId(cursor.getInt(0));
            contacto.setNombre(cursor.getString(1));
            contacto.setTelefono(cursor.getString(2));
            contacto.setNota(cursor.getString(3));
            contacto.setPais(cursor.getString(4));
            Contactos.add(contacto);
        }
        fillListaContactos();

    }

    private void fillListaContactos(){
        ArregloContactos = new ArrayList<String>();
        for(int i = 0;i<Contactos.size();i++){
            ArregloContactos.add((i+1) + " - " + Contactos.get(i).getNombre() + " (" + Contactos.get(i).getTelefono() + ")");
        }
    }

    private void Eliminar(String id){
        SQLiteDatabase db = conexion.getWritableDatabase();
        String[] params = {id};
        String WhereCondition = Transacciones.Id + "= ?";
        db.delete(Transacciones.TablaContactos, WhereCondition, params);
        Toast.makeText(getApplicationContext(), "Registro Eliminado con exito", Toast.LENGTH_LONG).show();
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



}