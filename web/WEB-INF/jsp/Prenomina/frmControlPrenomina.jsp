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
            @import "media/css/Prenomina.css";
        </style>
        <script src="media/js/generales/funcionesPrenomina.js"></script>

    </head>
    <body>
        <%
            if (null == session.getAttribute("usuario")) {

                response.sendRedirect("control.htm");
            }
        %>

        <div class="formul"> 
            <center>
                <br>  
                <h2><span>Control de Prenomina</span></h2>  
                <br><br>  

                <div id="dateFilter" style=" width: 80%;">
                    <label>Fecha Inicial: </label><input type="text" id="dateIni" /> - 
                    <label>Fecha Final: </label><input type="text" id="dateFin" />
                    <input type="button" id="reversar" value="Reversar"/>
                </div>
                    
                <br>
                <div style=" width: 80%;"><input type="button" id="cargar" value="Consultar Nomina"/></div>
                <br>
                <table class="fondo">

                    <tr><td align="center" class="titulos">1. Validación de Registros.</td>
                        <td align="center" class="titulos">2. Generar Prenomina.</td>
                        <td align="center" class="titulos">3. RRHH.</td>
                        <td align="center" class="titulos">4.Cierre Prenomina.</td>
                    </tr>               
                    <tr>
                        <td>
                            <div class="container1">
                                <ul id="progress1" >
                                    <li >       
                                        <div id="layer1"  ></div>
                                        <div id="layer7" ></div>
                                        <img width='102%' height='102%' style='display:none;' src="" />
                                    </li>
                                </ul>
                            </div>                            
                        </td>
                        <td>
                            <div class="container2">
                                <ul id="progress2">
                                    <li>                                        
                                        <div id="layer1"  ></div>
                                        <div id="layer7" ></div>
                                        <img width='102%' height='102%' style='display:none;' src="" >
                                    </li>
                                </ul>
                            </div>                            
                        </td>
                        <td>
                            <div class="container3">
                                <ul id="progress3">
                                    <li>                                        
                                        <div id="layer1"  ></div>
                                        <div id="layer7" ></div>
                                        <img width='102%' height='102%' style='display:none;' src="" >
                                    </li>
                                </ul>
                            </div>                            
                        </td>
                        <td>
                            <div class="container4">
                                <ul id="progress4">
                                    <li>                                        
                                        <div id="layer1"  ></div>
                                        <div id="layer7" ></div>
                                        <img width='102%' height='102%' style='display:none;' src="" >
                                    </li>
                                </ul>
                            </div>                            
                        </td>


                    </tr>
                    <tr><td><br></td></tr>
                    <tr><td align="center"><a id="registros" href="javascript: void(0)">Ver Registros</a></td>
                        <td align="center"><a id="prenomina" href="javascript: void(0)">Ver Prenomina</a></td>
                        <td align="center"><a id="novedades" href="javascript: void(0)">Ver Novedades</a></td>
                        <td align="center"></td>
                    </tr>                    
                    <tr><td><br></td></tr>

                </table>                
            </center>
        </div>
    </body>
</html>
