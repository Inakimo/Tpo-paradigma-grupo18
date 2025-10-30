package modelo;

public class ServicioMantenimiento extends Servicio {
    private String tipoMantenimiento; // Preventivo o Correctivo
    private int kilometraje;

    // Constructor completo
    public ServicioMantenimiento(int idServicio, String descripcion, double costoBase,
                                 String tipoMantenimiento, int kilometraje) {
        super(idServicio, descripcion, costoBase);

        // Validación del tipo de mantenimiento
        if (!tipoMantenimiento.equalsIgnoreCase("Preventivo") &&
                !tipoMantenimiento.equalsIgnoreCase("Correctivo")) {
            throw new IllegalArgumentException("Tipo de mantenimiento debe ser 'Preventivo' o 'Correctivo'");
        }

        // Validación del kilometraje
        if (kilometraje < 0) {
            throw new IllegalArgumentException("El kilometraje no puede ser negativo");
        }

        this.tipoMantenimiento = tipoMantenimiento;
        this.kilometraje = kilometraje;
    }

    // Getters y Setters
    public String getTipoMantenimiento() {
        return tipoMantenimiento;
    }

    public void setTipoMantenimiento(String tipoMantenimiento) {
        if (!tipoMantenimiento.equalsIgnoreCase("Preventivo") &&
                !tipoMantenimiento.equalsIgnoreCase("Correctivo")) {
            throw new IllegalArgumentException("Tipo de mantenimiento debe ser 'Preventivo' o 'Correctivo'");
        }
        this.tipoMantenimiento = tipoMantenimiento;
    }

    public int getKilometraje() {
        return kilometraje;
    }

    public void setKilometraje(int kilometraje) {
        if (kilometraje < 0) {
            throw new IllegalArgumentException("El kilometraje no puede ser negativo");
        }
        this.kilometraje = kilometraje;
    }

    @Override
    public double calcularCostoTotal() {
        if (this.tipoMantenimiento.equalsIgnoreCase("Preventivo")) {
            return this.costoBase * 0.90; // 10% de descuento por ser preventivo
        }
        return this.costoBase;
    }

    @Override
    public String generarResumen() {
        return String.format("Mantenimiento %s - KM: %,d, Costo: $%.2f",
                tipoMantenimiento, kilometraje, calcularCostoTotal());
    }

    /**
     * Método de negocio: Verifica si el mantenimiento es urgente
     * basándose en el kilometraje
     */
    public boolean esUrgente() {
        // Ejemplo: mantenimiento correctivo o kilometraje alto
        return tipoMantenimiento.equalsIgnoreCase("Correctivo") || kilometraje > 100000;
    }

    /**
     * Calcula el ahorro si se elige mantenimiento preventivo
     */
    public double calcularAhorro() {
        if (tipoMantenimiento.equalsIgnoreCase("Preventivo")) {
            return this.costoBase * 0.10; // 10% de ahorro
        }
        return 0.0;
    }
}