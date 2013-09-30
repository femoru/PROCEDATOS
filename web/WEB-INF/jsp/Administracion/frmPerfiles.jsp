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
        <script src="media/js/grid/GRDPerfiles.js"></script>
        <script src="media/js/treeview/TRVMenu.js"></script>
    </head>
    <body>
        <%
            if (null == session.getAttribute("usuario")) {

                response.sendRedirect("index.htm");
            }
        %>
        <div id="bodyLeft">
            <br>
            <h2><span>Administración de  Perfiles y Permisos</span> </h2>
            <br><br>
            <table>
                <tr>
                    <td>
                        <div id="grilla" class="grilla">
                            <table id='grid'> </table>
                            <div id='pager'></div>
                        </div>
                    </td>
                    <td valign="top">
                        <div>
                            <div id="checkTree" style="border: 1pt solid #01A9DB;width: 290px; height: 310px;"></div>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td style="text-align: right"><input type="button" id="btnLimpiar"   value="Cancelar" disabled/>
                    </td><td style="text-align: left">   <input type="button"  id="btnGrabar"  value="Grabar Permisos" disabled /></td>
                </tr>
            </table>
        </div>
    </body>
</html>
