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
        <script src="media/js/grid/GRDUsuarioLabor.js"   ></script>
        <script src="media/js/grid/GRDListaClientes.js"  ></script>
        <script src="media/js/dlg/DLGAsignar.js"         ></script>
        <script src="media/js/grid/GRDLaboresAsignar.js" ></script>
        <script src="media/js/grid/GRDLaboresUsuario.js" ></script>
        <script src="media/js/dlg/DLGHoras.js" ></script>
    </head>
    <body>
        <%
            if (null == session.getAttribute("usuario")) {

                response.sendRedirect("control.htm");
            }
        %>
        <div id="bodyLeft">
            <br>
            <h2><span>Asignación de Labores</span> </h2>
            <br><br>
            <table><tr>
                    <td>
                        <div id="grillaUsr" >
                            <table id='gridUsr'> </table>
                            <div id='pagerUsr'></div>
                        </div>   
                    </td>                            
                    <td style="width: 800px">
                        <div id="grillaLabor" >
                            <table id='gridLbr'> </table>
                            <div id='pagerLbr'></div>
                        </div>  
                    </td>
                </tr>
            </table>
        </div>
        <div id="asignarDialog" >
            <table><tr>
                    <td rowspan="3" >
                        <div id="listaClientes" >
                            <table id='gridLstClt'> </table>
                            <div id='pagerLstClt'></div>
                        </div>
                    </td>
                    <td  style="width: 100px;height: 20px"><span>Areas</span></td>
                    <td ><select id="areas" style="width: 200px" disabled><option value="-1">Seleccione Area...</option></select></td>
                </tr><tr>
                    <td  style="width: 100px;height: 20px"><span>Grupos</span></td>
                    <td ><select id="grupos" style="width: 200px" disabled><option value="-1">Seleccione Grupo...</option></select></td>
                </tr><tr>
                    <td colspan="2"  >
                        <div id="grillaLaborUsuario" >
                            <table id='gridLbrUsr'> </table>
                            <div id='pagerLbrUsr'></div>
                        </div>
                    </td>
                </tr></table>
        </div>
        <div id="fechas"><table><tr><td>Fecha inicial</td><td><input class="fecha"></td></tr>
                <tr><td>Fecha final</td><td><input class="fecha"></td></tr>
                <tr><td colspan="2" style="text-align: right"><input type="button" id="btnConfirmar" value="Confirmar"></td></tr></table>
        </div>
    </body>
</html>
