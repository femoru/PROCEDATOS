/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.co.sio.java.mbeans;

/**
 *
 * @author rquintero
 */
public class UsuarioBeans extends PerfilBeans {

    private int idusuario;
    private String login;
    private String clave;
    private int activo;
    private int idUsuarioDigita;
    private String fechaDigita;
    private String fechaUltimoAcceso;
    private PerfilBeans perfil;
    private String imagen;
    private String usuariosos;
    private String mensaje;

    public String getUsuariosos() {
        return usuariosos;
    }

    public void setUsuariosos(String usuariosos) {
        this.usuariosos = usuariosos;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public PerfilBeans getPerfil() {
        return perfil;
    }

    public void setPerfil(PerfilBeans perfil) {
        this.perfil = perfil;
    }

    public int getIdusuario() {
        return idusuario;
    }

    public void setIdusuario(int idusuario) {
        this.idusuario = idusuario;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public int getActivo() {
        return activo;
    }

    public void setActivo(int activo) {
        this.activo = activo;
    }

    public int getIdUsuarioDigita() {
        return idUsuarioDigita;
    }

    public void setIdUsuarioDigita(int idUsuarioDigita) {
        this.idUsuarioDigita = idUsuarioDigita;
    }

    public String getFechaDigita() {
        return fechaDigita;
    }

    public void setFechaDigita(String fechaDigita) {
        this.fechaDigita = fechaDigita;
    }

    public String getFechaUltimoAcceso() {
        return fechaUltimoAcceso;
    }

    public void setFechaUltimoAcceso(String fechaUltimoAcceso) {
        this.fechaUltimoAcceso = fechaUltimoAcceso;
    }
}
