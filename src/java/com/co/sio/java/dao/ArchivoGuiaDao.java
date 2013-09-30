/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.co.sio.java.dao;

import com.co.sio.java.db.ControllerPool;
import com.co.sio.java.mbeans.ArchivoGuiaBeans;
import com.co.sio.java.mbeans.ArchivoPlanoBeans;
import com.co.sio.java.mbeans.ContratoBeans;
import java.sql.ResultSet;
import java.util.TreeMap;

/**
 *
 * @author fmoctezuma
 */
public class ArchivoGuiaDao {

    private ControllerPool BD;

    public ArchivoGuiaDao() {
        BD = new ControllerPool();
    }

    public boolean Guardar(ArchivoGuiaBeans guiaBeans) throws Exception {
        try {
            String sql = "INSERT INTO darchivosguias "
                    + "        (idarchivoplano, guia, registros, "
                    + "         fecharegistro, usuariosos) "
                    + "     VALUES (?, ?, ?, TO_DATE (?, 'dd/mm/yyyy HH24:mi'), ?)";
            BD.conectar();
            BD.callableStatement(sql);
            BD.AsignarParametro(1, Integer.toString(guiaBeans.getIdArchivoPlano()), 2);
            BD.AsignarParametro(2, Integer.toString(guiaBeans.getGuia()), 2);
            BD.AsignarParametro(3, Integer.toString(guiaBeans.getRegistros()), 2);
            BD.AsignarParametro(4, guiaBeans.getFechaRegistro(), 1);
            BD.AsignarParametro(5, guiaBeans.getUsuarioSOS(), 1);

            return BD.registrar();
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        } finally {
            BD.desconectar();
        }


    }

    public boolean eliminarGuias(int idarchivoplano) throws Exception {
        try {
            BD.conectar();
            String sql = "DELETE FROM darchivosguias "
                    + "      WHERE idarchivoplano = ?";
            BD.callableStatement(sql);
            BD.AsignarParametro(1, Integer.toString(idarchivoplano), 2);

            return BD.registrar();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            BD.desconectar();
        }
    }

