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
        <style type="text/css" title="currentStyle">
            div.grilla{
                font-size: 75%;
            }
            div.dw-persp{
                z-index: 3050;
            }
            div.ui-dialog{
                top: 50%; /* Buscamos el centro horizontal (relativo) del navegador */
                left: 50%; 
            }

        </style>

        <script src="media/js/grid/GRDPrenomina.js"></script>
        <!-- 
        <script src="media/js/grid/GRDNovedades.js"></script>
        <script src="media/js/grid/GRDValidarLabores.js"></script>
        <script src="media/js/dlg/DLGPrenomina.js"></script>
        -->

    </head>
    <body>
        <%
            if (null == session.getAttribute("usuario")) {

                response.sendRedirect("control.htm");
            }
        %>
        <div id="bodyLeft">
            <br>
            <h2><span>Ajuste Prenomina</span> </h2>
            <br>

            <div style="margin: 0 auto">
                <table id='gridNom'> </table>
                <div id='pagerNom'></div>
            </div>  
        </div>
        <div id="dlg_detalle" style="text-align: center;">

            <div id="grilla" class="grilla" style="margin: 0 auto">
                <table id='gridLbr'> </table>
                <div id='pagerLbr'></div>
            </div>
            <br>
            <center>
                <div id="grilla" class="grilla">
                    <table id='gridNov'> </table>
                    <div id='pagerNov'></div>
                </div>
            </center>
        </div>
        <!--
                <div id="validar_Novedad" class="grilla"></div>
                <div id="registrarNovedad" class="grilla"> </div>
        -->
    </body>
</html>
