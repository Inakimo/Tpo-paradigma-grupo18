package modelo;

public abstract class Persona {
    protected int idPersona;
    protected String nombre;
    protected String dni;
    protected String telefono;
    protected String email;
    protected String direccion;

    // Constructor, getters y setters...

    public abstract String generarContacto();
}