    public String getListGuias(int page, int rows, String sidx, String sord, int idcliente, int idlabor, String fechaInicio, String fechaFin) throws Exception {
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

            sql = "SELECT   COUNT (COUNT (*)) "
                    + "    FROM mregistros "
                    + "   WHERE fechainicio BETWEEN TO_DATE (?, 'dd/mm/yyyy') "
                    + "                         AND TO_DATE (?, 'dd/mm/yyyy')+1 "
                    + "     AND idlaborcontrato = ? "
                    + "     AND estado = 1 "
                    + "GROUP BY idusuario";

            BD.conectar();
            BD.callableStatement(sql);
            BD.AsignarParametro(1, fechaInicio, 1);
            BD.AsignarParametro(2, fechaFin, 1);
            BD.AsignarParametro(3, Integer.toString(idlabor), 2);
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

            strQuery = "SELECT   mr.idusuario,pnombre||' '||papellido, cu.usuariosos, "
                    + "         SUM (mr.registroslabor) AS registrosingresos "
                    + "    FROM mregistros mr  "
                    + "        INNER JOIN mpersonas ON idpersona = mr.idusuario "
                    + "        INNER JOIN cusuarios cu ON cu.idusuario = mr.idusuario "
                    + "   WHERE mr.fechainicio BETWEEN TO_DATE (?, 'dd/mm/yyyy') "
                    + "                            AND TO_DATE (?, 'dd/mm/yyyy')+1 "
                    + "     AND idlaborcontrato = ? "
                    + "     AND estado = 1 "
                    + "GROUP BY mr.idusuario, cu.usuariosos,pnombre,papellido "
                    + "ORDER BY 1";
            BD.callableStatement(strQuery);
            BD.AsignarParametro(1, fechaInicio, 1);
            BD.AsignarParametro(2, fechaFin, 1);
            BD.AsignarParametro(3, Integer.toString(idlabor), 2);
            BD.consultar();
            datoSql = BD.obtenerConsulta();

            ContratoBeans contrato = new ContratosDao().consultar(idcliente);

            ArchivoPlanoBeans beans = new ArchivosPlanosDao().consultar(contrato.getCliente().getIdcliente(), idlabor, fechaInicio, fechaFin);


            String sql2 = "SELECT   usuariosos, SUM (registros) AS registros "
                    + "    FROM darchivosguias "
                    + "   WHERE idarchivoplano = ? "
                    + "GROUP BY usuariosos";
            BD.callableStatement(sql2);
            BD.AsignarParametro(1, Integer.toString(beans.getIdArchivo()), 2);
            BD.consultar();
            ResultSet datoSql2 = BD.obtenerConsulta();
            TreeMap usuariosSOS = new TreeMap();

            while (datoSql2.next()) {
                usuariosSOS.put(datoSql2.getString(1), datoSql2.getInt(2));
            }

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
                json = json + ",\"" + datoSql.getString(4) + "\"";
                json = json + ",\"" + usuariosSOS.get(datoSql.getString(3)) + "\"]";

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

    public String getDetalladoRegistros(int id, int idcliente, int idlabor, String fechaInicio, String fechaFin, int page, int rows) throws Exception {

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
            sql = "SELECT  COUNT (SUM (registroslabor))  "
                    + "    FROM mregistros "
                    + "   WHERE idusuario = ? "
                    + "     AND idlaborcontrato = ? "
                    + "     AND fechainicio BETWEEN TO_DATE (?, 'dd/mm/yyyy') "
                    + "                         AND TO_DATE (?, 'dd/mm/yyyy')+1 "
                    + "     AND estado = 1 "
                    + "GROUP BY  datolabor "
                    + "ORDER BY 1";

            BD.conectar();
            BD.callableStatement(sql);
            BD.AsignarParametro(1, Integer.toString(id), 2);
            BD.AsignarParametro(2, Integer.toString(idlabor), 2);
            BD.AsignarParametro(3, fechaInicio, 1);
            BD.AsignarParametro(4, fechaFin, 1);
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

            total = total1;
            strQuery = "SELECT  datolabor, SUM (registroslabor) "
                    + "    FROM mregistros "
                    + "   WHERE idusuario = ? "
                    + "     AND idlaborcontrato = ? "
                    + "     AND estado = 1 "
                    + "     AND fechainicio BETWEEN TO_DATE (?, 'dd/mm/yyyy') "
                    + "                         AND TO_DATE (?, 'dd/mm/yyyy')+1 "
                    + "     AND estado = 1 "
                    + "GROUP BY  datolabor "
                    + "ORDER BY 1";


            BD.callableStatement(strQuery);
            BD.AsignarParametro(1, Integer.toString(id), 2);
            BD.AsignarParametro(2, Integer.toString(idlabor), 2);
            BD.AsignarParametro(3, fechaInicio, 1);
            BD.AsignarParametro(4, fechaFin, 1);

            BD.consultar();
            datoSql = BD.obtenerConsulta();


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
                json = json + "\"cell\":[\"" + datoSql.getString(1) + "\"";
                json = json + ",\"" + (datoSql.getString(2)) + "\"]";

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

    public String getListRegistros(int auxiliar, int labor, String guia, String fechaInicio, String fechaFin) throws Exception {

        String strQuery;
        String json;

        boolean rc;

        ResultSet datoSql;

        try {

            strQuery = "SELECT   idregistro, TO_CHAR (fechainicio, 'dd/mm/yyyy hh24:mi') fechainicio, "
                    + "         registroslabor "
                    + "    FROM mregistros "
                    + "   WHERE idusuario = ? "
                    + "     AND idlaborcontrato = ? "
                    + "     AND datolabor = ? "
                    + "     AND fechainicio BETWEEN TO_DATE (?, 'dd/mm/yyyy') "
                    + "                         AND TO_DATE (?, 'dd/mm/yyyy')+1 "
                    + "     AND estado = 1 "
                    + "ORDER BY 1";


            BD.conectar();
            BD.callableStatement(strQuery);
            BD.AsignarParametro(1, Integer.toString(auxiliar), 2);
            BD.AsignarParametro(2, Integer.toString(labor), 2);
            BD.AsignarParametro(3, guia, 1);
            BD.AsignarParametro(4, fechaInicio, 1);
            BD.AsignarParametro(5, fechaFin, 1);

            BD.consultar();
            datoSql = BD.obtenerConsulta();


            json = "";
            json = json + "{\n";
            json = json + "\"rows\": [";
            rc = false;

            while (datoSql.next()) {
                if (rc) {
                    json = json + ",";
                }
                json = json + "\n{";
                json = json + "\"id\":\"" + datoSql.getString(1) + "\",";
                json = json + "\"cell\":[\"" + datoSql.getString(1) + "\"";
                json = json + ",\"" + datoSql.getString(2) + "\"";
                json = json + ",\"" + (datoSql.getString(3)) + "\"]";

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

    public boolean actualizarRegistro(int idregistro, int registros) throws Exception {
        try {
            String sql = "UPDATE mregistros "
                    + "   SET registroslabor = ? "
                    + " WHERE idregistro = ?";
            BD.conectar();
            BD.callableStatement(sql);
            BD.AsignarParametro(1, Integer.toString(registros), 2);
            BD.AsignarParametro(2, Integer.toString(idregistro), 2);

            return BD.registrar();
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        } finally {
            BD.desconectar();
        }
    }

    public boolean conciliarRegistros(int idlaborcontrato, String fechainicio, String fechafin) throws Exception {
        try {
            String sql = "UPDATE mregistros "
                    + "   SET estado = 2 "
                    + " WHERE idlaborcontrato = ? "
                    + "   AND fechainicio BETWEEN TO_DATE (?, 'dd/mm/yyyy') "
                    + "                       AND TO_DATE (?, 'dd/mm/yyyy')+1 "
                    + "   AND estado = 1";

            BD.conectar();
            BD.callableStatement(sql);
            BD.AsignarParametro(1, Integer.toString(idlaborcontrato), 2);
            BD.AsignarParametro(2, fechainicio, 1);
            BD.AsignarParametro(3, fechafin, 1);

            return BD.registrar();
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        } finally {
            BD.desconectar();
        }

    }

    public boolean validarEncabezado(int idlaborcontrato, String fechaInicio, String fechaFin) throws Exception {
        try {
            BD.conectar();

            String sql = "SELECT   idregistro, TO_CHAR (fechainicio, 'dd/mm/yyyy hh24:mi') fechainicio, "
                    + "         registroslabor "
                    + "    FROM mregistros "
                    + "   WHERE idlaborcontrato = ? "
                    + "     AND fechainicio BETWEEN TO_DATE (?, 'dd/mm/yyyy') "
                    + "                         AND TO_DATE (?, 'dd/mm/yyyy')+1 "
                    + "     AND estado = 1 "
                    + "     AND ROWNUM <=1 ";

            BD.callableStatement(sql);
            BD.AsignarParametro(1, Integer.toString(idlaborcontrato), 2);
            BD.AsignarParametro(2, fechaInicio, 1);
            BD.AsignarParametro(3, fechaFin, 1);
            BD.consultar();

            ResultSet datoSql = BD.obtenerConsulta();

            if (datoSql.next()) {
                return true;
            }


            return false;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            BD.desconectar();
        }
    }
}
