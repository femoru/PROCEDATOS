/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.co.sio.java.dao;

import com.co.sio.java.JSON.JSONArray;
import com.co.sio.java.JSON.JSONException;
import com.co.sio.java.JSON.JSONObject;
import com.co.sio.java.db.ControllerPool;
import com.co.sio.java.mbeans.LaborBeans;
import com.co.sio.java.mbeans.PlanillaBeans;
import com.co.sio.java.mbeans.ReferenciasBeans;
import com.co.sio.java.mbeans.RegistroBeans;
import com.co.sio.java.mbeans.UsuarioBeans;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

/**
 *
 * @author fmoctezuma
 */
public class PlanillaDao {

    private final ControllerPool BD;
    private final RegistrosDao registrosDao;

    public PlanillaDao() {
        BD = new ControllerPool();

        registrosDao = new RegistrosDao();
    }

    public boolean Guardar(PlanillaBeans beans) throws Exception {
        try {
            String sql = "INSERT INTO tplanilla\n"
                    + "            (idusuario, idlaborcontrato, fechalabor, hiniciod, hdescd,\n"
                    + "             tiempodiurno, hreiniciod, hfinald, tiempotarde, registroslabor,\n"
                    + "             valor, costo\n"
                    + "            )\n"
                    + "     VALUES (?, ?, to_date(?,'dd/mm/yyyy'), ?, ?,\n"
                    + "             ?, ?, ?, ?, ?,\n"
                    + "             ?, ?\n"
                    + "            )";

            BD.conectar();
            BD.callableStatement(sql);
            BD.AsignarParametro(1, Integer.toString(beans.getIdusuario()), 2);
            BD.AsignarParametro(2, Integer.toString(beans.getIdlaborcontrato()), 2);
            BD.AsignarParametro(3, beans.getFechalabor(), 1);
            BD.AsignarParametro(4, beans.getHoraInicioD(), 1);
            BD.AsignarParametro(5, beans.getHoraDescansoD(), 1);
            BD.AsignarParametro(6, Integer.toString(beans.getTiempoDiurno()), 2);
            BD.AsignarParametro(7, beans.getHoraReinicioD(), 1);
            BD.AsignarParametro(8, beans.getHoraFinalD(), 1);
            BD.AsignarParametro(9, Integer.toString(beans.getTiempoTarde()), 2);
            BD.AsignarParametro(10, Integer.toString(beans.getRegistrosLabor()), 2);
            BD.AsignarParametro(11, Integer.toString(beans.getValor()), 2);
            BD.AsignarParametro(12, Integer.toString(beans.getCosto()), 2);

            return BD.registrar();
        } catch (SQLException e) {
            throw new Exception(e.getMessage());
        } catch (ParseException e) {
            throw new Exception(e.getMessage());
        } finally {
            BD.desconectar();
        }
    }

