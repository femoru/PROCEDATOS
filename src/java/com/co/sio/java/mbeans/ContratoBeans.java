/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.co.sio.java.mbeans;

import java.util.Date;

/**
 *
 * @author fmoctezuma
 */
public class ContratoBeans {
    
    private int idcontrato;
    private String numcontrato;
    private String fechainicio;
    private String fechafin;
    private int activo;
    private ClienteBeans cliente;

    public int getIdcontrato() {
        return idcontrato;
    }

    public void setIdcontrato(int idcontrato) {
        this.idcontrato = idcontrato;
    }

    public String getNumcontrato() {
        return numcontrato;
    }

    public void setNumcontrato(String numcontrato) {
        this.numcontrato = numcontrato;
    }

    public String getFechainicio() {
        return fechainicio;
    }

    public void setFechainicio(String fechainicio) {
        this.fechainicio = fechainicio;
    }

    public String getFechafin() {
        return fechafin;
    }

    public void setFechafin(String fechafin) {
        this.fechafin = fechafin;
    }

    public int getActivo() {
        return activo;
    }

    public void setActivo(int activo) {
        this.activo = activo;
    }

    public ClienteBeans getCliente() {
        return cliente;
    }

    public void setCliente(ClienteBeans cliente) {
        this.cliente = cliente;
    }
    
    
}
