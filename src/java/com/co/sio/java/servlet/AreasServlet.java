/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.co.sio.java.servlet;

import com.co.sio.java.dao.AreasDao;
import com.co.sio.java.dao.ClientesDao;
import com.co.sio.java.mbeans.AreaBeans;
import com.co.sio.java.mbeans.ClienteBeans;
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
@WebServlet(name = "AreasServlet", urlPatterns = {"/AreasServlet"})
public class AreasServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int intpage = new Integer(request.getParameter("page"));
            int limit = new Integer(request.getParameter("rows"));
            int idcliente = new Integer(request.getParameter("cliente"));

            String sidx = request.getParameter("sidx");
            String sord = request.getParameter("sord");

            String search = request.getParameter("_search");

            String json = "";
            AreasDao areasDao;
            areasDao = new AreasDao();

            if (search.equals("false")) {
                json = areasDao.getListAreas(intpage, limit, sidx, sord, idcliente);
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
            Logger.getLogger(AreasServlet.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        AreasDao areasDao= new AreasDao();
        AreaBeans areaBeans= new AreaBeans();
        ClientesDao clientesDao= new ClientesDao();
        
        try {
            String oper = request.getParameter("oper");
            areaBeans.setArea(new Integer(request.getParameter("Descripcion")));
            areaBeans.setActivo(new Integer(request.getParameter("Estado")));
            ClienteBeans cliente= clientesDao.consultar(new Integer(request.getParameter("cliente")));
            areaBeans.setCliente(cliente);
            
            switch(oper.charAt(0)){
                case 'a':
                    areasDao.guardar(areaBeans);
                    break;
                case 'e':
                    areaBeans.setId(new Integer(request.getParameter("id")));
                    areasDao.actualizar(areaBeans);
                    break;
            }
            
            
        } catch (Exception ex) {
            Logger.getLogger(AreasServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        
        
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
