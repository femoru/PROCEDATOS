<%-- 
    Document   : getUsuariosSitio
    Created on : 28/08/2013, 05:13:23 PM
    Author     : fmoctezuma
--%>
<%@page import="com.co.sio.java.mbeans.PersonaBeans"%>
<%@page import="com.co.sio.java.dao.PersonaDao"%>
<%@page import="java.util.List"%>
<%@page import="com.co.sio.java.mbeans.UsuarioBeans"%>
<%@page import="com.co.sio.java.dao.UsuarioDao"%>
<%

    response.setContentType("text/html;charset=UTF-8");

    UsuarioDao usuarioDao = new UsuarioDao();
    PersonaDao personaDao = new PersonaDao();
    PersonaBeans cordina;
    int sitio = Integer.parseInt(request.getParameter("sitio"));
    List<UsuarioBeans> list = usuarioDao.listarPorSitio(sitio);


    response.getWriter().printf("<select>");
    response.getWriter().printf("<option value=\"" + "0" + "\">" + "Seleccione un Auxiliar" + "</option>");
    for (UsuarioBeans usuario : list) {
        cordina = personaDao.consultar(usuario.getIdusuario());
        response.getWriter().printf("<option value=\"" + usuario.getIdusuario() + "\">" + cordina.getNombres() + "</option>");
    }

    response.getWriter().printf("</select>");

    out.close();
%>