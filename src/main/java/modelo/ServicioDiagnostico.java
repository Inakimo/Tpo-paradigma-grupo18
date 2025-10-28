package modelo;

import enums.NivelComplejidad;

public class ServicioDiagnostico extends Servicio {
    private NivelComplejidad nivel; // Dato para el cálculo
    private String resultado;
    private boolean requiereAprobacion; // Dato para la lógica de negocio

    @Override
    public double calcularCostoTotal() {
        double recargo = 0;
        if (this.nivel == NivelComplejidad.ALTO) {
            recargo = this.costoBase * 0.50; // 50% de recargo por complejidad
        }
        return this.costoBase + recargo;
    }

    @Override
    public String generarResumen() {
        return "Diagnóstico - Nivel: " + nivel + ", Resultado: " + resultado;
    }
}