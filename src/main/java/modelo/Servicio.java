package modelo;

public abstract class Servicio implements ICalculable { // Implementa la Interfaz
    protected int idServicio;
    protected String descripcion;
    protected double costoBase;

    // Constructor, getters y setters...

    // El método abstracto se define en la interfaz, pero aquí se detalla la intención
    // Se cumple el contrato de ICalculable
    // public abstract double calcularCostoTotal(); (Ya está en la interfaz)

    // Método para el Polimorfismo
    public abstract String generarResumen();
}