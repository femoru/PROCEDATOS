/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.co.sio.java.servlet;

import com.co.sio.java.dao.ReferenciasDao;
import com.co.sio.java.mbeans.ReferenciasBeans;
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
@WebServlet(name = "ReferenciasServlet", urlPatterns = {"/ReferenciasServlet"})
public class ReferenciasServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String nomtabla = request.getParameter("nomtabla");
        if (nomtabla != null) {
            doPost(request, response);
        } else {
            try {
                int intpage = new Integer(request.getParameter("page"));
                int limit = new Integer(request.getParameter("rows"));

                String sidx = request.getParameter("sidx");
                String sord = request.getParameter("sord");

                String search = request.getParameter("_search");

                String json = "";
                ReferenciasDao referenciasDao;
                referenciasDao = new ReferenciasDao();

                if (search.equals("false")) {
                    json = referenciasDao.getListReferencias(intpage, limit, sidx, sord);
                } else {
                }
                response.setContentType("application/json");
                response.setCharacterEncoding("utf-8");
                response.setHeader("Pragma", "no-cache");
                response.setHeader("Cache-Control", "no-cache,must-revalidate");
                response.setHeader("Pragma", "no-cache");

                response.getWriter().print(json);
                response.getWriter().close();
            } catch (Exception ex) {
                Logger.getLogger(ReferenciasServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {

            String nomtabla = request.getParameter("nomtabla");
            String oper = request.getParameter("oper");

            ReferenciasDao referenciasDao = new ReferenciasDao();
            ReferenciasBeans referenciasBeans = new ReferenciasBeans();

            switch (oper.charAt(0)) {

                case 't': {

                    String json = referenciasDao.listarArbolReferencias(nomtabla);

                    response.setContentType("application/json");
                    response.getWriter().print(json);
                    response.getWriter().close();
                }
                break;

                case 'o': {

                    int intpage = new Integer(request.getParameter("page"));
                    int limit = new Integer(request.getParameter("rows"));

                    String sidx = request.getParameter("sidx");
                    String sord = request.getParameter("sord");

                    String search = request.getParameter("_search");

                    String json = "";
                    referenciasDao = new ReferenciasDao();

                    if (search.equals("false")) {
                        json = referenciasDao.getListadoReferencias(intpage, limit, sidx, sord, nomtabla);
                    } else {
                    }
                    response.setContentType("application/json");
                    response.setCharacterEncoding("utf-8");
                    response.setHeader("Pragma", "no-cache");
                    response.setHeader("Cache-Control", "no-cache,must-revalidate");
                    response.setHeader("Pragma", "no-cache");

                    response.getWriter().print(json);
                    response.getWriter().close();

                }
                break;

                case 'a':
                    //referenciasBeans.setCodigo(Integer.parseInt(request.getParameter("id")));
                    referenciasBeans.setDescripcion(request.getParameter("Descripcion"));
                    referenciasBeans.setNombreTabla(nomtabla);
                    referenciasDao.Guardar(referenciasBeans);
                    break;

                case 'e':
                    referenciasBeans.setCodigo(Integer.parseInt(request.getParameter("id"))); 
                    referenciasBeans.setDescripcion(request.getParameter("Descripcion"));
                    referenciasBeans.setNombreTabla(nomtabla);
                    referenciasDao.actualizar(referenciasBeans);
                    break;
            }
        } catch (Exception ex) {
            Logger.getLogger(ReferenciasServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
