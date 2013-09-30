package com.co.sio.java.dao;

import com.co.sio.java.db.ControllerPool;
import com.co.sio.java.mbeans.PerfilBeans;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jcarvajal
 */
public class PerfilDao {

    private ControllerPool BD;

    public PerfilDao() {
        BD = new ControllerPool();
    }

    public boolean insertar(PerfilBeans perfil) throws Exception {
        try {
            String sql = "INSERT INTO cperfiles(codperfil,desperfil,activo,idusuario,fechadigita,menu) "
                    + "VALUES (SEQ_CPERFILES.NEXTVAL,?,?,?,SYSDATE,?)";
            BD.conectar();
            BD.callableStatement(sql);
            BD.AsignarParametro(1, perfil.getDes_perfil().toUpperCase(), 1);
            BD.AsignarParametro(2, Integer.toString(perfil.getEstado()), 2);
            BD.AsignarParametro(3, Integer.toString(perfil.getUsuario().getIdusuario()), 2);
            BD.AsignarParametro(4, perfil.getMenu(), 1);
            return BD.registrar();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            BD.desconectar();
        }
    }

    public boolean actualizar(PerfilBeans perfil) throws Exception {
        try {
            String sql = "UPDATE cperfiles SET activo = ?,idusuario = ? ,fechadigita = SYSDATE "
                    + "WHERE codperfil = ?";
            BD.conectar();
            BD.callableStatement(sql);
            BD.AsignarParametro(1, Integer.toString(perfil.getEstado()), 2);
            BD.AsignarParametro(2, Integer.toString(perfil.getUsuario().getIdusuario()), 2);
            BD.AsignarParametro(3, Integer.toString(perfil.getCod_perfil()), 2);
            return BD.registrar();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            BD.desconectar();
        }
    }
    
    public boolean actualizarModifica(int perfil,int usuario) throws Exception {
        try {
            String sql = "UPDATE cperfiles SET idusuario = ? ,fechadigita = SYSDATE "
                    + "WHERE codperfil = ?";
            BD.conectar();
            BD.callableStatement(sql);
            BD.AsignarParametro(1, Integer.toString(perfil), 2);
            BD.AsignarParametro(2, Integer.toString(usuario), 2);
            return BD.registrar();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            BD.desconectar();
        }
    }

    public List listar() throws Exception {
        PerfilBeans perfil;
        List perfilList = new ArrayList();
        try {
            ResultSet datoSql;
            String sql = "SELECT cp.codperfil, cp.desperfil,cp.activo, cp.fechadigita,cp.menu "
                    + "FROM cperfiles cp "
                    + "ORDER BY cp.codperfil ASC";

            BD.conectar();
            BD.callableStatement(sql);
            BD.consultar();

            datoSql = BD.obtenerConsulta();

            while (datoSql.next()) {
                perfil = new PerfilBeans();
                perfil.setCod_perfil(datoSql.getInt(1));
                perfil.setDes_perfil(datoSql.getString(2));
                perfil.setEstado(datoSql.getInt(3));
                perfil.setMenu(datoSql.getString(5));
                perfilList.add(perfil);
            }
            return perfilList;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            BD.desconectar();
        }
    }

    public String getListPerfil(int page, int rows, String sidx, String sord) throws Exception {
        String sql;
        String strQuery;
        String json;

        int start = 0;
        int total = 0;
        int total1 = 0;
        int total_pages;

        boolean rc, estado;

        ResultSet rsCuenta;
        ResultSet datoSql;

        try {


            sql = "Select count(*) as valor from cperfiles";

            if ("".equals(sidx)) {
                sidx = "1";
            }

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

            start = ((rows * page) - rows) + 1;


            strQuery = "select rownum fila, "
                    + " cp.codperfil, cp.desperfil, "
                    + "cp.activo "
                    + "FROM cperfiles cp "
                    + "ORDER BY 2";
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

                if ("1".equals(datoSql.getString("activo"))) {
                    estado = true;
                } else {
                    estado = false;
                }

                json = json + "\n{";
                json = json + "\"id\":\"" + datoSql.getInt("codperfil") + "\",";
                json = json + "\"cell\":[" + datoSql.getInt("codperfil") + "";
                json = json + ",\"" + datoSql.getString("desperfil") + "\"";
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

    public PerfilBeans consultar(int codPerfil) throws Exception {
        PerfilBeans perfilBeans = new PerfilBeans();
        try {
            String sql = "SELECT "
                    + "c.codperfil, c.desperfil, c.activo, "
                    + "c.idusuario, c.fechadigita, c.menu "
                    + "FROM cperfiles c "
                    + "WHERE c.codperfil = ? ";

            BD.conectar();
            BD.callableStatement(sql);
            BD.AsignarParametro(1, Integer.toString(codPerfil), 2);
            BD.consultar();
            ResultSet datoSql = BD.obtenerConsulta();

            if (datoSql.next()) {
                perfilBeans.setCod_perfil(datoSql.getInt("codperfil"));
                perfilBeans.setDes_perfil(datoSql.getString("desperfil"));
                perfilBeans.setEstado(datoSql.getInt("activo"));
                perfilBeans.setMenu(datoSql.getString("menu"));
            }
            return perfilBeans;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            BD.desconectar();
        }
    }
    
    
    
}
