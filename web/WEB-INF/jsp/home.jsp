<%-- 
    Document   : Login
    Created on : 29/02/2012, 04:32:06 PM
    Author     : rquintero
--%>

<%@page import="org.apache.log4j.Logger"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.*"%>
<%@page import="java.io.*"%>
<%@page session="true"%>
<!DOCTYPE html>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>SIO SA | Procedatos</title>
        <link rel="shortcut icon" href="media/images/favicon.ico" />
        <style type="text/css" title="currentStyle">
            @import "media/css/Plantilla.css";
            @import "media/themes/redmond/jquery-ui-1.10.3.custom.css";
            @import "media/css/datepicker.css";
            @import "media/css/ui.dynatree.css";
            @import "media/css/Menu.css";
            @import "media/themes/ui.jqgrid.css";
            @import "media/themes/ui.multiselect.css";
            @import "media/css/Prenomina.css";
            @import "media/css/timepicker.min.css";
            @import "media/css/mobiscroll.custom-2.4.4.min.css"
        </style>
        <script src="media/js/include.js"></script>
        <script src="media/js/librerias.js"></script>
        <script src="media/js/menu/menu.js"></script>

        <script src="media/js/grid/GRDPlanillas.js"></script>

    </head>
    <body> 
        <%
            if (null == session.getAttribute("usuario")) {

                response.sendRedirect("control.htm");
            }
        %>
        <div id="container">
            <div id="top">
                <div id="topContact">
                    <p>Bienvenido,<%= session.getAttribute("usuario")%></p>
                    <a title="Cerrar Sesion" href="control.htm">Cerrar Sesi&oacute;n</a>?
                </div> 
                    <a href="<%= (this.getServletContext().getContextPath().equals("")?"/":this.getServletContext().getContextPath()) %>"><img src="media/images/logo.png"  alt="Soluciones Integrales de Oficina" border="0"  class="logo" title="Soluciones Integrales de Oficina"/></a>	
                <div id="topMenu">
                    <ul id="menu" ><%= session.getAttribute("menu")%></ul>
                </div>
            </div> 

            <div id="body"> 
                <div id="bodyIN"></div>
            </div>
            <div id="footermain">
                <div id="footer">
                    <p class="copyright">SIO S.A. | Cali: Cra 100 # 14 - 96 Barrio Ciudad Jardín PBX: (57 2) 485 5757 - (572) 485 5758 </p>
                    <p class="copyright1">Colombia</p>
                </div>
            </div>
        </div>
    </body>
</html>
