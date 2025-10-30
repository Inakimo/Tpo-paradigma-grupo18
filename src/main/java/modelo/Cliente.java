package modelo;

import java.util.ArrayList;
import java.util.List;

public class Cliente extends Persona {
    private int id;
    private String direccion;
    private String telefono;
    private List<Vehiculo> vehiculos; // Lista de vehículos del cliente

    // Constructor básico
    public Cliente(int id, String nombre) {
        super(nombre, id);
        this.id = id;
        this.vehiculos = new ArrayList<>();
    }

    // Constructor completo
    public Cliente(int id, String nombre, String email, String direccion, String telefono) {
        super(nombre, email);
        
        // Validaciones
        if (telefono != null && telefono.trim().isEmpty()) {
            throw new IllegalArgumentException("El teléfono no puede estar vacío");
        }
        if (direccion != null && direccion.trim().isEmpty()) {
            throw new IllegalArgumentException("La dirección no puede estar vacía");
        }
        
        this.id = id;
        this.email = email;
        this.direccion = direccion;
        this.telefono = telefono;
        this.vehiculos = new ArrayList<>();
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        if (direccion != null && direccion.trim().isEmpty()) {
            throw new IllegalArgumentException("La dirección no puede estar vacía");
        }
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        if (telefono != null && telefono.trim().isEmpty()) {
            throw new IllegalArgumentException("El teléfono no puede estar vacío");
        }
        this.telefono = telefono;
    }

    /**
     * Obtiene la lista de vehículos del cliente (lectura)
     */
    public List<Vehiculo> getVehiculos() {
        return new ArrayList<>(vehiculos); // Retorna una copia para proteger la lista interna
    }

    /**
     * Obtiene el correo electrónico del cliente
     */
    public String getCorreoElectronico() {
        return this.email != null ? this.email : "No registrado";
    }

    // ========== MÉTODOS DE GESTIÓN DE VEHÍCULOS ==========

    /**
     * Agrega un vehículo a la lista del cliente (uso interno)
     * No debe ser llamado directamente - el constructor de Vehiculo lo maneja
     */
    protected void agregarVehiculo(Vehiculo vehiculo) {
        if (vehiculo != null && !vehiculos.contains(vehiculo)) {
            vehiculos.add(vehiculo);
        }
    }

    /**
     * Remueve un vehículo de la lista del cliente (uso interno)
     */
    protected void removerVehiculo(Vehiculo vehiculo) {
        vehiculos.remove(vehiculo);
    }

    /**
     * Obtiene la cantidad de vehículos del cliente
     */
    public int getCantidadVehiculos() {
        return vehiculos.size();
    }

    /**
     * Busca un vehículo por placa
     */
    public Vehiculo buscarVehiculoPorPlaca(String placa) {
        for (Vehiculo v : vehiculos) {
            if (v.getPlaca().equalsIgnoreCase(placa)) {
                return v;
            }
        }
        return null;
    }

    /**
     * Verifica si el cliente tiene vehículos registrados
     */
    public boolean tieneVehiculos() {
        return !vehiculos.isEmpty();
    }

    /**
     * Obtiene un listado de todas las placas de los vehículos del cliente
     */
    public List<String> obtenerPlacasVehiculos() {
        List<String> placas = new ArrayList<>();
        for (Vehiculo v : vehiculos) {
            placas.add(v.getPlaca());
        }
        return placas;
    }

    /**
     * Cuenta cuántos vehículos antiguos tiene el cliente (más de 15 años)
     */
    public int contarVehiculosAntiguos() {
        int count = 0;
        for (Vehiculo v : vehiculos) {
            if (v.esAntiguo()) {
                count++;
            }
        }
        return count;
    }

    // ========== MÉTODOS DE INFORMACIÓN ==========

    /**
     * Genera información de contacto del cliente
     */
    @Override
    public String generarContacto() {
        return String.format("Cliente: %s | ID: %d | Correo: %s | Teléfono: %s | Dirección: %s | Vehículos: %d",
                           getNombre(), id, getCorreoElectronico(), 
                           telefono != null ? telefono : "No registrado",
                           direccion != null ? direccion : "No registrada",
                           vehiculos.size());
    }

    /**
     * Genera un resumen completo del cliente incluyendo sus vehículos
     */
    public String generarResumenCompleto() {
        StringBuilder sb = new StringBuilder();
        sb.append(generarContacto()).append("\n");
        sb.append("=".repeat(60)).append("\n");
        
        if (vehiculos.isEmpty()) {
            sb.append("No tiene vehículos registrados");
        } else {
            sb.append("Vehículos registrados:\n");
            for (int i = 0; i < vehiculos.size(); i++) {
                sb.append(String.format("%d. %s\n", i + 1, vehiculos.get(i).toString()));
            }
        }
        
        return sb.toString();
    }

    @Override
    public String toString() {
        return String.format("Cliente ID: %d | %s | Vehículos: %d", 
                           id, getNombre(), vehiculos.size());
    }
}