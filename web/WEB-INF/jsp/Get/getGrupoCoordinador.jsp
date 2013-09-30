<%-- 
    Document   : getgrupocoordinador
    Created on : 11-mar-2013, 17:40:07
    Author     : fmoctezuma
--%>

<%@page import="com.co.sio.java.mbeans.AreaBeans"%>
<%@page import="com.co.sio.java.dao.ReferenciasDao"%>
<%@page import="com.co.sio.java.mbeans.ReferenciasBeans"%>
<%@page import="com.co.sio.java.dao.AreasDao"%>
<%@page import="com.co.sio.java.dao.GruposDao"%>
<%@page import="com.co.sio.java.mbeans.GrupoBeans"%>
<%@page import="java.util.List"%>

<%

    response.setContentType("text/html;charset=UTF-8");

    GruposDao gruposDao = new GruposDao();
    AreasDao areasDao = new AreasDao();
    ReferenciasDao referenciasDao = new ReferenciasDao();
    AreaBeans areaBeans = new AreaBeans();
    int coordinador = 0;
    boolean grid = false;
    if ((coordinador = (Integer) request.getSession().getAttribute("id_usuario")) == 0) {
        coordinador = Integer.parseInt(request.getParameter("coordinador"));
        grid = true;
    }
    List<GrupoBeans> list = gruposDao.listar(coordinador);


    if (grid) {
       
    }
    ReferenciasBeans area;
    response.getWriter().printf("<option value=\"" + "0" + "\">" + "Seleccione grupo" + "</option>");
    for (GrupoBeans grupoBeans : list) {
        areaBeans = areasDao.consultar(grupoBeans.getIdarea());
        area = referenciasDao.consultar(areaBeans.getArea(), "RAREAS");
        response.getWriter().printf("<option value=\"" + grupoBeans.getIdgrupo() + "\">" + areaBeans.getCliente().getNomcliente()
                + " - " + area.getDescripcion()
                + " - " + grupoBeans.getDesgrupo() + "</option>");
    }
    if (grid) {
        
    }
    out.close();
%>
