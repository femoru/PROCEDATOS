/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.co.sio.java.mbeans;

/**
 *
 * @author fmoctezuma
 */
public class GrupoBeans {

    private int idgrupo;
    private int idarea;
    private int codgrupo;
    private String desgrupo;
    private UsuarioBeans cordinador;

    public int getIdgrupo() {
        return idgrupo;
    }

    public void setIdgrupo(int idgrupo) {
        this.idgrupo = idgrupo;
    }

    public int getIdarea() {
        return idarea;
    }

    public void setIdarea(int idarea) {
        this.idarea = idarea;
    }

    public int getCodgrupo() {
        return codgrupo;
    }

    public void setCodgrupo(int codgrupo) {
        this.codgrupo = codgrupo;
    }

    public String getDesgrupo() {
        return desgrupo;
    }

    public void setDesgrupo(String desgrupo) {
        this.desgrupo = desgrupo;
    }   

    public UsuarioBeans getCordinador() {
        return cordinador;
    }

    public void setCordinador(UsuarioBeans cordinador) {
        this.cordinador = cordinador;
    }
}
