/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.co.sio.java.mbeans;

/**
 *
 * @author fmoctezuma
 */
public class RegistroBeans {

    private int idregistro;
    private UsuarioBeans usuario;
    private LaborBeans labor;
    private String fechaInicio;
    private String fechaFin;
    private int estado;
    private ReferenciasBeans anulado;
    private String Observacion;
    private String datoLabor;
    private int tiempolabor;
    private int registroslabor;
    private int idnomina;
    private int valor;
    private int costo;
    private String mesFacturar;

    public int getIdregistro() {
        return idregistro;
    }

    public void setIdregistro(int idregistro) {
        this.idregistro = idregistro;
    }

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

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public ReferenciasBeans getAnulado() {
        return anulado;
    }

    public void setAnulado(ReferenciasBeans anulado) {
        this.anulado = anulado;
    }

    public String getObservacion() {
        return Observacion;
    }

    public void setObservacion(String Observacion) {
        this.Observacion = Observacion;
    }

    public String getDatoLabor() {
        return datoLabor;
    }

    public void setDatoLabor(String datoLabor) {
        this.datoLabor = datoLabor;
    }

    public int getTiempolabor() {
        return tiempolabor;
    }

    public void setTiempolabor(int tiempolabor) {
        this.tiempolabor = tiempolabor;
    }

    public int getRegistroslabor() {
        return registroslabor;
    }

    public void setRegistroslabor(int registroslabor) {
        this.registroslabor = registroslabor;
    }

    public int getIdnomina() {
        return idnomina;
    }

    public void setIdnomina(int idnomina) {
        this.idnomina = idnomina;
    }

    public int getValor() {
        return valor;
    }

    public void setValor(int valor) {
        this.valor = valor;
    }

    public int getCosto() {
        return costo;
    }

    public void setCosto(int costo) {
        this.costo = costo;
    }

    public String getMesFacturar() {
        return mesFacturar;
    }

    public void setMesFacturar(String mesFacturar) {
        this.mesFacturar = mesFacturar;
    }

    @Override
    public String toString() {
        return String.format("RegistroBeans{idregistro=%s, usuario=%s, labor=%s, fechaInicio=%s, fechaFin=%s, estado=%s, anulado=%s, Observacion=%s, datoLabor=%s, tiempolabor=%s, registroslabor=%s, idnomina=%s, valor=%s, costo=%s, mesFacturar = %s%s", idregistro, usuario, labor, fechaInicio, fechaFin, estado, anulado, Observacion, datoLabor, tiempolabor, registroslabor, idnomina, valor, costo, mesFacturar, '}');
    }
}
