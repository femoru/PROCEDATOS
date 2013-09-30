/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.co.sio.java.dao;

import com.co.sio.java.db.ControllerPool;
import com.co.sio.java.mbeans.ContratoBeans;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author fmoctezuma
 */
public class ContratosDao {

    private ControllerPool BD;

    public ContratosDao() {
        BD = new ControllerPool();
    }

    public ContratoBeans consultar(int idcontrato) throws Exception {
        try {
            ContratoBeans contrato = null;
            String sql = "SELECT idcontrato, numcontrato, fechainicio, fechafin, activo, idcliente "
                    + "  FROM pcontratos "
                    + " WHERE idcontrato = ?";
            BD.conectar();
            BD.callableStatement(sql);
            BD.AsignarParametro(1, Integer.toString(idcontrato), 2);
            BD.consultar();
            ResultSet datoSql = BD.obtenerConsulta();
            ClientesDao clientesDao = new ClientesDao();
            if (datoSql.next()) {
                contrato = new ContratoBeans();
                contrato.setIdcontrato(idcontrato);
                contrato.setNumcontrato(datoSql.getString("numcontrato"));
                contrato.setFechainicio(datoSql.getString("fechainicio"));
                contrato.setFechafin(datoSql.getString("fechafin"));
                contrato.setActivo(datoSql.getInt("activo"));
                contrato.setCliente(clientesDao.consultar(datoSql.getInt("idcliente")));
            }
            return contrato;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            BD.desconectar();
        }
    }

    public boolean Guardar(ContratoBeans contratoBeans) throws Exception {
        try {
            String sql = "INSERT INTO pcontratos(numcontrato, fechainicio, fechafin, activo, idcliente) "
                    + "VALUES (?, to_date(?, 'dd/MM/yy'), to_date(?, 'dd/MM/yy'), ?, ?)";
            BD.conectar();
            BD.callableStatement(sql);
            BD.AsignarParametro(1, contratoBeans.getNumcontrato().toUpperCase(), 1);
            BD.AsignarParametro(2, contratoBeans.getFechainicio().toString(), 1);
            BD.AsignarParametro(3, contratoBeans.getFechafin().toString(), 1);
            BD.AsignarParametro(4, Integer.toString(contratoBeans.getActivo()), 2);
            BD.AsignarParametro(5, Integer.toString(contratoBeans.getCliente().getIdcliente()), 2);
            return BD.registrar();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            BD.desconectar();
        }

    }

    public boolean Actualizar(ContratoBeans contratoBeans) throws Exception {
        try {
            String sql = "UPDATE pcontratos "
                    + "SET numcontrato = ?, "
                    + "fechainicio = to_date(?, 'dd/MM/yy'), "
                    + "fechafin = to_date(?, 'dd/MM/yy'), "
                    + "activo = ? "
                    + "WHERE idcontrato = ? ";
            BD.conectar();
            BD.callableStatement(sql);
            BD.AsignarParametro(1, contratoBeans.getNumcontrato().toUpperCase(), 1);
            BD.AsignarParametro(2, contratoBeans.getFechainicio().toString(), 1);
            BD.AsignarParametro(3, contratoBeans.getFechafin().toString(), 1);
            BD.AsignarParametro(4, Integer.toString(contratoBeans.getActivo()), 2);
            BD.AsignarParametro(5, Integer.toString(contratoBeans.getIdcontrato()), 2);
            return BD.registrar();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            BD.desconectar();
        }
    }

    public String getListContratos(int page, int rows, String sidx, String sord, int idcliente) throws Exception {
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

            sql = "SELECT COUNT(*) AS valor FROM pcontratos "
                    + "WHERE idcliente = ?";

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

            strQuery = "SELECT idcontrato, numcontrato, fechainicio, fechafin, activo,idcliente "
                    + "FROM pcontratos "
                    + "WHERE idcliente = ? "
                    + "ORDER BY 1 ASC ";
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
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            while (datoSql.next()) {
                if (rc) {
                    json = json + ",";
                }
                json = json + "\n{";
                json = json + "\"id\":\"" + datoSql.getInt("idcontrato") + "\",";
                json = json + "\"cell\":[" + datoSql.getInt("idcontrato") + "";
                json = json + ",\"" + datoSql.getString("numcontrato") + "\"";
                json = json + ",\"" + sdf.format(datoSql.getDate("fechainicio")) + "\"";
                json = json + ",\"" + sdf.format(datoSql.getDate("fechafin")) + "\"";
                json = json + ",\"" + datoSql.getInt("activo") + "\"]";
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

    public List listar() throws Exception {
        List<ContratoBeans> lista = new ArrayList<ContratoBeans>();
        try {

            String sql = "SELECT   idcontrato, numcontrato, idcliente "
                    + "    FROM pcontratos "
                    + "ORDER BY 3,1";
            BD.conectar();
            BD.callableStatement(sql);
            BD.consultar();
            ResultSet datoSql = BD.obtenerConsulta();
            ContratoBeans contrato;
            while (datoSql.next()) {
                contrato = new ContratoBeans();
                contrato.setIdcontrato(datoSql.getInt(1));
                contrato.setNumcontrato(datoSql.getString(2));
                contrato.setCliente(new ClientesDao().consultar(datoSql.getInt(3)));
                lista.add(contrato);
            }
            return lista;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            BD.desconectar();
        }
    }
}
