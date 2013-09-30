/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.co.sio.java.dao;

import com.co.sio.java.db.ControllerPool;
import com.co.sio.java.mbeans.ParametroBeans;
import com.co.sio.java.mbeans.UsuarioBeans;
import com.co.sio.java.mbeans.UsuarioLaboresBeans;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

/**
 *
 * @author fmoctezuma
 */
public class UsuarioLaboresDao {

    private ControllerPool BD;

    public UsuarioLaboresDao() {
        BD = new ControllerPool();
    }

    public boolean insertar(UsuarioLaboresBeans beans) throws Exception {
        UsuarioLaboresBeans temp = new UsuarioLaboresBeans();
        temp.setUsuario(beans.getUsuario());
        temp.setLabor(beans.getLabor());
        if (consultar(temp) == null) {
            try {
                String sql = "INSERT INTO plaboresusuarios (idlaborcontrato,idusuario,fechainicial,fechafinal) "
                        + "VALUES (?,?,to_date(?,'dd/mm/yyyy'),to_date(?,'dd/mm/yyyy')) ";
                BD.conectar();
                BD.callableStatement(sql);
                BD.AsignarParametro(1, Integer.toString(beans.getLabor().getIdlaborcontrato()), 2);
                BD.AsignarParametro(2, Integer.toString(beans.getUsuario().getIdusuario()), 2);
                BD.AsignarParametro(3, beans.getFechaInicio(), 1);
                BD.AsignarParametro(4, beans.getFechaFin(), 1);

                return BD.registrar();
            } catch (Exception e) {
                throw new Exception(e.getMessage());
            } finally {
                BD.desconectar();
            }
        } else {
            try {
                String sql = "UPDATE plaboresusuarios "
                        + "   SET fechainicial = to_date(?,'dd/mm/yyyy'), "
                        + "       fechafinal = to_date(?,'dd/mm/yyyy')  "
                        + " WHERE idlaborcontrato = ? AND idusuario = ? ";
                BD.conectar();
                BD.callableStatement(sql);
                BD.AsignarParametro(1, beans.getFechaInicio(), 1);
                BD.AsignarParametro(2, beans.getFechaFin(), 1);
                BD.AsignarParametro(3, Integer.toString(beans.getLabor().getIdlaborcontrato()), 2);
                BD.AsignarParametro(4, Integer.toString(beans.getUsuario().getIdusuario()), 2);


                return BD.registrar();
            } catch (Exception e) {
                throw new Exception(e.getMessage());
            } finally {
                BD.desconectar();
            }
        }

    }

    public UsuarioLaboresBeans consultar(UsuarioLaboresBeans beans) throws Exception {
        try {
            String sql = "SELECT idlaborcontrato,idusuario,to_char(fechainicial,'dd/mm/yyyy'),to_char(fechafinal,'dd/mm/yyyy') "
                    + "FROM plaboresusuarios "
                    + "WHERE idlaborcontrato = ? AND idusuario = ? ";
            BD.conectar();
            BD.callableStatement(sql);
            BD.AsignarParametro(1, Integer.toString(beans.getLabor().getIdlaborcontrato()), 2);
            BD.AsignarParametro(2, Integer.toString(beans.getUsuario().getIdusuario()), 2);
            BD.consultar();

            ResultSet datoSql = BD.obtenerConsulta();

            if (datoSql.next()) {
                beans.setFechaInicio(datoSql.getString(3));
                beans.setFechaFin(datoSql.getString(4));
                return beans;
            } else {
                return null;
            }
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        } finally {
            BD.desconectar();
        }

    }

    public boolean eliminar(UsuarioLaboresBeans beans) throws Exception {
        try {
            String sql = "DELETE FROM plaboresusuarios "
                    + "WHERE idlaborcontrato = ? AND idusuario = ? ";
            BD.conectar();
            BD.callableStatement(sql);
            BD.AsignarParametro(1, Integer.toString(beans.getLabor().getIdlaborcontrato()), 2);
            BD.AsignarParametro(2, Integer.toString(beans.getUsuario().getIdusuario()), 2);

            return BD.registrar();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            BD.desconectar();
        }

    }

