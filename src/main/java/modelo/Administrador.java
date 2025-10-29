package modelo;

public class Administrador extends Persona {
    private String usuario;
    private String contrasena;

    // ...
    @Override
    public String generarContacto() {
        return "Administrador: " + nombre + " | Usuario: " + usuario;
    }
}