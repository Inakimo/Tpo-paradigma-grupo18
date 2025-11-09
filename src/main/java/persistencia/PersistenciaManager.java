package persistencia;

import com.google.gson.*;
import modelo.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;


public class PersistenciaManager {
    private static final String DATA_DIR = "data";
    private static final String CLIENTES_FILE = DATA_DIR + "/clientes.json";
    private static final String VEHICULOS_FILE = DATA_DIR + "/vehiculos.json";
    private static final String MECANICOS_FILE = DATA_DIR + "/mecanicos.json";
    private static final String REPUESTOS_FILE = DATA_DIR + "/repuestos.json";
    private static final String ORDENES_FILE = DATA_DIR + "/ordenes.json";
    private static final String CONTADORES_FILE = DATA_DIR + "/contadores.json";

    private final Gson gson;

    public PersistenciaManager() {
        this.gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();

        try {
            Files.createDirectories(Paths.get(DATA_DIR));
        } catch (IOException e) {
            System.err.println("Error al crear directorio de datos: " + e.getMessage());
        }
    }

    public void guardarClientes(List<Cliente> clientes) {
        List<ClienteDTO> clientesDTO = new ArrayList<>();
        for (Cliente c : clientes) {
            clientesDTO.add(new ClienteDTO(c));
        }
        guardarJSON(CLIENTES_FILE, clientesDTO);
    }

    public void guardarVehiculos(List<Vehiculo> vehiculos) {
        List<VehiculoDTO> vehiculosDTO = new ArrayList<>();
        for (Vehiculo v : vehiculos) {
            vehiculosDTO.add(new VehiculoDTO(v));
        }
        guardarJSON(VEHICULOS_FILE, vehiculosDTO);
    }

    public void guardarMecanicos(List<Mecanico> mecanicos) {
        guardarJSON(MECANICOS_FILE, mecanicos);
    }

    public void guardarRepuestos(List<Repuesto> repuestos) {
        guardarJSON(REPUESTOS_FILE, repuestos);
    }

    public void guardarOrdenes(List<OrdenDeTrabajo> ordenes) {
        List<OrdenDTO> ordenesDTO = new ArrayList<>();
        for (OrdenDeTrabajo orden : ordenes) {
            ordenesDTO.add(new OrdenDTO(orden));
        }
        guardarJSON(ORDENES_FILE, ordenesDTO);
    }

    public void guardarContadores(int clienteId, int vehiculoId, int mecanicoId, 
                                   int repuestoId, int ordenId, int servicioId) {
        Map<String, Integer> contadores = new HashMap<>();
        contadores.put("clienteIdCounter", clienteId);
        contadores.put("vehiculoIdCounter", vehiculoId);
        contadores.put("mecanicoIdCounter", mecanicoId);
        contadores.put("repuestoIdCounter", repuestoId);
        contadores.put("ordenIdCounter", ordenId);
        contadores.put("servicioIdCounter", servicioId);
        guardarJSON(CONTADORES_FILE, contadores);
    }

