/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.co.sio.java.mbeans;

/**
 *
 * @author fmoctezuma
 */
public class PlanillaBeans {

    private int idplanilla;
    private int idusuario;
    private int idlaborcontrato;
    private String fechalabor;
    private String horaInicioD;
    private String horaDescansoD;
    private int tiempoDiurno;
    private String horaReinicioD;
    private String horaFinalD;
    private int tiempoTarde;
    private int registrosLabor;
    private int valor;
    private int costo;

    public int getIdplanilla() {
        return idplanilla;
    }

    public void setIdplanilla(int idplanilla) {
        this.idplanilla = idplanilla;
    }

    public int getIdusuario() {
        return idusuario;
    }

    public void setIdusuario(int idusuario) {
        this.idusuario = idusuario;
    }

    public int getIdlaborcontrato() {
        return idlaborcontrato;
    }

    public void setIdlaborcontrato(int idlaborcontrato) {
        this.idlaborcontrato = idlaborcontrato;
    }

    public String getFechalabor() {
        return fechalabor;
    }

    public void setFechalabor(String fechalabor) {
        this.fechalabor = fechalabor;
    }

    public String getHoraInicioD() {
        return horaInicioD;
    }

    public void setHoraInicioD(String horaInicioD) {
        this.horaInicioD = horaInicioD;
    }

    public String getHoraDescansoD() {
        return horaDescansoD;
    }

    public void setHoraDescansoD(String horaDescansoD) {
        this.horaDescansoD = horaDescansoD;
    }

    public int getTiempoDiurno() {
        return tiempoDiurno;
    }

    public void setTiempoDiurno(int tiempoDiurno) {
        this.tiempoDiurno = tiempoDiurno;
    }

    public String getHoraReinicioD() {
        return horaReinicioD;
    }

    public void setHoraReinicioD(String horaReinicioD) {
        this.horaReinicioD = horaReinicioD;
    }

    public String getHoraFinalD() {
        return horaFinalD;
    }

    public void setHoraFinalD(String horaFinalD) {
        this.horaFinalD = horaFinalD;
    }

    public int getTiempoTarde() {
        return tiempoTarde;
    }

    public void setTiempoTarde(int tiempoTarde) {
        this.tiempoTarde = tiempoTarde;
    }

    public int getRegistrosLabor() {
        return registrosLabor;
    }

    public void setRegistrosLabor(int registrosLabor) {
        this.registrosLabor = registrosLabor;
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
}
