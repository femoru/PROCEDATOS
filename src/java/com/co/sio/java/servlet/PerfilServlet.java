/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.co.sio.java.servlet;

import com.co.sio.java.dao.PerfilDao;
import com.co.sio.java.dao.UsuarioDao;
import com.co.sio.java.mbeans.PerfilBeans;
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
@WebServlet(name = "PerfilServlet", urlPatterns = {"/PerfilServlet"})
public class PerfilServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            PerfilDao daoperfil = new PerfilDao();
            PerfilBeans perfilBeans = new PerfilBeans();
            UsuarioDao daousuario = new UsuarioDao();
            UsuarioBeans digita;

            int idusuario = Integer.parseInt(request.getSession().getAttribute("id_usuario").toString());

            String oper = request.getParameter("oper");

            switch (oper.charAt(0)) {
                case 'a':
                    perfilBeans.setDes_perfil(request.getParameter("descripcion"));
                    perfilBeans.setEstado(Integer.parseInt(request.getParameter("estado")));

                    perfilBeans.setMenu("include(\"media/js/menu/domMenu_" + perfilBeans.getDes_perfil() + ".js\");");

                    digita = daousuario.consultar(idusuario);
                    perfilBeans.setUsuario(digita);

                    daoperfil.insertar(perfilBeans);
                    break;
                case 'e':
                    perfilBeans = daoperfil.consultar(Integer.parseInt(request.getParameter("id")));
                    perfilBeans.setEstado(Integer.parseInt(request.getParameter("estado")));

                    digita = daousuario.consultar(idusuario);
                    perfilBeans.setUsuario(digita);

                    daoperfil.actualizar(perfilBeans);
                    break;
                case 'c':
                    perfilBeans = daoperfil.consultar(Integer.parseInt(request.getParameter("codperfil")));

                    String desPerfil = perfilBeans.getDes_perfil();

                    response.setContentType("text/plain");
                    response.setCharacterEncoding("UTF-8");
                    response.getWriter().write(desPerfil);
                    break;
            }
        } catch (Exception ex) {
            Logger.getLogger(PerfilServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

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
            PerfilDao daoPerfil;
            daoPerfil = new PerfilDao();

            if (search.equals("false")) {
                json = daoPerfil.getListPerfil(intpage, limit, sidx, sord);
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
            Logger.getLogger(PerfilServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