    public String getListLaboresUsuario(int page, int rows, String sidx, String sord, int idusuario) throws Exception {
        try {
            String sql = "SELECT COUNT(*) AS valor from plaboresusuarios WHERE idusuario = ?";
            BD.conectar();
            BD.callableStatement(sql);
            BD.AsignarParametro(1, Integer.toString(idusuario), 2);
            BD.consultar();

            ResultSet rsCuenta = BD.obtenerConsulta();
            int total = 0, total1 = 0, total_pages;
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

            String strQuery = "SELECT plu.idlaborcontrato,nomcliente||' - '|| numcontrato, desarea, desgrupo, "
                    + "       deslabor, destipolabor, NVL(deshoraextra,' ')deshoraextra "
                    + "  FROM plaboresusuarios plu INNER JOIN plaborescontratos plc "
                    + "       ON plc.idlaborcontrato = plu.idlaborcontrato "
                    + "       INNER JOIN rlabores rl ON plc.codlabor = rl.codlabor "
                    + "       LEFT JOIN rhorasextras rhe ON rhe.codhoraextra = plc.codhoraextra "
                    + "       INNER JOIN rtipolabor rtl ON rtl.codtipolabor = plc.codtipolabor "
                    + "       INNER JOIN dareasgrupos dag ON dag.idgrupo = plc.idgrupo "
                    + "       INNER JOIN pcontratos pc ON pc.idcontrato = plc.idcontrato "
                    + "       INNER JOIN dclientesareas dca ON dca.idarea = dag.idarea "
                    + "       INNER JOIN rareas ra ON ra.codarea = dca.codarea "
                    + "       INNER JOIN mclientes mc ON mc.idcliente = dca.idcliente "
                    + " WHERE plu.idusuario = ?";

            BD.callableStatement(strQuery);
            BD.AsignarParametro(1, Integer.toString(idusuario), 2);
            BD.consultar();
            ResultSet datoSql = BD.obtenerConsulta();
            total = total1;

            String json = "";
            json = json + "{\n";
            json = json + "\"page\":" + page + ",\n";
            json = json + "\"total\":" + total_pages + ",\n";
            json = json + "\"records\":" + total + ",\n";
            json = json + "\"rows\": [";
            boolean rc = false;

            while (datoSql.next()) {
                if (rc) {
                    json = json + ",";
                }
                json = json + "\n{";
                json = json + "\"id\":\"" + datoSql.getInt(1) + "\",";
                json = json + "\"cell\":[" + datoSql.getInt(1) + "";
                json = json + ",\"" + datoSql.getString(2) + "\"";
                json = json + ",\"" + datoSql.getString(3) + "\"";
                json = json + ",\"" + datoSql.getString(4) + "\"";
                json = json + ",\"" + datoSql.getString(5) + "\"";
                json = json + ",\"" + datoSql.getString(6) + "\"";
                json = json + ",\"" + datoSql.getString(7) + "\"]";

                json = json + "}";
                rc = true;

            }
            json = json + "]\n";
            json = json + "}";

            return json;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            BD.desconectar();
        }
    }

