package com.co.sio.java.mbeans;

/**
 *
 * @author amejia
 */
public class PersonaBeans extends UsuarioBeans {

    private int idpersona;
    private String identificacion;
    private String pNombre;
    private String sNombre;
    private String pApellido;
    private String sApellido;
    private String fechaNacimiento;
    private String sexo;
    private String direccion;
    private String telefono;
    private String celular;
    private String email;
    private int codEstadoCivil;
    private UsuarioBeans usuario;
    private int salario;
    private String mensaje;
    private int sitiotrabajo;
    public String fechaIngreso;
    public String fechaRetiro;
    public int nocturno;

    public int getSitiotrabajo() {
        return sitiotrabajo;
    }

    public void setSitiotrabajo(int sitiotrabajo) {
        this.sitiotrabajo = sitiotrabajo;
    }

    @Override
    public String getMensaje() {
        return mensaje;
    }

    @Override
    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    @Override
    public UsuarioBeans getUsuario() {
        return usuario;
    }

    @Override
    public void setUsuario(UsuarioBeans usuario) {
        this.usuario = usuario;
    }

    public int getSalario() {
        return salario;
    }

    public void setSalario(int salario) {
        this.salario = salario;
    }

    public int getIdpersona() {
        return idpersona;
    }

    public void setIdpersona(int idpersona) {
        this.idpersona = idpersona;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public String getpNombre() {
        return pNombre == null ? "" : pNombre;
    }

    public void setpNombre(String pNombre) {
        this.pNombre = pNombre;
    }

    public String getsNombre() {
        return sNombre == null ? "" : sNombre;
    }

    public void setsNombre(String sNombre) {
        this.sNombre = sNombre;
    }

    public String getpApellido() {
        return pApellido == null ? "" : pApellido;
    }

    public void setpApellido(String pApellido) {
        this.pApellido = pApellido;
    }

    public String getsApellido() {
        return sApellido == null ? "" : sApellido;
    }

    public void setsApellido(String sApellido) {
        this.sApellido = sApellido;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getCodEstadoCivil() {
        return codEstadoCivil;
    }

    public void setCodEstadoCivil(int codEstadoCivil) {
        this.codEstadoCivil = codEstadoCivil;
    }

    public String getFechaIngreso() {
        return fechaIngreso;
    }

    public String getFechaRetiro() {
        return fechaRetiro;
    }

    public void setFechaIngreso(String fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public void setFechaRetiro(String fechaRetiro) {
        this.fechaRetiro = fechaRetiro;
    }

    public int getNocturno() {
        return nocturno;
    }

    public void setNocturno(int nocturno) {
        this.nocturno = nocturno;
    }
    
    public String getNombres() {
        return String.format("%s %s %s %s", this.getpNombre(), this.getsNombre(), this.getpApellido(), this.getsApellido());
    }
}