    public String getListPlanilla(int idpersona, int idlabor, String mes, int page, int rows) throws Exception {
        String sql;

        int total = 0;
        int total1 = 0;
        int total_pages;

        ResultSet rsCuenta;
        ResultSet datoSql;

        try {

            sql = "SELECT COUNT(*)\n"
                    + "  FROM tplanilla\n"
                    + " WHERE idusuario = ?\n"
                    + "   AND idlaborcontrato = ?\n"
                    + "   AND TO_CHAR (fechalabor, 'MM/YYYY') = ? ";

            BD.conectar();
            BD.callableStatement(sql);
            BD.AsignarParametro(1, Integer.toString(idpersona), 2);
            BD.AsignarParametro(2, Integer.toString(idlabor), 2);
            BD.AsignarParametro(3, mes, 1);
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

            sql = "SELECT idplanilla, TO_CHAR(fechalabor,'DD') AS DIA, "
                    + "      replace(hiniciod,':'), "
                    + "      replace(hdescd,':'), "
                    + "     TRIM (TO_CHAR (TRUNC (tiempodiurno / 60), '09'))\n"
                    + "       || ':'\n"
                    + "       || TRIM (TO_CHAR (TRUNC (MOD (ABS (tiempodiurno), 60)), '09')) AS tiempodiurno, "
                    + "      replace(hreiniciod,':'), "
                    + "      replace(hfinald,':'),\n"
                    + "       TRIM (TO_CHAR (TRUNC (tiempotarde / 60), '09'))\n"
                    + "       || ':'\n"
                    + "       || TRIM (TO_CHAR (TRUNC (MOD (ABS (tiempotarde), 60)), '09')) AS tiempotarde, "
                    + "       registroslabor\n"
                    + "  FROM tplanilla\n"
                    + " WHERE idusuario = ?\n"
                    + "   AND idlaborcontrato = ?\n"
                    + "   AND TO_CHAR (fechalabor, 'MM/YYYY') = ?\n"
                    + " ORDER BY DIA DESC";
            BD.callableStatement(sql);
            BD.AsignarParametro(1, Integer.toString(idpersona), 2);
            BD.AsignarParametro(2, Integer.toString(idlabor), 2);
            BD.AsignarParametro(3, mes, 1);
            BD.consultar();
            datoSql = BD.obtenerConsulta();
            total = total1;

            int totalColumnas = datoSql.getMetaData().getColumnCount();

            JSONObject jsonData = new JSONObject();

            jsonData.put("page", page);
            jsonData.put("total", total_pages);
            jsonData.put("records", total);
            JSONArray jsonRows = new JSONArray();

            int countColumn = 2;
            JSONObject jsono;
            JSONArray jsona;
            while (datoSql.next()) {
                jsono = new JSONObject();
                jsona = new JSONArray();
                jsono.put("id", datoSql.getString(1));
                while (countColumn <= totalColumnas) {
                    jsona.put(datoSql.getString(countColumn++));
                }
                jsono.put("cell", jsona);
                jsonRows.put(jsono);
                countColumn = 2;
            }

            jsonData.put("rows", jsonRows);

            return jsonData.toString();

        } catch (SQLException ex) {
            throw new Exception(ex.getMessage());
        } catch (ParseException ex) {
            throw new Exception(ex.getMessage());
        } catch (NumberFormatException ex) {
            throw new Exception(ex.getMessage());
        } catch (JSONException ex) {
            throw new Exception(ex.getMessage());
        } finally {
            BD.desconectar();
        }

    }

    public boolean Actualizar(PlanillaBeans beans) throws Exception {
        try {
            String sql = "UPDATE tplanilla\n"
                    + "   SET fechalabor = to_date(?,'dd/mm/yyyy'),\n"
                    + "       hiniciod = ?,\n"
                    + "       hdescd = ?,\n"
                    + "       tiempodiurno = ?,\n"
                    + "       hreiniciod = ?,\n"
                    + "       hfinald = ?,\n"
                    + "       tiempotarde = ?,\n"
                    + "       registroslabor = ?\n"
                    + " WHERE idplanilla = ? ";

            BD.conectar();
            BD.callableStatement(sql);
            BD.AsignarParametro(1, beans.getFechalabor(), 1);
            BD.AsignarParametro(2, beans.getHoraInicioD(), 1);
            BD.AsignarParametro(3, beans.getHoraDescansoD(), 1);
            BD.AsignarParametro(4, Integer.toString(beans.getTiempoDiurno()), 2);
            BD.AsignarParametro(5, beans.getHoraReinicioD(), 1);
            BD.AsignarParametro(6, beans.getHoraFinalD(), 1);
            BD.AsignarParametro(7, Integer.toString(beans.getTiempoTarde()), 2);
            BD.AsignarParametro(8, Integer.toString(beans.getRegistrosLabor()), 2);
            BD.AsignarParametro(9, Integer.toString(beans.getIdplanilla()), 2);

            return BD.actualizar();
        } catch (SQLException e) {
            throw new Exception(e.getMessage());
        } catch (ParseException e) {
            throw new Exception(e.getMessage());
        } finally {
            BD.desconectar();
        }
    }

    public boolean eliminar(int idplanilla) throws Exception {
        try {

            String sql = "DELETE FROM tplanilla WHERE idplanilla = ?";
            BD.conectar();
            BD.callableStatement(sql);
            BD.AsignarParametro(1, Integer.toString(idplanilla), 2);

            return BD.registrar();
        } catch (SQLException e) {
            throw new Exception(e.getMessage());
        } catch (ParseException e) {
            throw new Exception(e.getMessage());
        } finally {
            BD.desconectar();
        }
    }

