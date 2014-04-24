<%-- 
    Document   : Olvido Contraseña
    Created on : 12/03/2012, 10:36:29 AM
    Author     : jcarvajal
--%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.*"%>
<%@page import="java.io.*"%>
<%@page session="true"%>
<!DOCTYPE html>
<html>
    <head>

        <title>SIO SA | Procedatos</title>
        <link rel="shortcut icon" href="media/images/favicon.ico" />
        <style type="text/css" title="currentStyle">
            @import "media/css/Login.css";
            @import "media/css/Plantilla.css";
        </style>

        <style type="text/css">
            html, body {
                margin: 0;			/* Remove body margin/padding */
                padding: 0;
                overflow: hidden;	/* Remove scroll bars on browser window */
                font-size: 75%;
            }
        </style>

        <script src="media/js/include.js"></script>
        <script src="media/js/librerias.js"></script>
        <script src="media/js/validation/validateOlvidoContrasena.js"></script>
    </head>
    <body>    
        <%
            session.invalidate();
        %>
        <div id="top">
            <a href="http://sio.com.co/"><img src="media/images/logo.png" alt="Soluciones Integrales de Oficina" border="0"  class="logo" title="Soluciones Integrales de Oficina"/></a>	
            <div id="topContact">
                <a title="Cerrar Sesion" href="control.htm">Regresar a login.</a>
            </div>
        </div>
        <br>
        <div id="body">
            <div id="bodyLeft">            
                <center><h3>Recuperación de Contraseña</h3></center>
                <br><br>
                <form:form method="POST" commandName="frmRecuperar" id="frmRecuperar">
                    <div style="padding: 5px 15%"  align="center">
                        <table>
                            <tr><td><br></td></tr> 
                            <tr>
                                <td>Número de Documento:</td>
                                <td>Email:</td>                        
                            </tr>
                            <tr>                            
                                <td>
                                    <form:input  path="identificacion" id="identificacion" value="" cssClass="textbox" size="25" readonly="false" onkeypress="return justNumbers(event);" tabindex="2" maxlength="20"/>
                                </td>                        
                                <td><form:input  path="email" id="email" cssClass="textbox" size="54" readonly="false"  tabindex="17" maxlength="100"/></td>
                            </tr>
                            <tr><td colspan="2"><br></td></tr>
                            <tr>
                                <td  colspan="2"><center>
                                <input type="submit" value='Recuperar' id="btn" name="btn" class="guardar" />
                                </td></center>
                            </tr>
                            <tr><td colspan="2"><br><center><label path="mensaje" id="mensaje" Class="LabelError">${frmRecuperar.mensaje}</label></center></td></tr>
                        </table>
                    </div>
                </form:form>
                <div><br><br><br><br><br><br><br><br><br><br><br></div> 
            </div>
        </div>
        <div id="footermain">
            <div id="footer">
                <p class="copyright">SIO S.A. | Cali: Cra 100 # 14 - 96 Barrio Ciudad Jardín PBX: (57 2) 485 5757 - (572) 485 5758 </p>
                <p class="copyright1">Colombia</p>
            </div>
        </div>
    </body>
</html>
