/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.co.sio.java.utils;

import com.co.sio.java.JSON.JSONArray;
import com.co.sio.java.JSON.JSONException;
import com.co.sio.java.JSON.JSONObject;
import com.co.sio.java.dao.PerfilDao;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 *
 * @author jcarvajal
 */
public class Utils {

    public static JSONArray convert(JSONArray cabeceras, ResultSet rs)
            throws SQLException, JSONException, Exception {
        JSONArray json = new JSONArray();
        ResultSetMetaData rsmd = rs.getMetaData();
        int numColumns = rsmd.getColumnCount();
        Map obj;
        JSONObject jsono;
        String column_name;
        if (numColumns != cabeceras.length()) {
            throw new Exception("la longitud de la cabecera no coincide con la cantidad de registros");
        }
        while (rs.next()) {

            obj = new LinkedHashMap();

            for (int i = 1; i < numColumns + 1; i++) {
//                column_name = rsmd.getColumnName(i);
                column_name = cabeceras.getString(i - 1);
                if (rsmd.getColumnType(i) == java.sql.Types.ARRAY) {
                    obj.put(column_name, rs.getArray(i));
                } else if (rsmd.getColumnType(i) == java.sql.Types.BIGINT) {
                    obj.put(column_name, rs.getInt(i));
                } else if (rsmd.getColumnType(i) == java.sql.Types.BOOLEAN) {
                    obj.put(column_name, rs.getBoolean(i));
                } else if (rsmd.getColumnType(i) == java.sql.Types.BLOB) {
                    obj.put(column_name, rs.getBlob(i));
                } else if (rsmd.getColumnType(i) == java.sql.Types.DOUBLE) {
                    obj.put(column_name, rs.getDouble(i));
                } else if (rsmd.getColumnType(i) == java.sql.Types.FLOAT) {
                    obj.put(column_name, rs.getFloat(i));
                } else if (rsmd.getColumnType(i) == java.sql.Types.INTEGER) {
                    obj.put(column_name, rs.getInt(i));
                } else if (rsmd.getColumnType(i) == java.sql.Types.NVARCHAR) {
                    obj.put(column_name, rs.getNString(i));
                } else if (rsmd.getColumnType(i) == java.sql.Types.VARCHAR) {
                    obj.put(column_name, rs.getString(i));
                } else if (rsmd.getColumnType(i) == java.sql.Types.TINYINT) {
                    obj.put(column_name, rs.getInt(i));
                } else if (rsmd.getColumnType(i) == java.sql.Types.SMALLINT) {
                    obj.put(column_name, rs.getInt(i));
                } else if (rsmd.getColumnType(i) == java.sql.Types.DATE) {
                    obj.put(column_name, rs.getDate(i));
                } else if (rsmd.getColumnType(i) == java.sql.Types.TIMESTAMP) {
                    obj.put(column_name, rs.getTimestamp(i));
                } else {
                    obj.put(column_name, rs.getObject(i));
                }
            }

            jsono = new JSONObject(obj);
            json.put(jsono);
        }

        return json;
    }

    public static String padRight(String cadena, char valor, int cantidad) {
        return String.format("%" + cantidad + "s", cadena).replace(' ', valor);
    }

    public static String padLeft(String cadena, char valor, int cantidad) {
        return String.format("%-" + cantidad + "s", cadena).replace(' ', valor);
    }

    public static Map cargarListas() throws Exception {

        Map referenceData = new HashMap();
        PerfilDao daoperfil = new PerfilDao();

        List perfilesList = daoperfil.listar();

        referenceData.put("perfilesList", perfilesList);

        return referenceData;
    }

    /**
     * Lee el contenido textual desde un stream de entrada
     *
     * @param input Stream de entrada
     * @param encoding Codificación
     * @return El contenido del stream de entrada
     * @throws IOException Cualquier excepción de Entrada/Salida
     */
    public static String fileToString(InputStream input, String encoding) throws IOException {
        StringWriter sw = new StringWriter();
        InputStreamReader in = new InputStreamReader(input, encoding);

        char[] buffer = new char[1024 * 2];
        int n = 0;
        while (-1 != (n = in.read(buffer))) {
            sw.write(buffer, 0, n);
        }
        return sw.toString();
    }

