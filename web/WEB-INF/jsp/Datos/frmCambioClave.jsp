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
        <script src="media/js/validation/validateCambioClave.js" ></script>
    </head>
    <body>
        <%
            if (null == session.getAttribute("usuario")) {

                response.sendRedirect("control.htm");
            }
        %>
        <div id="bodyLeft">
            <br><br><br>
            <center><h2><span>Cambio de Contraseña</span></h2></center>
            <br><br>
            <form:form method="POST" commandName="frmActualizaDatos" id="frmActualizaDatos">
                <div style="padding: 3px 35%">
                    <table align="center">
                        <tr ><td><br/></td></tr>
                        <tr>
                            <td>Contraseña Actual:</td>
                            <td colspan="2">
                                <input type="password" name="actual" id="actual" maxlength="20" class="textboxN" size="25" tabindex="1"/>
                                <input id="passwordActual" type="text" value="${frmActualizaDatos.clave}" cssClass="textbox" size="25" readonly="false" style="display: none"/>
                            </td>
                        </tr>
                        <tr>
                            <td>Contraseña Nueva:</td>  
                            <td colspan="2"><form:input path="usuario.clave" id="clave" cssClass="textboxN" maxlength="20" size="25" type="password" tabindex="2"/></td>                        
                        </tr>
                        <tr>
                            <td>Confirmar Contraseña:</td>
                            <td colspan="2"><input type="password" name="passwordconf" id="passwordconf" maxlength="20" class="textboxN" size="25" tabindex="3"/></td>
                        </tr>
                        <tr>
                            <td>Ingrese los Caracteres:</td>                         
                            <td width="88px">
                                <div style="width: 80px; text-align:left">
                                    <img src="captcha.htm" />
                                </div> 
                            </td> 
                            <td>
                                <form:input path="usuario.imagen" id="imagen" cssClass="textboxN" maxlength="7" size="10" tabindex="4"/>
                            </td>
                        </tr>
                        <tr>
                            <td colspan="3"><center>
                            <input type="submit" value='Cambiar' id="btn" name="btn" class="cambiar" />
                            </td></center>
                        </tr>   
                        <tr><td><div>
                                    <center>
                                        <label path="mensaje" id="mensaje" style=" color:#89B814" Class="LabelError">${frmActualizaDatos.mensaje}</label>
                                    </center></div></td></tr>
                    </table>
                </div>
            </form:form>        
            <div><br><br><br><br><br><br><br></div>        
        </div> 
    </body>
</html>
