/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.co.sio.java.servlet;

import com.co.sio.java.dao.GruposDao;
import com.co.sio.java.dao.UsuarioDao;
import com.co.sio.java.mbeans.GrupoBeans;
import com.co.sio.java.mbeans.UsuarioBeans;
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
@WebServlet(name = "GrupoServlet", urlPatterns = {"/GrupoServlet"})
public class GrupoServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String listar = request.getParameter("listar");
        int idarea, idcliente;

        try {
            int intpage = new Integer(request.getParameter("page"));
            int limit = new Integer(request.getParameter("rows"));

            String sidx = request.getParameter("sidx");
            String sord = request.getParameter("sord");

            String search = request.getParameter("_search");

            String json = "";
            GruposDao gruposDao;
            gruposDao = new GruposDao();

            if (search.equals("false")) {

                if (listar!=null) { 
                    idarea = Integer.parseInt(request.getParameter("area"));
                    json = gruposDao.getListGrupos(intpage, limit, sidx, sord, idarea);
                } else {
                    idcliente = Integer.parseInt(request.getParameter("cliente"));
                    json= gruposDao.getListGruposCliente(intpage, limit, sidx, sord, idcliente);
                }
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

        GruposDao gruposDao = new GruposDao();
        GrupoBeans grupoBeans = new GrupoBeans();

        String oper = request.getParameter("oper");
        try {

            grupoBeans.setIdarea(Integer.parseInt(request.getParameter("area")));
            grupoBeans.setDesgrupo(request.getParameter("Descripcion"));

            UsuarioDao usuarioDao = new UsuarioDao();
            UsuarioBeans cordinador = usuarioDao.consultar(Integer.parseInt(request.getParameter("Coordinador")));
            grupoBeans.setCordinador(cordinador);

            switch (oper.charAt(0)) {
                case 'a': {

                    gruposDao.Guardar(grupoBeans);
                }
                break;
                case 'e': {
                    grupoBeans.setIdgrupo(Integer.parseInt(request.getParameter("id")));
                    gruposDao.Actualizar(grupoBeans);
                }
                break;

            }
        } catch (Exception ex) {
            Logger.getLogger(GrupoServlet.class.getName()).log(Level.SEVERE, null, ex);
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
