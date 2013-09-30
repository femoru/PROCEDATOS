/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.co.sio.java.dao;

import com.co.sio.java.db.ControllerPool;
import com.co.sio.java.mbeans.ReferenciasBeans;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author fmoctezuma
 */
public class ReferenciasDao {

    private ControllerPool BD;

    public ReferenciasDao() {
        BD = new ControllerPool();
    }

    public String getListReferencias(int page, int rows, String sidx, String sord) throws Exception {
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
            sql = "SELECT COUNT(*) AS valor FROM rreferencias";

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
            strQuery = "SELECT idreferencia, desreferencia, nomtabla "
                    + "FROM rreferencias "
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
                json = json + "\"id\":\"" + datoSql.getInt("idreferencia") + "\",";
                json = json + "\"cell\":[" + datoSql.getInt("idreferencia") + "";
                json = json + ",\"" + datoSql.getString("desreferencia") + "\"";
                json = json + ",\"" + datoSql.getString("nomtabla") + "\"]";
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

    public String getListadoReferencias(int page, int rows, String sidx, String sord, String nomtabla) throws Exception {

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
            sql = "SELECT COUNT(*) AS valor FROM " + nomtabla;
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
            strQuery = "SELECT * "
                    + "FROM " + nomtabla
                    + " ORDER BY 1 ";
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
                json = json + "\"id\":\"" + datoSql.getString(1) + "\",";
                json = json + "\"cell\":[" + datoSql.getString(1) + "";
                json = json + ",\"" + datoSql.getString(2) + "\"]";
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

    public List listar(String nomTabla) throws Exception {
        ReferenciasBeans referenciasBeans;
        List estadoList = new ArrayList();
        try {
            ResultSet datoSql;
            String sql = "SELECT * "
                    + "FROM " + nomTabla
                    + " ORDER BY 1";
            BD.conectar();
            BD.callableStatement(sql);
            BD.consultar();

            datoSql = BD.obtenerConsulta();

            while (datoSql.next()) {
                referenciasBeans = new ReferenciasBeans();
                referenciasBeans.setCodigo(datoSql.getInt(1));
                referenciasBeans.setDescripcion(datoSql.getString(2));
                referenciasBeans.setNombreTabla(nomTabla);
                estadoList.add(referenciasBeans);
            }
            return estadoList;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            BD.desconectar();
        }        
    }

    public boolean actualizar(ReferenciasBeans referenciasBeans) throws Exception {
        try {
            ResultSet datoSql;
            String[] nombreCol = new String[3];
            String sqlTableName = "SELECT column_id,column_name "
                    + "FROM user_tab_columns "
                    + "WHERE table_name = ? "
                    + "ORDER BY 1";
            BD.conectar();
            BD.callableStatement(sqlTableName);
            BD.AsignarParametro(1, referenciasBeans.getNombreTabla(), 1);
            if (!BD.consultar()) {
                throw new Exception("Error Realizando la consulta sql " + sqlTableName);
            } else {
                datoSql = BD.obtenerConsulta();
                while (datoSql.next()) {
                    nombreCol[datoSql.getInt(1)] = datoSql.getString(2);
                }
            }

            String sql = "UPDATE " + referenciasBeans.getNombreTabla()
                    + " SET " + nombreCol[2] + " = ? "
                    + " WHERE " + nombreCol[1] + " = ?";
            BD.callableStatement(sql);
            BD.AsignarParametro(1, referenciasBeans.getDescripcion().toUpperCase(), 1);
            BD.AsignarParametro(2, Integer.toString(referenciasBeans.getCodigo()), 2);

            return BD.registrar();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            BD.desconectar();
        }
    }
    
    public boolean Guardar(ReferenciasBeans referenciasBeans) throws Exception {
        try {
            String nombreCol = "";
            String sqlTableName = "SELECT column_id,column_name "
                    + "FROM user_tab_columns "
                    + "WHERE table_name = ? "
                    + "ORDER BY 1";
            BD.conectar();
            BD.callableStatement(sqlTableName);
            BD.AsignarParametro(1, referenciasBeans.getNombreTabla(), 1);
            BD.consultar();

            ResultSet datoSql = BD.obtenerConsulta();
            while (datoSql.next()) {
                nombreCol = datoSql.getString(2);
            }

            String sql = "INSERT INTO " + referenciasBeans.getNombreTabla()
                    + "("+ nombreCol.toUpperCase() + ") VALUES (?)";

            BD.callableStatement(sql);
            BD.AsignarParametro(1, referenciasBeans.getDescripcion(), 1);

            return BD.registrar();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            BD.desconectar();
        }
    }
    
    public String listarArbolReferencias(String nomtabla) throws Exception {
        String json = "";
        try {
            String sql = "SELECT * "
                    + "FROM " + nomtabla
                    + " ORDER by 1";
            BD.conectar();
            BD.callableStatement(sql);
            BD.consultar();

            ResultSet datoSql = BD.obtenerConsulta();

            json += "[";
            boolean rc = false;
            while (datoSql.next()) {
                if (rc) {
                    json += ",";
                }

                json += "{\"title\": \"" + datoSql.getString(2) + "\" ,\"key\": \"" + datoSql.getInt(1) + "\" }";

                rc = true;
            }
            json += "]";

            return json;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            BD.desconectar();
        }
    }    

    public ReferenciasBeans consultar(int codRef, String tabla) throws Exception {
        try {

            ReferenciasBeans ref = null;
            String nombreCol;
            String sqlTableName = "SELECT column_id,column_name "
                    + "FROM user_tab_columns "
                    + "WHERE table_name = ? "
                    + "ORDER BY 1";
            BD.conectar();
            BD.callableStatement(sqlTableName);
            BD.AsignarParametro(1, tabla, 1);
            BD.consultar();
            ResultSet datoSql = BD.obtenerConsulta();
            if (datoSql.next()) {
                nombreCol = datoSql.getString("column_name");


                String sql = "SELECT * "
                        + "FROM " + tabla
                        + " WHERE " + nombreCol + " = ?";
                BD.callableStatement(sql);
                BD.AsignarParametro(1, Integer.toString(codRef), 2);
                BD.consultar();
                datoSql = BD.obtenerConsulta();
                if(datoSql.next()){
                    ref =new ReferenciasBeans();
                    ref.setCodigo(codRef);
                    ref.setDescripcion(datoSql.getString(2));
                    ref.setNombreTabla(tabla); 
                }
                return ref; 
            }
            return null;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            BD.desconectar();
        }
    }
}
