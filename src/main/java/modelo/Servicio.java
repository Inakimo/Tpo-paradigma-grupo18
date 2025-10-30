package modelo;

public class Servicio {
    private int idServicio;
    private String descripcion;
    protected double costoBase;

    public Servicio() {
    }

    // Constructor corregido y mejorado
    public Servicio(int idServicio, String descripcion, double costoBase) {
        this.idServicio = idServicio; // Asignación correcta
        this.descripcion = descripcion;
        this.costoBase = costoBase;
    }

    // Dejamos el constructor que tenías en ServicioPintura por compatibilidad
    public Servicio(String descripcion, double costoBase) {
        this.descripcion = descripcion;
        this.costoBase = costoBase;
    }


    public String getDescripcion() {
        return descripcion;
    }

    public double getCostoBase() {
        return costoBase;
    }

    public int getIdServicio() {
        return idServicio;
    }

    /**
     * Método polimórfico para calcular el costo total del servicio, que será
     * sobreescrito en las clases hijas.
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