/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.co.sio.java.dao;

import com.co.sio.java.db.ControllerPool;
import com.co.sio.java.mbeans.LaborBeans;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author fmoctezuma
 */
public class LaboresDao {

    private ControllerPool BD;

    public LaboresDao() {
        BD = new ControllerPool();
    }

    public LaborBeans consultar(int idcontrato, int idgrupo, int codlabor, int tipoLabor, int horaExtra) throws Exception {
        try {
            LaborBeans laborBeans = null;
            String sql = "SELECT idlaborcontrato,  idcontrato,  idgrupo,  codlabor,  valor, costo,  "
                    + "activo,  datolabor,  conciliacion,  codhoraextra, codtipolabor "
                    + "FROM plaborescontratos "
                    + "WHERE idcontrato = ? "
                    + "AND idgrupo = ? "
                    + "AND codlabor = ? "
                    + "AND codtipolabor = ? "
                    + "AND codhoraextra = ? ";

            BD.conectar();
            BD.callableStatement(sql);
            BD.AsignarParametro(1, Integer.toString(idcontrato), 2);
            BD.AsignarParametro(2, Integer.toString(idgrupo), 2);
            BD.AsignarParametro(3, Integer.toString(codlabor), 2);
            BD.AsignarParametro(4, Integer.toString(tipoLabor), 2);
            BD.AsignarParametro(5, Integer.toString(horaExtra), 2);
            BD.consultar();

            ResultSet datoSql = BD.obtenerConsulta();

            ContratosDao contratosDao = new ContratosDao();
            GruposDao gruposDao = new GruposDao();
            if (datoSql.next()) {
                laborBeans = new LaborBeans();
                laborBeans.setIdlaborcontrato(datoSql.getInt(1));
                laborBeans.setContrato(contratosDao.consultar(datoSql.getInt(2)));
                laborBeans.setGrupo(gruposDao.consultar(datoSql.getInt(3)));
                laborBeans.setLabor(datoSql.getInt(4));
                laborBeans.setValor(datoSql.getString(5));
                laborBeans.setCosto(datoSql.getString(6));
                laborBeans.setActivo(datoSql.getInt(7));
                laborBeans.setDatolabor(datoSql.getInt(8));
                laborBeans.setConciliacion(datoSql.getInt(9));
                laborBeans.setHoraextra(datoSql.getInt(10));
                laborBeans.setTipolabor(datoSql.getInt(11));
            }
            return laborBeans;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            BD.desconectar();
        }
    }

    public LaborBeans consultar(int idlabor) throws Exception {
        try {
            LaborBeans laborBeans = null;
            String sql = "SELECT idlaborcontrato, idcontrato, idgrupo, codlabor, valor, costo, "
                    + "activo,  datolabor,  conciliacion,  codhoraextra, codtipolabor "
                    + "FROM plaborescontratos "
                    + "WHERE idlaborcontrato = ? ";
            BD.conectar();
            BD.callableStatement(sql);
            BD.AsignarParametro(1, Integer.toString(idlabor), 2);
            BD.consultar();

            ResultSet datoSql = BD.obtenerConsulta();

            if (datoSql.next()) {
                laborBeans = new LaborBeans();
                laborBeans.setIdlaborcontrato(datoSql.getInt(1));
                laborBeans.setContrato(new ContratosDao().consultar(datoSql.getInt(2)));
                laborBeans.setGrupo(new GruposDao().consultar(datoSql.getInt(3)));
                laborBeans.setLabor(datoSql.getInt(4));
                laborBeans.setValor(datoSql.getString(5));
                laborBeans.setCosto(datoSql.getString(6));
                laborBeans.setActivo(datoSql.getInt(7));
                laborBeans.setDatolabor(datoSql.getInt(8));
                laborBeans.setConciliacion(datoSql.getInt(9));
                laborBeans.setHoraextra(datoSql.getInt(10));
                laborBeans.setTipolabor(datoSql.getInt(11));
            }
            return laborBeans;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            BD.desconectar();
        }
    }

