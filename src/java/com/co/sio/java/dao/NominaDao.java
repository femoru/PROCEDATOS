/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.co.sio.java.dao;

import com.co.sio.java.JSON.JSONArray;
import com.co.sio.java.JSON.JSONObject;
import com.co.sio.java.db.ControllerPool;
import com.co.sio.java.mbeans.NominaBeans;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author fmoctezuma
 */
public class NominaDao {

    private ControllerPool BD;

    public NominaDao() {
        BD = new ControllerPool();
    }

    public JSONObject getListNomina(int pages, int rows) throws Exception {
        try {
            JSONObject jsonData = new JSONObject();

            int total = 0;

            String sql = "SELECT COUNT(*) FROM mnomina";
            BD.conectar();
            BD.callableStatement(sql);
            BD.consultar();

            ResultSet rs = BD.obtenerConsulta();
            if (rs.next()) {
                total = rs.getInt(1);
            }


            jsonData.put("page", pages);
            jsonData.put("records", total);
            if (total > 0) {
                double d = Math.ceil((double) (total) / (double) (rows));
                jsonData.put("total", (int) (d));
            } else {
                jsonData.put("total", 0);
            }


            sql = "SELECT idnomina, "
                    + "          TO_CHAR (fechainicio, 'dd/mm/yyyy') "
                    + "       || ' - ' "
                    + "       || TO_CHAR (fechafin, 'dd/mm/yyyy') rango, "
                    + "       estado "
                    + "  FROM mnomina";
            BD.callableStatement(sql);
            BD.consultar();

            rs = BD.obtenerConsulta();
            ResultSetMetaData rsmt = rs.getMetaData();

            int countColumns = rsmt.getColumnCount();

            JSONArray jsonRows = new JSONArray();
            JSONObject jsono;
            JSONArray jsona;
            while (rs.next()) {
                jsono = new JSONObject();
                jsona = new JSONArray();

                jsono.put("id", rs.getString(1));
                for (int i = 1; i <= countColumns; i++) {
                    jsona.put(rs.getString(i));
                }
                jsono.put("cell", jsona);
                jsonRows.put(jsono);
            }
            jsonData.put("rows", jsonRows);

            return jsonData;
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        } finally {
            BD.desconectar();
        }
    }

    public List listar() throws Exception {
        try {
            ArrayList<NominaBeans> lista = new ArrayList<NominaBeans>();


            String sql = "SELECT idnomina, TO_CHAR(fechainicio,'DD/MM/YYYY'), TO_CHAR(fechafin,'DD/MM/YYYY') FROM mnomina order by fechainicio desc ";
            BD.conectar();
            BD.callableStatement(sql);
            BD.consultar();

            ResultSet datoSql = BD.obtenerConsulta();
            NominaBeans nominaBeans;
            while (datoSql.next()) {
                nominaBeans = new NominaBeans();
                nominaBeans.setId(datoSql.getInt(1));
                nominaBeans.setFechaInicio(datoSql.getString(2));
                nominaBeans.setFechaFin(datoSql.getString(3));

                lista.add(nominaBeans);
            }



            return lista;
        } catch (SQLException ex) {
            throw new Exception(ex.getMessage());
        } finally {
            BD.desconectar();
        }

    }
}
