/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.co.sio.java.servlet;

import com.co.sio.java.JSON.JSONArray;
import com.co.sio.java.JSON.JSONObject;
import com.co.sio.java.dao.NovedadesDao;
import com.co.sio.java.dao.ParametroDao;
import com.co.sio.java.mbeans.NovedadBeans;
import com.co.sio.java.mbeans.ParametroBeans;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.catalina.core.ApplicationPart;

/**
 *
 * @author fmoctezuma
 */
@WebServlet(name = "NovedadesServlet", urlPatterns = {"/NovedadesServlet"})
@MultipartConfig()
public class NovedadesServlet extends HttpServlet {

    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String oper = request.getParameter("oper");
            if (!oper.equals("oper")) {
                doPost(request, response);
            } else {
                int intpage = new Integer(request.getParameter("page"));
                int limit = new Integer(request.getParameter("rows"));

                String mes = request.getParameter("mes");

                String json;
                NovedadesDao novedadesDao;
                novedadesDao = new NovedadesDao();

                json = novedadesDao.getListNovedades(mes, intpage, limit);

                response.setContentType("application/json");
                response.setCharacterEncoding("utf-8");
                response.setHeader("Pragma", "no-cache");
                response.setHeader("Cache-Control", "no-cache,must-revalidate");
                response.setHeader("Pragma", "no-cache");

                response.getWriter().print(json);
            }
        } catch (Exception ex) {
            Logger.getLogger(NovedadesServlet.class.getName()).log(Level.SEVERE, null, ex);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            NovedadesDao dao = new NovedadesDao();
            String oper = request.getParameter("oper");
            if (oper == null) {
                registrarNovedad(request, response, dao);
            } else {

                switch (oper.charAt(0)) {
                    case 'c': {
                        int idnovedad = Integer.parseInt(request.getParameter("idnovedad"));
                        NovedadBeans beans = dao.consultar(idnovedad);
                        JSONObject jsono = new JSONObject(beans);
                        response.setContentType("application/json");
                        response.setCharacterEncoding("utf-8");
                        response.setHeader("Pragma", "no-cache");
                        response.getWriter().println(jsono.toString());
                    }
                    break;
                    case 'l': {
                        int idusuario = Integer.parseInt(request.getParameter("idusuario"));
                        int idnovedad = Integer.parseInt(request.getParameter("idnovedad"));
                        List<NovedadBeans> list = dao.listar(idusuario, idnovedad);
                        response.setContentType("text/html;charset=UTF-8");
                        response.getWriter().printf("<option value=\"%s\">%s</option>", 0, "Seleccione Incapacidad");
                        for (NovedadBeans novedadBeans : list) {
                            response.getWriter().printf("<option value=\"%s\">%s - %s</option>",
                                    novedadBeans.getIdnovedad(),
                                    novedadBeans.getFechainicio(),
                                    novedadBeans.getFechafin());
                        }
                    }
                    break;
                    case 'v': {
                        NovedadBeans beans = dao.consultar(Integer.parseInt(request.getParameter("idnovedad")));
                        beans.setIdnovedad(Integer.parseInt(request.getParameter("idnovedad")));
                        beans.setIdusuario(Integer.parseInt(request.getParameter("idusuario")));
                        //beans.setCodnovedad(Integer.parseInt(request.getParameter("codnovedad")));
                        beans.setFechainicio(request.getParameter("dateIni"));
                        beans.setFechafin(request.getParameter("dateFin"));
                        beans.setDias(Integer.parseInt(request.getParameter("dias")));
                        beans.setNroincapacidad(request.getParameter("nroInc"));
                        beans.setCoddiagnostico(request.getParameter("codDx"));
                        beans.setClaseincapacidad(Integer.parseInt(request.getParameter("clsInc")));
                        beans.setFechaaccidente(request.getParameter("dateAcc"));
                        beans.setIndprorroga(Integer.parseInt(request.getParameter("indPro").equals("") ? "0" : request.getParameter("indPro")));
                        beans.setIdnovprorroga(Integer.parseInt(request.getParameter("incAnt")));
                        beans.setNronovcg(Integer.parseInt(request.getParameter("nroIncCg").equals("") ? "0" : request.getParameter("nroIncCg")));
                        beans.setVlrempresa(Integer.parseInt(request.getParameter("vlrSIO")));
                        beans.setVlreps(Integer.parseInt(request.getParameter("vlrEPS")));
                        if (Integer.parseInt(request.getParameter("tipo")) != 1) {
                            beans.setDiasSIO(Integer.parseInt(request.getParameter("diasSIO")));
                        } else {
                            beans.setDiasSIO(Integer.parseInt(request.getParameter("dias")));
                        }
                        beans.setDiasEPS(Integer.parseInt(request.getParameter("diasEPS")));
                        beans.setDiasComp(Integer.parseInt(request.getParameter("diasComp")));
                        beans.setPorcentaje(Double.parseDouble(request.getParameter("prc").equals("") ? "100" : request.getParameter("prc")));

                        response.setContentType("application/json");
                        response.setCharacterEncoding("utf-8");
                        response.setHeader("Pragma", "no-cache");
                        beans.setEstado(2);
                        boolean actualizar = dao.actualizar(beans);
                        response.getWriter().println(actualizar);

                        if (actualizar) {
                            registrosCalculoNomina(beans, dao);
                        }
                    }
                    break;
                    case 'd': {
                        int id = Integer.parseInt(request.getParameter("id"));
                        int anulado = Integer.parseInt(request.getParameter("anulado"));
                        response.setContentType("application/json");
                        response.setCharacterEncoding("utf-8");
                        response.setHeader("Pragma", "no-cache");
                        response.getWriter().println(dao.anular(id, anulado));

                    }
                    break;
                    case 'h': {
                        String fInicial = request.getParameter("fechaInicial");
                        String fFinal = request.getParameter("fechaFinal");

                        response.setContentType("application/json");
                        response.setCharacterEncoding("utf-8");
                        response.getWriter().print(dao.calcularLaborales(fInicial, fFinal));
                    }
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(NovedadesServlet.class.getName()).log(Level.SEVERE, null, ex);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

    }

    public void registrarNovedad(HttpServletRequest request, HttpServletResponse response, NovedadesDao dao) throws Exception {

        String auxiliar = request.getParameter("auxiliar");
        int idusuario;
        if (auxiliar == null) {
            idusuario = (Integer) request.getSession().getAttribute("id_usuario");
        } else {
            idusuario = Integer.parseInt(auxiliar);
        }

        String novedad = request.getParameter("novedad");
        String fechainicio = request.getParameter("dateIni");
        String dias = request.getParameter("dias");
        String fechafin = request.getParameter("dateFin");
        String nroInc = request.getParameter("nroInc");
        String codDx = request.getParameter("codDx");
        String observacion = request.getParameter("observacion");
        ApplicationPart p = (ApplicationPart) request.getPart("sopInc");

        NovedadBeans beans = new NovedadBeans();
        beans.setIdusuario(idusuario);
        beans.setCodnovedad(Integer.parseInt(novedad));
        beans.setFechainicio(fechainicio);
        beans.setFechafin(fechafin);
        beans.setDias(Integer.parseInt(dias));
        beans.setNroincapacidad(nroInc);
        beans.setCoddiagnostico(codDx);
        beans.setObservacion(observacion);
        beans.setPlano(1);
        if (p.getSize() > 0) {
            String archivoNovedad = crearArchivoNovedad(beans, p);
            beans.setArchivo(archivoNovedad);
        } else {
            beans.setArchivo("");
        }


        boolean guardar = dao.Guardar(beans);

        JSONArray array = new JSONArray();
        if (guardar) {
            array.put(guardar);
            array.put("Ingreso novedad correctamente");
            //beans = dao.consultar(beans);
            //registrosCalculoNomina(beans, dao);
        } else {
            array.put(guardar);
            array.put("Ocurrio un error al ingresar la novedad");
        }
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Pragma", "no-cache");
        response.getWriter().print(array.toString());



    }

    private void registrosCalculoNomina(NovedadBeans beans, NovedadesDao dao) throws ParseException, Exception {
        beans.setPlano(0);
        beans.setIdnovprorroga(beans.getIdnovedad());

        int diasSio = beans.getDiasSIO();
        int diasEps = beans.getDiasEPS();
        int diasNovedad = beans.getDias();
        String fechainicio = beans.getFechainicio();
        String fechafin = beans.getFechafin();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        int contadorMeses, contadorAño;

        Calendar inicio = Calendar.getInstance();
        Calendar fin = Calendar.getInstance();
        inicio.setTime(sdf.parse(fechainicio));
        fin.setTime(sdf.parse(fechainicio));

        String[] fechaI = fechainicio.split("/");
        String[] fechaF = fechafin.split("/");
        contadorMeses = Integer.parseInt(fechaF[1]) - Integer.parseInt(fechaI[1]);
        contadorAño = Integer.parseInt(fechaF[2]) - Integer.parseInt(fechaI[2]);
        contadorMeses += contadorAño * 12;
        if (contadorMeses < 0 && contadorAño > 0) {
            contadorMeses *= -1;
            contadorMeses -= 1;
        }
        System.out.println("Meses " + contadorMeses);
        int dias;
        if (contadorMeses == 0) {
            fin.setTime(sdf.parse(fechafin));
            dias = (int) (1 + (fin.getTime().getTime() - inicio.getTime().getTime()) / 1000 / 60 / 60 / 24/*segEndia*/);
            beans.setFechainicio(sdf.format(inicio.getTime()));
            beans.setFechafin(sdf.format(fin.getTime()));

            System.out.println("dias sio " + beans.getDiasSIO());
            System.out.println("dias eps " + beans.getDiasEPS());
            beans.setDias(dias);
            dao.Guardar(beans);
            System.out.println("rango ingresado " + sdf.format(inicio.getTime()) + " - " + sdf.format(fin.getTime()));
            System.out.println("dias ingresados " + dias);
        } else {
            fin.set(Calendar.DATE, inicio.getActualMaximum(Calendar.DAY_OF_MONTH));

            dias = (int) (1 + (fin.getTime().getTime() - inicio.getTime().getTime()) / 1000 / 60 / 60 / 24/*segEndia*/);
            beans.setFechainicio(sdf.format(inicio.getTime()));
            beans.setFechafin(sdf.format(fin.getTime()));
            if (dias < diasSio) {
                diasSio -= dias;
                beans.setDiasSIO(dias);
                beans.setDiasEPS(0);
            } else {
                beans.setDiasSIO(diasSio);
                int diasRestantes = dias - diasSio;
                diasSio = 0;
                if (diasRestantes > diasEps) {
                    diasEps = 0;
                    beans.setDiasEPS(diasEps);
                } else {
                    diasEps -= diasRestantes;
                    beans.setDiasEPS(diasRestantes);
                }

            }
            beans.setDias(dias);
            dao.Guardar(beans);
            System.out.println("rango ingresado " + sdf.format(inicio.getTime()) + " - " + sdf.format(fin.getTime()));
            System.out.println("dias ingresados " + dias);
            contadorMeses--;

            for (int i = contadorMeses; i >= 0; i--) {
                inicio.set(Calendar.DATE, 1);
                inicio.set(Calendar.MONTH, inicio.get(Calendar.MONTH) + 1);

                fin.set(Calendar.MONTH, inicio.get(Calendar.MONTH));
                fin.set(Calendar.YEAR, inicio.get(Calendar.YEAR));
                if (i >= 1) {
                    fin.set(Calendar.DATE, inicio.getActualMaximum(Calendar.DAY_OF_MONTH));
                } else {
                    fin.set(Calendar.DATE, Integer.parseInt(fechaF[0]));
                }
                dias = (int) (1 + (fin.getTime().getTime() - inicio.getTime().getTime()) / 1000 / 60 / 60 / 24/*segEndia*/);
                beans.setFechainicio(sdf.format(inicio.getTime()));
                beans.setFechafin(sdf.format(fin.getTime()));
                if (dias < diasSio) {
                    diasSio -= dias;
                    beans.setDiasSIO(dias);
                    beans.setDiasEPS(0);
                } else {
                    beans.setDiasSIO(diasSio);
                    int diasRestantes = dias - diasSio;
                    if (diasRestantes > diasEps) {
                        diasEps = 0;
                        beans.setDiasEPS(diasEps);
                    } else {
                        diasEps -= diasRestantes;
                        beans.setDiasEPS(diasRestantes);
                    }

                }
                beans.setDias(dias);
                dao.Guardar(beans);
                System.out.println("rango ingresado " + sdf.format(inicio.getTime()) + " - " + sdf.format(fin.getTime()));
                System.out.println("dias ingresados " + dias);
            }

        }
    }

    private String crearArchivoNovedad(NovedadBeans beans, ApplicationPart p) throws Exception {
        ParametroBeans parametroBeans = new ParametroDao().consultar(15);
        File directorio = new File(parametroBeans.getValparametro());
        directorio.mkdirs();
        String extension = p.getSubmittedFileName();
        extension = extension.substring(extension.indexOf("."), extension.length());

        directorio = new File(parametroBeans.getValparametro() + beans.getIdusuario() + "_" + beans.getFechainicio().replaceAll("/", "") + extension);
        directorio.delete();
        if (directorio.createNewFile()) {
            InputStream inputStream = p.getInputStream();

            FileOutputStream salida = new FileOutputStream(directorio);

            int read;
            byte[] bytes = new byte[1024];

            while ((read = inputStream.read(bytes)) != -1) {
                salida.write(bytes, 0, read);
            }
            System.out.println("Creado!");
            salida.close();
            inputStream.close();

        }
        return directorio.getName();
    }
}