    public boolean Guardar(LaborBeans laborBeans) throws Exception {
        LaborBeans lb;
        lb = consultar(laborBeans.getContrato().getIdcontrato(),
                laborBeans.getGrupo().getIdgrupo(),
                laborBeans.getLabor(), laborBeans.getTipolabor(), laborBeans.getHoraextra());

        if (lb != null) {
            laborBeans.setIdlaborcontrato(lb.getIdlaborcontrato());
            return Actualizar(laborBeans);
        } else {
            try {
                String sql = "INSERT INTO plaborescontratos (idcontrato, idgrupo, codlabor, valor, costo, "
                        + "activo,  datolabor,  conciliacion,  codhoraextra, codtipolabor ) "
                        + "VALUES ( ?,?,?,?,?,?,?,?,?,? )";
                BD.conectar();
                BD.callableStatement(sql);
                BD.AsignarParametro(1, Integer.toString(laborBeans.getContrato().getIdcontrato()), 2);
                BD.AsignarParametro(2, Integer.toString(laborBeans.getGrupo().getIdgrupo()), 2);
                BD.AsignarParametro(3, Integer.toString(laborBeans.getLabor()), 2);
                BD.AsignarParametro(4, laborBeans.getValor(), 1);
                BD.AsignarParametro(5, laborBeans.getCosto(), 1);
                BD.AsignarParametro(6, Integer.toString(laborBeans.getActivo()), 2);
                BD.AsignarParametro(7, Integer.toString(laborBeans.getDatolabor()), 2);
                BD.AsignarParametro(8, Integer.toString(laborBeans.getConciliacion()), 2);
                BD.AsignarParametro(9, Integer.toString(laborBeans.getHoraextra()), 2);
                BD.AsignarParametro(10, Integer.toString(laborBeans.getTipolabor()), 2);
                return BD.registrar();
            } catch (Exception e) {
                throw new Exception(e.getMessage());
            } finally {
                BD.desconectar();
            }
        }
    }

    public boolean Actualizar(LaborBeans laborBeans) throws Exception {
        try {
            String sql = "UPDATE plaborescontratos "
                    + "   SET idgrupo = ?, "
                    + "       codlabor = ?, "
                    + "       valor = ?, "
                    + "       costo = ?, "
                    + "       activo = ?, "
                    + "       datolabor = ?, "
                    + "       conciliacion = ?, "
                    + "       codhoraextra = ?, "
                    + "       codtipolabor = ? "
                    + " WHERE idlaborcontrato = ? ";
            BD.conectar();
            BD.callableStatement(sql);
            BD.AsignarParametro(1, Integer.toString(laborBeans.getGrupo().getIdgrupo()), 2);
            BD.AsignarParametro(2, Integer.toString(laborBeans.getLabor()), 2);
            BD.AsignarParametro(3, laborBeans.getValor(), 1);
            BD.AsignarParametro(4, laborBeans.getCosto(), 1);
            BD.AsignarParametro(5, Integer.toString(laborBeans.getActivo()), 2);
            BD.AsignarParametro(6, Integer.toString(laborBeans.getDatolabor()), 2);
            BD.AsignarParametro(7, Integer.toString(laborBeans.getConciliacion()), 2);
            BD.AsignarParametro(8, Integer.toString(laborBeans.getHoraextra()), 2);
            BD.AsignarParametro(9, Integer.toString(laborBeans.getTipolabor()), 2);
            BD.AsignarParametro(10, Integer.toString(laborBeans.getIdlaborcontrato()), 2);
            return BD.registrar();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            BD.desconectar();
        }
    }

