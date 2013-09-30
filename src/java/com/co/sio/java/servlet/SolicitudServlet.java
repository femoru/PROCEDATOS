/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.co.sio.java.servlet;

import com.co.sio.java.dao.GruposDao;
import com.co.sio.java.dao.SolicitudesDao;
import com.co.sio.java.dao.UsuarioDao;
import com.co.sio.java.mbeans.GrupoBeans;
import com.co.sio.java.mbeans.ReferenciasBeans;
import com.co.sio.java.mbeans.SolicitudBeans;
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
@WebServlet(name = "SolicitudServlet", urlPatterns = {"/SolicitudServlet"})
public class SolicitudServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int intpage = new Integer(request.getParameter("page"));
            int limit = new Integer(request.getParameter("rows"));
            int idusuario = (Integer) request.getSession().getAttribute("id_usuario");

            String sidx = request.getParameter("sidx");
            String sord = request.getParameter("sord");

            String search = request.getParameter("_search");
            String inicial = request.getParameter("inicial");
            String fFinal = request.getParameter("fFinal");
            int estado = Integer.parseInt(request.getParameter("estado"));
            int opcion = Integer.parseInt(request.getParameter("opcion"));
            int filtro = Integer.parseInt(request.getParameter("filtro"));

            String json = "";
            SolicitudesDao solicitudesDao;
            solicitudesDao = new SolicitudesDao();

            if (search.equals("false")) {
                json = solicitudesDao.getListSolicitudes(intpage, limit, sidx, sord, idusuario, inicial, fFinal, opcion, estado, filtro);
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
            Logger.getLogger(SolicitudServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String oper = request.getParameter("oper");
            int idusuario = (Integer) request.getSession().getAttribute("id_usuario");


            SolicitudesDao solicitudesDao = new SolicitudesDao();
            SolicitudBeans solicitudBeans = new SolicitudBeans();

            switch (oper.charAt(0)) {

                case 'p':
                    int perfil = new UsuarioDao().consultar(idusuario).getPerfil().getCod_perfil();

                    response.getWriter().print(perfil);


                    break;



                case 'a':
                    int solicitud1 = Integer.parseInt(request.getParameter("tipo"));
                    ReferenciasBeans referenciasBeans = new ReferenciasBeans(); //ReferenciasDao().consultar(solicitud1, "RSOLICITUDES");
                    referenciasBeans.setCodigo(solicitud1);
                    UsuarioBeans user = new UsuarioDao().consultar(idusuario);
                    int idgrupo = Integer.parseInt(request.getParameter("grupo"));
                    GrupoBeans grupo = new GruposDao().consultar(idgrupo);
                    String observacion = request.getParameter("descripcion");

                    solicitudBeans.setUsuarioSolicitud(user);
                    solicitudBeans.setSolicitud(referenciasBeans);
                    solicitudBeans.setGrupo(grupo);
                    solicitudBeans.setObservacion(observacion);

                    response.getWriter().print(solicitudesDao.Guardar(solicitudBeans));
                    break;

                case 'e':
                    int idsolicitud2 = Integer.parseInt(request.getParameter("id"));
                    solicitudBeans = solicitudesDao.consultar(idsolicitud2);
                    solicitudBeans.setUsuarioRespuesta(new UsuarioDao().consultar(idusuario));
                    solicitudBeans.setRespuesta(request.getParameter("respuesta"));

                    response.getWriter().print(solicitudesDao.actualizar(solicitudBeans));

                    break;
                case 'd':
                    int idsolicitud3 = Integer.parseInt(request.getParameter("id"));
                    solicitudBeans = solicitudesDao.consultar(idsolicitud3);

                    response.getWriter().print(solicitudesDao.anular(solicitudBeans));

            }
        } catch (Exception ex) {
            Logger.getLogger(SolicitudServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
