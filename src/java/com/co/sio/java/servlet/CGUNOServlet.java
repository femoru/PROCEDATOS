/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.co.sio.java.servlet;

import com.co.sio.java.JSON.JSONObject;
import com.co.sio.java.dao.CGUNODao;
import com.co.sio.java.dao.PrenominaDao;
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
@WebServlet(name = "CGUNOServlet", urlPatterns = {"/CGUNOServlet"})
public class CGUNOServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            CGUNODao cGUNODao = new CGUNODao();

            String idnomina = request.getParameter("nomina");

            String fileSalida = cGUNODao.cargarHorasExtras();


            String[] salida = fileSalida.split("\n");
            (new PrenominaDao()).cerrarRegistrosNomina(Integer.parseInt(idnomina));

            response.setContentType("application/txt");
            response.setHeader("Content-Disposition", "attachment; filename=" + "NMBATCHNMBATCH.txt" + "");
            for (String nov : salida) {
                response.getOutputStream().println(nov);
            }

        } catch (Exception ex) {
            Logger.getLogger(CGUNOServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {


        try {
            String idnomina = request.getParameter("nomina");
            JSONObject jsono = new JSONObject();
            jsono.put("file", "CGUNOServlet?nomina=" + idnomina);
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            response.getWriter().println(jsono.toString());
        } catch (Exception ex) {
            Logger.getLogger(CGUNOServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
