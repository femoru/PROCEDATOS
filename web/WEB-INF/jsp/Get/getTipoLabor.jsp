<%-- 
    Document   : getAreas
    Created on : 08-feb-2013, 10:18:41
    Author     : fmoctezuma
--%>

<%@page import="com.co.sio.java.mbeans.ReferenciasBeans"%>
<%@page import="com.co.sio.java.dao.ReferenciasDao"%>
<%@page import="java.util.List"%>

<%

    response.setContentType("text/html;charset=UTF-8");

    ReferenciasDao referenciasDao=new ReferenciasDao();
   
    List<ReferenciasBeans> list = referenciasDao.listar("RTIPOLABOR");


    //response.getWriter().printf("<select>");
    response.getWriter().printf("<option value=\""+"0"+"\">"+"Seleccione Tipo..."+"</option>");
    for (ReferenciasBeans referenciasBeans: list) {
        response.getWriter().printf("<option value=\""+referenciasBeans.getCodigo()+"\">"+referenciasBeans.getDescripcion()+"</option>");
    }
    
    //
    
    out.close();
%>
