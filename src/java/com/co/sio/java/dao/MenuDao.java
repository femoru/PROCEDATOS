/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.co.sio.java.dao;

import com.co.sio.java.db.*;
import com.co.sio.java.mbeans.PerfilBeans;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.HashMap;

/**
 *
 * @author jcarvajal
 */
public class MenuDao {

    private ControllerPool BD;

    public MenuDao() {
        BD = new ControllerPool();
    }

    public String generarMenu(PerfilBeans perfil) throws Exception {
        String estructura;
        try {
            BD.conectar();


            String sqlMenu = "SELECT codgrupo, desgrupo FROM cgrupos WHERE codgrupopadre = codgrupo ORDER BY 1";
            String sqlSubMenu = "SELECT codgrupo, desgrupo FROM cgrupos CGR WHERE codgrupopadre <> codgrupo ORDER BY 1";

            String sql = "SELECT   cgru.codgrupopadre, cgru.codgrupo, desaccion, descomando "
                    + "FROM cpermisos cper "
                    + "left JOIN cpermisosperfiles cperper ON cper.codpermiso = cperper.codpermiso "
                    + "left JOIN cperfiles cperfil ON cperfil.codperfil = cperper.codperfil "
                    + "left JOIN cgrupos cgru ON cgru.codgrupo = cper.codgrupo "
                    + "where cperper.codperfil = ? "
                    + "ORDER BY 1, 2, 3";

            BD.callableStatement(sql);
            BD.AsignarParametro(1, Integer.toString(perfil.getCod_perfil()), 2);
            BD.consultar();
            ResultSet datoSql = BD.obtenerConsulta();

            BD.callableStatement(sqlMenu);
            BD.consultar();
            ResultSet menus = BD.obtenerConsulta();

            BD.callableStatement(sqlSubMenu);
            BD.consultar();
            ResultSet subMenus = BD.obtenerConsulta();

            HashMap mapMenus = new HashMap();
            ResultSetMetaData md = menus.getMetaData();
            while (menus.next()) {
                for (int i = 0; i < md.getColumnCount(); i++) {
                    mapMenus.put(menus.getInt("codgrupo"), menus.getString("desgrupo"));
                }
            }

            HashMap mapSubMenus = new HashMap();
            md = subMenus.getMetaData();
            while (subMenus.next()) {
                for (int i = 0; i < md.getColumnCount(); i++) {
                    mapSubMenus.put(subMenus.getInt("codgrupo"), subMenus.getString("desgrupo"));
                }
            }

            String desaccion, descomando;
            int codgrupopadre, codgrupo, padreAnt = 0, grupoAnt = 0;
            int ordenCabeza = 2, ordenHijo = 1, ordensubHijo = 1, contadorSubmenu = 0;
            boolean rc = false, raizAbierta = false, nuevoNodo = false;

            estructura = "domMenu_data.set('domMenu_main', new Hash(\n";
            estructura += "1, new Hash('contents','INICIO','uri','home.htm'),\n";

            while (datoSql.next()) {
                codgrupopadre = datoSql.getInt("codgrupopadre");
                codgrupo = datoSql.getInt("codgrupo");
                desaccion = datoSql.getString("desaccion");
                descomando = datoSql.getString("descomando");

                if (codgrupopadre != padreAnt) {
                    if (rc) {
                        if (raizAbierta) {
                            //cerrar Raiz OK
                            while (contadorSubmenu > 0) {
                                estructura += ")";
                                contadorSubmenu--;
                            }
                            raizAbierta = false;
                        }
                        //Cerrar Menu Anterior OK
                        estructura += ")";
                        estructura += ",\n";
                    }
                    //nuevo menu OK
                    estructura += ordenCabeza++ + ", new Hash('contents','" + mapMenus.get(codgrupopadre) + "',\n";
                    ordenHijo = 1;
                    ordensubHijo = 1;

                    nuevoNodo = false;
                }
/////////////////SUBMENUS//////////////////////
                if (codgrupo != grupoAnt && (mapSubMenus.get(codgrupo) != null)) {
                    //if ((codgrupo != grupoAnt && codgrupopadre == padreAnt) || (codgrupo != grupoAnt && codgrupopadre == grupoAnt) || (codgrupo != grupoAnt && codgrupopadre != grupoAnt)) {
                    if (contadorSubmenu > 0 && codgrupo != grupoAnt) {
                        estructura += ")";
                        contadorSubmenu--;
                    }
                    if (rc && ordenHijo > 1) {
                        estructura += ",";
                    }

                    //nuevo submenu OK
                    if (mapSubMenus.get(codgrupo) != null) {
                        estructura += ordenHijo++ + ", new Hash('contents','" + mapSubMenus.get(codgrupo) + "',\n";
                    }
                    raizAbierta = true;
                    //ordenSubmenu = ordenHijo;
                    contadorSubmenu++;
                    ordensubHijo = 1;

                    nuevoNodo = false;
                }

                if (rc && nuevoNodo) {
                    estructura += ",";
                }
                //nuevo nodo

                estructura += ordensubHijo++ + ", new Hash('contents','" + desaccion + "','uri','" + descomando + "')\n";

                if (!raizAbierta) {
                    ordenHijo++;
                }

                nuevoNodo = true;

                padreAnt = codgrupopadre;
                grupoAnt = codgrupo;
                rc = true;
            }

            if (raizAbierta) {
                estructura += "))";
            }
            estructura += ")));\n";





            estructura += "domMenu_settings.set('domMenu_main', new Hash( "
                    + "'subMenuWidthCorrection', -1, "
                    + "'verticalSubMenuOffsetX', -1, "
                    + "'verticalSubMenuOffsetY', -1, "
                    + "'horizontalSubMenuOffsetX', domLib_isOpera ? 0 : 1, "
                    + "'horizontalSubMenuOffsetY', domLib_isOpera ? -1 : 0, "
                    + "'openMouseoverMenuDelay', 100, "
                    + "'closeMouseoutMenuDelay', 300, "
                    + "'expandMenuArrowUrl', 'media/images/arrow.gif'));";



        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            BD.desconectar();
        }
        return estructura;
    }

