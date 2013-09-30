/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.co.sio.java.servlet;

import com.co.sio.java.dao.ClientesDao;
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
@WebServlet(name = "ClientesServlet", urlPatterns = {"/ClientesServlet"})
public class ClientesServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String json;
        String list = request.getParameter("list");
        int intpage = new Integer(request.getParameter("page"));
        int limit = new Integer(request.getParameter("rows"));
        String sidx = request.getParameter("sidx");
        String sord = request.getParameter("sord");
        try {
            ClientesDao clientesDao = new ClientesDao();
            if (list == null) {
                json = clientesDao.getListClientes(intpage, limit, sidx, sord);
            }else{
                json=clientesDao.getListClientesContratos(intpage, limit, sidx, sord); 
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

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            ClientesDao clientesDao = new ClientesDao();
            ClienteBeans clienteBeans = new ClienteBeans();
            String oper = request.getParameter("oper");

            if (oper.charAt(0) != 'c') {

                clienteBeans.setNitcliente(request.getParameter("Nit"));
                clienteBeans.setNomcliente(request.getParameter("Nombre"));
                clienteBeans.setDireccion(request.getParameter("Direccion"));
                clienteBeans.setTelefono(request.getParameter("Telefono"));
                clienteBeans.setContacto(request.getParameter("Contacto"));
                clienteBeans.setTelefonocontacto(request.getParameter("TelefonoContacto"));
                clienteBeans.setEmail(request.getParameter("Email"));
            }

            switch (oper.charAt(0)) {
                case 'a':
                    clientesDao.Guardar(clienteBeans);
                    break;
                case 'e':
                    clienteBeans.setIdcliente(Integer.parseInt(request.getParameter("id")));
                    clientesDao.Actualizar(clienteBeans);
                    break;
                case 'c':
                    clienteBeans = clientesDao.consultar(Integer.parseInt(request.getParameter("id")));
                    break;
            }
        } catch (Exception ex) {
            Logger.getLogger(ClientesServlet.class.getName()).log(Level.SEVERE, null, ex);
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
