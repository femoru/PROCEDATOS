/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.co.sio.java.mbeans;

import java.util.ResourceBundle;

/**
 *
 * @author jcarvajal
 */
public class ConfiguracionBeans {

    private static String sede;
    private static String usuarioWeb;
    private static String smtp;
    private static String remitente;
    private static String usuarioMail;
    private static String claveMail;
    private static String tipoMail;
    private static int CantidadCitasPaciente;
    private static String MotivoCancelacion;

    public ConfiguracionBeans() {
        ResourceBundle rb = ResourceBundle.getBundle("com.co.sio.java.utils.general");
        sede = rb.getString("sede");
        usuarioWeb = rb.getString("usuarioWeb");
        smtp = rb.getString("smtp");
        remitente = rb.getString("remitente");
        usuarioMail = rb.getString("usuarioMail");
        claveMail = rb.getString("claveMail");
        tipoMail = rb.getString("tipoMail");
        CantidadCitasPaciente = Integer.parseInt(rb.getString("CantidadCitasPaciente"));
        MotivoCancelacion = rb.getString("MotivoCancelacion");
    }

    public static String getMotivoCancelacion() {
        return MotivoCancelacion;
    }

    public static void setMotivoCancelacion(String MotivoCancelacion) {
        ConfiguracionBeans.MotivoCancelacion = MotivoCancelacion;
    }

    public static int getCantidadCitasPaciente() {
        return CantidadCitasPaciente;
    }

    public static void setCantidadCitasPaciente(int CantidadCitasPaciente) {
        ConfiguracionBeans.CantidadCitasPaciente = CantidadCitasPaciente;
    }

    public static String getTipoMail() {
        return tipoMail;
    }

    public static String getClaveMail() {
        return claveMail;
    }

    public static String getUsuarioMail() {
        return usuarioMail;
    }

    public static String getRemitente() {
        return remitente;
    }

    public static String getSmtp() {
        return smtp;
    }

    public static String getUsuarioWeb() {
        return usuarioWeb;
    }

    public static String getSede() {
        return sede;
    }
}

/*
 * private static String driver = "oracle.jdbc.driver.OracleDriver";
 *
 * private static String url = "jdbc:oracle:thin:@192.168.0.86:1521:orcl";
 * private static String usuario = "SALUD"; 
 * private static String clave = "salud";      
 * 
 * private static String url = "jdbc:oracle:thin:@10.40.61.120:1521:PRUEBA";
 * private static String usuario = "salud"; 
 * private static String clave = "salud";

 public static String getClave() {
 return clave;
 }

 public static String getDriver() {
 return driver;
 }

 public static String getUrl() {
 return url;
 }

 public static String getUsuario() {
 return usuario;
 }*/
