package com.example.pm2e1201810060177;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.pm2e1201810060177.Tablas.Contactos;
import com.example.pm2e1201810060177.Transacciones.Transacciones;

import java.util.ArrayList;

public class ListaContactos extends AppCompatActivity {

    SQLiteConexion conexion;
    ListView lsViewContactos;
    ArrayList<Contactos> Contactos;
    ArrayList<String> ArregloContactos;

    Integer ContactoSelected = null;

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
        ArrayAdapter adp = new ArrayAdapter(this, android.R.layout.simple_list_item_1, ArregloContactos);
        lsViewContactos.setAdapter(adp);
        //-----------------------------------------------------------------------------------------------------------------------------------//

        //-----------------------------------------------------------------------------------------------------------------------------------//
        /* Creamos un evento on click para los elementos del ListView */
        lsViewContactos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /*SELECCIONAMOS EL ID CORRESPONDIENTE AL CONTACTO SELECCIONADO */
                ContactoSelected = Contactos.get(position).getId();
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
                if(!ContactoSelected.equals("")){
                    /*Creamos el objeto de dialogo para la ventana emergente*/
                    AlertDialog.Builder builder;
                    builder = new AlertDialog.Builder(v.getContext());
                    builder.setMessage("Desea llamar a "+Contactos.get(ContactoSelected).getNombre()).setTitle("Llamar Contacto");

                    /*Boton de dialogo ACEPTAR*/
                    builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User clicked OK button
                            Intent i = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+Contactos.get(ContactoSelected).getTelefono()));
                            startActivity(i);
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



}