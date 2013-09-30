<%-- 
    Document   : getNominas
    Created on : 18/09/2013, 08:08:47 AM
    Author     : fmoctezuma
--%>

<%@page import="com.co.sio.java.mbeans.NominaBeans"%>
<%@page import="com.co.sio.java.dao.NominaDao"%>
<%@page import="java.util.List"%>

<%

    response.setContentType("text/html;charset=UTF-8");

    NominaDao nominaDao = new NominaDao();

    List<NominaBeans> list = nominaDao.listar();


    //response.getWriter().printf("<select>");
    response.getWriter().printf("<option value=\"" + "0" + "\">" + "Seleccione Rango Nomina..." + "</option>");
    for (NominaBeans nominaBeans : list) {
        response.getWriter().printf("<option value=\"" + nominaBeans.getId() + "\">" + nominaBeans.getFechaInicio() + " --> " + nominaBeans.getFechaFin() + "</option>");
    }

    // 

    out.close();
%>