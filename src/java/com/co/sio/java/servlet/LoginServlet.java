/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.co.sio.java.servlet;

import com.co.sio.java.JSON.JSONObject;
import com.co.sio.java.dao.LoginDao;
import com.co.sio.java.dao.MenuDao;
import com.co.sio.java.dao.PersonaDao;
import com.co.sio.java.mbeans.PersonaBeans;
import com.co.sio.java.mbeans.UsuarioBeans;
import java.io.IOException;
import java.text.MessageFormat;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;

/**
 *
 * @author fmoctezuma
 */
@WebServlet(name = "LoginServlet", urlPatterns = {"/LoginServlet"})
public class LoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {

            UsuarioBeans usuarioBeans = new UsuarioBeans();
            usuarioBeans.setLogin(request.getParameter("login"));
            usuarioBeans.setClave(request.getParameter("clave"));

            LoginDao daoLogin = new LoginDao();
            PersonaBeans persona;

            MenuDao daoMenu = new MenuDao();

            if (request.getParameter("btnConsultar").equals("Aceptar")) {

                usuarioBeans = daoLogin.validarLogin(usuarioBeans.getLogin(), usuarioBeans.getClave());

                if (usuarioBeans.getMensaje() == null) {
                    PersonaDao daoPersona = new PersonaDao();
                    PersonaBeans pbean = daoPersona.consultar(usuarioBeans.getIdusuario());
                    request.getSession().setAttribute("userName", pbean.getNombres());
                    request.getSession().setAttribute("cedula", usuarioBeans.getLogin());

                    HttpSession sesion = request.getSession();
                    sesion.setAttribute("id_usuario", usuarioBeans.getIdusuario());
                    persona = daoPersona.consultar(usuarioBeans.getIdusuario());
                    sesion.setAttribute("usuario", persona.getNombres());

                    String menuJquery = daoMenu.generarMenuJquery(usuarioBeans.getPerfil());

                    sesion.setAttribute("menu", menuJquery);

                    Logger.getLogger("ACCESO").info("Nuevo Ingreso de " + pbean.getNombres());

                    response.sendRedirect("home.htm");
                } else {
                    Logger.getLogger("ACCESO").info(MessageFormat.format("Fallo al iniciar sesion: \"{0}\" - \"{1}\"", request.getParameter("login"), usuarioBeans.getMensaje()));
                    request.getRequestDispatcher("index.htm").forward(request, response);
                }
            }
        } catch (Exception ex) {
            Logger.getLogger("Error").warn(ex.getMessage());
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {

            String login = request.getParameter("login");
            String clave = request.getParameter("clave");
            JSONObject jsono = new JSONObject();

            if (clave != null) {

                LoginDao daoLogin = new LoginDao();

                UsuarioBeans usuarioBeans = daoLogin.validarLogin(login, clave);


                if (usuarioBeans.getMensaje() == null) {

                    PersonaDao daoPersona = new PersonaDao();
                    HttpSession sesion = request.getSession();
                    sesion.setAttribute("id_usuario", usuarioBeans.getIdusuario());
                    PersonaBeans persona = daoPersona.consultar(usuarioBeans.getIdusuario());
                    sesion.setAttribute("usuario", persona.getNombres());

                    jsono.put("confirma", true);
                    jsono.put("usuario", persona.getNombres());
                    jsono.put("idusuario", usuarioBeans.getIdusuario());
                } else {
                    jsono.put("mensaje", usuarioBeans.getMensaje());
                }


            } else {
                PersonaBeans persona = new PersonaDao().consultarxIdenditifcacion(login);
                if (persona.getIdpersona() != 0) {
                    jsono.put("existe", true);
                    jsono.put("nombre", persona.getNombres());
                } else {
                    jsono.put("existe", false);
                    jsono.put("nombre", "Usuario no Existe");
                }
            }
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            response.getWriter().print(jsono.toString());
        } catch (Exception ex) {
            Logger.getLogger("Error").warn(ex.getMessage());
        }
    }
}
