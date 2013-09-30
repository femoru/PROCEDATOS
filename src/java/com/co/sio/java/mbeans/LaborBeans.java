/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.co.sio.java.mbeans;

/**
 *
 * @author fmoctezuma
 */
public class LaborBeans {

    private int idlaborcontrato;
    private ContratoBeans contrato;
    private GrupoBeans grupo;
    private int labor;
    private int tipolabor;
    private int horaextra;
    private String valor;
    private String costo;
    private int activo;
    private int datolabor;
    private int conciliacion;

    public int getConciliacion() {
        return conciliacion;
    }

    public void setConciliacion(int conciliacion) {
        this.conciliacion = conciliacion;
    }

    public int getTipolabor() {
        return tipolabor;
    }

    public void setTipolabor(int tipolabor) {
        this.tipolabor = tipolabor;
    }

    public int getHoraextra() {
        return horaextra;
    }

    public void setHoraextra(int horaextra) {
        this.horaextra = horaextra;
    }

    public int getDatolabor() {
        return datolabor;
    }

    public void setDatolabor(int datolabor) {
        this.datolabor = datolabor;
    }

    public int getIdlaborcontrato() {
        return idlaborcontrato;
    }

    public void setIdlaborcontrato(int idlaborcontrato) {
        this.idlaborcontrato = idlaborcontrato;
    }

    public ContratoBeans getContrato() {
        return contrato;
    }

    public void setContrato(ContratoBeans contrato) {
        this.contrato = contrato;
    }

    public GrupoBeans getGrupo() {
        return grupo;
    }

    public void setGrupo(GrupoBeans grupo) {
        this.grupo = grupo;
    }

    public int getLabor() {
        return labor;
    }

    public void setLabor(int labor) {
        this.labor = labor;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getCosto() {
        return costo;
    }

    public void setCosto(String costo) {
        this.costo = costo;
    }

    public int getActivo() {
        return activo;
    }

    public void setActivo(int activo) {
        this.activo = activo;
    }
}
