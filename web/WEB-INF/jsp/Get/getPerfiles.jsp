<%-- 
    Document   : getPerfiles
    Created on : 16-ene-2013, 15:12:03
    Author     : fmoctezuma
--%>

<%@page import="java.util.List"%>
<%@page import="com.co.sio.java.mbeans.PerfilBeans" %>
<%@page import="com.co.sio.java.dao.PerfilDao" %>
<%

    response.setContentType("text/html;charset=UTF-8");

    PerfilDao daoperfil = new PerfilDao();
    List<PerfilBeans> list = daoperfil.listar();


    response.getWriter().printf("<select>");
    response.getWriter().printf("<option value=\""+"0"+"\">"+"Seleccione Perfil..."+"</option>");
    for (PerfilBeans perfil : list) {
        response.getWriter().printf("<option value=\""+perfil.getCod_perfil()+"\">"+perfil.getDes_perfil()+"</option>");
    }
    
    /*
    response.getWriter().printf("<option value=\"%1s\">%2s</option>", 0, "Seleccione Item");
    for (PerfilBeans perfil : list) {
        response.getWriter().printf("<option value=\"%1s\">%2s</option>", perfil.getCod_perfil(), perfil.getDes_perfil());
    }
    */
    response.getWriter().printf("</select>");
    
    out.close();
%>