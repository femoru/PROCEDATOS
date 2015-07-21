package com.co.sio.java.db;

import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.apache.tomcat.dbcp.dbcp.BasicDataSource;

/**
 *
 * @author jcarvajal
 */
public class ControllerPool {

    private Connection con;
    private CallableStatement st;
    private ResultSet rs;
    private String myError = "";
    private static final Logger logger = Logger.getLogger("com.co.sio.java.db");
    private static BasicDataSource ds;
    private SQLException exception;
    private boolean oldStateOfAutoCommit = false;
    private final int oldStateOfTransactionIsolation = Connection.TRANSACTION_READ_COMMITTED;

    static {//Genera el pool de conexiones solo una vez
        ds = null;
        try {
            Context ctx = new InitialContext();
            ds = (BasicDataSource) ctx.lookup("java:comp/env/jdbc/myoracle");
            ctx.close();
        } catch (NamingException ex) {
            logger.log(Level.SEVERE, "loginAction", ex);
        }
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = (SQLException) exception;
    }

    public String getError() {
        return this.myError;
    }

    public ResultSet getResultSet() {
        return this.rs;
    }

    public static BasicDataSource getDs() {
        return ds;
    }

//carga el pool de conexiones
    public boolean conectar() {

        try {

            if (ds == null) {
                this.myError = "No data source";
                return false;
            }


            con = ds.getConnection();

            if (con == null) {
                this.myError = "No connection";
                return false;
            }

            return true;

        } catch (Exception ex) {

            this.myError = "Exception " + ex.getMessage();
            logger.log(Level.SEVERE, "loginAction", ex);
            return false;

        }

    }//conectarPool()

    public Connection coneccion() {
        return con;
    }

    public void desconectar() {
        try {
            if (con != null) {
                con.close();
            }
            if (st != null) {
                st.close();
            }

        } catch (SQLException e) {
            System.out.println(e.toString());
            Logger
                    .getLogger(ControllerPool.class
                    .getName()).log(Level.SEVERE, null, e);
            this.exception = e;
        } catch (Exception e) {
            System.out.println(e.toString());
            Logger
                    .getLogger(ControllerPool.class
                    .getName()).log(Level.SEVERE, null, e);
        }
    }

    public boolean consultar() {
        boolean correcto;
        try {
            this.rs = st.executeQuery();
            correcto = true;
        } catch (SQLException ex) {
            this.myError = "SQLException " + ex.getMessage();
            logger.log(Level.SEVERE, "loginAction", ex);
            return false;
        } catch (Exception ex) {
            this.myError = "Exception " + ex.getMessage();
            logger.log(Level.SEVERE, "loginAction", ex);
            return false;
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
            Logger
                    .getLogger(ControllerPool.class
                    .getName()).log(Level.SEVERE, null, e);
            this.exception = e;
        }
        return correcto;

    }

    public void abrirTransaccion() {

        try {
            if (this.con.getAutoCommit()) {
                this.con.setAutoCommit(false);
                this.oldStateOfAutoCommit = true;
            }
        } catch (SQLException e) {
            System.out.println(e);
            Logger
                    .getLogger(ControllerPool.class
                    .getName()).log(Level.SEVERE, null, e);
            this.exception = e;
        }
    }

    // hace el commit
    public void exitoTransaccion() {

        try {
            this.con.commit();
        } catch (SQLException e) {
            System.out.println(e);
            Logger
                    .getLogger(ControllerPool.class
                    .getName()).log(Level.SEVERE, null, e);
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
            Logger
                    .getLogger(ControllerPool.class
                    .getName()).log(Level.SEVERE, null, e);
            this.exception = e;
        }
    }

    // hace el rollback.
    public void fallaTransaccion() {

        try {
            this.con.rollback();
        } catch (SQLException e) {
            System.out.println(e);
            Logger
                    .getLogger(ControllerPool.class
                    .getName()).log(Level.SEVERE, null, e);
            this.exception = e;
        }
    }

    public void callableStatement(String Sql) throws SQLException {
        this.st = this.con.prepareCall(Sql);
    }

    /**
     * Funcion para la asignacion de parametros de un sql
     *
     * @param parametro posicion del parametro dentro del SQL
     * @param valor valor del parametro
     * @param tipo identifica el tipo de parametro a asignar
     * <br/> 1 String
     * <br/> 2 int
     * <br/> 3 Date formato "yyyy-MM-dd"
     * <br/> 4 Date formato "dd/MM/yyyy"
     * <br/> 5 double
     * @throws java.sql.SQLException
     * @throws java.text.ParseException
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
            case 5:
                this.st.setDouble(parametro, Double.parseDouble(valor));
                break;
        }
    }

    public boolean registrar() {

        boolean correcto = false;

        try {
            st.execute();
            correcto = true;

        } catch (SQLException e) {

            System.out.println(e);
            Logger
                    .getLogger(ControllerPool.class
                    .getName()).log(Level.SEVERE, null, e);
            this.exception = e;
        }
        return correcto;
    }
}
