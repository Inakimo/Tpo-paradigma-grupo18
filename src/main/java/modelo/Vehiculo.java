package modelo;

public class Vehiculo {
    private int idVehiculo;
    private String marca;
    private String modelo;
    private String placa;
    private int anioFabricacion;
    private Cliente propietario; // Relación con Cliente

    // Constructor completo
    public Vehiculo(int idVehiculo, String marca, String modelo, String placa, 
                    int anioFabricacion, Cliente propietario) {
        // Validaciones
        if (marca == null || marca.trim().isEmpty()) {
            throw new IllegalArgumentException("La marca no puede estar vacía");
        }
        if (modelo == null || modelo.trim().isEmpty()) {
            throw new IllegalArgumentException("El modelo no puede estar vacío");
        }
        if (placa == null || placa.trim().isEmpty()) {
            throw new IllegalArgumentException("La placa no puede estar vacía");
        }
        if (anioFabricacion < 1900 || anioFabricacion > java.time.Year.now().getValue() + 1) {
            throw new IllegalArgumentException("Año de fabricación inválido");
        }
        if (propietario == null) {
            throw new IllegalArgumentException("El vehículo debe tener un propietario");
        }

        this.idVehiculo = idVehiculo;
        this.marca = marca;
        this.modelo = modelo;
        this.placa = placa;
        this.anioFabricacion = anioFabricacion;
        this.propietario = propietario;
        
        // Registrar el vehículo en la lista del cliente
        propietario.agregarVehiculo(this);
    }

    // Constructor sin ID (para cuando se genera automáticamente)
    public Vehiculo(String marca, String modelo, String placa, 
                    int anioFabricacion, Cliente propietario) {
        this(0, marca, modelo, placa, anioFabricacion, propietario);
    }

    // Getters
    public int getIdVehiculo() {
        return idVehiculo;
    }

    public void setIdVehiculo(int idVehiculo) {
        this.idVehiculo = idVehiculo;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        if (marca == null || marca.trim().isEmpty()) {
            throw new IllegalArgumentException("La marca no puede estar vacía");
        }
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        if (modelo == null || modelo.trim().isEmpty()) {
            throw new IllegalArgumentException("El modelo no puede estar vacío");
        }
        this.modelo = modelo;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        if (placa == null || placa.trim().isEmpty()) {
            throw new IllegalArgumentException("La placa no puede estar vacía");
        }
        this.placa = placa;
    }

    public int getAnioFabricacion() {
        return anioFabricacion;
    }

    public void setAnioFabricacion(int anioFabricacion) {
        if (anioFabricacion < 1900 || anioFabricacion > java.time.Year.now().getValue() + 1) {
            throw new IllegalArgumentException("Año de fabricación inválido");
        }
        this.anioFabricacion = anioFabricacion;
    }

    public Cliente getPropietario() {
        return propietario;
    }

    public void setPropietario(Cliente propietario) {
        if (propietario == null) {
            throw new IllegalArgumentException("El vehículo debe tener un propietario");
        }
        
        // Remover de la lista del propietario anterior si existe
        if (this.propietario != null) {
            this.propietario.removerVehiculo(this);
        }
        
        // Asignar nuevo propietario y agregarlo a su lista
        this.propietario = propietario;
        propietario.agregarVehiculo(this);
    }

    /**
     * Obtiene el año de fabricación como String
     */
    public String getAnio() {
        return String.valueOf(anioFabricacion);
    }

    /**
     * Calcula la antigüedad del vehículo en años
     */
    public int calcularAntiguedad() {
        return java.time.Year.now().getValue() - anioFabricacion;
    }

    /**
     * Verifica si el vehículo es considerado antiguo (más de 15 años)
     */
    public boolean esAntiguo() {
        return calcularAntiguedad() > 15;
    }

    /**
     * Obtiene información completa del vehículo incluyendo propietario
     */
    public String obtenerInformacionCompleta() {
        return toString() + "\nPropietario: " + propietario.getNombre() + 
               " (ID: " + propietario.getId() + ")";
    }

    @Override
    public String toString() {
        return String.format("Vehículo ID: %d | %s %s | Placa: %s | Año: %d", 
                           idVehiculo, marca, modelo, placa, anioFabricacion);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Vehiculo vehiculo = (Vehiculo) obj;
        return placa.equals(vehiculo.placa); // Dos vehículos son iguales si tienen la misma placa
    }

    @Override
    public int hashCode() {
        return placa.hashCode();
    }
}