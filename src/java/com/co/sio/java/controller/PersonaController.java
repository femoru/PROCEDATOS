package com.co.sio.java.controller;

import com.co.sio.java.mbeans.PersonaBeans;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

/**
 *
 * @author fmoctezuma
 */
public class PersonaController extends SimpleFormController {

    public PersonaController() {
        setCommandClass(PersonaBeans.class);
        setCommandName("frmUsuarios");
    }

    @Override
    protected ModelAndView onSubmit(HttpServletRequest request,
            HttpServletResponse response, Object command,
            BindException errors) throws Exception {return null;} 
}
