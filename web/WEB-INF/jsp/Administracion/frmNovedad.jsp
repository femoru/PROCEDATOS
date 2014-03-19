<%-- 
    Document   : frmNovedades
    Created on : 05-mar-2013, 14:07:39
    Author     : fmoctezuma
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page session="true"%>

<!DOCTYPE html>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html>
    <head>
        <script src="media/js/grid/GRDRefNovedad.js"></script>
    </head>
    <body>
        <%
            if (null == session.getAttribute("usuario")) {
                
                response.sendRedirect("control.htm");
            }
        %>
        <div id="bodyLeft">
            <br>
            <h2><span>Gestión de Novedades</span> </h2>
            <br><br>
            <div id="grilla" class="grilla" style="margin: 0 auto">
                <table id='gridNov'> </table>
                <div id='pagerNov'></div><br><br>
            </div>  
        </div>

    </body>
</html>
