/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.co.sio.java.mbeans;

import java.util.Date;

/**
 *
 * @author rquintero
 */
public class PerfilBeans {

    private int cod_perfil;
    private String des_perfil;
    private int estado;
    private UsuarioBeans usuario;
    private String fecha_digita;
    private String menu;
    private String Mensaje;

    public String getMenu() {
        return menu;
    }

    public void setMenu(String menu) {
        this.menu = menu;
    }

    public String getMensaje() {
        return Mensaje;
    }

    public void setMensaje(String Mensaje) {
        this.Mensaje = Mensaje;
    }

    public int getCod_perfil() {
        return cod_perfil;
    }

    public void setCod_perfil(int cod_perfil) {
        this.cod_perfil = cod_perfil;
    }

    public String getDes_perfil() {
        return des_perfil;
    }

    public void setDes_perfil(String des_perfil) {
        this.des_perfil = des_perfil;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public String getFecha_digita() {
        return fecha_digita;
    }

    public void setFecha_digita(String fecha_digita) {
        this.fecha_digita = fecha_digita;
    }

    public UsuarioBeans getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioBeans usuario) {
        this.usuario = usuario;
    }
}
