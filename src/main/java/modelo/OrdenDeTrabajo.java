package modelo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import enums.EstadoOrden;

public class OrdenDeTrabajo {
    private int idOrden;
    private LocalDateTime fechaDeIngreso;
    private LocalDateTime fechaDeEntrega;
    private Vehiculo vehiculo;
    private EstadoOrden estado;
    private String observaciones;
    
    // Composición: Los servicios solo existen dentro de la orden
    private List<Servicio> listaServicios;
    
    // Constructor completo
    public OrdenDeTrabajo(int idOrden, Vehiculo vehiculo, String observaciones) {
        if (vehiculo == null) {
            throw new IllegalArgumentException("La orden debe estar asociada a un vehículo");
        }
        
        this.idOrden = idOrden;
        this.vehiculo = vehiculo;
        this.fechaDeIngreso = LocalDateTime.now();
        this.estado = EstadoOrden.PENDIENTE;
        this.observaciones = observaciones != null ? observaciones : "";
        this.listaServicios = new ArrayList<>();
    }
    
    // Constructor simplificado
    public OrdenDeTrabajo(int idOrden, Vehiculo vehiculo) {
        this(idOrden, vehiculo, "");
    }
    
    // ========== GETTERS Y SETTERS ==========
    
    public int getIdOrden() {
        return idOrden;
    }
    
    public LocalDateTime getFechaDeIngreso() {
        return fechaDeIngreso;
    }
    
    public LocalDateTime getFechaDeEntrega() {
        return fechaDeEntrega;
    }
    
    public void setFechaDeEntrega(LocalDateTime fechaDeEntrega) {
        this.fechaDeEntrega = fechaDeEntrega;
    }
    
    public Vehiculo getVehiculo() {
        return vehiculo;
    }
    
    public Cliente getCliente() {
        return vehiculo.getPropietario();
    }
    
    public EstadoOrden getEstado() {
        return estado;
    }
    
    public void setEstado(EstadoOrden estado) {
        this.estado = estado;
        
        // Si se completa, registrar fecha de entrega
        if (estado == EstadoOrden.ENTREGADA && fechaDeEntrega == null) {
            this.fechaDeEntrega = LocalDateTime.now();
        }
    }
    
    public String getObservaciones() {
        return observaciones;
    }
    
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
    
    public List<Servicio> getListaServicios() {
        return new ArrayList<>(listaServicios); // Retorna copia
    }
    
    // ========== GESTIÓN DE SERVICIOS ==========
    
    /**
     * Agrega un servicio a la orden
     */
    public void agregarServicio(Servicio servicio) {
        if (servicio == null) {
            throw new IllegalArgumentException("El servicio no puede ser nulo");
        }
        
        if (estado == EstadoOrden.ENTREGADA || estado == EstadoOrden.CANCELADA) {
            throw new IllegalStateException("No se pueden agregar servicios a una orden " + estado);
        }
        
        listaServicios.add(servicio);
    }
    
    /**
     * Remueve un servicio de la orden
     */
    public boolean removerServicio(Servicio servicio) {
        if (estado == EstadoOrden.ENTREGADA || estado == EstadoOrden.CANCELADA) {
            throw new IllegalStateException("No se pueden remover servicios de una orden " + estado);
        }
        
        return listaServicios.remove(servicio);
    }
    
    /**
     * Obtiene la cantidad de servicios en la orden
     */
    public int getCantidadServicios() {
        return listaServicios.size();
    }
    
    /**
     * Verifica si la orden tiene servicios
     */
    public boolean tieneServicios() {
        return !listaServicios.isEmpty();
    }
    
    // ========== CÁLCULOS ==========
    
    /**
     * Calcula el total de la orden sumando todos los servicios
     */
    public double calcularTotal() {
        double total = 0.0;
        for (Servicio s : listaServicios) {
            total += s.calcularCostoTotal();
        }
        return total;
    }
    
    /**
     * Calcula el subtotal (sin IVA)
     */
    public double calcularSubtotal() {
        return calcularTotal();
    }
    
    /**
     * Calcula el IVA (21%)
     */
    public double calcularIVA() {
        return calcularSubtotal() * 0.21;
    }
    
    /**
     * Calcula el total con IVA
     */
    public double calcularTotalConIVA() {
        return calcularSubtotal() + calcularIVA();
    }
    
    // ========== MÉTODOS DE NEGOCIO ==========
    
