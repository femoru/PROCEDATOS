<%-- 
    Document   : getSolicitudes
    Created on : 28-feb-2013, 14:55:30
    Author     : fmoctezuma
--%>

<%@page import="com.co.sio.java.mbeans.ReferenciasBeans"%>
<%@page import="com.co.sio.java.dao.ReferenciasDao"%>
<%@page import="java.util.List"%>

<%

    response.setContentType("text/html;charset=UTF-8");

    ReferenciasDao referenciasDao=new ReferenciasDao();
   
    List<ReferenciasBeans> list = referenciasDao.listar("RSOLICITUDES");


   
    response.getWriter().printf("<option value=\""+"0"+"\">"+"Seleccione Solicitud..."+"</option>");
    for (ReferenciasBeans referenciasBeans: list) {
        response.getWriter().printf("<option value=\""+referenciasBeans.getCodigo()+"\">"+referenciasBeans.getDescripcion()+"</option>");
    }
    
    
    
    out.close();
%>