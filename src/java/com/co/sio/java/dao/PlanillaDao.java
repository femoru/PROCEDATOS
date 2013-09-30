/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.co.sio.java.dao;

import com.co.sio.java.JSON.JSONArray;
import com.co.sio.java.JSON.JSONObject;
import com.co.sio.java.db.ControllerPool;
import com.co.sio.java.mbeans.LaborBeans;
import com.co.sio.java.mbeans.PlanillaBeans;
import com.co.sio.java.mbeans.ReferenciasBeans;
import com.co.sio.java.mbeans.RegistroBeans;
import com.co.sio.java.mbeans.UsuarioBeans;
import java.sql.ResultSet;

/**
 *
 * @author fmoctezuma
 */
public class PlanillaDao {

    private ControllerPool BD;
    private RegistrosDao registrosDao;

    public PlanillaDao() {
        BD = new ControllerPool();

        registrosDao = new RegistrosDao();
    }

    public boolean Guardar(PlanillaBeans beans) throws Exception {
        try {
            String sql = "INSERT INTO tplanilla\n"
                    + "            (idusuario, idlaborcontrato, fechalabor, hiniciod, hdescd,\n"
                    + "             tiempodiurno, hreiniciod, hfinald, tiempotarde, registroslabor,\n"
                    + "             valor, costo\n"
                    + "            )\n"
                    + "     VALUES (?, ?, to_date(?,'dd/mm/yyyy'), ?, ?,\n"
                    + "             ?, ?, ?, ?, ?,\n"
                    + "             ?, ?\n"
                    + "            )";

            BD.conectar();
            BD.callableStatement(sql);
            BD.AsignarParametro(1, Integer.toString(beans.getIdusuario()), 2);
            BD.AsignarParametro(2, Integer.toString(beans.getIdlaborcontrato()), 2);
            BD.AsignarParametro(3, beans.getFechalabor(), 1);
            BD.AsignarParametro(4, beans.getHoraInicioD(), 1);
            BD.AsignarParametro(5, beans.getHoraDescansoD(), 1);
            BD.AsignarParametro(6, Integer.toString(beans.getTiempoDiurno()), 2);
            BD.AsignarParametro(7, beans.getHoraReinicioD(), 1);
            BD.AsignarParametro(8, beans.getHoraFinalD(), 1);
            BD.AsignarParametro(9, Integer.toString(beans.getTiempoTarde()), 2);
            BD.AsignarParametro(10, Integer.toString(beans.getRegistrosLabor()), 2);
            BD.AsignarParametro(11, Integer.toString(beans.getValor()), 2);
            BD.AsignarParametro(12, Integer.toString(beans.getCosto()), 2);

            return BD.registrar();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            BD.desconectar();
        }
    }

