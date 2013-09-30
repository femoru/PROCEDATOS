/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.co.sio.java.servlet;

import com.co.sio.java.JSON.JSONObject;
import com.co.sio.java.dao.RefNovedadDao;
import com.co.sio.java.mbeans.RefNovedadBeans;
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
@WebServlet(name = "RefNovedadServlet", urlPatterns = {"/RefNovedadServlet"})
public class RefNovedadServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int intpage = new Integer(request.getParameter("page"));
            int limit = new Integer(request.getParameter("rows"));

            String json;
            RefNovedadDao novedadesDao;
            novedadesDao = new RefNovedadDao();

            json = novedadesDao.getListRefNovedades(intpage, limit);

            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Cache-Control", "no-cache,must-revalidate");
            response.setHeader("Pragma", "no-cache");

            response.getWriter().print(json);
            response.getWriter().close();
        } catch (Exception ex) {
            Logger.getLogger(RefNovedadServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String oper = request.getParameter("oper");
            RefNovedadBeans beans = new RefNovedadBeans();
            if (oper.charAt(0) != 'c') {
                beans.setDesNovedad(request.getParameter("descripcion"));
                beans.setTipoNovedad(Integer.parseInt(request.getParameter("tipo")));
                beans.setAfectaBasico(Integer.parseInt(request.getParameter("basico")));
                beans.setAfectaAuxilio(Integer.parseInt(request.getParameter("auxilio")));
                beans.setPagar(Integer.parseInt(request.getParameter("pagar")));
                beans.setAfectaNeto(Integer.parseInt(request.getParameter("neto")));
                beans.setAplicaAseguradora(Integer.parseInt(request.getParameter("aseguradora")));
            }
            RefNovedadDao dao = new RefNovedadDao();
            switch (oper.charAt(0)) {
                case 'a':
                    dao.Guardar(beans);
                    break;
                case 'e':
                    beans.setCodNovedad(Integer.parseInt(request.getParameter("id")));
                    dao.actualizar(beans);
                    break;
                case 'c': {
                    beans = dao.consultar(Integer.parseInt(request.getParameter("id")));

                    JSONObject jo = new JSONObject();
                    JSONObject je = new JSONObject();
                    je.put("id", beans.getCodNovedad());
                    je.put("descripcion", beans.getDesNovedad());
                    je.put("tipo", beans.getTipoNovedad());
                    je.put("basico", beans.getAfectaBasico());
                    je.put("auxilio", beans.getAfectaAuxilio());
                    je.put("pagar", beans.getPagar());
                    je.put("neto", beans.getAfectaNeto());
                    je.put("aseguradora", beans.getAplicaAseguradora());
                    jo.put("novedad", je);



                    String json = jo.toString();
                    response.setContentType("application/json");
                    response.setCharacterEncoding("utf-8");
                    response.setHeader("Pragma", "no-cache");
                    response.setHeader("Cache-Control", "no-cache,must-revalidate");
                    response.setHeader("Pragma", "no-cache");

                    response.getWriter().print(json);
                    response.getWriter().close();
                }
                break;
            }

        } catch (Exception ex) {
            Logger.getLogger(RefNovedadServlet.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
