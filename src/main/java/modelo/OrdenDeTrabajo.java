package modelo;

import enums.EstadoOrden;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class OrdenDeTrabajo {
    private int idOrden;
    private Date fechaDeIngreso;
    private Date fechaDeEntrega;
    private Vehiculo vehiculo; // Relación 1
    private Mecanico mecanicoAsignado; // Relación 1
    private EstadoOrden estado;

    // Composición: Los servicios solo existen dentro de la orden
    private List<Servicio> listaServicios;

    // Agregación/Dependencia: Repuestos utilizados (Map: Repuesto, Cantidad)
    private Map<Repuesto, Integer> repuestosUtilizados;

    private Set<Mecanico> asignados; // Set para evitar duplicidad de asignaciones

    // Constructor, getters y setters...

    // Polimorfismo: La Orden suma los costos que cada servicio calcula.
    public double calcularTotal() {
        double totalServicios = 0.0;
        for (Servicio s : listaServicios) {
            totalServicios += s.calcularCostoTotal(); // Llama al método polimórfico
        }

        double totalRepuestos = 0.0;
        for (Map.Entry<Repuesto, Integer> entry : repuestosUtilizados.entrySet()) {
            totalRepuestos += entry.getKey().getPrecioUnitario() * entry.getValue();
        }

        return totalServicios + totalRepuestos;
    }
}