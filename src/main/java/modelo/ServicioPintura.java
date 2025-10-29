package modelo;

public class ServicioPintura extends Servicio {
    private String codigoColor;
    private String tipoPintura; // Ej: Acrílica, Uretano, Laca
    private double superficieM2; // Dato clave para el cálculo

    public ServicioPintura(int idServicio, String descripcion, double costoBase,
                           String codigoColor, String tipoPintura, double superficieM2) {
        super(descripcion, costoBase);
        this.codigoColor = codigoColor;
        this.tipoPintura = tipoPintura;
        this.superficieM2 = superficieM2;
    }

    /**
     * Implementación polimórfica: Calcula el costo sumando el costo base y un
     * costo variable basado en la superficie y la calidad de la pintura.
     */
    @Override
    public double calcularCostoTotal() {
        double costoMaterialPorM2 = 0;

        if (this.tipoPintura.equalsIgnoreCase("Uretano")) {
            costoMaterialPorM2 = 500.0; // Costo más alto
        } else {
            costoMaterialPorM2 = 300.0; // Costo estándar
        }

        return this.costoInicial + (this.superficieM2 * costoMaterialPorM2);
    }

    @Override
    public String generarResumen() {
        return "Pintura - Superficie: " + superficieM2 + " m2, Color: " + codigoColor;
    }

    public void mezclarColor() {
        System.out.println("Preparando mezcla para el color " + codigoColor + " (" + tipoPintura + ").");
    }

}