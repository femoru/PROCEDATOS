/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.co.sio.java.servlet;

import com.co.sio.java.JSON.JSONObject;
import com.co.sio.java.dao.LoginDao;
import com.co.sio.java.mbeans.UsuarioBeans;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

/**
 *
 * @author fmoctezuma
 */
@WebServlet(name = "AsistenciaServlet", urlPatterns = {"/AsistenciaServlet"})
public class AsistenciaServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String oper = request.getParameter("oper");
        if(oper==null){
            registrarAsistencia(request, response);
        }else{
            switch(oper.charAt(0)){
                case 'c':{
                    asistenciaAbierta(request, response);
                }break;
            }
        }
    }
    
    private void registrarAsistencia(HttpServletRequest request, HttpServletResponse response){
        try {

            UsuarioBeans usuarioBeans = new UsuarioBeans();
            usuarioBeans.setLogin(request.getParameter("login"));
            usuarioBeans.setClave(request.getParameter("clave"));
            LoginDao dao = new LoginDao();
            usuarioBeans = dao.validarLogin(request.getParameter("login"), request.getParameter("clave"));
            JSONObject json = new JSONObject();
            if( usuarioBeans.getMensaje()==null){
                Object [] asistencia = dao.registraAsistencia(usuarioBeans.getIdusuario());
                if(!((Boolean)asistencia[0]).booleanValue()){
                    usuarioBeans.setMensaje("Fallo al registrar asistencia\nIntente de nuevo");
                }else{
                    if(((String)asistencia[1]).equals("in")){
                        json.put("msj", "Bienvenido");
                    }else{
                        json.put("msj", "Hasta Luego!");
                        usuarioBeans.setMensaje("Hasta Luego!");
                    }
                }
            }
            json.put("mensaje", usuarioBeans.getMensaje()==null?"":usuarioBeans.getMensaje());
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Cache-Control", "no-cache,must-revalidate");
            response.setHeader("Pragma", "no-cache");
            response.getWriter().print(json);
            
        } catch (Exception ex) {
            Logger.getLogger("Error").warn(ex.getMessage());
        }
    }

    private void asistenciaAbierta(HttpServletRequest request, HttpServletResponse response){
        try{
            JSONObject json = new JSONObject();
            json.put("resp", new LoginDao().asistenciaAbierta(Integer.parseInt(request.getParameter(""))));
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Cache-Control", "no-cache,must-revalidate");
            response.setHeader("Pragma", "no-cache");
            response.getWriter().print(json);
        }catch(Exception ex){
            Logger.getLogger("Error").warn(ex.getMessage());
        }
    }
}
