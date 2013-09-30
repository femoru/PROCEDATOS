<%-- 
    Document   : getAreasClientes
    Created on : 19-feb-2013, 15:58:10
    Author     : fmoctezuma
--%>

<%@page import="com.co.sio.java.mbeans.AreaBeans"%>
<%@page import="com.co.sio.java.dao.AreasDao"%>
<%@page import="com.co.sio.java.mbeans.ReferenciasBeans"%>
<%@page import="com.co.sio.java.dao.ReferenciasDao"%>
<%@page import="java.util.List"%>

<%

    response.setContentType("text/html;charset=UTF-8");

    AreasDao areasDao = new AreasDao();
    ReferenciasDao referenciasDao = new ReferenciasDao();
    ReferenciasBeans referenciasBeans;
    String cliente = "";
    boolean grid = false;
    if ((cliente = request.getParameter("idcliente")) == null) {
        cliente = request.getParameter("cliente");
        grid = true;
    }

    List<AreaBeans> list = areasDao.listar(cliente);


    if (grid) {
       
    }
    response.getWriter().printf("<option value=\"" + "0" + "\">" + "Seleccione Area..." + "</option>");
    for (AreaBeans areaBean : list) {
        referenciasBeans = referenciasDao.consultar(areaBean.getArea(), "RAREAS");
        response.getWriter().printf("<option value=\"" + areaBean.getId() + "\">" + referenciasBeans.getDescripcion() + "</option>");
    }
    
    if (grid) {
        
    }
    out.close();
%>
