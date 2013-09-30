<%-- 
    Document   : getContratos
    Created on : 25-abr-2013, 11:10:44
    Author     : fmoctezuma
--%>
<%@page import="com.co.sio.java.dao.ReferenciasDao"%>
<%@page import="com.co.sio.java.mbeans.ReferenciasBeans"%>
<%@page import="com.co.sio.java.mbeans.LaborBeans"%>
<%@page import="com.co.sio.java.dao.LaboresDao"%>
<%@page import="java.util.List"%>

<%

    response.setContentType("text/html;charset=UTF-8");

    LaboresDao laboresDao = new LaboresDao();
    int idcontrato = Integer.parseInt(request.getParameter("idcontrato"));
    List<LaborBeans> list = laboresDao.listar(idcontrato);

    ReferenciasDao referencias = new ReferenciasDao();
    ReferenciasBeans beans = new ReferenciasBeans();
    //response.getWriter().printf("<select>");
    response.getWriter().printf("<option value=\"" + "0" + "\">" + "Seleccione Labor..." + "</option>");
    for (LaborBeans labor : list) {
        String nombrelabor = "";
        if ((beans = referencias.consultar(labor.getLabor(), "RLABORES")) != null) {
            nombrelabor = beans.getDescripcion();
        }
        String tipolabor = "";
        if ((beans = referencias.consultar(labor.getTipolabor(), "RTIPOLABOR")) != null) {
            tipolabor = beans.getDescripcion();
        }
        String extralabor = "";
        if ((beans = referencias.consultar(labor.getHoraextra(), "RHORASEXTRAS")) != null) {
            extralabor = beans.getDescripcion();
        }

        response.getWriter().printf("<option value=\"" + labor.getIdlaborcontrato() + "\">" + nombrelabor
                + " " + tipolabor + " " + extralabor + "</option>");
    }

    // 

    out.close();
%>
