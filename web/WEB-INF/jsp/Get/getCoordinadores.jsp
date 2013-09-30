<%-- 
    Document   : getCordinadores
    Created on : 07-feb-2013, 15:21:18
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
    List<UsuarioBeans> list = usuarioDao.listarPorPerfil(5);

    response.getWriter().printf("<select>");

    response.getWriter().printf("<option value=\"" + "0" + "\">" + "Seleccione un Coordinador" + "</option>");
    for (UsuarioBeans cordinadores : list) {
        cordina = personaDao.consultar(cordinadores.getIdusuario());
        response.getWriter().printf("<option value=\"" + cordinadores.getIdusuario() + "\">" + cordina.getpNombre() + " " + cordina.getpApellido() + "</option>");
    }

    response.getWriter().printf("</select>");

    out.close();
%>
