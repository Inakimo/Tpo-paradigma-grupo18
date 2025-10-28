package modelo;

public class Repuesto {
    private int idRepuesto;
    private String nombre;
    private int stock;
    private double precioUnitario;

    // Constructor, getters y setters...

    // Lógica para la excepción de negocio
    public void descontarStock(int cantidad) throws excepciones.StockInsuficienteException {
        if (this.stock < cantidad) {
            throw new excepciones.StockInsuficienteException(this.nombre, this.stock, cantidad);
        }
        this.stock -= cantidad;
    }
}