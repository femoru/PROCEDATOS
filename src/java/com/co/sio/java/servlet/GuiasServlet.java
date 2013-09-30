/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.co.sio.java.servlet;

import com.co.sio.java.dao.ArchivoGuiaDao;
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
 * @author fmoctezuma
 */
@WebServlet(name = "GuiasServlet", urlPatterns = {"/GuiasServlet"})
public class GuiasServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {


        try {
            String id = request.getParameter("id");
            String cliente = request.getParameter("cliente");
            String laborContrato = request.getParameter("laborContrato");
            String fechaInicio = request.getParameter("inicio");
            String fechaFin = request.getParameter("fin");

            String json;

            ArchivoGuiaDao guiaDao;
            guiaDao = new ArchivoGuiaDao();
            if (id == null) {
                int intpage = new Integer(request.getParameter("page"));
                int limit = new Integer(request.getParameter("rows"));

                String sidx = request.getParameter("sidx");
                String sord = request.getParameter("sord");
                String auxiliar = request.getParameter("auxiliar");
                if (auxiliar == null) {
                    try {
                        Integer.parseInt(cliente);
                        json = guiaDao.getListGuias(intpage, limit, sidx, sord, Integer.parseInt(cliente), Integer.parseInt(laborContrato), fechaInicio, fechaFin);
                    } catch (Exception e) {
                        json = "[]";
                    }
                } else {
                    String guia = request.getParameter("guia");
                    String labor = request.getParameter("labor");
                    json = guiaDao.getListRegistros(Integer.parseInt(auxiliar), Integer.parseInt(labor), guia, fechaInicio, fechaFin);
                }
            } else {
                int intpage = new Integer(request.getParameter("page"));
                int limit = new Integer(request.getParameter("rows"));
                json = guiaDao.getDetalladoRegistros(Integer.parseInt(id), Integer.parseInt(cliente), Integer.parseInt(laborContrato), fechaInicio, fechaFin, intpage, limit);
            }
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Cache-Control", "no-cache,must-revalidate");
            response.setHeader("Pragma", "no-cache");
            System.err.println(json + "n1234");
            response.getWriter().print(json);
            response.getWriter().close();
        } catch (Exception ex) {
            Logger.getLogger(GuiasServlet.class.getName()).log(Level.SEVERE, null, ex);
        }



    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String oper = request.getParameter("oper");

            ArchivoGuiaDao guiaDao;
            guiaDao = new ArchivoGuiaDao();

            switch (oper.charAt(0)) {
                case 'e':
                    int id = Integer.parseInt(request.getParameter("id"));
                    int ingresado = Integer.parseInt(request.getParameter("ingresado"));

                    response.getWriter().println(guiaDao.actualizarRegistro(id, ingresado));
                    break;
                case 'c':
                    int labor = Integer.parseInt(request.getParameter("labor"));
                    String inicio = request.getParameter("inicio");
                    String fin = request.getParameter("fin");

                    response.getWriter().print(guiaDao.conciliarRegistros(labor, inicio, fin));
                    break;
            }

        } catch (Exception ex) {
            Logger.getLogger(GuiasServlet.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
