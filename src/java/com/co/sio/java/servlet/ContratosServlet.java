/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.co.sio.java.servlet;

import com.co.sio.java.dao.ClientesDao;
import com.co.sio.java.dao.ContratosDao;
import com.co.sio.java.mbeans.ContratoBeans;
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
@WebServlet(name = "ContratosServlet", urlPatterns = {"/ContratosServlet"})
public class ContratosServlet extends HttpServlet {

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
            ContratosDao contratosDao;
            contratosDao = new ContratosDao();

            if (search.equals("false")) {
                json = contratosDao.getListContratos(intpage, limit, sidx, sord, idcliente);
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
            Logger.getLogger(ContratosServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String oper = request.getParameter("oper");

            ContratosDao contratosDao = new ContratosDao();
            ContratoBeans contratoBeans = new ContratoBeans();

            contratoBeans.setNumcontrato(request.getParameter("numero"));
            contratoBeans.setFechainicio(request.getParameter("inicio"));
            contratoBeans.setFechafin(request.getParameter("fin"));
            contratoBeans.setActivo(Integer.parseInt(request.getParameter("activo")));
            ClientesDao clientesDao = new ClientesDao();
            contratoBeans.setCliente(clientesDao.consultar(Integer.parseInt(request.getParameter("cliente"))));

            switch (oper.charAt(0)) {
                case 'a':
                    contratosDao.Guardar(contratoBeans);
                    break;
                case 'e':
                    contratoBeans.setIdcontrato(Integer.parseInt(request.getParameter("id")));
                    contratosDao.Actualizar(contratoBeans);
                    break;
            }
        } catch (Exception ex) {
            Logger.getLogger(ContratosServlet.class.getName()).log(Level.SEVERE, null, ex);
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