    public List<Cliente> cargarClientes() {
        try {
            String json = leerArchivo(CLIENTES_FILE);
            ClienteDTO[] array = gson.fromJson(json, ClienteDTO[].class);
            if (array == null) return new ArrayList<>();
            
            List<Cliente> clientes = new ArrayList<>();
            for (ClienteDTO dto : array) {
                Cliente c = new Cliente(dto.id, dto.nombre, dto.email, dto.direccion, dto.telefono);
                clientes.add(c);
            }
            return clientes;
        } catch (Exception e) {
            System.out.println("No se encontraron datos de clientes, iniciando con lista vacía.");
            System.out.println("ERROR: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<Vehiculo> cargarVehiculos(List<Cliente> clientes) {
        try {
            String json = leerArchivo(VEHICULOS_FILE);
            VehiculoDTO[] array = gson.fromJson(json, VehiculoDTO[].class);
            if (array == null) return new ArrayList<>();

            List<Vehiculo> vehiculos = new ArrayList<>();
            for (VehiculoDTO dto : array) {
                Cliente propietario = buscarClientePorId(clientes, dto.propietarioId);
                if (propietario != null) {
                    Vehiculo v = new Vehiculo(dto.idVehiculo, dto.marca, dto.modelo, 
                                             dto.placa, dto.anioFabricacion, propietario);
                    vehiculos.add(v);
                }
            }
            return vehiculos;
        } catch (Exception e) {
            System.out.println("No se encontraron datos de vehículos, iniciando con lista vacía.");
            return new ArrayList<>();
        }
    }

    public List<Mecanico> cargarMecanicos() {
        try {
            String json = leerArchivo(MECANICOS_FILE);
            Mecanico[] array = gson.fromJson(json, Mecanico[].class);
            return array != null ? new ArrayList<>(Arrays.asList(array)) : new ArrayList<>();
        } catch (Exception e) {
            System.out.println("No se encontraron datos de mecánicos, iniciando con lista vacía.");
            return new ArrayList<>();
        }
    }

    public List<Repuesto> cargarRepuestos() {
        try {
            String json = leerArchivo(REPUESTOS_FILE);
            Repuesto[] array = gson.fromJson(json, Repuesto[].class);
            return array != null ? new ArrayList<>(Arrays.asList(array)) : new ArrayList<>();
        } catch (Exception e) {
            System.out.println("No se encontraron datos de repuestos, iniciando con lista vacía.");
            return new ArrayList<>();
        }
    }

    public List<OrdenDeTrabajo> cargarOrdenes(List<Vehiculo> vehiculos, List<Mecanico> mecanicos, 
                                               List<Repuesto> repuestos) {
        try {
            String json = leerArchivo(ORDENES_FILE);
            OrdenDTO[] array = gson.fromJson(json, OrdenDTO[].class);
            if (array == null) return new ArrayList<>();

            List<OrdenDeTrabajo> ordenes = new ArrayList<>();
            for (OrdenDTO dto : array) {
                OrdenDeTrabajo orden = dto.toOrden(vehiculos, mecanicos, repuestos);
                if (orden != null) {
                    ordenes.add(orden);
                }
            }
            return ordenes;
        } catch (Exception e) {
            System.out.println("No se encontraron datos de órdenes, iniciando con lista vacía.");
            return new ArrayList<>();
        }
    }

    public Map<String, Integer> cargarContadores() {
        try {
            String json = leerArchivo(CONTADORES_FILE);
            java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<Map<String, Integer>>(){}.getType();
            Map<String, Integer> contadores = gson.fromJson(json, type);
            return contadores != null ? contadores : inicializarContadores();
        } catch (Exception e) {
            System.out.println("No se encontraron contadores, iniciando en 1.");
            return inicializarContadores();
        }
    }

    private void guardarJSON(String archivo, Object datos) {
        try {
            String json = gson.toJson(datos);
            Files.write(Paths.get(archivo), json.getBytes());
        } catch (IOException e) {
            System.err.println("Error al guardar " + archivo + ": " + e.getMessage());
        }
    }

    private String leerArchivo(String archivo) throws IOException {
        return new String(Files.readAllBytes(Paths.get(archivo)));
    }

    private Cliente buscarClientePorId(List<Cliente> clientes, int id) {
        for (Cliente c : clientes) {
            if (c.getId() == id) return c;
        }
        return null;
    }

    private Map<String, Integer> inicializarContadores() {
        Map<String, Integer> contadores = new HashMap<>();
        contadores.put("clienteIdCounter", 1);
        contadores.put("vehiculoIdCounter", 1);
        contadores.put("mecanicoIdCounter", 1);
        contadores.put("repuestoIdCounter", 1);
        contadores.put("ordenIdCounter", 1);
        contadores.put("servicioIdCounter", 1);
        return contadores;
    }

    private static class ClienteDTO {
        int id;
        String nombre;
        String email;
        String telefono;
        String direccion;

        ClienteDTO(Cliente c) {
            this.id = c.getId();
            this.nombre = c.getNombre();
            this.email = c.getCorreoElectronico();
            this.telefono = c.getTelefono();
            this.direccion = c.getDireccion();
        }
    }

    private static class VehiculoDTO {
        int idVehiculo;
        String marca;
        String modelo;
        String placa;
        int anioFabricacion;
        int propietarioId;

        VehiculoDTO(Vehiculo v) {
            this.idVehiculo = v.getIdVehiculo();
            this.marca = v.getMarca();
            this.modelo = v.getModelo();
            this.placa = v.getPlaca();
            this.anioFabricacion = v.getAnioFabricacion();
            this.propietarioId = v.getPropietario().getId();
        }
    }

    private static class OrdenDTO {
        int idOrden;
        int vehiculoId;
        Integer mecanicoId;
        String estado;
        String observaciones;
        LocalDateTime fechaIngreso;
        LocalDateTime fechaEntrega;
        List<ServicioDTO> servicios;
        Map<Integer, Integer> repuestos; // repuestoId -> cantidad

        OrdenDTO(OrdenDeTrabajo orden) {
            this.idOrden = orden.getIdOrden();
            this.vehiculoId = orden.getVehiculo().getIdVehiculo();
            this.mecanicoId = orden.getMecanicoAsignado() != null ? 
                             orden.getMecanicoAsignado().getIdMecanico() : null;
            this.estado = orden.getEstado().name();
            this.observaciones = orden.getObservaciones();
            this.fechaIngreso = orden.getFechaDeIngreso();
            this.fechaEntrega = orden.getFechaDeEntrega();

            this.servicios = new ArrayList<>();
            for (Servicio s : orden.getListaServicios()) {
                this.servicios.add(new ServicioDTO(s));
            }

            this.repuestos = new HashMap<>();
            for (Map.Entry<Repuesto, Integer> entry : orden.getRepuestosUtilizados().entrySet()) {
                this.repuestos.put(entry.getKey().getIdRepuesto(), entry.getValue());
            }
        }

        OrdenDeTrabajo toOrden(List<Vehiculo> vehiculos, List<Mecanico> mecanicos, 
                               List<Repuesto> repuestos) {
            Vehiculo vehiculo = null;
            for (Vehiculo v : vehiculos) {
                if (v.getIdVehiculo() == vehiculoId) {
                    vehiculo = v;
                    break;
                }
            }
            if (vehiculo == null) return null;

            OrdenDeTrabajo orden = new OrdenDeTrabajo(idOrden, vehiculo, observaciones);
            try {
                orden.setEstado(enums.EstadoOrden.valueOf(estado));
            } catch (Exception e) {
            }
            if (fechaEntrega != null) {
                orden.setFechaDeEntrega(fechaEntrega);
            }

            if (mecanicoId != null) {
                for (Mecanico m : mecanicos) {
                    if (m.getIdMecanico() == mecanicoId) {
                        orden.setMecanicoAsignado(m);
                        break;
                    }
                }
            }

            for (ServicioDTO sDto : servicios) {
                Servicio servicio = sDto.toServicio();
                if (servicio != null) {
                    try {
                        orden.agregarServicio(servicio);
                    } catch (Exception e) {
                    }
                }
            }

            if (this.repuestos != null) {
                for (Map.Entry<Integer, Integer> entry : this.repuestos.entrySet()) {
                    int repuestoId = entry.getKey();
                    int cantidad = entry.getValue();

                    for (Repuesto rep : repuestos) {
                        if (rep.getIdRepuesto() == repuestoId) {
                            orden.restaurarRepuesto(rep, cantidad);
                            break;
                        }
                    }
                }
            }
            
            return orden;
        }
    }

    private static class ServicioDTO {
        String tipo;
        int idServicio;
        String descripcion;
        double costoBase;
        String nivelComplejidad;
        String tipoMantenimiento;
        Integer kilometraje;
        String codigoColor;
        String tipoPintura;
        Double superficie;
        Double horasTrabajo;
        Double costoPorHora;

        ServicioDTO(Servicio s) {
            this.idServicio = s.getIdServicio();
            this.descripcion = s.getDescripcion();
            this.costoBase = s.getCostoBase();
            
            if (s instanceof modelo.ServicioDiagnostico) {
                this.tipo = "DIAGNOSTICO";
                modelo.ServicioDiagnostico sd = (modelo.ServicioDiagnostico) s;
                this.nivelComplejidad = sd.getNivelComplejidad().name();
            } else if (s instanceof modelo.ServicioMantenimiento) {
                this.tipo = "MANTENIMIENTO";
                modelo.ServicioMantenimiento sm = (modelo.ServicioMantenimiento) s;
                this.tipoMantenimiento = sm.getTipoMantenimiento();
                this.kilometraje = sm.getKilometrajeActual();
            } else if (s instanceof modelo.ServicioPintura) {
                this.tipo = "PINTURA";
                modelo.ServicioPintura sp = (modelo.ServicioPintura) s;
                this.codigoColor = sp.getCodigoColor();
                this.tipoPintura = sp.getTipoPintura();
                this.superficie = sp.getSuperficie();
            } else if (s instanceof modelo.ServicioReparacion sr) {
                this.tipo = "REPARACION";
                try {
                    this.horasTrabajo = (double) sr.getClass().getMethod("getHorasTrabajo").invoke(sr);
                    this.costoPorHora = (double) sr.getClass().getMethod("getCostoPorHora").invoke(sr);
                } catch (Exception e) {
                    this.horasTrabajo = 0.0;
                    this.costoPorHora = 0.0;
                }
            } else {
                this.tipo = "GENERICO";
            }
        }

        Servicio toServicio() {
            try {
                switch (tipo) {
                    case "DIAGNOSTICO":
                        enums.NivelComplejidad nivel = enums.NivelComplejidad.valueOf(nivelComplejidad);
                        return new modelo.ServicioDiagnostico(idServicio, descripcion, costoBase, nivel, false);
                    case "MANTENIMIENTO":
                        return new modelo.ServicioMantenimiento(idServicio, descripcion, costoBase, 
                                                               tipoMantenimiento, kilometraje);
                    case "PINTURA":
                        return new modelo.ServicioPintura(idServicio, descripcion, costoBase, 
                                                         codigoColor, tipoPintura, superficie);
                    case "REPARACION":
                        return new modelo.ServicioReparacion(idServicio, descripcion, costoBase, 
                                                            horasTrabajo, costoPorHora);
                    default:
                        return new Servicio(descripcion, costoBase);
                }
            } catch (Exception e) {
                return new Servicio(descripcion, costoBase);
            }
        }
    }

    private static class LocalDateTimeAdapter implements JsonSerializer<LocalDateTime>, JsonDeserializer<LocalDateTime> {
        @Override
        public JsonElement serialize(LocalDateTime dateTime, java.lang.reflect.Type type, JsonSerializationContext context) {
            return new JsonPrimitive(dateTime.toString());
        }

        @Override
        public LocalDateTime deserialize(JsonElement json, java.lang.reflect.Type type, JsonDeserializationContext context) 
                throws JsonParseException {
            return LocalDateTime.parse(json.getAsString());
        }
    }
}
