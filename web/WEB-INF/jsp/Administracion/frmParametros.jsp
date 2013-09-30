<%-- 
    Document   : frmparametros
    Created on : 05-mar-2013, 14:07:39
    Author     : fmoctezuma
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page session="true"%>

<!DOCTYPE html>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html>
    <head>
        <script src="media/js/grid/GRDParametros.js"></script>
    </head>
    <body>
        <%
            if (null == session.getAttribute("usuario")) {

                response.sendRedirect("index.htm");
            }
        %>

        <div id="bodyLeft">
            <br>
            <h2><span>Gestión de Parámetros</span> </h2>
            <br><br>
            <div id="grilla" class="grilla" style="margin: 0 auto">
                <table id='gridParam'> </table>
                <div id='pagerParam'></div><br><br>
            </div>  
        </div>

    </body>

</html>
