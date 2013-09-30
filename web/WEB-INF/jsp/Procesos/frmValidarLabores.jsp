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
        <script src="media/js/grid/GRDValidarLabores.js"></script>
        <script src="media/js/grid/GRDAuxiliaresFaltantes.js"></script>
        <script src="media/js/grid/GRDAuxiliaresNovedades.js"></script>

    </head>
    <body>
        <%
            if (null == request.getSession().getAttribute("usuario")) {
                response.sendRedirect("index.htm");
            }
        %>
        <div id="bodyLeft">
            <h2><span>Validacion de labores </span> </h2>
            <table style="display: block;">                    
                <tr>                        
                    <td>
                        <div id="tr_filtros">
                            <input type="radio" name="flt" value="0" >Grupos
                            <input type="radio" name="flt" value="1" checked>Sitio
                        </div>
                    </td>
                    <td id="td_radio" style="display: block;" >
                        <input type="radio" name="rdio" value="0" checked> Sin validar 
                        <input type="radio" name="rdio" value="2"> Validados
                    </td>                       
                    <td  style="text-align: right;">
                        <input type="button" id="vTodos" value="Validar Todos"/>
                    </td>
                    <td style="text-align: right">
                        <div id="dateFilter">
                            Fecha <input type="text" id="dateIni"  readonly/>
                        </div>
                    </td>
                </tr>
                <tr><td colspan="4">
                        <div id="flt_grp" style="display: none;">
                            <label>Grupos: </label>
                            <select id="grupo"><option value="0">Seleccione grupo </option></select>
                        </div>
                        <div id="flt_sitio" >
                            <label>Sitio de Trabajo: </label>
                            <select id="sitio"><option value="0">PROCEDATOS</option><option value="1">OTRO</option></select>
                        </div></td></tr>
                <tr>
                    <td colspan="3" rowspan="2" style="width: 800px">
                        <div id="grilla" class="grilla">
                            <table id='gridLbr'> </table>
                            <div id='pagerLbr'></div>
                        </div>  
                    </td>
                    <td style="display: block">
                        <div id="grillaAux" class="grillaAux" >
                            <table id='gridAux'> </table>
                            <div id='pagerAux'></div>
                        </div>  
                    </td>
                </tr>
                <tr><td style="display: block">
                        <div id="grillaJust" class="grillaJust" >
                            <table id='gridJust'> </table>
                            <div id='pagerJust'></div>
                        </div>  </td></tr>
            </table>
        </div>
    </body>
</html>
