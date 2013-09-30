/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.co.sio.java.servlet;

import com.co.sio.java.dao.ArchivoGuiaDao;
import com.co.sio.java.dao.ArchivosPlanosDao;
import com.co.sio.java.dao.ContratosDao;
import com.co.sio.java.mbeans.ArchivoGuiaBeans;
import com.co.sio.java.mbeans.ArchivoPlanoBeans;
import com.co.sio.java.mbeans.ContratoBeans;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

/**
 *
 * @author fmoctezuma
 */
@WebServlet(name = "ArchivosPlanosServlet", urlPatterns = {"/ArchivosPlanosServlet"})
@MultipartConfig()
public class ArchivosPlanosServlet extends HttpServlet {

    private String msjError;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String cliente = request.getParameter("cliente");
            String labor = request.getParameter("labor");
            String inicio = request.getParameter("inicio");
            String fin = request.getParameter("fin");
            int idusuario = (Integer) request.getSession().getAttribute("id_usuario");
            Part archivo = request.getPart("archivo");

            ArchivosPlanosDao planosDao = new ArchivosPlanosDao();
            ArchivoGuiaDao guiaDao = new ArchivoGuiaDao();
            ContratoBeans contrato = new ContratosDao().consultar(Integer.parseInt(cliente));

            ArchivoPlanoBeans planoBeans = planosDao.consultar(contrato.getCliente().getIdcliente(), Integer.parseInt(labor), inicio, fin);
            planoBeans.setIdCliente(contrato.getCliente().getIdcliente());
            planoBeans.setIdLaborContrato(Integer.parseInt(labor));
            planoBeans.setInicialCarga(inicio);
            planoBeans.setFinalCarga(fin);
            planoBeans.setIdUsuario(idusuario);

            boolean control = planosDao.Guardar(planoBeans);

            if (control) {
                planoBeans = planosDao.consultar(contrato.getCliente().getIdcliente(), Integer.parseInt(labor), inicio, fin);
                int idarchivoplano = planoBeans.getIdArchivo();
                List<ArchivoGuiaBeans> lista = null;
                if (guiaDao.validarEncabezado(Integer.parseInt(labor), inicio, fin)) {
                    lista = analizarArchivo(idarchivoplano, archivo);
                } else {
                    msjError = "No existen registros para conciliar en este rango de fechas";
                }

                response.setContentType("text/html;charset=UTF-8");
                PrintWriter out = response.getWriter();
                if (lista == null) {

                    out.println("<div style='text-align: center'>");
                    out.println("<span style='color: red;'>El archivo no pudo ser procesado, " + msjError + "</span>");
                    out.println("</div>");
                } else {

                    /*ESTA LINEA*/ guiaDao.eliminarGuias(idarchivoplano);
                    int numRegistros = 0;
                    for (int i = 0; i < lista.size(); i++) {
                        numRegistros += lista.get(i).getRegistros();
                        guiaDao.Guardar(lista.get(i));
                    }
                    out.println("<div style='text-align: center'>");
                    out.println("<span style='color: green;'>El archivo fue procesado correctamente</span>");
                    out.println("</div>");
                    planoBeans.setTotalRegistros(numRegistros);
                    planosDao.actualizar(planoBeans);
                }
            }


        } catch (Exception ex) {
            Logger.getLogger(ArchivosPlanosServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private List<ArchivoGuiaBeans> analizarArchivo(int idarchivo, Part archivo) throws Exception {

        List<ArchivoGuiaBeans> listaGuias = new ArrayList<ArchivoGuiaBeans>();

        String separador = ",";
        String patronFechaEntrada = "\\d{4}/\\d{1,2}/\\d{1,2} \\d{1,2}:\\d{1,2}:\\d{1,2}";

        Scanner lector = new Scanner(archivo.getInputStream());

        //String cabeceras[] = {"nmro_ga", "nmro_frmlrs_ga", "fcha_envo_prvdr", "usro_envo_prvdr"};
        int posiciones[] = {3, 6, 7, 8};

        String datos[];
        msjError = "";
        ArchivoGuiaBeans guiaBeans = new ArchivoGuiaBeans();
        guiaBeans.setIdArchivoPlano(idarchivo);
        while (lector.hasNext()) {
            String linea = lector.nextLine();
            while (linea.contains("\"")) {
                linea = linea.replaceAll("\"", "");
            }

            if (!linea.contains(separador)) {
                msjError = "Error en el procesamiento, no se encuentra el separador ( , ) ";
                return null;
            } else {
                datos = linea.split(separador);

                guiaBeans = new ArchivoGuiaBeans();
                guiaBeans.setIdArchivoPlano(idarchivo);

                try {
                    guiaBeans.setGuia(Integer.parseInt(datos[posiciones[0]]));
                } catch (NumberFormatException e) {
                    msjError = "Error en el procesamiento en la linea " + (listaGuias.size()+1) + ""
                            + ", numero de guia no valido";
                    return null;
                } catch (Exception e) {
                    msjError = "Error en el procesamiento en la linea " + (listaGuias.size()+1) + ""
                            + ", no se encuentra el campo numero de guia";
                    return null;
                }

                try {
                    guiaBeans.setRegistros(Integer.parseInt(datos[posiciones[1]]));
                } catch (NumberFormatException e) {
                    msjError = "Error en el procesamiento en la linea " + (listaGuias.size()+1) + ""
                            + ", numero de registros no valido";
                    return null;
                } catch (Exception e) {
                    msjError = "Error en el procesamiento en la linea " + (listaGuias.size()+1) + ""
                            + ", no se encuentra el campo numero de registros";
                    return null;
                }
                String fecha;
                try {
                    fecha = datos[posiciones[2]];
                } catch (Exception e) {
                    msjError = "Error en el procesamiento en la linea " + (listaGuias.size()+1) + ""
                            + ", no se encuentra el campo fecha";
                    return null;
                }
                if (!fecha.matches(patronFechaEntrada)) {
                    msjError = "Error en el procesamiento en la linea " + (listaGuias.size()+1) + ""
                            + ", patron de fechas invalido, verifique el patron yyyy/mm/dd";
                    return null;
                }
                SimpleDateFormat dateFormatString = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Date d = dateFormatString.parse(fecha);
                dateFormatString.applyPattern("dd/MM/yyyy HH:mm");
                fecha = dateFormatString.format(d);

                guiaBeans.setFechaRegistro(fecha);
                try {
                    guiaBeans.setUsuarioSOS(datos[posiciones[3]]);
                } catch (Exception e) {
                    msjError = "Error en el procesamiento en la linea " + (listaGuias.size()+1) + ""
                            + ", no se encuentra el campo nombre usuario"; 
                    return null;
                }

                listaGuias.add(guiaBeans);
            }
        }

        return listaGuias;
    }
}
