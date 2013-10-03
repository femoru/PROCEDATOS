/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.co.sio.java.servlet;

import com.co.sio.java.JSON.JSONObject;
import com.co.sio.java.dao.PrenominaDao;

import com.co.sio.java.mbeans.NominaBeans;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author jcarvajal
 */
@WebServlet(name = "PrenominaServlet", urlPatterns = {"/PrenominaServlet"})
public class PrenominaServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, Exception {
        response.setContentType("text/html;charset=UTF-8");
        try {
            int validacion = 0;
            String dateIni = request.getParameter("dateIni");
            String dateFin = request.getParameter("dateFin");
            PrenominaDao prenominaD = new PrenominaDao();

            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");

            int validacionProduccion = prenominaD.validacionProduccion(dateIni, dateFin);
            System.out.println("Produccion = " + validacionProduccion);
            int validacionNovedades = prenominaD.validacionNovedades(dateIni, dateFin);
            System.out.println("Novedades = " + validacionNovedades);

            if (validacionProduccion < 0 || validacionNovedades < 0) {
                validacion = 4;
            }
            if (validacionNovedades > 0) {
                validacion = 2;
            }
            if (validacionProduccion > 0) {
                validacion = 1;
            }
            if (!prenominaD.existenRegistros(dateIni, dateFin)) {
                validacion = 3;
            }
            response.getWriter().print(validacion);

            response.getWriter().close();
        } catch (Exception ex) {
            Logger.getLogger(PrenominaServlet.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String oper = request.getParameter("oper");
            if (oper == null) {
                processRequest(request, response);
            } else {
                int intpage = new Integer(request.getParameter("page"));
                int limit = new Integer(request.getParameter("rows"));

                String json;
                PrenominaDao nominaDaoao;
                nominaDaoao = new PrenominaDao();

                json = nominaDaoao.getListPrenomina(intpage, limit);

                response.setContentType("application/json");
                response.setCharacterEncoding("utf-8");
                response.setHeader("Pragma", "no-cache");
                response.setHeader("Cache-Control", "no-cache,must-revalidate");
                response.setHeader("Pragma", "no-cache");

                response.getWriter().print(json);
                response.getWriter().close();
            }
        } catch (Exception ex) {
            Logger.getLogger(PrenominaServlet.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String oper = request.getParameter("oper");

            PrenominaDao nominaDao = new PrenominaDao();

            switch (oper.charAt(0)) {
                case 'n': {

                    String jsonNombres;

                    JSONObject jsono = new JSONObject();
                    jsono.put("nombres", nominaDao.nombresColumnas());
                    jsono.put("modelo", nominaDao.modeloColumnas());

                    jsonNombres = jsono.toString();
                    response.setContentType("application/json");
                    response.setCharacterEncoding("utf-8");
                    response.getWriter().print(jsonNombres);
                    response.getWriter().close();
                }
                break;
                case 'c': {//Consulta dia dentro de una nomina especifica
                    String idnomina = request.getParameter("idnomina");
                    String fecha = request.getParameter("fecha");

                    response.getWriter().print(nominaDao.verificarFecha(idnomina, fecha));

                }
                break;
                case 'm': {//lanza el calculo de prenomina para un usuario y nomina especifica
                    String idusuario = request.getParameter("usuario");
                    String idnomina = request.getParameter("nomina");
                    String finicial = request.getParameter("finicial");
                    String ffinal = request.getParameter("ffinal");
                    
                    boolean retorno = nominaDao.terminarModificaciones(idusuario, idnomina, finicial, ffinal);
                    if (oper.equals("mCalculo") && retorno) {
                        nominaDao.actualizarNomina(Integer.parseInt(idnomina), 2);
                    }
                    response.getWriter().print(retorno);

                }
                break;
                case 'v': {//Consulta dia dentro de alguna nomina
                    String fecha = request.getParameter("fecha");
                    if (nominaDao.consultarDiaNomina(fecha)) {
                        response.getWriter().print(1);
                    }
                }
                break;
                case 'e': {//Consultar fecha dentro de nomina
                    String dateIni = request.getParameter("dateIni");
                    String dateFin = request.getParameter("dateFin");
                    NominaBeans nomina = nominaDao.consultarNomina(dateIni, dateFin);
                    JSONObject jsono;
                    if (nomina != null) {
                        jsono = new JSONObject(0);
                    } else {
                        nominaDao.insertarNomina(dateIni, dateFin);
                        nomina = nominaDao.consultarNomina(dateIni, dateFin);
                        jsono = new JSONObject(nomina);

                    }
                    response.getWriter().print(jsono.toString());
                    response.setContentType("application/json");
                    response.setCharacterEncoding("utf-8");
                }
                break;
                case 'f': {//Consultar nomina
                    String dateIni = request.getParameter("fechainicio");
                    String dateFin = request.getParameter("fechafin");
                    NominaBeans nomina = nominaDao.consultarNomina(dateIni, dateFin);
                    JSONObject jsono = null;
                    if (nomina != null) {
                        jsono = new JSONObject(nomina);
                    }
                    response.getWriter().print(jsono == null ? null : jsono.toString());
                    response.setContentType("application/json");
                    response.setCharacterEncoding("utf-8");
                }
                break;
                case 'a': {//nueva nomina
                    String dateIni = request.getParameter("dateIni");
                    String dateFin = request.getParameter("dateFin");
                    if (!nominaDao.consultarPorRango(dateIni, dateFin)) {
                        nominaDao.insertarNomina(dateIni, dateFin);
                        NominaBeans nomina = nominaDao.consultarNomina(dateIni, dateFin);
                        nominaDao.actualizarRegistrosNomina(nomina);
                        response.getWriter().print(new JSONObject(nomina).toString());
                    }
                    response.setContentType("application/json");
                    response.setCharacterEncoding("utf-8");
                }
                break;
                case 'u': {
                    String idnomina = request.getParameter("nomina");
                    String estado = request.getParameter("estado");
                    response.getWriter().print(nominaDao.actualizarNomina(Integer.parseInt(idnomina), Integer.parseInt(estado)));
                    response.setContentType("application/json");
                    response.setCharacterEncoding("utf-8");
                }
                break;
                case 'r': {
                    int validacion;
                    String dateIni = request.getParameter("dateIni");
                    String dateFin = request.getParameter("dateFin");

                    NominaBeans nomina = nominaDao.consultarNomina(dateIni, dateFin);

                    int cregistros = nominaDao.validacionProduccion(dateIni, dateFin);
                    if (cregistros == 0) {
                        validacion = 0;
                        int cnovedades = nominaDao.validacionNovedades(dateIni, dateFin);
                        if (cnovedades == 1) {
                            validacion = 2;
                        } else {
                            if (nominaDao.actualizarRegistrosNomina(nomina) == false) {
                                validacion = 4;
                            } else {
                                //nominaDao.actualizarNomina(nomina.getId(), 4);
                            }
                        }
                    } else {
                        validacion = cregistros;
                    }
                    response.getWriter().print(validacion);
                    response.setContentType("application/json");
                    response.setCharacterEncoding("utf-8");
                }
                break;
                case 'g': {
                    String idnomina = request.getParameter("nomina");
                    response.getWriter().print(nominaDao.cerrarRegistrosNomina(Integer.parseInt(idnomina)));
                    response.setContentType("application/json");
                    response.setCharacterEncoding("utf-8");
                }
                break;

                case 'x': {
                    OutputStream out = response.getOutputStream();
                    File archivo = nominaDao.datosNomina();
                    FileInputStream in = new FileInputStream(archivo);
                    byte[] buffer = new byte[4096];
                    int length;
                    response.setHeader("Content-Type", "application/vnd.ms-excel");
                    response.setHeader("Content-Disposition", "attachment; filename=" + archivo.getName() + "");

                    while ((length = in.read(buffer)) > 0) {
                        out.write(buffer, 0, length);
                    }
                    in.close();
                    out.flush();

                }
                break;
            }
        } catch (Exception ex) {
            Logger.getLogger(PrenominaServlet.class.getName()).log(Level.SEVERE, null, ex);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
