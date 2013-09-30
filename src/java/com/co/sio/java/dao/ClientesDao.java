/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.co.sio.java.dao;

import com.co.sio.java.db.ControllerPool;
import com.co.sio.java.mbeans.ClienteBeans;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author fmoctezuma
 */
public class ClientesDao {

    ControllerPool BD;

    public ClientesDao() {
        BD = new ControllerPool();
    }

    public boolean Guardar(ClienteBeans clienteBeans) throws Exception {

        try {

            String sql = "INSERT INTO mclientes "
                    + "(nitcliente, nomcliente, direccion, telefono, contacto,telefonocontacto, email) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?)";

            BD.conectar();
            BD.callableStatement(sql);
            BD.AsignarParametro(1, clienteBeans.getNitcliente(), 1);
            BD.AsignarParametro(2, clienteBeans.getNomcliente().toUpperCase(), 1);
            BD.AsignarParametro(3, clienteBeans.getDireccion().toUpperCase(), 1);
            BD.AsignarParametro(4, clienteBeans.getTelefono(), 1);
            BD.AsignarParametro(5, clienteBeans.getContacto(), 1);
            BD.AsignarParametro(6, clienteBeans.getTelefonocontacto(), 1);
            BD.AsignarParametro(7, clienteBeans.getEmail().toUpperCase(), 1);

            return BD.registrar();


        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            BD.desconectar();
        }
    }

    public boolean Actualizar(ClienteBeans clienteBeans) throws Exception {

        try {
            String sql = "UPDATE mclientes "
                    + "SET nitcliente = ?, "
                    + "nomcliente = ?, "
                    + "direccion = ?, "
                    + "telefono = ?, "
                    + "contacto = ?, "
                    + "telefonocontacto = ?, "
                    + "email = ? "
                    + "WHERE idcliente = ?";
            BD.conectar();
            BD.callableStatement(sql);
            BD.AsignarParametro(1, clienteBeans.getNitcliente(), 1);
            BD.AsignarParametro(2, clienteBeans.getNomcliente().toUpperCase(), 1);
            BD.AsignarParametro(3, clienteBeans.getDireccion().toUpperCase(), 1);
            BD.AsignarParametro(4, clienteBeans.getTelefono(), 1);
            BD.AsignarParametro(5, clienteBeans.getContacto(), 1);
            BD.AsignarParametro(6, clienteBeans.getTelefonocontacto(), 1);
            BD.AsignarParametro(7, clienteBeans.getEmail().toUpperCase(), 1);
            BD.AsignarParametro(8, Integer.toString(clienteBeans.getIdcliente()), 2);

            return BD.registrar();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            BD.desconectar();
        }
    }

    public ClienteBeans consultar(int idcliente) throws Exception {
        ClienteBeans clienteBeans = new ClienteBeans();
        try {
            String sql = "SELECT idcliente, nitcliente, nomcliente, direccion, telefono, contacto, telefonocontacto, email "
                    + "FROM mclientes "
                    + "WHERE idcliente = ?";

            BD.conectar();
            BD.callableStatement(sql);
            BD.AsignarParametro(1, Integer.toString(idcliente), 2);
            BD.consultar();

            ResultSet datoSql = BD.obtenerConsulta();
            if (datoSql.next()) {
                clienteBeans.setIdcliente(datoSql.getInt("idcliente"));
                clienteBeans.setNitcliente(datoSql.getString("nitcliente"));
                clienteBeans.setNomcliente(datoSql.getString("nomcliente"));
                clienteBeans.setDireccion(datoSql.getString("direccion"));
                clienteBeans.setTelefono(datoSql.getString("telefono"));
                clienteBeans.setContacto(datoSql.getString("contacto"));
                clienteBeans.setTelefonocontacto(datoSql.getString("telefonocontacto"));
                clienteBeans.setEmail(datoSql.getString("email"));
            }
            return clienteBeans;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            BD.desconectar();
        }
    }

    public String getListClientes(int page, int rows, String sidx, String sord) throws Exception {
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

            sql = "SELECT COUNT(*) AS valor FROM mclientes";

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

            strQuery = "SELECT idcliente, nitcliente, nomcliente, direccion, telefono, contacto, telefonocontacto, email "
                    + "FROM mclientes "
                    + "ORDER BY 1 ";
            BD.callableStatement(strQuery);

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
                json = json + "\"id\":\"" + datoSql.getInt("idcliente") + "\",";
                json = json + "\"cell\":[" + datoSql.getInt("idcliente") + "";
                json = json + ",\"" + datoSql.getString("nitcliente") + "\"";
                json = json + ",\"" + datoSql.getString("nomcliente") + "\"";
                json = json + ",\"" + datoSql.getString("direccion") + "\"";
                json = json + ",\"" + datoSql.getString("telefono") + "\"";
                json = json + ",\"" + datoSql.getString("contacto") + "\"";
                json = json + ",\"" + datoSql.getString("telefonocontacto") + "\"";
                json = json + ",\"" + datoSql.getString("email") + "\"]";
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
    
    public String getListClientesContratos(int page, int rows, String sidx, String sord) throws Exception {
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

            sql = "SELECT COUNT(*) AS valor FROM pcontratos WHERE activo = 1";

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

            strQuery = "SELECT pc.idcontrato, pc.numcontrato, mc.nomcliente "
                    + "FROM mclientes mc, pcontratos pc "
                    + "WHERE pc.idcliente = mc.idcliente "
                    + "ORDER BY 1 ";
            BD.callableStatement(strQuery);

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
                json = json + "\"id\":\"" + datoSql.getInt("idcontrato") + "\",";
                json = json + "\"cell\":[" + datoSql.getInt("idcontrato") + "";
                json = json + ",\""+ datoSql.getString("nomcliente") + " - " + datoSql.getString("numcontrato")   + "\"]";
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
        List<ClienteBeans> lista = new ArrayList<ClienteBeans>();
        try {
            
            String sql="SELECT idcliente, nomcliente "
                    + "FROM mclientes "
                    + "ORDER BY 1";
            BD.conectar();
            BD.callableStatement(sql);
            BD.consultar();
            ResultSet datoSql=BD.obtenerConsulta();
            ClienteBeans cliente;
            while(datoSql.next()){
                cliente=new ClienteBeans();
                cliente.setIdcliente(datoSql.getInt("idcliente"));
                cliente.setNomcliente(datoSql.getString("nomcliente"));
                lista.add(cliente);
            }
            return lista;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            BD.desconectar();
        }
    }
}
