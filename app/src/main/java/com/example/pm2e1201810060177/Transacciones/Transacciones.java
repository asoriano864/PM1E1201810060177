package com.example.pm2e1201810060177.Transacciones;

public class Transacciones {
    /* Tablas */
    public static final String TablaContactos = "contactos";

    /*Campos*/
    public static final String Id = "id";
    public static final String Nombre = "nombre";
    public static final String Telefono = "telefono";
    public static final String Nota = "nota";
    public static final String Pais = "pais";

    /*tabla Create - drop*/
    public static final String CreateTableContactos = "CREATE TABLE contactos(id INTEGER PRIMARY KEY AUTOINCREMENT, nombre TEXT, telefono TEXT, nota TEXT, pais TEXT)";
    public static final String DropContactos = "DROP TABLE IF EXIST contactos";

    /*creacion del nombre de la base de datos*/
    public static final String NameDatabase = "DBContactos";

}