    public String getListLaboresUsuario(int page, int rows, String sidx, String sord, int idgrupo, int idcontrato, int idusuario) throws Exception {
        String sql;
        String strQuery;
        String json;

        int total = 0;
        int total1 = 0;
        int total_pages;

        boolean rc;

        ResultSet rsCuenta;
        ResultSet datoSql;

        try {
            sql = "SELECT count(*) as valor "
                    + "FROM rlabores rl INNER JOIN plaborescontratos plc "
                    + "ON rl.codlabor = plc.codlabor AND plc.idcontrato = ? "
                    + "INNER JOIN dareasgrupos dag ON dag.idgrupo = plc.idgrupo "
                    + "AND dag.idgrupo = ? ";
            BD.conectar();
            BD.callableStatement(sql);
            BD.AsignarParametro(1, Integer.toString(idcontrato), 2);
            BD.AsignarParametro(2, Integer.toString(idgrupo), 2);
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

            strQuery = "SELECT   plc.idlaborcontrato, rl.deslabor, rtl.destipolabor, rhe.deshoraextra "
                    + "FROM plaborescontratos plc "
                    + "INNER JOIN rlabores rl ON rl.codlabor = plc.codlabor "
                    + "INNER JOIN rtipolabor rtl ON rtl.codtipolabor = plc.codtipolabor "
                    + "LEFT JOIN rhorasextras rhe ON rhe.codhoraextra = plc.codhoraextra "
                    + "INNER JOIN dareasgrupos dag ON dag.idgrupo = plc.idgrupo "
                    + "WHERE plc.idcontrato = ? AND plc.idgrupo = ?"
                    + "ORDER BY 2, 3, 4 DESC ";
            BD.callableStatement(strQuery);
            BD.AsignarParametro(1, Integer.toString(idcontrato), 2);
            BD.AsignarParametro(2, Integer.toString(idgrupo), 2);

            BD.consultar();
            datoSql = BD.obtenerConsulta();
            total = total1;


            String sql2 = "SELECT plu.idlaborcontrato, to_char(fechainicial,'dd/mm/yyyy'),to_char(fechafinal,'dd/mm/yyyy') "
                    + "  FROM plaboresusuarios plu INNER JOIN plaborescontratos plc "
                    + "       ON plc.idlaborcontrato = plu.idlaborcontrato "
                    + " WHERE plu.idusuario = ? AND plc.idgrupo = ? AND plc.idcontrato = ?";
            BD.callableStatement(sql2);
            BD.AsignarParametro(1, Integer.toString(idusuario), 2);
            BD.AsignarParametro(2, Integer.toString(idgrupo), 2);
            BD.AsignarParametro(3, Integer.toString(idcontrato), 2);

            BD.consultar();
            ResultSet datoAsignadas = BD.obtenerConsulta();

            List asignadas = new ArrayList();
            TreeMap<String, String> tm = new TreeMap<String, String>();



            while (datoAsignadas.next()) {
                tm.put(datoAsignadas.getString(1), datoAsignadas.getString(2) + ";" + datoAsignadas.getString(3));
                asignadas.add(datoAsignadas.getString(1));
            }


            json = "";
            json = json + "{\n";
            json = json + "\"page\":" + page + ",\n";
            json = json + "\"total\":" + total_pages + ",\n";
            json = json + "\"records\":" + total + ",\n";
            json = json + "\"rows\": [";
            rc = false;

            while (datoSql.next()) {
                if (rc) {
                    json = json + ",";
                }
                json = json + "\n{";
                json = json + "\"id\":\"" + datoSql.getInt(1) + "\",";
                json = json + "\"cell\":[" + datoSql.getInt(1) + "";
                json = json + ",\"" + datoSql.getString(2) + "\"";
                json = json + ",\"" + datoSql.getString(3) + "\"";
                json = json + ",\"" + (datoSql.getString(4) == null ? "" : datoSql.getString(4)) + "\"";
                json = json + ",\"" + (tm.get(datoSql.getString(1)) == null ? "" : tm.get(datoSql.getString(1)).split(";")[0]) + "\"";
                json = json + ",\"" + (tm.get(datoSql.getString(1)) == null ? "" : tm.get(datoSql.getString(1)).split(";")[1]) + "\"";
                json = json + ",\"" + (tm.containsKey(datoSql.getString(1)) ? "1" : "0") + "\"]";

                json = json + "}";
                rc = true;
            }
            json = json + "]\n";
            json = json + "}";

            return json;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            BD.desconectar();
        }
    }

