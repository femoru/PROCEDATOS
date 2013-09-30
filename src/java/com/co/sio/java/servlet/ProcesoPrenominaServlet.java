/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.co.sio.java.servlet;

import com.co.sio.java.dao.PrenominaDao;
import com.co.sio.java.mbeans.NominaBeans;
import java.io.IOException;
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
@WebServlet(name = "ProcesoPrenominaServlet", urlPatterns = {"/ProcesoPrenominaServlet"})
public class ProcesoPrenominaServlet extends HttpServlet {

    @SuppressWarnings("empty-statement")
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, Exception {
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try {
            int validacion = 0;
            String dateIni = request.getParameter("dateIni");
            String dateFin = request.getParameter("dateFin");
            PrenominaDao prenominaD = new PrenominaDao();

            //registrar nueva nomina
            prenominaD.insertarNomina(dateIni, dateFin);
            NominaBeans nomina = prenominaD.consultarNomina(dateIni, dateFin);

            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Cache-Control", "no-cache,must-revalidate");
            response.setHeader("Pragma", "no-cache");

            switch (nomina.getEstado()) {
                case 0:
                    //valida registros estado = 1                    
                    int cregistros = prenominaD.validacionProduccion(dateIni, dateFin);
                    if (cregistros == 0) {
                        validacion = 0;
                        int cnovedades = prenominaD.validacionProduccion(dateIni, dateFin);
                        if (cnovedades == 1) {
                            validacion = 2;
                        } else {
                            if (prenominaD.actualizarRegistrosNomina(nomina) == false) {
                                validacion = 4;
                            }
                        }
                    } else {
                        validacion = cregistros;
                    }
                    response.getWriter().print(validacion);
                    break;
            }
            response.getWriter().close();
        } catch (Exception ex) {
            Logger.getLogger(ProcesoPrenominaServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String oper = request.getParameter("oper");
            PrenominaDao prenominaD = new PrenominaDao();

            switch (oper.charAt(0)) {
                case 'v': {
                    String fecha = request.getParameter("fecha");
                    if (prenominaD.consultarDiaNomina(fecha)) {
                        response.getWriter().print(1);
                    }
                }
                break;
                case 'n': {
                    String dateIni = request.getParameter("dateIni");
                    String dateFin = request.getParameter("dateFin");
                    NominaBeans nomina = prenominaD.consultarNomina(dateIni, dateFin);
                    if (nomina == null) {
                        response.getWriter().print(0);
                    } else {
                        response.getWriter().print(1);
                    }
                }
                break;
                case 'd': {
                    String dateIni = request.getParameter("dateIni");
                    String dateFin = request.getParameter("dateFin");
                    NominaBeans nomina = prenominaD.consultarNomina(dateIni, dateFin);
                    if (nomina == null) {
                        response.getWriter().print(0);
                    } else {
                        response.getWriter().print(1);
                    }
                }
                break;
            }


            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Cache-Control", "no-cache,must-revalidate");
            response.setHeader("Pragma", "no-cache");

        } catch (Exception ex) {
            Logger.getLogger(ProcesoPrenominaServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
