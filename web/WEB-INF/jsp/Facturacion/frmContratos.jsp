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
        <script src="media/js/grid/GRDContratos.js"></script>
        <script src="media/js/grid/GRDLabores.js"></script>
        <script src="media/js/dlg/DLGLaborContrato.js"></script>
        <script src="media/js/dlg/DLGLaboresExtra.js"></script>
    </head>
    <body>
        <%
            if (null == session.getAttribute("usuario")) {

                response.sendRedirect("control.htm");
            }
        %>

        <div id="bodyLeft">
            <br>
            <h2><span>Administración de Contratos y Labores</span> </h2>
            <br>
            <label>Cliente: </label> <select id="cliente" style="width: 100%"><option value="0">Seleccione Cliente...</option></select>
            <br><br>
            <table>
                <tr>
                    <td valign="top"><div id="grillaContrato">
                            <table id='gridCtr'> </table>
                            <div id='pagerCtr'></div>
                        </div></td>
                    <td valign="top"><div id="grillaAreaLabores">
                            <table id='gridLbr'> </table>
                            <div id='pagerLbr'></div>
                        </div></td>
                </tr>
            </table>
            <div id="dlglabor" style="font-size: 75%">

                <table>

                    <tr class="">
                        <td>Area</td>
                        <td>Grupo</td>
                        <td>Tipo de Labor</td>
                    </tr>
                    <tr>
                        <td><select id="area" ></select></td>
                        <td><select id="grupo" ></select></td>
                        <td><select id="tipo" ></select></td>
                    </tr>
                    <tr>
                        <td colspan="3" >Labor</td>
                    </tr>
                    <tr>
                        <td colspan="3" ><select id="labor"></select></td>

                    </tr>
                    <tr>
                        <td>Valor</td>
                        <td>Costo</td>

                    </tr>
                    <tr>
                        <td><input id="valor" class="numero"></td>
                        <td><input id="costo" class="numero"></td>
                        <td><input type="checkbox" id="conciliacion">Conciliacion</td>
                    </tr>
                    <tr>
                        <td>Estado</td>
                        <td>Requiere DatoLabor</td>
                    </tr>
                    <tr>
                        <td><select id="estado"><option value="1">Activo</option><option value="0">Inactivo</option></select></td>
                        <td><select id="datolabor"><option value="1">Si</option><option value="0">No</option></select></td>
                    </tr>

                </table>

            </div>
            <div id="dlgExtras" style="font-size: 75%">
                <fieldset>
                    <legend>Extras:</legend>
                    <table>
                        <tr><td><table id="tablaExtra"></table></td></tr>
                        <tr><td><select id="horaextras"></select>
                                Valor Facturado: <input id="valorExtra" disabled>
                                Costo: <input id="costoExtra" disabled>
                                <input type="button" id="otroExtra" value="Aplicar Extra" disabled/></td>
                        </tr>
                    </table>
                </fieldset>
            </div>
        </div>
    </body>
</html>
