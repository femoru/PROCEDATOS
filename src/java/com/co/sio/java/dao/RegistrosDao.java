/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.co.sio.java.dao;

import com.co.sio.java.db.ControllerPool;
import com.co.sio.java.mbeans.LaborBeans;
import com.co.sio.java.mbeans.ReferenciasBeans;
import com.co.sio.java.mbeans.RegistroBeans;
import com.co.sio.java.mbeans.UsuarioBeans;
import java.sql.ResultSet;

/**
 *
 * @author fmoctezuma
 */
public class RegistrosDao {

    private ControllerPool BD;

    public RegistrosDao() {
        BD = new ControllerPool();
    }

    public boolean Guardar(RegistroBeans registroBeans) throws Exception {

        RegistroBeans temp = consultar(registroBeans.getUsuario().getIdusuario());
        if (temp.getFechaFin() != null || temp.getIdregistro() == 0) {
            return registrarEntrada(registroBeans);
        } else {
            return false;
        }

    }

    public boolean actualizar(RegistroBeans registroBeans) throws Exception {
        try {
            String sql = "UPDATE mregistros "
                    + "SET idlaborcontrato = ?, "
                    + "fechainicio = to_date( ? , 'DD/MM/YYYY HH24:MI' ), "
                    + "fechafin = to_date( ? , 'DD/MM/YYYY HH24:MI' ), "
                    + "estado = ?, "
                    + "observacion = ?, "
                    + "datolabor = ?, "
                    + "registroslabor = ?, "
                    + "anulado = ?, "
                    + "costo = ?, "
                    + "valor = ? "
                    + "WHERE idregistro = ? ";
            BD.conectar();
            BD.callableStatement(sql);
            BD.AsignarParametro(1, Integer.toString(registroBeans.getLabor().getIdlaborcontrato()), 2);
            BD.AsignarParametro(2, registroBeans.getFechaInicio(), 1);
            BD.AsignarParametro(3, registroBeans.getFechaFin(), 1);
            BD.AsignarParametro(4, Integer.toString(registroBeans.getEstado()), 2);
            BD.AsignarParametro(5, registroBeans.getObservacion(), 1);
            BD.AsignarParametro(6, registroBeans.getDatoLabor(), 1);
            BD.AsignarParametro(7, Integer.toString(registroBeans.getRegistroslabor()), 2);
            BD.AsignarParametro(8, Integer.toString(registroBeans.getAnulado().getCodigo()), 2);
            BD.AsignarParametro(9, registroBeans.getLabor().getCosto(), 2);
            BD.AsignarParametro(10, registroBeans.getLabor().getValor(), 2);
            BD.AsignarParametro(11, Integer.toString(registroBeans.getIdregistro()), 2);



            return BD.registrar();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            BD.desconectar();
        }
    }

    public RegistroBeans consultar(int idusuario) throws Exception {
        try {
            /*
             String sql = "SELECT idregistro, idusuario, idlaborcontrato, "
             + "TO_CHAR(fechainicio,'DD/MM/YYYY HH24:MI') AS fechainicio, "
             + "TO_CHAR(fechafin,'DD/MM/YYYY HH24:MI') AS fechafin, estado, anulado, "
             + "observacion, datolabor, tiempolabor, registroslabor, idnomina, valor, costo "
             + "FROM mregistros "
             + "WHERE idusuario = ?  "
             + "AND fechafin IS NULL "
             + "AND estado = 0 "
             + "ORDER BY 1 DESC";
             */

            String sql = "SELECT idregistro, idusuario, idlaborcontrato, "
                    + "TO_CHAR(fechainicio,'DD/MM/YYYY HH24:MI') AS fechainicio, "
                    + "TO_CHAR(fechafin,'DD/MM/YYYY HH24:MI') AS fechafin, estado, anulado, "
                    + "observacion, datolabor, tiempolabor, registroslabor, idnomina, valor, costo "
                    + "FROM mregistros "
                    + "WHERE fechainicio = "
                    + "                (SELECT MAX(fechainicio) "
                    + "                KEEP( "
                    + "                    DENSE_RANK first "
                    + "                    ORDER BY fechainicio DESC NULLS LAST "
                    + "                 ) AS ultima_fecha_registro "
                    + "                 FROM mregistros "
                    + "                 WHERE idusuario = ? ) ";

            BD.conectar();
            BD.callableStatement(sql);
            BD.AsignarParametro(1, Integer.toString(idusuario), 2);
            BD.consultar();

            ResultSet datoSql = BD.obtenerConsulta();
            RegistroBeans registroBeans = new RegistroBeans();

            if (datoSql.next()) {
                registroBeans.setIdregistro(datoSql.getInt(1));
                registroBeans.setUsuario(new UsuarioDao().consultar(datoSql.getInt(2)));
                registroBeans.setLabor(new LaboresDao().consultar(datoSql.getInt(3)));
                registroBeans.setFechaInicio(datoSql.getString(4));
                registroBeans.setFechaFin(datoSql.getString(5));
                registroBeans.setEstado(datoSql.getInt(6));
                registroBeans.setAnulado(new ReferenciasDao().consultar(datoSql.getInt(7), "RCAUSASANULACION"));
                registroBeans.setObservacion(datoSql.getString(8));
                registroBeans.setDatoLabor(datoSql.getString(9));
                registroBeans.setTiempolabor(datoSql.getInt(10));
                registroBeans.setRegistroslabor(datoSql.getInt(11));
                registroBeans.setIdnomina(datoSql.getInt(12));
                registroBeans.setValor(datoSql.getInt(13));
                registroBeans.setCosto(datoSql.getInt(14));
            }
            return registroBeans;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            BD.desconectar();
        }
    }

