/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.co.sio.java.dao;

import com.co.sio.java.db.ControllerPool;
import com.co.sio.java.mbeans.PersonaBeans;
import com.co.sio.java.mbeans.UsuarioBeans;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author fmoctezuma
 */
public class PersonaDao {

    private ControllerPool BD;

    public PersonaDao() {
        BD = new ControllerPool();
    }

    public boolean actualizarFechaDigita(UsuarioBeans usuarioDigita, UsuarioBeans usuarioNuevo) throws Exception {
        try {
            String sql1 = "UPDATE mpersonas "
                    + "SET idusuario = ?, "
                    + "fechadigita = SYSDATE "
                    + "WHERE idusuario = ?";
            BD.conectar();
            BD.callableStatement(sql1);
            BD.AsignarParametro(1, Integer.toString(usuarioDigita.getIdusuario()), 2);
            BD.AsignarParametro(2, Integer.toString(usuarioNuevo.getIdusuario()), 2);
            return BD.registrar();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            BD.desconectar();
        }
    }

    public PersonaBeans consultarxIdenditifcacion(String identificacion) throws Exception {
        PersonaBeans personabeans = new PersonaBeans();
        try {
            String sql;
            ResultSet datoSql;
            BD.conectar();
            sql = "SELECT m.idpersona, m.identificacion, m.pnombre, m.snombre, m.papellido, "
                    + "m.sapellido, m.fechanacimiento, m.sexo, m.direccion, m.telefono, "
                    + "m.celular, m.email, m.codestadocivil, m.idusuario, m.sitiotrabajo "
                    + "FROM mpersonas m "
                    + "WHERE M.identificacion = ?";
            BD.callableStatement(sql);
            BD.AsignarParametro(1, identificacion, 1);

            if (!BD.consultar()) {
                throw new Exception("Error Realizando La Consulta: " + sql);
            } else {
                datoSql = BD.obtenerConsulta();
                if (datoSql.next()) {
                    personabeans.setIdpersona(datoSql.getInt(1));
                    personabeans.setIdentificacion(datoSql.getString(2));
                    personabeans.setpNombre(datoSql.getString(3));
                    personabeans.setsNombre(datoSql.getString(4));
                    personabeans.setpApellido(datoSql.getString(5));
                    personabeans.setsApellido(datoSql.getString(6));
                    personabeans.setFechaNacimiento(datoSql.getString(7));
                    personabeans.setSexo(datoSql.getString(8));
                    personabeans.setDireccion(datoSql.getString(9));
                    personabeans.setTelefono(datoSql.getString(10));
                    personabeans.setCelular(datoSql.getString(11));
                    personabeans.setEmail(datoSql.getString(12));
                    personabeans.setCodEstadoCivil(datoSql.getInt(13));
                    UsuarioBeans usuariobean = new UsuarioBeans();
                    usuariobean.setIdusuario(datoSql.getInt(14));
                    personabeans.setSitiotrabajo(datoSql.getInt(15));
                    personabeans.setUsuario(usuariobean);
                }
            }
            return personabeans;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            BD.desconectar();
        }
    }

