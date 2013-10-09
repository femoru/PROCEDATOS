<%-- 
    Document   : getLaboresUsuarios
    Created on : 27-feb-2013, 10:05:57
    Author     : fmoctezuma
--%>

<%@page import="com.co.sio.java.dao.UsuarioDao"%>
<%@page import="com.co.sio.java.mbeans.UsuarioBeans"%>
<%@page import="java.util.List"%>
<%@page import="com.co.sio.java.mbeans.UsuarioLaboresBeans"%>
<%@page import="com.co.sio.java.mbeans.ReferenciasBeans"%>
<%@page import="com.co.sio.java.dao.ReferenciasDao"%>
<%@page import="com.co.sio.java.dao.UsuarioLaboresDao"%>
<%

    response.setContentType("text/html;charset=UTF-8");

    UsuarioLaboresDao usuarioLaboresDao = new UsuarioLaboresDao();
    ReferenciasDao referenciasDao = new ReferenciasDao();

    String usuario = "", option = "", grid;

    grid = request.getParameter("grid");
    if ((usuario = request.getParameter("idusuario")) == null) {
        usuario = request.getParameter("usuario");

        if (usuario == null) {
            usuario = Integer.toString((Integer) request.getSession().getAttribute("id_usuario"));
        }
    }
    UsuarioBeans usuarioBeans = null;
    if (request.getSession().getAttribute("id_usuario") != null) {
        int usuarioLog = (Integer) request.getSession().getAttribute("id_usuario");
        usuarioBeans = new UsuarioDao().consultar(usuarioLog);
    }

    List<UsuarioLaboresBeans> list;
    boolean costo = false;
    if (usuarioBeans != null && usuarioBeans.getPerfil().getCod_perfil() != 4) {
        list = usuarioLaboresDao.listar(Integer.parseInt(usuario), null);
        costo = true;
    } else {
        list = usuarioLaboresDao.listar(Integer.parseInt(usuario));
    }

    ReferenciasBeans tipolabor, labor, extra;
    System.out.print(grid);
    if (grid != null && !grid.equals("")) {
        response.getWriter().printf("<select>");
    }


    response.getWriter().printf("<option value=\"" + "0" + "\">" + "Seleccione Labor" + "</option>");
    for (UsuarioLaboresBeans usuarioLaboresBeans : list) {
        labor = referenciasDao.consultar(usuarioLaboresBeans.getLabor().getLabor(), "RLABORES");
        tipolabor = referenciasDao.consultar(usuarioLaboresBeans.getLabor().getTipolabor(), "RTIPOLABOR");
        extra = referenciasDao.consultar(usuarioLaboresBeans.getLabor().getHoraextra(), "RHORASEXTRAS");

        option = "<option value=\"" + usuarioLaboresBeans.getLabor().getIdlaborcontrato() + "\">"
                + usuarioLaboresBeans.getLabor().getContrato().getCliente().getNomcliente() + " \t- "
                + usuarioLaboresBeans.getLabor().getGrupo().getDesgrupo() + " \t- "
                + labor.getDescripcion() + " \t- " + tipolabor.getDescripcion() + (extra != null ? " \t- " + extra.getDescripcion() : "");
        if (costo) {
            option += " (" + usuarioLaboresBeans.getLabor().getCosto() + ")";
        }
        option += "</option>";

        response.getWriter().printf(option);
    }
    if (grid != null && !grid.equals("")) {
        response.getWriter().printf("</select>");
    }

    out.close();
%>