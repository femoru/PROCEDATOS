/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.co.sio.java.dao;

import com.co.sio.java.JSON.JSONArray;
import com.co.sio.java.JSON.JSONObject;
import com.co.sio.java.db.ControllerPool;
import com.co.sio.java.mbeans.NovedadBeans;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author fmoctezuma
 */
public class NovedadesDao {

    ControllerPool BD;

    public NovedadesDao() {
        BD = new ControllerPool();
    }

    public NovedadBeans consultar(int idnovedad) throws Exception {
        try {
            NovedadBeans novedadBeans = new NovedadBeans();

            String sql = "SELECT m.idnovedad, m.codnovedad, m.idusuario, TO_CHAR(m.fechanovedad,'DD/MM/YYYY'), TO_CHAR(m.fechainicio,'DD/MM/YYYY'),\n"
                    + "       TO_CHAR(m.fechafin,'DD/MM/YYYY'), m.dias, m.observacion, m.nroincapacidad, m.coddiagnostico,\n"
                    + "       m.claseincapacidad, TO_CHAR(m.fechaaccidente,'DD/MM/YYYY'), m.indprorroga, m.idnovprorroga,\n"
                    + "       m.nroprorrogacg, m.vlrempresa, m.vlreps, m.archivo, m.estado,\n"
                    + "       m.idnomina, m.anulado, m.plano\n"
                    + "  FROM mnovedades m where idnovedad = ?";
            BD.conectar();
            BD.callableStatement(sql);
            BD.AsignarParametro(1, Integer.toString(idnovedad), 2);
            BD.consultar();
            ResultSet datoSql = BD.obtenerConsulta();
            if (datoSql.next()) {
                novedadBeans.setIdnovedad(datoSql.getInt(1));
                novedadBeans.setCodnovedad(datoSql.getInt(2));
                novedadBeans.setIdusuario(datoSql.getInt(3));
                novedadBeans.setFecharegistro(datoSql.getString(4));
                novedadBeans.setFechainicio(datoSql.getString(5));
                novedadBeans.setFechafin(datoSql.getString(6));
                novedadBeans.setDias(datoSql.getInt(7));
                novedadBeans.setObservacion(datoSql.getString(8));
                novedadBeans.setNroincapacidad(datoSql.getString(9));
                novedadBeans.setCoddiagnostico(datoSql.getString(10));
                novedadBeans.setClaseincapacidad(datoSql.getInt(11));
                novedadBeans.setFechaaccidente(datoSql.getString(12));
                novedadBeans.setIndprorroga(datoSql.getInt(13));
                novedadBeans.setIdnovprorroga(datoSql.getInt(14));
                novedadBeans.setNronovcg(datoSql.getInt(15));
                novedadBeans.setVlrempresa(datoSql.getInt(16));
                novedadBeans.setVlreps(datoSql.getInt(17));
                novedadBeans.setArchivo(datoSql.getString(18));
                novedadBeans.setEstado(datoSql.getInt(19));
                novedadBeans.setIdnomina(datoSql.getInt(20));
                novedadBeans.setAnulado(datoSql.getInt(21));
                novedadBeans.setPlano(datoSql.getInt(22));
            }

            return novedadBeans;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            BD.desconectar();
        }
    }

