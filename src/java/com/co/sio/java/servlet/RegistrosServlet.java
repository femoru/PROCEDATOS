/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.co.sio.java.servlet;

import com.co.sio.java.JSON.JSONObject;
import com.co.sio.java.dao.*;
import com.co.sio.java.mbeans.*;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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
@WebServlet(name = "RegistrosServlet", urlPatterns = {"/RegistrosServlet"})
public class RegistrosServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        RegistrosDao registrodao = new RegistrosDao();
        String json;
        try {
            String identificacion = request.getParameter("login");
            if (identificacion != null) {
                String clave = request.getParameter("clave");
                LoginDao dao = new LoginDao();
                UsuarioBeans usuarioBeans = dao.validarLogin(identificacion, clave);
                JSONObject jsono = new JSONObject();
                
                if(!dao.asistenciaAbierta(usuarioBeans.getIdusuario())){
                    usuarioBeans.setMensaje("El usuario no ha registrado la asistencia para dia en curso");
                }
                
                if (usuarioBeans.getMensaje() == null) {
                    PersonaBeans pbean = (new PersonaDao()).consultar(usuarioBeans.getIdusuario());

                    jsono.put("idusuario", usuarioBeans.getIdusuario());
                    int idusuario = usuarioBeans.getIdusuario();

                    RegistroBeans registroBeans = registrodao.consultar(idusuario);

                    jsono.put("getFechaFin", "" + registroBeans.getFechaFin());
                    jsono.put("getIdregistro", registroBeans.getIdregistro());

                    if (registroBeans.getFechaFin() != null || registroBeans.getIdregistro() == 0) {
                        jsono.put("abierta", false);
                    } else {
                        jsono.put("abierta", true);
                    }

                    String labor = "";
                    int datolabor = 0;
                    int tipolabor = 0;
                    if (registroBeans.getIdregistro() != 0) {
                        ReferenciasDao referenciasDao = new ReferenciasDao();
                        labor = referenciasDao.consultar(registroBeans.getLabor().getLabor(), "RLABORES").getDescripcion() + " ";
                        labor += referenciasDao.consultar(registroBeans.getLabor().getTipolabor(), "RTIPOLABOR").getDescripcion();

                        datolabor = registroBeans.getLabor().getDatolabor();

                        tipolabor = registroBeans.getLabor().getTipolabor();
                    }
                    jsono.put("tipolabor", tipolabor);
                    jsono.put("labor", labor);
                    jsono.put("requiere", datolabor);
                    jsono.put("error", "null");

                } else {
                    jsono.put("error", usuarioBeans.getMensaje());
                }
                json = jsono.toString();

            } else {
                int intpage = new Integer(request.getParameter("page"));
                int limit = new Integer(request.getParameter("rows"));

                String sidx = request.getParameter("sidx");
                String sord = request.getParameter("sord");
                String id = request.getParameter("id");

                String nomina = request.getParameter("nomina");


                if (id == null && nomina == null) {
                    String fecha = request.getParameter("fecha");

                    if (fecha.equals("undefined")) {
                        json = "{}";
                    } else {
                        int filtro = Integer.parseInt(request.getParameter("filtro"));
                        int estado = Integer.parseInt(request.getParameter("estado"));
                        int periodo = Integer.parseInt(request.getParameter("periodo"));

                        if (periodo != 0) {
                            fecha = request.getParameter("mes");
                        }

                        if (filtro == 0) {
                            int idgrupo = Integer.parseInt(request.getParameter("grupo"));
                            json = registrodao.getListReferencias(intpage, limit, sidx, sord, idgrupo, fecha, estado, periodo);
                        } else {
                            int sitio = Integer.parseInt(request.getParameter("sitio"));
                            json = registrodao.getListRegistrosSitio(intpage, limit, sidx, sord, sitio, fecha, estado, periodo);
                        }
                    }
                } else {

                    if (nomina == null) {
                        json = registrodao.getListRegistrosDatoLabor(intpage, limit, id);
                    } else {
                        json = registrodao.getListRegistros(intpage, limit, id, nomina);
                    }
                }



            }
            response.setContentType("application/json");
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Cache-Control", "no-cache,must-revalidate");
            response.setHeader("Pragma", "no-cache");
            response.setCharacterEncoding("utf-8");
            response.getWriter().print(json);
            response.getWriter().close();
        } catch (Exception ex) {
            Logger.getLogger("Error").warn("Error al cargar", ex);
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String userName = (String) request.getSession().getAttribute("userName");
        try {
            String oper = request.getParameter("oper");

            RegistrosDao registrosDao = new RegistrosDao();
            RegistroBeans registroBeans = new RegistroBeans();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            switch (oper.charAt(0)) {
                case 'r': {//edicion en fila
                    registroBeans = registrosDao.consultar(request.getParameter("id"));
                    Logger.getLogger("REGISTROS").info(String.format("%s va a actualizar el registro \n%s", userName, registroBeans.toString()));
                    registroBeans.setEstado(Integer.parseInt(request.getParameter("est")));
                    if (registrosDao.actualizar(registroBeans)) {
                        Logger.getLogger("REGISTROS").info(String.format("%s se actualizo el registro \n%s", userName, registroBeans.toString()));
                    }
                }
                break;
                case 'a': {//nuevo registro por grilla
                    LaborBeans labor = new LaboresDao().consultar(Integer.parseInt(request.getParameter("labor")));
                    registroBeans.setUsuario(new UsuarioDao().consultar(Integer.parseInt(request.getParameter("auxiliar"))));
                    registroBeans.setLabor(labor);
                    registroBeans.setFechaInicio(request.getParameter("fechas") + " " + request.getParameter("inicio"));
                    registroBeans.setFechaFin(request.getParameter("fechas") + " " + request.getParameter("fin"));
                    registroBeans.setAnulado(new ReferenciasDao().consultar(0, "RCAUSASANULACION"));
                    registroBeans.setObservacion(request.getParameter("observacion"));
                    registroBeans.setDatoLabor(request.getParameter("dato"));
                    registroBeans.setRegistroslabor(Integer.parseInt(request.getParameter("registros").equals("") ? "0" : request.getParameter("registros")));
                    registroBeans.setValor(Integer.parseInt(labor.getValor()));
                    registroBeans.setCosto(Integer.parseInt(labor.getCosto()));
                    registroBeans.setMesFacturar(request.getParameter("mesFacturar"));

                    if (request.getParameter("nomina") != null) {
                        registroBeans.setEstado(2);
                        registroBeans.setIdnomina(Integer.parseInt(request.getParameter("nomina")));
                    }
                    if (registrosDao.insertar(registroBeans)) {
                        Logger.getLogger("REGISTROS").info(String.format("%s ingreso el registro \n%s", userName, registroBeans.toString()));
                    }
                    int ultimoID = registrosDao.consultarUltimoID(registroBeans);
                    registrosDao.horasLaboradas(ultimoID);

                    if (registroBeans.getLabor().getTipolabor() == 4) {
                        registrosDao.imagenesProcesadas(registrosDao.consultar(Integer.toString(ultimoID)));
                    }
                }
                break;
                case 'e': {//editar registro grilla
                    LaborBeans labor = new LaboresDao().consultar(Integer.parseInt(request.getParameter("labor")));
                    registroBeans = registrosDao.consultar(request.getParameter("id"));
                    //Log
                    Logger.getLogger("REGISTROS").info(String.format("%s va a actualizar el registro \n%s", userName, registroBeans.toString()));
                    if (labor != null) {
                        registroBeans.setLabor(labor);
                    } else {
                        labor = registroBeans.getLabor();
                    }
                    
                    Date inicio = sdf.parse(request.getParameter("fechas") + " " + request.getParameter("inicio"));
                    Date fin = sdf.parse(request.getParameter("fechas") + " " + request.getParameter("fin"));
                    
                    if(fin.compareTo(inicio) < 0){
                        Calendar cFin = Calendar.getInstance();
                        cFin.setTime(fin);
                        cFin.set(Calendar.DATE, cFin.get(Calendar.DATE)+1);
                        fin = cFin.getTime();
                    }
                    
                    
                    registroBeans.setFechaInicio(sdf.format(inicio));
                    registroBeans.setFechaFin(sdf.format(fin));
                    registroBeans.setValor(Integer.parseInt(labor.getValor()));
                    registroBeans.setCosto(Integer.parseInt(labor.getCosto()));

                    registroBeans.setDatoLabor(request.getParameter("dato") == null ? "" : request.getParameter("dato"));
                    registroBeans.setObservacion(request.getParameter("observacion"));
                    registroBeans.setRegistroslabor(Integer.parseInt(request.getParameter("registros")));
                    registroBeans.setMesFacturar(request.getParameter("mesFacturar"));

                    if (registrosDao.actualizar(registroBeans)) {
                        Logger.getLogger("REGISTROS").info(String.format("%s actualizo el registro \n%s", userName, registroBeans.toString()));
                    }
                    registrosDao.horasLaboradas(registroBeans.getIdregistro());
                    if (registroBeans.getLabor().getTipolabor() == 4) {
                        registrosDao.imagenesProcesadas(registroBeans);
                    } else {
                    }

                }
                break;
                case 'i': {//entrada registro auxiliar
                    LaborBeans laborBeans = new LaboresDao().consultar(Integer.parseInt(request.getParameter("labor")));
                    int usuario = Integer.parseInt(request.getParameter("usuario"));
                    registroBeans.setUsuario(new UsuarioDao().consultar(usuario));
                    registroBeans.setLabor(laborBeans);
                    registroBeans.setValor(Integer.parseInt(laborBeans.getValor()));
                    registroBeans.setCosto(Integer.parseInt(laborBeans.getCosto()));
                    boolean respuesta = registrosDao.Guardar(registroBeans);
                    if (respuesta) {
                        Logger.getLogger("REGISTROS").info(String.format("%s registro entrada \nidlabor: %s", registroBeans.getUsuario().getLogin(), request.getParameter("labor")));
                    }
                    response.getWriter().print(respuesta);
                }
                break;
                case 'o': {//salida registro auxiliar
                    int usuario = Integer.parseInt(request.getParameter("usuario"));
                    registroBeans = registrosDao.consultar((new UsuarioDao()).consultar(usuario).getIdusuario());

                    if (request.getParameter("dato").equals("")) {
                        registroBeans.setDatoLabor("");
                    } else {
                        registroBeans.setDatoLabor(request.getParameter("dato"));
                    }
                    if (request.getParameter("registros").equals("")) {
                        if (request.getParameter("imagenes").equals("")) {
                            registroBeans.setRegistroslabor(0);
                        } else {
                            registroBeans.setRegistroslabor(Integer.parseInt(request.getParameter("imagenes")));
                        }
                    } else {
                        registroBeans.setRegistroslabor(Integer.parseInt(request.getParameter("registros")));
                    }

                    boolean respuesta;
                    //si labor tipo hora y requiere datolabor registrar la hora 
//                    de salida en mregistros y los datos labor en pregistrodatolabor
                    if ((registroBeans.getLabor().getTipolabor() == 2 && registroBeans.getLabor().getDatolabor() == 1)) {
                        registrosDao.registrarDatoLabor(registroBeans);
                    }

                    if (registroBeans.getFechaFin() == null) {
                        respuesta = registrosDao.registrarSalida(registroBeans);
                        registrosDao.horasLaboradas(registroBeans.getIdregistro());
                    } else {
                        respuesta = registrosDao.insertar(registroBeans);

                    }
                    //}
                    response.getWriter().print(respuesta);
                }
                break;

                case 'v': {//condicion labor valida o no
                    LaborBeans labor = new LaboresDao().consultar(Integer.parseInt(request.getParameter("idlabor")));
                    if (labor != null) {
                        response.getWriter().print(labor.getDatolabor());
                    } else {
                        response.getWriter().print(0);
                    }
                }
                break;
                case 'h': {//Validar horas

                    String idregistro = request.getParameter("id");

                    String fechaInicio = request.getParameter("fechas") + " " + request.getParameter("inicio"),
                            fechaFin = request.getParameter("fechas") + " " + request.getParameter("fin");

                    int respuesta = 0;

                    if (!idregistro.equals("")) {
                        RegistroBeans actual = registrosDao.consultar(idregistro);
                        RegistroBeans fechaInicial = registrosDao.consultarRegistroFecha(actual.getUsuario().getIdusuario(), fechaInicio);
                        RegistroBeans fechaFinal = registrosDao.consultarRegistroFecha(actual.getUsuario().getIdusuario(), fechaFin);

                        if (fechaInicial.getIdregistro() != 0 && actual.getIdregistro() != fechaInicial.getIdregistro()) {
                            respuesta = 1;
                        }
                        if (fechaFinal.getIdregistro() != 0 && actual.getIdregistro() != fechaFinal.getIdregistro()) {
                            respuesta = 2;
                        }
                        Date now = new Date();
                        if (now.compareTo(sdf.parse(fechaFin)) < 0) {
                            respuesta = 3;
                        }

                    } else {

                        String idusuario = request.getParameter("idusuario");
                        RegistroBeans registroFechaInicial = registrosDao.consultarRegistroFecha(Integer.parseInt(idusuario), fechaInicio);
                        RegistroBeans registroFechaFinal = registrosDao.consultarRegistroFecha(Integer.parseInt(idusuario), fechaFin);

                        if (registroFechaInicial.getIdregistro() != 0) {
                            respuesta = 1;
                        }
                        if (registroFechaFinal.getIdregistro() != 0) {
                            respuesta = 2;
                        }

                        Date now = new Date();
                        if (now.compareTo(sdf.parse(fechaFin)) < 0) {
                            respuesta = 3;
                        }


                        registroBeans = registrosDao.consultar(Integer.parseInt(idusuario));
                        if (registroBeans.getIdregistro() != 0 && registroBeans.getFechaFin() == null) {
                            sdf.applyPattern("dd/MM/yyyy");
                            if (sdf.format(now).equals(request.getParameter("fechas"))) {
                                respuesta = 4;
                            }
                        }

                    }
                    response.getWriter().print(respuesta);
                }
                break;
                case 'n': {//RegistroSiguiente
                    registroBeans = registrosDao.consultar(Integer.parseInt(request.getParameter("idusuario")));
                    response.getWriter().print(registroBeans.getFechaFin());
                }
                break;
                case 'd': {// validacion datapicker
                    int filtro = Integer.parseInt(request.getParameter("filtro"));
                    int idgrupo = Integer.parseInt(request.getParameter("grupo"));
                    int sitio = Integer.parseInt(request.getParameter("sitio"));
                    String dia = request.getParameter("dia");
                    int validado;
                    if (filtro == 0) {
                        validado = registrosDao.diaValidadoGrupo(idgrupo, dia);
                    } else {
                        validado = registrosDao.diaValidadoSitio(sitio, dia);
                    }
                    response.getWriter().print(validado);
                }
                break;
                case 't': {// valida todos
                    String fecha = request.getParameter("dia");
                    int filtro = Integer.parseInt(request.getParameter("filtro"));
                    int idgrupo = Integer.parseInt(request.getParameter("grupo"));
                    int sitio = Integer.parseInt(request.getParameter("sitio"));

                    int AntesdeValidar = registrosDao.cuentaValidarTodos(fecha, idgrupo, sitio, filtro, 3);

                    boolean validarTodo = registrosDao.validarTodos(fecha, idgrupo, sitio, filtro);

                    int DespuesdeValidar = registrosDao.cuentaValidarTodos(fecha, idgrupo, sitio, filtro, 2);

                    if (validarTodo && (AntesdeValidar == DespuesdeValidar)) {
                        response.getWriter().print("true");
                    } else {
                        response.getWriter().print("false");
                    }


                }
                break;
                case 'c': {
                    registroBeans = registrosDao.consultar(request.getParameter("id"));
                    ReferenciasBeans anul = new ReferenciasBeans();
                    anul.setCodigo(Integer.parseInt(request.getParameter("anulado")));
                    registroBeans.setAnulado(anul);
                    registrosDao.actualizar(registroBeans);
                }
                break;
                case 'l': {
                    String dato = request.getParameter("dato");
                    String registro = request.getParameter("reg");
                    String datoAnt = request.getParameter("id");
                    registrosDao.editarDatoLabor(registro, dato, datoAnt);
                }
                break;
                case 'j': {
                    String dato = request.getParameter("dato");
                    String registro = request.getParameter("reg");
                    registrosDao.registrarDatoLabor(registro, dato);
                }
                break;
            }
        } catch (Exception ex) {
            Logger.getLogger("Error").warn("Error", ex);
        }
    }
}