    /**
     * Inicia el trabajo en la orden
     */
    public void iniciarTrabajo() {
        if (estado != EstadoOrden.PENDIENTE && estado != EstadoOrden.ESPERANDO_APROBACION) {
            throw new IllegalStateException("La orden debe estar pendiente para iniciar");
        }
        
        if (!tieneServicios()) {
            throw new IllegalStateException("La orden debe tener al menos un servicio");
        }
        
        this.estado = EstadoOrden.EN_PROCESO;
    }
    
    /**
     * Completa la orden
     */
    public void completarOrden() {
        if (estado != EstadoOrden.EN_PROCESO) {
            throw new IllegalStateException("La orden debe estar en proceso para completarla");
        }
        
        this.estado = EstadoOrden.COMPLETADA;
    }
    
    /**
     * Entrega la orden al cliente
     */
    public void entregar() {
        if (estado != EstadoOrden.COMPLETADA) {
            throw new IllegalStateException("La orden debe estar completada para entregarla");
        }
        
        this.estado = EstadoOrden.ENTREGADA;
        this.fechaDeEntrega = LocalDateTime.now();
    }
    
    /**
     * Cancela la orden
     */
    public void cancelar(String motivo) {
        if (estado == EstadoOrden.ENTREGADA) {
            throw new IllegalStateException("No se puede cancelar una orden entregada");
        }
        
        this.estado = EstadoOrden.CANCELADA;
        this.observaciones += "\nMotivo de cancelación: " + motivo;
    }
    
    // ========== GENERACIÓN DE REPORTES ==========
    
    /**
     * Genera una factura/resumen completo de la orden
     */
    public String generarFactura() {
        StringBuilder factura = new StringBuilder();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        
        factura.append("═".repeat(70)).append("\n");
        factura.append("                    ORDEN DE TRABAJO #").append(idOrden).append("\n");
        factura.append("═".repeat(70)).append("\n\n");
        
        // Información del cliente
        factura.append("CLIENTE:\n");
        factura.append("  Nombre: ").append(getCliente().getNombre()).append("\n");
        factura.append("  Teléfono: ").append(getCliente().getTelefono()).append("\n");
        factura.append("  Email: ").append(getCliente().getCorreoElectronico()).append("\n\n");
        
        // Información del vehículo
        factura.append("VEHÍCULO:\n");
        factura.append("  ").append(vehiculo.getMarca()).append(" ").append(vehiculo.getModelo()).append("\n");
        factura.append("  Placa: ").append(vehiculo.getPlaca()).append("\n");
        factura.append("  Año: ").append(vehiculo.getAnioFabricacion()).append("\n\n");
        
        // Fechas
        factura.append("FECHAS:\n");
        factura.append("  Ingreso: ").append(fechaDeIngreso.format(formatter)).append("\n");
        if (fechaDeEntrega != null) {
            factura.append("  Entrega: ").append(fechaDeEntrega.format(formatter)).append("\n");
        }
        factura.append("  Estado: ").append(estado.getDescripcion()).append("\n\n");
        
        // Servicios
        factura.append("SERVICIOS REALIZADOS:\n");
        factura.append("-".repeat(70)).append("\n");
        
        if (listaServicios.isEmpty()) {
            factura.append("  No hay servicios registrados\n");
        } else {
            int num = 1;
            for (Servicio s : listaServicios) {
                factura.append(String.format("%d. %s\n", num++, s.generarResumen()));
                factura.append(String.format("   Costo: $%.2f\n", s.calcularCostoTotal()));
            }
        }
        
        factura.append("-".repeat(70)).append("\n\n");
        
        // Totales
        factura.append("TOTALES:\n");
        factura.append(String.format("  Subtotal:      $%,10.2f\n", calcularSubtotal()));
        factura.append(String.format("  IVA (21%%):     $%,10.2f\n", calcularIVA()));
        factura.append(String.format("  TOTAL:         $%,10.2f\n", calcularTotalConIVA()));
        
        if (!observaciones.isEmpty()) {
            factura.append("\nOBSERVACIONES:\n");
            factura.append("  ").append(observaciones).append("\n");
        }
        
        factura.append("\n").append("═".repeat(70)).append("\n");
        
        return factura.toString();
    }
    
    /**
     * Genera un resumen corto de la orden
     */
    public String generarResumenCorto() {
        return String.format("Orden #%d | %s %s (%s) | Estado: %s | Total: $%.2f",
                           idOrden,
                           vehiculo.getMarca(),
                           vehiculo.getModelo(),
                           vehiculo.getPlaca(),
                           estado.name(),
                           calcularTotalConIVA());
    }
    
    @Override
    public String toString() {
        return generarResumenCorto();
    }
}