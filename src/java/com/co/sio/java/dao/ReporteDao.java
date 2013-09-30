/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.co.sio.java.dao;

import com.co.sio.java.JSON.JSONArray;
import com.co.sio.java.JSON.JSONObject;
import com.co.sio.java.db.ControllerPool;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

/**
 *
 * @author fmoctezuma
 */
public class ReporteDao {

    ControllerPool BD;

    public ReporteDao() {
        BD = new ControllerPool();
    }

    public String getListFacturacion(int page, int rows) throws Exception {
        try {

            String sql = "SELECT COUNT(*) AS valor FROM plaborescontratos";

            BD.conectar();
            BD.callableStatement(sql);
            BD.consultar();

            ResultSet rsCuenta = BD.obtenerConsulta();

            int total = 0, total1 = 0, total_pages;

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

            sql = "SELECT   plc.idlaborcontrato,nomcliente AS cliente, desarea AS area, desgrupo AS grupo,\n"
                    + "         deslabor AS labor, destipolabor AS tipo,\n"
                    + "         NVL (deshoraextra, ' ') AS extra\n"
                    + "    FROM plaborescontratos plc INNER JOIN rlabores rl\n"
                    + "         ON rl.codlabor = plc.codlabor\n"
                    + "         INNER JOIN rtipolabor rtl ON rtl.codtipolabor = plc.codtipolabor\n"
                    + "         LEFT JOIN rhorasextras rhe ON rhe.codhoraextra = plc.codhoraextra\n"
                    + "         INNER JOIN dareasgrupos dag ON dag.idgrupo = plc.idgrupo\n"
                    + "         INNER JOIN dclientesareas dca ON dca.idarea = dag.idarea\n"
                    + "         INNER JOIN mclientes mc ON mc.idcliente = dca.idcliente\n"
                    + "         INNER JOIN rareas ra ON ra.codarea = dca.codarea\n"
                    + "ORDER BY 2, 3, 4, 5, 6, 7";
            BD.callableStatement(sql);

            BD.consultar();
            rsCuenta = BD.obtenerConsulta();
            total = total1;

            ResultSetMetaData mtd = rsCuenta.getMetaData();

            int totalColumnas = mtd.getColumnCount();

            JSONObject jsonData = new JSONObject();

            jsonData.put("page", page);
            jsonData.put("total", total_pages);
            jsonData.put("records", total);
            JSONArray jsonRows = new JSONArray();

            int countColumn = 2;
            JSONObject jsono;
            JSONArray jsona;
            while (rsCuenta.next()) {
                jsono = new JSONObject();
                jsona = new JSONArray();
                jsono.put("id", rsCuenta.getString(1));
                while (countColumn <= totalColumnas) {
                    jsona.put(rsCuenta.getString(countColumn++));
                }
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

    public String consolidadoFacturacion(String labores, int idnomina) throws Exception {
        try {

            Connection cox = ControllerPool.getDs().getConnection();

            String sql = "SELECT   nomcliente AS cliente, desarea AS area, desgrupo AS grupo,\n"
                    + "         deslabor AS labor,\n"
                    + "         destipolabor || ' ' || NVL (deshoraextra, '') AS tipo,\n"
                    + "         identificacion,\n"
                    + "            pnombre\n"
                    + "         || ' '\n"
                    + "         || snombre\n"
                    + "         || ' '\n"
                    + "         || papellido\n"
                    + "         || ' '\n"
                    + "         || sapellido AS \"NOMBRE\",\n"
                    + "         CASE\n"
                    + "            WHEN SUM (TRUNC (mr.tiempolabor / 60, 2)) <= 192\n"
                    + "               THEN SUM (TRUNC (mr.tiempolabor / 60, 2))\n"
                    + "            WHEN SUM (TRUNC (mr.tiempolabor / 60, 2)) > 192\n"
                    + "               THEN 192\n"
                    + "         END AS cantidad,\n"
                    + "         CASE\n"
                    + "            WHEN SUM (TRUNC (mr.tiempolabor / 60, 2)) <= 192\n"
                    + "               THEN 0\n"
                    + "            WHEN SUM (TRUNC (mr.tiempolabor / 60, 2)) > 192\n"
                    + "               THEN SUM (TRUNC (mr.tiempolabor / 60, 2)) - 192\n"
                    + "         END AS cantidad2\n"
                    + "    FROM mregistros mr INNER JOIN plaborescontratos plc\n"
                    + "         ON plc.idlaborcontrato = mr.idlaborcontrato\n"
                    + "         INNER JOIN mpersonas mp ON mp.idpersona = mr.idusuario\n"
                    + "         INNER JOIN rlabores rl ON rl.codlabor = plc.codlabor\n"
                    + "         INNER JOIN rtipolabor rtl ON rtl.codtipolabor = plc.codtipolabor\n"
                    + "         LEFT JOIN rhorasextras rhe ON rhe.codhoraextra = plc.codhoraextra\n"
                    + "         INNER JOIN dareasgrupos dag ON dag.idgrupo = plc.idgrupo\n"
                    + "         INNER JOIN dclientesareas dca ON dca.idarea = dag.idarea\n"
                    + "         INNER JOIN mclientes mc ON mc.idcliente = dca.idcliente\n"
                    + "         INNER JOIN rareas ra ON ra.codarea = dca.codarea\n"
                    + "   WHERE (plc.codtipolabor = 1 OR plc.codtipolabor = 2)\n"
                    + "     AND idnomina = %s and mr.anulado = 0\n"
                    + "	AND plc.idlaborcontrato IN ( %s )\n"
                    + "GROUP BY nomcliente,\n"
                    + "         desarea,\n"
                    + "         desgrupo,\n"
                    + "         deslabor,\n"
                    + "         destipolabor,\n"
                    + "         deshoraextra,\n"
                    + "         identificacion,\n"
                    + "         pnombre,\n"
                    + "         snombre,\n"
                    + "         papellido,\n"
                    + "         sapellido\n"
                    + "ORDER BY desarea, grupo, labor, tipo, nombre";
            sql = String.format(sql, idnomina, labores);
            String reporte = "ConsolidadoFacturacion";
            JasperPrint generarReporte = generarReporte(reporte, sql);

            return exportarExcel(generarReporte);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }

    }

    public String detalladoFacturacion(String labores, int idnomina) throws Exception {
        try {

            Connection cox = ControllerPool.getDs().getConnection();

            String sql = "SELECT   nomcliente as CLIENTE, desarea AS AREA, desgrupo AS GRUPO, deslabor AS LABOR,\n"
                    + "         destipolabor || ' ' || NVL (deshoraextra, '') AS TIPO, identificacion,\n"
                    + "            pnombre\n"
                    + "         || ' '\n"
                    + "         || snombre\n"
                    + "         || ' '\n"
                    + "         || papellido\n"
                    + "         || ' '\n"
                    + "         || sapellido AS \"NOMBRE\",TO_CHAR(FECHAINICIO,'DD/MM/YYYY') FECHA,TO_CHAR(FECHAINICIO,'HH24:MI')H_INI,TO_CHAR(FECHAFIN,'HH24:MI')H_FIN,\n"
                    + "         TRUNC (mr.tiempolabor / 60, 2) AS cantidad\n"
                    + "    FROM mregistros mr INNER JOIN plaborescontratos plc\n"
                    + "         ON plc.idlaborcontrato = mr.idlaborcontrato\n"
                    + "         INNER JOIN mpersonas mp ON mp.idpersona = mr.idusuario\n"
                    + "         INNER JOIN rlabores rl ON rl.codlabor = plc.codlabor\n"
                    + "         INNER JOIN rtipolabor rtl ON rtl.codtipolabor = plc.codtipolabor\n"
                    + "         LEFT JOIN rhorasextras rhe ON rhe.codhoraextra = plc.codhoraextra\n"
                    + "         INNER JOIN dareasgrupos dag ON dag.idgrupo = plc.idgrupo\n"
                    + "         INNER JOIN dclientesareas dca ON dca.idarea = dag.idarea\n"
                    + "         INNER JOIN mclientes mc ON mc.idcliente = dca.idcliente\n"
                    + "         INNER JOIN rareas ra ON ra.codarea = dca.codarea\n"
                    + "   WHERE (plc.codtipolabor = 1 OR plc.codtipolabor = 2 )\n"
                    + "     AND idnomina = %s and mr.anulado = 0\n"
                    + "	    AND plc.idlaborcontrato IN ( %s )\n"
                    + "ORDER BY desarea,deslabor,TIPO,NOMBRE,FECHAINICIO";
            sql = String.format(sql, idnomina, labores);
            String reporte = "DetalladoFacturacion";
            JasperPrint generarReporte = generarReporte(reporte, sql);

            return exportarExcel(generarReporte);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }

    }

    public String consolidadoNomina(String labores, int idnomina) throws Exception {
        try {

            String sql = "SELECT   nomcliente AS cliente, desarea AS area, desgrupo AS grupo,\n"
                    + "         deslabor AS labor,\n"
                    + "         destipolabor || ' ' || NVL (deshoraextra, ' ') AS tipo,\n"
                    + "         identificacion,\n"
                    + "            pnombre\n"
                    + "         || ' '\n"
                    + "         || snombre\n"
                    + "         || ' '\n"
                    + "         || papellido\n"
                    + "         || ' '\n"
                    + "         || sapellido AS \"NOMBRE\",\n"
                    + "         plc.valor, SUM(mr.registroslabor) AS cantidad,\n"
                    + "         SUM(TRUNC (mr.registroslabor * mr.valor, 2)) produccion\n"
                    + "    FROM mregistros mr INNER JOIN plaborescontratos plc\n"
                    + "         ON plc.idlaborcontrato = mr.idlaborcontrato\n"
                    + "         INNER JOIN mpersonas mp ON mp.idpersona = mr.idusuario\n"
                    + "         INNER JOIN rlabores rl ON rl.codlabor = plc.codlabor\n"
                    + "         INNER JOIN rtipolabor rtl ON rtl.codtipolabor = plc.codtipolabor\n"
                    + "         LEFT JOIN rhorasextras rhe ON rhe.codhoraextra = plc.codhoraextra\n"
                    + "         INNER JOIN dareasgrupos dag ON dag.idgrupo = plc.idgrupo\n"
                    + "         INNER JOIN dclientesareas dca ON dca.idarea = dag.idarea\n"
                    + "         INNER JOIN mclientes mc ON mc.idcliente = dca.idcliente\n"
                    + "         INNER JOIN rareas ra ON ra.codarea = dca.codarea\n"
                    + "   WHERE (plc.codtipolabor = 3 OR plc.codtipolabor = 4 OR plc.codtipolabor = 5 )\n"
                    + "     AND idnomina = %s and mr.anulado = 0\n"
                    + "	    AND plc.idlaborcontrato IN ( %s )\n"
                    + "     GROUP BY nomcliente,\n"
                    + "         desarea,\n"
                    + "         desgrupo,\n"
                    + "         deslabor,\n"
                    + "         destipolabor,\n"
                    + "         deshoraextra,\n"
                    + "         identificacion,\n"
                    + "         pnombre,\n"
                    + "         snombre,\n"
                    + "         papellido,\n"
                    + "         sapellido,\n"
                    + "         plc.valor\n"
                    + "UNION ALL\n"
                    + "SELECT   nomcliente AS cliente, desarea AS area, desgrupo AS grupo,\n"
                    + "         deslabor AS labor,\n"
                    + "         destipolabor || ' ' || NVL (deshoraextra, ' ') AS tipo,\n"
                    + "         identificacion,\n"
                    + "            pnombre\n"
                    + "         || ' '\n"
                    + "         || snombre\n"
                    + "         || ' '\n"
                    + "         || papellido\n"
                    + "         || ' '\n"
                    + "         || sapellido AS \"NOMBRE\",\n"
                    + "         plc.valor, SUM(TRUNC (mr.tiempolabor / 60, 2)) AS cantidad,\n"
                    + "         SUM(TRUNC (TRUNC (mr.tiempolabor / 60, 2) * mr.valor, 2)) produccion\n"
                    + "    FROM mregistros mr INNER JOIN plaborescontratos plc\n"
                    + "         ON plc.idlaborcontrato = mr.idlaborcontrato\n"
                    + "          INNER JOIN mpersonas mp ON mp.idpersona = mr.idusuario\n"
                    + "         INNER JOIN rlabores rl ON rl.codlabor = plc.codlabor\n"
                    + "         INNER JOIN rtipolabor rtl ON rtl.codtipolabor = plc.codtipolabor\n"
                    + "         LEFT JOIN rhorasextras rhe ON rhe.codhoraextra = plc.codhoraextra\n"
                    + "         INNER JOIN dareasgrupos dag ON dag.idgrupo = plc.idgrupo\n"
                    + "         INNER JOIN dclientesareas dca ON dca.idarea = dag.idarea\n"
                    + "         INNER JOIN mclientes mc ON mc.idcliente = dca.idcliente\n"
                    + "         INNER JOIN rareas ra ON ra.codarea = dca.codarea\n"
                    + "   WHERE (plc.codtipolabor = 1 OR plc.codtipolabor = 2)\n"
                    + "     AND idnomina = %s and mr.anulado = 0\n"
                    + "	    AND plc.idlaborcontrato IN ( %s )\n"
                    + "     GROUP BY nomcliente,\n"
                    + "         desarea,\n"
                    + "         desgrupo,\n"
                    + "         deslabor,\n"
                    + "         destipolabor,\n"
                    + "         deshoraextra,\n"
                    + "         identificacion,\n"
                    + "         pnombre,\n"
                    + "         snombre,\n"
                    + "         papellido,\n"
                    + "         sapellido,\n"
                    + "         plc.valor\n"
                    + "ORDER BY AREA,GRUPO, LABOR,NOMBRE,TIPO";
            sql = String.format(sql, idnomina, labores, idnomina, labores);

            String reporte = "ConsolidadoNomina";
            JasperPrint generarReporte = generarReporte(reporte, sql);

            return exportarExcel(generarReporte);

        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    public String detalladoNomina(String labores, int idnomina) throws Exception {
        try {

            String sql = "SELECT   nomcliente AS cliente, desarea AS area, desgrupo AS grupo,\n"
                    + "         deslabor AS labor,\n"
                    + "         destipolabor || ' ' || NVL (deshoraextra, ' ') AS tipo,\n"
                    + "         identificacion,\n"
                    + "            pnombre\n"
                    + "         || ' '\n"
                    + "         || snombre\n"
                    + "         || ' '\n"
                    + "         || papellido\n"
                    + "         || ' '\n"
                    + "         || sapellido AS \"NOMBRE\",TO_CHAR(FECHAINICIO,'DD/MM/YYYY') FECHA,TO_CHAR(FECHAINICIO,'HH24:MI')H_INI,TO_CHAR(FECHAFIN,'HH24:MI')H_FIN,\n"
                    + "         plc.valor, mr.registroslabor AS cantidad,\n"
                    + "         TRUNC (mr.registroslabor * mr.valor, 2) produccion\n"
                    + "    FROM mregistros mr INNER JOIN plaborescontratos plc\n"
                    + "         ON plc.idlaborcontrato = mr.idlaborcontrato\n"
                    + "         INNER JOIN mpersonas mp ON mp.idpersona = mr.idusuario\n"
                    + "         INNER JOIN rlabores rl ON rl.codlabor = plc.codlabor\n"
                    + "         INNER JOIN rtipolabor rtl ON rtl.codtipolabor = plc.codtipolabor\n"
                    + "         LEFT JOIN rhorasextras rhe ON rhe.codhoraextra = plc.codhoraextra\n"
                    + "         INNER JOIN dareasgrupos dag ON dag.idgrupo = plc.idgrupo\n"
                    + "         INNER JOIN dclientesareas dca ON dca.idarea = dag.idarea\n"
                    + "         INNER JOIN mclientes mc ON mc.idcliente = dca.idcliente\n"
                    + "         INNER JOIN rareas ra ON ra.codarea = dca.codarea\n"
                    + "   WHERE (plc.codtipolabor = 3 OR plc.codtipolabor = 4 OR plc.codtipolabor = 5 )\n"
                    + "     AND idnomina = %s and mr.anulado = 0\n"
                    + "	    AND plc.idlaborcontrato IN ( %s )\n"
                    + "UNION ALL\n"
                    + "SELECT   nomcliente AS cliente, desarea AS area, desgrupo AS grupo,\n"
                    + "         deslabor AS labor,\n"
                    + "         destipolabor || ' ' || NVL (deshoraextra, ' ') AS tipo,\n"
                    + "         identificacion,\n"
                    + "            pnombre\n"
                    + "         || ' '\n"
                    + "         || snombre\n"
                    + "         || ' '\n"
                    + "         || papellido\n"
                    + "         || ' '\n"
                    + "         || sapellido AS \"NOMBRE\",TO_CHAR(FECHAINICIO,'DD/MM/YYYY') FECHA,TO_CHAR(FECHAINICIO,'HH24:MI')H_INI,TO_CHAR(FECHAFIN,'HH24:MI')H_FIN,\n"
                    + "         plc.valor, TRUNC (mr.tiempolabor / 60, 2) AS cantidad,\n"
                    + "         TRUNC (TRUNC (mr.tiempolabor / 60, 2) * mr.valor, 2) produccion\n"
                    + "    FROM mregistros mr INNER JOIN plaborescontratos plc\n"
                    + "         ON plc.idlaborcontrato = mr.idlaborcontrato\n"
                    + "          INNER JOIN mpersonas mp ON mp.idpersona = mr.idusuario\n"
                    + "         INNER JOIN rlabores rl ON rl.codlabor = plc.codlabor\n"
                    + "         INNER JOIN rtipolabor rtl ON rtl.codtipolabor = plc.codtipolabor\n"
                    + "         LEFT JOIN rhorasextras rhe ON rhe.codhoraextra = plc.codhoraextra\n"
                    + "         INNER JOIN dareasgrupos dag ON dag.idgrupo = plc.idgrupo\n"
                    + "         INNER JOIN dclientesareas dca ON dca.idarea = dag.idarea\n"
                    + "         INNER JOIN mclientes mc ON mc.idcliente = dca.idcliente\n"
                    + "         INNER JOIN rareas ra ON ra.codarea = dca.codarea\n"
                    + "   WHERE (plc.codtipolabor = 1 OR plc.codtipolabor = 2)\n"
                    + "     AND idnomina = %s and mr.anulado = 0\n"
                    + "	    AND plc.idlaborcontrato IN ( %s )\n"
                    + "ORDER BY AREA,GRUPO, LABOR,NOMBRE, TIPO,FECHA";
            sql = String.format(sql, idnomina, labores, idnomina, labores);

            String reporte = "DetalladoNomina";
            JasperPrint generarReporte = generarReporte(reporte, sql);
            return exportarExcel(generarReporte);

        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    private JasperPrint generarReporte(String nombreReporte, String consulta) throws Exception {
        try {
            Connection cox = ControllerPool.getDs().getConnection();

            InputStream in = getClass().getClassLoader().getResourceAsStream("../../media/reports/" + nombreReporte + ".jrxml");
            JasperDesign jasperDesign = JRXmlLoader.load(in);

            JRDesignQuery query = new JRDesignQuery();
            query.setText(consulta);
            jasperDesign.setQuery(query);

            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, cox);

            in.close();
            cox.close();

            return jasperPrint;
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    public String exportarExcel(JasperPrint jasperPrint) throws Exception {
        try {
            String directorio = getClass().getClassLoader().getResource("../../media/reports/").getPath();

            JRXlsExporter exporter = new JRXlsExporter();
            exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
            exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, directorio + jasperPrint.getName() + ".xls");
           
            exporter.exportReport();
            return jasperPrint.getName() + ".xls";
        } catch (JRException ex) {
            throw new Exception(ex.getMessage());
        }

    }
    
}
