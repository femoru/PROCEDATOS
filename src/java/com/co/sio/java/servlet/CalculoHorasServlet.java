/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.co.sio.java.servlet;

import com.co.sio.java.dao.PersonaDao;
import com.co.sio.java.dao.RegistrosDao;
import com.co.sio.java.mbeans.PersonaBeans;
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
 * @author jcarvajal
 */
@WebServlet(name = "CalculoHorasServlet", urlPatterns = {"/CalculoHorasServlet"})
public class CalculoHorasServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RegistrosDao registrodao = new RegistrosDao();

        try {
            String identificacion = request.getParameter("login");
            PersonaBeans persona = new PersonaDao().consultarxIdenditifcacion(identificacion);

            int idpersona = persona.getIdpersona();
            
            //cambiar por consulta id de registro
            int idRegistro = registrodao.consultarUltimoID(idpersona);

            String respuesta = "";
               if (registrodao.horasLaboradas(idRegistro)) {
                    respuesta = Integer.toString(registrodao.consultarHorasLaboradas(idRegistro));
                } else {
                    respuesta = "-1";
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