    public RegistroBeans consultar(String idregistro) throws Exception {

        try {
            String sql = "SELECT idregistro, idusuario, idlaborcontrato, "
                    + "TO_CHAR(fechainicio,'DD/MM/YYYY HH24:MI') AS fechainicio, "
                    + "TO_CHAR(fechafin,'DD/MM/YYYY HH24:MI') AS fechafin, estado, anulado, "
                    + "observacion, datolabor, tiempolabor, registroslabor, idnomina, valor, costo "
                    + "FROM mregistros "
                    + "WHERE idregistro = ? ";

            BD.conectar();
            BD.callableStatement(sql);
            BD.AsignarParametro(1, idregistro, 2);
            BD.consultar();

            ResultSet datoSql = BD.obtenerConsulta();
            RegistroBeans registroBeans = new RegistroBeans();
            if (datoSql.next()) {
                registroBeans.setIdregistro(datoSql.getInt(1));

                UsuarioBeans usuarioBeans = new UsuarioBeans();
                usuarioBeans.setIdusuario(datoSql.getInt(2));
                registroBeans.setUsuario(usuarioBeans);

                LaborBeans laborBeans = new LaboresDao().consultar(datoSql.getInt(3));
                registroBeans.setLabor(laborBeans);

                registroBeans.setFechaInicio(datoSql.getString(4));
                registroBeans.setFechaFin(datoSql.getString(5));
                registroBeans.setEstado(datoSql.getInt(6));

                ReferenciasBeans anulado = new ReferenciasBeans();
                anulado.setCodigo(datoSql.getInt(7));
                registroBeans.setAnulado(anulado);

                registroBeans.setObservacion(datoSql.getString(8));
                registroBeans.setDatoLabor(datoSql.getString(9));
                registroBeans.setTiempolabor(datoSql.getInt(10));
                registroBeans.setRegistroslabor(datoSql.getInt(11));
                registroBeans.setIdnomina(datoSql.getInt(12));
                registroBeans.setValor(datoSql.getInt(13));
                registroBeans.setCosto(datoSql.getInt(14));
            }
            return registroBeans;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            BD.desconectar();
        }
    }

    public RegistroBeans consultarAnterior(String idregistro) throws Exception {

        try {
            String sql = "SELECT idregistro "
                    + "FROM mregistros "
                    + "WHERE idregistro < (SELECT idregistro "
                    + "                    FROM mregistros "
                    + "                    WHERE idregistro = ?) "
                    + "AND idusuario = (SELECT idusuario "
                    + "                 FROM mregistros "
                    + "                 WHERE idregistro = ?) "
                    + "AND ROWNUM <= 1 "
                    + "GROUP BY idregistro, idusuario "
                    + "ORDER BY idregistro DESC";
            BD.conectar();
            BD.callableStatement(sql);
            BD.AsignarParametro(1, idregistro, 2);
            BD.AsignarParametro(2, idregistro, 2);

            BD.consultar();

            ResultSet datoSql = BD.obtenerConsulta();
            String registro = "0";
            if (datoSql.next()) {
                registro = datoSql.getString("idregistro");
            }
            BD.desconectar();
            return consultar(registro);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            BD.desconectar();
        }

    }

    public RegistroBeans consultarSiguiente(String idregistro) throws Exception {

        try {
            String sql = "SELECT idregistro "
                    + "FROM mregistros "
                    + "WHERE idregistro > (SELECT idregistro "
                    + "                    FROM mregistros "
                    + "                    WHERE idregistro = ?) "
                    + "AND idusuario = (SELECT idusuario "
                    + "                 FROM mregistros "
                    + "                 WHERE idregistro = ?) "
                    + "AND ROWNUM <= 1 "
                    + "GROUP BY idregistro, idusuario "
                    + "ORDER BY idregistro ASC";
            BD.conectar();
            BD.callableStatement(sql);
            BD.AsignarParametro(1, idregistro, 2);
            BD.AsignarParametro(2, idregistro, 2);

            BD.consultar();

            ResultSet datoSql = BD.obtenerConsulta();
            String registro = "0";
            if (datoSql.next()) {
                registro = datoSql.getString("idregistro");
            }
            BD.desconectar();
            return consultar(registro);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            BD.desconectar();
        }

    }

