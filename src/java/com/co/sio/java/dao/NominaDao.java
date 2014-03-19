/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.co.sio.java.dao;

import com.co.sio.java.JSON.JSONArray;
import com.co.sio.java.JSON.JSONObject;
import com.co.sio.java.db.ControllerPool;
import com.co.sio.java.mbeans.NominaBeans;
import com.co.sio.java.utils.Utils;
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
            sql = "SELECT mn.idnomina,mn.idnomina,TO_CHAR (mn.fechainicio, 'dd/mm/yyyy')||' - '|| TO_CHAR (mn.fechafin, 'dd/mm/yyyy') rango,"
                    + "    SUM("
                    + "    CASE plc.codtipolabor"
                    + "        when 1 then  TRUNC (mr.tiempolabor / 60, 2) * mr.valor"
                    + "        when 2 then  TRUNC (mr.tiempolabor / 60, 2) * mr.valor"
                    + "        when 3 then  TRUNC (mr.registroslabor , 2) * mr.valor"
                    + "        when 4 then  TRUNC (mr.registroslabor , 2) * mr.valor"
                    + "        when 5 then  TRUNC (mr.registroslabor , 2) * mr.valor"
                    + "    END ) as ProduccionNomina,"
                    + "    SUM("
                    + "    CASE plc.codtipolabor"
                    + "        when 1 then  TRUNC (mr.tiempolabor / 60, 2) * mr.costo"
                    + "        when 2 then  TRUNC (mr.tiempolabor / 60, 2) * mr.costo"
                    + "        when 3 then  TRUNC (mr.registroslabor , 2) * mr.costo"
                    + "        when 4 then  TRUNC (mr.registroslabor , 2) * mr.costo"
                    + "        when 5 then  TRUNC (mr.registroslabor , 2) * mr.costo"
                    + "    END ) as CostoNomina"
                    + "    ,MN.ESTADO "
                    + "  FROM mregistros mr "
                    + "  INNER JOIN plaborescontratos plc ON plc.idlaborcontrato = mr.idlaborcontrato"
                    + "  INNER JOIN mnomina mn on mn.idnomina = mr.idnomina "
                    + " WHERE anulado = 0 group by mn.idnomina,MN.FECHAINICIO,MN.FECHAFIN,MN.ESTADO order by MN.FECHAINICIO";
            BD.callableStatement(sql);
            BD.consultar();

            rs = BD.obtenerConsulta();
          
            JSONArray jsonRows = Utils.llenarGrilla(rs);
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
