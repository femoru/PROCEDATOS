/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.co.sio.java.mbeans;

/**
 *
 * @author fmoctezuma
 */
public class ArchivoPlanoBeans {
    private int idArchivo;
    private int idCliente;
    private int idLaborContrato;
    private String inicialCarga;
    private String finalCarga;
    private String fechaCarga;
    private int totalRegistros;
    private int idUsuario;

    public int getIdArchivo() {
        return idArchivo;
    }

    public void setIdArchivo(int idArchivo) {
        this.idArchivo = idArchivo;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public int getIdLaborContrato() {
        return idLaborContrato;
    }

    public void setIdLaborContrato(int idLaborContrato) {
        this.idLaborContrato = idLaborContrato;
    }

    public String getInicialCarga() {
        return inicialCarga;
    }

    public void setInicialCarga(String inicialCarga) {
        this.inicialCarga = inicialCarga;
    }

    public String getFinalCarga() {
        return finalCarga;
    }

    public void setFinalCarga(String finalCarga) {
        this.finalCarga = finalCarga;
    }

    public String getFechaCarga() {
        return fechaCarga;
    }

    public void setFechaCarga(String fechaCarga) {
        this.fechaCarga = fechaCarga;
    }

    public int getTotalRegistros() {
        return totalRegistros;
    }

    public void setTotalRegistros(int totalRegistros) {
        this.totalRegistros = totalRegistros;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }
    
}
