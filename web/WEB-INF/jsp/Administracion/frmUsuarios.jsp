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
        <style type="text/css">
            #users select{
                height: 100%;
                width: 100%;
            }
        </style>
        <script src="media/js/grid/GRDUsuarios.js" language="javascript" type="text/javascript"></script>
    </head>
    <body>
        <%
            if (null == session.getAttribute("usuario")) {

                response.sendRedirect("control.htm");
            }
        %>
        <div id="bodyLeft">
            <br>
            <h2><span>Administración de Usuarios</span> </h2>
            <br><br>
            <div id="grilla" class="grilla" style="margin: 0 auto">
                <table id='grid1'> </table>
                <div id='pager1'></div>
            </div>  
            <div id="users">
                <h5>Información personal</h5>
                <div>
                    <table>
                        <tr>
                            <td>Identificación: </td>
                        </tr>
                        <tr>
                            <td><input id="identificacion" class="requerido" placeholder="Cedula" maxlength="20"></td>
                        </tr>
                        <tr>
                            <td>Primer Nombre: </td>
                            <td>Segundo Nombre: </td>
                            <td>Primer Apellido: </td>
                            <td>Segundo Apellido: </td>
                        </tr>
                        <tr>
                            <td><input id="pnombre" class="requerido" placeholder="Primer Nombre" maxlength="20"/></td>
                            <td><input id="snombre" placeholder="Segundo Nombre" maxlength="30"/></td>
                            <td><input id="papellido" class="requerido" placeholder="Primer Apellido" maxlength="20"/></td>
                            <td><input id="sapellido" placeholder="Segundo Apellido" maxlength="30"/></td>
                        </tr>
                        <tr>
                            <td>Fecha Nacimiento </td>
                            <td>Sexo </td>
                            <td>Estado Civil</td>
                        </tr>
                        <tr>
                            <td><input id="fechaNac" class="datepicker requerido" placeholder="dd/mm/yyyy"></td>
                            <td><select id="sexo"><option value="M">Hombre</option><option value="F">Mujer</option></select></td>
                            <td><select id="estadocivil"></select></td>
                        </tr>
                    </table>
                </div>
                <h5>Datos de contacto</h5>
                <div>
                    <table>
                        <tr>
                            <td>Dirección </td>
                            <td>Correo </td>
                            <td>Telefono </td>
                            <td>Celular </td>
                        </tr>
                        <tr>
                            <td><input id="direccion" class="requerido" placeholder="Dirección" maxlength="50"/></td>
                            <td><input id="email" class="requerido" placeholder="Correo" maxlength="100"/></td>
                            <td><input id="telefono" class="numero requerido" placeholder="Telefono" maxlength="10"/></td>
                            <td><input id="celular" class="numero requerido" placeholder="Celular" maxlength="10"/></td>
                        </tr>
                    </table>
                </div>
                <h5>Datos Contrato</h5>
                <div>
                    <table>
                        <tr>
                            <td>Perfil </td>
                            <td>Sitio de Trabajo </td>
                            <td>Turno </td>
                            <td>Estado </td>
                        </tr>
                        <tr>
                            <td><select id="codPerfil"></select></td>
                            <td><select id="sitio"></select></td>
                            <td><select id="horario" ><option value="0">Diurno</option><option value="1">Nocturno</option></select></td>
                            <td><select id="estado" ><option value="1">Activo</option><option value="0">Inactivo</option></select></td>
                        </tr>
                        <tr>
                            <td>Usuario en cliente </td>
                            <td>Fecha Ingreso </td>
                            <td>Fecha Retiro </td>
                            <td>Salario </td>
                        </tr>
                        <tr>
                            <td><input id="usuariocliente"  placeholder="Usuario" maxlength="20"/></td>
                            <td><input id="fechaIngreso" class="datepicker requerido" placeholder="Fecha Ingreso"/></td>
                            <td><input id="fechaRetiro" class="datepicker" placeholder="Fecha Retiro"/></td>
                            <td><input id="salario"  class="numero requerido" placeholder="Salario" maxlength="9"/></td>
                        </tr>
                    </table>

                </div>
            </div>
        </div>
    </body>
</html>