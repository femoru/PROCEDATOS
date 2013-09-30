<%-- 
    Document   : getAuxiliaresCoordinador
    Created on : 03-may-2013, 11:05:41
    Author     : fmoctezuma
--%>

<%@page import="com.co.sio.java.mbeans.PersonaBeans"%>
<%@page import="com.co.sio.java.dao.PersonaDao"%>
<%@page import="java.util.List"%>

<%

    response.setContentType("text/html;charset=UTF-8"); 

    PersonaDao personaDao = new PersonaDao();
    int idusuario = (Integer)request.getSession().getAttribute("id_usuario");
   
    List<PersonaBeans> list = personaDao.listarAuxiliaresPorCoordinador(idusuario);

    
    //response.getWriter().println("<select>");
    
    response.getWriter().println("<option value=\""+"-1"+"\">"+"Seleccione un Auxiliar..."+"</option>");
    for (PersonaBeans personaBeans: list) {
        response.getWriter().println("<option value=\""+personaBeans.getIdpersona()+"\">"+personaBeans.getNombres()+"</option>");
    }
    
    //
    
%>