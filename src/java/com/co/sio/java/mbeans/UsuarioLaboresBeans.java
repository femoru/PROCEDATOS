/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.co.sio.java.mbeans;

/**
 *
 * @author fmoctezuma
 */
public class UsuarioLaboresBeans {

    private UsuarioBeans usuario;
    private LaborBeans labor;
    private String fechaInicio;
    private String fechaFin;

    public UsuarioBeans getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioBeans usuario) {
        this.usuario = usuario;
    }

    public LaborBeans getLabor() {
        return labor;
    }

    public void setLabor(LaborBeans labor) {
        this.labor = labor;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(String fechaFin) {
        this.fechaFin = fechaFin;
    }
}
