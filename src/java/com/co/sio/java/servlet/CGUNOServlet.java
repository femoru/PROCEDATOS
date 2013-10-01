/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.co.sio.java.servlet;

import com.co.sio.java.JSON.JSONObject;
import com.co.sio.java.dao.CGUNODao;
import com.co.sio.java.dao.PrenominaDao;
import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
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

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ZipOutputStream out = new ZipOutputStream(baos);
            byte[] buf = new byte[2048];

            String idnomina = request.getParameter("nomina");
//-----------------------------------------------------------------
            String fileSalida = cGUNODao.cargarHorasExtras();

            String[] salida = fileSalida.split("\n");

            File file = new File("NMBATCHNMBATCH.txt");

            BufferedWriter writer = new BufferedWriter(new FileWriter(file));

            for (int i = 0; i < salida.length; i++) {
                writer.write(salida[i]);
                writer.newLine();
            }
            writer.close();

//-----------------------------------------------------------------
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file.getName()));
            out.putNextEntry(new ZipEntry(file.getName()));
            int bytesRead;
            while ((bytesRead = bis.read(buf)) != -1) {
                out.write(buf, 0, bytesRead);
            }
            out.closeEntry();
            bis.close();
            
            file.delete();
//-----------------------------------------------------------------
            fileSalida = cGUNODao.cargarNovedades(Integer.parseInt(idnomina));

            salida = fileSalida.split("\n");

            file = new File("NMTNLTRM.txt");


            writer = new BufferedWriter(new FileWriter(file));

            for (int i = 0; i < salida.length; i++) {
                writer.write(salida[i]);
                writer.newLine();
            }
            writer.close();

//-----------------------------------------------------------------
            bis = new BufferedInputStream(new FileInputStream(file.getName()));
            out.putNextEntry(new ZipEntry(file.getName()));

            while ((bytesRead = bis.read(buf)) != -1) {
                out.write(buf, 0, bytesRead);
            }
            out.closeEntry();
            bis.close();
            
            file.delete();
//-----------------------------------------------------------------
            out.flush();
            baos.flush();
            out.close();
            baos.close();

////            (new PrenominaDao()).cerrarRegistrosNomina(Integer.parseInt(idnomina));

            response.setContentType("application/zip");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + "NOMINA.ZIP" + "\"");
            response.getOutputStream().write(baos.toByteArray());

        } catch (Exception ex) {
            Logger.getLogger(CGUNOServlet.class.getName()).log(Level.SEVERE, null, ex);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
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
