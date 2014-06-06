/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.co.sio.java.dao;

import com.co.sio.java.db.ControllerPool;
import com.co.sio.java.mbeans.PerfilBeans;
import com.co.sio.java.mbeans.UsuarioBeans;
import com.co.sio.java.utils.SeguridadUtils;
import java.sql.ResultSet;

/**
 *
 * @author jcarvajal
 */
public class LoginDao {

    private ControllerPool BD;

    public LoginDao() {
        BD = new ControllerPool();
    }

    public UsuarioBeans validarLogin(String user, String pass) throws Exception {
        UsuarioBeans usuario = new UsuarioBeans();
        UsuarioDao daousuario = new UsuarioDao();

        try {
            String sql;
            ResultSet datoSql;
            BD.conectar();

            sql = "SELECT usu.idusuario, usu.login,usu.clave, "
                    + "mper.pnombre || ' ' || mper.snombre || ' ' || mper.papellido || ' ' || mper.sapellido nombre, "
                    + "usu.activo, per.codperfil ,per.menu, per.activo, to_char(per.fechadigita,'dd/MM/yyyy hh24:mm:ss') "
                    + "FROM cusuarios usu, cperfiles per, mpersonas mper "
                    + "WHERE usu.codperfil = per.codperfil "
                    + "AND mper.idpersona = usu.IDUSUARIO "
                    + "AND usu.login = ? AND usu.clave = ? ";

            BD.callableStatement(sql);
            BD.AsignarParametro(1, user, 1);
            BD.AsignarParametro(2, SeguridadUtils.encripta(pass), 1);

            if (!BD.consultar()) {
                throw new Exception("Error Realizando La Consulta: " + sql);
            }
            datoSql = BD.obtenerConsulta();

            if (datoSql.next()) {
                usuario.setIdusuario(datoSql.getInt(1));
                usuario.setLogin(datoSql.getString(2));
                usuario.setClave(datoSql.getString(3));
                usuario.setActivo(datoSql.getInt(5));

                PerfilBeans perfil = new PerfilBeans();
                perfil.setCod_perfil(datoSql.getInt(6));
                perfil.setMenu(datoSql.getString(7));
                perfil.setEstado(datoSql.getInt(8));
                perfil.setFecha_digita(datoSql.getString(9));
                usuario.setPerfil(perfil);

                if (1 != usuario.getActivo()) {
                    usuario.setMensaje("Usuario Inactivo en el Sistema");
                }

                if (1 != perfil.getEstado()) {
                    usuario = new UsuarioBeans();
                    usuario.setMensaje("El perfil al que pertenece esta inactivo.\nPor favor comuniquese con el Coordinador.");
                }

                daousuario.IngresoSistema(usuario);
            } else {
                usuario.setMensaje("Usuario o Clave Invalida");
            }
            return usuario;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            BD.desconectar();
        }
    }


    public Object[] registraAsistencia(int idUsuario) throws Exception{
        try {
            String mSql;
            Object []resultado = new Object[2];

            if(asistenciaAbierta(idUsuario)){
                mSql = "UPDATE dasistencia SET fechasalida = SYSDATE WHERE idusuario = ? "
                        + " AND fechasalida is null AND TRUNC(fechaingreso) = TRUNC(SYSDATE)";
                resultado[1] = "out";
            }else{
                mSql = "INSERT INTO dasistencia (idusuario,fechaingreso) VALUES ( ? , SYSDATE)";
                resultado[1] = "in";
            }
            BD.conectar();
            BD.callableStatement(mSql);
            BD.AsignarParametro(1, Integer.toString(idUsuario) , 2);
            resultado[0] = BD.registrar();

            return resultado;

        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }finally{
            BD.desconectar();
        }
    }
            
    public boolean asistenciaAbierta(int idUsuario) throws Exception{
        try{
            String mSql="UPDATE dasistencia SET fechasalida = TRUNC(fechaingreso) + 1 "
                    + "WHERE idusuario = ? AND fechasalida is null "
                    + "AND TRUNC(fechaingreso) <> TRUNC(SYSDATE)";
             BD.conectar();
            BD.callableStatement(mSql);
            BD.AsignarParametro(1, Integer.toString(idUsuario) , 2);
            BD.registrar();
                    
                    
            mSql = "SELECT idasistencia,idusuario,fechaingreso,fechasalida,tiempolaborado "
            + "FROM dasistencia da WHERE idusuario = ? AND fechasalida is null AND TRUNC(fechaingreso) = TRUNC(SYSDATE)";
            BD.conectar();
            BD.callableStatement(mSql);
            BD.AsignarParametro(1, Integer.toString(idUsuario) , 2);
            BD.consultar();
            ResultSet datoSql = BD.obtenerConsulta();

            return  datoSql.next();
            
        }catch(Exception ex){
            throw new Exception(ex.getMessage());
        }finally{
            BD.desconectar();
        }
            
    }
}
