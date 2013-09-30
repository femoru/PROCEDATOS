/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.co.sio.java.mbeans;

/**
 *
 * @author fmoctezuma
 */
public class SolicitudBeans {

    private int idsolicitud;
    private ReferenciasBeans solicitud;
    private UsuarioBeans usuarioSolicitud;
    private String fechaSolicitud;
    private String observacion;
    private int estado;
    private UsuarioBeans usuarioRespuesta;
    private String fechaRespuesta;
    private String respuesta;
    private GrupoBeans grupo;

    public GrupoBeans getGrupo() {
        return grupo;
    }

    public void setGrupo(GrupoBeans grupo) {
        this.grupo = grupo;
    }

    public int getIdsolicitud() {
        return idsolicitud;
    }

    public void setIdsolicitud(int idsolicitud) {
        this.idsolicitud = idsolicitud;
    }

    public ReferenciasBeans getSolicitud() {
        return solicitud;
    }

    public void setSolicitud(ReferenciasBeans solicitud) {
        this.solicitud = solicitud;
    }

    public UsuarioBeans getUsuarioSolicitud() {
        return usuarioSolicitud;
    }

    public void setUsuarioSolicitud(UsuarioBeans usuarioSolicitud) {
        this.usuarioSolicitud = usuarioSolicitud;
    }

    public String getFechaSolicitud() {
        return fechaSolicitud;
    }

    public void setFechaSolicitud(String fechaSolicitud) {
        this.fechaSolicitud = fechaSolicitud;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public UsuarioBeans getUsuarioRespuesta() {
        return usuarioRespuesta;
    }

    public void setUsuarioRespuesta(UsuarioBeans usuarioRespuesta) {
        this.usuarioRespuesta = usuarioRespuesta;
    }

    public String getFechaRespuesta() {
        return fechaRespuesta;
    }

    public void setFechaRespuesta(String fechaRespuesta) {
        this.fechaRespuesta = fechaRespuesta;
    }

    public String getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
    }
}
