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
        <script src="media/js/validation/validateGuias.js" ></script>
        <script src="media/js/grid/GRDGuias.js" ></script>
        <script src="media/js/grid/GRDRegistros.js" ></script>
    </head>
    <body>
        <%
            if (null == request.getSession().getAttribute("usuario")) {
                response.sendRedirect("index.htm");
            }
        %>
        <div id="bodyLeft" style="text-align: center;"> 
            <br>
            <h2 style="text-align: center;"><span>Carga de archivo para validacion</span></h2>
            <br><br>
            <form action="ArchivosPlanosServlet" method="POST" id="cabecera" enctype="multipart/form-data" target="upload_target">
                <table align="center" style="text-align: center;font-weight: bold;">
                    <tr><td colspan="2"><select id="cliente" name="cliente" style="width: 80%"><option>Seleccione Cliente...</option></select></td></tr>
                    <tr><td colspan="2"><br><select id="labor" name="labor" style="width: 80%"><option>Seleccione Labor...</option></select></td></tr>
                    <tr><td colspan="2"><br>Rango de Fechas a Cargar</td></tr>
                    <tr><td><br>Fecha Inicial  <input id="inicio" name="inicio" readonly="" class="fecha"/></td>
                        <td><br>Fecha Final  <input id="fin" name="fin" readonly="" class="fecha"/></td></tr>
                    <tr><td colspan="2"><br><input type="file" id="archivo" name="archivo" size="40%"/>
                            <input type="button" id="cargar" value="Analizar"/></td></tr>
                    <tr><td colspan="2"><div id="grid"><table id='gridCon'></table><div id='pagerCon'></div></div></td></tr>
                </table>
            </form>
            <iframe id="upload_target" name="upload_target" src=""  style="width: 50%;height: 60px;border:0px solid #fff;"></iframe>
            <br>
            <div id="grillaGuia" style="margin: 0 auto;">
                <center>
                    <table id='gridGuias'> </table>
                    <div id='pagerGuias'></div>
                </center>
                <input type="button" id="concilia" value="Conciliar" disabled=""/>
            </div> 
            <br>

            <div id="dlg_registros" style="font-size: 80%;">
                <center>
                    <table id='gridReg'> </table>
                    <div id='pagerReg'></div>
                </center>

                <label>Seleccione para editar</label><br>
                <label>Enter para confirmar</label>
            </div> 
        </div>
    </body>
</html>
