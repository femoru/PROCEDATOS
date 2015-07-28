<%-- 
    Document   : frmIngresoPlanoSOS
    Created on : 21/07/2015, 01:28:13 PM
    Author     : felipe
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page session="true"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <style type="text/css">
            #respuesta {
                width: 65%;
                margin: 15px auto;
                text-align: left;
                font-weight: bold;
                padding: 10px;
            }

        </style>
    </head>
    <body>
        <div style="text-align: center" id="bodyLeft">
            <br/>
            <h2><span>Ingreso de archivo plano Consolidado SOS</span></h2>
            <br/>
            <form id="planosos" action="PlanillaServlet" method="POST" enctype="multipart/form-data" target="_parent">
                <div class="ui-widget ui-wi ui-widget-content">
                    <label class="ui-widget"  for="consolidado"><b>Seleccione el archivo xls</b></label>
                    <br/>
                    <input id="consolidado" name="consolidado" type="file" placeholder="Seleccione el archivo xls" 
                           required="true" 
                           accept="application/vnd.ms-excel"
                           class="ui-button ui-widget"/>
                </div>
                <br/>
                <input type="submit" class="ui-button ui-widget ui-state-default" value="Subir Plano"/> 
                <div id="respuesta"></div>
            </form>
        </div>
        <script src="media/js/validation/validatePlanoSOS.js"></script>
    </body>
</html>
