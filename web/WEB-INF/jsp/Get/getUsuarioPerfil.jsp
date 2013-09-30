<%-- 
    Document   : getusuarioperfil
    Created on : 12-mar-2013, 10:33:39
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
    PersonaDao personaDao =new PersonaDao();
    PersonaBeans cordina;
    int idperfil =Integer.parseInt(request.getParameter("perfil"));
    List<UsuarioBeans> list = usuarioDao.listarPorPerfil(idperfil);


    response.getWriter().printf("<option value=\""+"0"+"\">"+"Seleccione un Auxiliar"+"</option>");
    for (UsuarioBeans usuario : list) {
        cordina=personaDao.consultar(usuario.getIdusuario()); 
        response.getWriter().printf("<option value=\""+usuario.getIdusuario()+"\">" +cordina.getNombres()+"</option>");
    }
  
    out.close();
%>