    public PersonaBeans consultar(int id) throws Exception {
        PersonaBeans personabeans = new PersonaBeans();
        try {
            String sql;
            ResultSet datoSql;
            BD.conectar();
            sql = "SELECT m.idpersona, m.identificacion, m.pnombre, m.snombre, m.papellido, "
                    + "m.sapellido, to_char(m.fechanacimiento, 'dd/MM/yyyy'), m.sexo, m.direccion, m.telefono, "
                    + "m.celular, m.email, m.codestadocivil, m.idusuario, cu.clave , m.salario  "
                    + "FROM mpersonas m "
                    + "INNER JOIN cusuarios cu ON cu.idusuario = m.idpersona "
                    + "WHERE M.idpersona = ?";
            BD.callableStatement(sql);
            BD.AsignarParametro(1, Integer.toString(id), 1);

            if (!BD.consultar()) {
                throw new Exception("Error Realizando La Consulta: " + sql);
            } else {
                datoSql = BD.obtenerConsulta();
                if (datoSql.next()) {
                    personabeans.setIdpersona(datoSql.getInt(1));
                    personabeans.setIdentificacion(datoSql.getString(2));
                    personabeans.setpNombre(datoSql.getString(3));
                    personabeans.setsNombre(datoSql.getString(4) == null ? "" : datoSql.getString(4));
                    personabeans.setpApellido(datoSql.getString(5));
                    personabeans.setsApellido(datoSql.getString(6) == null ? "" : datoSql.getString(6));
                    personabeans.setFechaNacimiento(datoSql.getString(7));
                    personabeans.setSexo(datoSql.getString(8));
                    personabeans.setDireccion(datoSql.getString(9));
                    personabeans.setTelefono(datoSql.getString(10));
                    personabeans.setCelular(datoSql.getString(11));
                    personabeans.setEmail(datoSql.getString(12));
                    personabeans.setCodEstadoCivil(datoSql.getInt(13));
                    UsuarioBeans usuariobean = new UsuarioBeans();
                    usuariobean.setIdusuario(datoSql.getInt(14));
                    usuariobean.setClave(datoSql.getString(15));
                    personabeans.setSalario(datoSql.getInt(16));
                    personabeans.setUsuario(usuariobean);
                }
            }
            return personabeans;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            BD.desconectar();
        }
    }

