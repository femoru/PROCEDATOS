/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.co.sio.java.servlet;

import com.co.sio.java.dao.UsuarioDao;
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
@WebServlet(name = "UsuarioServlet", urlPatterns = {"/UsuarioServlet"})
public class UsuarioServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int intpage = new Integer(request.getParameter("page"));
            int limit = new Integer(request.getParameter("rows"));

            String sidx = request.getParameter("sidx");
            String sord = request.getParameter("sord");

            String json;
            UsuarioDao usuarioDao;
            usuarioDao = new UsuarioDao();
            String sGrupo = request.getParameter("grupo");
            String fecha = request.getParameter("fecha");
            if (sGrupo != null) {
                int grupo = Integer.parseInt(sGrupo);
                if (fecha != null) {
                    json = usuarioDao.getListUsuario(intpage, limit, grupo,fecha);
                } else {
                    json = usuarioDao.getListUsuario(intpage, limit, grupo);
                }
            } else {
                json = usuarioDao.getListUsuario(intpage, limit, sidx, sord);
            }
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Cache-Control", "no-cache,must-revalidate");
            response.setHeader("Pragma", "no-cache");

            response.getWriter().print(json);
            response.getWriter().close();
        } catch (Exception ex) {
            Logger.getLogger(UsuarioServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
