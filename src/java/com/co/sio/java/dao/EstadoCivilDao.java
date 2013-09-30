/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.co.sio.java.dao;

import com.co.sio.java.db.ControllerPool;
import com.co.sio.java.mbeans.EstadoCivilBeans;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author fmoctezuma
 */
public class EstadoCivilDao {

    private ControllerPool BD;

    public EstadoCivilDao() {
        BD = new ControllerPool();
    }

    public List listar() throws Exception {
        EstadoCivilBeans estadobeans;
        List estadoList = new ArrayList();
        try {
            ResultSet datoSql;

            String sql = "SELECT codestadocivil, desestadocivil "
                    + "FROM restadosciviles";

            BD.conectar();
            BD.callableStatement(sql);
            BD.consultar();

            datoSql = BD.obtenerConsulta();

            while (datoSql.next()) {
                estadobeans = new EstadoCivilBeans();
                estadobeans.setCodEstadoCivil(datoSql.getInt(1));
                estadobeans.setDesEstadoCivil(datoSql.getString(2));
                estadoList.add(estadobeans);
            }
            return estadoList;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            BD.desconectar();
        }        
    }
}
