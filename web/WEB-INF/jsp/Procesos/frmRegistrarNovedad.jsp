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
        <script src="media/js/validation/novedad.js"></script>
        <style type="text/css">
            .interno{
                margin: auto;
                width: 450px;
                float: left
            }


        </style>
    </head>
    <body>
        <%
            if (null == request.getSession().getAttribute("usuario")) {
                response.sendRedirect("control.htm");
            }
        %>
        <div id="bodyLeft" style="text-align: center;">
            <br><br>
            <h2><span>Registrar Novedad</span></h2>
            <br><br>
            <form id="formNov" enctype="multipart/form-data" method="POST" action="NovedadesServlet" target="_parent">
                <div id="general" class="ui-widget">
                    <div style="width: 900px; height: 30px; margin: auto;">
                        <div class="interno">                    
                            <label for="auxiliar">Auxiliar&nbsp</label>
                            <select id="auxiliar" name="auxiliar"></select>
                        </div><div class="interno">  
                            <label for="novedad">Novedad</label>
                            <select id="novedad" name="novedad"></select>
                        </div>

                    </div>
                    <br><p>
                        <label>Fecha Inicio <input id="dateIni" name="dateIni" class="datepicker"></label>
                        <label for="dias">Dias </label><input maxlength="3" id="dias" name="dias" >
                        <label>Fecha Fin <input id="dateFin" name="dateFin" disabled=""></label>
                    </p><br>

                </div>
                <div id="incapacidad" class="ui-widget">
                    <label>Nro Incapacidad <input id="nroInc" name="nroInc" maxlength="6"></label>
                    <label>Cod Diagnostico <input id="codDx" name="codDx" maxlength="6"></label>
                    <br>
                </div>
                <div id="vacaciones" class="ui-widget">
                    <label>Dias Habiles<input id="diasHab" name="diasSio" class="numero" maxlength="3" disabled=""></label>
                    <label>Dias No Habiles<input id="diasNoHab" name="diasComp" class="numero" maxlength="3" disabled=""></label>
                    <br>
                </div>
                <div id="valores" class="ui-widget">
                    <label>Valor <input id="vlrNov" name="vlrNov" class="numero" maxlength="11" size="30"></label>
                    <br>
                </div>
                <br>
                <div class="ui-widget">
                    <label for="sopInc">Soporte Incapacidad <input id="sopInc" name="sopInc" type="file" accept="image/*"></label>
                </div>
                <br>
                <div class="ui-widget">
                    <textarea name="observacion" id="obsv" maxlength="40" cols="50" style="resize: none" placeholder="Escribe una pequeña descripción"></textarea><br><br>
                    <input type="submit" id="sub" class="ui-button"><input type="reset" class="ui-button" >
                </div>

            </form>

        </div> 
    </body>
</html>
