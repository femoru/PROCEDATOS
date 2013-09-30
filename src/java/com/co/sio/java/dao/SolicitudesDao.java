/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.co.sio.java.dao;

import com.co.sio.java.db.ControllerPool;
import com.co.sio.java.mbeans.GrupoBeans;
import com.co.sio.java.mbeans.PersonaBeans;
import com.co.sio.java.mbeans.SolicitudBeans;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author fmoctezuma
 */
public class SolicitudesDao {

    ControllerPool BD;

    public SolicitudesDao() {
        BD = new ControllerPool();
    }

    public boolean Guardar(SolicitudBeans solicitudBeans) throws Exception {
        try {
            int perfil = solicitudBeans.getUsuarioSolicitud().getPerfil().getCod_perfil();
            String sql;
            BD.conectar();
            if (perfil == 4) {
                sql = "INSERT INTO dsolicitudes (codtiposolicitud, idusuariosolicitud,fechasolicitud,observacion , idgrupo) "
                        + "VALUES (?,?,SYSDATE,?,?)";
                BD.callableStatement(sql);
                BD.AsignarParametro(4, Integer.toString(solicitudBeans.getGrupo().getIdgrupo()), 2);
            } else {

                sql = "INSERT INTO dsolicitudes (codtiposolicitud, idusuariosolicitud,fechasolicitud,observacion) "
                        + "VALUES (?,?,SYSDATE,?)";
                BD.callableStatement(sql);
            }


            BD.AsignarParametro(1, Integer.toString(solicitudBeans.getSolicitud().getCodigo()), 2);
            BD.AsignarParametro(2, Integer.toString(solicitudBeans.getUsuarioSolicitud().getIdusuario()), 2);
            BD.AsignarParametro(3, solicitudBeans.getObservacion().toUpperCase(), 1);

            return BD.registrar();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            BD.desconectar();
        }
    }

    public boolean actualizar(SolicitudBeans solicitudBeans) throws Exception {
        try {
            String sql = "UPDATE dsolicitudes "
                    + "SET estado = 1, "
                    + "idusuariorespuesta = ?, "
                    + "fecharespuesta = SYSDATE, "
                    + "respuesta = ? "
                    + "WHERE idsolicitud = ?";
            BD.conectar();
            BD.callableStatement(sql);
            BD.AsignarParametro(1, Integer.toString(solicitudBeans.getUsuarioRespuesta().getIdusuario()), 2);
            BD.AsignarParametro(2, solicitudBeans.getRespuesta().toUpperCase(), 1);
            BD.AsignarParametro(3, Integer.toString(solicitudBeans.getIdsolicitud()), 2);

            return BD.registrar();
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        } finally {
            BD.desconectar();
        }
    }

