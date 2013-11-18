/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.co.sio.java.servlet;

import com.co.sio.java.dao.ReporteDao;
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
 * @author fmoctezuma
 */
@WebServlet(name = "ReportesServlet", urlPatterns = {"/ReportesServlet"})
public class ReportesServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String reporte = request.getParameter("reporte");

            ReporteDao reporteDao = new ReporteDao();
            String json = "{}";
            switch (reporte.charAt(0)) {
                case 'f': {
                    int intpage = new Integer(request.getParameter("page"));
                    int limit = new Integer(request.getParameter("rows"));
                    json = reporteDao.getListFacturacion(intpage, limit);
                }
                break;
                case 'u': {
                    int intpage = new Integer(request.getParameter("page"));
                    int limit = new Integer(request.getParameter("rows"));
                    json = reporteDao.getListUsuario(intpage, limit, request.getParameter("mes"));
                }
                break;

            }
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Cache-Control", "no-cache,must-revalidate");
            response.setHeader("Pragma", "no-cache");

            response.getWriter().print(json);
            response.getWriter().close();
        } catch (Exception ex) {
            Logger.getLogger(ReportesServlet.class.getName()).log(Level.SEVERE, null, ex);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            ReporteDao reporteDao = new ReporteDao();
            String oper = request.getParameter("oper");


            switch (oper.charAt(0)) {
                case 'g': {
                    generarFacturacion(request, response, reporteDao);
                }
                break;
                case 'u': {
                    generarReporteUsuarios(response, reporteDao, request.getParameter("mes"));
                }
                break;

            }
        } catch (Exception ex) {
            Logger.getLogger(ReportesServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void generarFacturacion(HttpServletRequest request, HttpServletResponse response, ReporteDao reporteDao) throws Exception {


        int reporte = Integer.parseInt(request.getParameter("reporte"));
        int clase = Integer.parseInt(request.getParameter("clase"));
        int idnomina = Integer.parseInt(request.getParameter("nomina"));
        String fini = request.getParameter("fini");
        String ffin = request.getParameter("ffin");
        String labores = request.getParameter("labores");
        String nombre = null;

        switch (reporte) {
            case 0:
                if (clase == 0) {
                    nombre = reporteDao.consolidadoFacturacion(labores, idnomina);
                } else {
                    nombre = reporteDao.consolidadoFacturacion(labores, fini, ffin);
                }
                break;
            case 1:
                if (clase == 0) {
                    nombre = reporteDao.detalladoFacturacion(labores, idnomina);
                } else {
                    nombre = reporteDao.detalladoFacturacion(labores, fini, ffin);
                }
                break;
            case 2:
                if (clase == 0) {
                    nombre = reporteDao.consolidadoNomina(labores, idnomina);
                } else {
                    nombre = reporteDao.consolidadoNomina(labores, fini, ffin);
                }
                break;
            case 3:
                if (clase == 0) {
                    nombre = reporteDao.detalladoNomina(labores, idnomina);
                } else {
                    nombre = reporteDao.detalladoNomina(labores, fini, ffin);
                }
                break;
            case 4:
                nombre = reporteDao.reporteFacturacion(labores, idnomina);
                break;
        }

        if (nombre != null) {
            OutputStream out = response.getOutputStream();
            String arch = getServletContext().getRealPath("/media/reports/" + nombre);

            File archivo = new File(arch);
            FileInputStream in = new FileInputStream(archivo);
            byte[] buffer = new byte[4096];
            int length;
            response.setHeader("Content-Type", "application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + archivo.getName() + "\"");

            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }
            in.close();
            out.flush();
            archivo.delete();
        }

    }

    private void generarReporteUsuarios(HttpServletResponse response, ReporteDao reporteDao, String mes) throws Exception {
        String nombre = reporteDao.estadoUsuarios(mes);

        if (nombre != null) {
            OutputStream out = response.getOutputStream();
            String arch = getServletContext().getRealPath("/media/reports/" + nombre);

            File archivo = new File(arch);
            FileInputStream in = new FileInputStream(archivo);
            byte[] buffer = new byte[4096];
            int length;
            response.setHeader("Content-Type", "application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + archivo.getName() + "\"");

            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }
            in.close();
            out.flush();
            archivo.delete();
        }
    }
}