    public String getListLabores(int page, int rows, String sidx, String sord, int idcontrato) throws Exception {
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

            sql = "SELECT COUNT(*) AS valor FROM plaborescontratos WHERE idcontrato = ? ";

            BD.conectar();
            BD.callableStatement(sql);
            BD.AsignarParametro(1, Integer.toString(idcontrato), 2);
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

            strQuery = "SELECT   idlaborcontrato, desarea, desgrupo, deslabor, destipolabor, "
                    + "deshoraextra, valor, costo, plc.activo, datolabor, conciliacion "
                    + "FROM plaborescontratos plc "
                    + "INNER JOIN dareasgrupos dag ON dag.idgrupo = plc.idgrupo "
                    + "INNER JOIN dclientesareas dca ON dca.idarea = dag.idarea "
                    + "INNER JOIN rareas ra ON ra.codarea = dca.codarea "
                    + "INNER JOIN rlabores rl ON rl.codlabor = plc.codlabor "
                    + "LEFT JOIN rtipolabor rtl ON rtl.codtipolabor = plc.codtipolabor "
                    + "LEFT JOIN rhorasextras rhe ON rhe.codhoraextra = plc.codhoraextra "
                    + "WHERE idcontrato = ? "
                    + "ORDER BY dag.idarea, plc.idgrupo,plc.codlabor,plc.codhoraextra";

            BD.callableStatement(strQuery);
            BD.AsignarParametro(1, Integer.toString(idcontrato), 2);
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
                json = json + "\"id\":\"" + datoSql.getInt(1) + "\",";
                json = json + "\"cell\":[" + datoSql.getInt(1) + "";
                json = json + ",\"" + datoSql.getString(2) + "\"";
                json = json + ",\"" + datoSql.getString(3) + "\"";
                json = json + ",\"" + datoSql.getString(4) + "\"";
                json = json + ",\"" + datoSql.getString(5) + "\"";
                json = json + ",\"" + ((datoSql.getString(6) == null) ? "" : datoSql.getString(6)) + "\"";
                json = json + ",\"" + datoSql.getInt(7) + "\"";
                json = json + ",\"" + datoSql.getInt(8) + "\"";

                json = json + ",\"" + datoSql.getInt(9) + "\"";
                json = json + ",\"" + datoSql.getInt(10) + "\"";
                json = json + ",\"" + datoSql.getInt(11) + "\"]";

                json = json + "}";
                rc = true;
            }
            json = json + "]\n";
            json = json + "}";
            BD.desconectar();
            return json;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            BD.desconectar();
        }
    }

    public String extrasPendientes(LaborBeans laborBeans) throws Exception {
        try {
            String sql = "SELECT   codhoraextra "
                    + "    FROM rhorasextras "
                    + "   WHERE codhoraextra IN ( "
                    + "            SELECT codhoraextra "
                    + "              FROM plaborescontratos plc "
                    + "             WHERE codlabor = ? "
                    + "               AND idgrupo = ? "
                    + "               AND codtipolabor = ? "
                    + "               AND idcontrato = ? ) "
                    + "ORDER BY 1";
            BD.conectar();
            BD.callableStatement(sql);
            BD.AsignarParametro(1, Integer.toString(laborBeans.getLabor()), 2);
            BD.AsignarParametro(2, Integer.toString(laborBeans.getGrupo().getIdgrupo()), 2);
            BD.AsignarParametro(3, Integer.toString(laborBeans.getTipolabor()), 2);
            BD.AsignarParametro(4, Integer.toString(laborBeans.getContrato().getIdcontrato()), 2);
            BD.consultar();

            ResultSet datoSql = BD.obtenerConsulta();
            String respuesta = "";
            while (datoSql.next()) {
                respuesta += datoSql.getString(1) + ",";
            }

            return respuesta;
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        } finally {
            BD.desconectar();
        }

    }

    public List listar(int idcontrato) throws Exception {
        List<LaborBeans> lista = new ArrayList<LaborBeans>();
        try {

            String sql = "SELECT   idlaborcontrato, codlabor, codtipolabor, codhoraextra "
                    + "    FROM plaborescontratos plc "
                    + "   WHERE idcontrato = ? "
                    + "     AND conciliacion = 1 "
                    + "ORDER BY 1";
            BD.conectar();
            BD.callableStatement(sql);
            BD.AsignarParametro(1, Integer.toString(idcontrato), 2);
            BD.consultar();
            ResultSet datoSql = BD.obtenerConsulta();
            LaborBeans labor;
            while (datoSql.next()) {
                labor = new LaborBeans();
                labor.setIdlaborcontrato(datoSql.getInt(1));
                labor.setLabor(datoSql.getInt(2));
                labor.setTipolabor(datoSql.getInt(3));
                labor.setHoraextra(datoSql.getInt(4));
                lista.add(labor);
            }
            return lista;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            BD.desconectar();
        }
    }
}
