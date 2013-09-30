/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.co.sio.java.servlet;

import com.co.sio.java.dao.ParametroDao;
import com.co.sio.java.mbeans.ParametroBeans;
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
@WebServlet(name = "ParametroServlet", urlPatterns = {"/ParametroServlet"})
public class ParametroServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int intpage = new Integer(request.getParameter("page"));
            int limit = new Integer(request.getParameter("rows"));

            String sidx = request.getParameter("sidx");
            String sord = request.getParameter("sord");

            String search = request.getParameter("_search");

            String json = "";
            ParametroDao parametroDao;
            parametroDao = new ParametroDao();

            if (search.equals("false")) {
                json = parametroDao.getListParametros(intpage, limit, sidx, sord);
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
            Logger.getLogger(ParametroServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

   @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        ParametroDao parametroDao = new ParametroDao();
        ParametroBeans parametroBeans = new ParametroBeans();
        try {

            String oper = request.getParameter("oper");

            
            parametroBeans.setDesparametro(request.getParameter("Descripcion"));
            parametroBeans.setValparametro(request.getParameter("Valor"));

            switch (oper.charAt(0)) {
                case 'a':
                    parametroBeans.setIdparametro(0);
                    parametroDao.Guardar(parametroBeans);
                    break;
                case 'e':
                    parametroBeans.setIdparametro(Integer.parseInt(request.getParameter("Id")));
                    parametroDao.actualizar(parametroBeans);
                    break;
                case 'c':
                    parametroBeans = parametroDao.consultar(Integer.parseInt(request.getParameter("id")));
                    response.getWriter().print(parametroBeans.getValparametro());
                    break;
            }

        } catch (Exception e) {
            Logger.getLogger(ParametroServlet.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
