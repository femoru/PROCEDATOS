<%-- 
    Document   : frmAsistencias
    Created on : 11/03/2014, 10:27:20 AM
    Author     : fmoctezuma
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>SIO | Procedatos | Asistencia</title>
        <link rel="shortcut icon" href="media/images/favicon.ico" />
        <style type="text/css" title="currentStyle">
            @import "media/css/Plantilla.css";
            @import "media/css/Login.css";
            *{
                margin: 0 ;
            }
            html, body{
                height: 100%;
            }
            section{
                min-height: calc(100% - (135px + 6em));
                height: auto !important;
            }
            section article {
                width: 30%;
                padding: 1em;
                margin: auto;
            }
        </style>
        <script src="media/js/include.js"></script>
        <script src="media/js/librerias.js"></script>
        <script src="media/js/validation/validateAsistencia.js"></script>
    </head>
    <body>
        <header>
            <div id="top">
                <a href="<%= (this.getServletContext().getContextPath().equals("")?"/":this.getServletContext().getContextPath()) %>"><img src="media/images/logo.png" alt="Soluciones Integrales de Oficina" border="0"  class="logo" title="Soluciones Integrales de Oficina"/></a>	
            </div>
        </header>
        <section>
            <article>
                <form action="AsistenciaServlet" class="contacto" method="POST" id="frmlogin">  
                    <div class="titulo"><p class="titulo">ASISTENCIA</p><p class="titulo">PROCEDATOS</p></div>
                    <div><label>Usuario:</label><input tabindex="1"  name="login" id="login" type='text' class="usuario" value='' placeholder="Usuario" required/></div>
                    <div><label>Clave:</label><input tabindex="2"  name="clave" id="clave" type='password' value='' placeholder="Contraseña" required/></div>
                    <div align="right"><label><a href="OlvidoContrasena.htm">¿No puedes iniciar sesión?</a></label></div>   
                    <div><center><input type='submit' tabindex="3" value='Aceptar'  id="btnConsultar" name="btnConsultar"/></center></div>
                    <div><label id="mensaje" class="LabelError"></label></div>
                </form>
            </article>
        </section>
        <footer>
            <div id="footer">
                <p class="">SIO S.A. | Cali: Cra 100 # 14 - 96 Barrio Ciudad Jardín PBX: (57 2) 485 5757 - (572) 485 5758 </p>
                <p class="">Colombia</p>
            </div>
        </footer>
    </body>
</html>
