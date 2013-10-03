<%-- 
    Document   : frmReporteFacturacion
    Created on : 17/09/2013, 08:31:52 AM
    Author     : fmoctezuma
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page session="true"%>

<!DOCTYPE html>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html>
    <head>
        <script src="media/js/grid/GRDReporte.js"></script>
    </head>
    <body>
        <%
            if (null == request.getSession().getAttribute("usuario")) {
                response.sendRedirect("index.htm");
            }
        %>
        <div id="bodyLeft">
            <br>
            <h2><span>Reportes de Facturacion</span> </h2>
            <br>
            <div style="height: 40px">
                <div style="float: left">
                    <div>Facturacion:
                        <input type="radio" name="tipo" value="0" checked>Consolidado 
                        <input type="radio" name="tipo" value="1" >Detallado 
                    </div>
                    <div>Nomina:
                        <input type="radio" name="tipo" value="2" >Consolidado 
                        <input type="radio" name="tipo" value="3" >Detallado 
                    </div>
                </div>
                <div style="float: left;margin-left: 30px">
                    <div style="display: none">
                        <input type="radio" name="clase" value="0" checked onchange="$('#rango').hide();$('#idnomina').show();">Nomina 
                        <input type="radio" name="clase" value="1" onchange="$('#idnomina').hide();$('#rango').show();" >Fechas 
                    </div>
                    <div id="idnomina">
                        <select id="nomina" style="margin: 0"></select>
                    </div>
                    <div id="rango" style="display: none;">
                        Fecha Inicial<input class="datepicker">
                        Fecha Final<input class="datepicker">
                    </div>
                </div>

                <input type="button" class="ui-button" id="generar" style="float: right"  value="Generar">
                <input type="button" class="ui-button" id="previa" style="float: right;display: none"  value="Vista Previa">
            </div>
            <br>
            <div id="grilla" class="grilla">
                <table id='gridRep'> </table>
                <div id='pagerRep'></div>
            </div>

        </div>
    </body>
</html>