    public static long DateToTimeStampUnix(String Fecha) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = df.parse(Fecha);
        long epoch = date.getTime();
        return epoch;
    }

    public static FileOutputStream resulSetToExcel(JSONArray cabeceras, ResultSet rs, File nomArchivo) {

        try {
            //Crear el libro
            HSSFWorkbook libro = new HSSFWorkbook();
            //Crear una hoja
            HSSFSheet hoja = libro.createSheet();
            int rownum = 0;
            ResultSetMetaData rsmd = rs.getMetaData();
            int colTotal = rsmd.getColumnCount();
            //Crear instancia de fila
            HSSFRow fila = hoja.createRow(rownum++);
            if (cabeceras != null) {
                for (int i = 0; i < cabeceras.length(); i++) {

                    fila.createCell(i).setCellValue(cabeceras.getString(i));
                }
            } else {
                for (int i = 1; i <= colTotal; i++) {
                    fila.createCell(i - 1).setCellValue(rsmd.getColumnName(i));
                }
            }

            while (rs.next()) {
                fila = hoja.createRow(rownum);

                for (int i = 1; i <= colTotal; i++) {
                    if (rsmd.getColumnType(i) == java.sql.Types.VARCHAR) {
                        fila.createCell(i - 1).setCellValue(rs.getString(i));
                    } else if (rsmd.getColumnType(i) == java.sql.Types.NVARCHAR) {
                        fila.createCell(i - 1).setCellValue(rs.getString(i));
                    } else if (rsmd.getColumnType(i) == java.sql.Types.DATE) {
                        fila.createCell(i - 1).setCellValue(rs.getString(i));
                    } else if (rsmd.getColumnType(i) == java.sql.Types.NUMERIC) {
                        fila.createCell(i - 1).setCellValue(rs.getDouble(i));
                    }
                }

                rownum++;
            }

            FileOutputStream archivo = new FileOutputStream(nomArchivo);
            libro.write(archivo);
            archivo.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static JSONArray llenarGrilla(ResultSet datoSql) throws Exception {
        JSONArray jsonRows = new JSONArray();

        int countColumn = 2;
        JSONObject jsono;
        JSONArray jsona;
        int totalColumnas = datoSql.getMetaData().getColumnCount();
        while (datoSql.next()) {
            jsono = new JSONObject();
            jsona = new JSONArray();
            jsono.put("id", datoSql.getString(1));
            while (countColumn <= totalColumnas) {
                jsona.put(datoSql.getString(countColumn++));
            }
            jsono.put("cell", jsona);
            jsonRows.put(jsono);
            countColumn = 2;
        }
        return jsonRows;
    }
    /*
     public static void subirArchivo(SolicitudHCBeans Archivos) throws Exception {
     CommonsMultipartFile formato = Archivos.getFormato();
     File guardarFormato = new File("C:\\prueba\\" + formato.getOriginalFilename());

     CommonsMultipartFile cedula = Archivos.getCedula();
     File guardarCedula = new File("C:\\prueba\\" + cedula.getOriginalFilename());

     FileOutputStream os = null;
     try {

     if (!Archivos.getCedula().equals("")) {
     os = new FileOutputStream(guardarFormato);
     os.write(formato.getBytes());
     }

     if (!Archivos.getFormato().equals("")) {
     os = new FileOutputStream(guardarCedula);
     os.write(cedula.getBytes());
     }

     if (!Archivos.getTercero().equals("")) {
     CommonsMultipartFile tercero = Archivos.getTercero();
     File guardartercero = new File("C:\\prueba\\" + tercero.getOriginalFilename());
     os = new FileOutputStream(guardartercero);
     os.write(cedula.getBytes());
     }

     if (!Archivos.getCedulaTercero().equals("")) {
     CommonsMultipartFile cedulaTercero = Archivos.getCedulaTercero();
     File guardarcedulaTercero = new File("C:\\prueba\\" + cedulaTercero.getOriginalFilename());
     os = new FileOutputStream(guardarcedulaTercero);
     os.write(cedula.getBytes());
     }
     } finally {
     if (os != null) {
     try {
     os.close();
     } catch (IOException e) {
     throw new Exception(e.getMessage());
     }
     }
     }
     }*/
}
