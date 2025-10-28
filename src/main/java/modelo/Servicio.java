package modelo;

public class Servicio {
    private int idServicio;
    private String descripcion;
    protected double costoBase;

    public Servicio() {
    }

    public Servicio(int idServicio, String descripcion, double costoBase) {
        this.idServicio = idServicio;
        this.descripcion = descripcion;
        this.costoBase = costoBase;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public double getCostoBase() {
        return costoBase;
    }

    /**
     * Método polimórfico para calcular el costo total del servicio, que será
     * sobreescrito en las clases hijas como `ServicioPintura` y `ServicioReparacion`.
     */
    public double calcularCostoTotal() {
        return costoBase;
    }

    /**
     * Método polimórfico para generar un resumen, sobreescrito por las clases hijas.
     */
    public String generarResumen() {
        return "Servicio general: " + descripcion;
    }
}