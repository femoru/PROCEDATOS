<%-- 
    Document   : getSitioTrabajo
    Created on : 28/08/2013, 11:41:47 AM
    Author     : fmoctezuma
--%>

<%@page import="com.co.sio.java.mbeans.ReferenciasBeans"%>
<%@page import="com.co.sio.java.dao.ReferenciasDao"%>
<%@page import="java.util.List"%>

<%

    response.setContentType("text/html;charset=UTF-8");

    ReferenciasDao referenciasDao = new ReferenciasDao();

    List<ReferenciasBeans> list = referenciasDao.listar("RSITIOTRABAJO");

    boolean grid = Boolean.parseBoolean(request.getParameter("grid"));

    if (grid) {
        response.getWriter().printf("<select>");
    }

    for (ReferenciasBeans referenciasBeans : list) {
        response.getWriter().printf("<option value=\"" + referenciasBeans.getCodigo() + "\">" + referenciasBeans.getDescripcion() + "</option>");
    }
    if (grid) {
        response.getWriter().printf("</select>");
    }
    //

    out.close();
%>