    public boolean insertar(RegistroBeans registroBeans) throws Exception {

        try {
            String sql;
            BD.conectar();

            sql = "INSERT INTO MREGISTROS ( IDUSUARIO, IDLABORCONTRATO, FECHAINICIO, FECHAFIN, ESTADO, "
                    + "ANULADO, OBSERVACION, DATOLABOR, registroslabor, valor, costo, idnomina, tiempolabor) "
                    + "VALUES ( ?, ?, to_date( ? , 'DD/MM/YYYY HH24:MI' ), to_date( ? , 'DD/MM/YYYY HH24:MI' ), ?, "
                    + "?, ?, ?, ?, ?, ?, ?, ? )";
            BD.callableStatement(sql);
            BD.AsignarParametro(1, Integer.toString(registroBeans.getUsuario().getIdusuario()), 2);
            BD.AsignarParametro(2, Integer.toString(registroBeans.getLabor().getIdlaborcontrato()), 2);
            BD.AsignarParametro(3, registroBeans.getFechaInicio(), 1);
            BD.AsignarParametro(4, registroBeans.getFechaFin(), 1);
            BD.AsignarParametro(5, Integer.toString(registroBeans.getEstado()), 2);
            BD.AsignarParametro(6, Integer.toString(registroBeans.getAnulado().getCodigo()), 2);
            BD.AsignarParametro(7, registroBeans.getObservacion(), 1);
            BD.AsignarParametro(8, registroBeans.getDatoLabor(), 1);
            BD.AsignarParametro(9, Integer.toString(registroBeans.getRegistroslabor()), 2);
            BD.AsignarParametro(10, Integer.toString(registroBeans.getValor()), 2);
            BD.AsignarParametro(11, Integer.toString(registroBeans.getCosto()), 2);
            BD.AsignarParametro(12, Integer.toString(registroBeans.getIdnomina()), 2);
            BD.AsignarParametro(13, Integer.toString(registroBeans.getTiempolabor()), 2);

            return BD.registrar();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            BD.desconectar();
        }
    }

    public boolean registrarEntrada(RegistroBeans registroBeans) throws Exception {
        try {
            String sql;
            BD.conectar();
            sql = "INSERT INTO mregistros ( idusuario, idlaborcontrato, estado, observacion, valor, costo) "
                    + "VALUES (?,?,0,'',?,?) ";
            BD.callableStatement(sql);
            BD.AsignarParametro(1, Integer.toString(registroBeans.getUsuario().getIdusuario()), 2);
            BD.AsignarParametro(2, Integer.toString(registroBeans.getLabor().getIdlaborcontrato()), 2);
            BD.AsignarParametro(3, Integer.toString(registroBeans.getValor()), 2);
            BD.AsignarParametro(4, Integer.toString(registroBeans.getCosto()), 2);

            return BD.registrar();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            BD.desconectar();
        }
    }

    public boolean registrarSalida(RegistroBeans registroBeans) throws Exception {

        if (registroBeans.getFechaFin() != null) {
            return false;
        } else {
            try {
                String sql = "UPDATE mregistros "
                        + "SET fechafin = SYSDATE, "
                        + "    datolabor = ?, "
                        + "    registroslabor = ?, "
                        + "    tiempolabor = ? "
                        + "WHERE idregistro = ? ";
                BD.conectar();
                BD.callableStatement(sql);
                BD.AsignarParametro(1, registroBeans.getDatoLabor().trim(), 1);
                BD.AsignarParametro(2, Integer.toString(registroBeans.getRegistroslabor()), 2);
                BD.AsignarParametro(3, Integer.toString(registroBeans.getTiempolabor()), 2);
                BD.AsignarParametro(4, Integer.toString(registroBeans.getIdregistro()), 2);
                return BD.registrar();
            } catch (Exception e) {
                throw new Exception(e.getMessage());
            } finally {
                BD.desconectar();
            }
        }
    }

