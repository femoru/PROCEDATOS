/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.co.sio.java.dao;

import com.co.sio.java.db.ControllerPool;
import com.co.sio.java.mbeans.AreaBeans;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author fmoctezuma
 */
public class AreasDao {
    
    ControllerPool BD;
    
    public AreasDao() {
        BD = new ControllerPool();
    }
    
    public String getListAreas(int page, int rows, String sidx, String sord, int idcliente) throws Exception {
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
            
            sql = "SELECT COUNT(*) AS valor FROM dclientesareas "
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
            
            strQuery = "SELECT   idarea, ra.codarea, desarea, idcliente, activo "
                    + "FROM rareas ra "
                    + "INNER JOIN dclientesareas dca ON ra.codarea = dca.codarea "
                    + "AND idcliente = ? "
                    + "ORDER BY 2 ASC ";
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
                json = json + "\"id\":\"" + datoSql.getInt("idarea") + "\",";
                json = json + "\"cell\":[" + datoSql.getInt("idarea") + "";
                json = json + ",\"" + datoSql.getString("desarea") + "\"";
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
    
    public boolean guardar(AreaBeans areaBeans) throws Exception {
        try {
            areaBeans = consultar(areaBeans);
            if (areaBeans.getId() == 0) {
                String sql = "INSERT INTO dclientesareas(codarea,idcliente, activo) "
                        + "VALUES (?, ?, ?) ";
                
                BD.conectar();
                BD.callableStatement(sql);
                BD.AsignarParametro(1, Integer.toString(areaBeans.getArea()), 2);
                BD.AsignarParametro(2, Integer.toString(areaBeans.getCliente().getIdcliente()), 2);
                BD.AsignarParametro(3, Integer.toString(areaBeans.getActivo()), 2);
                
                return BD.registrar();
            } else {
                return false;
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            BD.desconectar();
        }
    }
    
    public boolean actualizar(AreaBeans areaBeans) throws Exception {
        try {
            String sql = "UPDATE dclientesareas "
                    + "SET activo = ? "
                    + "WHERE idarea= ?";
            
            BD.conectar();
            BD.callableStatement(sql);
            BD.AsignarParametro(1, Integer.toString(areaBeans.getActivo()), 2);
            BD.AsignarParametro(2, Integer.toString(areaBeans.getId()), 2);
            
            return BD.registrar();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            BD.desconectar();
        }
    }
    
    public AreaBeans consultar(AreaBeans areaBeans) throws Exception {
        
        try {
            String sql = "SELECT idarea, codarea, idcliente, activo "
                    + "FROM dclientesareas "
                    + "WHERE codarea = ? "
                    + "AND idcliente = ? ";
            
            BD.conectar();
            BD.callableStatement(sql);
            BD.AsignarParametro(1, Integer.toString(areaBeans.getArea()), 2);
            BD.AsignarParametro(2, Integer.toString(areaBeans.getCliente().getIdcliente()), 2);
            
            BD.consultar();
            
            ResultSet datoSql = BD.obtenerConsulta();
            
            if (datoSql.next()) {
                areaBeans.setId(datoSql.getInt("idarea"));
                areaBeans.setActivo(datoSql.getInt("activo"));
            }
            
            return areaBeans;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            BD.desconectar();
        }
    }

    public AreaBeans consultar(int idarea) throws Exception {
        
        try {
            AreaBeans areaBeans = new AreaBeans();
            String sql = "SELECT idarea, codarea, idcliente, activo "
                    + "FROM dclientesareas "
                    + "WHERE idarea = ? ";
            
            
            BD.conectar();
            BD.callableStatement(sql);
            BD.AsignarParametro(1, Integer.toString(idarea), 2);
            
            
            BD.consultar();
            
            ResultSet datoSql = BD.obtenerConsulta();
            
            if (datoSql.next()) {
                
                areaBeans.setArea(datoSql.getInt("codarea"));
                areaBeans.setId(datoSql.getInt("idarea"));
                areaBeans.setCliente(new ClientesDao().consultar(datoSql.getInt("idcliente")));
                areaBeans.setActivo(datoSql.getInt("activo"));
            }
            
            return areaBeans;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            BD.desconectar();
        }
    }
    
    public List listar(String idcontrato) throws Exception {
        List<AreaBeans> list = new ArrayList<AreaBeans>();
        try {
            String sql = "SELECT   dca.idarea, dca.codarea, dca.idcliente, dca.activo "
                    + "FROM dclientesareas dca "
                    + "INNER JOIN pcontratos pcon ON dca.idcliente = pcon.idcliente "
                    + "AND pcon.idcontrato = ? "
                    + "AND dca.activo = 1"
                    + "ORDER BY 1";
            BD.conectar();
            BD.callableStatement(sql);
            BD.AsignarParametro(1, idcontrato, 2);
            BD.consultar();
            ResultSet datoSql = BD.obtenerConsulta();
            AreaBeans areaBeans;
            while (datoSql.next()) {
                areaBeans = new AreaBeans();
                areaBeans.setId(datoSql.getInt("idarea"));
                areaBeans.setArea(datoSql.getInt("codarea"));
                areaBeans.setCliente(new ContratosDao().consultar(Integer.parseInt(idcontrato)).getCliente());
                areaBeans.setActivo(datoSql.getInt("activo"));
                list.add(areaBeans);
            }
            return list;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            BD.desconectar();
        }
    }
}
