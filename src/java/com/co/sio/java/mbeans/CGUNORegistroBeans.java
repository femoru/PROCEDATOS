/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.co.sio.java.mbeans;

/**
 *
 * @author fmoctezuma
 */
public class CGUNORegistroBeans implements Cloneable {

    private String codigo;
    private String sucursal;
    private String concepto;
    private String centroOperacion;
    private String centroCosto;
    private String fechaMovimiento;
    private String fechaInicialNoLaborado;
    private String fechaFinalNoLaborado;
    private String diasNoLaborado;
    private String actividadDestajo;
    private String ubicacionDestajo;
    private String horasNovedad;
    private String valorNovedad;
    private String cantidadDestajo;
    private String cuota;
    private String fechaConceptosPrima;
    private String cedula;
    private String proyecto;

    public CGUNORegistroBeans() {
        sucursal = "00";
        centroOperacion = "001";
        centroCosto = "08010101";
        actividadDestajo = "        ";
        ubicacionDestajo = "        ";
        cantidadDestajo = "0000000+";
        cuota = "000";
        proyecto = "          ";
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getSucursal() {
        return sucursal;
    }

    public void setSucursal(String sucursal) {
        this.sucursal = sucursal;
    }

    public String getConcepto() {
        return concepto;
    }

    public void setConcepto(String concepto) {
        this.concepto = concepto;
    }

    public String getCentroOperacion() {
        return centroOperacion;
    }

    public void setCentroOperacion(String centroOperacion) {
        this.centroOperacion = centroOperacion;
    }

    public String getCentroCosto() {
        return centroCosto;
    }

    public void setCentroCosto(String centroCosto) {
        this.centroCosto = centroCosto;
    }

    public String getFechaMovimiento() {
        return fechaMovimiento;
    }

    public void setFechaMovimiento(String fechaMovimiento) {
        this.fechaMovimiento = fechaMovimiento;
    }

    public String getFechaInicialNoLaborado() {
        return fechaInicialNoLaborado;
    }

    public void setFechaInicialNoLaborado(String fechaInicialNoLaborado) {
        this.fechaInicialNoLaborado = fechaInicialNoLaborado;
    }

    public String getFechaFinalNoLaborado() {
        return fechaFinalNoLaborado;
    }

    public void setFechaFinalNoLaborado(String fechaFinalNoLaborado) {
        this.fechaFinalNoLaborado = fechaFinalNoLaborado;
    }

    public String getDiasNoLaborado() {
        return diasNoLaborado;
    }

    public void setDiasNoLaborado(String diasNoLaborado) {
        this.diasNoLaborado = diasNoLaborado;
    }

    public String getActividadDestajo() {
        return actividadDestajo;
    }

    public void setActividadDestajo(String actividadDestajo) {
        this.actividadDestajo = actividadDestajo;
    }

    public String getUbicacionDestajo() {
        return ubicacionDestajo;
    }

    public void setUbicacionDestajo(String ubicacionDestajo) {
        this.ubicacionDestajo = ubicacionDestajo;
    }

    public String getHorasNovedad() {
        return horasNovedad;
    }

    public void setHorasNovedad(String horasNovedad) {
        this.horasNovedad = horasNovedad;
    }

    public String getValorNovedad() {
        return valorNovedad;
    }

    public void setValorNovedad(String valorNovedad) {
        this.valorNovedad = valorNovedad;
    }

    public String getCantidadDestajo() {
        return cantidadDestajo;
    }

    public void setCantidadDestajo(String cantidadDestajo) {
        this.cantidadDestajo = cantidadDestajo;
    }

    public String getCuota() {
        return cuota;
    }

    public void setCuota(String cuota) {
        this.cuota = cuota;
    }

    public String getFechaConceptosPrima() {
        return fechaConceptosPrima;
    }

    public void setFechaConceptosPrima(String fechaConceptosPrima) {
        this.fechaConceptosPrima = fechaConceptosPrima;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getProyecto() {
        return proyecto;
    }

    public void setProyecto(String proyecto) {
        this.proyecto = proyecto;
    }

    @Override
    public CGUNORegistroBeans clone() throws CloneNotSupportedException {
        return (CGUNORegistroBeans) super.clone(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String toString() {
        return codigo + sucursal + concepto + centroOperacion + centroCosto + fechaMovimiento + fechaInicialNoLaborado + fechaFinalNoLaborado + diasNoLaborado + actividadDestajo + ubicacionDestajo + horasNovedad + valorNovedad + cantidadDestajo + cuota + fechaConceptosPrima + cedula + proyecto;
    }
}
