/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.co.sio.java.servlet;

import com.co.sio.java.JSON.JSONObject;
import com.co.sio.java.dao.ParametroDao;
import com.co.sio.java.dao.PerfilDao;
import com.co.sio.java.dao.PersonaDao;
import com.co.sio.java.dao.UsuarioDao;
import com.co.sio.java.mbeans.ParametroBeans;
import com.co.sio.java.mbeans.PersonaBeans;
import com.co.sio.java.mbeans.UsuarioBeans;
import com.co.sio.java.utils.EnvioMail;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author fmoctezuma
 */
@WebServlet(name = "PersonaServlet", urlPatterns = {"/PersonaServlet"})
public class PersonaServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int intpage = new Integer(request.getParameter("page"));
            int limit = new Integer(request.getParameter("rows"));

            String sidx = request.getParameter("sidx");
            String sord = request.getParameter("sord");

            PersonaDao daopersona = new PersonaDao();


            String json = daopersona.getListPersonas(intpage, limit, sidx, sord);

            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Cache-Control", "no-cache,must-revalidate");
            response.setHeader("Pragma", "no-cache");

            response.getWriter().print(json);
            response.getWriter().close();
        } catch (Exception ex) {
            Logger.getLogger(PersonaServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            PersonaBeans personaBeans = new PersonaBeans();
            PersonaDao daoPersona = new PersonaDao();
            UsuarioBeans usuariobean = new UsuarioBeans();
            UsuarioDao daousuario = new UsuarioDao();
            PerfilDao daoperfil = new PerfilDao();

            String oper = request.getParameter("oper");
            switch (oper.charAt(0)) {
                case 'r': {//Reestablecer
                    usuariobean = daousuario.consultar(Integer.parseInt(request.getParameter("idpersona")));
                    usuariobean.setClave(usuariobean.getLogin());
                    daousuario.actualizar(usuariobean);
                }
                break;
                case 'i': {
                    String id = request.getParameter("id");
                    if (!id.equals("")) {
                        usuariobean = daousuario.consultar(Integer.parseInt(id));
                        usuariobean.setActivo(Integer.parseInt(request.getParameter("estado")));
                        personaBeans = daoPersona.consultar(Integer.parseInt(request.getParameter("id")));
                        personaBeans.setSitiotrabajo(Integer.parseInt(request.getParameter("sitio")));
                        boolean Guardar = daoPersona.Guardar(personaBeans);
                        UsuarioBeans digita = daousuario.consultar((Integer) request.getSession().getAttribute("id_usuario"));
                        usuariobean.setIdUsuarioDigita(digita.getIdusuario());
                        daousuario.Guardar(usuariobean);
                    }
                }
                break;
                case 'c': {//Consultar
                    personaBeans = daoPersona.consultarxIdenditifcacion(request.getParameter("identificacion"));

                    String confirmacion = "false";
                    if (personaBeans.getIdpersona() != 0) {
                        confirmacion = "true";
                    }
                    response.setContentType("text/plain");
                    response.setCharacterEncoding("UTF-8");
                    response.getWriter().write(confirmacion);

                }
                break;
                case 'g': {

                    personaBeans = daoPersona.consultar(Integer.parseInt(request.getParameter("idusuario")));
                    ParametroBeans param = new ParametroDao().consultar(5);

                    double valorDia = personaBeans.getSalario() / Integer.parseInt(param.getValparametro());

                    JSONObject jsono = new JSONObject(personaBeans);
                    response.setContentType("application/json");
                    response.setCharacterEncoding("utf-8");
                    response.setHeader("Pragma", "no-cache");
                    response.getWriter().write(Double.toString(valorDia));

                }
                break;
                default: {//Guardar o Editar
                    personaBeans.setIdentificacion(request.getParameter("identificacion"));
                    personaBeans.setpNombre(request.getParameter("pnombre"));
                    personaBeans.setsNombre(request.getParameter("snombre"));
                    personaBeans.setpApellido(request.getParameter("papellido"));
                    personaBeans.setsApellido(request.getParameter("sapellido"));
                    personaBeans.setFechaNacimiento(request.getParameter("fechaNac"));
                    personaBeans.setSexo(request.getParameter("sexo"));
                    personaBeans.setDireccion(request.getParameter("direccion"));
                    personaBeans.setTelefono(request.getParameter("telefono"));
                    personaBeans.setCelular(request.getParameter("celular"));
                    personaBeans.setEmail(request.getParameter("email"));
                    personaBeans.setSalario(Integer.parseInt(request.getParameter("salario")));
                    personaBeans.setCodEstadoCivil(Integer.parseInt(request.getParameter("estadocivil")));
                    personaBeans.setSitiotrabajo(Integer.parseInt(request.getParameter("sitio")));
                    personaBeans.setFechaIngreso(request.getParameter("fechaIngreso"));
                    personaBeans.setFechaRetiro(request.getParameter("fechaRetiro"));
                    personaBeans.setNocturno(Integer.parseInt(request.getParameter("horario")));
                    personaBeans.setAplicaAuxilio(Integer.parseInt(request.getParameter("auxilioT")));
                   if (oper.charAt(0) == 'a') {
                        usuariobean.setLogin(personaBeans.getIdentificacion());
                        usuariobean.setClave(personaBeans.getIdentificacion());
                        usuariobean.setActivo(Integer.parseInt(request.getParameter("estado")));
                        usuariobean.setIncluidonomina(Integer.parseInt(request.getParameter("incluidoNomina")));
                    } else if (oper.charAt(0) == 'e') {
                        personaBeans.setIdpersona(Integer.parseInt(request.getParameter("id")));
                        usuariobean = daousuario.consultar(Integer.parseInt(request.getParameter("id")));//request.getParameter("idpersona")
                        usuariobean.setActivo(Integer.parseInt(request.getParameter("estado")));
                        usuariobean.setIncluidonomina(Integer.parseInt(request.getParameter("incluidoNomina")));
                    }

                    HttpSession sesion = request.getSession();
                    Object attrib = sesion.getAttribute("id_usuario");

                    UsuarioBeans digita;

                    if (attrib != null) {
                        digita = daousuario.consultar(Integer.parseInt(attrib.toString()));
                    } else {
                        digita = daousuario.consultar(1);
                    }

                    usuariobean.setIdUsuarioDigita(digita.getIdusuario());
                    usuariobean.setPerfil(daoperfil.consultar(Integer.parseInt(request.getParameter("codPerfil"))));
                    personaBeans.setUsuario(usuariobean);

                    daoPersona.Guardar(personaBeans);
                    usuariobean.setIdusuario(daoPersona.consultarId(personaBeans));
                    usuariobean.setIdUsuarioDigita(digita.getIdUsuarioDigita());
                    usuariobean.setUsuariosos(request.getParameter("usuariocliente"));
                    daoPersona.actualizarFechaDigita(digita, usuariobean);
                    usuariobean.setLogin(personaBeans.getIdentificacion());
                    daousuario.Guardar(usuariobean);

                    if (oper.charAt(0) == 'a') {
                        //  EnvioMail.enviaEmailRegistro("Nuevo Registro", personaBeans.getEmail());
                    } else if (oper.charAt(0) == 'e') {
                        //  EnvioMail.enviaEmailRegistro("Modificación de Registro", personaBeans.getEmail());
                    }
                    response.setContentType("application/json");
                    response.setCharacterEncoding("utf-8");
                    response.setHeader("Pragma", "no-cache");
                    response.getWriter().print(true);

                }
            }
        } catch (Exception e) {
            Logger.getLogger(PersonaServlet.class.getName()).log(Level.SEVERE, null, e);
        }
    }
}
