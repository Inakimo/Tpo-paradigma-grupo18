package modelo;

public class ServicioReparacion extends Servicio { // Usado como la Mano de Obra general
    private double horasTrabajo;
    private double costoPorHora;
    private List<Repuesto> repuestosUsados; // Ya está modelado en OrdenDeTrabajo, aquí se usa para detalle

    @Override
    public double calcularCostoTotal() {
        return this.costoBase + (this.horasTrabajo * this.costoPorHora); // Cálculo basado en tiempo
    }

    @Override
    public String generarResumen() {
        return "Reparación - Horas: " + horasTrabajo;
    }
}

