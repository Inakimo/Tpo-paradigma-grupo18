package modelo;

public final class Vehiculo { // final para asegurar inmutabilidad
    private final String patente;
    private final String marca;
    private final String modelo;
    private final int anio;
    private final Cliente dueno; // Relación 1

    public Vehiculo(String patente, String marca, String modelo, int anio, Cliente dueno) {
        this.patente = patente;
        this.marca = marca;
        this.modelo = modelo;
        this.anio = anio;
        this.dueno = dueno;
    }

    // Solo getters
    public String getPatente() { return patente; }
    public int getAnio() { return anio; }
    // ...
}