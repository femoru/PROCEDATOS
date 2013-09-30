/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.co.sio.java.dao;

import com.co.sio.java.db.ControllerPool;
import com.co.sio.java.mbeans.GrupoBeans;
import com.co.sio.java.mbeans.UsuarioBeans;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author fmoctezuma
 */
public class GruposDao {

    ControllerPool BD;

    public GruposDao() {
        BD = new ControllerPool();
    }

    public GrupoBeans consultar(int idgrupo) throws Exception {
        try {
            GrupoBeans grupoBeans = null;
            String sql = "SELECT idgrupo, idarea, desgrupo, idcoordinador "
                    + "FROM dareasgrupos "
                    + "WHERE idgrupo = ? ";
            BD.conectar();
            BD.callableStatement(sql);
            BD.AsignarParametro(1, Integer.toString(idgrupo), 2);
            BD.consultar();

            ResultSet datoSql = BD.obtenerConsulta();

            if (datoSql.next()) {
                grupoBeans = new GrupoBeans();
                grupoBeans.setIdgrupo(idgrupo);
                grupoBeans.setIdarea(datoSql.getInt("idarea"));
                grupoBeans.setDesgrupo(datoSql.getString("desgrupo"));
                UsuarioDao usuarioDao = new UsuarioDao();
                grupoBeans.setCordinador(usuarioDao.consultar(datoSql.getInt("idcoordinador")));
            }

            return grupoBeans;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            BD.desconectar();
        }
    }

    public boolean Guardar(GrupoBeans grupoBeans) throws Exception {

        try {
            String sql = "INSERT INTO dareasgrupos(idarea, desgrupo, idcoordinador) "
                    + "VALUES (?, ?, ?)";

            BD.conectar();
            BD.callableStatement(sql);
            BD.AsignarParametro(1, Integer.toString(grupoBeans.getIdarea()), 2);
            BD.AsignarParametro(2, grupoBeans.getDesgrupo(), 1);
            BD.AsignarParametro(3, Integer.toString(grupoBeans.getCordinador().getIdusuario()), 2);


            return BD.registrar();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            BD.desconectar();
        }
    }

    public boolean Actualizar(GrupoBeans grupoBeans) throws Exception {

        try {
            String sql = "UPDATE dareasgrupos "
                    + "SET desgrupo = ?, "
                    + "idcoordinador = ? "
                    + "WHERE idgrupo = ? ";

            BD.conectar();
            BD.callableStatement(sql);
            BD.AsignarParametro(1, grupoBeans.getDesgrupo(), 1);
            BD.AsignarParametro(2, Integer.toString(grupoBeans.getCordinador().getIdusuario()), 2);
            BD.AsignarParametro(3, Integer.toString(grupoBeans.getIdgrupo()), 2);


            return BD.registrar();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            BD.desconectar();
        }
    }

