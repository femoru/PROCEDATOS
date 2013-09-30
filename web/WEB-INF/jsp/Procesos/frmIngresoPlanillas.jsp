<%-- 
    Document   : frmIngresoPlanillas
    Created on : 21/08/2013, 05:09:21 PM
    Author     : fmoctezuma
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page session="true"%>
<!DOCTYPE html>
<html>
    <head>
        <script src="media/js/grid/GRDPlanillas.js"></script>
        <style type="text/css">
            #encabezado{
                display: inline-block;
                width: 100%
            }
            #nombre{
                border-width: 0;
                width: 100%;
            }
            .campos{
                display: inline-block;
            }
        
        </style>
    </head>
    <body>
        <div id="bodyLeft" >
            <div id="encabezado">
                <div class="campos" style="width: 15%"><label>Cedula: </label><br><input id="cedula" class="ui-widget" tabindex=0 maxlength="20" /></div>
                <div class="campos" style="width: 50%"><label>Nombre del Auxiliar:</label><br><input id="nombre" class="ui-widget" disabled/></div>
                <div class="campos" style="width: 15%;float: right"><label>Mes a Registrar: </label><br><input id="mes" class="ui-widget" tabindex=1/></div>
            </div>

            <div class="campos" style="width: 100%"><label>Labor: </label><br><select id="labor" class="ui-widget" style="width: 100%" tabindex=2><option value="-1">Ingrese el numero de cedula...</option></select></div>

            <br><br>

            <div class="grilla">
                <table id="gridPlanilla" tabindex="3"></table>
                <div id="pagerPlanilla"></div>
            </div>
            <br>
            <div>
                <input id="validar" type="button" class="ui-widget" value="Validar" style="float: right">
            </div>
            <br>
            <div class="campos" style="width: 100%"><label>Labor: </label><br><select id="laborE" class="ui-widget" style="width: 100%" tabindex=4><option value="-1">Ingrese el numero de cedula...</option></select></div>
            <br><br>
            <div class="grilla">
                <table id="gridPlanillaExtra" tabindex="5"></table>
                <div id="pagerPlanillaExtra"></div>
            </div>
            <br>
            <div>

                <input id="validarE" type="button" class="ui-widget" value="Validar" style="float: right">
            </div>

        </div>
    </body>
</html>
