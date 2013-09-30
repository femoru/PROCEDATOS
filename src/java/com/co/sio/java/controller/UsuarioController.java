package com.co.sio.java.controller;

import com.co.sio.java.dao.*;
import com.co.sio.java.mbeans.PersonaBeans;
import com.co.sio.java.mbeans.UsuarioBeans;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

/**
 *
 * @author jcarvajal
 */
public class UsuarioController extends SimpleFormController {

    public UsuarioController() {
        setCommandClass(UsuarioBeans.class);
        setCommandName("frmlogin");
    }

    @Override
    protected ModelAndView onSubmit(HttpServletRequest request,
            HttpServletResponse response, Object command,
            BindException errors) throws Exception {

        UsuarioBeans usuarioBeans = (UsuarioBeans) command;
        LoginDao daoLogin = new LoginDao();

        if (request.getParameter("btnConsultar").equals("Aceptar")) {
            usuarioBeans = daoLogin.validarLogin(usuarioBeans.getLogin(), usuarioBeans.getClave());

            if (usuarioBeans.getMensaje() == null) {
                return new ModelAndView("redirect:home.htm");
            } else {
                return new ModelAndView("Login", "frmlogin", usuarioBeans);
            }
        } else {
            return new ModelAndView("redirect:index.htm");
        }
    }
}
