package com.co.sio.java.controller;

import com.co.sio.java.dao.PersonaDao;
import com.co.sio.java.dao.UsuarioDao;
import com.co.sio.java.mbeans.PersonaBeans;
import com.co.sio.java.utils.SeguridadUtils;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

/**
 *
 * @author jcarvajal
 */
public class ActualizarDatosController extends SimpleFormController {

    public ActualizarDatosController() {
        setCommandClass(PersonaBeans.class);
        setCommandName("frmActualizaDatos");
    }

    @Override
    protected Map referenceData(HttpServletRequest request) throws Exception {
        Map referenceData = new HashMap();
        PersonaBeans persona = new PersonaBeans();
        PersonaDao personadao = new PersonaDao();
        HttpSession session = request.getSession();

        if (null == session.getAttribute("usuario")) {
            referenceData.put("frmActualizaDatos", persona);

        } else {
            int idusuario = (Integer) request.getSession().getAttribute("id_usuario");
            persona = personadao.consultar(idusuario);
        }

        String pass = SeguridadUtils.desencripta(persona.getUsuario().getClave());
        persona.setClave(pass);
        referenceData.put("frmActualizaDatos", persona);

        return referenceData;
    }

    @Override
    protected ModelAndView onSubmit(
            HttpServletRequest request,
            HttpServletResponse response,
            Object command,
            BindException errors) throws Exception {

        PersonaDao personaDao = new PersonaDao();
        UsuarioDao usuarioDao = new UsuarioDao();
        PersonaBeans pacienteCambio = (PersonaBeans) command;
        PersonaBeans paciente = new PersonaBeans();
        int idusuario = (Integer) request.getSession().getAttribute("id_usuario");
        pacienteCambio.setIdpersona(idusuario);

        if (request.getParameter("btn").equals("Actualizar")) {
            personaDao.ActualizarDatosContacto(pacienteCambio);
        } else if (request.getParameter("btn").equals("Cambiar")) {
            HttpSession sesion = request.getSession();
            String captcha = sesion.getAttribute("key").toString();
            if (pacienteCambio.getUsuario().getImagen().toUpperCase().equals(captcha.toUpperCase())) {
                usuarioDao.CambiarClave(Integer.toString(pacienteCambio.getIdpersona()), pacienteCambio.getUsuario().getClave());
                paciente.setMensaje("Se Cambio Satisfactoriamente su clave");
            } else {
                paciente = pacienteCambio;
                paciente.setMensaje("La verificacion de los caracteres no es valida");
                return new ModelAndView("Pacientes/frmCambioClave", "frmActualizaDatos", paciente);
            }
        }
        return new ModelAndView("redirect:home.htm");
    }
}