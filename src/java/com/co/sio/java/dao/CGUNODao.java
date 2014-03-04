/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.co.sio.java.dao;

import com.co.sio.java.JSON.JSONArray;
import com.co.sio.java.db.ControllerPool;
import com.co.sio.java.mbeans.CGUNORegistroBeans;
import com.co.sio.java.mbeans.ConceptoBeans;
import com.co.sio.java.utils.Utils;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author fmoctezuma
 */
public class CGUNODao {

    ControllerPool BD;
    private Map<String, ConceptoBeans> conceptos;
    private Map<Integer, String> parametros;

    public CGUNODao() throws Exception {
        BD = new ControllerPool();
        conceptos = new TreeMap<String, ConceptoBeans>();
        parametros = new TreeMap<Integer, String>();
        cargarConceptos();
        cargarVariables();
    }

    public String cargarNovedades(int idnomina) throws Exception {
        try {

            String cad = "";

            ArrayList<JSONArray> novedades = new ArrayList<JSONArray>();
            ArrayList<JSONArray> tnls = new ArrayList<JSONArray>();

            int consecutivo = 1;

            String sql = "SELECT rn.tiponovedad tipo,\n"
                    + "       RPAD (mp.identificacion, 13) empl, '00' suc, '00' contr, '000' nrotnl,\n"
                    + "       LPAD (rn.codconcepto, 3, 0) concp,\n"
                    + "       TO_CHAR (mn.fechainicio, 'YYYYMMDD') finicio,\n"
                    + "       TO_CHAR (mn.fechafin, 'YYYYMMDD') ffin, LPAD (mn.dias, 5, 0) dias,\n"
                    + "       '0000' hinicio, '0000' hfin, LPAD ('0', 13, 0) || '+' vlrtnl,\n"
                    + "       LPAD ('0', 13, 0) || '+' vlrbase, RPAD (NVL(mn.observacion,' '), 40) obsv\n"
                    + "  FROM mnovedades mn "
                    + "       INNER JOIN rnovedades rn ON rn.codnovedad = mn.codnovedad\n"
                    + "       INNER JOIN mpersonas mp ON mp.idpersona = mn.idusuario\n"
                    + " WHERE rn.tiponovedad = 1 AND mn.plano = 1 AND estado = 2 AND anulado = 0 AND idnomina = ?"
                    + " ORDER BY mn.fechainicio";
            BD.conectar();
            BD.callableStatement(sql);
            BD.AsignarParametro(1, Integer.toString(idnomina), 2);
            BD.consultar();

            ResultSet datoSql = BD.obtenerConsulta();
            ResultSetMetaData rsmd = datoSql.getMetaData();
            int colCount = rsmd.getColumnCount();
            JSONArray jsona;

            while (datoSql.next()) {
                jsona = new JSONArray();
                jsona.put(0, Utils.padRight(Integer.toString(consecutivo++), '0', 9));
                for (int i = 1; i <= colCount; i++) {
                    jsona.put(i, datoSql.getString(i));
                }
                tnls.add(jsona);
            }
            novedades.addAll(tnls);
            ArrayList<JSONArray> incapacidades = new ArrayList<JSONArray>();

            sql = "SELECT     rn.tiponovedad tipo,\n"
                    + "       RPAD (mp.identificacion, 13) empl, '00' suc, '00' contr, '000' nrotnl,\n"
                    + "       LPAD (rn.codconcepto, 3, 0) concp,\n"
                    + "       TO_CHAR (mn.fechainicio, 'YYYYMMDD') finicio,\n"
                    + "       TO_CHAR (mn.fechafin, 'YYYYMMDD') ffin, LPAD (mn.dias, 5, 0) dias,\n"
                    + "       LPAD (mn.vlreps, 11, 0) || '00+' vlrtnl,\n"
                    + "       LPAD ('0', 13, 0) || '+' vlrbase, RPAD (mn.observacion, 40) obsv,\n"
                    + "       ' ' serie, RPAD (mn.nroincapacidad, 6, ' ') nroinc, 'A' tipoinc,\n"
                    + "       RPAD (mn.coddiagnostico, 6, ' ') coddx, mn.claseincapacidad clsinc,\n"
                    + "       RPAD (NVL (TO_CHAR (mn.fechaaccidente, 'YYYYMMDD'), ' '), 8) facc,\n"
                    + "       mn.indprorroga indpro, LPAD (NVL (mn.idnovprorroga, 0), 9, 0) novpro,\n"
                    + "       LPAD (mn.vlrempresa, 11, 0) || '00+' vlrsio,\n"
                    + "       LPAD ('0', 13, 0) || '-' vlrotro,\n"
                    + "       TO_CHAR (mn.fechainicio, 'YYYYMMDD') fexpd,\n"
                    + "       LPAD ('0', 13, 0) || '+' vlribc, '00' msprom,\n"
                    + "       LPAD (mn.nroprorrogacg, 3, 0) nrocg, mn.idnovedad \n"
                    + "  FROM mnovedades mn INNER JOIN rnovedades rn ON rn.codnovedad = mn.codnovedad\n"
                    + "       INNER JOIN mpersonas mp ON mp.idpersona = mn.idusuario\n"
                    + " WHERE rn.tiponovedad = 2 AND mn.plano = 1 AND estado = 2 AND anulado = 0 AND idnomina = ?"
                    + " ORDER BY mn.idnovedad";
            BD.conectar();
            BD.callableStatement(sql);
            BD.AsignarParametro(1, Integer.toString(idnomina), 2);
            BD.consultar();

            datoSql = BD.obtenerConsulta();
            rsmd = datoSql.getMetaData();
            colCount = rsmd.getColumnCount();

            while (datoSql.next()) {
                jsona = new JSONArray();
                jsona.put(0, Utils.padRight(Integer.toString(consecutivo++), '0', 9));
                for (int i = 1; i <= colCount; i++) {
                    if (i != 20) {
                        jsona.put(i, datoSql.getString(i));
                    } else {
                        int idnov = datoSql.getInt(20);
                        if (idnov == 0) {
                            jsona.put(i, datoSql.getString(i));
                        } else {
                            for (JSONArray array : incapacidades) {
                                if ((Integer.parseInt((String) array.get(27))) == idnov) {
                                    jsona.put(20, (String) array.getString(0));
                                }
                            }
                        }
                    }


                }
                incapacidades.add(jsona);
            }

            for (JSONArray array : incapacidades) {
                array.remove(colCount);
            }
            novedades.addAll(incapacidades);

            for (JSONArray array : novedades) {
                cad += array.toString().replaceAll(",", "").replaceAll("\"", "").replace("[", "").replace("]", "") + "\n";
            }

            return cad;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            BD.desconectar();
        }
    }

