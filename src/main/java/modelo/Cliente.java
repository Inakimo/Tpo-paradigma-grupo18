package modelo;

public class Cliente extends Persona {
    private List<Vehiculo> vehiculos; // Relación 1..* (Agregación)
    private List<Servicio> historialServicios;

    // Constructor, getters y setters...

    @Override
    public String generarContacto() {
        return "Cliente: " + nombre + " | Teléfono: " + telefono;
    }
}