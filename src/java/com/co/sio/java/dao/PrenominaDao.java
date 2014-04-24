package com.co.sio.java.dao;

import com.co.sio.java.JSON.JSONArray;
import com.co.sio.java.JSON.JSONObject;
import com.co.sio.java.db.ControllerPool;
import com.co.sio.java.mbeans.NominaBeans;
import com.co.sio.java.utils.Utils;
import java.io.File;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 *
 * @author jcarvajal
 */
public class PrenominaDao {

    ControllerPool BD;

    public PrenominaDao() {
        BD = new ControllerPool();
    }

    public NominaBeans consultarNomina(String fechaI, String fechaF) throws Exception {
        NominaBeans nomina = null;
        String sql;
        ResultSet datoSql;
        try {
            sql = "SELECT idnomina,TO_CHAR(fechainicio,'dd/mm/yyyy'),"
                    + "TO_CHAR(fechafin,'dd/mm/yyyy'),estado "
                    + "FROM mnomina "
                    + "WHERE fechainicio = TO_DATE(?,'dd/mm/yyyy') "
                    + "AND fechafin = TO_DATE(?,'dd/mm/yyyy')";
            BD.conectar();
            BD.callableStatement(sql);
            BD.AsignarParametro(1, fechaI, 1);
            BD.AsignarParametro(2, fechaF, 1);

            if (!BD.consultar()) {
                throw new Exception("Error Realizando La Consulta: " + sql);
            } else {
                datoSql = BD.obtenerConsulta();
                if (datoSql.next()) {
                    nomina = new NominaBeans();
                    nomina.setId(datoSql.getInt(1));
                    nomina.setFechaInicio(datoSql.getString(2));
                    nomina.setFechaFin(datoSql.getString(3));
                    nomina.setEstado(datoSql.getInt(4));
                }
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            BD.desconectar();
        }
        return nomina;
    }

    public boolean consultarPorRango(String fechaI, String fechaF) throws Exception {
        boolean existe = false;
        try {
            String sql = "SELECT COUNT (*)\n"
                    + "  FROM mnomina\n"
                    + " WHERE (   TO_DATE (?, 'dd/mm/yyyy') BETWEEN fechainicio AND TRUNC (fechafin)\n"
                    + "        OR TO_DATE (?, 'dd/mm/yyyy') BETWEEN fechainicio AND TRUNC (fechafin)\n"
                    + "       )";

            BD.conectar();
            BD.callableStatement(sql);
            BD.AsignarParametro(1, fechaI, 1);
            BD.AsignarParametro(2, fechaF, 1);

            BD.consultar();

            ResultSet datoSql = BD.obtenerConsulta();

            if (datoSql.next()) {
                if (datoSql.getInt(1) != 0) {
                    existe = true;
                }
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            BD.desconectar();
        }
        return existe;
    }

    public boolean consultarDiaNomina(String fecha) throws Exception {
        boolean existe = false;
        String sql;
        ResultSet datoSql;
        try {
            sql = "SELECT COUNT(*) "
                    + "FROM mnomina "
                    + "WHERE TO_DATE( ? ,'dd/mm/yyyy') "
                    + "BETWEEN TRUNC(fechainicio) AND TRUNC(fechafin) ";
            BD.conectar();
            BD.callableStatement(sql);
            BD.AsignarParametro(1, fecha, 1);

            if (!BD.consultar()) {
                throw new Exception("Error Realizando La Consulta: " + sql);
            } else {
                datoSql = BD.obtenerConsulta();
                if (datoSql.next()) {
                    if (datoSql.getInt(1) != 0) {
                        existe = true;
                    }
                }
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            BD.desconectar();
        }
        return existe;
    }

    public boolean insertarNomina(String fechaInicio, String fechaFin) throws Exception {
        int id = 0;
        String sql, sql1;
        ResultSet estadoSql;
        try {
            sql = "SELECT NVL(MAX(idnomina),0) idnomina FROM mnomina";
            BD.conectar();
            BD.callableStatement(sql);

            if (!BD.consultar()) {
                throw new Exception("Error Realizando La Consulta: " + sql);
            } else {
                estadoSql = BD.obtenerConsulta();
                if (estadoSql.next()) {
                    id = Integer.parseInt(estadoSql.getString("idnomina"));
                    id = id + 1;
                }
            }


            sql1 = "INSERT INTO mnomina(idnomina,fechainicio,fechafin,estado) "
                    + "VALUES (?,TO_DATE(?,'dd/mm/yyyy'),TO_DATE(?,'dd/mm/yyyy'),1)";
            BD.conectar();
            BD.callableStatement(sql1);
            BD.AsignarParametro(1, Integer.toString(id), 2);
            BD.AsignarParametro(2, fechaInicio, 1);
            BD.AsignarParametro(3, fechaFin, 1);

            return BD.registrar();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            BD.desconectar();
        }
    }

    public boolean actualizarNomina(int idNomina, int estado) throws Exception {
        try {
            String sql = "UPDATE mnomina SET estado = ? WHERE idnomina = ?";
            BD.conectar();
            BD.callableStatement(sql);
            BD.AsignarParametro(1, Integer.toString(estado), 2);
            BD.AsignarParametro(2, Integer.toString(idNomina), 2);
            return BD.registrar();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            BD.desconectar();
        }
    }

    public int validacionProduccion(String fechaInicio, String fechaFin) throws Exception {
        int resultado = -1;
        try {
            String sql = "SELECT count(*)\n"
                    + "  FROM mregistros\n"
                    + " WHERE TRUNC(fechainicio) <= TO_DATE (?, 'dd/mm/yyyy') \n"
                    + "   AND (   (estado < 2 AND anulado = 0)\n"
                    + "        OR (estado = 2 AND fechafin IS NULL AND anulado = 0)\n"
                    + "       )";

            BD.conectar();
            BD.callableStatement(sql);
            BD.AsignarParametro(1, fechaFin, 1);
            BD.consultar();

            ResultSet rsCuenta = BD.obtenerConsulta();

            if (rsCuenta.next()) {
                resultado = rsCuenta.getInt(1);
            }


        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            BD.desconectar();
        }
        return resultado;
    }

    public int validacionNovedades(String fechaInicio, String fechaFin) throws Exception {
        int resultado = -1;
        try {
            String sql = "SELECT count(*)  FROM mnovedades \n"
                    + "WHERE trunc(fechainicio) <= TO_DATE(?,'dd/mm/yyyy') \n"
                    + "AND estado < 2 AND anulado = 0";
            BD.conectar();
            BD.callableStatement(sql);
            BD.AsignarParametro(1, fechaFin, 1);
            BD.consultar();

            ResultSet rsCuenta = BD.obtenerConsulta();

            if (rsCuenta.next()) {
                resultado = rsCuenta.getInt(1);
            }


        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            BD.desconectar();
        }
        return resultado;
    }

    public boolean actualizarRegistrosNomina(NominaBeans nomina) throws Exception {
        boolean confirmacion = false;
        String sql;
        try {
            BD.conectar();
            BD.abrirTransaccion();

            sql = "UPDATE mregistros SET idnomina = ?  "
                    + "WHERE idregistro IN "
                    + "(select idregistro from mregistros "
                    + "WHERE TRUNC(fechainicio) <= TO_DATE(?,'dd/mm/yyyy') "
                    + "AND (estado = 2 OR anulado > 0) AND (idnomina IS NULL OR idnomina = 0) )";
            BD.callableStatement(sql);
            BD.AsignarParametro(1, Integer.toString(nomina.getId()), 2);
            BD.AsignarParametro(2, nomina.getFechaFin(), 1);

            confirmacion = BD.registrar();

            sql = "UPDATE mnovedades SET idnomina = ? "
                    + "WHERE TRUNC(fechainicio) <= TO_DATE(?,'dd/mm/yyyy') "
                    + "AND (estado = 2 OR anulado > 0) AND (idnomina IS NULL OR idnomina = 0)  ";
            BD.callableStatement(sql);
            BD.AsignarParametro(1, Integer.toString(nomina.getId()), 2);
            BD.AsignarParametro(2, nomina.getFechaFin(), 1);

            confirmacion = BD.registrar();

            BD.exitoTransaccion();

        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            BD.desconectar();
        }
        return confirmacion;
    }

    public boolean cerrarRegistrosNomina(int idnomina) throws Exception {
        boolean confirmacion = false;
        try {
            BD.conectar();

            String sql = "UPDATE mregistros "
                    + "   SET estado = 3 "
                    + " WHERE idnomina = ? ";
            BD.callableStatement(sql);
            BD.AsignarParametro(1, Integer.toString(idnomina), 2);
            confirmacion = BD.registrar();

            sql = "UPDATE mnovedades "
                    + "   SET estado = 3 "
                    + " WHERE idnomina = ? ";
            BD.callableStatement(sql);
            BD.AsignarParametro(1, Integer.toString(idnomina), 2);
            confirmacion = BD.registrar();

            BD.exitoTransaccion();

        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            BD.desconectar();
        }
        return confirmacion;
    }

    public JSONArray nombresColumnas() throws Exception {

        try {
            String sql = "  SELECT orden, descripcion "
                    + "         FROM tprenominaenc "
                    + "     ORDER BY orden ASC";
            BD.conectar();
            BD.callableStatement(sql);
            BD.consultar();

            ResultSet datoSql = BD.obtenerConsulta();

            JSONArray jsona = new JSONArray();
            while (datoSql.next()) {
                jsona.put(datoSql.getString(2));
            }

            return jsona;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            BD.desconectar();
        }
    }

    public String modeloColumnas() throws Exception {

        try {
            String sql = "  SELECT orden, descripcion "
                    + "         FROM tprenominaenc "
                    + "     ORDER BY orden ASC";
            BD.conectar();
            BD.callableStatement(sql);
            BD.consultar();

            ResultSet datoSql = BD.obtenerConsulta();
            JSONArray jsona = new JSONArray();
            JSONObject jsono;

            boolean rc = false;
            int cont = 1;
            while (datoSql.next()) {
                jsono = new JSONObject();
                String col = datoSql.getString(2);

                jsono.put("name", "col" + cont++);

                if (col.equals("NOMBRES")) {
                    jsono.put("width", "200");
                    
                }else if (col.contains("FECHA")){
                    jsono.put("width", "80");
                    jsono.put("align", "center");
                }else {
                    jsono.put("width", "80");
                    jsono.put("align", "right");
                    jsono.put("formatter", "currency");
                }

                if (!rc) {
                    jsono.put("hidden", true);
                }
                rc = true;


                jsona.put(jsono);
            }

            return jsona.toString();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            BD.desconectar();
        }
    }

    public String getListPrenomina(int page, int rows) throws Exception {
        String sql;
        String strQuery;

        int total = 0;
        int total1 = 0;
        int total_pages;

        ResultSet rsCuenta;
        ResultSet datoSql;

        try {

            sql = "SELECT COUNT(*) AS valor FROM TPRENOMINA";

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

            strQuery = "SELECT * "
                    + "  FROM TPRENOMINA "
                    + "ORDER BY 1 ";
            BD.callableStatement(strQuery);

            BD.consultar();
            datoSql = BD.obtenerConsulta();
            total = total1;

            ResultSetMetaData mtd = datoSql.getMetaData();

            int totalColumnas = mtd.getColumnCount();

            JSONObject jsonData = new JSONObject();

            jsonData.put("page", page);
            jsonData.put("total", total_pages);
            jsonData.put("records", total);
            JSONArray jsonRows = new JSONArray();

            int countColumn = 2;
            JSONObject jsono;
            JSONArray jsona;
            while (datoSql.next()) {
                jsono = new JSONObject();
                jsona = new JSONArray();
                jsono.put("id", datoSql.getString(1));
                jsona.put(datoSql.getInt(1));

                while (countColumn < totalColumnas) {
                    jsona.put(datoSql.getString(countColumn++));
                }
                jsona.put(datoSql.getString(totalColumnas));

                jsono.put("cell", jsona);
                jsonRows.put(jsono);
                countColumn = 2;
            }

            jsonData.put("rows", jsonRows);

            return jsonData.toString();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            BD.desconectar();
        }
    }

    public boolean verificarFecha(String idnomina, String fecha) throws Exception {
        try {

            String sql = "SELECT CASE "
                    + "          WHEN COUNT (*) > 0 "
                    + "             THEN 1 "
                    + "          ELSE 0 "
                    + "       END "
                    + "  FROM mnomina "
                    + " WHERE idnomina = ? "
                    + "   AND TO_DATE (?, 'dd/mm/yyyy') BETWEEN fechainicio AND fechafin ";

            BD.conectar();
            BD.callableStatement(sql);
            BD.AsignarParametro(1, idnomina, 2);
            BD.AsignarParametro(2, fecha, 1);

            BD.consultar();
            ResultSet datoSql = BD.obtenerConsulta();
            boolean b = false;
            if (datoSql.next()) {
                b = datoSql.getBoolean(1);
            }

            return b;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            BD.desconectar();
        }
    }

    public boolean terminarModificaciones(String idusuario, String idnomina, String finicial, String ffinal) throws Exception {
        try {

            String sql = "CALL  PKG_PRENOMINA.CALCULO ( ?, ?, ?, ? ) ";

            BD.conectar();
            BD.callableStatement(sql);
            BD.AsignarParametro(1, idusuario, 2);
            BD.AsignarParametro(2, idnomina, 2);
            BD.AsignarParametro(3, finicial, 1);
            BD.AsignarParametro(4, ffinal, 1);

            return BD.registrar();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            BD.desconectar();
        }
    }

    public boolean existenRegistros(String fechaInicio, String fechaFin) throws Exception {
        boolean existen = false;
        try {
            String sql = "SELECT count(*) "
                    + "  FROM mregistros "
                    + " WHERE fechainicio >= TO_DATE(?,'dd/mm/yyyy') "
                    + "AND TRUNC(fechainicio) <= TO_DATE(?,'dd/mm/yyyy') ";

            BD.conectar();
            BD.callableStatement(sql);
            BD.AsignarParametro(1, fechaInicio, 1);
            BD.AsignarParametro(2, fechaFin, 1);

            BD.consultar();

            ResultSet datoSql = BD.obtenerConsulta();
            if (datoSql.next()) {
                existen = datoSql.getInt(1) > 0 ? true : false;
            }

            System.out.println("Existen = " + existen);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            BD.desconectar();
        }
        return existen;
    }

    public File datosNomina() throws Exception {

        try {
            String strQuery = "SELECT * "
                    + "  FROM TPRENOMINA "
                    + "ORDER BY 1 ";
            BD.conectar();
            BD.callableStatement(strQuery);

            BD.consultar();
            ResultSet datoSql = BD.obtenerConsulta();
            File archivo = new File("prenomina.xls");
            Utils.resulSetToExcel(nombresColumnas(), datoSql, archivo);
            return archivo;

        } catch (SQLException ex) {
            throw new Exception(ex.getMessage());
        }

    }
}
