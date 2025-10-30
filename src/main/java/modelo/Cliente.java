package modelo;

public class Cliente extends Persona {
    private int id;
    private String direccion;
    private String telefono;

    public Cliente(int id, String nombre){
        super(nombre,id);

    }

    public Cliente(int id, String nombre, String email, String direccion, String telefono) {
        super(nombre, email);
        this.id = id;
        this.direccion = direccion;
        this.telefono = telefono;
    }

    public int getId() {
        return id;
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

    public String getCorreoElectronico(){
        return "correoejemplo";
    }

    @Override
    public String generarContacto() {
        return "Cliente: " + getNombre() + " | ID: " + id + " | Correo: " + getCorreoElectronico() +
                " | Teléfono: " + telefono + " | Dirección: " + direccion;
    }
}