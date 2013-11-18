package com.co.sio.java.servlet;

import com.co.sio.java.dao.PersonaDao;
import com.co.sio.java.dao.RegistrosDao;
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
@WebServlet(name = "ProduccionServlet", urlPatterns = {"/ProduccionServlet"})
public class ProduccionServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int intpage = new Integer(request.getParameter("page"));
            int limit = new Integer(request.getParameter("rows"));
            int idusuario = (Integer) request.getSession().getAttribute("id_usuario");

            String sidx = request.getParameter("sidx");
            String sord = request.getParameter("sord");

            String identificacion = request.getParameter("identificacion");
            String inicial = request.getParameter("inicial");
            String fFinal = request.getParameter("fFinal");
            String nomina = request.getParameter("nomina");
            int estado = Integer.parseInt(request.getParameter("estado"));

            RegistrosDao RegistrosDao;
            RegistrosDao = new RegistrosDao();
            if (!identificacion.equals("")) {
                idusuario = new PersonaDao().consultarxIdenditifcacion(identificacion).getIdpersona();
            }

            String json = RegistrosDao.getListProduccion(intpage, limit, idusuario, inicial, fFinal, estado, nomina);

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
}
