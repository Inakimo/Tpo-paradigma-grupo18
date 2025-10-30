package modelo;

import enums.NivelComplejidad;

public class ServicioDiagnostico extends Servicio {
    private NivelComplejidad nivel;
    private String resultado;
    private boolean requiereAprobacion;

    // Constructor completo
    public ServicioDiagnostico(int idServicio, String descripcion, double costoBase,
                               NivelComplejidad nivel, String resultado, boolean requiereAprobacion) {
        super(idServicio, descripcion, costoBase);
        this.nivel = nivel;
        this.resultado = resultado;
        this.requiereAprobacion = requiereAprobacion;
    }

    // Constructor sin resultado (para cuando aún no se ha realizado el diagnóstico)
    public ServicioDiagnostico(int idServicio, String descripcion, double costoBase,
                               NivelComplejidad nivel, boolean requiereAprobacion) {
        super(idServicio, descripcion, costoBase);
        this.nivel = nivel;
        this.resultado = "Pendiente";
        this.requiereAprobacion = requiereAprobacion;
    }

    // Getters y Setters
    public NivelComplejidad getNivel() {
        return nivel;
    }

    public void setNivel(NivelComplejidad nivel) {
        this.nivel = nivel;
    }

    public String getResultado() {
        return resultado;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }

    public boolean isRequiereAprobacion() {
        return requiereAprobacion;
    }

    public void setRequiereAprobacion(boolean requiereAprobacion) {
        this.requiereAprobacion = requiereAprobacion;
    }

    @Override
    public double calcularCostoTotal() {
        double recargo = 0;
        if (this.nivel == NivelComplejidad.ALTO) {
            recargo = this.costoBase * 0.50; // 50% de recargo por complejidad
        } else if (this.nivel == NivelComplejidad.MEDIO) {
            recargo = this.costoBase * 0.25; // 25% de recargo
        }
        // BAJO no tiene recargo
        return this.costoBase + recargo;
    }

    @Override
    public String generarResumen() {
        String aprobacion = requiereAprobacion ? " (Requiere aprobación)" : "";
        return String.format("Diagnóstico - Nivel: %s, Resultado: %s, Costo: $%.2f%s",
                nivel, resultado, calcularCostoTotal(), aprobacion);
    }

    /**
     * Método de negocio: Verifica si el diagnóstico está completado
     */
    public boolean estaCompletado() {
        return resultado != null && !resultado.equals("Pendiente");
    }
}