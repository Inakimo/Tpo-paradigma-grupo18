package modelo;

public class Proveedor {
    private int idProveedor;
    private String nombreProveedor;
    private String direccion;
    private List<Repuesto> repuestos; // Relación n..* (muchos a muchos, modelado aquí como lista de repuestos que provee)

}