    public String cargarHorasExtras() throws Exception {
        try {
            BD.conectar();

            List<CGUNORegistroBeans> registros = new ArrayList<CGUNORegistroBeans>();


            String sql = "SELECT codhoraextra, to_char(codconcepto,'009') codconcepto "
                    + "  FROM rhorasextras";

            BD.callableStatement(sql);
            BD.consultar();
            ResultSet datoSql = BD.obtenerConsulta();
            while (datoSql.next()) {
                String concepto = datoSql.getString("codconcepto").trim();
                String codhoraExtra = datoSql.getString("codhoraextra");

                sql = "SELECT rpad(tp.identificacion,13,' ')identificacion, to_char(he" + codhoraExtra + ",'0000.00') \"EXTRA\" "
                        + "  FROM tprenomina tp "
                        + " WHERE he" + codhoraExtra + "> 0";
                BD.callableStatement(sql);
                BD.consultar();
                ResultSet datoSqlInt = BD.obtenerConsulta();
                CGUNORegistroBeans bean;
                while (datoSqlInt.next()) {
                    bean = new CGUNORegistroBeans();
                    bean.setSucursal(parametros.get(12));
                    bean.setCentroOperacion(parametros.get(13));
                    bean.setCentroCosto(parametros.get(14));

                    bean.setCodigo(datoSqlInt.getString("identificacion"));
                    bean.setConcepto(concepto);
                    bean.setFechaMovimiento("        ");
                    bean.setFechaInicialNoLaborado("        ");
                    bean.setFechaFinalNoLaborado("        ");
                    bean.setDiasNoLaborado("000");
                    bean.setCantidadDestajo("        ");
                    bean.setUbicacionDestajo("        ");
                    bean.setHorasNovedad(datoSqlInt.getString("EXTRA").replace(".", "").trim() + "+");
                    bean.setValorNovedad("0000000000000+");
                    bean.setCantidadDestajo("0000000+");
                    bean.setCuota("   ");
                    bean.setFechaConceptosPrima("        ");
                    bean.setCedula(datoSqlInt.getString("identificacion").replace(" ", "0"));
                    bean.setProyecto("          ");
                    registros.add(bean);

                }
            }
            String cad = "";
            for (CGUNORegistroBeans cGUNORegistroBeans : registros) {
                cad += cGUNORegistroBeans.toString() + "\n";

            }

            return cad;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            BD.desconectar();
        }
    }

    private void cargarConceptos() throws Exception {
        try {
            BD.conectar();

            String sql = "SELECT codconcepto, desconcepto, modo, tipo FROM RCONCEPTOS";
            BD.callableStatement(sql);

            BD.consultar();
            ResultSet datoSql = BD.obtenerConsulta();
            ConceptoBeans cp;
            while (datoSql.next()) {
                cp = new ConceptoBeans();
                cp.setCodConcepto(datoSql.getString(1));
                cp.setDesConcepto(datoSql.getString(2));
                cp.setModo(datoSql.getInt(3));
                cp.setTipo(datoSql.getInt(4));
                conceptos.put(cp.getCodConcepto(), cp);
            }

        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            BD.desconectar();
        }
    }

    private void cargarVariables() throws Exception {
        try {
            BD.conectar();
            String sql = "SELECT CODPARAMETRO, VALPARAMETRO FROM PPARAMETROS WHERE CODPARAMETRO IN (12,13,14,17)";

            BD.callableStatement(sql);
            BD.consultar();
            ResultSet datoSql = BD.obtenerConsulta();

            while (datoSql.next()) {
                parametros.put(datoSql.getInt(1), datoSql.getString(2));
            }

        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            BD.desconectar();
        }
    }
}
