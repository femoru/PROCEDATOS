<%-- 
    Document   : getClientes
    Created on : 22-feb-2013, 17:05:31
    Author     : fmoctezuma
--%>

<%@page import="com.co.sio.java.mbeans.ClienteBeans"%>
<%@page import="com.co.sio.java.dao.ClientesDao"%>
<%@page import="java.util.List"%>

<%

    response.setContentType("text/html;charset=UTF-8");

    ClientesDao clientesDao = new ClientesDao();

    List<ClienteBeans> list = clientesDao.listar();


    //response.getWriter().printf("<select>");
    response.getWriter().printf("<option value=\"" + "0" + "\">" + "Seleccione Cliente..." + "</option>");
    for (ClienteBeans clienteBean : list) {
        response.getWriter().printf("<option value=\"" + clienteBean.getIdcliente() + "\">" + clienteBean.getNomcliente() + "</option>");
    }

    // 

    out.close();
%>