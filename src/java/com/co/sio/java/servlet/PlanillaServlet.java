/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.co.sio.java.servlet;

import com.co.sio.java.JSON.JSONArray;
import com.co.sio.java.JSON.JSONException;
import com.co.sio.java.JSON.JSONObject;
import com.co.sio.java.dao.LaboresDao;
import com.co.sio.java.dao.PersonaDao;
import com.co.sio.java.dao.PlanillaDao;
import com.co.sio.java.dao.UsuarioDao;
import com.co.sio.java.dao.UsuarioLaboresDao;
import com.co.sio.java.mbeans.LaborBeans;
import com.co.sio.java.mbeans.PersonaBeans;
import com.co.sio.java.mbeans.PlanillaBeans;
import com.co.sio.java.mbeans.UsuarioBeans;
import com.co.sio.java.mbeans.UsuarioLaboresBeans;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.catalina.core.ApplicationPart;
import org.apache.log4j.Logger;

/**
 *
 * @author fmoctezuma
 */
@WebServlet(name = "PlanillaServlet", urlPatterns = {"/PlanillaServlet"})
@MultipartConfig()
public class PlanillaServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int intpage = new Integer(request.getParameter("page"));
            int limit = new Integer(request.getParameter("rows"));

            int idpersona = Integer.parseInt(request.getParameter("idpersona"));
            int idlabor = Integer.parseInt(request.getParameter("labor"));
            String mes = request.getParameter("mes");

            String json;
            PlanillaDao planillaDao;
            planillaDao = new PlanillaDao();

            json = planillaDao.getListPlanilla(idpersona, idlabor, mes, intpage, limit);

            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Cache-Control", "no-cache,must-revalidate");
            response.setHeader("Pragma", "no-cache");

            response.getWriter().print(json);
        } catch (Exception ex) {
            System.err.println(ex);
            Logger.getLogger("ERROR").warn("Error Mostrando Planilla", ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String oper = request.getParameter("oper");

            PlanillaDao planillaDao = new PlanillaDao();

            switch (oper.charAt(0)) {

                case 'c': {
                    JSONObject jsono = new JSONObject();
                    String cedula = request.getParameter("cedula");
                    PersonaBeans personaBeans = new PersonaDao().consultarxIdenditifcacion(cedula.trim());
                    boolean existe = personaBeans.getIdpersona() != 0;

                    int perfil = 0;
                    if (existe) {
                        UsuarioBeans usuarioBeans = new UsuarioDao().consultar(personaBeans.getIdpersona());
                        perfil = usuarioBeans.getPerfil().getCod_perfil();
                    }

                    int sitio = personaBeans.getSitiotrabajo();

                    if (!existe) {
                        jsono.put("idpersona", 0);
                        jsono.put("nombre", "Auxiliar no existe");
                    } else if (perfil != 4) {
                        jsono.put("idpersona", 0);
                        jsono.put("nombre", "No es un Auxiliar");
                    } else if (sitio == 0) {
                        jsono.put("idpersona", 0);
                        jsono.put("nombre", "Auxiliar debe estar fuera de las oficinas de SIO");
                    } else {
                        jsono.put("idpersona", personaBeans.getIdpersona());
                        jsono.put("nombre", personaBeans.getNombres());
                    }

                    response.setContentType("application/json");
                    response.setCharacterEncoding("utf-8");
                    response.getWriter().println(jsono.toString());

                }
                break;
                case 'd': {
                    int idplanilla = Integer.parseInt(request.getParameter("id"));
                    response.getWriter().print(planillaDao.eliminar(idplanilla));
                }
                break;
                case 'l': {
                    int idusuario = Integer.parseInt(request.getParameter("idusuario"));
                    int idlabor = Integer.parseInt(request.getParameter("idlabor"));
                    UsuarioLaboresBeans beans = new UsuarioLaboresBeans();
                    LaborBeans laborBeans = new LaborBeans();
                    laborBeans.setIdlaborcontrato(idlabor);
                    UsuarioBeans usuarioBeans = new UsuarioBeans();
                    usuarioBeans.setIdusuario(idusuario);
                    beans.setLabor(laborBeans);
                    beans.setUsuario(usuarioBeans);

                    UsuarioLaboresDao dao = new UsuarioLaboresDao();
                    beans = dao.consultar(beans);

                    JSONObject jsono = new JSONObject();
                    jsono.put("inicial", beans.getFechaInicio());
                    jsono.put("final", beans.getFechaFin());
                    response.setContentType("application/json");
                    response.setCharacterEncoding("utf-8");
                    response.getWriter().println(jsono.toString());

                }
                break;
                case 'v': {
                    int idpersona = Integer.parseInt(request.getParameter("idpersona"));
                    int idlabor = Integer.parseInt(request.getParameter("labor"));
                    String mes = request.getParameter("mes");

                    int validadas = planillaDao.validarPlanilla(idpersona, idlabor, mes, (String) request.getSession().getAttribute("cedula"));
                    JSONArray jsona = new JSONArray();
                    if (validadas > 0) {
                        jsona.put("Existen " + validadas + " registros que no se pudieron ingresar por que se cruzan con registros existentes, informe al coordinador");
                    } else {
                        jsona.put("Se validaron todos los registros");
                    }
                    response.setContentType("application/json");
                    response.setCharacterEncoding("utf-8");
                    response.getWriter().println(jsona.toString());
                }
                break;
                case 'p': {
                    ApplicationPart p = (ApplicationPart) request.getPart("consolidado");
                    response.setContentType("application/json");
                    response.setCharacterEncoding("utf-8");
                    response.getWriter().println(verificarPlano(p).toString());
                }
                break;
                default: {

                    int idlabor = Integer.parseInt(request.getParameter("labor"));
                    int idusuario = Integer.parseInt(request.getParameter("idusuario"));
                    LaborBeans laborBeans = new LaboresDao().consultar(idlabor);

                    String fecha = request.getParameter("dia") + request.getParameter("mes");
                    String hInicioD = acomodarHoras(request.getParameter("hInicioD"));
                    String hDescD = acomodarHoras(request.getParameter("hDescD"));
                    String hReinicioD = acomodarHoras(request.getParameter("hReinicioD"));
                    String hFinalD = acomodarHoras(request.getParameter("hFinalD"));

                    int registros = Integer.parseInt(request.getParameter("registros"));

                    PlanillaBeans planillaBeans = new PlanillaBeans();
                    planillaBeans.setFechalabor(fecha.trim());
                    planillaBeans.setHoraInicioD(hInicioD);
                    planillaBeans.setHoraDescansoD(hDescD);
                    planillaBeans.setHoraReinicioD(hReinicioD);
                    planillaBeans.setHoraFinalD(hFinalD);
                    planillaBeans.setRegistrosLabor(registros);
                    planillaBeans.setTiempoDiurno(intervaloMinutos(fecha + hInicioD, fecha + hDescD));
                    planillaBeans.setTiempoTarde(intervaloMinutos(fecha + hReinicioD, fecha + hFinalD));
                    planillaBeans.setIdusuario(idusuario);
                    planillaBeans.setIdlaborcontrato(idlabor);
                    planillaBeans.setValor(Integer.parseInt(laborBeans.getValor()));
                    planillaBeans.setCosto(Integer.parseInt(laborBeans.getCosto()));
                    JSONArray jsona = new JSONArray();
                    boolean resp;
                    boolean existen;

                    if (oper.charAt(0) == 'a') {

                        existen = planillaDao.consultarRango(planillaBeans) > 0;
                        if (existen) {
                            jsona.put(false);
                            jsona.put("Existen registros que se cruzan con las horas ingresadas");
                        } else {
                            resp = planillaDao.Guardar(planillaBeans);
                            jsona.put(resp);
                            if (resp) {
                                jsona.put("");
                            } else {
                                jsona.put("No se pudo ingresar el resgistro, verifique el dia");
                            }
                        }
                    } else if (oper.charAt(0) == 'e') {
                        int idplanilla = Integer.parseInt(request.getParameter("id"));
                        planillaBeans.setIdplanilla(idplanilla);
                        existen = planillaDao.consultarRango(planillaBeans) > 0;
                        if (existen) {
                            jsona.put(false);
                            jsona.put("Existen registros que se cruzan con las horas ingresadas");
                        } else {
                            resp = planillaDao.Actualizar(planillaBeans);
                            jsona.put(resp);
                            if (resp) {
                                jsona.put("");
                            } else {
                                jsona.put("No se pudo ingresar el resgistro, verifique el dia");
                            }
                        }
                    }
                    response.setContentType("application/json");
                    response.setCharacterEncoding("utf-8");
                    response.getWriter().println(jsona);
                }
                break;
            }

        } catch (Exception e) {
            System.out.println(e.getClass());
            for (int i = 0; i < 4; i++) {

                System.out.println(
                        "LINE: " + Integer.toString(e.getStackTrace()[i].getLineNumber())
                        + " - "
                        + "CLASS: " + e.getStackTrace()[i].getClassName()
                        + " - "
                        + "METHOD: " + e.getStackTrace()[i].getMethodName()
                        + " - "
                        + "CAUSE: " + e.getLocalizedMessage()
                );
            }
            Logger.getLogger("ERROR").warn("Error de Aplicativo Planillas", e);
        }
    }

    private int intervaloMinutos(String fechaMenor, String fechaMayor) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Date d = df.parse(fechaMenor);
        Date c = df.parse(fechaMayor);
        if (d.compareTo(c) > 0) {
            c.setDate(c.getDate() + 1);
        }
        int minutos = (int) (c.getTime() - d.getTime()) / 1000 / 60;

        return minutos;
    }

    private String acomodarHoras(String hora) {
        StringBuilder cadena = new StringBuilder(hora);
        String signo = ":";
        if (cadena.length() == 3) {
            cadena.insert(0, "0");
        }
        cadena.insert(2, signo);

        return cadena.toString();
    }

    private JSONArray verificarPlano(ApplicationPart part) throws JSONException, Exception {
        JSONArray resp = new JSONArray();
        if (part.getSize() > 0) {
            String filename = part.getSubmittedFileName();
            String extension = filename.substring(filename.indexOf("."), filename.length());
            if (!extension.equals(".xls")) {
                resp.put(0, false);
                resp.put(1, "Extension inapropiada para el archivo");
                return resp;
            }
            try {
                PlanillaDao dao = new PlanillaDao();
                JSONObject datos = dao.subirPlano(part.getInputStream());
                boolean respPlano = !datos.getBoolean("error");
                resp.put(0, respPlano);
                resp.put(1, respPlano ? "Archivo procesado" : "Error procesando");
                resp.put(2, datos);
                return resp;
            } catch (IOException ex) {
                resp.put(0, false);
                resp.put(1, "Error al leer el archivo");
                return resp;
            }
        } else {
            resp.put(0, false);
            resp.put(1, "Tamaño inapropiado para el archivo");
            return resp;
        }

    }
}