    public int consultarRango(PlanillaBeans beans) throws Exception {

        try {

            int diurno = consultaRangoDiurno(beans, true);
            int nocturno = consultaRangoDiurno(beans, false);

            return diurno + nocturno;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }

    }

    private int consultaRangoDiurno(PlanillaBeans beans, boolean diurno) throws Exception {
        try {
            String sql;
            sql = "SELECT count(*)\n"
                    + "  FROM tplanilla\n"
                    + " WHERE TO_CHAR (fechalabor, 'DD/MM/YYYY') = ? \n"
                    + "   AND idusuario = ? "
                    + " AND idplanilla <> ? ";
            if (diurno) {
                sql += "    AND (   "
                        + " TO_DATE (?, 'HH24:MI') > TO_DATE (hiniciod,'HH24:MI')\n"
                        + "     AND TO_DATE (?, 'HH24:MI') < TO_DATE (hdescd,'HH24:MI')\n"
                        + " OR "
                        + " TO_DATE (?, 'HH24:MI') > TO_DATE (hiniciod,'HH24:MI')\n"
                        + "     AND TO_DATE (?, 'HH24:MI') < TO_DATE (hdescd,'HH24:MI')\n"
                        + "       )\n";
            } else {
                sql += "   AND (   "
                        + " TO_DATE (?, 'HH24:MI') > TO_DATE (hreiniciod,'HH24:MI')\n"
                        + "     AND TO_DATE (?, 'HH24:MI') < TO_DATE (hfinald,'HH24:MI')\n"
                        + " OR "
                        + " TO_DATE (?, 'HH24:MI') > TO_DATE (hreiniciod,'HH24:MI')\n"
                        + "     AND TO_DATE (?, 'HH24:MI') < TO_DATE (hfinald,'HH24:MI')\n"
                        + "       )\n";
            }
            BD.conectar();
            BD.callableStatement(sql);
            BD.AsignarParametro(1, beans.getFechalabor(), 1);
            BD.AsignarParametro(2, Integer.toString(beans.getIdusuario()), 2);
            BD.AsignarParametro(3, Integer.toString(beans.getIdplanilla()), 2);
            if (diurno) {
                BD.AsignarParametro(4, beans.getHoraInicioD(), 1);
                BD.AsignarParametro(5, beans.getHoraInicioD(), 1);
                BD.AsignarParametro(6, beans.getHoraDescansoD(), 1);
                BD.AsignarParametro(7, beans.getHoraDescansoD(), 1);
            } else {
                BD.AsignarParametro(4, beans.getHoraReinicioD(), 1);
                BD.AsignarParametro(5, beans.getHoraReinicioD(), 1);
                BD.AsignarParametro(6, beans.getHoraFinalD(), 1);
                BD.AsignarParametro(7, beans.getHoraFinalD(), 1);
            }
            BD.consultar();

            ResultSet rsCuenta = BD.obtenerConsulta();

            int total = 0;
            if (rsCuenta.next()) {
                total = rsCuenta.getInt(1);
            }
            return total;
        } catch (SQLException e) {
            throw new Exception(e.getMessage());
        } catch (ParseException e) {
            throw new Exception(e.getMessage());
        } finally {
            BD.desconectar();
        }
    }

    public PlanillaBeans consultar(int idplanilla) throws Exception {
        PlanillaBeans beans = new PlanillaBeans();
        try {
            String sql = "SELECT \n"
                    + " IDUSUARIO, IDLABORCONTRATO, \n"
                    + "   FECHALABOR, HINICIOD, HDESCD, \n"
                    + "    HREINICIOD, HFINALD, \n"
                    + "   REGISTROSLABOR \n"
                    + "FROM TPLANILLA WHERE IDPLANILLA = ? ";
            BD.conectar();
            BD.callableStatement(sql);
            BD.AsignarParametro(1, Integer.toString(idplanilla), 2);

            BD.consultar();

            ResultSet datoSql = BD.obtenerConsulta();

            if (datoSql.next()) {
                beans.setIdusuario(datoSql.getInt(1));
                beans.setIdlaborcontrato(datoSql.getInt(2));
                beans.setFechalabor(datoSql.getString(3));
                beans.setHoraInicioD(datoSql.getString(4));
                beans.setHoraDescansoD(datoSql.getString(5));
                beans.setHoraReinicioD(datoSql.getString(6));
                beans.setHoraFinalD(datoSql.getString(7));
                beans.setRegistrosLabor(datoSql.getInt(8));
            }

            return beans;
        } catch (SQLException e) {
            throw new Exception(e.getMessage());
        } catch (ParseException e) {
            throw new Exception(e.getMessage());
        } finally {
            BD.desconectar();
        }

    }