    public String getListReferencias(int page, int rows, String sidx, String sord, int idgrupo, String fecha, int filtro) throws Exception {
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
            sql = "SELECT COUNT(*) AS valor FROM mregistros mr, plaborescontratos plc "
                    + "WHERE plc.idlaborcontrato = mr.idlaborcontrato "
                    + "AND plc.idgrupo = ? "
                    + "AND TO_CHAR(mr.fechainicio, 'dd/mm/yyyy') = ? "
                    + "AND mr.estado = ? ";

            BD.conectar();
            BD.callableStatement(sql);
            BD.AsignarParametro(1, Integer.toString(idgrupo), 2);
            BD.AsignarParametro(2, fecha, 1);
            BD.AsignarParametro(3, Integer.toString(filtro), 2);

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
            String anulados = "";
            if (filtro == 0) {
                anulados = "mr.estado = ? AND ANULADO = 0 ";
            }
            if (filtro == 2) {
                anulados = "(mr.estado = ? OR anulado <> 0) ";
            }
            strQuery = "SELECT mr.idregistro, mr.idlaborcontrato, mr.idusuario, pnombre || ' ' || papellido AS usuario, rl.deslabor, NVL(rhe.deshoraextra,' '), rtl.destipolabor ,TO_CHAR(mr.fechainicio, 'dd/MM/yyyy') AS fecha, TO_CHAR(mr.fechainicio, 'HH24:MI') AS fechainicio,  "
                    + "TO_CHAR(mr.fechafin, 'HH24:MI') AS fechafin, mr.tiempolabor, mr.registroslabor, mr.costo, mr.observacion, mr.datolabor, plc.datolabor, rca.descausa "
                    + "FROM mregistros mr   "
                    + "INNER JOIN plaborescontratos plc ON plc.idlaborcontrato = mr.idlaborcontrato  "
                    + "INNER JOIN rlabores rl ON plc.codlabor = rl.codlabor  "
                    + "LEFT JOIN rhorasextras rhe ON rhe.codhoraextra = plc.codhoraextra "
                    + "INNER JOIN mpersonas mp ON mp.idpersona = mr.idusuario "
                    + "INNER JOIN rtipolabor rtl ON plc.codtipolabor = rtl.codtipolabor  "
                    + "INNER JOIN rcausasanulacion rca ON rca.codcausa = mr.anulado  "
                    + "WHERE plc.idgrupo = ? "
                    + "AND TO_CHAR(mr.fechainicio, 'dd/mm/yyyy') = ? "
                    + "AND " + anulados
                    + "ORDER BY 1 ";

            BD.callableStatement(strQuery);
            BD.AsignarParametro(1, Integer.toString(idgrupo), 2);
            BD.AsignarParametro(2, fecha, 1);
            BD.AsignarParametro(3, Integer.toString(filtro), 2);

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
                json = json + ",\"" + datoSql.getString(6) + "\"";
                json = json + ",\"" + datoSql.getString(7) + "\"";
                json = json + ",\"" + datoSql.getString(8) + "\"";
                json = json + ",\"" + datoSql.getString(9) + "\"";
                json = json + ",\"" + (datoSql.getString(10) == null ? "" : datoSql.getString(10)) + "\"";
                json = json + ",\"" + datoSql.getString(11) + "\"";
                json = json + ",\"" + datoSql.getString(12) + "\"";
                json = json + ",\"" + datoSql.getString(13) + "\"";
                json = json + ",\"" + (datoSql.getString(14) == null ? "" : datoSql.getString(14)) + "\"";
                json = json + ",\"" + (datoSql.getString(15) == null ? "" : datoSql.getString(15)) + "\"";
                json = json + ",\"" + datoSql.getString(16) + "\"";
                json = json + ",\"" + datoSql.getString(17) + "\"]";
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

    public int diaValidadoGrupo(int idgrupo, String fecha) throws Exception {
        try {
            String sql = "SELECT estado, anulado "
                    + "FROM mregistros mr, plaborescontratos plc  "
                    + "WHERE plc.idlaborcontrato = mr.idlaborcontrato "
                    + "AND plc.idgrupo = ? "
                    + "AND to_char(fechainicio,'dd/MM/yyyy') = ? "
                    + "ORDER BY estado";
            BD.conectar();
            BD.callableStatement(sql);
            BD.AsignarParametro(1, Integer.toString(idgrupo), 2);
            BD.AsignarParametro(2, fecha, 1);

            BD.consultar();
            ResultSet datoSql = BD.obtenerConsulta();

            boolean siguiente = datoSql.next();
            if (!siguiente) {
                return 2;
            }
            while (siguiente) {
                int estado = datoSql.getInt("estado");
                int anulado = datoSql.getInt("anulado");
                if (estado == 0 && anulado == 0) {
                    return 0;
                }
                siguiente = datoSql.next();
            }


            return 1;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            BD.desconectar();
        }
    }

    public int diaValidadoSitio(int sitio, String fecha) throws Exception {
        try {
            String sql = "SELECT estado,anulado "
                    + "FROM mregistros mr, mpersonas mp  "
                    + "WHERE mp.idpersona = mr.idusuario "
                    + "AND mp.sitiotrabajo = ? "
                    + "AND to_char(fechainicio,'dd/MM/yyyy') = ? "
                    + "ORDER BY estado";
            BD.conectar();
            BD.callableStatement(sql);
            BD.AsignarParametro(1, Integer.toString(sitio), 2);
            BD.AsignarParametro(2, fecha, 1);

            BD.consultar();
            ResultSet datoSql = BD.obtenerConsulta();

            boolean siguiente = datoSql.next();
            if (!siguiente) {
                return 2;
            }
            while (siguiente) {
                int estado = datoSql.getInt(1);
                int anulado = datoSql.getInt(2);
                if (estado == 0 && anulado == 0) {
                    return 0;
                }
                siguiente = datoSql.next();
            }


            return 1;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            BD.desconectar();
        }
    }

    public boolean validarTodos(String fecha, int idgrupo, int sitio, int filtro) throws Exception {
        try {



            String sql = "UPDATE (SELECT estado, anulado, mr.idlaborcontrato "
                    + "          FROM mregistros mr INNER JOIN plaborescontratos pl "
                    + "               ON pl.idlaborcontrato = mr.idlaborcontrato "
                    + "               INNER JOIN mpersonas mp ON mp.idpersona = mr.idusuario "
                    + "         WHERE ";
            if (filtro != 0) {
                sql += "mp.sitiotrabajo = ? ";
            } else {
                sql += "pl.idgrupo = ? ";
            }


            sql += "           AND TO_CHAR (fechainicio, 'DD/MM/YYYY') = ? "
                    + "           AND DECODE (pl.datolabor, "
                    + "                       1, DECODE (mr.datolabor, NULL, 'no', 'si'), "
                    + "                       'si' "
                    + "                      ) = 'si' "
                    + "           AND LENGTH (fechafin) > 1 "
                    + "           AND anulado = 0) "
                    + "   SET estado = 2 "
                    + " WHERE estado = 0 ";
            BD.conectar();
            System.out.println(sql);
            BD.callableStatement(sql);
            if (filtro != 0) {
                BD.AsignarParametro(1, Integer.toString(sitio), 2);
            } else {
                BD.AsignarParametro(1, Integer.toString(idgrupo), 2);
            }
            BD.AsignarParametro(2, fecha, 1);
            return BD.registrar();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            BD.desconectar();
        }
    }

    //Valor 3 es para que no tome el filtro por estado
    public int cuentaValidarTodos(String fecha, int idgrupo, int sitio, int filtro, int estado) throws Exception {
        try {
            ResultSet datoSql;
            String esta = "";
            int Cantidad;



            String sql = "SELECT COUNT(*) AS valor FROM mregistros mr ";
            if (filtro != 0) {
                sql += "INNER JOIN mpersonas mp ON mp.idpersona = mr.idusuario "
                        + " WHERE mp.sitiotrabajo = ? ";
            } else {
                sql += " INNER JOIN plaborescontratos plc ON PLC.IDLABORCONTRATO = MR.IDLABORCONTRATO "
                        + "WHERE PLC.IDGRUPO = ? ";
            }

            sql += "AND TO_CHAR(mr.fechainicio, 'dd/mm/yyyy') = ? ";

            if (estado != 3) {
                sql += "AND mr.estado = ? ";
            }
            BD.conectar();
            BD.callableStatement(sql);
            if (filtro != 0) {
                BD.AsignarParametro(1, Integer.toString(sitio), 2);
            } else {
                BD.AsignarParametro(1, Integer.toString(idgrupo), 2);
            }
            BD.AsignarParametro(2, fecha, 1);

            if (estado != 3) {
                BD.AsignarParametro(3, Integer.toString(estado), 2);
            }

            if (!BD.consultar()) {
                throw new Exception("Error Realizando La Consulta: " + sql);
            }
            datoSql = BD.obtenerConsulta();

            if (datoSql.next()) {
                Cantidad = datoSql.getInt(1);
            } else {
                Cantidad = 0;
            }
            return Cantidad;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            BD.desconectar();
        }
    }

    public String getListProduccion(int page, int rows, String sidx, String sord, int idusuario, String fechaInicial, String fechaFinal, int estado) throws Exception {
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
            sql = "SELECT COUNT(*) AS valor FROM mregistros mr "
                    + "WHERE idusuario = ? "
                    + "AND TO_CHAR(mr.fechainicio, 'dd/mm/yyyy') >= ? "
                    + "AND TO_CHAR(mr.fechafin, 'dd/mm/yyyy') <= ? "
                    + "AND mr.estado = ? ";

            BD.conectar();
            BD.callableStatement(sql);
            BD.AsignarParametro(1, Integer.toString(idusuario), 2);
            BD.AsignarParametro(2, fechaInicial, 1);
            BD.AsignarParametro(3, fechaFinal, 1);
            BD.AsignarParametro(4, Integer.toString(estado), 2);

            if (!BD.consultar()) {
                throw new Exception("Error Realizando La Consulta: " + sql);
            }

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
            strQuery = "SELECT mr.idregistro, "
                    + "       ra.desarea"
                    + "       || ' - '\n"
                    + "       || dag.desgrupo, \n"
                    + "       rl.deslabor labor,\n"
                    + "       rtl.destipolabor, NVL(deshoraextra,' '), \n"
                    + "       TO_CHAR (mr.fechainicio, 'dd/MM/yyyy hh24:mi') AS fechainicio,\n"
                    + "       TO_CHAR (mr.fechafin, 'dd/MM/yyyy hh24:mi') AS fechafin,\n"
                    + "       NVL (mr.observacion, ' ') observacion,\n"
                    + "       NVL (mr.datolabor, ' ') datolabor, NVL (mr.tiempolabor, 0) tiempolabor,\n"
                    + "       NVL (mr.registroslabor, 0) registroslabor, mr.estado\n"
                    + "  FROM mregistros mr INNER JOIN mpersonas mp ON mr.idusuario = mp.idpersona\n"
                    + "       INNER JOIN plaborescontratos plc\n"
                    + "       ON plc.idlaborcontrato = mr.idlaborcontrato\n"
                    + "       INNER JOIN dareasgrupos dag ON dag.idgrupo = plc.idgrupo\n"
                    + "       INNER JOIN dclientesareas dca ON dag.idarea = dca.idarea\n"
                    + "       INNER JOIN rareas ra ON dca.codarea = ra.codarea\n"
                    + "       INNER JOIN mclientes mcl ON mcl.idcliente = dca.idcliente\n"
                    + "       INNER JOIN rtipolabor rtl ON rtl.codtipolabor = plc.codtipolabor\n"
                    + "       INNER JOIN rlabores rl ON plc.codlabor = rl.codlabor\n"
                    + "       LEFT  JOIN rhorasextras rhe ON plc.codhoraextra = rhe.codhoraextra\n"
                    + " WHERE mr.idusuario = ? \n"
                    + "   AND mr.fechainicio >= TO_DATE (? , 'dd/mm/yyyy')\n"
                    + "   AND mr.fechafin <= TO_DATE (?, 'dd/mm/yyyy') + 1\n";
            if (estado != 3) {
                strQuery += "   AND mr.anulado = 0"
                        + "     AND mr.estado = " + estado;
            } else {
                strQuery += "   AND mr.anulado <> 0 ";
            }

            BD.callableStatement(strQuery);
            BD.AsignarParametro(1, Integer.toString(idusuario), 2);
            BD.AsignarParametro(2, fechaInicial, 1);
            BD.AsignarParametro(3, fechaFinal, 1);

            BD.consultar();
            datoSql = BD.obtenerConsulta();
            total = total1;

            json = "";
            json = json + "{\n";
            json = json + "\"page\":" + page + ",\n";
            json = json + "\"total\":" + total_pages + ",\n";
            json = json + "\"records\":" + total + ",\n";
            json = json + "\"reccount\":" + total + ",\n";
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
                json = json + ",\"" + datoSql.getString(6) + "\"";
                json = json + ",\"" + datoSql.getString(7) + "\"";
                json = json + ",\"" + datoSql.getString(8) + "\"";
                json = json + ",\"" + datoSql.getString(9) + "\"";
                json = json + ",\"" + datoSql.getString(10) + "\"";
                json = json + ",\"" + datoSql.getString(11) + "\"";
                json = json + ",\"" + datoSql.getInt(12) + "\"]";
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

    public boolean imagenesProcesadas(RegistroBeans registroBeans) throws Exception {
        try {
            String sql = "CALL PKG_REGISTRO_AUXILIAR.ActRegistrosLabor(?,?,?,?)";
            BD.conectar();
            BD.callableStatement(sql);
            BD.AsignarParametro(1, Integer.toString(registroBeans.getIdregistro()), 2);
            BD.AsignarParametro(2, registroBeans.getUsuario().getLogin(), 1);
            BD.AsignarParametro(3, registroBeans.getFechaInicio(), 1);
            BD.AsignarParametro(4, registroBeans.getFechaFin(), 1);
            return BD.registrar();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            BD.desconectar();
        }
    }

    public boolean horasLaboradas(int idregistro) throws Exception {
        try {
            String sql = "CALL PKG_REGISTRO_AUXILIAR.ActTiempoLabor(?)";
            BD.conectar();
            BD.callableStatement(sql);
            BD.AsignarParametro(1, Integer.toString(idregistro), 1);
            return BD.registrar();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            BD.desconectar();
        }
    }

    public int consultarImagenes(int idregistro) throws Exception {
        try {
            int imagenes = 0;
            String sql = "SELECT NVL(registroslabor,0) "
                    + "FROM mregistros "
                    + "WHERE idregistro = ? ";
            BD.conectar();
            BD.callableStatement(sql);
            BD.AsignarParametro(1, Integer.toString(idregistro), 2);
            BD.consultar();
            ResultSet datoSql = BD.obtenerConsulta();
            if (datoSql.next()) {
                imagenes = datoSql.getInt(1);
            }

            return imagenes;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            BD.desconectar();
        }
    }

    public int consultarHorasLaboradas(int idregistro) throws Exception {
        try {
            int imagenes = 0;
            String sql = "SELECT NVL(tiempolabor,0) "
                    + "FROM mregistros "
                    + "WHERE idregistro = ? ";
            BD.conectar();
            BD.callableStatement(sql);
            BD.AsignarParametro(1, Integer.toString(idregistro), 2);
            BD.consultar();
            ResultSet datoSql = BD.obtenerConsulta();
            if (datoSql.next()) {
                imagenes = datoSql.getInt(1);
            }

            return imagenes;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            BD.desconectar();
        }
    }

    public boolean registrarDatoLabor(RegistroBeans registroBeans) throws Exception {
        try {
            String sql = "INSERT INTO pregistrosdatolabor(idregistro, datolabor) "
                    + "     VALUES (?, ?) ";
            BD.conectar();
            BD.callableStatement(sql);
            BD.AsignarParametro(1, Integer.toString(registroBeans.getIdregistro()), 2);
            BD.AsignarParametro(2, registroBeans.getDatoLabor(), 1);

            return BD.registrar();
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        } finally {
            BD.desconectar();

        }
    }

    public String getListRegistrosDatoLabor(int page, int rows, String id) throws Exception {
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

            sql = "SELECT COUNT(*) AS valor FROM pregistrosdatolabor "
                    + " WHERE idregistro = ? ";

            BD.conectar();
            BD.callableStatement(sql);
            BD.AsignarParametro(1, id, 2);
            BD.consultar();

            rsCuenta = BD.obtenerConsulta();

            if (rsCuenta.next()) {
                total = Integer.parseInt(rsCuenta.getString(1));
                total1 = total;
            }


            if (total > 0) {
                double d = Math.ceil((double) (total) / (double) (rows));
                total_pages = (int) (d);
            } else {
                total_pages = 0;
            }

            strQuery = "SELECT idregistro, datolabor "
                    + "  FROM pregistrosdatolabor "
                    + " WHERE idregistro = ? ";
            BD.callableStatement(strQuery);
            BD.AsignarParametro(1, id, 2);

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
                json = json + "\"id\":\"" + datoSql.getInt(2) + "\",";
                json = json + "\"cell\":[" + datoSql.getInt(1) + "";
                json = json + ",\"" + datoSql.getString(2) + "\"]";
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

    public boolean editarDatoLabor(String registro, String datolabor, String datolaborAnt) throws Exception {
        try {
            String sql = "UPDATE pregistrosdatolabor "
                    + "   SET datolabor = ? "
                    + " WHERE idregistro = ? "
                    + " AND datolabor = ? ";
            BD.conectar();
            BD.callableStatement(sql);
            BD.AsignarParametro(1, datolabor, 1);
            BD.AsignarParametro(2, registro, 2);
            BD.AsignarParametro(3, datolaborAnt, 1);

            return BD.registrar();
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        } finally {
            BD.desconectar();

        }
    }

    public int consultarUltimoID(int idusuario) throws Exception {
        try {
            int id = 0;
            String sql = "SELECT  idregistro "
                    + "FROM mregistros "
                    + "WHERE fechafin = (SELECT "
                    + "  max(fechafin) "
                    + "  keep ( "
                    + "      dense_rank first "
                    + "      order by fechafin desc NULLS LAST "
                    + "  ) as ultima_fecha_registro "
                    + "FROM mregistros "
                    + "WHERE idusuario = ?)";

            BD.conectar();
            BD.callableStatement(sql);
            BD.AsignarParametro(1, Integer.toString(idusuario), 2);
            BD.consultar();
            ResultSet datoSql = BD.obtenerConsulta();
            if (datoSql.next()) {
                id = datoSql.getInt(1);
            }
            return id;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            BD.desconectar();
        }
    }

    public int consultarUltimoID(RegistroBeans beans) throws Exception {
        try {
            int id = 0;
            String sql = "SELECT idregistro "
                    + "  FROM mregistros "
                    + " WHERE idusuario = ?  "
                    + " AND idlaborcontrato = ?  "
                    + " AND anulado = 0"
                    + " AND fechainicio = to_date( ? , 'DD/MM/YYYY HH24:MI' ) "
                    + " AND fechafin = to_date( ? , 'DD/MM/YYYY HH24:MI' )";

            BD.conectar();
            BD.callableStatement(sql);
            BD.AsignarParametro(1, Integer.toString(beans.getUsuario().getIdusuario()), 2);
            BD.AsignarParametro(2, Integer.toString(beans.getLabor().getIdlaborcontrato()), 2);
            BD.AsignarParametro(3, beans.getFechaInicio(), 1);
            BD.AsignarParametro(4, beans.getFechaFin(), 1);


            BD.consultar();
            ResultSet datoSql = BD.obtenerConsulta();
            if (datoSql.next()) {
                id = datoSql.getInt(1);
            }
            return id;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            BD.desconectar();
        }
    }

    public int consultarIgual(RegistroBeans beans) throws Exception {
        try {
            int id = 0;
            String sql = "SELECT idregistro "
                    + "  FROM mregistros "
                    + " WHERE idusuario = ?"
                    + " AND anulado = 0  "
                    + " AND (fechainicio = to_date( ? , 'DD/MM/YYYY HH24:MI' ) "
                    + " OR fechafin = to_date( ? , 'DD/MM/YYYY HH24:MI' ))";

            BD.conectar();
            BD.callableStatement(sql);
            BD.AsignarParametro(1, Integer.toString(beans.getUsuario().getIdusuario()), 2);
            BD.AsignarParametro(2, beans.getFechaInicio(), 1);
            BD.AsignarParametro(3, beans.getFechaFin(), 1);


            BD.consultar();
            ResultSet datoSql = BD.obtenerConsulta();
            if (datoSql.next()) {
                id = datoSql.getInt(1);
            }
            return id;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            BD.desconectar();
        }
    }

    public RegistroBeans consultarRegistroFecha(int idusuario, String fecha) throws Exception {
        try {
            String sql = "SELECT idregistro "
                    + "  FROM mregistros "
                    + " WHERE idusuario = ? "
                    + " AND to_date( ? ,'dd/mm/yyyy hh24:mi') > fechainicio "
                    + " AND to_date( ? ,'dd/mm/yyyy hh24:mi') < fechafin "
                    + " AND anulado = 0";
            BD.conectar();
            BD.callableStatement(sql);
            BD.AsignarParametro(1, Integer.toString(idusuario), 2);
            BD.AsignarParametro(2, fecha, 1);
            BD.AsignarParametro(3, fecha, 1);

            BD.consultar();

            ResultSet datoSql = BD.obtenerConsulta();
            String registro = "0";
            if (datoSql.next()) {
                registro = datoSql.getString("idregistro");
            }
            BD.desconectar();
            return consultar(registro);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            BD.desconectar();
        }
    }

    public String getListRegistros(int page, int rows, String idusuario, String idnomina) throws Exception {
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

            sql = "SELECT COUNT(*) AS valor FROM mregistros "
                    + " WHERE idusuario = ? AND idnomina = ? ";

            BD.conectar();
            BD.callableStatement(sql);
            BD.AsignarParametro(1, idusuario, 2);
            BD.AsignarParametro(2, idnomina, 2);
            BD.consultar();

            rsCuenta = BD.obtenerConsulta();

            if (rsCuenta.next()) {
                total = Integer.parseInt(rsCuenta.getString(1));
                total1 = total;
            }


            if (total > 0) {
                double d = Math.ceil((double) (total) / (double) (rows));
                total_pages = (int) (d);
            } else {
                total_pages = 0;
            }

            strQuery = "SELECT mr.idregistro, mr.idlaborcontrato, mr.idusuario,\n"
                    + "       pnombre || ' ' || papellido AS usuario, rl.deslabor,\n"
                    + "       NVL (rhe.deshoraextra, ' '), rtl.destipolabor,\n"
                    + "       TO_CHAR (mr.fechainicio, 'dd/MM/yyyy') AS fecha,\n"
                    + "       TO_CHAR (mr.fechainicio, 'HH24:MI') AS fechainicio,\n"
                    + "       TO_CHAR (mr.fechafin, 'HH24:MI') AS fechafin, mr.tiempolabor,\n"
                    + "       mr.registroslabor, mr.costo, mr.observacion, mr.datolabor,\n"
                    + "       plc.datolabor, rca.descausa \n"
                    + "  FROM mregistros mr \n"
                    + "       INNER JOIN plaborescontratos plc       ON plc.idlaborcontrato = mr.idlaborcontrato\n"
                    + "       INNER JOIN rlabores rl ON plc.codlabor = rl.codlabor\n"
                    + "       LEFT JOIN rhorasextras rhe ON rhe.codhoraextra = plc.codhoraextra\n"
                    + "       INNER JOIN mpersonas mp ON mp.idpersona = mr.idusuario\n"
                    + "       INNER JOIN rtipolabor rtl ON plc.codtipolabor = rtl.codtipolabor\n"
                    + "       INNER JOIN rcausasanulacion rca ON rca.codcausa = mr.anulado "
                    + "AND mr.idusuario = ? AND mr.idnomina = ? ";

            BD.callableStatement(strQuery);
            BD.AsignarParametro(1, idusuario, 2);
            BD.AsignarParametro(2, idnomina, 2);

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
                json = json + ",\"" + datoSql.getString(6) + "\"";
                json = json + ",\"" + datoSql.getString(7) + "\"";
                json = json + ",\"" + datoSql.getString(8) + "\"";
                json = json + ",\"" + datoSql.getString(9) + "\"";
                json = json + ",\"" + (datoSql.getString(10) == null ? "" : datoSql.getString(10)) + "\"";
                json = json + ",\"" + datoSql.getString(11) + "\"";
                json = json + ",\"" + datoSql.getString(12) + "\"";
                json = json + ",\"" + datoSql.getString(13) + "\"";
                json = json + ",\"" + (datoSql.getString(14) == null ? "" : datoSql.getString(14)) + "\"";
                json = json + ",\"" + (datoSql.getString(15) == null ? "" : datoSql.getString(15)) + "\"";
                json = json + ",\"" + datoSql.getString(16) + "\"";
                json = json + ",\"" + datoSql.getString(17) + "\"]";
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

    public void registrarDatoLabor(String registro, String dato) throws Exception {
        try {
            String sql = "INSERT INTO pregistrosdatolabor(idregistro, datolabor) "
                    + "     VALUES (?, ?) ";
            BD.conectar();
            BD.callableStatement(sql);
            BD.AsignarParametro(1, registro, 2);
            BD.AsignarParametro(2, dato, 1);

            BD.registrar();
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        } finally {
            BD.desconectar();

        }
    }

    public String getListRegistrosSitio(int page, int rows, String sidx, String sord, int sitio, String fecha, int filtro) throws Exception {
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
            sql = "SELECT COUNT(*) AS valor "
                    + "FROM mregistros mr, mpersonas mp "
                    + "WHERE mp.idpersona = mr.idusuario "
                    + "AND mp.sitiotrabajo = ? "
                    + "AND TO_CHAR(mr.fechainicio, 'dd/mm/yyyy') = ? "
                    + "AND mr.estado = ? ";

            BD.conectar();
            BD.callableStatement(sql);
            BD.AsignarParametro(1, Integer.toString(sitio), 2);
            BD.AsignarParametro(2, fecha, 1);
            BD.AsignarParametro(3, Integer.toString(filtro), 2);

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
            String anulados = "";
            if (filtro == 0) {
                anulados = "mr.estado = ? AND ANULADO = 0 ";
            }
            if (filtro == 2) {
                anulados = " (mr.estado = ? OR anulado <> 0 )";
            }
            strQuery = "SELECT mr.idregistro, mr.idlaborcontrato, mr.idusuario, pnombre || ' ' || papellido AS usuario, rl.deslabor, NVL(rhe.deshoraextra,' '), rtl.destipolabor ,TO_CHAR(mr.fechainicio, 'dd/MM/yyyy') AS fecha, TO_CHAR(mr.fechainicio, 'HH24:MI') AS fechainicio,  "
                    + "TO_CHAR(mr.fechafin, 'HH24:MI') AS fechafin, mr.tiempolabor, mr.registroslabor, mr.costo, mr.observacion, NVL(mr.datolabor,'0'), plc.datolabor, rca.descausa "
                    + "FROM mregistros mr   "
                    + "INNER JOIN plaborescontratos plc ON plc.idlaborcontrato = mr.idlaborcontrato  "
                    + "INNER JOIN rlabores rl ON plc.codlabor = rl.codlabor  "
                    + "LEFT JOIN rhorasextras rhe ON rhe.codhoraextra = plc.codhoraextra "
                    + "INNER JOIN mpersonas mp ON mp.idpersona = mr.idusuario "
                    + "INNER JOIN rtipolabor rtl ON plc.codtipolabor = rtl.codtipolabor  "
                    + "INNER JOIN rcausasanulacion rca ON rca.codcausa = mr.anulado  "
                    + "WHERE mp.sitiotrabajo = ?  "
                    + "AND TO_CHAR(mr.fechainicio, 'dd/mm/yyyy') = ? "
                    + "AND " + anulados
                    + "ORDER BY 1 ";

            BD.callableStatement(strQuery);
            BD.AsignarParametro(1, Integer.toString(sitio), 2);
            BD.AsignarParametro(2, fecha, 1);
            BD.AsignarParametro(3, Integer.toString(filtro), 2);

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
                json = json + ",\"" + datoSql.getString(6) + "\"";
                json = json + ",\"" + datoSql.getString(7) + "\"";
                json = json + ",\"" + datoSql.getString(8) + "\"";
                json = json + ",\"" + datoSql.getString(9) + "\"";
                json = json + ",\"" + (datoSql.getString(10) == null ? "" : datoSql.getString(10)) + "\"";
                json = json + ",\"" + datoSql.getString(11) + "\"";
                json = json + ",\"" + datoSql.getString(12) + "\"";
                json = json + ",\"" + datoSql.getString(13) + "\"";
                json = json + ",\"" + (datoSql.getString(14) == null ? "" : datoSql.getString(14)) + "\"";
                json = json + ",\"" + (datoSql.getString(15) == null ? "" : datoSql.getString(15)) + "\"";
                json = json + ",\"" + datoSql.getString(16) + "\"";
                json = json + ",\"" + datoSql.getString(17) + "\"]";
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
}
