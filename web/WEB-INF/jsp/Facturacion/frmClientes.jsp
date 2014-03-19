<%-- 
    Document   : frmvalidarlabores
    Created on : 05-mar-2013, 14:07:39
    Author     : fmoctezuma
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.util.*"%>
<%@ page import="java.io.*"%>
<%@ page session="true"%>

<!DOCTYPE html>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html>
    <head>
        <script src="media/js/grid/GRDClientes.js" ></script>
        <script src="media/js/grid/GRDGrupos.js" ></script>
        <script src="media/js/grid/GRDAreas.js" ></script>
    </head>
    <body>
        <%
            if (null == session.getAttribute("usuario")) {

                response.sendRedirect("control.htm");
            }
        %>
        <div id="bodyLeft">
            <br>
            <h2><span>Gestión de Clientes</span> </h2>
            <br><br>
            <table>
                <tr>
                    <td >
                        <div id="grilla">
                            <table id='gridClt'> </table>
                            <div id='pagerClt'></div>
                        </div>  
                    </td> 
                    <td>
                        <div id="grillaArea" hidden="true">
                            <table id='gridArea'> </table>
                            <div id='pagerArea'></div>
                        </div>  
                    </td>
                    <td>
                        <div id="grillaGrp" hidden="true">
                            <table id='gridGrp'> </table>
                            <div id='pagerGrp'></div>
                        </div>  
                    </td>
                </tr>
                <tr><td><br><br></td></tr>
            </table>
        </div>
    </body>
</html>
