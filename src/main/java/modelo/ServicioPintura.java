package modelo;

public class ServicioPintura extends Servicio {
    private String codigoColor;
    private String tipoPintura; // Ej: Acrílica, Uretano, Laca
    private double superficieM2;

    // Constructor completo - AHORA USA EL CONSTRUCTOR CORRECTO DEL PADRE
    public ServicioPintura(int idServicio, String descripcion, double costoBase,
                           String codigoColor, String tipoPintura, double superficieM2) {
        // Usamos el constructor completo de la clase padre
        super(idServicio, descripcion, costoBase);

        // Validaciones
        if (superficieM2 <= 0) {
            throw new IllegalArgumentException("La superficie debe ser mayor a 0");
        }

        this.codigoColor = codigoColor;
        this.tipoPintura = tipoPintura;
        this.superficieM2 = superficieM2;
    }

    // Getters y Setters
    public String getCodigoColor() {
        return codigoColor;
    }

    public void setCodigoColor(String codigoColor) {
        this.codigoColor = codigoColor;
    }

    public String getTipoPintura() {
        return tipoPintura;
    }

    public void setTipoPintura(String tipoPintura) {
        this.tipoPintura = tipoPintura;
    }

    public double getSuperficieM2() {
        return superficieM2;
    }

    public void setSuperficieM2(double superficieM2) {
        if (superficieM2 <= 0) {
            throw new IllegalArgumentException("La superficie debe ser mayor a 0");
        }
        this.superficieM2 = superficieM2;
    }

    /**
     * Implementación polimórfica: Calcula el costo sumando el costo base (mano de obra)
     * y un costo variable basado en la superficie y la calidad de la pintura.
     */
    @Override
    public double calcularCostoTotal() {
        double costoMaterialPorM2 = calcularCostoPinturaPorM2();
        return this.costoBase + (this.superficieM2 * costoMaterialPorM2);
    }

    /**
     * Método auxiliar privado para calcular el costo por m2 según el tipo de pintura
     */
    private double calcularCostoPinturaPorM2() {
        switch (this.tipoPintura.toLowerCase()) {
            case "uretano":
                return 500.0; // Costo premium
            case "laca":
                return 400.0; // Costo medio-alto
            case "acrilica":
                return 300.0; // Costo estándar
            default:
                return 300.0; // Por defecto
        }
    }

    @Override
    public String generarResumen() {
        return String.format("Pintura %s - Superficie: %.2f m², Color: %s, Costo: $%.2f",
                tipoPintura, superficieM2, codigoColor, calcularCostoTotal());
    }

    /**
     * Método de negocio específico de pintura
     */
    public void mezclarColor() {
        System.out.println("Preparando mezcla para el color " + codigoColor +
                " (" + tipoPintura + ").");
    }

    /**
     * Calcula la cantidad estimada de litros de pintura necesarios
     * Asumiendo rendimiento de 8-10 m² por litro
     */
    public double calcularLitrosNecesarios() {
        double rendimientoPorLitro = 9.0; // m² por litro (promedio)
        return Math.ceil(superficieM2 / rendimientoPorLitro);
    }

    /**
     * Calcula solo el costo de materiales (sin mano de obra)
     */
    public double calcularCostoMateriales() {
        return this.superficieM2 * calcularCostoPinturaPorM2();
    }
}