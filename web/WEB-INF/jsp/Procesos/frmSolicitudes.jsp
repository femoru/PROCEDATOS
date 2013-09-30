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
        <script src="media/js/grid/GRDSolicitudes.js"></script>
    </head>
    <body>
        <%
            if (null == request.getSession().getAttribute("usuario")) {
                response.sendRedirect("index.htm");
            }
        %>
        <div id="bodyLeft">
            <br>
            <h2><span>Gestion de Solicitudes</span> </h2>
            <br>
            <table><tr><td>
                        <div id="dateFilter">
                            <select id="comboFecha" >
                                <option value="1" selected="selected">Fecha Solicitud</option>
                                <option value="2" >Fecha Respuesta</option>
                            </select> Entre  
                            <input type="text" id="dateIni"  readonly/> - <input type="text" id="dateFin" readonly /></div>
                    </td>
                    <td id="td_radio" style="text-align: left">
                        <input type="radio" name="rdio" value="5" checked> Solicitudes Coordinador
                        <input type="radio" name="rdio" value="4"> Solicitudes Auxiliares
                    </td>
                    <td style="text-align: right">
                        <div><label>Estado de solicitud </label><select id="comboEstado" >
                                <option value="0" selected="selected">Registradas</option>
                                <option value="1" >Contestadas</option>
                                <option value="2" >Canceladas</option>
                            </select></div>
                    </td></tr>
                <tr><td colspan="3">
                        <div id="grilla" class="grilla" style="width: max-content">
                            <table id='gridSlt'> </table>
                            <div id='pagerSlt'></div>
                        </div> <br> 
                    </td></tr></table>
        </div>
    </body>
</html>
