/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.co.sio.java.dao;

import com.co.sio.java.JSON.JSONException;
import com.co.sio.java.JSON.JSONObject;
import com.co.sio.java.db.ControllerPool;
import com.co.sio.java.mbeans.ArchivoPlanoBeans;
import java.sql.ResultSet;
import java.sql.SQLException;
/**
 *
 * @author fmoctezuma
 */
public class ArchivosPlanosDao {

    private ControllerPool BD;

    public ArchivosPlanosDao() {
        BD = new ControllerPool();
    }

    public boolean Guardar(ArchivoPlanoBeans planoBeans) throws Exception {
        ArchivoPlanoBeans temp = consultar(planoBeans.getIdCliente(), planoBeans.getIdLaborContrato(), planoBeans.getInicialCarga(), planoBeans.getFinalCarga());
        if (temp.getIdArchivo() == 0) {
            return insertar(planoBeans);
        } else {
            return true;
        }
    }

    public ArchivoPlanoBeans consultar(int idcliente, int idlabor, String inicio, String fin) throws Exception {
        try {
            String sql = "SELECT idarchivoplano, idcliente, idlaborcontrato, to_char(fechainicialcarga,'dd/mm/yyyy') fechainicialcarga, "
                    + "       to_char(fechafinalcarga,'dd/mm/yyyy') fechafinalcarga, fecha, totalregistros, idusuario "
                    + "  FROM harchivosplanos "
                    + " WHERE idcliente = ? "
                    + "   AND idlaborcontrato = ? "
                    + "   AND fechainicialcarga = TO_DATE (?, 'dd/mm/yyyy') "
                    + "   AND fechafinalcarga = TO_DATE (?, 'dd/mm/yyyy')";
            BD.conectar();
            BD.callableStatement(sql);
            BD.AsignarParametro(1, Integer.toString(idcliente), 2);
            BD.AsignarParametro(2, Integer.toString(idlabor), 2);
            BD.AsignarParametro(3, inicio, 1);
            BD.AsignarParametro(4, fin, 1);

            BD.consultar();

            ResultSet datoSql = BD.obtenerConsulta();
            ArchivoPlanoBeans planoBeans = new ArchivoPlanoBeans();
            if (datoSql.next()) {
                planoBeans.setIdArchivo(datoSql.getInt(1));
                planoBeans.setIdCliente(datoSql.getInt(2));
                planoBeans.setIdLaborContrato(datoSql.getInt(3));
                planoBeans.setInicialCarga(datoSql.getString(4));
                planoBeans.setFinalCarga(datoSql.getString(5));
                planoBeans.setFechaCarga(datoSql.getString(6));
                planoBeans.setTotalRegistros(datoSql.getInt(7));
                planoBeans.setIdUsuario(datoSql.getInt(8));
            }

            return planoBeans;
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        } finally {
            BD.desconectar();
        }
    }

    public boolean insertar(ArchivoPlanoBeans planoBeans) throws Exception {
        try {
            String sql = "INSERT INTO harchivosplanos (idcliente, idlaborcontrato, "
                    + "fechainicialcarga, fechafinalcarga,idusuario,totalregistros) "
                    + "     VALUES (?, ?, to_date(?,'dd/mm/yyyy'), "
                    + "     to_date(?,'dd/mm/yyyy'), ?, 0)";
            BD.conectar();
            BD.callableStatement(sql);
            BD.AsignarParametro(1, Integer.toString(planoBeans.getIdCliente()), 2);
            BD.AsignarParametro(2, Integer.toString(planoBeans.getIdLaborContrato()), 2);
            BD.AsignarParametro(3, planoBeans.getInicialCarga(), 1);
            BD.AsignarParametro(4, planoBeans.getFinalCarga(), 1);
            BD.AsignarParametro(5, Integer.toString(planoBeans.getIdUsuario()), 2);

            return BD.registrar();


        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        } finally {
            BD.desconectar();
        }
    }

    public boolean actualizar(ArchivoPlanoBeans planoBeans) throws Exception {

        try {
            String sql = "UPDATE harchivosplanos "
                    + "   SET idcliente = ?, "
                    + "       idlaborcontrato = ?, "
                    + "       fechainicialcarga = to_date(?,'dd/mm/yyyy'), "
                    + "       fechafinalcarga = to_date(?,'dd/mm/yyyy'), "
                    + "       fecha = sysdate, "
                    + "       totalregistros = ?, "
                    + "       idusuario = ? "
                    + " WHERE idarchivoplano = ? ";

            BD.conectar();
            BD.callableStatement(sql);
            BD.AsignarParametro(1, Integer.toString(planoBeans.getIdCliente()), 2);
            BD.AsignarParametro(2, Integer.toString(planoBeans.getIdLaborContrato()), 2);
            BD.AsignarParametro(3, planoBeans.getInicialCarga(), 1);
            BD.AsignarParametro(4, planoBeans.getFinalCarga(), 1);
            BD.AsignarParametro(5, Integer.toString(planoBeans.getTotalRegistros()), 2);
            BD.AsignarParametro(6, Integer.toString(planoBeans.getIdUsuario()), 2);
            BD.AsignarParametro(7, Integer.toString(planoBeans.getIdArchivo()), 2);

            return BD.registrar();
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        } finally {
            BD.desconectar();
        }
    }
    
    public JSONObject consultarHora(){
        try {
            String sql = "SELECT TO_CHAR(SYSDATE,'dd/mm/yyyy') fecha,TO_CHAR(SYSDATE,'hh24:mi:ss') hora from DUAL";
                BD.conectar();
                BD.callableStatement(sql);
                BD.consultar();
                ResultSet datoSql = BD.obtenerConsulta();
                JSONObject json = new JSONObject();
                if (datoSql.next()) {
                    json.put("fecha", datoSql.getString("fecha"));
                    json.put("hora", datoSql.getString("hora"));
                }
                return json; 
        } catch (SQLException ex) {
            return null;
        } catch (JSONException ex) {
            return null;
        } finally {
            BD.desconectar();
        }
    } 
}
