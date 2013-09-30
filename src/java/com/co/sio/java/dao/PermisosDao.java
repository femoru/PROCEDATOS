/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.co.sio.java.dao;

import com.co.sio.java.db.ControllerPool;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.HashMap;

/**
 *
 * @author fmoctezuma
 */
public class PermisosDao {

    private ControllerPool BD;

    public PermisosDao() {
        BD = new ControllerPool();
    }

    public String listarPermisos(String perfil) throws Exception {
        String json = "";
        if (perfil == null) {
            perfil = "0";
        }
        try {

            String sqlPadres = "SELECT codgrupopadre, desgrupo FROM cgrupos WHERE codgrupopadre = codgrupo ORDER BY 1";
            String sqlHojas = "SELECT codgrupo, desgrupo FROM cgrupos CGR WHERE codgrupopadre <> codgrupo ORDER BY 1";
            String sqlPermisos = "SELECT cper.codpermiso, cgr.codgrupo, cgr.codgrupopadre, cper.desaccion, cper.activo, cperper.codperfil "
                    + "FROM cgrupos cgr "
                    + "LEFT JOIN cpermisos cper ON cgr.codgrupo = cper.codgrupo "
                    + "LEFT JOIN cpermisosperfiles cperper ON cper.codpermiso = cperper.codpermiso "
                    + "AND cperper.codperfil = ? "
                    + "ORDER BY 3, 2, 1";
            BD.conectar();

            BD.callableStatement(sqlPadres);
            BD.consultar();
            ResultSet padres = BD.obtenerConsulta();

            BD.callableStatement(sqlHojas);
            BD.consultar();
            ResultSet hojas = BD.obtenerConsulta();

            BD.callableStatement(sqlPermisos);
            BD.AsignarParametro(1, perfil, 2);
            BD.consultar();
            ResultSet permisos = BD.obtenerConsulta();

            HashMap mapPadres = new HashMap();
            ResultSetMetaData md = padres.getMetaData();
            while (padres.next()) {
                for (int i = 0; i < md.getColumnCount(); i++) {
                    mapPadres.put(padres.getInt("codgrupopadre"), padres.getString("desgrupo"));
                }
            }

            HashMap mapHojas = new HashMap();
            md = hojas.getMetaData();
            while (hojas.next()) {
                for (int i = 0; i < md.getColumnCount(); i++) {
                    mapHojas.put(hojas.getInt("codgrupo"), hojas.getString("desgrupo"));
                }
            }
            boolean rc = false;

            json += "[\n";

            int codgrupopadre, codgrupo, codpermiso, padreAnt = 0, grupoAnt = 0, contadorSubmenu = 0;
            String desaccion, codperfil;
            boolean activo, raizAbierta = false, nuevoNodo = false;
            while (permisos.next()) {
                codgrupopadre = permisos.getInt("codgrupopadre");
                codgrupo = permisos.getInt("codgrupo");
                desaccion = permisos.getString("desaccion");
                codpermiso = permisos.getInt("codpermiso");
                activo = permisos.getBoolean("activo");
                codperfil = permisos.getString("codperfil");


                if (codgrupopadre != padreAnt) {
                    if (rc) {
                        if (raizAbierta) {
                            //cerrar Raiz OK
                            while (contadorSubmenu > 0) {
                                json += "]}";
                                contadorSubmenu--;
                            }
                            raizAbierta = false;
                        }
                        //Cerrar Menu Anterior OK
                        json += "]}";
                        json += ",\n";
                    }
                    //nuevo menu OK
                    json += "{\"title\": \"" + mapPadres.get(codgrupopadre) + "\" ,\"key\": \"GP" + codgrupopadre + "\" ,\"isFolder\": \"true\" , \"expand\": \"true\","
                            + "\n\"children\": [ ";
                    nuevoNodo = false;
                }
/////////////////SUBMENUS//////////////////////
                if ((codgrupo != grupoAnt && mapHojas.get(codgrupo) != null)) {
                    if (contadorSubmenu > 0 && codgrupo != grupoAnt) {
                        json += "]}";
                        contadorSubmenu--;
                    }

                    if (rc && nuevoNodo) {
                        json += ",";
                    }
                    json += "\n{\"title\": \"" + mapHojas.get(codgrupo) + "\" ,\"key\": \"GP" + codgrupopadre + "\" ,\"isFolder\": \"true\" , \"expand\": \"true\","
                            + "\n\"children\": [ ";
                    raizAbierta = true;
                    contadorSubmenu++;
                    nuevoNodo = false;
                }

                if (activo) {
                    if (rc && nuevoNodo) {
                        json += ",";
                    }
                    json += "\n{\"title\": \"" + desaccion + "\" ,\"key\": \"" + codpermiso + "\" ";
                    if (perfil.equals(codperfil)) {
                        json += ", \"select\": true";
                    }
                    json += "}";
                    nuevoNodo = true;
                }

                padreAnt = codgrupopadre;
                grupoAnt = codgrupo;
                rc = true;
            }



            if (raizAbierta) {
                json += "]}";
            }
            json += "]}]";

            return json;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            BD.desconectar();
        }
    }
}