    public int validarPlanilla(int idusuario, int idlaborcontrato, String mes, String ingresado) throws Exception {
        try {

            UsuarioBeans usuarioBeans = new UsuarioBeans();
            usuarioBeans.setIdusuario(idusuario);
            LaborBeans laborBeans = new LaborBeans();
            laborBeans.setIdlaborcontrato(idlaborcontrato);

            String sql = "SELECT COUNT(*)\n"
                    + "  FROM tplanilla\n"
                    + " WHERE idusuario = ?\n"
                    + "   AND idlaborcontrato = ?\n"
                    + "   AND TO_CHAR (fechalabor, 'MM/YYYY') = ? ";

            BD.conectar();
            BD.callableStatement(sql);
            BD.AsignarParametro(1, Integer.toString(idusuario), 2);
            BD.AsignarParametro(2, Integer.toString(idlaborcontrato), 2);
            BD.AsignarParametro(3, mes, 1);
            BD.consultar();

            ResultSet datoSql = BD.obtenerConsulta();
            int total = 0;
            if (datoSql.next()) {
                total = Integer.parseInt(datoSql.getString(1));
            }
            sql = "SELECT idplanilla, TO_CHAR (fechalabor, 'DD/MM/YYYY ') || hiniciod hiniciod,\n"
                    + "       TO_CHAR (fechalabor, 'DD/MM/YYYY ') || hdescd hdescd, tiempodiurno,\n"
                    + "       TO_CHAR (fechalabor, 'DD/MM/YYYY ') || hreiniciod hreiniciod,\n"
                    + "       TO_CHAR (fechalabor, 'DD/MM/YYYY ') || hfinald hfinald, tiempotarde,\n"
                    + "       registroslabor, valor, costo\n"
                    + "  FROM tplanilla \n"
                    + " WHERE idusuario = ?\n"
                    + "   AND idlaborcontrato = ?\n"
                    + "   AND TO_CHAR (fechalabor, 'MM/YYYY') = ?\n";

            BD.callableStatement(sql);
            BD.AsignarParametro(1, Integer.toString(idusuario), 2);
            BD.AsignarParametro(2, Integer.toString(idlaborcontrato), 2);
            BD.AsignarParametro(3, mes, 1);
            BD.consultar();

            datoSql = BD.obtenerConsulta();

            ReferenciasBeans referenciasBeans = new ReferenciasBeans();
            referenciasBeans.setCodigo(0);
            String observacion = "REGISTRO POR PLANILLA INGRESO " + ingresado;
            RegistroBeans registroDiurno;
            RegistroBeans registroTarde;
            boolean registro;

            while (datoSql.next()) {
                registroDiurno = new RegistroBeans();
                registroTarde = new RegistroBeans();

                registroDiurno.setUsuario(usuarioBeans);
                registroDiurno.setLabor(laborBeans);
                registroDiurno.setFechaInicio(datoSql.getString("hiniciod"));
                registroDiurno.setFechaFin(datoSql.getString("hdescd"));
                registroDiurno.setTiempolabor(datoSql.getInt("tiempodiurno"));
                registroDiurno.setObservacion(observacion);
                registroDiurno.setEstado(2);
                registroDiurno.setValor(datoSql.getInt("valor"));
                registroDiurno.setCosto(datoSql.getInt("costo"));
                registroDiurno.setAnulado(referenciasBeans);

                registroTarde.setUsuario(usuarioBeans);
                registroTarde.setLabor(laborBeans);
                registroTarde.setFechaInicio(datoSql.getString("hreiniciod"));
                registroTarde.setFechaFin(datoSql.getString("hfinald"));
                registroTarde.setTiempolabor(datoSql.getInt("tiempotarde"));
                registroTarde.setRegistroslabor(datoSql.getInt("registroslabor"));
                registroTarde.setObservacion(observacion);
                registroTarde.setEstado(2);
                registroTarde.setValor(datoSql.getInt("valor"));
                registroTarde.setCosto(datoSql.getInt("costo"));
                registroTarde.setAnulado(referenciasBeans);

                if (!existeRegistro(idusuario, registroDiurno) && !existeRegistro(idusuario, registroTarde)) {
                    registro = false;
                    if (registroDiurno.getTiempolabor() > 0 && registrosDao.insertar(registroDiurno)) {
                        registro = true;
                    }
                    if (registroTarde.getTiempolabor() > 0 && registrosDao.insertar(registroTarde)) {
                        registro = true;
                    }
                    //if (new RegistrosDao().insertar(registroDiurno) && new RegistrosDao().insertar(registroTarde)) {
                    if (registro) {
                        eliminar(datoSql.getInt("idplanilla"));
                        total--;
                    }
                }

            }
            return total;

        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            BD.desconectar();
        }
    }

