<%-- 
    Document   : reject
    Created on : 21-jun-2013, 11:29:48
    Author     : fmoctezuma
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page session="true"%>

<!DOCTYPE html>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>SIO SA | Procedatos</title>
        <link rel="shortcut icon" href="media/images/favicon.ico" />
        <style type="text/css" title="currentStyle">
            @import "media/css/Plantilla.css";
            @import "media/css/login.css";
        </style>

        <script src=""></script>

    </head>
    <body>
        <%
            session.invalidate();
        %>
        <div id="top">
            <a href="http://sio.com.co/"><img src="media/images/logo.png" alt="Soluciones Integrales de Oficina" border="0"  class="logo" title="Soluciones Integrales de Oficina"/></a>	
        </div><br>
        <div class="contacto" style="">
            <center>
                <h2>Internet Explorer no soporta muchas de las características usadas, <br><br>
                    Te invitamos a usar uno de los siguientes navegadores
                </h2><br><br><br><br>
                <img src="media/images/browserwar.jpg">

            </center>
        </div>

        <div id="footermain" style="position: relative;bottom: 0;">
            <div id="footer">
                <p class="copyright">SIO S.A. | Cali: Cra 100 # 14 - 96 Barrio Ciudad Jardín PBX: (57 2) 485 5757 - (572) 485 5758 </p>
                <p class="copyright1">Colombia</p>
            </div>
        </div>
    </body>
</html>
