<%-- 
    Document   : getEstadoCivil
    Created on : 29-ene-2013, 8:18:07
    Author     : fmoctezuma
--%>

<%@page import="com.co.sio.java.mbeans.ReferenciasBeans"%>
<%@page import="com.co.sio.java.dao.ReferenciasDao"%>
<%@page import="java.util.List"%>

<%

    response.setContentType("text/html;charset=UTF-8");

    ReferenciasDao referenciasDao=new ReferenciasDao();
   
    List<ReferenciasBeans> list = referenciasDao.listar("RESTADOSCIVILES");

    response.getWriter().printf("<option value=\""+"0"+"\">"+"Estado Civil..."+"</option>");
    for (ReferenciasBeans referenciasBeans: list) {
        response.getWriter().printf("<option value=\""+referenciasBeans.getCodigo()+"\">"+referenciasBeans.getDescripcion()+"</option>");
    }
    
    
    out.close();
%>