package modelo;

public class Vehiculo {
    private int idVehiculo;      // Identificador único
    private String marca;        // Marca del vehículo (ej: Toyota)
    private String modelo;       // Modelo del vehículo (ej: Corolla)
    private String placa;        // Matrícula/Placa
    private int anioFabricacion; // Año de fabricación

    public Vehiculo(String marca, String modelo, int anioFabricacion) {
        this.idVehiculo = idVehiculo;
        this.marca = marca;
        this.modelo = modelo;
        this.placa = placa;
        this.anioFabricacion = anioFabricacion;
    }

    // Getters y setters
    public int getIdVehiculo() {
        return idVehiculo;
    }

    public String getMarca() {
        return marca;
    }

    public String getModelo() {
        return modelo;
    }

    public String getPlaca() {
        return placa;
    }

    public int getAnioFabricacion() {
        return anioFabricacion;
    }

    @Override
    public String toString() {
        return "Vehículo ID: " + idVehiculo + " | Marca: " + marca + " | Modelo: " + modelo +
                " | Placa: " + placa + " | Año: " + anioFabricacion;
    }

    public String getAnio() {
    }
}