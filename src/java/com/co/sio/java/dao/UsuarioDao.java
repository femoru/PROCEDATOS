/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.co.sio.java.dao;

import com.co.sio.java.JSON.JSONArray;
import com.co.sio.java.JSON.JSONObject;
import com.co.sio.java.db.ControllerPool;
import com.co.sio.java.mbeans.PerfilBeans;
import com.co.sio.java.mbeans.UsuarioBeans;
import com.co.sio.java.utils.SeguridadUtils;
import com.co.sio.java.utils.Utils;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author rquintero
 */
public class UsuarioDao {

    private ControllerPool BD;

    public UsuarioDao() {
        BD = new ControllerPool();
    }

    public boolean actualizarFechaDigita(UsuarioBeans usuarioDigita, UsuarioBeans usuarioNuevo) throws Exception {
        try {
            String sql1 = "UPDATE MPERSONAS "
                    + "SET idusuariodigita = ? "
                    + "WHERE idusuario = ?";
            BD.conectar();
            BD.callableStatement(sql1);
            BD.AsignarParametro(1, Integer.toString(usuarioDigita.getIdusuario()), 2);
            BD.AsignarParametro(2, Integer.toString(usuarioNuevo.getIdusuario()), 2);

            return BD.registrar();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            BD.desconectar();
        }
    }