    public String generarMenuJquery(PerfilBeans perfil) throws Exception {
        String estructura;
        try {
            BD.conectar();


            String sqlMenu = "SELECT codgrupo, desgrupo FROM cgrupos WHERE codgrupopadre = codgrupo ORDER BY 1";
            String sqlSubMenu = "SELECT codgrupo, desgrupo FROM cgrupos CGR WHERE codgrupopadre <> codgrupo ORDER BY 1";

            String sql = "SELECT   cgru.codgrupopadre, cgru.codgrupo, desaccion, descomando "
                    + "FROM cpermisos cper "
                    + "left JOIN cpermisosperfiles cperper ON cper.codpermiso = cperper.codpermiso "
                    + "left JOIN cperfiles cperfil ON cperfil.codperfil = cperper.codperfil "
                    + "left JOIN cgrupos cgru ON cgru.codgrupo = cper.codgrupo "
                    + "where cperper.codperfil = ? and cper.activo = 1 "
                    + "ORDER BY 1, 2, 3";

            BD.callableStatement(sql);
            BD.AsignarParametro(1, Integer.toString(perfil.getCod_perfil()), 2);
            BD.consultar();
            ResultSet datoSql = BD.obtenerConsulta();

            BD.callableStatement(sqlMenu);
            BD.consultar();
            ResultSet menus = BD.obtenerConsulta();

            BD.callableStatement(sqlSubMenu);
            BD.consultar();
            ResultSet subMenus = BD.obtenerConsulta();

            HashMap mapMenus = new HashMap();
            ResultSetMetaData md = menus.getMetaData();
            while (menus.next()) {
                for (int i = 0; i < md.getColumnCount(); i++) {
                    mapMenus.put(menus.getInt("codgrupo"), menus.getString("desgrupo"));
                }
            }

            HashMap mapSubMenus = new HashMap();
            md = subMenus.getMetaData();
            while (subMenus.next()) {
                for (int i = 0; i < md.getColumnCount(); i++) {
                    mapSubMenus.put(subMenus.getInt("codgrupo"), subMenus.getString("desgrupo"));
                }
            }

            String desaccion, descomando;
            int codgrupopadre, codgrupo, padreAnt = 0, grupoAnt = 0;
            int ordenCabeza = 2, ordenHijo = 1, ordensubHijo = 1, contadorSubmenu = 0;
            boolean rc = false, raizAbierta = false, nuevoNodo = false;

//            estructura = "domMenu_data.set('domMenu_main', new Hash(\n";
//            estructura += "1, new Hash('contents','INICIO','uri','home.htm'),\n";
            estructura = "<li><a href=\"home.htm\">INICIO</a></li>\n";

            while (datoSql.next()) {
                codgrupopadre = datoSql.getInt("codgrupopadre");
                codgrupo = datoSql.getInt("codgrupo");
                desaccion = datoSql.getString("desaccion");
                descomando = datoSql.getString("descomando");

                if (codgrupopadre != padreAnt) {
                    if (rc) {
                        if (raizAbierta) {
                            //cerrar Encabezados OK
                            while (contadorSubmenu > 0) {
                                estructura += "</ul></li>\n";
                                contadorSubmenu--;
                            }
                            raizAbierta = false;
                        }
                        //Cerrar Menu Anterior OK 
                        estructura += "</ul></li>\n";

                    }
                    //nuevo menu OK
                    estructura += "<li><a href=\"javascript:void(0)\">" + mapMenus.get(codgrupopadre) + "</a><ul>\n";
                    ordenHijo = 1;
                    ordensubHijo = 1;

                    nuevoNodo = false;
                }
/////////////////SUBMENUS//////////////////////
                if (codgrupo != grupoAnt && (mapSubMenus.get(codgrupo) != null)) {
                    //if ((codgrupo != grupoAnt && codgrupopadre == padreAnt) || (codgrupo != grupoAnt && codgrupopadre == grupoAnt) || (codgrupo != grupoAnt && codgrupopadre != grupoAnt)) {
                    if (contadorSubmenu > 0 && codgrupo != grupoAnt) {
                        estructura += "</ul>\n";
                        contadorSubmenu--;
                    }
                    if (rc && ordenHijo > 1) {
//                        estructura += ",";
                    }

                    //nuevo submenu OK
                    if (mapSubMenus.get(codgrupo) != null) {
                        estructura += "<li><a href=\"javascript:void(0)\">" + mapSubMenus.get(codgrupo) + "</a><ul>\n";
                    }
                    raizAbierta = true;
                    //ordenSubmenu = ordenHijo;
                    contadorSubmenu++;
                    ordensubHijo = 1;

                    nuevoNodo = false;
                }

                if (rc && nuevoNodo) {
                    // estructura += ",";
                }
                //nuevo nodo

                estructura += "<li onclick=\"loadNewPage('" + descomando + "'); \"><a href=\"javascript:void(0)\">" + desaccion + "</a></li>\n";

                if (!raizAbierta) {
                    ordenHijo++;
                }

                nuevoNodo = true;

                padreAnt = codgrupopadre;
                grupoAnt = codgrupo;
                rc = true;
            }

            while (raizAbierta) {
                estructura += "</ul></li>";
            }
            estructura += "</ul></li>\n";


        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            BD.desconectar();
        }
        return estructura;
    }
}