    public String getListSolicitudes(int page, int rows, String sidx, String sord, int idusuario, String fechaInicial, String fechaFinal, int opcion, int estado, int filtro) throws Exception {
        String sql;
        String strQuery;
        String json;

        int perfil = new UsuarioDao().consultar(idusuario).getPerfil().getCod_perfil();

        int total = 0;
        int total1 = 0;
        int total_pages;

        boolean rc;

        ResultSet rsCuenta;
        ResultSet datoSql;

        try {

            BD.conectar();



            if (perfil == 3 || perfil == 1) {
                sql = "SELECT COUNT(*) AS valor FROM dsolicitudes WHERE estado = ? AND idusuariosolicitud = ?";
                BD.callableStatement(sql);
                BD.AsignarParametro(2, Integer.toString(idusuario), 2);
            } else {
                sql = "SELECT COUNT(*) AS valor FROM dsolicitudes WHERE estado = ?";
                BD.callableStatement(sql);
            }

            BD.AsignarParametro(1, Integer.toString(estado), 2);

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

            if (perfil == 3 || perfil == 1) {//Coordinador SIO
                strQuery = "SELECT   ds.idsolicitud, rs.dessolicitud, mp.pnombre, mp.papellido, "
                        + "TO_CHAR (ds.fechasolicitud, 'dd/MM/yyyy hh24:mm') fechasolicitud, "
                        + "ds.observacion, ds.estado, "
                        + "TO_CHAR (ds.fecharespuesta, 'dd/MM/yyyy hh24:mm') fecharespuesta, "
                        + "ds.respuesta ";
                if (filtro != 5) {//Auxiliares

                    strQuery += ", ds.idgrupo "
                            + "FROM dsolicitudes ds "
                            + "INNER JOIN rsolicitudes rs ON rs.codsolicitud = ds.codtiposolicitud "
                            + "INNER JOIN mpersonas mp ON ds.idusuariosolicitud = mp.idpersona "
                            + "INNER JOIN cusuarios cu ON ds.idusuariosolicitud = cu.idusuario "
                            + "WHERE ds.estado = ? AND cu.codperfil = 4 ";

                } else {//todas coordinadorSIO
                    strQuery += "FROM dsolicitudes ds "
                            + "INNER JOIN rsolicitudes rs ON rs.codsolicitud = ds.codtiposolicitud "
                            + "INNER JOIN mpersonas mp ON ds.idusuariosolicitud = mp.idpersona "
                            + "INNER JOIN cusuarios cu ON ds.idusuariosolicitud = cu.idusuario "
                            + "WHERE ds.estado = ? AND cu.codperfil = 5 ";
                }

                if (opcion == 1) {
                    strQuery += "AND ds.fechasolicitud ";
                } else {
                    strQuery += "AND ds.fecharespuesta ";
                }
                strQuery += "BETWEEN to_date ( ? , 'dd/mm/yyyy') AND to_date ( ?, 'dd/mm/yyyy') + 1 ORDER BY 1 DESC";
                BD.callableStatement(strQuery);
                
                BD.AsignarParametro(2, fechaInicial, 1);
                BD.AsignarParametro(3, fechaFinal, 1);
                
            }

            if (perfil == 4) {//Auxiliar

                strQuery = "SELECT ds.idsolicitud, rs.dessolicitud, mp.pnombre, mp.papellido, "
                        + "TO_CHAR (ds.fechasolicitud, 'dd/MM/yyyy hh24:mm') fechasolicitud, "
                        + "ds.observacion, ds.estado, "
                        + "TO_CHAR (ds.fecharespuesta, 'dd/MM/yyyy hh24:mm') fecharespuesta, "
                        + "ds.respuesta, ds.idgrupo "
                        + "FROM dsolicitudes ds "
                        + "INNER JOIN rsolicitudes rs ON rs.codsolicitud = ds.codtiposolicitud "
                        + "INNER JOIN mpersonas mp ON mp.idpersona = ds.idusuariosolicitud "
                        + "WHERE ds.estado = ? "
                        + "AND ds.idusuariosolicitud = ? ";
                if (opcion == 1) {
                    strQuery += "AND ds.fechasolicitud ";
                } else {
                    strQuery += "AND ds.fecharespuesta ";
                }
                strQuery += "BETWEEN to_date ( ? , 'dd/mm/yyyy') AND to_date ( ?, 'dd/mm/yyyy') + 1 ORDER BY 1 DESC";
                BD.callableStatement(strQuery);
                
                BD.AsignarParametro(2, Integer.toString(idusuario), 2);
                BD.AsignarParametro(3, fechaInicial, 1);
                BD.AsignarParametro(4, fechaFinal, 1);
                

            }

            if (perfil == 5) {//Coordinador Grupo

                strQuery = "SELECT   ds.idsolicitud, rs.dessolicitud, mp.pnombre, mp.papellido, "
                        + "TO_CHAR (ds.fechasolicitud, 'dd/MM/yyyy hh24:mm') fechasolicitud, "
                        + "ds.observacion, ds.estado, "
                        + "TO_CHAR (ds.fecharespuesta, 'dd/MM/yyyy hh24:mm') fecharespuesta,ds.respuesta ";
                if (filtro != 5) {//Auxiliares

                    strQuery += ", ds.idgrupo "
                            + "FROM dsolicitudes ds "
                            + "INNER JOIN rsolicitudes rs on rs.codsolicitud = ds.codtiposolicitud "
                            + "INNER JOIN mpersonas mp on ds.idusuariosolicitud = mp.idpersona "
                            + "INNER JOIN dareasgrupos dag ON dag.idgrupo = ds.idgrupo "
                            + "WHERE ds.estado = ? "
                            + "AND dag.idcoordinador = ? ";
                } else {

                    strQuery += "FROM dsolicitudes ds "
                            + "INNER JOIN rsolicitudes rs on rs.codsolicitud = ds.codtiposolicitud "
                            + "INNER JOIN mpersonas mp on ds.idusuariosolicitud = mp.idpersona "
                            + "WHERE ds.estado = ? "
                            + "AND ds.idusuariosolicitud = ? ";
                }
                if (opcion == 1) {
                    strQuery += "AND ds.fechasolicitud ";
                } else {
                    strQuery += "AND ds.fecharespuesta ";
                }
                strQuery += "BETWEEN to_date ( ? , 'dd/mm/yyyy') AND to_date ( ?, 'dd/mm/yyyy') + 1 ORDER BY 1 DESC";
                BD.callableStatement(strQuery);
                

                BD.AsignarParametro(2, Integer.toString(idusuario), 1);
                BD.AsignarParametro(3, fechaInicial, 1);
                BD.AsignarParametro(4, fechaFinal, 1);


            }
            BD.AsignarParametro(1, Integer.toString(estado), 2);
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
                json = json + "\"id\":\"" + datoSql.getInt("idsolicitud") + "\",";
                json = json + "\"cell\":[" + datoSql.getInt("idsolicitud") + "";
                json = json + ",\"" + datoSql.getString("dessolicitud") + "\"";
                if (filtro != 5 || perfil == 4) {
                    int idgrupo = datoSql.getInt("idgrupo");
                    GrupoBeans grupo = new GruposDao().consultar(idgrupo);
                    PersonaBeans persona = new PersonaDao().consultar(grupo.getCordinador().getIdusuario());
                    json = json + ",\"" + persona.getpNombre() + " " + persona.getpApellido() + "\"";
                } else {
                    json = json + ",\"" + "Registro sin coordinador" + "\"";
                }
                json = json + ",\"" + datoSql.getString("pnombre") + " " + datoSql.getString("papellido") + "\"";
                json = json + ",\"" + ((datoSql.getString("fechasolicitud") == null) ? "" : datoSql.getString("fechasolicitud")) + "\"";
                json = json + ",\"" + datoSql.getString("observacion") + "\"";
                json = json + ",\"" + datoSql.getInt("estado") + "\"";
                json = json + ",\"" + ((datoSql.getString("fecharespuesta") == null) ? "" : datoSql.getString("fecharespuesta")) + "\"";
                json = json + ",\"" + ((datoSql.getString("respuesta") == null) ? "" : datoSql.getString("respuesta")) + "\"]";
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

    public SolicitudBeans consultar(int idsolicitud) throws Exception {
        SolicitudBeans solicitudBeans = new SolicitudBeans();
        try {
            String sql = "SELECT idsolicitud, codtiposolicitud, idusuariosolicitud, fechasolicitud, "
                    + "observacion, estado, idusuariorespuesta, fecharespuesta, respuesta "
                    + "FROM dsolicitudes "
                    + "WHERE idsolicitud = ? ";
            BD.conectar();
            BD.callableStatement(sql);
            BD.AsignarParametro(1, Integer.toString(idsolicitud), 2);
            BD.consultar();

            ResultSet datoSql = BD.obtenerConsulta();

            if (datoSql.next()) {
                solicitudBeans.setIdsolicitud(datoSql.getInt("idsolicitud"));
                solicitudBeans.setSolicitud(new ReferenciasDao().consultar(datoSql.getInt("codtiposolicitud"), "RLABORES"));
                solicitudBeans.setUsuarioSolicitud(new UsuarioDao().consultar(datoSql.getInt("idusuariosolicitud")));
                solicitudBeans.setFechaSolicitud(datoSql.getString("fechasolicitud"));
                solicitudBeans.setObservacion(datoSql.getString("observacion"));
                solicitudBeans.setEstado(datoSql.getInt("estado"));
                solicitudBeans.setUsuarioRespuesta(new UsuarioDao().consultar(datoSql.getInt("idusuariorespuesta")));
                solicitudBeans.setFechaRespuesta(datoSql.getString("fecharespuesta"));
                solicitudBeans.setRespuesta(datoSql.getString("respuesta"));
            }


        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        } finally {
            BD.desconectar();
        }
        return solicitudBeans;
    }

    public boolean anular(SolicitudBeans solicitudBeans) throws Exception {
        try {
            String sql = "UPDATE dsolicitudes "
                    + "SET estado = 2 "
                    + "WHERE idsolicitud = ? ";
            BD.conectar();
            BD.callableStatement(sql);
            BD.AsignarParametro(1, Integer.toString(solicitudBeans.getIdsolicitud()), 2);

            return BD.registrar();

        } catch (SQLException ex) {
            throw new Exception(ex.getMessage());
        } finally {
            BD.desconectar();
        }


    }
}