    public NovedadBeans consultar(NovedadBeans beans) throws Exception {
        try {
            NovedadBeans novedadBeans = new NovedadBeans();

            String sql = "SELECT m.idnovedad, m.codnovedad, m.idusuario, TO_CHAR(m.fechanovedad,'DD/MM/YYYY'), TO_CHAR(m.fechainicio,'DD/MM/YYYY'),\n"
                    + "       TO_CHAR(m.fechafin,'DD/MM/YYYY'), m.dias, m.observacion, m.nroincapacidad, m.coddiagnostico,\n"
                    + "       m.claseincapacidad, TO_CHAR(m.fechaaccidente,'DD/MM/YYYY'), m.indprorroga, m.idnovprorroga,\n"
                    + "       m.nroprorrogacg, m.vlrempresa, m.vlreps, m.archivo, m.estado,\n"
                    + "       m.idnomina, m.anulado, m.plano\n"
                    + "  FROM mnovedades m "
                    + "     WHERE m.idusuario = ? "
                    + "         AND m.codnovedad = ? "
                    + "         AND TO_CHAR(m.fechainicio,'DD/MM/YYYY') = ?"
                    + "         AND TO_CHAR(m.fechafin,'DD/MM/YYYY') = ?";
            BD.conectar();
            BD.callableStatement(sql);
            BD.AsignarParametro(1, Integer.toString(beans.getIdusuario()), 2);
            BD.AsignarParametro(2, Integer.toString(beans.getCodnovedad()), 2);
            BD.AsignarParametro(3, beans.getFechainicio(), 1);
            BD.AsignarParametro(4, beans.getFechafin(), 1);
            BD.consultar();
            ResultSet datoSql = BD.obtenerConsulta();
            if (datoSql.next()) {
                novedadBeans.setIdnovedad(datoSql.getInt(1));
                novedadBeans.setCodnovedad(datoSql.getInt(2));
                novedadBeans.setIdusuario(datoSql.getInt(3));
                novedadBeans.setFecharegistro(datoSql.getString(4));
                novedadBeans.setFechainicio(datoSql.getString(5));
                novedadBeans.setFechafin(datoSql.getString(6));
                novedadBeans.setDias(datoSql.getInt(7));
                novedadBeans.setObservacion(datoSql.getString(8));
                novedadBeans.setNroincapacidad(datoSql.getString(9));
                novedadBeans.setCoddiagnostico(datoSql.getString(10));
                novedadBeans.setClaseincapacidad(datoSql.getInt(11));
                novedadBeans.setFechaaccidente(datoSql.getString(12));
                novedadBeans.setIndprorroga(datoSql.getInt(13));
                novedadBeans.setIdnovprorroga(datoSql.getInt(14));
                novedadBeans.setNronovcg(datoSql.getInt(15));
                novedadBeans.setVlrempresa(datoSql.getInt(16));
                novedadBeans.setVlreps(datoSql.getInt(17));
                novedadBeans.setArchivo(datoSql.getString(18));
                novedadBeans.setEstado(datoSql.getInt(19));
                novedadBeans.setIdnomina(datoSql.getInt(20));
                novedadBeans.setAnulado(datoSql.getInt(21));
                novedadBeans.setPlano(datoSql.getInt(22));
            }

            return novedadBeans;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            BD.desconectar();
        }
    }

    public boolean anular(int idnovedad, int anulado) throws Exception {
        try {
            String sql = "UPDATE mnovedades "
                    + "     SET anulado = ? "
                    + "  WHERE  idnovedad = ? OR idnovprorroga = ?";
            BD.conectar();
            BD.callableStatement(sql);
            BD.AsignarParametro(1, Integer.toString(anulado), 2);
            BD.AsignarParametro(2, Integer.toString(idnovedad), 2);
            BD.AsignarParametro(3, Integer.toString(idnovedad), 2);

            return BD.registrar();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            BD.desconectar();
        }
    }

    public List<NovedadBeans> listar(int idusuario, int idnovedad) throws Exception {
        try {
            List<NovedadBeans> list = new ArrayList<NovedadBeans>();
            String sql = "SELECT m.idnovedad, TO_CHAR(m.fechainicio,'DD/MM/YYYY'), TO_CHAR(m.fechafin,'DD/MM/YYYY')\n"
                    + "  FROM mnovedades m\n"
                    + " WHERE idusuario = ? AND idnovedad <> ? AND m.indprorroga = 0 AND idnomina = 0 AND anulado = 0 AND plano = 1"
                    + " ORDER BY fechainicio ASC";
            BD.conectar();
            BD.callableStatement(sql);
            BD.AsignarParametro(1, Integer.toString(idusuario), 2);
            BD.AsignarParametro(2, Integer.toString(idnovedad), 2);
            BD.consultar();
            ResultSet datoSql = BD.obtenerConsulta();
            NovedadBeans novedadBeans;
            while (datoSql.next()) {
                novedadBeans = new NovedadBeans();
                novedadBeans.setIdnovedad(datoSql.getInt(1));
                novedadBeans.setFechainicio(datoSql.getString(2));
                novedadBeans.setFechafin(datoSql.getString(3));
                list.add(novedadBeans);
            }

            return list;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            BD.desconectar();
        }


    }

