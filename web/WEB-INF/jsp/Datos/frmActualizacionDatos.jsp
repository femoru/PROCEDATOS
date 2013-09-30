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
        <script src="media/js/validation/validateCambioPaciente.js" type="text/javascript"></script>
    </head>
    <body>
        <%
            if (null == session.getAttribute("usuario")) {

                response.sendRedirect("index.htm");
            }
        %>
        <div id="bodyLeft">            
            <center><h2><span>Datos de Contacto</span></h2></center>        
            <br><br>
            <form:form method="POST" commandName="frmActualizaDatos" id="frmActualizaDatos">
                <div style="padding: 5px 15%"  align="center">
                    <table>
                        <tr>
                            <td>Dirección de Residencia:</td>
                            <td>Nro. Telefono</td>
                        </tr>
                        <tr>
                            <td><form:input  path="direccion" id="direccion" value="${frmActualizaDatos.direccion}" cssClass="textbox" size="59" readonly="false" onkeypress="return justCharacterNumber(event);"  tabindex="14"/></td>
                            <td><form:input  path="telefono" id="telefono" value="${frmActualizaDatos.telefono}" cssClass="textbox" size="22" readonly="false" maxlength="7" onkeypress="return justNumbers(event);"  tabindex="15"/></td>
                        </tr>
                        <tr>
                            <td>Email:</td>
                            <td>Nro. Celular:</td>
                        </tr>
                        <tr>
                            <td><form:input  path="email" id="email" value="${frmActualizaDatos.email}" cssClass="textbox" size="59" readonly="false"  tabindex="17" maxlength="100"/></td>
                            <td><form:input  path="celular" id="celular" value="${frmActualizaDatos.celular}" cssClass="textbox" size="22" readonly="false" maxlength="10" onkeypress="return justNumbers(event);"  tabindex="16"/></td>
                        </tr>
                        <tr><td><br><br></td></tr>
                        <tr>
                            <td colspan="2"><center>
                            <input type="submit" value='Actualizar' id="btn" name="btn"  class="actualizar" />
                            </td></center>
                        </tr>
                    </table>
                    <div><label path="mensaje" id="mensaje" Class="LabelError">${frmActualizaDatos.mensaje}</label></div>
                </div>
            </form:form>
            <div><br><br><br><br><br><br><br></div>        
        </div> 
    </body>
</html>
