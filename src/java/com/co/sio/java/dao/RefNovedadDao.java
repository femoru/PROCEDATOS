/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.co.sio.java.dao;

import com.co.sio.java.db.ControllerPool;
import com.co.sio.java.mbeans.RefNovedadBeans;
import java.sql.ResultSet;

/**
 *
 * @author fmoctezuma
 */
public class RefNovedadDao {

    private ControllerPool BD;

    public RefNovedadDao() {
        BD = new ControllerPool();
    }

    public boolean Guardar(RefNovedadBeans beans) throws Exception {
        try {
            String sql = "INSERT INTO rnovedades "
                    + "            (desnovedad, afectabasico, afectaauxilio, pagar, afectaneto, "
                    + "             aplicaaseguradora, tiponovedad) "
                    + "     VALUES (?, ?, ?, ?, ?, ?, ?)";
            BD.conectar();
            BD.callableStatement(sql);
            BD.AsignarParametro(1, beans.getDesNovedad().toUpperCase(), 1);
            BD.AsignarParametro(2, Integer.toString(beans.getAfectaBasico()), 2);
            BD.AsignarParametro(3, Integer.toString(beans.getAfectaAuxilio()), 2);
            BD.AsignarParametro(4, Integer.toString(beans.getPagar()), 2);
            BD.AsignarParametro(5, Integer.toString(beans.getAfectaNeto()), 2);
            BD.AsignarParametro(6, Integer.toString(beans.getAplicaAseguradora()), 2);
            BD.AsignarParametro(7, Integer.toString(beans.getTipoNovedad()), 2);

            return BD.registrar();
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        } finally {
            BD.desconectar();
        }
    }

    public boolean actualizar(RefNovedadBeans beans) throws Exception {
        try {
            String sql = "UPDATE rnovedades "
                    + "   SET desnovedad = ?, "
                    + "       afectabasico = ?, "
                    + "       afectaauxilio = ?, "
                    + "       pagar = ?, "
                    + "       afectaneto = ?, "
                    + "       aplicaaseguradora = ?, "
                    + "       tiponovedad = ? "
                    + " WHERE codnovedad = ?";
            BD.conectar();
            BD.callableStatement(sql);
            BD.AsignarParametro(1, beans.getDesNovedad().toUpperCase(), 1);
            BD.AsignarParametro(2, Integer.toString(beans.getAfectaBasico()), 2);
            BD.AsignarParametro(3, Integer.toString(beans.getAfectaAuxilio()), 2);
            BD.AsignarParametro(4, Integer.toString(beans.getPagar()), 2);
            BD.AsignarParametro(5, Integer.toString(beans.getAfectaNeto()), 2);
            BD.AsignarParametro(6, Integer.toString(beans.getAplicaAseguradora()), 2);
            BD.AsignarParametro(7, Integer.toString(beans.getTipoNovedad()), 2);
            BD.AsignarParametro(8, Integer.toString(beans.getCodNovedad()), 2);

            return BD.registrar();
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        } finally {
            BD.desconectar();
        }
    }

    public RefNovedadBeans consultar(int codNovedad) throws Exception {
        try {
            String sql = "SELECT   codnovedad, desnovedad, afectabasico, afectaauxilio, pagar, "
                    + "         afectaneto, aplicaaseguradora, tiponovedad "
                    + "         FROM rnovedades "
                    + "         WHERE codnovedad = ? ";
            BD.conectar();
            BD.callableStatement(sql);
            BD.AsignarParametro(1, Integer.toString(codNovedad), 2);

            BD.consultar();
            ResultSet datoSql = BD.obtenerConsulta();
            RefNovedadBeans beans = new RefNovedadBeans();

            if (datoSql.next()) {
                beans.setCodNovedad(datoSql.getInt(1));
                beans.setDesNovedad(datoSql.getString(2));
                beans.setAfectaBasico(datoSql.getInt(3));
                beans.setAfectaAuxilio(datoSql.getInt(4));
                beans.setPagar(datoSql.getInt(5));
                beans.setAfectaNeto(datoSql.getInt(6));
                beans.setAplicaAseguradora(datoSql.getInt(7));
                beans.setTipoNovedad(datoSql.getInt(8));
            }

            return beans;
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        } finally {
            BD.desconectar();
        }



    }

    public String getListRefNovedades(int page, int rows) throws Exception {
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

            sql = "SELECT COUNT(*) AS valor FROM rnovedades";

            BD.conectar();
            BD.callableStatement(sql);
            BD.consultar();

            rsCuenta = BD.obtenerConsulta();

            if (rsCuenta.next()) {
                total = Integer.parseInt(rsCuenta.getString(1));
                total1 = total;
            }


            if (total > 0) {
                double d = Math.ceil((double) (total) / (double) (rows));
                total_pages = (int) (d);
            } else {
                total_pages = 0;
            }

            strQuery = "SELECT   codnovedad, desnovedad, tiponovedad , afectabasico, afectaauxilio, pagar, "
                    + "         afectaneto, aplicaaseguradora "
                    + "  FROM rnovedades "
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
                json = json + "\"id\":\"" + datoSql.getInt(1) + "\",";
                json = json + "\"cell\":[" + datoSql.getInt(1) + "";
                json = json + ",\"" + datoSql.getString(2) + "\"";
                json = json + ",\"" + datoSql.getString(3) + "\"";
                json = json + ",\"" + datoSql.getString(4) + "\"";
                json = json + ",\"" + datoSql.getString(5) + "\"";
                json = json + ",\"" + datoSql.getString(6) + "\"";
                json = json + ",\"" + datoSql.getString(7) + "\"";
                json = json + ",\"" + datoSql.getString(8) + "\"]";
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
}
