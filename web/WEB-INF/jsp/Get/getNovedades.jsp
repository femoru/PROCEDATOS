<%-- 
    Document   : getNovedades
    Created on : 11-abr-2013, 16:22:19
    Author     : fmoctezuma
--%>

<%@page import="com.co.sio.java.mbeans.ReferenciasBeans"%>
<%@page import="com.co.sio.java.dao.ReferenciasDao"%>
<%@page import="java.util.List"%>

<%

    response.setContentType("text/html;charset=UTF-8");

    ReferenciasDao referenciasDao = new ReferenciasDao();
    boolean grid = false;
    List<ReferenciasBeans> list = referenciasDao.listar("RNOVEDADES");

    if (grid) {
       
    }
    response.getWriter().printf("<option value=\"" + "0" + "\">" + "Seleccione Novedad..." + "</option>");
    for (ReferenciasBeans referenciasBeans : list) {
        response.getWriter().printf("<option value=\"" + referenciasBeans.getCodigo() + "\">" + referenciasBeans.getDescripcion() + "</option>");
    }
    if (grid) {

        
    }
    out.close();
%>

