package com.ikasgela;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Usuario {
    private String handle;
    private String nombre;
    private LocalDateTime fecha_Registro;
    private Nivel nivel;

    //Asociations
    private List<Usuario> seguidos = new ArrayList<>();
    private List<Usuario> seguidores = new ArrayList<>();

    private List<Mensaje> publicaciones = new ArrayList<>();

    //Constructors
    public Usuario(String handle, String nombre, LocalDateTime fecha_Registro, Nivel nivel) {
        this.handle = handle;
        this.nombre = nombre;
        this.fecha_Registro = fecha_Registro;
        this.nivel = nivel;
    }

    //Getters and Setters
    public String getHandle() {
        return handle;
    }

    public void setHandle(String handle) {
        this.handle = handle;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public LocalDateTime getFecha_Registro() {
        return fecha_Registro;
    }

    public void setFecha_Registro(LocalDateTime fecha_Registro) {
        this.fecha_Registro = fecha_Registro;
    }

    public Nivel getNivel() {
        return nivel;
    }

    public void setNivel(Nivel nivel) {
        this.nivel = nivel;
    }

    public List<Usuario> getSeguidos() {
        return seguidos;
    }

    public List<Usuario> getSeguidores() {
        return seguidores;
    }

    public List<Mensaje> getPublicaciones() {
        return publicaciones;
    }

    //Utilities
    public void NewSeguido(Usuario seguido) {
        this.seguidos.add(seguido);
    }

    public void NewSeguidor(Usuario seguidor) {
        this.seguidores.add(seguidor);
        System.out.println("Ahora Sigues a " + this.getHandle());
    }

    public void NewMensaje(Mensaje mensaje) {
        this.publicaciones.add(mensaje);
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "handle='" + handle + '\'' +
                ", nombre='" + nombre + '\'' +
                ", nivel=" + nivel +
                '}';
    }
}
