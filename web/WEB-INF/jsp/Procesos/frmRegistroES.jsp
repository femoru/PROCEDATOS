<%-- 
    Document   : FRMRegistroES
    Created on : 26-feb-2013, 14:49:07
    Author     : fmoctezuma
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>


<!DOCTYPE html>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>SIO | Procedatos | Inicio</title>
        <link rel="shortcut icon" href="media/images/favicon.ico" />
        <style type="text/css" title="currentStyle">
            @import "media/css/Plantilla.css";
            @import "media/css/Login.css";
        </style>

        <script src="media/js/include.js"></script>
        <script src="media/js/librerias.js"></script>
        <script src="media/js/validation/validateInOut.js"></script>
    </head>
    <body>
        <%
            session.invalidate();
        %>
        <div id="container">
            <div id="top">
                <a href="http://sio.com.co/"><img src="media/images/logo.png" alt="Soluciones Integrales de Oficina" border="0"  class="logo" title="Soluciones Integrales de Oficina"/></a>	
            </div>

            <div class="formul"> 
                <table class="contacto" align="center"  style="font-weight: bold;text-align: center">
                    <tr><td colspan="3"><center><h2 style="font-size: 18px">Registro de Entrada y salida</h2></center></td></tr>
                    <tr><td colspan="3" ><br></td></tr>        
                    <tr><td colspan="3" >Identificación: &nbsp; <input id="login" type="text" value='' style="text-align: center" /></td></tr>


                    <tr><td colspan="3" >Contraseña: &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <input id="pass" type="password" value=''  style="text-align: center" /></td></tr>        


                    <tr><td colspan="3" ><input id="nombre" type="text" style="width: 657px;text-align: center" disabled="true"/></td></tr>
                    <tr><td colspan="3" ><center><input id="consulta" type="button" value='Cargar Labores' style="text-align: center" /></center></td></tr>

                    <tr id="lbr_tr"><td colspan="3"><input id="lbr" type="text" style="width: 657px;text-align: center" disabled="true"/></td></tr>
                    <tr id="tr_labor" ><td colspan="1" ><label>Labores Asignadas</label></td>
                        <td colspan="2"><select id="labor" disabled  style="display: block;"><option value="0">Seleccione Labor</option> </select></td>
                    </tr>

                    <tr id="guias">
                        <td id="tr_dato">Dato labor: <input style="width: 200px;text-align: center" type="text" id="dato_0" class="dato"/>  </td>
                        <td id="tr_registro">Registros: <input type="text" style="width: 100px;text-align: center" id="registros_0" class="registros"/></td>
                        <td><input type="button" value="+" id="btnguias"  style="font-size: 15px;font-weight: bold"/></td>
                    </tr>

                    <tr id="imagenes" >
                        <td id="tr_imagenes" >Imágenes: <input type="text" style="width: 100px;text-align: center" id="imagenes_0" class="imagenes"/></td>
                    </tr>

                    <tr><td colspan="3"><br></td></tr>
                    <tr id="tr_entradaSalida">
                        <td colspan="3"><input type="button" id="entrada" value="Entrada" disabled/><input type="button" id="salida" value="Salida" disabled/></td>
                    </tr>
                    <tr><td colspan="3"><br><br><br></td></tr>
                </table>
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