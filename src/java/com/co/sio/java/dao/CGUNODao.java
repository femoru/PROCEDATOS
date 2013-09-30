/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.co.sio.java.dao;

import com.co.sio.java.db.ControllerPool;
import com.co.sio.java.mbeans.CGUNORegistroBeans;
import com.co.sio.java.mbeans.ConceptoBeans;
import java.sql.ResultSet;
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

            String horas = new ParametroDao().consultar(8).getValparametro().trim();
            BD.conectar();

            String sql = "SELECT rpad(mp.identificacion,13,' ')identificacion, lpad(codconcepto,3,' ') codconcepto, mn.codnovedad, to_char(fechanovedad,'yyyyMMDD') fechanovedad, "
                    + "to_char(fechainicio,'yyyyMMDD') fechainicio, to_char(fechafin,'yyyyMMDD') fechafin, "
                    + "       to_char(round((fechafin-fechainicio)*" + horas + "),'0000.00') as tiempo, lpad(diasempresa,3,'0')diasempresa, "
                    + "       lpad(diasotros,3,'0')diasotros, lpad(diascompensados,3,'0')diascompensados, to_char(valor,'00000000000.00')valor "
                    + "  FROM mnovedades mn INNER JOIN mpersonas mp ON mp.idpersona = mn.idusuario "
                    + "       INNER JOIN rnovedades rn ON rn.codnovedad = mn.codnovedad "
                    + " WHERE idnomina = ? ";
            BD.callableStatement(sql);
            BD.AsignarParametro(1, Integer.toString(idnomina), 2);

            BD.consultar();
            ResultSet datoSql = BD.obtenerConsulta();
            List<CGUNORegistroBeans> registros = new ArrayList<CGUNORegistroBeans>();

            CGUNORegistroBeans bean;
            while (datoSql.next()) {
                bean = new CGUNORegistroBeans();
                bean.setSucursal(parametros.get(12));
                bean.setCentroOperacion(parametros.get(13));
                bean.setCentroCosto(parametros.get(14));

                bean.setCodigo(datoSql.getString("identificacion"));
                bean.setConcepto(datoSql.getString("codconcepto"));
                bean.setFechaMovimiento(datoSql.getString("fechanovedad"));

                if (conceptos.get(datoSql.getString("codconcepto").trim()).getTipo() == 0) {
                    bean.setFechaInicialNoLaborado(datoSql.getString("fechainicio"));
                    bean.setFechaFinalNoLaborado(datoSql.getString("fechafin"));
                    bean.setDiasNoLaborado(datoSql.getString("diasempresa"));
                } else {
                    bean.setFechaInicialNoLaborado("        ");
                    bean.setFechaFinalNoLaborado("        ");
                    bean.setDiasNoLaborado("000");
                }
                bean.setCantidadDestajo("        ");
                bean.setUbicacionDestajo("        ");

                bean.setHorasNovedad(datoSql.getString("tiempo").replace(".", "").trim() + "+");
                bean.setValorNovedad(datoSql.getString("valor").replace(".", "").trim() + "+");

                bean.setCantidadDestajo("0000000+");
                bean.setCuota("   ");
                bean.setFechaConceptosPrima("        ");
                bean.setCedula(datoSql.getString("identificacion").replace(" ", "0"));
                bean.setProyecto("          ");

                registros.add(bean);

                if (datoSql.getInt("codnovedad") == 2) {
                    if (datoSql.getInt("diascompensados") > 0) {
                        CGUNORegistroBeans bean2;
                        bean2 = bean.clone();
                        bean2.setConcepto(" 33");
                        bean2.setDiasNoLaborado(datoSql.getString("diascompensados"));
                        registros.add(bean2);
                    }
                    if (datoSql.getInt("diasotros") > 0) {
                        CGUNORegistroBeans bean3;
                        bean3 = bean.clone();
                        bean3.setConcepto(" 34");
                        bean3.setDiasNoLaborado(datoSql.getString("diasotros"));
                        registros.add(bean3);
                    }
                }
                if (datoSql.getInt("codnovedad") == 3) {
                    CGUNORegistroBeans bean2;
                    bean2 = bean.clone();
                    bean2.setConcepto(parametros.get(17));
                    bean2.setDiasNoLaborado(datoSql.getString("diasotros"));
                    registros.add(bean2);
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
