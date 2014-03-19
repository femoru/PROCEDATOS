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

        <style type="text/css">

            #grid, #slider-map{
                float: left;
                position: absolute;
            }
            #expand{
                width:12px;
                height:460px;
            }
            #encab div{
                width: 200px;
                float: right;
            }
            #encab {
                float: right;
                width: 100%
            }
        </style>
    </head>
    <body>
        <%
            if (null == request.getSession().getAttribute("usuario")) {
                response.sendRedirect("control.htm");
            }
        %>
        <div id="bodyLeft">
            <h2><span>Validacion de labores </span> </h2><br>

            <div id="encab">
                <div id="tr_filtros" style="float: left;">
                    <input type="radio" name="flt" value="0" >Grupos

                    <input type="radio" name="flt" value="1" checked>Sitio
                </div>

                <div style="text-align: center">
                    <input type="text" id="dateMon" style="display: none;" readonly/>
                    <input type="text" id="dateIni"  readonly/>
                </div>


                <div>
                    <input type="radio" name="fechas" value="0" onclick="$('#dateMon').hide();
                            $('#dateIni,#vTodos').show();" checked> Dias 
                    <br>
                    <input type="radio" name="fechas" value="2" onclick="$('#dateIni,#vTodos').hide();
                            $('#dateMon').show();"> Meses
                </div>

                <div style="float: left;">
                    <input type="radio" name="rdio" value="0"  checked> Sin validar 
                    <input type="radio" name="rdio" value="2"> Validados
                </div>
                <div>
                    <input type="button" id="vTodos" class="ui-button" value="Validar Todos"/>
                </div>
            </div>
            <br><br><div  style="float: left;">
                <div id="flt_grp" style="display: none;">
                    <label>Grupos: </label>
                    <select id="grupo"><option value="0">Seleccione grupo </option></select>
                </div>
                <div id="flt_sitio" >
                    <label>Sitio de Trabajo: </label>
                    <select id="sitio"><option value="0">PROCEDATOS</option><option value="1">OTRO</option></select>
                </div>
            </div>
            <br><br>

            <div id="grilla" class="grilla" style="float: left;width: 98%">

                <table id='gridLbr'> </table>
                <div id='pagerLbr'></div>

            </div>  

            <div style="float: left;">
                <div id="slider-map"><span id="expand">Mas Información</span></div>
                <div id="grid" style="right:  7%">
                    <div id="grillaAux" class="grillaAux" >
                        <table id='gridAux'> </table>
                        <div id='pagerAux'></div>
                    </div>  
                    <div id="grillaJust" class="grillaJust" >
                        <table id='gridJust'> </table>
                        <div id='pagerJust'></div>
                    </div>
                </div>
            </div>


        </div>
    </body>
</html>
