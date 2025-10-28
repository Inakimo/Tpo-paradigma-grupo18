package modelo;

public class ServicioMantenimiento extends Servicio {
    private String tipoMantenimiento; // Preventivo o Correctivo
    private int kilometraje;
    private boolean incluyeLavado;

    @Override
    public double calcularCostoTotal() {
        if (this.tipoMantenimiento.equals("Preventivo")) {
            return this.costoBase * 0.90; // 10% de descuento por ser preventivo
        }
        return this.costoBase;
    }

    @Override
    public String generarResumen() {
        return "Mantenimiento - Tipo: " + tipoMantenimiento + ", KM: " + kilometraje;
    }
}