/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.co.sio.java.servlet;

import com.co.sio.java.JSON.JSONObject;
import com.co.sio.java.dao.NominaDao;
import com.co.sio.java.dao.PrenominaDao;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
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
@WebServlet(name = "NominaServlet", urlPatterns = {"/NominaServlet"})
public class NominaServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int pages = Integer.parseInt(request.getParameter("page"));
            int rows = Integer.parseInt(request.getParameter("rows"));

            NominaDao nominaDao = new NominaDao();

            JSONObject jsono = nominaDao.getListNomina(pages, rows);

            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Cache-Control", "no-cache,must-revalidate");
            response.getWriter().print(jsono.toString());
            response.getWriter().close();
        } catch (Exception ex) {
            Logger.getLogger(NominaServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String oper = request.getParameter("oper");

            switch (oper.charAt(0)) {
                case 'e': {

                    String idnomina = request.getParameter("idnomina");
                    PrenominaDao preDao = new PrenominaDao();
                  //  preDao.terminarModificaciones("0", idnomina);

                    // JSONArray datosNomina = preDao.datosNomina();

                    // String cdl = CDL.toString(datosNomina);

                    String rango = request.getParameter("rango").replace("/", "_").replace(" - ", "__");
                    
                    OutputStream out = response.getOutputStream();
                    File archivo = preDao.datosNomina();
                    FileInputStream in = new FileInputStream(archivo);
                    byte[] buffer = new byte[4096];
                    int length;
                    response.setHeader("Content-Type", "application/vnd.ms-excel");
                    response.setHeader("Content-Disposition", "attachment; filename=" + rango + archivo.getName() + "");

                    while ((length = in.read(buffer)) > 0) {
                        out.write(buffer, 0, length);
                    }
                    in.close();
                    out.flush();
                }
                break;
            }
        } catch (Exception ex) {
            Logger.getLogger(NominaServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
