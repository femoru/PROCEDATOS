<%-- 
    Document   : frmReporteUsuarios
    Created on : 7/10/2013, 08:50:26 AM
    Author     : fmoctezuma
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page session="true"%>

<!DOCTYPE html>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html>
    <head>
        <script src="media/js/grid/GRDRUsuario.js"></script>
    </head>
    <body>
        <%
            if (null == request.getSession().getAttribute("usuario")) {
                response.sendRedirect("index.htm");
            }
        %>
        <div id="bodyLeft">
            <br>
            <h2><span>Reporte Estado de Usuarios</span> </h2>
            <br>
            <div>
                <label for="month">Mes de Consulta Producci&oacute;n</label><input class="months" id="month">
            </div>
            <br>
            <div id="grilla" class="grilla">
                <table id='gridAux'> </table>
                <div id='pagerAux'></div>
            </div>

        </div>
    </body>
</html>
