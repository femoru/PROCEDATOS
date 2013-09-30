package com.co.sio.java.controller;

import com.co.sio.java.dao.*;
import com.co.sio.java.mbeans.*;
import com.co.sio.java.utils.EnvioMail;
import com.co.sio.java.utils.SeguridadUtils;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

/**
 *
 * @author jcarvajal
 */
public class RecuperarContrasenaController extends SimpleFormController {

    public RecuperarContrasenaController() {
        setCommandClass(PersonaBeans.class);
        setCommandName("frmRecuperar");
    }

    @Override
    protected ModelAndView onSubmit(HttpServletRequest request,
            HttpServletResponse response, Object command,
            BindException errors) throws Exception {

        PersonaBeans PersonaBeans = (PersonaBeans) command;
        PersonaDao daoPersona = new PersonaDao();
        PersonaBeans PersonaE = new PersonaBeans();

        if (request.getParameter("btn").equals("Recuperar")) {
            PersonaBeans persona = daoPersona.consultarxIdenditifcacion(PersonaBeans.getIdentificacion());
            if (persona.getIdpersona() != 0) {
                if (persona.getEmail().toUpperCase().equals(PersonaBeans.getEmail().toUpperCase())) {
                    String pass = SeguridadUtils.desencripta(daoPersona.recuperarClave(persona));
                    EnvioMail.enviaEmailContrasena("Recuperación de contraseña", persona.getEmail(), persona);
                } else {
                    PersonaE.setMensaje("El correo ingresado no coincide con el registrado  en el sistema.");
                    return new ModelAndView("Datos/frmOlvidoContrasena", "frmRecuperar", PersonaE);
                }
            } else {
                PersonaE.setMensaje("Por favor verifique la información registrada.");
                return new ModelAndView("Datos/frmOlvidoContrasena", "frmRecuperar", PersonaE);
            }
        }
        return new ModelAndView("redirect:home.htm");
    }
}
