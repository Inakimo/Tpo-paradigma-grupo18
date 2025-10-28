package modelo;

public class Vehiculo {
    private String modelo;
    private String marca;
    private int anio;

    public Vehiculo(String modelo, String marca, int anio) {
        this.modelo = modelo;
        this.marca = marca;
        this.anio = anio;
    }

    public String getModelo() {
        return modelo;
    }

    public String getMarca() {
        return marca;
    }

    public int getAnio() {
        return anio;
    }
}