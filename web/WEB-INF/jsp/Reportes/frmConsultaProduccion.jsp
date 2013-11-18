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
        <script src="media/js/grid/GRDProduccion.js"></script>
    </head>
    <body>
        <%
            if (null == request.getSession().getAttribute("usuario")) {
                response.sendRedirect("index.htm");
            }
        %>
        <div id="bodyLeft">
            <br>
            <h2><span>Consulta de Producción</span> </h2>
            <br>
            <table><tr><td id="tr_documento">Documento: <input id="documento"><input type="button" value="Consultar" id="consulta"></td><td>
                        <div id="dateFilter">
                            Fecha Inicial: <input type="text" id="dateIni"  readonly/> - Fecha Final: <input type="text" id="dateFin" readonly />
                        </div>
                        <div id="nomina_enc">
                            <label for="nomina">Nomina: </label><select id="nomina" style="margin: 0"></select>
                        </div>
                    </td>                        
                    <td style="text-align: right">
                        <div><label>Estado de registro </label><select id="comboEstado" >
                                <option value="0">Sin Validar</option>
                                <option value="2" selected="selected">Validado</option>  
                                <option value="3" >Pagados</option>
                                <option value="4" >Anulados</option>                                   
                            </select></div>
                    </td></tr>
                <tr><td colspan="3">
                        <div id="grilla" class="grilla" style="width: max-content">
                            <table id='gridProd'> </table>
                            <div id='pagerProd'></div>
                        </div> <br> 
                    </td></tr></table>
        </div>
    </body>
</html>