    public boolean Guardar(NovedadBeans beans) throws Exception {
        try {
            String sql = "INSERT INTO mnovedades\n"
                    + "            (codnovedad, idusuario, fechainicio, fechafin,\n"
                    + "             dias, observacion, nroincapacidad, coddiagnostico,\n"
                    + "             claseincapacidad, fechaaccidente, indprorroga, idnovprorroga,\n"
                    + "             nroprorrogacg, vlrempresa, vlreps, archivo, plano,\n"
                    + "             diassio,diaseps,porcentaje,estado,diascompensados,diasncomp\n"
                    + "            )\n"
                    + "     VALUES (?, ?, TO_DATE(?,'DD/MM/YYYY'), TO_DATE(?,'DD/MM/YYYY'),\n"
                    + "             ?, ?, ?, ?,\n"
                    + "             ?, TO_DATE(?,'DD/MM/YYYY'), ?, ?,\n"
                    + "             ?, ?, ?, ?, ?,\n"
                    + "             ?, ?, ?, ?, ?, ?\n"
                    + "            )";

            BD.conectar();
            BD.callableStatement(sql);
            BD.AsignarParametro(1, Integer.toString(beans.getCodnovedad()), 2);
            BD.AsignarParametro(2, Integer.toString(beans.getIdusuario()), 2);
            BD.AsignarParametro(3, beans.getFechainicio(), 1);
            BD.AsignarParametro(4, beans.getFechafin(), 1);
            BD.AsignarParametro(5, Integer.toString(beans.getDias()), 2);
            BD.AsignarParametro(6, beans.getObservacion(), 1);
            BD.AsignarParametro(7, beans.getNroincapacidad(), 1);
            BD.AsignarParametro(8, beans.getCoddiagnostico(), 1);
            BD.AsignarParametro(9, Integer.toString(beans.getClaseincapacidad()), 2);
            BD.AsignarParametro(10, beans.getFechaaccidente(), 1);
            BD.AsignarParametro(11, Integer.toString(beans.getIndprorroga()), 2);
            BD.AsignarParametro(12, Integer.toString(beans.getIdnovprorroga()), 2);
            BD.AsignarParametro(13, Integer.toString(beans.getNronovcg()), 2);
            BD.AsignarParametro(14, Integer.toString(beans.getVlrempresa()), 2);
            BD.AsignarParametro(15, Integer.toString(beans.getVlreps()), 2);
            BD.AsignarParametro(16, beans.getArchivo(), 1);
            BD.AsignarParametro(17, Integer.toString(beans.getPlano()), 2);
            BD.AsignarParametro(18, Integer.toString(beans.getDiasSIO()), 2);
            BD.AsignarParametro(19, Integer.toString(beans.getDiasEPS()), 2);
            BD.AsignarParametro(20, Double.toString(beans.getPorcentaje()), 5);
            BD.AsignarParametro(21, Integer.toString(beans.getEstado()), 2);
            BD.AsignarParametro(22, Integer.toString(beans.getDiasComp()), 2);
            BD.AsignarParametro(23, Integer.toString(beans.getDiasncomp()), 2);


            return BD.registrar();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            BD.desconectar();
        }
    }