    public boolean Guardar(PersonaBeans persona) throws Exception {
        PersonaBeans CPersona;
        boolean confirmacion;
        try {
            CPersona = consultar(persona.getIdpersona());
            if (CPersona.getIdpersona() == 0) {
                confirmacion = Insertar(persona);
                return confirmacion;
            } else {
                confirmacion = Actualizar(persona);
                return confirmacion;
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    private boolean Insertar(PersonaBeans persona) throws Exception {
        try {
            BD.conectar();
            String sql = "INSERT INTO mpersonas (identificacion, pnombre, snombre, papellido, sapellido, "
                    + "fechanacimiento, sexo, direccion, telefono, celular, email, codestadocivil, salario, idusuario, sitiotrabajo,fechaingreso,fecharetiro) "
                    + "VALUES (?,?,?,?,?,to_date(?, 'dd/MM/yyyy'),?,?,?,?,?,?,?,?,?,to_date(?, 'dd/MM/yyyy'),to_date(?, 'dd/MM/yyyy') )";
            BD.callableStatement(sql);
            BD.AsignarParametro(1, persona.getIdentificacion(), 1);
            BD.AsignarParametro(2, persona.getpNombre().toUpperCase(), 1);
            BD.AsignarParametro(3, persona.getsNombre().toUpperCase(), 1);
            BD.AsignarParametro(4, persona.getpApellido().toUpperCase(), 1);
            BD.AsignarParametro(5, persona.getsApellido().toUpperCase(), 1);
            BD.AsignarParametro(6, persona.getFechaNacimiento(), 1);
            BD.AsignarParametro(7, persona.getSexo(), 1);
            BD.AsignarParametro(8, persona.getDireccion().toUpperCase(), 1);
            BD.AsignarParametro(9, persona.getTelefono(), 1);
            BD.AsignarParametro(10, persona.getCelular(), 1);
            BD.AsignarParametro(11, persona.getEmail().toUpperCase(), 1);
            BD.AsignarParametro(12, Integer.toString(persona.getCodEstadoCivil()), 2);
            BD.AsignarParametro(13, Integer.toString(persona.getSalario()), 2);
            BD.AsignarParametro(14, Integer.toString(persona.getUsuario().getIdUsuarioDigita()), 2);
            BD.AsignarParametro(15, Integer.toString(persona.getSitiotrabajo()), 2);
            BD.AsignarParametro(16, persona.getFechaIngreso(), 1);
            BD.AsignarParametro(17, persona.getFechaRetiro(), 1);
            return BD.registrar();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            BD.desconectar();
        }
    }

    private boolean Actualizar(PersonaBeans persona) throws Exception {
        try {
            String sql = "UPDATE mpersonas "
                    + "SET identificacion = ?, "
                    + "pnombre = ?, "
                    + "snombre = ?, "
                    + "papellido = ?, "
                    + "sapellido = ?, "
                    + "fechanacimiento = to_date(?, 'dd/MM/yy'), "
                    + "sexo = ?, "
                    + "direccion = ?, "
                    + "telefono = ?, "
                    + "celular = ?, "
                    + "email = ?, "
                    + "codestadocivil = ?, "
                    + "idusuario = ?, "
                    + "fechadigita = SYSDATE, "
                    + "salario = ?, "
                    + "sitiotrabajo = ?,"
                    + "fechaingreso = TO_DATE(?,'dd/mm/yyyy'), "
                    + "fecharetiro = TO_DATE(?,'dd/mm/yyyy') "
                    + "WHERE idpersona = ? ";
            BD.conectar();
            BD.callableStatement(sql);
            BD.AsignarParametro(1, persona.getIdentificacion(), 1);
            BD.AsignarParametro(2, persona.getpNombre().toUpperCase(), 1);
            BD.AsignarParametro(3, (persona.getsNombre() == null ? "" : persona.getsNombre().toUpperCase()), 1);
            BD.AsignarParametro(4, persona.getpApellido().toUpperCase(), 1);
            BD.AsignarParametro(5, (persona.getsApellido() == null ? "" : persona.getsApellido().toUpperCase()), 1);
            BD.AsignarParametro(6, persona.getFechaNacimiento(), 1);
            BD.AsignarParametro(7, persona.getSexo(), 1);
            BD.AsignarParametro(8, persona.getDireccion().toUpperCase(), 1);
            BD.AsignarParametro(9, persona.getTelefono(), 1);
            BD.AsignarParametro(10, persona.getCelular(), 1);
            BD.AsignarParametro(11, persona.getEmail().toUpperCase(), 1);
            BD.AsignarParametro(12, Integer.toString(persona.getCodEstadoCivil()), 2);
            BD.AsignarParametro(13, Integer.toString(persona.getUsuario().getIdusuario()), 2);
            BD.AsignarParametro(14, Integer.toString(persona.getSalario()), 2);
            BD.AsignarParametro(15, Integer.toString(persona.getSitiotrabajo()), 2);
            BD.AsignarParametro(16, persona.getFechaIngreso(), 1);
            BD.AsignarParametro(17, persona.getFechaRetiro(), 1);
            BD.AsignarParametro(18, Integer.toString(persona.getIdpersona()), 2);

            return BD.registrar();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            BD.desconectar();
        }
    }

    public boolean ActualizarDatosContacto(PersonaBeans persona) throws Exception {
        try {
            BD.conectar();

            String sql = "UPDATE mpersonas "
                    + "SET direccion = ?,"
                    + "telefono = ?,"
                    + "celular = ?,"
                    + "email = ? "
                    + "WHERE idpersona = ?";

            BD.callableStatement(sql);
            BD.AsignarParametro(1, persona.getDireccion().toUpperCase(), 1);
            BD.AsignarParametro(2, persona.getTelefono(), 1);
            BD.AsignarParametro(3, persona.getCelular(), 1);
            BD.AsignarParametro(4, persona.getEmail(), 1);
            BD.AsignarParametro(5, Integer.toString(persona.getIdpersona()), 1);
            return BD.registrar();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            BD.desconectar();
        }
    }

    public String getListPersonas(int page, int rows, String sidx, String sord) throws Exception {

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            String sql;
            String strQuery;
            String json;
            String nombre;
            String usuario;


            int start;
            int total = 0;
            int total1 = 0;
            int total_pages;

            boolean rc, estado;

            ResultSet rsCuenta;
            ResultSet datoSql;

            strQuery = "SELECT COUNT(*) AS valor FROM mpersonas mp, cusuarios cu WHERE mp.idpersona=cu.idusuario ";



            BD.conectar();
            BD.callableStatement(strQuery);
            BD.consultar();

            rsCuenta = BD.obtenerConsulta();

            if (rsCuenta.next()) {
                total = Integer.parseInt(rsCuenta.getString("valor"));
                total1 = total;
            }


            if (total > 0) {
                double d = Math.ceil((double) (total) / (double) (rows));
                total_pages = (int) (d);
            } else {
                total_pages = 0;
            }

            start = ((rows * page) - rows) + 1;


            sql = "SELECT mp.idpersona, mp.identificacion, mp.pnombre, mp.snombre, "
                    + "mp.papellido, mp.sapellido, mp.fechanacimiento, mp.sexo, "
                    + "mp.direccion, mp.telefono, mp.celular, mp.email, rce.desestadocivil, to_char(fechaingreso,'dd/mm/yyyy') fechaingreso,to_char(fecharetiro,'dd/mm/yyyy') fecharetiro, cu.activo, "
                    + "cper.desperfil, salario, cu.usuariosos, rst.dessitio "
                    + "FROM mpersonas mp "
                    + "INNER JOIN cusuarios cu ON mp.idpersona =cu.idusuario "
                    + "INNER JOIN cperfiles cper ON cu.codperfil = cper.codperfil "
                    + "INNER JOIN restadosciviles rce ON rce.codestadocivil = mp.codestadocivil "
                    + "INNER JOIN rsitiotrabajo rst ON rst.codsitio = mp.sitiotrabajo "
                    + "ORDER BY 2";

            BD.callableStatement(sql);
            BD.consultar();

            datoSql = BD.obtenerConsulta();
            total = total1;

            json = "";
            json = json + "{\n";
            json = json + "\"page\":" + page + ",\n";
            json = json + "\"total\":" + total_pages + ",\n";
            json = json + "\"records\":" + total + ",\n";
            json = json + "\"rows\": [";
            rc = false;
            ///////////////////////////////////////////////////////////////////
            while (datoSql.next()) {
                if (rc) {
                    json = json + ",";
                }
                if (datoSql.getString("activo").equals("1")) {
                    estado = true;
                } else {
                    estado = false;
                }

                json = json + "\n{";
                json = json + "\"id\":\"" + datoSql.getInt("idpersona") + "\",";
                json = json + "\"cell\":[" + datoSql.getInt("idpersona") + "";
                json = json + ",\"" + datoSql.getString("identificacion") + "\"";
                json = json + ",\"" + datoSql.getString("pnombre") + "\"";
                json = json + ",\"" + (datoSql.getString("snombre") == null ? "" : datoSql.getString("snombre")) + "\"";
                json = json + ",\"" + datoSql.getString("papellido") + "\"";
                json = json + ",\"" + (datoSql.getString("sapellido") == null ? "" : datoSql.getString("sapellido")) + "\"";
                json = json + ",\"" + sdf.format(datoSql.getDate("fechanacimiento")) + "\"";
                json = json + ",\"" + datoSql.getString("sexo") + "\"";
                json = json + ",\"" + datoSql.getString("direccion") + "\"";
                json = json + ",\"" + datoSql.getString("telefono") + "\"";
                json = json + ",\"" + datoSql.getString("celular") + "\"";
                json = json + ",\"" + datoSql.getString("email") + "\"";
                json = json + ",\"" + datoSql.getString("desestadocivil") + "\"";
                json = json + ",\"" + (datoSql.getString("fechaingreso") == null ? "" : datoSql.getString("fechaingreso")) + "\"";
                json = json + ",\"" + (datoSql.getString("fecharetiro") == null ? "" : datoSql.getString("fecharetiro")) + "\"";

                nombre = datoSql.getString("pnombre") + " ";
                nombre += datoSql.getString("snombre") == null ? "" : datoSql.getString("snombre") + " ";
                nombre += datoSql.getString("papellido") + " ";
                nombre += datoSql.getString("sapellido") == null ? "" : datoSql.getString("sapellido") + " ";

                json = json + ",\"" + nombre + "\"";
                json = json + ",\"" + datoSql.getString("desperfil") + "\"";
                json = json + ",\"" + datoSql.getInt("activo") + "\"";
                json = json + ",\"" + datoSql.getInt("salario") + "\"";

                usuario = datoSql.getString("usuariosos") == null ? "" : datoSql.getString("usuariosos") + " ";

                json = json + ",\"" + usuario + "\"";
                json = json + ",\"" + "Reestablecer" + "\"";
                json = json + ",\"" + datoSql.getString("dessitio") + "\"]";

                json = json + "}";
                rc = true;



            }
            json = json + "]\n";
            json = json + "}";
            BD.desconectar();

            return json;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            BD.desconectar();
        }
    }

    public int consultarId(PersonaBeans persona) throws Exception {
        int id = 0;
        ResultSet datoSql;
        try {
            BD.conectar();
            String sql = "SELECT idpersona FROM mpersonas "
                    + "WHERE identificacion = ?";
            BD.callableStatement(sql);
            BD.AsignarParametro(1, persona.getIdentificacion(), 1);

            if (!BD.consultar()) {
                throw new Exception("Error Realizando La Consulta: " + sql);
            } else {
                datoSql = BD.obtenerConsulta();
                while (datoSql.next()) {
                    id = Integer.parseInt(datoSql.getString("idpersona"));
                }
            }
            return id;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            BD.desconectar();
        }
    }

    public List<PersonaBeans> listarAuxiliaresPorCoordinador(int idusuario) throws Exception {
        ArrayList<PersonaBeans> list = new ArrayList<PersonaBeans>();

        UsuarioBeans usuarioBeans = new UsuarioDao().consultar(idusuario);
        try {
            BD.conectar();
            String sql = "SELECT   idpersona, pnombre, snombre, papellido, sapellido "
                    + "    FROM plaboresusuarios plau "
                    + "     INNER JOIN mpersonas mp ON mp.idpersona = plau.idusuario "
                    + "     INNER JOIN plaborescontratos pla ON pla.idlaborcontrato = plau.idlaborcontrato "
                    + "     INNER JOIN dareasgrupos da ON da.idgrupo = pla.idgrupo ";
            if (usuarioBeans.getPerfil().getCod_perfil() == 5) {
                sql += "   WHERE da.idcoordinador = ? ";
            }
            sql += "GROUP BY idpersona, pnombre, snombre, papellido, sapellido "
                    + "ORDER BY 2";
            BD.callableStatement(sql);
            if (usuarioBeans.getPerfil().getCod_perfil() == 5) {
                BD.AsignarParametro(1, Integer.toString(idusuario), 2);
            }
            BD.consultar();

            ResultSet datoSql = BD.obtenerConsulta();
            PersonaBeans personaBeans;

            while (datoSql.next()) {
                personaBeans = new PersonaBeans();
                personaBeans.setIdpersona(datoSql.getInt(1));
                personaBeans.setpNombre(datoSql.getString(2));
                personaBeans.setsNombre(datoSql.getString(3) == null ? "" : datoSql.getString(3));
                personaBeans.setpApellido(datoSql.getString(4));
                personaBeans.setsApellido(datoSql.getString(5) == null ? "" : datoSql.getString(5));

                list.add(personaBeans);
            }
            return list;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            BD.desconectar();
        }
    }

    public String recuperarClave(PersonaBeans Persona) throws Exception {
        try {
            String sql, clave = "";
            ResultSet datoSql;
            BD.conectar();
            sql = "SELECT clave "
                    + "FROM mpersonas mp "
                    + "INNER JOIN cusuarios cu ON cu.login = mp.identificacion "
                    + "WHERE mp.identificacion = ? "
                    + "AND mp.email = ? ";
            BD.callableStatement(sql);
            BD.AsignarParametro(1, Persona.getIdentificacion(), 1);
            BD.AsignarParametro(2, Persona.getEmail(), 1);
            if (!BD.consultar()) {
                throw new Exception("Error Realizando La Consulta: " + sql);
            } else {
                datoSql = BD.obtenerConsulta();
                while (datoSql.next()) {
                    clave = datoSql.getString("clave");
                }
            }
            return clave;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            BD.desconectar();
        }
    }
}
