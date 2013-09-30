/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.co.sio.java.dao;

import com.co.sio.java.db.ControllerPool;
import com.co.sio.java.mbeans.ParametroBeans;
import java.sql.ResultSet;

/**
 *
 * @author fmoctezuma
 */
public class ParametroDao {

    private ControllerPool BD;

    public ParametroDao() {
        BD = new ControllerPool();
    }

    public boolean Guardar(ParametroBeans parametroBeans) throws Exception {
        try {
            String sql = "INSERT INTO pparametros(desparametro, valparametro) "
                    + "VALUES (?, ?)";
            BD.conectar();
            BD.callableStatement(sql);
            BD.AsignarParametro(1, parametroBeans.getDesparametro().toUpperCase(), 1);
            BD.AsignarParametro(2, parametroBeans.getValparametro(), 1);
            return BD.registrar();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            BD.desconectar();
        }
    }

    public boolean actualizar(ParametroBeans parametroBeans) throws Exception {
        boolean confirmar = false;
        try {
            String sql = "UPDATE pparametros "
                    + "SET desparametro = ?, "
                    + "valparametro = ? "
                    + "WHERE codparametro = ?";
            BD.conectar();
            BD.callableStatement(sql);
            BD.AsignarParametro(1, parametroBeans.getDesparametro().toUpperCase(), 1);
            BD.AsignarParametro(2, parametroBeans.getValparametro(), 1);
            BD.AsignarParametro(3, Integer.toString(parametroBeans.getIdparametro()), 2);
            confirmar = BD.registrar();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            BD.desconectar();
        }
        return confirmar;
    }

    public String getListParametros(int page, int rows, String sidx, String sord) throws Exception {
        String sql;
        String strQuery;
        String json;


        int total = 0;
        int total1 = 0;
        int total_pages;

        boolean rc;

        ResultSet rsCuenta;
        ResultSet datoSql;

        try {

            sql = "SELECT COUNT(*) AS valor FROM pparametros";

            BD.conectar();
            BD.callableStatement(sql);
            BD.consultar();

            rsCuenta = BD.obtenerConsulta();

            if (rsCuenta.next()) {
                total = Integer.parseInt(rsCuenta.getString("valor"));
                total1 = total;
            }


            if (total > 0) {
                double d = Math.ceil((double) (total) / (double) (rows));
                total_pages = (int) (d);
            } else {
                total_pages = 0;
            }

            strQuery = "SELECT codparametro, desparametro, valparametro "
                    + "FROM pparametros "
                    + "ORDER BY 1 ";
            BD.callableStatement(strQuery);

            BD.consultar();
            datoSql = BD.obtenerConsulta();
            total = total1;

            json = "";
            json = json + "{\n";
            json = json + "\"page\":" + page + ",\n";
            json = json + "\"total\":" + total_pages + ",\n";
            json = json + "\"records\":" + total + ",\n";
            json = json + "\"rows\": [";
            rc = false;

            while (datoSql.next()) {
                if (rc) {
                    json = json + ",";
                }
                json = json + "\n{";
                json = json + "\"id\":\"" + datoSql.getInt("codparametro") + "\",";
                json = json + "\"cell\":[" + datoSql.getInt("codparametro") + "";
                json = json + ",\"" + datoSql.getString("desparametro") + "\"";
                json = json + ",\"" + datoSql.getString("valparametro") + "\"]";
                json = json + "}";
                rc = true;
            }
            json = json + "]\n";
            json = json + "}";

            return json;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            BD.desconectar();
        }
    }

    public ParametroBeans consultar(int codparametro) throws Exception {
        try {
            ParametroBeans parametroBeans = new ParametroBeans();
            String sql = "SELECT codparametro, desparametro, valparametro "
                    + "FROM pparametros "
                    + "WHERE codparametro = ?"
                    + "ORDER BY 1 ";
            BD.conectar();
            BD.callableStatement(sql);
            BD.AsignarParametro(1, Integer.toString(codparametro), 2);
            BD.consultar();

            ResultSet datoSql = BD.obtenerConsulta();
            if (datoSql.next()) {
                parametroBeans.setIdparametro(datoSql.getInt(1));
                parametroBeans.setDesparametro(datoSql.getString(2));
                parametroBeans.setValparametro(datoSql.getString(3));
            }

            return parametroBeans;
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }finally {
            BD.desconectar();
        }
    }
}