    public String getListNovedades(String mes, int page, int rows) throws Exception {
        String sql;

        int total = 0;
        int total1 = 0;
        int total_pages;

        ResultSet datoSql;

        try {

            sql = "SELECT COUNT (*)\n"
                    + "  FROM mnovedades mn INNER JOIN mpersonas mp ON mp.idpersona = mn.idusuario\n"
                    + "       INNER JOIN rnovedades rn ON rn.codnovedad = mn.codnovedad\n"
                    + "       INNER JOIN rcausasanulacion rca ON mn.anulado = rca.codcausa\n"
                    + " WHERE plano = 1 AND TO_CHAR (mn.fechainicio, 'mm/yyyy') =  ? ";

            BD.conectar();
            BD.callableStatement(sql);
            BD.AsignarParametro(1, mes, 1);
            BD.consultar();

            datoSql = BD.obtenerConsulta();

            if (datoSql.next()) {
                total = datoSql.getInt(1);
                total1 = total;
            }


            if (total > 0) {
                double d = Math.ceil((double) (total) / (double) (rows));
                total_pages = (int) (d);
            } else {
                total_pages = 0;
            }

            sql = "SELECT mn.idnovedad, mn.idusuario, mn.codnovedad,\n"
                    + "          mp.pnombre\n"
                    + "       || ''\n"
                    + "       || mp.snombre\n"
                    + "       || ''\n"
                    + "       || mp.papellido\n"
                    + "       || ''\n"
                    + "       || mp.sapellido AS nombre,\n"
                    + "       to_char(mn.fechainicio,'dd/mm/yyyy'), to_char(mn.fechafin,'dd/mm/yyyy'), rn.desnovedad, mn.observacion,mn.estado, mn.dias,\n"
                    + "       mn.diassio, mn.diaseps, rca.descausa,mn.nroincapacidad,mn.coddiagnostico,rn.tiponovedad,mn.vlrempresa \n"
                    + "  FROM mnovedades mn INNER JOIN mpersonas mp ON mp.idpersona = mn.idusuario\n"
                    + "       INNER JOIN rnovedades rn ON rn.codnovedad = mn.codnovedad\n"
                    + "       INNER JOIN rcausasanulacion rca ON mn.anulado = rca.codcausa\n"
                    + " WHERE plano = 1 AND TO_CHAR (mn.fechainicio, 'mm/yyyy') = ? ";
            BD.callableStatement(sql);
            BD.AsignarParametro(1, mes, 1);
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

    public boolean actualizar(NovedadBeans beans) throws Exception {
        try {
            String sql = "UPDATE mnovedades\n"
                    + "   SET fechainicio = TO_DATE(?,'DD/MM/YYYY'),\n"
                    + "       fechafin = TO_DATE(?,'DD/MM/YYYY'),\n"
                    + "       dias = ?,\n"
                    + "       nroincapacidad = ?,\n"
                    + "       coddiagnostico = ?,\n"
                    + "       claseincapacidad = ?,\n"
                    + "       fechaaccidente = TO_DATE(?,'DD/MM/YYYY'),\n"
                    + "       indprorroga = ?,\n"
                    + "       idnovprorroga = ?,\n"
                    + "       nroprorrogacg = ?,\n"
                    + "       vlrempresa = ?,\n"
                    + "       vlreps = ?,\n"
                    + "       estado = ?,\n"
                    + "       diassio = ?,\n"
                    + "       diaseps = ?,\n"
                    + "       diascompensados = ?,\n"
                    + "       porcentaje = ?\n"
                    + " WHERE idnovedad = ?";

            BD.conectar();
            BD.callableStatement(sql);
            BD.AsignarParametro(1, beans.getFechainicio(), 1);
            BD.AsignarParametro(2, beans.getFechafin(), 1);
            BD.AsignarParametro(3, Integer.toString(beans.getDias()), 2);
            BD.AsignarParametro(4, beans.getNroincapacidad(), 1);
            BD.AsignarParametro(5, beans.getCoddiagnostico(), 1);
            BD.AsignarParametro(6, Integer.toString(beans.getClaseincapacidad()), 2);
            BD.AsignarParametro(7, beans.getFechaaccidente(), 1);
            BD.AsignarParametro(8, Integer.toString(beans.getIndprorroga()), 2);
            BD.AsignarParametro(9, Integer.toString(beans.getIdnovprorroga()), 2);
            BD.AsignarParametro(10, Integer.toString(beans.getNronovcg()), 2);
            BD.AsignarParametro(11, Integer.toString(beans.getVlrempresa()), 5);
            BD.AsignarParametro(12, Integer.toString(beans.getVlreps()), 5);
            BD.AsignarParametro(13, Integer.toString(beans.getEstado()), 2);
            BD.AsignarParametro(14, Integer.toString(beans.getDiasSIO()), 2);
            BD.AsignarParametro(15, Integer.toString(beans.getDiasEPS()), 2);
            BD.AsignarParametro(16, Integer.toString(beans.getDiasComp()), 2);
            BD.AsignarParametro(17, Double.toString(beans.getPorcentaje()), 5);
            BD.AsignarParametro(18, Integer.toString(beans.getIdnovedad()), 2);

            return BD.actualizar();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            BD.desconectar();
        }
    }

    public int calcularLaborales(String fInicial, String fFinal) throws Exception {

        try {
            BD.conectar();

            String sql = "SELECT dias_laborables (to_date(?,'dd/MM/yyyy'), to_date(?,'dd/MM/yyyy')) laborales "
                    + "  FROM DUAL";
            BD.callableStatement(sql);
            BD.AsignarParametro(1, fInicial, 1);
            BD.AsignarParametro(2, fFinal, 1);

            BD.consultar();

            ResultSet datoSql = BD.obtenerConsulta();

            if (datoSql.next()) {
                return datoSql.getInt(1);
            } else {
                return 0;
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            BD.desconectar();

        }
    }
}
