/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.co.sio.java.servlet;

import com.co.sio.java.dao.LaboresDao;
import com.co.sio.java.dao.UsuarioDao;
import com.co.sio.java.dao.UsuarioLaboresDao;
import com.co.sio.java.mbeans.UsuarioLaboresBeans;
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
@WebServlet(name = "UsuarioLaboresServlet", urlPatterns = {"/UsuarioLaboresServlet"})
public class UsuarioLaboresServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int intpage = new Integer(request.getParameter("page"));
            int limit = new Integer(request.getParameter("rows"));
            String grupo = request.getParameter("idgrupo");
            String usuario = request.getParameter("usuario");
            String contrato = request.getParameter("idcontrato");

            String sidx = request.getParameter("sidx");
            String sord = request.getParameter("sord");

            String search = request.getParameter("_search");

            String json = "";
            UsuarioLaboresDao laboresDao = new UsuarioLaboresDao();

            if (search.equals("false")) {
                int idusuario = Integer.parseInt(usuario);
                if (grupo != null) {
                    int idgrupo = Integer.parseInt(grupo);
                    int idcontrato = new Integer(contrato);

                    json = laboresDao.getListLaboresUsuario(intpage, limit, sidx, sord, idgrupo, idcontrato, idusuario);
                } else {
                    json = laboresDao.getListLaboresUsuario(intpage, limit, sidx, sord, idusuario);
                }
            }
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Cache-Control", "no-cache,must-revalidate");
            response.setHeader("Pragma", "no-cache");

            response.getWriter().print(json);
            response.getWriter().close();
        } catch (Exception ex) {
            Logger.getLogger(UsuarioLaboresServlet.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int idusuario = Integer.parseInt(request.getParameter("usuario"));
            int idlabor = Integer.parseInt(request.getParameter("id"));
            int activo = Integer.parseInt(request.getParameter("activo"));
            String fechaInicio = request.getParameter("fechaI");
            String fechaFin = request.getParameter("fechaF");
            UsuarioLaboresDao usuarioLaboresDao = new UsuarioLaboresDao();
            UsuarioLaboresBeans beans = new UsuarioLaboresBeans();
            LaboresDao laboresDao = new LaboresDao();
            UsuarioDao usuarioDao = new UsuarioDao();

            beans.setUsuario(usuarioDao.consultar(idusuario));
            beans.setLabor(laboresDao.consultar(idlabor));

            switch (activo) {
                case 1:
                    beans.setFechaInicio(fechaInicio);
                    beans.setFechaFin(fechaFin);
                    usuarioLaboresDao.insertar(beans);
                    break;
                case 0:
                    usuarioLaboresDao.eliminar(beans);
                    break;
            }

        } catch (Exception ex) {
            Logger.getLogger(UsuarioLaboresServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
