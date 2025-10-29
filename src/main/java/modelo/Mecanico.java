package modelo;

import java.util.List;

public class Mecanico extends Persona {
    private double tarifaHora;
    private String especialidad;
    private List<OrdenDeTrabajo> ordenesAsignadas; // Relación 1..*

    // Constructor, getters y setters...

    @Override
    public String generarContacto() {
        return "Mecánico: " + nombre + " | Especialidad: " + especialidad;
    }

    // Método para demostrar la excepción de negocio
    public void cambiarTarifa(double nuevaTarifa) {
        if (nuevaTarifa < 0) {

            throw new IllegalArgumentException("La tarifa no puede ser negativa.");
        }
        this.tarifaHora = nuevaTarifa;
    }
}