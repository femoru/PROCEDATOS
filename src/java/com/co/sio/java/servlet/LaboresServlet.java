/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.co.sio.java.servlet;

import com.co.sio.java.JSON.JSONObject;
import com.co.sio.java.dao.LaboresDao;
import com.co.sio.java.mbeans.ContratoBeans;
import com.co.sio.java.mbeans.GrupoBeans;
import com.co.sio.java.mbeans.LaborBeans;
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
@WebServlet(name = "LaboresServlet", urlPatterns = {"/LaboresServlet"})
public class LaboresServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int intpage = new Integer(request.getParameter("page"));
            int limit = new Integer(request.getParameter("rows"));
            int idcontrato = new Integer(request.getParameter("contrato"));

            String sidx = request.getParameter("sidx");
            String sord = request.getParameter("sord");
            String json;
            LaboresDao laboresDao = new LaboresDao();

            json = laboresDao.getListLabores(intpage, limit, sidx, sord, idcontrato);

            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Cache-Control", "no-cache,must-revalidate");
            response.setHeader("Pragma", "no-cache");

            response.getWriter().print(json);
            response.getWriter().close();
        } catch (Exception ex) {
            Logger.getLogger(LaboresServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String oper = request.getParameter("oper");

            LaboresDao laboresDao = new LaboresDao();
            LaborBeans laborBeans = new LaborBeans();

            switch (oper.charAt(0)) {
                case 'i':
                    laborBeans = laboresDao.consultar(Integer.parseInt(request.getParameter("id")));
                    String cambiar = request.getParameter("cambiar");
                    switch (cambiar.charAt(0)) {
                        case 'e'://modifica estado
                            laborBeans.setActivo(Integer.parseInt(request.getParameter("valor")));
                            break;
                        case 'c'://modifica conciliacion
                            laborBeans.setConciliacion(Integer.parseInt(request.getParameter("valor")));
                            break;
                        case 'd'://modifica datolabor
                            laborBeans.setDatolabor(Integer.parseInt(request.getParameter("valor")));
                            break;
                        case 't':
                            laborBeans.setTipolabor(Integer.parseInt(request.getParameter("valor")));
                            break;
                    }
                    laboresDao.Actualizar(laborBeans);
                    break;
                case 'm':
                    laborBeans = laboresDao.consultar(Integer.parseInt(request.getParameter("idlabor")));
                    LaborBeans extra = laborBeans;
                    extra.setIdlaborcontrato(0);
                    extra.setHoraextra(Integer.parseInt(request.getParameter("extra")));
                    extra.setValor(request.getParameter("valor"));
                    extra.setCosto(request.getParameter("costo"));

                    response.getWriter().print(laboresDao.Guardar(extra));
                    break;
                case 'c':
                    laborBeans = laboresDao.consultar(Integer.parseInt(request.getParameter("idlabor")));
                    response.getWriter().print(laboresDao.extrasPendientes(laborBeans));
                    break;
                default:
                    if (oper.equals("editconsulta")) {
                        laborBeans = laboresDao.consultar(Integer.parseInt(request.getParameter("id")));
                        JSONObject jSONObject = new JSONObject(laborBeans);
                        response.getWriter().print(jSONObject.toString());
                        response.setContentType("application/json");
                        break;
                    } else {

                        if (oper.charAt(0) == 'e') {
                            int idlabor = Integer.parseInt(request.getParameter("id"));
                            laborBeans = laboresDao.consultar(idlabor);
                        }

                        ContratoBeans contrato = new ContratoBeans();
                        contrato.setIdcontrato(Integer.parseInt(request.getParameter("contrato")));
                        laborBeans.setContrato(contrato);

                        GrupoBeans grupo = new GrupoBeans();
                        grupo.setIdgrupo(Integer.parseInt(request.getParameter("grupo")));
                        laborBeans.setGrupo(grupo);

                        laborBeans.setLabor(Integer.parseInt(request.getParameter("labor")));
                        laborBeans.setTipolabor(Integer.parseInt(request.getParameter("tipo")));
                        //laborBeans.setHoraextra(Integer.parseInt(request.getParameter("extra") == null ? "0" : request.getParameter("extra")));
                        laborBeans.setValor(request.getParameter("valor"));
                        laborBeans.setCosto(request.getParameter("costo"));

                        laborBeans.setConciliacion(Boolean.parseBoolean(request.getParameter("conciliacion")) ? 1 : 0);
                        laborBeans.setActivo(Integer.parseInt(request.getParameter("estado")));
                        laborBeans.setDatolabor(Integer.parseInt(request.getParameter("datolabor")));
                        switch (oper.charAt(0)) {
                            case 'a':
                                response.getWriter().print(laboresDao.Guardar(laborBeans));
                                break;
                            case 'e':
                                response.getWriter().print(laboresDao.Actualizar(laborBeans));
                                break;
                        }
                    }
                    break;

            }
        } catch (Exception ex) {
            Logger.getLogger(LaboresServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