    public String getListPlanilla(int idpersona, int idlabor, String mes, int page, int rows) throws Exception {
        String sql;


        int total = 0;
        int total1 = 0;
        int total_pages;

        ResultSet rsCuenta;
        ResultSet datoSql;

        try {

            sql = "SELECT COUNT(*)\n"
                    + "  FROM tplanilla\n"
                    + " WHERE idusuario = ?\n"
                    + "   AND idlaborcontrato = ?\n"
                    + "   AND TO_CHAR (fechalabor, 'MM/YYYY') = ? ";

            BD.conectar();
            BD.callableStatement(sql);
            BD.AsignarParametro(1, Integer.toString(idpersona), 2);
            BD.AsignarParametro(2, Integer.toString(idlabor), 2);
            BD.AsignarParametro(3, mes, 1);
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

            sql = "SELECT idplanilla, TO_CHAR(fechalabor,'DD') AS DIA, "
                    + "      replace(hiniciod,':'), "
                    + "      replace(hdescd,':'), "
                    + "     TRIM (TO_CHAR (TRUNC (tiempodiurno / 60), '09'))\n"
                    + "       || ':'\n"
                    + "       || TRIM (TO_CHAR (TRUNC (MOD (ABS (tiempodiurno), 60)), '09')) AS tiempodiurno, "
                    + "      replace(hreiniciod,':'), "
                    + "      replace(hfinald,':'),\n"
                    + "       TRIM (TO_CHAR (TRUNC (tiempotarde / 60), '09'))\n"
                    + "       || ':'\n"
                    + "       || TRIM (TO_CHAR (TRUNC (MOD (ABS (tiempotarde), 60)), '09')) AS tiempotarde, "
                    + "       registroslabor\n"
                    + "  FROM tplanilla\n"
                    + " WHERE idusuario = ?\n"
                    + "   AND idlaborcontrato = ?\n"
                    + "   AND TO_CHAR (fechalabor, 'MM/YYYY') = ?\n"
                    + " ORDER BY DIA DESC";
            BD.callableStatement(sql);
            BD.AsignarParametro(1, Integer.toString(idpersona), 2);
            BD.AsignarParametro(2, Integer.toString(idlabor), 2);
            BD.AsignarParametro(3, mes, 1);
            BD.consultar();
            datoSql = BD.obtenerConsulta();
            total = total1;

            int totalColumnas = datoSql.getMetaData().getColumnCount();

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
                while (countColumn <= totalColumnas) {
                    jsona.put(datoSql.getString(countColumn++));
                }
                jsono.put("cell", jsona);
                jsonRows.put(jsono);
                countColumn = 2;
            }

            jsonData.put("rows", jsonRows);

            return jsonData.toString();

        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        } finally {
            BD.desconectar();
        }

    }

    public boolean Actualizar(PlanillaBeans beans) throws Exception {
        try {
            String sql = "UPDATE tplanilla\n"
                    + "   SET fechalabor = to_date(?,'dd/mm/yyyy'),\n"
                    + "       hiniciod = ?,\n"
                    + "       hdescd = ?,\n"
                    + "       tiempodiurno = ?,\n"
                    + "       hreiniciod = ?,\n"
                    + "       hfinald = ?,\n"
                    + "       tiempotarde = ?,\n"
                    + "       registroslabor = ?\n"
                    + " WHERE idplanilla = ? ";

            BD.conectar();
            BD.callableStatement(sql);
            BD.AsignarParametro(1, beans.getFechalabor(), 1);
            BD.AsignarParametro(2, beans.getHoraInicioD(), 1);
            BD.AsignarParametro(3, beans.getHoraDescansoD(), 1);
            BD.AsignarParametro(4, Integer.toString(beans.getTiempoDiurno()), 2);
            BD.AsignarParametro(5, beans.getHoraReinicioD(), 1);
            BD.AsignarParametro(6, beans.getHoraFinalD(), 1);
            BD.AsignarParametro(7, Integer.toString(beans.getTiempoTarde()), 2);
            BD.AsignarParametro(8, Integer.toString(beans.getRegistrosLabor()), 2);
            BD.AsignarParametro(9, Integer.toString(beans.getIdplanilla()), 2);

            return BD.actualizar();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            BD.desconectar();
        }
    }

    public boolean eliminar(int idplanilla) throws Exception {
        try {


            String sql = "DELETE FROM tplanilla WHERE idplanilla = ?";
            BD.conectar();
            BD.callableStatement(sql);
            BD.AsignarParametro(1, Integer.toString(idplanilla), 2);


            return BD.registrar();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            BD.desconectar();
        }
    }

    public int consultarRango(PlanillaBeans beans) throws Exception {

        try {

            int diurno = consultaRangoDiurno(beans, true);
            int nocturno = consultaRangoDiurno(beans, false);

            return diurno + nocturno;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }

    }

    private int consultaRangoDiurno(PlanillaBeans beans, boolean diurno) throws Exception {
        try {
            String sql;
            sql = "SELECT count(*)\n"
                    + "  FROM tplanilla\n"
                    + " WHERE TO_CHAR (fechalabor, 'DD/MM/YYYY') = ? \n"
                    + "   AND idusuario = ? "
                    + " AND idplanilla <> ? ";
            if (diurno) {
                sql += "    AND (   "
                        + " TO_DATE (?, 'HH24:MI') > TO_DATE (hiniciod,'HH24:MI')\n"
                        + "     AND TO_DATE (?, 'HH24:MI') < TO_DATE (hdescd,'HH24:MI')\n"
                        + " OR "
                        + " TO_DATE (?, 'HH24:MI') > TO_DATE (hiniciod,'HH24:MI')\n"
                        + "     AND TO_DATE (?, 'HH24:MI') < TO_DATE (hdescd,'HH24:MI')\n"
                        + "       )\n";
            } else {
                sql += "   AND (   "
                        + " TO_DATE (?, 'HH24:MI') > TO_DATE (hreiniciod,'HH24:MI')\n"
                        + "     AND TO_DATE (?, 'HH24:MI') < TO_DATE (hfinald,'HH24:MI')\n"
                        + " OR "
                        + " TO_DATE (?, 'HH24:MI') > TO_DATE (hreiniciod,'HH24:MI')\n"
                        + "     AND TO_DATE (?, 'HH24:MI') < TO_DATE (hfinald,'HH24:MI')\n"
                        + "       )\n";
            }
            BD.conectar();
            BD.callableStatement(sql);
            BD.AsignarParametro(1, beans.getFechalabor(), 1);
            BD.AsignarParametro(2, Integer.toString(beans.getIdusuario()), 2);
            BD.AsignarParametro(3, Integer.toString(beans.getIdplanilla()), 2);
            if (diurno) {
                BD.AsignarParametro(4, beans.getHoraInicioD(), 1);
                BD.AsignarParametro(5, beans.getHoraInicioD(), 1);
                BD.AsignarParametro(6, beans.getHoraDescansoD(), 1);
                BD.AsignarParametro(7, beans.getHoraDescansoD(), 1);
            } else {
                BD.AsignarParametro(4, beans.getHoraReinicioD(), 1);
                BD.AsignarParametro(5, beans.getHoraReinicioD(), 1);
                BD.AsignarParametro(6, beans.getHoraFinalD(), 1);
                BD.AsignarParametro(7, beans.getHoraFinalD(), 1);
            }
            BD.consultar();

            ResultSet rsCuenta = BD.obtenerConsulta();

            int total = 0;
            if (rsCuenta.next()) {
                total = rsCuenta.getInt(1);
            }
            return total;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            BD.desconectar();
        }
    }

    public PlanillaBeans consultar(int idplanilla) throws Exception {
        PlanillaBeans beans = new PlanillaBeans();
        try {
            String sql = "SELECT \n"
                    + " IDUSUARIO, IDLABORCONTRATO, \n"
                    + "   FECHALABOR, HINICIOD, HDESCD, \n"
                    + "    HREINICIOD, HFINALD, \n"
                    + "   REGISTROSLABOR \n"
                    + "FROM TPLANILLA WHERE IDPLANILLA = ? ";
            BD.conectar();
            BD.callableStatement(sql);
            BD.AsignarParametro(1, Integer.toString(idplanilla), 2);

            BD.consultar();

            ResultSet datoSql = BD.obtenerConsulta();

            if (datoSql.next()) {
                beans.setIdusuario(datoSql.getInt(1));
                beans.setIdlaborcontrato(datoSql.getInt(2));
                beans.setFechalabor(datoSql.getString(3));
                beans.setHoraInicioD(datoSql.getString(4));
                beans.setHoraDescansoD(datoSql.getString(5));
                beans.setHoraReinicioD(datoSql.getString(6));
                beans.setHoraFinalD(datoSql.getString(7));
                beans.setRegistrosLabor(datoSql.getInt(8));
            }

            return beans;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            BD.desconectar();
        }

    }

    public int validarPlanilla(int idusuario, int idlaborcontrato, String mes, String ingresado) throws Exception {
        try {

            UsuarioBeans usuarioBeans = new UsuarioBeans();
            usuarioBeans.setIdusuario(idusuario);
            LaborBeans laborBeans = new LaborBeans();
            laborBeans.setIdlaborcontrato(idlaborcontrato);

            String sql = "SELECT COUNT(*)\n"
                    + "  FROM tplanilla\n"
                    + " WHERE idusuario = ?\n"
                    + "   AND idlaborcontrato = ?\n"
                    + "   AND TO_CHAR (fechalabor, 'MM/YYYY') = ? ";

            BD.conectar();
            BD.callableStatement(sql);
            BD.AsignarParametro(1, Integer.toString(idusuario), 2);
            BD.AsignarParametro(2, Integer.toString(idlaborcontrato), 2);
            BD.AsignarParametro(3, mes, 1);
            BD.consultar();

            ResultSet datoSql = BD.obtenerConsulta();
            int total = 0;
            if (datoSql.next()) {
                total = Integer.parseInt(datoSql.getString(1));
            }
            sql = "SELECT idplanilla, TO_CHAR (fechalabor, 'DD/MM/YYYY ') || hiniciod hiniciod,\n"
                    + "       TO_CHAR (fechalabor, 'DD/MM/YYYY ') || hdescd hdescd, tiempodiurno,\n"
                    + "       TO_CHAR (fechalabor, 'DD/MM/YYYY ') || hreiniciod hreiniciod,\n"
                    + "       TO_CHAR (fechalabor, 'DD/MM/YYYY ') || hfinald hfinald, tiempotarde,\n"
                    + "       registroslabor, valor, costo\n"
                    + "  FROM tplanilla \n"
                    + " WHERE idusuario = ?\n"
                    + "   AND idlaborcontrato = ?\n"
                    + "   AND TO_CHAR (fechalabor, 'MM/YYYY') = ?\n";

            BD.callableStatement(sql);
            BD.AsignarParametro(1, Integer.toString(idusuario), 2);
            BD.AsignarParametro(2, Integer.toString(idlaborcontrato), 2);
            BD.AsignarParametro(3, mes, 1);
            BD.consultar();

            datoSql = BD.obtenerConsulta();


            ReferenciasBeans referenciasBeans = new ReferenciasBeans();
            referenciasBeans.setCodigo(0);
            String observacion = "REGISTRO POR PLANILLA INGRESO " + ingresado;
            RegistroBeans registroDiurno;
            RegistroBeans registroTarde;
            boolean registro;


            while (datoSql.next()) {
                registroDiurno = new RegistroBeans();
                registroTarde = new RegistroBeans();

                registroDiurno.setUsuario(usuarioBeans);
                registroDiurno.setLabor(laborBeans);
                registroDiurno.setFechaInicio(datoSql.getString("hiniciod"));
                registroDiurno.setFechaFin(datoSql.getString("hdescd"));
                registroDiurno.setTiempolabor(datoSql.getInt("tiempodiurno"));
                registroDiurno.setObservacion(observacion);
                registroDiurno.setEstado(2);
                registroDiurno.setValor(datoSql.getInt("valor"));
                registroDiurno.setCosto(datoSql.getInt("costo"));
                registroDiurno.setAnulado(referenciasBeans);

                registroTarde.setUsuario(usuarioBeans);
                registroTarde.setLabor(laborBeans);
                registroTarde.setFechaInicio(datoSql.getString("hreiniciod"));
                registroTarde.setFechaFin(datoSql.getString("hfinald"));
                registroTarde.setTiempolabor(datoSql.getInt("tiempotarde"));
                registroTarde.setRegistroslabor(datoSql.getInt("registroslabor"));
                registroTarde.setObservacion(observacion);
                registroTarde.setEstado(2);
                registroTarde.setValor(datoSql.getInt("valor"));
                registroTarde.setCosto(datoSql.getInt("costo"));
                registroTarde.setAnulado(referenciasBeans);


                if (!existeRegistro(idusuario, registroDiurno) && !existeRegistro(idusuario, registroTarde)) {
                    registro = false;
                    if (registroDiurno.getTiempolabor() > 0 && registrosDao.insertar(registroDiurno)) {
                        registro = true;
                    }
                    if (registroTarde.getTiempolabor() > 0 && registrosDao.insertar(registroTarde)) {
                        registro = true;
                    }
                    //if (new RegistrosDao().insertar(registroDiurno) && new RegistrosDao().insertar(registroTarde)) {
                    if (registro) {
                        eliminar(datoSql.getInt("idplanilla"));
                        total--;
                    }
                }

            }
            return total;

        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            BD.desconectar();
        }
    }

    private boolean existeRegistro(int idusuario, RegistroBeans registroBeans) throws Exception {

        RegistroBeans registro = registrosDao.consultarRegistroFecha(idusuario, registroBeans.getFechaInicio());

        if (registro.getIdregistro() != 0) {
            return true;
        }
        registro = registrosDao.consultarRegistroFecha(idusuario, registroBeans.getFechaFin());
        if (registro.getIdregistro() != 0) {
            return true;
        }
        int idRegistroIgual = registrosDao.consultarUltimoID(registroBeans);
        if (idRegistroIgual != 0) {
            return true;
        }
        idRegistroIgual = registrosDao.consultarIgual(registroBeans);
        if (idRegistroIgual != 0) {
            return true;
        }


        return false;
    }
}
