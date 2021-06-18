package com.example.pm2e1201810060177.Tablas;

public class Contactos {

    private Integer Id;
    private String Nombre;
    private String Telefono;
    private String Nota;
    private String Pais;

    public static void Contactos(Integer Id, String Nombre, String Telefono, String Nota, String Pais){

    }

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getTelefono() {
        return Telefono;
    }

    public void setTelefono(String telefono) {
        Telefono = telefono;
    }

    public String getNota() {
        return Nota;
    }

    public void setNota(String nota) {
        Nota = nota;
    }

    public String getPais() {
        return Pais;
    }

    public void setPais(String pais) {
        Pais = pais;
    }
}
