package com.ikasgela;

import java.time.LocalDateTime;

public class Mensaje {
    private String texto;
    private LocalDateTime fecha_Publicacion;
    private int likes;

    //Asociations
    private Usuario usuario;

    //Constructor
    public Mensaje(String texto, LocalDateTime fecha_Publicacion) {
        this.texto = texto;
        this.fecha_Publicacion = fecha_Publicacion;
        this.likes = 0;
    }

    public Mensaje(String texto, LocalDateTime fecha_Publicacion, Usuario usuario) {
        this.texto = texto;
        this.fecha_Publicacion = fecha_Publicacion;
        this.likes = 0;
        this.usuario = usuario;
    }

    //Getters and Setters

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public LocalDateTime getFecha_Publicacion() {
        return fecha_Publicacion;
    }

    public void setFecha_Publicacion(LocalDateTime fecha_Publicacion) {
        this.fecha_Publicacion = fecha_Publicacion;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    //Utilities
    public void DarLike() {
        this.likes++;
    }

    @Override
    public String toString() {
        int hora = fecha_Publicacion.getHour();
        int minuto = fecha_Publicacion.getMinute();

        return String.format("%-15s\n%-1s\nLikes: %-3d\nPublicacion:%td %tb %tY | Hora: %-2d : %-2d\n",
                this.usuario.getHandle(), texto, likes, fecha_Publicacion, fecha_Publicacion, fecha_Publicacion, hora, minuto);
    }
}
