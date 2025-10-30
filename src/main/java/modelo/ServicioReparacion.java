package modelo;

public class ServicioReparacion extends Servicio {
    private double horasTrabajo;
    private double costoPorHora;

    // Constructor completo
    public ServicioReparacion(int idServicio, String descripcion, double costoBase,
                              double horasTrabajo, double costoPorHora) {
        super(idServicio, descripcion, costoBase);

        // Validaciones
        if (horasTrabajo < 0) {
            throw new IllegalArgumentException("Las horas de trabajo no pueden ser negativas");
        }
        if (costoPorHora < 0) {
            throw new IllegalArgumentException("El costo por hora no puede ser negativo");
        }

        this.horasTrabajo = horasTrabajo;
        this.costoPorHora = costoPorHora;
    }

    // Getters y Setters
    public double getHorasTrabajo() {
        return horasTrabajo;
    }

    public void setHorasTrabajo(double horasTrabajo) {
        if (horasTrabajo < 0) {
            throw new IllegalArgumentException("Las horas de trabajo no pueden ser negativas");
        }
        this.horasTrabajo = horasTrabajo;
    }

    public double getCostoPorHora() {
        return costoPorHora;
    }

    public void setCostoPorHora(double costoPorHora) {
        if (costoPorHora < 0) {
            throw new IllegalArgumentException("El costo por hora no puede ser negativo");
        }
        this.costoPorHora = costoPorHora;
    }

    @Override
    public double calcularCostoTotal() {
        // Costo base (repuestos/materiales) + Costo de mano de obra
        return this.costoBase + (this.horasTrabajo * this.costoPorHora);
    }

    @Override
    public String generarResumen() {
        return String.format("Reparación - Horas: %.2f, Costo/hora: $%.2f, Total: $%.2f",
                horasTrabajo, costoPorHora, calcularCostoTotal());
    }

    /**
     * Calcula solo el costo de mano de obra (sin materiales)
     */
    public double calcularCostoManoDeObra() {
        return this.horasTrabajo * this.costoPorHora;
    }

    /**
     * Calcula el costo de materiales/repuestos (costoBase)
     */
    public double calcularCostoMateriales() {
        return this.costoBase;
    }

    /**
     * Método de negocio: Verifica si la reparación es extensa
     * (más de 8 horas de trabajo)
     */
    public boolean esReparacionExtensa() {
        return this.horasTrabajo > 8.0;
    }

    /**
     * Calcula el tiempo estimado de finalización en días
     * Asumiendo 8 horas de trabajo por día
     */
    public int calcularDiasEstimados() {
        return (int) Math.ceil(this.horasTrabajo / 8.0);
    }
}