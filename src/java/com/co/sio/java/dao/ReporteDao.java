/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.co.sio.java.dao;

import com.co.sio.java.JSON.JSONArray;
import com.co.sio.java.JSON.JSONObject;
import com.co.sio.java.db.ControllerPool;
import com.co.sio.java.utils.Utils;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

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

    public String detalladoNomina(String labores, String fechaInicial, String fechaFinal) throws Exception {
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
                    + "     AND TRUNC(fechainicio) BETWEEN TO_DATE('%s','DD/MM/YYYY')\n"
                    + "     AND TO_DATE('%s','DD/MM/YYYY') AND mr.anulado = 0\n"
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
                    + "     AND TRUNC(fechainicio) BETWEEN TO_DATE('%s','DD/MM/YYYY')\n"
                    + "     AND TO_DATE('%s','DD/MM/YYYY') AND mr.anulado = 0\n"
                    + "	    AND plc.idlaborcontrato IN ( %s )\n"
                    + "ORDER BY AREA,GRUPO, LABOR,NOMBRE, TIPO,FECHA";
            sql = String.format(sql, fechaInicial, fechaFinal, labores, fechaInicial, fechaFinal, labores);
            System.out.println(sql);
            String reporte = "DetalladoNomina";
            JasperPrint generarReporte = generarReporte(reporte, sql);
            return exportarExcel(generarReporte);

        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    private JasperPrint generarReporte(String nombreReporte, String consulta) throws Exception {
        Connection cox = ControllerPool.getDs().getConnection();
        try {


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
        } finally {
            if (!cox.isClosed()) {
                cox.close();
            }
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

    public String exportarHtml(JasperPrint jasperPrint) throws Exception {
        try {
            String directorio = getClass().getClassLoader().getResource("../../media/reports/").getPath();

            JasperExportManager.exportReportToHtmlFile(jasperPrint, directorio + jasperPrint.getName() + ".html");

            return jasperPrint.getName() + ".html";
        } catch (JRException ex) {
            throw new Exception(ex.getMessage());
        }

    }

    public String consolidadoFacturacion(String labores, String fini, String ffin) throws Exception {
        try {
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
                    + "     AND TRUNC(fechainicio) BETWEEN TO_DATE('%s','DD/MM/YYYY')\n"
                    + "     AND TO_DATE('%s','DD/MM/YYYY')  and mr.anulado = 0\n"
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
            sql = String.format(sql, ffin, ffin, labores);
            String reporte = "ConsolidadoFacturacion";
            JasperPrint generarReporte = generarReporte(reporte, sql);

            return exportarExcel(generarReporte);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    public String detalladoFacturacion(String labores, String fini, String ffin) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public String consolidadoNomina(String labores, String fini, String ffin) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public String estadoUsuarios() throws Exception {

        String sql = "SELECT   identificacion,\n"
                + "            papellido\n"
                + "         || ' '\n"
                + "         || sapellido\n"
                + "         || ' '\n"
                + "         || pnombre\n"
                + "         || ' '\n"
                + "         || snombre AS nombres,\n"
                + "         DECODE (cu.activo, 1, 'ACTIVO', 'INACTIVO') AS estado\n"
                + "    FROM mpersonas INNER JOIN cusuarios cu ON idpersona = cu.idusuario\n"
                + "   WHERE codperfil = 4\n"
                + "ORDER BY activo DESC, TRIM (papellido)";

        String reporte = "EstadoUsuarios";
        JasperPrint generarReporte = generarReporte(reporte, sql);

        return exportarExcel(generarReporte);
    }

    public String getListUsuario(int page, int rows) throws Exception {
        try {

            String sql = "SELECT COUNT(*) AS valor "
                    + "    FROM mpersonas INNER JOIN cusuarios cu ON idpersona = cu.idusuario\n"
                    + "   WHERE codperfil = 4\n";

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

            sql = "SELECT   idpersona, identificacion,\n"
                    + "            papellido\n"
                    + "         || ' '\n"
                    + "         || sapellido\n"
                    + "         || ' '\n"
                    + "         || pnombre\n"
                    + "         || ' '\n"
                    + "         || snombre AS nombres,\n"
                    + "         DECODE (cu.activo, 1, 'ACTIVO', 'INACTIVO') AS estado\n"
                    + "    FROM mpersonas INNER JOIN cusuarios cu ON idpersona = cu.idusuario\n"
                    + "   WHERE codperfil = 4\n"
                    + "ORDER BY activo DESC, TRIM (papellido)";
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

    public String reporteFacturacion(String labores, int idnomina) throws Exception {
        String directorio = getClass().getClassLoader().getResource("../../media/reports/").getPath();

        File archivo = new File(directorio + "FACTURACION.xls");
        try {
            String sql = "SELECT   desarea, desgrupo, deslabor, destipolabor, NVL(deshoraextra,' '),\n"
                    + "         identificacion,\n"
                    + "            pnombre\n"
                    + "         || ' '\n"
                    + "         || snombre\n"
                    + "         || ' '\n"
                    + "         || papellido\n"
                    + "         || ' '\n"
                    + "         || sapellido AS nombres,\n"
                    + "         SUM (TRUNC (mr.tiempolabor / 60, 2)) AS cantidad\n"
                    + "    FROM mregistros mr INNER JOIN plaborescontratos plc\n"
                    + "         ON plc.idlaborcontrato = mr.idlaborcontrato\n"
                    + "         INNER JOIN mpersonas mp ON mp.idpersona = mr.idusuario\n"
                    + "         INNER JOIN rlabores rl ON rl.codlabor = plc.codlabor\n"
                    + "         INNER JOIN rtipolabor rtl ON rtl.codtipolabor = plc.codtipolabor\n"
                    + "         LEFT JOIN rhorasextras rhe ON rhe.codhoraextra = plc.codhoraextra\n"
                    + "         INNER JOIN dareasgrupos dag ON dag.idgrupo = plc.idgrupo\n"
                    + "         INNER JOIN dclientesareas dca ON dca.idarea = dag.idarea\n"
                    + "         INNER JOIN rareas ra ON ra.codarea = dca.codarea\n"
                    + "   WHERE (plc.codtipolabor = 1 OR plc.codtipolabor = 2)\n"
                    + "     AND mr.idnomina = %s\n"
                    + "     AND mr.anulado = 0\n"
                    + "     AND plc.idlaborcontrato IN ( %s )\n"
                    + "GROUP BY desarea,\n"
                    + "         desgrupo,\n"
                    + "         deslabor,\n"
                    + "         destipolabor,\n"
                    + "         deshoraextra,\n"
                    + "         identificacion,\n"
                    + "         pnombre,\n"
                    + "         snombre,\n"
                    + "         papellido,\n"
                    + "         sapellido\n"
                    + "ORDER BY desarea";
            sql = String.format(sql, idnomina,labores);
            BD.conectar();
            BD.callableStatement(sql);
            BD.consultar();
            ResultSet rs = BD.obtenerConsulta();
            ResultSetMetaData rsmd = rs.getMetaData();
            int countCol = rsmd.getColumnCount();
            ArrayList<JSONArray> arrays = new ArrayList<JSONArray>();
            JSONArray jsona;
            while (rs.next()) {
                jsona = new JSONArray();
                for (int i = 1; i <= countCol; i++) {
                    jsona.put(rs.getString(i));
                }
                arrays.add(jsona);
            }
            BD.desconectar();
            JSONArray cab = new JSONArray("[AREA,GRUPO,LABOR,TIPO,EXTRA,IDENTIFICACION,NOMBRES,CANTIDAD,PTM,PTD,HORAS]");
            facturacion(arrays, cab, archivo);

            return archivo.getName();


        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    private FileOutputStream facturacion(ArrayList<JSONArray> arrays, JSONArray cabeceras, File nomArchivo) {

        try {
            //Crear el libro
            HSSFWorkbook libro = new HSSFWorkbook();
            //Crear una hoja
            HSSFSheet hoja = libro.createSheet();
            //Crear instancia de fila
            int rownum = 0;
            HSSFRow fila = hoja.createRow(rownum++);
            for (int i = 0; i < cabeceras.length(); i++) {

                fila.createCell(i).setCellValue(cabeceras.getString(i));
            }
            double cantidad;
            for (JSONArray jsona : arrays) {
                fila = hoja.createRow(rownum);

                for (int i = 0; i < jsona.length(); i++) {
                    if (!jsona.isNull(i)) {
                        if (i < 7) {
                            fila.createCell(i).setCellValue(jsona.getString(i));
                        } else {
                            fila.createCell(i).setCellValue(jsona.getDouble(i));
                        }
                    }
                }
                cantidad = jsona.getDouble(7);
                /*RANGO PTM*/
                int ptms = cantidad >= 161 ? 1 : 0;

                double restanteptms = ptms > 0 ? cantidad - 192 : cantidad;
                int ptds = (int) ((restanteptms < 0 ? 0 : restanteptms) / 8.5);
                double restanteptds = ptds * 8.5;
                double horas = restanteptms >= 0 ? restanteptms - restanteptds : 0;
                horas = Math.round(horas * 100.0) / 100.0;

                fila.createCell(8).setCellValue(ptms);
                fila.createCell(9).setCellValue(ptds);
                fila.createCell(10).setCellValue(horas);

                rownum++;
            }
//
//            while (rs.next()) {
//                fila = hoja.createRow(rownum);
//
//                for (int i = 1; i <= colTotal; i++) {
//                    if (rsmd.getColumnType(i) == java.sql.Types.VARCHAR) {
//                        fila.createCell(i - 1).setCellValue(rs.getString(i));
//                    } else if (rsmd.getColumnType(i) == java.sql.Types.NVARCHAR) {
//                        fila.createCell(i - 1).setCellValue(rs.getString(i));
//                    } else if (rsmd.getColumnType(i) == java.sql.Types.DATE) {
//                        fila.createCell(i - 1).setCellValue(rs.getString(i));
//                    } else if (rsmd.getColumnType(i) == java.sql.Types.NUMERIC) {
//                        fila.createCell(i - 1).setCellValue(rs.getDouble(i));
//                    }
//                }
//
//                rownum++;
//            }

            FileOutputStream archivo = new FileOutputStream(nomArchivo);
            libro.write(archivo);
            archivo.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
