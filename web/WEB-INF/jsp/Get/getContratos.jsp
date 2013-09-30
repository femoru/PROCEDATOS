<%-- 
    Document   : getContratos
    Created on : 25-abr-2013, 11:10:44
    Author     : fmoctezuma
--%>
<%@page import="java.util.List"%>
<%@page import="com.co.sio.java.mbeans.ContratoBeans"%>
<%@page import="com.co.sio.java.dao.ContratosDao"%>
<%

    response.setContentType("text/html;charset=UTF-8");

    ContratosDao contratosDao = new ContratosDao();

    List<ContratoBeans> list = contratosDao.listar();


    //response.getWriter().printf("<select>");
    response.getWriter().printf("<option value=\"" + "0" + "\">" + "Seleccione Cliente..." + "</option>");
    for (ContratoBeans contrato : list) {
        response.getWriter().printf("<option value=\"" + contrato.getIdcontrato() + "\">" + contrato.getCliente().getNomcliente()+" - "+contrato.getNumcontrato() + "</option>");
    }

    // 

    out.close();
%>
