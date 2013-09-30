<%-- 
    Document   : Login
    Created on : 29/08/2013, 04:32:06 PM
    Author     : jcarvajal
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.util.*"%>
<%@ page import="java.io.*"%>
<%@ page session="true"%>

<!DOCTYPE html>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>SIO SA | Procedatos</title>
        <link rel="shortcut icon" href="media/images/favicon.ico" />
        <style type="text/css" title="currentStyle">
            @import "media/css/Login.css";
            @import "media/css/Plantilla.css";
        </style>

        <script src="media/js/include.js"></script>
        <script src="media/js/librerias.js"></script>
        <script src="media/js/generales/funcionesGenerales.js"></script>
        <script src="media/js/validation/validateLogin.js"></script>
    </head>
    <body >
        <%
            session.invalidate();
        %>
        <div id="container">
            <div id="top">
                <a href="http://sio.com.co/"><img src="media/images/logo.png" alt="Soluciones Integrales de Oficina" border="0"  class="logo" title="Soluciones Integrales de Oficina"/></a>	
            </div><br>

            <div class="algo">
                <form:form action="LoginServlet" cssClass="contacto" method="POST" commandName="frmlogin" id="frmlogin">
                    <div class="titulo">PROCEDATOS</div>
                    <div><label>Usuario:</label><form:input tabindex="1" path="login" id="login" type='text' cssClass="usuario" value='' placeholder="Usuario" /></div>
                    <div><label>Clave:</label><form:input tabindex="2" path="clave" id="clave" type='password' data-mini="true" value='' placeholder="Contraseña"/></div>
                    <div align="right"><label><a href="OlvidoContrasena.htm">¿No puedes iniciar sesión?</a></label></div>   
                    <div><center><input type='submit' tabindex="3" value='Aceptar'  id="btnConsultar" name="btnConsultar" onclick="Generar();"/></center></div>
                    <div><label path="mensaje" id="mensaje" Class="LabelError">${frmlogin.mensaje}</label></div>
                    </form:form>
            </div>

            <div id="footermain">
                <div id="footer">
                    <p class="copyright">SIO S.A. | Cali: Cra 100 # 14 - 96 Barrio Ciudad Jardín PBX: (57 2) 485 5757 - (572) 485 5758 </p>
                    <p class="copyright1">Colombia</p>
                </div>
            </div>
        </div>
    </body>
</html>