    public List<UsuarioLaboresBeans> listar(int idusuario) throws Exception {
        try {

            ArrayList<UsuarioLaboresBeans> lista = new ArrayList<UsuarioLaboresBeans>();
            String sql = "SELECT plu.idlaborcontrato, plu.idusuario, plc.codhoraextra "
                    + "  FROM plaboresusuarios plu "
                    + "INNER JOIN plaborescontratos plc ON plc.idlaborcontrato = plu.idlaborcontrato "
                    + " WHERE idusuario = ? "
                    + "   AND SYSDATE BETWEEN fechainicial AND fechafinal + 1 "
                    + "   AND codhoraextra = 0";
            BD.conectar();
            BD.callableStatement(sql);
            BD.AsignarParametro(1, Integer.toString(idusuario), 2);
            BD.consultar();

            ResultSet datoSql = BD.obtenerConsulta();
            UsuarioBeans usuarioBeans = new UsuarioDao().consultar(idusuario);
            UsuarioLaboresBeans usuarioLaboresBeans;
            while (datoSql.next()) {
                usuarioLaboresBeans = new UsuarioLaboresBeans();
                usuarioLaboresBeans.setUsuario(usuarioBeans);
                usuarioLaboresBeans.setLabor(new LaboresDao().consultar(datoSql.getInt("idlaborcontrato")));

                lista.add(usuarioLaboresBeans);
            }

            ParametroBeans bean = (new ParametroDao()).consultar(18);
            sql = "SELECT TO_CHAR (SYSDATE, 'DD/MM/YYYY') || ' ' || ? FROM DUAL";
            BD.callableStatement(sql);
            BD.AsignarParametro(1, bean.getValparametro(), 1);
            BD.consultar();

            datoSql = BD.obtenerConsulta();
            String fecha = "";
            if (datoSql.next()) {
                fecha = datoSql.getString(1);
            }
            sql = "SELECT plu.idlaborcontrato, plu.idusuario, plc.codhoraextra "
                    + "  FROM plaboresusuarios plu "
                    + "INNER JOIN plaborescontratos plc ON plc.idlaborcontrato = plu.idlaborcontrato "
                    + "INNER JOIN rhorasextras rhe ON rhe.codhoraextra = plc.codhoraextra "
                    + " WHERE idusuario = ? "
                    + "   AND SYSDATE BETWEEN fechainicial AND fechafinal + 1 "
                    + "   AND SYSDATE >= TO_DATE(?,'DD/MM/YYYY HH24:MI') "
                    + "   AND tipoextra = 2";
            BD.callableStatement(sql);
            BD.AsignarParametro(1, Integer.toString(idusuario), 2);
            BD.AsignarParametro(2, fecha, 1);
            BD.consultar();

            datoSql = BD.obtenerConsulta();

            while (datoSql.next()) {
                usuarioLaboresBeans = new UsuarioLaboresBeans();
                usuarioLaboresBeans.setUsuario(usuarioBeans);
                usuarioLaboresBeans.setLabor(new LaboresDao().consultar(datoSql.getInt("idlaborcontrato")));

                lista.add(usuarioLaboresBeans);
            }


            sql = "SELECT plu.idlaborcontrato, plu.idusuario, plc.codhoraextra "
                    + "  FROM plaboresusuarios plu "
                    + "INNER JOIN plaborescontratos plc ON plc.idlaborcontrato = plu.idlaborcontrato "
                    + "INNER JOIN rhorasextras rhe ON rhe.codhoraextra = plc.codhoraextra "
                    + " WHERE idusuario = ? "
                    + "   AND SYSDATE BETWEEN fechainicial AND fechafinal + 1 "
                    + "   AND (   TO_CHAR (SYSDATE, 'dd/mm/yyyy') = TO_CHAR ((SELECT desfestivo "
                    + "                 FROM rfestivos "
                    + "                WHERE TO_CHAR (desfestivo, 'dd/mm/yyyy') = TO_CHAR (SYSDATE, 'dd/mm/yyyy')), 'dd/mm/yyyy') "
                    + "        OR TO_CHAR (SYSDATE, 'D') = 7 "
                    + "       ) "
                    + "   AND tipoextra = 1";
            BD.callableStatement(sql);
            BD.AsignarParametro(1, Integer.toString(idusuario), 2);
            BD.consultar();

            datoSql = BD.obtenerConsulta();

            while (datoSql.next()) {
                usuarioLaboresBeans = new UsuarioLaboresBeans();
                usuarioLaboresBeans.setUsuario(usuarioBeans);
                usuarioLaboresBeans.setLabor(new LaboresDao().consultar(datoSql.getInt("idlaborcontrato")));

                lista.add(usuarioLaboresBeans);
            }

            return lista;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            BD.desconectar();
        }
    }

    public List<UsuarioLaboresBeans> listar(int idusuario, String param) throws Exception {
        try {
            ArrayList<UsuarioLaboresBeans> lista = new ArrayList<UsuarioLaboresBeans>();
            String sql = "SELECT plu.idlaborcontrato, plu.idusuario, plc.codhoraextra "
                    + "  FROM plaboresusuarios plu "
                    + "INNER JOIN plaborescontratos plc ON plc.idlaborcontrato = plu.idlaborcontrato "
                    + " WHERE idusuario = ? ";

            BD.conectar();
            BD.callableStatement(sql);
            BD.AsignarParametro(1, Integer.toString(idusuario), 2);
            BD.consultar();

            ResultSet datoSql = BD.obtenerConsulta();
            UsuarioBeans usuarioBeans = new UsuarioDao().consultar(idusuario);
            UsuarioLaboresBeans usuarioLaboresBeans;
            while (datoSql.next()) {
                usuarioLaboresBeans = new UsuarioLaboresBeans();
                usuarioLaboresBeans.setUsuario(usuarioBeans);
                usuarioLaboresBeans.setLabor(new LaboresDao().consultar(datoSql.getInt("idlaborcontrato")));

                lista.add(usuarioLaboresBeans);
            }
            return lista;
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        } finally {
            BD.desconectar();
        }
    }
}
