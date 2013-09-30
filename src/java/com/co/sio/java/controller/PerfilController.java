package com.co.sio.java.controller;

import com.co.sio.java.mbeans.PerfilBeans;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

public class PerfilController extends SimpleFormController {
    public PerfilController() {
        setCommandClass(PerfilBeans.class);
        setCommandName("frmPerfiles");        
    }

    @Override
    protected ModelAndView onSubmit(HttpServletRequest request,
            HttpServletResponse response, Object command,
            BindException errors) throws Exception {return new ModelAndView("Seguridad/FRMPerfiles");}
}
