package modelo;

public class Repuesto {
    private int idRepuesto;
    private String nombre;
    private double precioUnitario;
    private int stock;


    public Repuesto(int idRepuesto, String nombre, double precioUnitario, int stock) {
        this.idRepuesto = idRepuesto;
        this.nombre = nombre;
        this.precioUnitario = precioUnitario;
        this.stock = stock;
    }

    public int getIdRepuesto() {
        return idRepuesto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(double precioUnitario) {
        if (precioUnitario < 0) {
            throw new IllegalArgumentException("El precio unitario no puede ser negativo.");
        }
        this.precioUnitario = precioUnitario;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        if (stock < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo.");
        }
        this.stock = stock;
    }

    public void reducirStock(int cantidad) {
        if (cantidad > stock) {
            throw new IllegalArgumentException("No hay suficiente stock para realizar la operación.");
        }
        stock -= cantidad;
    }

    public void incrementarStock(int cantidad) {
        if (cantidad < 0) {
            throw new IllegalArgumentException("La cantidad de stock a incrementar no puede ser negativa.");
        }
        stock += cantidad;
    }

    @Override
    public String toString() {
        return "Repuesto - ID: " + idRepuesto + ", Nombre: " + nombre +
                ", Precio: $" + precioUnitario + ", Stock actual: " + stock;
    }
}