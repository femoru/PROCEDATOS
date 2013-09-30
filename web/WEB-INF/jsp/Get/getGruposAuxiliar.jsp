<%-- 
    Document   : getGruposAuxiliar
    Created on : 26-mar-2013, 9:24:57
    Author     : fmoctezuma
--%>

<%@page import="com.sun.org.apache.bcel.internal.generic.AALOAD"%>
<%@page import="com.co.sio.java.dao.PersonaDao"%>
<%@page import="com.co.sio.java.mbeans.PersonaBeans"%>
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
    PersonaDao personaDao = new PersonaDao();
    int auxiliar = 0;
    boolean grid = true;
    if ((auxiliar = (Integer) request.getSession().getAttribute("id_usuario")) == 0) {
        auxiliar = Integer.parseInt(request.getParameter("auxiliar"));
        grid = false;
    }
    List<GrupoBeans> list = gruposDao.listarCoordinadoresAuxiliar(auxiliar);
    

    if (grid) {
       
    }
    ReferenciasBeans area;
    PersonaBeans personaBeans;
    response.getWriter().printf("<option value=\"" + "0" + "\">" + "Seleccione coordinador" + "</option>");
    for (GrupoBeans grupoBeans : list) {
        areaBeans = areasDao.consultar(grupoBeans.getIdarea());
        area = referenciasDao.consultar(areaBeans.getArea(), "RAREAS");
        personaBeans = personaDao.consultar(grupoBeans.getCordinador().getIdusuario());
        response.getWriter().printf("<option value=\"" + grupoBeans.getIdgrupo() + "\">" 
                + area.getDescripcion()
                + " - " + grupoBeans.getDesgrupo() 
                + " - " + personaBeans.getpNombre()+" "+personaBeans.getpApellido()+"</option>");
    }
    if (grid) {
        
    }
    out.close();
%>
