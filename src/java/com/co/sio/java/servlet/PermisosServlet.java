/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.co.sio.java.servlet;

import com.co.sio.java.dao.PerfilDao;
import com.co.sio.java.dao.PermisoPerfilDao;
import com.co.sio.java.dao.PermisosDao;
import com.co.sio.java.dao.UsuarioDao;
import com.co.sio.java.mbeans.PermisosPerfilesBeans;
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
@WebServlet(name = "PermisosServlet", urlPatterns = {"/PermisosServlet"})
public class PermisosServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            PermisosDao daopermisos = new PermisosDao();
            String perfilId = request.getParameter("perfil");
            
            if (perfilId == null) perfilId = "0";
            
            String json = daopermisos.listarPermisos(perfilId);
            response.setContentType("application/json");            
            response.getWriter().print(json);
            response.getWriter().close();
        } catch (Exception ex) {
            Logger.getLogger(PermisosServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        boolean confirmar = false;
        try {
            PermisosPerfilesBeans permisosPerfilesBeans;
            UsuarioDao daousuario = new UsuarioDao();
            PermisoPerfilDao permisoPerfilDao = new PermisoPerfilDao();
            PerfilDao PerfilDao = new PerfilDao();
            
            UsuarioBeans digita;
            digita = daousuario.consultar(Integer.parseInt(request.getSession().getAttribute("id_usuario").toString()));

            String nodos = request.getParameter("nodes");
            String codperfil = request.getParameter("perfil");
            String[] listaNodos = nodos.split(",");

            boolean Borrar = permisoPerfilDao.Eliminar(codperfil);

            if (Borrar) {
                for (String codpermiso : listaNodos) {
                    if (!codpermiso.startsWith("GP")) {
                        permisosPerfilesBeans = new PermisosPerfilesBeans();
                        permisosPerfilesBeans.setCodperfil(codperfil);
                        permisosPerfilesBeans.setCodpermiso(codpermiso);
                        permisosPerfilesBeans.setUsuario(digita);
                        permisoPerfilDao.Guardar(permisosPerfilesBeans);
                    }
                }
                PerfilDao.actualizarModifica(Integer.parseInt(codperfil),digita.getIdusuario());
            }
            confirmar = true;
        } catch (Exception ex) {
            Logger.getLogger(PermisosServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("" + confirmar);
    }
}
