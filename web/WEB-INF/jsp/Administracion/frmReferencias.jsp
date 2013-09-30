<%-- 
    Document   : frmvalidarlabores
    Created on : 05-mar-2013, 14:07:39
    Author     : fmoctezuma
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page session="true"%>

<!DOCTYPE html>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html>
    <head>

        <script src="media/js/grid/GRDReferencias.js"></script>
        <script src="media/js/grid/GRDListadoReferencias.js"></script>

    </head>
    <body>
        <%
            if (null == session.getAttribute("usuario")) {

                response.sendRedirect("index.htm");
            }
        %>

        <div id="bodyLeft">
            <br>
            <h2><span>Gestión de Referencias</span> </h2>
            <br><br>
            <table >
                <tr valign="top">
                    <td >
                        <div id="grilla" class="grilla" style="margin: 0 auto">
                            <table id='gridRef'> </table>
                            <div id='pagerRef'></div>
                        </div>  <br>
                    </td>
                    <td >                        
                        <div id="grilla2" class="grilla2" style="margin: 0 auto" style="display: none;">
                            <table id='gridLRef'> </table>
                            <div id='pagerLRef'></div>
                        </div>  
                    </td>
                </tr>
            </table>
        </div>

    </body>

</html>
