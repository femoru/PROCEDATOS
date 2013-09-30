<%-- 
    Document   : getGruposAreas
    Created on : 19-feb-2013, 15:58:34
    Author     : fmoctezuma
--%>

<%@page import="com.co.sio.java.dao.GruposDao"%>
<%@page import="com.co.sio.java.mbeans.GrupoBeans"%>
<%@page import="java.util.List"%>

<%

    response.setContentType("text/html;charset=UTF-8");

    GruposDao gruposDao = new GruposDao();
    String area = "";
    boolean grid = false;
    if ((area = request.getParameter("idarea")) == null) {
        area = request.getParameter("area");
        grid = true;
    }
    List<GrupoBeans> list = gruposDao.listar(area);


    if (grid) {
       
    }
    response.getWriter().printf("<option value=\"" + "0" + "\">" + "Seleccione Grupo..." + "</option>");
    for (GrupoBeans grupoBeans : list) {

        response.getWriter().printf("<option value=\"" + grupoBeans.getIdgrupo() + "\">" + grupoBeans.getDesgrupo() + "</option>");
    }
    if (grid) {
        
    }
    out.close();
%>
