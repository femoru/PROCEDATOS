<%-- 
    Document   : frmConsultaNomina
    Created on : 8/08/2013, 11:41:47 AM
    Author     : fmoctezuma
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <script src="media/js/grid/GRDConsultaNomina.js" ></script>
    </head>
    <body>
        <%
            if (null == session.getAttribute("usuario")) {
                response.sendRedirect("control.htm");
            }
        %>
        <div id="bodyLeft">
            <br>
            <h2><span>Consulta De Nómina</span> </h2>
            <br><br>
            <div class="grilla">
                <table id='grid'> </table>
                <div id='pager'></div>
            </div>  
        </div>
    </body>
</html>