    public String getListGrupos(int page, int rows, String sidx, String sord, int idarea) throws Exception {
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

            sql = "SELECT COUNT(*) AS valor FROM dareasgrupos "
                    + "WHERE idarea = ?";

            BD.conectar();
            BD.callableStatement(sql);
            BD.AsignarParametro(1, Integer.toString(idarea), 2);
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

            strQuery = "SELECT idgrupo, idarea, desgrupo, pnombre, papellido "
                    + "FROM dareasgrupos dag, mpersonas mp "
                    + "WHERE dag.idcoordinador=mp.idpersona AND idarea = ? "
                    + "ORDER BY 1 ";
            BD.callableStatement(strQuery);
            BD.AsignarParametro(1, Integer.toString(idarea), 2);

            BD.consultar();
            datoSql = BD.obtenerConsulta();
            total = total1;

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
                json = json + "\"id\":\"" + datoSql.getInt("idgrupo") + "\",";
                json = json + "\"cell\":[" + datoSql.getInt("idgrupo") + "";
                json = json + ",\"" + datoSql.getInt("idarea") + "\"";
                json = json + ",\"" + datoSql.getString("desgrupo") + "\"";
                json = json + ",\"" + datoSql.getString("pnombre") + " " + datoSql.getString("papellido") + "\"]";
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

    public String getListGruposCliente(int page, int rows, String sidx, String sord, int idcliente) throws Exception {
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

            sql = "SELECT COUNT(*) AS valor FROM dareasgrupos dag, dclientesareas dca "
                    + "WHERE dag.idarea = dca.idarea AND dca.idcliente = ? ";

            BD.conectar();
            BD.callableStatement(sql);
            BD.AsignarParametro(1, Integer.toString(idcliente), 2);
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

            strQuery = "SELECT dag.idgrupo, ra.desarea, dag.desgrupo "
                    + "FROM rareas ra, dareasgrupos dag, dclientesareas dca "
                    + "WHERE ra.codarea=dca.codarea AND dag.idarea = dca.idarea AND dca.idcliente = ? "
                    + "ORDER BY 1 ";
            BD.callableStatement(strQuery);
            BD.AsignarParametro(1, Integer.toString(idcliente), 2);

            BD.consultar();
            datoSql = BD.obtenerConsulta();
            total = total1;

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
                json = json + "\"id\":\"" + datoSql.getInt("idgrupo") + "\",";
                json = json + "\"cell\":[" + datoSql.getInt("idgrupo") + "";
                json = json + ",\"" + datoSql.getString("desarea") + "\"";
                json = json + ",\"" + datoSql.getString("desgrupo") + "\"]";
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

    public List listar(String idarea) throws Exception {
        List<GrupoBeans> list = new ArrayList<GrupoBeans>();
        try {
            String sql = "SELECT idgrupo, idarea, desgrupo, idcoordinador "
                    + "FROM dareasgrupos "
                    + "WHERE idarea=? "
                    + "ORDER BY 1";
            BD.conectar();
            BD.callableStatement(sql);
            BD.AsignarParametro(1, idarea, 2);
            BD.consultar();
            ResultSet datoSql = BD.obtenerConsulta();
            GrupoBeans grupoBeans;
            while (datoSql.next()) {
                grupoBeans = new GrupoBeans();
                grupoBeans.setIdgrupo(datoSql.getInt("idgrupo"));
                grupoBeans.setIdarea(datoSql.getInt("idarea"));
                grupoBeans.setDesgrupo(datoSql.getString("desgrupo"));
                grupoBeans.setCordinador(new UsuarioDao().consultar(datoSql.getInt("idcoordinador")));

                list.add(grupoBeans);
            }
            return list;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            BD.desconectar();
        }
    }

    public List listarCoordinadoresAuxiliar(int idauxiliar) throws Exception {
        try {
            List<GrupoBeans> list = new ArrayList<GrupoBeans>();
            String sql = "SELECT   dag.idgrupo, idarea, desgrupo, idcoordinador "
                    + "FROM dareasgrupos dag "
                    + "INNER JOIN plaborescontratos plc ON dag.idgrupo = plc.idgrupo "
                    + "INNER JOIN plaboresusuarios plu ON plc.idlaborcontrato = plu.idlaborcontrato "
                    + "AND plu.idusuario = ? "
                    + "GROUP BY dag.idgrupo, idarea, desgrupo, idcoordinador "
                    + "ORDER BY 1 ";
            BD.conectar();
            BD.callableStatement(sql);
            BD.AsignarParametro(1, Integer.toString(idauxiliar), 2);
            BD.consultar();
            ResultSet datoSql = BD.obtenerConsulta();
            GrupoBeans grupoBeans;
            while (datoSql.next()) {
                grupoBeans = new GrupoBeans();
                grupoBeans.setIdgrupo(datoSql.getInt("idgrupo"));
                grupoBeans.setIdarea(datoSql.getInt("idarea"));
                grupoBeans.setDesgrupo(datoSql.getString("desgrupo"));
                grupoBeans.setCordinador(new UsuarioDao().consultar(datoSql.getInt("idcoordinador")));
                list.add(grupoBeans);
            }
            return list;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            BD.desconectar();
        }
    }
    
    public List listar(int idcoordinador) throws Exception {
        List<GrupoBeans> list = new ArrayList<GrupoBeans>();
        try {
            UsuarioBeans coord = new UsuarioDao().consultar(idcoordinador);

            String sql = "SELECT idgrupo, idarea, desgrupo, idcoordinador "
                    + "FROM dareasgrupos ";
            if (coord.getPerfil().getCod_perfil() == 5) {
                sql += "WHERE idcoordinador = ? ";
            }
            sql += "ORDER BY 1";
            BD.conectar();
            BD.callableStatement(sql);
            if (coord.getPerfil().getCod_perfil() == 5) {
                BD.AsignarParametro(1, Integer.toString(idcoordinador), 2);
            }
            BD.consultar();
            ResultSet datoSql = BD.obtenerConsulta();
            GrupoBeans grupoBeans;
            while (datoSql.next()) {
                grupoBeans = new GrupoBeans();
                grupoBeans.setIdgrupo(datoSql.getInt("idgrupo"));
                grupoBeans.setIdarea(datoSql.getInt("idarea"));
                grupoBeans.setDesgrupo(datoSql.getString("desgrupo"));
                grupoBeans.setCordinador(new UsuarioDao().consultar(datoSql.getInt("idcoordinador")));

                list.add(grupoBeans);
            }


            return list;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            BD.desconectar();
        }
    }
    
}
