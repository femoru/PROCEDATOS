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

        <script src="media/js/grid/GRDNovedades.js"></script>
        <script src="media/js/dlg/DLGValidarNovedad.js"></script>
    </head>
    <body>
        <%
            if (null == request.getSession().getAttribute("usuario")) {
                response.sendRedirect("index.htm");
            }
        %>
        <div id="bodyLeft">
            <br>
            <h2><span>Validar Novedades</span> </h2>
            <br><br>
            <label>Mes a validar: </label><input id="periodo">
            <br><br>
            <div id="grilla" class="grilla" >
                <table id='gridNov'> </table>
                <div id='pagerNov'></div><br><br>
            </div>
            <div id="dlgvalidar" class="ui-widget" style="font-size: 90%;text-align: center">
                <div>
                    <label>Fecha Inicio <input id="dateIni" name="dateIni" class="datepicker"></label>
                    <label for="dias">Dias </label><input maxlength="3" id="dias" name="dias" >
                    <label>Fecha Fin <input id="dateFin" name="dateFin" disabled=""></label>
                </div><br> 
                <div>
                    <label>Nro Incapacidad <input id="nroInc" name="nroInc" maxlength="6"></label>
                    <label>Cod Diagnostico <input id="codDx" name="codDx" maxlength="6"></label>

                </div><br>
                <div >
                    <div>
                        <div>
                            <label for="clsInc">Clase Incapacidad</label><select id="clsInc" name="clsInc">
                                <option value="0"></option>
                                <option value="1">ENFERMEDAD GENERAL</option>
                                <option value="2">MATERNIDAD / PATERNIDAD</option>
                                <option value="3">ACCIDENTE DE TRABAJO</option>
                                <option value="4">ENFERMEDAD PROFESINAL</option>
                            </select>
                        </div>
                        <br>
                        <div>
                            <label >Fecha Accidente <input id="dateAcc" name="dateAcc" class="datepicker" disabled="" ></label>
                        </div>
                    </div><br><div>
                        <input type="checkbox" id="indPro" name="indPro" value="1"><label for="indPro">Prorroga </label>
                        <label for="clsInc">Incapacidades</label>
                        <select id="incAnt" name="incAnt"><option value="0">Seleccione la incapacidad</option></select>
                        <label>Nro Incapacidad CGUNO <input id="nroIncCg" name="nroIncCg" maxlength="3" class="numero"></label>
                    </div><br><div>
                        <label>Dias SIO <input id="diasSIO" name="diasSIO" maxlength="3" size="10" class="numero dias" style="text-align: center"></label>
                        <label>Dias EPS <input id="diasEPS" name="diasEPS" maxlength="3" size="10"  class="numero dias" style="text-align: center"></label>
                    </div><br><div>
                        <label>Valor SIO 3 dias <input id="vlrSIO" name="vlrSIO" maxlength="11" class="numero"></label>
                        <label>Porcentaje EPS <input id="prc" name="prc" maxlength="5" size="6" class="numero"></label>
                        <label>Valor EPS 2/3<input id="vlrEPS" name="vlrEPS" maxlength="11" class="numero"></label>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>
