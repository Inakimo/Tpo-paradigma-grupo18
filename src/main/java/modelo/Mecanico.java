package modelo;

import java.util.ArrayList;
import java.util.List;

public class Mecanico extends Persona {
    private double tarifaHora;
    private String especialidad;
    private List<OrdenDeTrabajo> ordenesAsignadas;

    public Mecanico(String nombre, String correoElectronico, double tarifaHora, String especialidad) {
        super(nombre, correoElectronico);
        this.tarifaHora = tarifaHora;
        this.especialidad = especialidad;
        this.ordenesAsignadas = new ArrayList<>();
    }

    public double getTarifaHora() {
        return tarifaHora;
    }

    public void setTarifaHora(double tarifaHora) {
        if (tarifaHora < 0) {
            throw new IllegalArgumentException("La tarifa no puede ser negativa.");
        }
        this.tarifaHora = tarifaHora;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public List<OrdenDeTrabajo> getOrdenesAsignadas() {
        return ordenesAsignadas;
    }

    public void asignarOrden(OrdenDeTrabajo orden) {
        if (!ordenesAsignadas.contains(orden)) {
            ordenesAsignadas.add(orden);
        }
    }

    @Override
    public String generarContacto() {
        return "Mecánico: " + getNombre() + " | Especialidad: " + especialidad + " | Tarifa: $" + tarifaHora;
    }
}