    private boolean existeRegistro(int idusuario, RegistroBeans registroBeans) throws Exception {

        RegistroBeans registro = registrosDao.consultarRegistroFecha(idusuario, registroBeans.getFechaInicio());

        if (registro.getIdregistro() != 0) {
            return true;
        }
        registro = registrosDao.consultarRegistroFecha(idusuario, registroBeans.getFechaFin());
        if (registro.getIdregistro() != 0) {
            return true;
        }
        int idRegistroIgual = registrosDao.consultarUltimoID(registroBeans);
        if (idRegistroIgual != 0) {
            return true;
        }
        idRegistroIgual = registrosDao.consultarIgual(registroBeans);
        return idRegistroIgual != 0;
    }

    public JSONObject subirPlano(InputStream in) throws IOException, ParseException, Exception {

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        SimpleDateFormat sdfr = new SimpleDateFormat("dd/MM/yyyy");
        POIFSFileSystem fs = new POIFSFileSystem(in);
        HSSFWorkbook wb = new HSSFWorkbook(fs);
        HSSFSheet hoja;
        HSSFRow fila;

        HashMap<String, Object> params = new HashMap<String, Object>();
        ArrayList<RegistroBeans> registros = new ArrayList<RegistroBeans>();
        LaboresDao laboresDao = new LaboresDao();
        UsuarioDao usuarioDao = new UsuarioDao();

        UsuarioBeans ub;
        RegistroBeans rb;
        LaborBeans lb = null;
        boolean error = false;

        ReferenciasBeans anulado = new ReferenciasBeans();
        anulado.setCodigo(0);

        int nuevos = 0, actualizados = 0, procesados = 0, cantRegistros = 0, errores = 0;
        String mensajeError = "", observacionArchivo = "";
        Date fechaIni = null, fechaFin = null;

        for (int j = 0; j < wb.getNumberOfSheets(); j++) {
            hoja = wb.getSheetAt(j);

            int filas = hoja.getPhysicalNumberOfRows();
            int acumDiurnos = 0, acumNocturno = 0, acumFestivos = 0, acumFestNoct = 0,
                    ultimaFila = 0;

            mensajeError += mensajeError.length() > 0 ? "\n" : "";
            mensajeError += "Hoja: " + hoja.getSheetName() + "\n";

            mensajeError += "LINEA " + "\t| "
                    + "DATO\t\t\t\t\t\t" + "\t| "
                    + "CAUSA\n";

            Date finicio = sdf.parse(hoja.getRow(8).getCell(10).toString());
            Date ffin = sdf.parse(hoja.getRow(8).getCell(13).toString());
            String observacion = hoja.getRow(0).getCell(13).getStringCellValue() + " - "
                    + hoja.getRow(5).getCell(3).getStringCellValue();

            for (int i = 11; i < filas; i++) {
                fila = hoja.getRow(i);
                if (fila.getCell(0).getCellType() != HSSFCell.CELL_TYPE_NUMERIC) {
                    ultimaFila = i;
                    break;
                }//Ultimo registro 

                long identificacion = (long) fila.getCell(0).getNumericCellValue();
                String idlaborcontrato = fila.getCell(3).getStringCellValue();
                /**
                 * Validaciones de usuario
                 */
                ub = usuarioDao.consultar(Long.toString(identificacion));
                if (ub == null) {
                    mensajeError += Integer.toString(i + 1) + "\t| "
                            + Long.toString(identificacion) + "\t- " + fila.getCell(2).getStringCellValue() + "\t| "
                            + "No de cedula no encontrado\n";
                    error = true;
                }
                /**
                 * Validaciones de la labor
                 */
                if (idlaborcontrato == null || idlaborcontrato.isEmpty()) {
                    mensajeError += Integer.toString(i + 1) + "\t| "
                            + idlaborcontrato + "\t" + "\t| "
                            + "Codigo de labor no encontrado\n";
                    error = true;
                } else {
                    lb = laboresDao.consultar(Integer.parseInt(idlaborcontrato));
                    if (lb == null) {
                        mensajeError += Integer.toString(i + 1) + "\t| "
                                + idlaborcontrato + "\t" + "\t| "
                                + "Codigo de labor no encontrado\n";
                        error = true;
                    } else if (lb.getActivo() == 0) {
                        mensajeError += Integer.toString(i + 1) + "\t| "
                                + idlaborcontrato + "\t" + "\t| "
                                + "Labor inactiva en el sistema\n";
                        error = true;
                    } else if (lb.getHoraextra() != 0) {
                        mensajeError += Integer.toString(i + 1) + "\t| "
                                + idlaborcontrato + "\t" + "\t| "
                                + "Codigo de Labor pertenece a labor extra\n";
                        error = true;
                    }
                }//Fin validaciones por registro

                int minutosDiurnos = conversionMinutos(getTiempo(fila.getCell(6)));
                int minutosNocturno = conversionMinutos(getTiempo(fila.getCell(7)));
                int minutosFestivo = conversionMinutos(getTiempo(fila.getCell(8)));
                int minutosFestNoct = conversionMinutos(getTiempo(fila.getCell(9)));

                /**
                 * Crear el registro de la labor
                 */
                if (lb != null && ub != null && minutosDiurnos > 0 && !error) {
                    rb = new RegistroBeans();
                    rb.setUsuario(ub);
                    rb.setLabor(lb);
                    rb.setFechaInicio(sdfr.format(finicio));
                    rb.setFechaFin(sdfr.format(ffin));
                    rb.setEstado(2);
                    rb.setAnulado(anulado);
                    rb.setObservacion(observacion);
                    rb.setRegistroslabor(0);
                    rb.setTiempolabor(minutosDiurnos);
                    rb.setValor(Integer.parseInt(lb.getValor()));
                    rb.setCosto(Integer.parseInt(lb.getCosto()));

                    registros.add(rb);

                    int codlabor = lb.getLabor();
                    if (minutosNocturno > 0) {
                        try {
                            lb = laboresDao.consultar(lb.getContrato().getIdcontrato(),
                                    lb.getGrupo().getIdgrupo(),
                                    lb.getLabor(),
                                    lb.getTipolabor(), 5);
                            rb = rb.clone();
                            rb.setLabor(lb);
                            rb.setTiempolabor(minutosNocturno);
                            rb.setCosto(Integer.parseInt(lb.getCosto()));
                            rb.setValor(Integer.parseInt(lb.getValor()));
                            registros.add(rb);
                        } catch (Exception e) {
                            System.out.println(codlabor + " - 5");
                            error = true;
                            ReferenciasBeans labor = new ReferenciasDao().consultar(codlabor, "RLABORES");
                            mensajeError += "Falta parametrizar labor nocturna de " + labor.getDescripcion() + "\n";
                            //throw e;
                        }
                    }
                    if (minutosFestivo > 0) {
                        try {
                            lb = laboresDao.consultar(lb.getContrato().getIdcontrato(),
                                    lb.getGrupo().getIdgrupo(),
                                    lb.getLabor(),
                                    lb.getTipolabor(), 4);
                            rb = rb.clone();
                            rb.setLabor(lb);
                            rb.setTiempolabor(minutosFestivo);
                            rb.setCosto(Integer.parseInt(lb.getCosto()));
                            rb.setValor(Integer.parseInt(lb.getValor()));

                            registros.add(rb);
                        } catch (Exception e) {
                            System.out.println(codlabor + " - 4");
                            error = true;
                            ReferenciasBeans labor = new ReferenciasDao().consultar(codlabor, "RLABORES");
                            mensajeError += "Falta parametrizar labor festiva de " + labor.getDescripcion() + "\n";
                            //throw e;
                        }
                    }
                    if (minutosFestNoct > 0) {
                        try {
                            lb = laboresDao.consultar(lb.getContrato().getIdcontrato(),
                                    lb.getGrupo().getIdgrupo(),
                                    lb.getLabor(),
                                    lb.getTipolabor(), 10);
                            rb = rb.clone();
                            rb.setLabor(lb);
                            rb.setTiempolabor(minutosFestNoct);
                            rb.setCosto(Integer.parseInt(lb.getCosto()));
                            rb.setValor(Integer.parseInt(lb.getValor()));

                            registros.add(rb);
                        } catch (Exception e) {
                            System.out.println(codlabor + " - 10");
                            error = true;
                            ReferenciasBeans labor = new ReferenciasDao().consultar(codlabor, "RLABORES");
                            mensajeError += "Falta parametrizar labor festiva nocturna de " + labor.getDescripcion() + "\n";
                            //throw e;
                        }
                    }

                    acumDiurnos += minutosDiurnos;
                    acumNocturno += minutosNocturno;
                    acumFestivos += minutosFestivo;
                    acumFestNoct += minutosFestNoct;

                    procesados++;
                }
            }//Final for

            int filaTotales = ultimaFila + 2;

            /**
             * Datos del plano
             */
            long totalDiurnas = conversionMinutos(hoja.getRow(filaTotales).getCell(6).getNumericCellValue());
            long totalNocturnas = conversionMinutos(hoja.getRow(filaTotales).getCell(7).getNumericCellValue());
            long totalFestivas = conversionMinutos(hoja.getRow(filaTotales).getCell(8).getNumericCellValue());
            long totalFestNocturnas = conversionMinutos(hoja.getRow(filaTotales).getCell(9).getNumericCellValue());

            /**
             * Validacion totales
             */
            if (!error) {
                if ((acumDiurnos != totalDiurnas)
                        || (acumNocturno != totalNocturnas)
                        || (acumFestivos != totalFestivas)
                        || (acumFestNoct != totalFestNocturnas)) {
                    error = true;
                    mensajeError += "\nValores totales no coinciden\n";
                    mensajeError += "Minutos \t|" + " "
                            + "Diurnas" + " "
                            + "   Nocturnas" + " "
                            + " Festivas" + " "
                            + "Festivas Nocturnas\n";
                    mensajeError += "Totales "
                            + Long.toString(totalDiurnas) + "\t| "
                            + Long.toString(totalNocturnas) + "\t| "
                            + Long.toString(totalFestivas) + "\t| "
                            + Long.toString(totalFestNocturnas) + "\n";
                    mensajeError += "Registro" + "\t| "
                            + Integer.toString(acumDiurnos) + "\t| "
                            + Integer.toString(acumNocturno) + "\t| "
                            + Integer.toString(acumFestivos) + "\t| "
                            + Integer.toString(acumFestNoct) + "\n";

                }
            }
            cantRegistros += ultimaFila - 11;
            if (observacionArchivo.length() > 0) {
                observacionArchivo += "\n";
            }
            observacionArchivo += observacion;
            if (fechaIni == null) {
                fechaIni = finicio;
                fechaFin = ffin;
            } else if (ffin.compareTo(fechaFin) > 0) {
                fechaFin = ffin;

            }

        }//Fin Recorrido por hojas
        if (!error) {
            for (RegistroBeans registro : registros) {
                int resultado = registrosDao.guardarRegistro(registro);
                if (resultado != 0) {
                    if (resultado > 0) {
                        nuevos++;
                    } else {
                        actualizados++;
                    }
                } else {
                    errores++;
                }
            }
        }

        /**
         * respuesta
         */
        params.put("error", error);
        params.put("errormessage", error ? mensajeError : "");
        params.put("observacion", observacionArchivo);
        params.put("fechainicio", sdf.format(fechaIni));
        params.put("fechafin", sdf.format(fechaFin));
        params.put("registros", cantRegistros);
        params.put("procesados", procesados);
        params.put("nuevos", nuevos);
        params.put("errores", errores);
        params.put("actualizados", actualizados);

        return new JSONObject(params);
    }

    private int conversionMinutos(double tiempo) {
        return (int) Math.round(tiempo * 24 * 60);
    }

    private double getTiempo(HSSFCell celda) {
        if (celda.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
            return celda.getNumericCellValue();
        }
        return 0;
    }
}
