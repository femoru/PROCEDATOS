/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.co.sio.java.dao;

import com.co.sio.java.db.ControllerPool;
import com.co.sio.java.mbeans.PermisosPerfilesBeans;
import com.co.sio.java.mbeans.UsuarioBeans;
import java.sql.ResultSet;

/**
 *
 * @author fmoctezuma
 */
public class PermisoPerfilDao {

    private ControllerPool BD;

    public PermisoPerfilDao() {
        BD = new ControllerPool();
    }

    public PermisosPerfilesBeans consultar(PermisosPerfilesBeans permisosPerfilesBeans) throws Exception {
        PermisosPerfilesBeans temp = new PermisosPerfilesBeans();
        try {
            String sql;
            ResultSet datoSql;
            sql = "SELECT * "
                    + "FROM cpermisosperfiles "
                    + "WHERE codpermiso=? "
                    + "AND codperfil = ? ";

            BD.callableStatement(sql);
            BD.AsignarParametro(1, permisosPerfilesBeans.getCodpermiso(), 2);
            BD.AsignarParametro(2, permisosPerfilesBeans.getCodperfil(), 2);
            if (!BD.consultar()) {
                throw new Exception("Error Realizando la consulta " + sql);
            } else {
                datoSql = BD.obtenerConsulta();
                if (datoSql.next()) {
                    temp.setCodperfil(datoSql.getString("codperfil"));
                    temp.setCodpermiso(datoSql.getString("codpermiso"));
                    UsuarioBeans digita = new UsuarioBeans();
                    digita.setIdusuario(datoSql.getInt("idusuario"));
                    temp.setUsuario(digita);
                }
            }
            return temp;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            BD.desconectar();
        }        
    }

    public boolean Guardar(PermisosPerfilesBeans permisosPerfilesBeans) throws Exception {
        try {
            String sql = "INSERT INTO cpermisosperfiles (codpermiso, codperfil, idusuario, fechadigita)"
                    + "VALUES (?, ?, ?, SYSDATE)";
            BD.conectar();
            BD.callableStatement(sql);
            BD.AsignarParametro(1, permisosPerfilesBeans.getCodpermiso(), 2);
            BD.AsignarParametro(2, permisosPerfilesBeans.getCodperfil(), 2);
            BD.AsignarParametro(3, Integer.toString(permisosPerfilesBeans.getUsuario().getIdusuario()), 2);
            return BD.registrar();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            BD.desconectar();
        }
    }

    public boolean Eliminar(String codperfil) throws Exception {
        try {
            String sql = "DELETE FROM cpermisosperfiles WHERE codperfil = ?";
            BD.conectar();
            BD.callableStatement(sql);
            BD.AsignarParametro(1, codperfil, 2);
            return BD.registrar();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            BD.desconectar();
        }
    }
}
