<%--
Views should be stored under the WEB-INF folder so that
they are not accessible except through controller process.

This JSP is here to provide a redirect to the dispatcher
servlet but should be the only JSP outside of WEB-INF.
--%>
<%@page import="com.co.sio.java.db.ControllerPool"%>

<%  ControllerPool.getDs(); //response.sendRedirect("control.htm");%>
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
                margin: auto;
                min-height: calc(100% - (135px + 6em));
                height: auto !important;
                text-align: center;
            }
            section article {
                width: 250px;
                height: 250px;
                padding: 8.5em 1em;
                margin: auto;
                display: inline-block;
            }
            a.button{
                vertical-align: middle;
                font-size: 1.2em;
            }
            #reloj{
                display: block;
                position: absolute;
                top: 10px;
                right: 10px;
                color: white;
                font-size: 1.2em;
            }
        </style>
        <script src="media/js/jquery/jquery-1.9.1.js" ></script>
        <script language="JavaScript">
            
            function mueveReloj(){

                var hora;
                $.ajax({
                    url:'ArchivosPlanosServlet',
                    type:'get',
                    success:function(data){
                        hora = "Hora servidor : " + data.fecha + " " + data.hora;
                        $('#reloj').text(hora);
                    },error:function(data){
                        console.log(JSON.stringify(data));
                    }
                });
                setTimeout("mueveReloj()",1000);
            }
        </script> 
    </head>
    <body onload="mueveReloj();">
        <header>
            <div id="top">
                <a href="<%= (this.getServletContext().getContextPath().equals("")?"/":this.getServletContext().getContextPath()) %>"><img src="media/images/logo.png" alt="Soluciones Integrales de Oficina" border="0"  class="logo" title="Soluciones Integrales de Oficina"/></a>	
                <div style="float: right">
                    <span id="reloj"></span>
                </div>
            </div>
        </header>
        <section>
            <article>
                <a class="button" href="Asistencia.htm"><img src="media/images/folder-clock-icon.png"/> Asistencia</a>
            </article>
            <article>
                <a class="button" href="control.htm"><img src="media/images/folder-public-icon.png"/> Registros</a>
            </article>
            <article>
                <a class="button" href="RegistroES.htm"><img src="media/images/contacts-icon.png"/> Labores</a>
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
