/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.co.sio.java.mbeans;

/**
 *
 * @author fmoctezuma
 */
public class PermisosPerfilesBeans {

    private String codpermiso;
    private String codperfil;
    private UsuarioBeans usuario;

    public String getCodpermiso() {
        return codpermiso;
    }

    public void setCodpermiso(String codpermiso) {
        this.codpermiso = codpermiso;
    }

    public String getCodperfil() {
        return codperfil;
    }

    public void setCodperfil(String codperfil) {
        this.codperfil = codperfil;
    }

    public UsuarioBeans getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioBeans usuario) {
        this.usuario = usuario;
    }
}
