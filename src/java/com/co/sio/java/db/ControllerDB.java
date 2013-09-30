/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.co.sio.java.db;

import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author rquintero
 */
public class ControllerDB {

    private Exception exception = null;
    private String driver;
    private String url;
    private String usuario;
    private String clave;
    private Connection con = null;
    private CallableStatement st = null;
    private ResultSet rs = null;
    private boolean oldStateOfAutoCommit = false;
    private int oldStateOfTransactionIsolation = Connection.TRANSACTION_READ_COMMITTED;

    public ControllerDB() {
        this.driver = "oracle.jdbc.driver.OracleDriver";
        this.url = "jdbc:oracle:thin:@192.168.0.122:1521:SIOHS";
        this.usuario = "PROCEDATOS";
        this.clave = "PROCEDATOS";

        this.con = null;
        this.st = null;
        this.rs = null;
        this.exception = null;
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    public void conectar() {

        try {

            Class.forName(driver);


        } catch (ClassNotFoundException e) {
            String res = "Error en el Driver";
            System.out.println(res);

            Logger.getLogger(ControllerDB.class.getName()).log(Level.SEVERE, null, res);
        }

        try {
            this.con = DriverManager.getConnection(url, usuario, clave);

        } catch (Exception e) {

            System.out.println(e.toString());
            Logger.getLogger(ControllerDB.class.getName()).log(Level.SEVERE, null, e);
            this.exception = e;
        }
    }

    public Connection coneccion() {

        return con;
    }

    public void desconectar() {

        try {

            con.close();
        } catch (SQLException e) {

            System.out.println(e.toString());
            Logger.getLogger(ControllerDB.class.getName()).log(Level.SEVERE, null, e);
            this.exception = e;
        }
    }

    public boolean registrar() {

        boolean correcto = false;

        try {
            st.execute();
            correcto = true;

        } catch (SQLException e) {

            System.out.println(e);
            Logger.getLogger(ControllerDB.class.getName()).log(Level.SEVERE, null, e);
            this.exception = e;
        }

        return correcto;
    }

    public boolean consultar() {

        boolean correcto = false;

        try {
            this.rs = st.executeQuery();
            correcto = true;

        } catch (SQLException e) {

            System.out.println(e.toString());
            Logger.getLogger(ControllerDB.class.getName()).log(Level.SEVERE, null, e);
            this.exception = e;
        }

        return correcto;
    }

    public ResultSet obtenerConsulta() {

        return rs;
    }

    public boolean actualizar() {

        boolean correcto = false;

        try {
            st.executeUpdate();
            correcto = true;

        } catch (SQLException e) {

            System.out.println(e);
            Logger.getLogger(ControllerDB.class.getName()).log(Level.SEVERE, null, e);
            this.exception = e;
        }

        return correcto;

    }

    // inicia la trasaccion
    public void abrirTransaccion() {

        try {
            if (this.con.getAutoCommit()) {
                this.con.setAutoCommit(false);
                this.oldStateOfAutoCommit = true;
            }
        } catch (SQLException e) {
            System.out.println(e);
            Logger.getLogger(ControllerDB.class.getName()).log(Level.SEVERE, null, e);
            this.exception = e;
        }
    }

    // hace el commit
    public void exitoTransaccion() {

        try {
            this.con.commit();
        } catch (SQLException e) {
            System.out.println(e);
            Logger.getLogger(ControllerDB.class.getName()).log(Level.SEVERE, null, e);
            this.exception = e;
        }
    }

    public void end() {

        try {

            this.con.setAutoCommit(this.oldStateOfAutoCommit);
            this.con.setTransactionIsolation(this.oldStateOfTransactionIsolation);
            this.con.close();
        } catch (SQLException e) {
            System.out.println(e);
            Logger.getLogger(ControllerDB.class.getName()).log(Level.SEVERE, null, e);
            this.exception = e;
        }
    }

    // hace el rollback.
    public void fallaTransaccion() {

        try {
            this.con.rollback();
        } catch (SQLException e) {
            System.out.println(e);
            Logger.getLogger(ControllerDB.class.getName()).log(Level.SEVERE, null, e);
            this.exception = e;
        }
    }

    public void callableStatement(String Sql) throws SQLException {
        this.st = this.con.prepareCall(Sql);
    }

    /**
     * Funcion para la asignacion de parametros de un sql
     * 
     *  @param parametro  posicion del parametro dentro del SQL
     *  @param valor  valor del parametro
     *  @param tipo  identifica el tipo de parametro a asignar
     *  @param 1  String
     *  @param 2  int
     *  @param 3  Date
     */
    public void AsignarParametro(int parametro, String valor, int tipo) throws SQLException, ParseException {

        DateFormat sdf;
        java.util.Date date;

        switch (tipo) {
            case 1:
                this.st.setString(parametro, valor);
                break;
            case 2:
                this.st.setInt(parametro, Integer.parseInt(valor));
                break;
            case 3:
                sdf = new SimpleDateFormat("yyyy-MM-dd");
                date = sdf.parse(valor);
                this.st.setDate(parametro, Date.valueOf(sdf.format(date)));
                break;
            case 4:
                sdf = new SimpleDateFormat("dd/MM/yyyy");
                date = sdf.parse(valor);
                this.st.setDate(parametro, Date.valueOf(sdf.format(date)));
                break;
        }
    }
}