    public boolean Guardar(UsuarioBeans usuario) throws Exception {
        boolean confirmacion;
        UsuarioBeans usuarioC;
        try {
            usuarioC = consultar(usuario.getIdusuario());
            if (usuarioC.getLogin() == null) {
                confirmacion = insertar(usuario);
                return confirmacion;
            } else {
                confirmacion = actualizar(usuario);
                return confirmacion;
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }

    }

    public boolean insertar(UsuarioBeans usuario) throws Exception {
        try {
            BD.conectar();
            String sql = " INSERT INTO cusuarios(idusuario,login,clave,activo,idusuariodigita,codperfil,usuariosos) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ? )";
            BD.callableStatement(sql);
            BD.AsignarParametro(1, Integer.toString(usuario.getIdusuario()), 2);
            BD.AsignarParametro(2, usuario.getLogin(), 1);
            BD.AsignarParametro(3, SeguridadUtils.encripta(usuario.getClave()), 1);
            BD.AsignarParametro(4, Integer.toString(usuario.getActivo()), 2);
            BD.AsignarParametro(5, Integer.toString(usuario.getIdUsuarioDigita()), 2);
            BD.AsignarParametro(6, Integer.toString(usuario.getPerfil().getCod_perfil()), 2);
            BD.AsignarParametro(7, usuario.getUsuariosos().toUpperCase(), 1);
            return BD.registrar();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            BD.desconectar();
        }
    }

    public String getIdLogin() throws Exception {
        try {
            ResultSet datoSql;
            String sql = "SELECT last_number "
                    + "FROM user_sequences "
                    + "WHERE sequence_name='SEC_CSEG_USUARIOS'";

            String id_usuario = "";
            BD.conectar();
            BD.callableStatement(sql);
            BD.consultar();
            datoSql = BD.obtenerConsulta();

            if (datoSql.next()) {
                id_usuario = datoSql.getString(1);
            }
            return id_usuario;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            BD.desconectar();
        }
    }

    public boolean actualizar(UsuarioBeans usuario) throws Exception {
        try {
            String sql1, sql2;
            String password;

            //Reestablecer Contraseña
            sql1 = "UPDATE cusuarios "
                    + "SET clave = ? , "
                    + "idusuariodigita = ?, "
                    + "fechadigita = SYSDATE "
                    + "WHERE idusuario = ?";

            //Campiar Perfil y estado
            sql2 = "UPDATE cusuarios "
                    + "SET activo = ? ,"
                    + "codperfil = ?,"
                    + "idusuariodigita = ?, "
                    + "fechadigita = SYSDATE, "
                    + "login = ?, "
                    + "usuariosos = ? "
                    + "WHERE idusuario = ?";

            BD.conectar();
            password = usuario.getClave();
            if (!password.startsWith("{DES}")) {//Reestablecer Contraseña
                BD.callableStatement(sql1);
                BD.AsignarParametro(1, SeguridadUtils.encripta(usuario.getClave()), 1);
                BD.AsignarParametro(2, Integer.toString(usuario.getIdUsuarioDigita()), 2);
                BD.AsignarParametro(3, Integer.toString(usuario.getIdusuario()), 1);
            } else {//Cambiar Estado
                BD.callableStatement(sql2);
                BD.AsignarParametro(1, Integer.toString(usuario.getActivo()), 2);
                BD.AsignarParametro(2, Integer.toString(usuario.getPerfil().getCod_perfil()), 2);
                BD.AsignarParametro(3, Integer.toString(usuario.getIdUsuarioDigita()), 2);
                BD.AsignarParametro(4, usuario.getLogin(), 1);
                BD.AsignarParametro(5, (usuario.getUsuariosos() == null ? "" : usuario.getUsuariosos().toUpperCase()), 1);
                BD.AsignarParametro(6, Integer.toString(usuario.getIdusuario()), 1);
            }
            return BD.registrar();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            BD.desconectar();
        }
    }

    public void IngresoSistema(UsuarioBeans usuario) throws Exception {
        try {
            String sql = "UPDATE cusuarios  "
                    + "SET fechaultimoacceso = SYSDATE "
                    + "WHERE idusuario = ? ";
            BD.conectar();
            BD.callableStatement(sql);
            BD.AsignarParametro(1, Integer.toString(usuario.getIdusuario()), 1);
            BD.registrar();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            BD.desconectar();
        }
    }

    public void CambiarClave(String user, String pass) throws Exception {
        try {
            String sql = "UPDATE cusuarios SET clave = ? "
                    + "WHERE idusuario = ? ";
            BD.conectar();
            BD.callableStatement(sql);
            BD.AsignarParametro(1, SeguridadUtils.encripta(pass), 1);
            BD.AsignarParametro(2, user, 1);
            BD.actualizar();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            BD.desconectar();
        }
    }

    public String getListUsuario(int page, int rows, String sidx, String sord) throws Exception {
        String sql;
        String strQuery;
       
        int total = 0;
        int total1 = 0;
        int total_pages;

        ResultSet rsCuenta;
        ResultSet datoSql;

        try {
            sql = "SELECT COUNT(*) AS valor FROM cusuarios cu, mpersonas mp "
                    + "WHERE cu.idusuario = mp.idpersona AND cu.activo = 1";

            BD.conectar();
            BD.callableStatement(sql);
            BD.consultar();

            rsCuenta = BD.obtenerConsulta();

            if (rsCuenta.next()) {
                total = Integer.parseInt(rsCuenta.getString("valor"));
                total1 = total;
            }


            if (total > 0) {
                double d = Math.ceil((double) (total) / (double) (rows));
                total_pages = (int) (d);
            } else {
                total_pages = 0;
            }

        
            strQuery = "SELECT cu.idusuario n1, cu.idusuario n2, cu.login, mp.pnombre||' '|| mp.papellido||' '|| "
                    + "nvl(mp.snombre,' ') ||' '|| nvl(mp.sapellido,' ') NOMBRES "
                    + "FROM cusuarios cu, mpersonas mp "
                    + "WHERE cu.idusuario = mp.idpersona AND cu.activo = 1 AND cu.codperfil = 4";

            BD.callableStatement(strQuery);

            BD.consultar();
            datoSql = BD.obtenerConsulta();
            total = total1;

            JSONObject jsonData = new JSONObject();

            jsonData.put("page", page);
            jsonData.put("total", total_pages);
            jsonData.put("records", total);
            JSONArray jsonRows = Utils.llenarGrilla(datoSql);
            jsonData.put("rows", jsonRows);

            return jsonData.toString();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            BD.desconectar();
        }
    }

    public String getListUsuario(int page, int rows, int grupo) throws Exception {
        String sql;
        String strQuery;

        int total = 0;
        int total1 = 0;
        int total_pages;


        ResultSet rsCuenta;
        ResultSet datoSql;

        try {
            sql = "SELECT COUNT(*) AS valor FROM plaboresusuarios "
                    + "GROUP BY idusuario";

            BD.conectar();
            BD.callableStatement(sql);
            BD.consultar();

            rsCuenta = BD.obtenerConsulta();

            if (rsCuenta.next()) {
                total = Integer.parseInt(rsCuenta.getString("valor"));
                total1 = total;
            }


            if (total > 0) {
                double d = Math.ceil((double) (total) / (double) (rows));
                total_pages = (int) (d);
            } else {
                total_pages = 0;
            }

            strQuery = "SELECT   plu.idusuario ni, plu.idusuario no, pnombre || ' ' || papellido auxiliar, login "
                    + "    FROM plaboresusuarios plu, "
                    + "         plaborescontratos plc, "
                    + "         mpersonas mp, "
                    + "         cusuarios cu "
                    + "   WHERE plc.idlaborcontrato = plu.idlaborcontrato "
                    + "     AND mp.idpersona = plu.idusuario "
                    + "     AND cu.idusuario = plu.idusuario "
                    + "     AND plc.idgrupo = ? "
                    + "     AND plu.idusuario NOT IN ( "
                    + "            SELECT idusuario "
                    + "              FROM mregistros mr, plaborescontratos plc "
                    + "             WHERE TO_CHAR (fechainicio, 'dd/mm/yyyy') = TO_CHAR (SYSDATE, 'dd/mm/yyyy') "
                    + "               AND plc.idlaborcontrato = mr.idlaborcontrato "
                    + "               AND plc.idgrupo = ?) "
                    + "GROUP BY plu.idusuario, pnombre, papellido, login "
                    + "ORDER BY 1";

            BD.callableStatement(strQuery);
            BD.AsignarParametro(1, Integer.toString(grupo), 2);
            BD.AsignarParametro(2, Integer.toString(grupo), 2);

            BD.consultar();
            datoSql = BD.obtenerConsulta();
            total = total1;

            JSONObject jsonData = new JSONObject();

            jsonData.put("page", page);
            jsonData.put("total", total_pages);
            jsonData.put("records", total);
            JSONArray jsonRows = Utils.llenarGrilla(datoSql);
            jsonData.put("rows", jsonRows);

            return jsonData.toString();

        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            BD.desconectar();
        }
    }

    public String getListUsuario(int page, int rows, int grupo, String fecha) throws Exception {
        String sql;
        String strQuery;
        
        int total = 0;
        int total1 = 0;
        int total_pages;

        ResultSet rsCuenta;
        ResultSet datoSql;

        try {
            sql = "SELECT COUNT(*) AS valor FROM plaboresusuarios "
                    + "GROUP BY idusuario";

            BD.conectar();
            BD.callableStatement(sql);
            BD.consultar();

            rsCuenta = BD.obtenerConsulta();

            if (rsCuenta.next()) {
                total = Integer.parseInt(rsCuenta.getString("valor"));
                total1 = total;
            }


            if (total > 0) {
                double d = Math.ceil((double) (total) / (double) (rows));
                total_pages = (int) (d);
            } else {
                total_pages = 0;
            }

            strQuery = "SELECT idnovedad, idnovedad, desnovedad, pnombre || ' ' || papellido auxiliar, "
                    + "       TO_CHAR (fechainicio, 'dd/mm/yyyy hh24:mi'), TO_CHAR (fechafin, 'dd/mm/yyyy hh24:mi') "
                    + "  FROM mnovedades mn "
                    + "     INNER JOIN mpersonas ON idpersona = mn.idusuario "
                    + "     INNER JOIN rnovedades rn ON rn.codnovedad = mn.codnovedad "
                    + " WHERE TO_DATE (?, 'dd/MM/yy') BETWEEN TO_DATE (fechainicio,'dd/MM/yy')AND fechafin "
                    + "   AND mn.idusuario IN ( "
                    + "          SELECT   idusuario "
                    + "              FROM plaboresusuarios plu, plaborescontratos plc "
                    + "             WHERE plc.idlaborcontrato = plu.idlaborcontrato "
                    + "               AND plc.idgrupo = ? "
                    + "          GROUP BY idusuario) "
                    + "   ORDER BY 1";

            BD.callableStatement(strQuery);
            BD.AsignarParametro(1, fecha, 1);
            BD.AsignarParametro(2, Integer.toString(grupo), 2);

            BD.consultar();
            datoSql = BD.obtenerConsulta();
            total = total1;


            JSONObject jsonData = new JSONObject();

            jsonData.put("page", page);
            jsonData.put("total", total_pages);
            jsonData.put("records", total);
            JSONArray jsonRows = Utils.llenarGrilla(datoSql);
            jsonData.put("rows", jsonRows);

            return jsonData.toString();

        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            BD.desconectar();
        }
    }

    public UsuarioBeans consultar(int idusuario) throws Exception {
        UsuarioBeans usuariobean = new UsuarioBeans();
        try {
            String sql;
            ResultSet datoSql;
            BD.conectar();

            sql = "SELECT c.idusuario, c.login, c.clave, c.activo, c.idusuariodigita, "
                    + "c.codperfil "
                    + "FROM cusuarios c "
                    + "WHERE idusuario = ?";
            BD.callableStatement(sql);
            BD.AsignarParametro(1, Integer.toString(idusuario), 2);

            if (!BD.consultar()) {
                throw new Exception("Error Realizando La Consulta: " + sql);
            } else {
                datoSql = BD.obtenerConsulta();
                if (datoSql.next()) {
                    usuariobean.setIdusuario(datoSql.getInt("idusuario"));
                    usuariobean.setLogin(datoSql.getString("login"));
                    usuariobean.setClave(datoSql.getString("clave"));
                    usuariobean.setActivo(datoSql.getInt("activo"));
                    usuariobean.setIdUsuarioDigita(datoSql.getInt("idusuariodigita"));
                    PerfilBeans perfilbean = new PerfilBeans();
                    perfilbean.setCod_perfil(datoSql.getInt("codperfil"));
                    usuariobean.setPerfil(perfilbean);
                }
            }
            return usuariobean;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            BD.desconectar();
        }
    }

    public List<UsuarioBeans> listarPorPerfil(int codperfil) throws Exception {
        ArrayList<UsuarioBeans> list = new ArrayList<UsuarioBeans>();
        try {
            BD.conectar();
            String sql = "SELECT cu.idusuario, login, clave, activo, idusuariodigita, cu.fechadigita, fechaultimoacceso, codperfil "
                    + "FROM cusuarios cu INNER JOIN mpersonas mp ON mp.idpersona = cu.idusuario "
                    + "WHERE codperfil = ? ORDER BY pnombre";
            BD.callableStatement(sql);
            BD.AsignarParametro(1, Integer.toString(codperfil), 2);
            BD.consultar();

            ResultSet datoSql = BD.obtenerConsulta();
            UsuarioBeans usuarioBeans;

            while (datoSql.next()) {
                usuarioBeans = new UsuarioBeans();
                usuarioBeans.setIdusuario(datoSql.getInt("idusuario"));
                usuarioBeans.setLogin(datoSql.getString("login"));
                usuarioBeans.setClave(datoSql.getString("clave"));
                usuarioBeans.setActivo(datoSql.getInt("activo"));
                usuarioBeans.setIdUsuarioDigita(datoSql.getInt("idusuariodigita"));
                usuarioBeans.setFechaDigita(datoSql.getString("fechadigita"));
                usuarioBeans.setFechaUltimoAcceso(datoSql.getString("fechaultimoacceso"));

                PerfilDao perfilDao = new PerfilDao();
                PerfilBeans perfilBeans = perfilDao.consultar(datoSql.getInt("codperfil"));

                usuarioBeans.setPerfil(perfilBeans);

                list.add(usuarioBeans);

            }
            return list;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            BD.desconectar();
        }
    }

    public List<UsuarioBeans> listarPorSitio(int sitio) throws Exception {
        ArrayList<UsuarioBeans> list = new ArrayList<UsuarioBeans>();
        try {
            BD.conectar();
            String sql = "SELECT cu.idusuario, login, clave, activo, idusuariodigita, cu.fechadigita, fechaultimoacceso, codperfil "
                    + "FROM cusuarios cu INNER JOIN mpersonas mp ON mp.idpersona = cu.idusuario "
                    + "WHERE sitiotrabajo = ? ORDER BY pnombre";
            BD.callableStatement(sql);
            BD.AsignarParametro(1, Integer.toString(sitio), 2);
            BD.consultar();

            ResultSet datoSql = BD.obtenerConsulta();
            UsuarioBeans usuarioBeans;

            while (datoSql.next()) {
                usuarioBeans = new UsuarioBeans();
                usuarioBeans.setIdusuario(datoSql.getInt("idusuario"));
                usuarioBeans.setLogin(datoSql.getString("login"));
                usuarioBeans.setClave(datoSql.getString("clave"));
                usuarioBeans.setActivo(datoSql.getInt("activo"));
                usuarioBeans.setIdUsuarioDigita(datoSql.getInt("idusuariodigita"));
                usuarioBeans.setFechaDigita(datoSql.getString("fechadigita"));
                usuarioBeans.setFechaUltimoAcceso(datoSql.getString("fechaultimoacceso"));

                PerfilDao perfilDao = new PerfilDao();
                PerfilBeans perfilBeans = perfilDao.consultar(datoSql.getInt("codperfil"));

                usuarioBeans.setPerfil(perfilBeans);

                list.add(usuarioBeans);

            }
            return list;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            BD.desconectar();
        }
    }
}
