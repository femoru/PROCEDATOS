/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.co.sio.java.servlet;

import com.co.sio.java.dao.PersonaDao;
import com.co.sio.java.dao.RegistrosDao;
import com.co.sio.java.mbeans.PersonaBeans;
import com.co.sio.java.mbeans.RegistroBeans;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author jcarvajal
 */
@WebServlet(name = "CalculoImagenesServlet", urlPatterns = {"/CalculoImagenesServlet"})
public class CalculoImagenesServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RegistrosDao registrodao = new RegistrosDao();

        try {
            String identificacion = request.getParameter("login");
            PersonaBeans persona = new PersonaDao().consultarxIdenditifcacion(identificacion);

            int idpersona = persona.getIdpersona();
            RegistroBeans registroBeans = registrodao.consultar(idpersona);

            String respuesta = "";
            if (registroBeans.getIdregistro() != 0 && registroBeans.getLabor().getTipolabor() == 4) {
                if (registrodao.imagenesProcesadas(registroBeans)) {
                    respuesta = Integer.toString(registrodao.consultarImagenes(registroBeans.getIdregistro()));
                } else {
                    respuesta = "-1";
                }
            } else {
                respuesta = "-0";
            }
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Cache-Control", "no-cache,must-revalidate");
            response.setHeader("Pragma", "no-cache");

            response.getWriter().print(respuesta);
            response.getWriter().close();


        } catch (Exception ex) {
            Logger.getLogger(RegistrosServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
