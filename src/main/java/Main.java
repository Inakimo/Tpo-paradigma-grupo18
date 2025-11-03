import enums.EstadoOrden;
import enums.NivelComplejidad;
import gui.MainWindow;
import modelo.*;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static List<Cliente> clientes = new ArrayList<>();
    private static List<Vehiculo> vehiculos = new ArrayList<>();
    private static List<OrdenDeTrabajo> ordenes = new ArrayList<>();
    private static List<Mecanico> mecanicos = new ArrayList<>();
    private static List<Repuesto> repuestos = new ArrayList<>();
    private static int clienteIdCounter = 1;
    private static int vehiculoIdCounter = 1;
    private static int ordenIdCounter = 1;
    private static int servicioIdCounter = 1;
    private static int mecanicoIdCounter = 1;
    private static int repuestoIdCounter = 1;

    public static void main(String[] args) {
       
        SwingUtilities.invokeLater(() -> {
            MainWindow window = new MainWindow();
            window.setVisible(true);
        });
    }

    
    public static void mainConsola(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            System.out.println("\n╔════════════════════════════════════════╗");
            System.out.println("║       SISTEMA DE TALLER MECÁNICO      ║");
            System.out.println("╚════════════════════════════════════════╝");
            System.out.println("1. Gestionar Clientes");
            System.out.println("2. Gestionar Vehículos");
            System.out.println("3. Gestionar Mecánicos");
            System.out.println("4. Gestionar Repuestos");
            System.out.println("5. Gestionar Órdenes de Trabajo");
            System.out.println("6. Ver Reportes");
            System.out.println("7. Salir");
            System.out.print("Seleccione una opción: ");

            if (!scanner.hasNextInt()) {
                System.out.println("⚠ Por favor ingrese un número válido.");
                scanner.next();
                continue;
            }

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    gestionarClientes(scanner);
                    break;
                case 2:
                    gestionarVehiculos(scanner);
                    break;
                case 3:
                    gestionarMecanicos(scanner);
                    break;
                case 4:
                    gestionarRepuestos(scanner);
                    break;
                case 5:
                    gestionarOrdenesDeTrabajo(scanner);
                    break;
                case 6:
                    verReportes();
                    break;
                case 7:
                    System.out.println("\n✓ Saliendo del sistema...");
                    exit = true;
                    break;
                default:
                    System.out.println("⚠ Opción no válida. Intente nuevamente.");
            }
        }

        scanner.close();
    }

    // ========== GESTIÓN DE CLIENTES ==========

    private static void gestionarClientes(Scanner scanner) {
        System.out.println("\n=== Gestión de Clientes ===");
        System.out.println("1. Agregar Cliente");
        System.out.println("2. Ver Clientes");
        System.out.println("3. Ver Detalle de Cliente");
        System.out.println("4. Regresar");
        System.out.print("Seleccione una opción: ");

        if (!scanner.hasNextInt()) {
            System.out.println("⚠ Opción inválida.");
            scanner.next();
            return;
        }

        int opcion = scanner.nextInt();
        scanner.nextLine();

        switch (opcion) {
            case 1:
                agregarCliente(scanner);
                break;
            case 2:
                verClientes();
                break;
            case 3:
                verDetalleCliente(scanner);
                break;
            case 4:
                break;
            default:
                System.out.println("⚠ Opción inválida.");
        }
    }

    private static void agregarCliente(Scanner scanner) {
        System.out.println("\n--- Agregar Nuevo Cliente ---");
        System.out.print("Nombre: ");
        String nombre = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Dirección: ");
        String direccion = scanner.nextLine();
        System.out.print("Teléfono: ");
        String telefono = scanner.nextLine();

        try {
            Cliente cliente = new Cliente(clienteIdCounter++, nombre, email, direccion, telefono);
            clientes.add(cliente);
            System.out.println("\n✓ Cliente agregado correctamente con ID: " + cliente.getId());
        } catch (Exception e) {
            System.out.println("\n⚠ Error: " + e.getMessage());
        }
    }

    private static void verClientes() {
        if (clientes.isEmpty()) {
            System.out.println("\n⚠ No hay clientes registrados.");
            return;
        }

        System.out.println("\n=== Lista de Clientes ===");
        for (Cliente c : clientes) {
            System.out.println(c.generarContacto());
        }
    }

    private static void verDetalleCliente(Scanner scanner) {
        if (clientes.isEmpty()) {
            System.out.println("\n⚠ No hay clientes registrados.");
            return;
        }

        System.out.print("\nIngrese el ID del cliente: ");
        if (!scanner.hasNextInt()) {
            System.out.println("⚠ ID inválido.");
            scanner.next();
            return;
        }
        int idBuscar = scanner.nextInt();
        scanner.nextLine();

        Cliente cliente = buscarClientePorId(idBuscar);
        if (cliente != null) {
            System.out.println("\n" + cliente.generarResumenCompleto());
        } else {
            System.out.println("\n⚠ Cliente no encontrado.");
        }
    }

    // ========== GESTIÓN DE VEHÍCULOS ==========

    private static void gestionarVehiculos(Scanner scanner) {
        System.out.println("\n=== Gestión de Vehículos ===");
        System.out.println("1. Registrar Vehículo");
        System.out.println("2. Ver Todos los Vehículos");
        System.out.println("3. Buscar Vehículo por Placa");
        System.out.println("4. Regresar");
        System.out.print("Seleccione una opción: ");

        if (!scanner.hasNextInt()) {
            System.out.println("⚠ Opción inválida.");
            scanner.next();
            return;
        }

        int opcion = scanner.nextInt();
        scanner.nextLine();

        switch (opcion) {
            case 1:
                registrarVehiculo(scanner);
                break;
            case 2:
                verTodosLosVehiculos();
                break;
            case 3:
                buscarVehiculoPorPlacaInteractivo(scanner);
                break;
            case 4:
                break;
            default:
                System.out.println("⚠ Opción inválida.");
        }
    }

    private static void registrarVehiculo(Scanner scanner) {
        if (clientes.isEmpty()) {
            System.out.println("\n⚠ Error: Debe registrar al menos un cliente antes de agregar vehículos.");
            return;
        }

        System.out.println("\n=== Clientes Disponibles ===");
        for (Cliente c : clientes) {
            System.out.println("ID: " + c.getId() + " | " + c.getNombre() + " | Tel: " + c.getTelefono());
        }

        System.out.print("\nIngrese el ID del cliente propietario: ");
        if (!scanner.hasNextInt()) {
            System.out.println("⚠ ID inválido.");
            scanner.next();
            return;
        }
        int idCliente = scanner.nextInt();
        scanner.nextLine();

        Cliente propietario = buscarClientePorId(idCliente);
        if (propietario == null) {
            System.out.println("\n⚠ Cliente no encontrado.");
            return;
        }

        System.out.println("\n--- Datos del Vehículo ---");
        System.out.print("Marca: ");
        String marca = scanner.nextLine();
        System.out.print("Modelo: ");
        String modelo = scanner.nextLine();
        System.out.print("Placa: ");
        String placa = scanner.nextLine();
        System.out.print("Año de fabricación: ");

        if (!scanner.hasNextInt()) {
            System.out.println("⚠ Año inválido.");
            scanner.next();
            return;
        }
        int anio = scanner.nextInt();
        scanner.nextLine();

        try {
            Vehiculo vehiculo = new Vehiculo(vehiculoIdCounter++, marca, modelo, placa, anio, propietario);
            vehiculos.add(vehiculo);

            System.out.println("\n✓ Vehículo registrado correctamente!");
            System.out.println("  ID: " + vehiculo.getIdVehiculo());
            System.out.println("  Placa: " + vehiculo.getPlaca());
            System.out.println("  Propietario: " + propietario.getNombre());
        } catch (Exception e) {
            System.out.println("\n⚠ Error: " + e.getMessage());
        }
    }

    private static void buscarVehiculoPorPlacaInteractivo(Scanner scanner) {
        System.out.print("\nIngrese la placa del vehículo: ");
        String placaBuscar = scanner.nextLine();
        Vehiculo vehiculo = buscarVehiculoPorPlaca(placaBuscar);

        if (vehiculo != null) {
            System.out.println("\n=== Vehículo Encontrado ===");
            System.out.println(vehiculo.obtenerInformacionCompleta());
        } else {
            System.out.println("\n⚠ Vehículo no encontrado.");
        }
    }

    // ========== GESTIÓN DE MECÁNICOS ==========

    private static void gestionarMecanicos(Scanner scanner) {
        System.out.println("\n=== Gestión de Mecánicos ===");
        System.out.println("1. Agregar Mecánico");
        System.out.println("2. Ver Mecánicos");
        System.out.println("3. Cambiar Disponibilidad");
        System.out.println("4. Regresar");
        System.out.print("Seleccione una opción: ");

        if (!scanner.hasNextInt()) {
            System.out.println("⚠ Opción inválida.");
            scanner.next();
            return;
        }

        int opcion = scanner.nextInt();
        scanner.nextLine();

        switch (opcion) {
            case 1:
                agregarMecanico(scanner);
                break;
            case 2:
                verMecanicos();
                break;
            case 3:
                cambiarDisponibilidadMecanico(scanner);
                break;
            case 4:
                break;
            default:
                System.out.println("⚠ Opción inválida.");
        }
    }

    private static void agregarMecanico(Scanner scanner) {
        System.out.println("\n--- Agregar Nuevo Mecánico ---");
        System.out.print("Nombre: ");
        String nombre = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Especialidad: ");
        String especialidad = scanner.nextLine();
        System.out.print("Teléfono: ");
        String telefono = scanner.nextLine();

        try {
            Mecanico mecanico = new Mecanico(mecanicoIdCounter++, nombre, email, especialidad, telefono);
            mecanicos.add(mecanico);
            System.out.println("\n✓ Mecánico agregado correctamente con ID: " + mecanico.getIdMecanico());
        } catch (Exception e) {
            System.out.println("\n⚠ Error: " + e.getMessage());
        }
    }

    private static void verMecanicos() {
        if (mecanicos.isEmpty()) {
            System.out.println("\n⚠ No hay mecánicos registrados.");
            return;
        }

        System.out.println("\n=== Lista de Mecánicos ===");
        for (Mecanico m : mecanicos) {
            System.out.println(m.generarContacto());
        }
    }

    private static void cambiarDisponibilidadMecanico(Scanner scanner) {
        if (mecanicos.isEmpty()) {
            System.out.println("\n⚠ No hay mecánicos registrados.");
            return;
        }

        System.out.println("\n=== Mecánicos ===");
        for (Mecanico m : mecanicos) {
            System.out.println(m.generarResumen());
        }

        System.out.print("\nIngrese el ID del mecánico: ");
        if (!scanner.hasNextInt()) {
            System.out.println("⚠ ID inválido.");
            scanner.next();
            return;
        }
        int id = scanner.nextInt();
        scanner.nextLine();

        Mecanico mecanico = buscarMecanicoPorId(id);
        if (mecanico != null) {
            mecanico.setDisponible(!mecanico.isDisponible());
            System.out.println("\n✓ Estado actualizado: " + (mecanico.isDisponible() ? "Disponible" : "Ocupado"));
        } else {
            System.out.println("\n⚠ Mecánico no encontrado.");
        }
    }

    // ========== GESTIÓN DE REPUESTOS ==========

    private static void gestionarRepuestos(Scanner scanner) {
        System.out.println("\n=== Gestión de Repuestos ===");
        System.out.println("1. Agregar Repuesto");
        System.out.println("2. Ver Repuestos");
        System.out.println("3. Actualizar Stock");
        System.out.println("4. Regresar");
        System.out.print("Seleccione una opción: ");

        if (!scanner.hasNextInt()) {
            System.out.println("⚠ Opción inválida.");
            scanner.next();
            return;
        }

        int opcion = scanner.nextInt();
        scanner.nextLine();

        switch (opcion) {
            case 1:
                agregarRepuesto(scanner);
                break;
            case 2:
                verRepuestos();
                break;
            case 3:
                actualizarStockRepuesto(scanner);
                break;
            case 4:
                break;
            default:
                System.out.println("⚠ Opción inválida.");
        }
    }

    private static void agregarRepuesto(Scanner scanner) {
        System.out.println("\n--- Agregar Nuevo Repuesto ---");
        System.out.print("Código: ");
        String codigo = scanner.nextLine();
        System.out.print("Nombre: ");
        String nombre = scanner.nextLine();
        System.out.print("Descripción: ");
        String descripcion = scanner.nextLine();
        System.out.print("Precio unitario: ");

        if (!scanner.hasNextDouble()) {
            System.out.println("⚠ Precio inválido.");
            scanner.next();
            return;
        }
        double precio = scanner.nextDouble();

        System.out.print("Stock inicial: ");
        if (!scanner.hasNextInt()) {
            System.out.println("⚠ Stock inválido.");
            scanner.next();
            return;
        }
        int stock = scanner.nextInt();
        scanner.nextLine();

        try {
            Repuesto repuesto = new Repuesto(repuestoIdCounter++, codigo, nombre, descripcion, precio, stock);
            repuestos.add(repuesto);
            System.out.println("\n✓ Repuesto agregado correctamente con ID: " + repuesto.getIdRepuesto());
        } catch (Exception e) {
            System.out.println("\n⚠ Error: " + e.getMessage());
        }
    }

    private static void verRepuestos() {
        if (repuestos.isEmpty()) {
            System.out.println("\n⚠ No hay repuestos registrados.");
            return;
        }

        System.out.println("\n=== Lista de Repuestos ===");
        for (Repuesto r : repuestos) {
            System.out.println(r.generarResumen());
        }
    }

    private static void actualizarStockRepuesto(Scanner scanner) {
        if (repuestos.isEmpty()) {
            System.out.println("\n⚠ No hay repuestos registrados.");
            return;
        }

        System.out.println("\n=== Repuestos ===");
        for (Repuesto r : repuestos) {
            System.out.println(r.generarResumen());
        }

        System.out.print("\nIngrese el ID del repuesto: ");
        if (!scanner.hasNextInt()) {
            System.out.println("⚠ ID inválido.");
            scanner.next();
            return;
        }
        int id = scanner.nextInt();
        scanner.nextLine();

        Repuesto repuesto = buscarRepuestoPorId(id);
        if (repuesto == null) {
            System.out.println("\n⚠ Repuesto no encontrado.");
            return;
        }

        System.out.println("Stock actual: " + repuesto.getStockDisponible());
        System.out.print("Cantidad a agregar (negativo para restar): ");
        if (!scanner.hasNextInt()) {
            System.out.println("⚠ Cantidad inválida.");
            scanner.next();
            return;
        }
        int cantidad = scanner.nextInt();
        scanner.nextLine();

        try {
            if (cantidad > 0) {
                repuesto.aumentarStock(cantidad);
                System.out.println("\n✓ Stock actualizado: " + repuesto.getStockDisponible());
            } else if (cantidad < 0) {
                if (repuesto.reducirStock(-cantidad)) {
                    System.out.println("\n✓ Stock actualizado: " + repuesto.getStockDisponible());
                } else {
                    System.out.println("\n⚠ Stock insuficiente.");
                }
            }
        } catch (Exception e) {
            System.out.println("\n⚠ Error: " + e.getMessage());
        }
    }

    // ========== GESTIÓN DE ÓRDENES DE TRABAJO ==========

    private static void gestionarOrdenesDeTrabajo(Scanner scanner) {
        System.out.println("\n=== Gestión de Órdenes de Trabajo ===");
        System.out.println("1. Crear Nueva Orden");
        System.out.println("2. Ver Todas las Órdenes");
        System.out.println("3. Ver Detalle de Orden");
        System.out.println("4. Asignar Mecánico a Orden");
        System.out.println("5. Agregar Servicio a Orden");
        System.out.println("6. Agregar Repuesto a Orden");
        System.out.println("7. Cambiar Estado de Orden");
        System.out.println("8. Generar Factura");
        System.out.println("9. Regresar");
        System.out.print("Seleccione una opción: ");

        if (!scanner.hasNextInt()) {
            System.out.println("⚠ Opción inválida.");
            scanner.next();
            return;
        }

        int opcion = scanner.nextInt();
        scanner.nextLine();

        switch (opcion) {
            case 1:
                crearNuevaOrden(scanner);
                break;
            case 2:
                verTodasLasOrdenes();
                break;
            case 3:
                verDetalleOrden(scanner);
                break;
            case 4:
                asignarMecanicoAOrden(scanner);
                break;
            case 5:
                agregarServicioAOrden(scanner);
                break;
            case 6:
                agregarRepuestoAOrden(scanner);
                break;
            case 7:
                cambiarEstadoOrden(scanner);
                break;
            case 8:
                generarFactura(scanner);
                break;
            case 9:
                break;
            default:
                System.out.println("⚠ Opción inválida.");
        }
    }

    private static void crearNuevaOrden(Scanner scanner) {
        if (vehiculos.isEmpty()) {
            System.out.println("\n⚠ Error: Debe registrar al menos un vehículo antes de crear órdenes.");
            return;
        }

        System.out.println("\n=== Vehículos Disponibles ===");
        for (Vehiculo v : vehiculos) {
            System.out.println("ID: " + v.getIdVehiculo() + " | " + v.getMarca() + " " +
                    v.getModelo() + " | Placa: " + v.getPlaca() +
                    " | Dueño: " + v.getPropietario().getNombre());
        }

        System.out.print("\nIngrese el ID del vehículo: ");
        if (!scanner.hasNextInt()) {
            System.out.println("⚠ ID inválido.");
            scanner.next();
            return;
        }
        int idVehiculo = scanner.nextInt();
        scanner.nextLine();

        Vehiculo vehiculo = buscarVehiculoPorId(idVehiculo);
        if (vehiculo == null) {
            System.out.println("\n⚠ Vehículo no encontrado.");
            return;
        }

        System.out.print("Observaciones iniciales (opcional): ");
        String observaciones = scanner.nextLine();

        try {
            OrdenDeTrabajo orden = new OrdenDeTrabajo(ordenIdCounter++, vehiculo, observaciones);
            ordenes.add(orden);

            System.out.println("\n✓ Orden de trabajo creada correctamente!");
            System.out.println("  Orden #" + orden.getIdOrden());
            System.out.println("  Vehículo: " + vehiculo.getMarca() + " " + vehiculo.getModelo());
            System.out.println("  Cliente: " + vehiculo.getPropietario().getNombre());
            System.out.println("  Estado: " + orden.getEstado());
        } catch (Exception e) {
            System.out.println("\n⚠ Error: " + e.getMessage());
        }
    }

    private static void asignarMecanicoAOrden(Scanner scanner) {
        if (ordenes.isEmpty()) {
            System.out.println("\n⚠ No hay órdenes registradas.");
            return;
        }

        if (mecanicos.isEmpty()) {
            System.out.println("\n⚠ No hay mecánicos registrados. Debe agregar mecánicos primero.");
            return;
        }

        // Mostrar órdenes sin mecánico o con mecánico asignado
        System.out.println("\n=== Órdenes Registradas ===");
        for (OrdenDeTrabajo o : ordenes) {
            String mecanicoInfo = o.getMecanicoAsignado() != null ?
                    " | Mecánico: " + o.getMecanicoAsignado().getNombre() :
                    " | Sin mecánico asignado";
            System.out.println("ID: " + o.getIdOrden() + " | " +
                    o.getVehiculo().getPlaca() + " - " +
                    o.getVehiculo().getMarca() + " " + o.getVehiculo().getModelo() +
                    mecanicoInfo);
        }

        System.out.print("\nIngrese el ID de la orden: ");
        if (!scanner.hasNextInt()) {
            System.out.println("⚠ ID inválido.");
            scanner.next();
            return;
        }
        int idOrden = scanner.nextInt();
        scanner.nextLine();

        OrdenDeTrabajo orden = buscarOrdenPorId(idOrden);
        if (orden == null) {
            System.out.println("\n⚠ Orden no encontrada.");
            return;
        }

        // Mostrar mecánicos disponibles
        System.out.println("\n=== Mecánicos Disponibles ===");
        List<Mecanico> mecanicosDisponibles = new ArrayList<>();
        for (Mecanico m : mecanicos) {
            if (m.isDisponible()) {
                mecanicosDisponibles.add(m);
                System.out.println(m.generarResumen());
            }
        }

        if (mecanicosDisponibles.isEmpty()) {
            System.out.println("\n⚠ No hay mecánicos disponibles en este momento.");
            return;
        }

        System.out.print("\nIngrese el ID del mecánico: ");
        if (!scanner.hasNextInt()) {
            System.out.println("⚠ ID inválido.");
            scanner.next();
            return;
        }
        int idMecanico = scanner.nextInt();
        scanner.nextLine();

        Mecanico mecanico = buscarMecanicoPorId(idMecanico);
        if (mecanico == null) {
            System.out.println("\n⚠ Mecánico no encontrado.");
            return;
        }

        if (!mecanico.isDisponible()) {
            System.out.println("\n⚠ Este mecánico no está disponible actualmente.");
            return;
        }

        // Si la orden ya tenía un mecánico, liberarlo
        if (orden.getMecanicoAsignado() != null) {
            orden.getMecanicoAsignado().setDisponible(true);
        }

        orden.setMecanicoAsignado(mecanico);
        mecanico.setDisponible(false);

        System.out.println("\n✓ Mecánico asignado correctamente!");
        System.out.println("  Orden #" + orden.getIdOrden());
        System.out.println("  Mecánico: " + mecanico.getNombre());
        System.out.println("  Especialidad: " + mecanico.getEspecialidad());
    }

    private static void agregarRepuestoAOrden(Scanner scanner) {
        if (ordenes.isEmpty()) {
            System.out.println("\n⚠ No hay órdenes registradas.");
            return;
        }

        if (repuestos.isEmpty()) {
            System.out.println("\n⚠ No hay repuestos registrados. Debe agregar repuestos primero.");
            return;
        }

        // Filtrar solo órdenes que pueden recibir repuestos
        List<OrdenDeTrabajo> ordenesDisponibles = new ArrayList<>();
        for (OrdenDeTrabajo o : ordenes) {
            if (o.getEstado() != EstadoOrden.ENTREGADA && o.getEstado() != EstadoOrden.CANCELADA) {
                ordenesDisponibles.add(o);
            }
        }

        if (ordenesDisponibles.isEmpty()) {
            System.out.println("\n⚠ No hay órdenes disponibles para agregar repuestos.");
            return;
        }

        System.out.println("\n=== Órdenes Disponibles ===");
        for (OrdenDeTrabajo o : ordenesDisponibles) {
            System.out.println("ID: " + o.getIdOrden() + " | " +
                    o.getVehiculo().getPlaca() + " - " +
                    o.getVehiculo().getMarca() + " " + o.getVehiculo().getModelo() +
                    " | Estado: " + o.getEstado());
        }

        System.out.print("\nIngrese el ID de la orden: ");
        if (!scanner.hasNextInt()) {
            System.out.println("⚠ ID inválido.");
            scanner.next();
            return;
        }
        int idOrden = scanner.nextInt();
        scanner.nextLine();

        OrdenDeTrabajo orden = buscarOrdenPorId(idOrden);
        if (orden == null || !ordenesDisponibles.contains(orden)) {
            System.out.println("\n⚠ Orden no válida.");
            return;
        }

        // Mostrar repuestos disponibles con stock
        System.out.println("\n=== Repuestos Disponibles ===");
        for (Repuesto r : repuestos) {
            if (r.getStockDisponible() > 0) {
                System.out.println(r.generarResumen());
            }
        }

        System.out.print("\nIngrese el ID del repuesto: ");
        if (!scanner.hasNextInt()) {
            System.out.println("⚠ ID inválido.");
            scanner.next();
            return;
        }
        int idRepuesto = scanner.nextInt();
        scanner.nextLine();

        Repuesto repuesto = buscarRepuestoPorId(idRepuesto);
        if (repuesto == null) {
            System.out.println("\n⚠ Repuesto no encontrado.");
            return;
        }

        if (repuesto.getStockDisponible() == 0) {
            System.out.println("\n⚠ Repuesto sin stock disponible.");
            return;
        }

        System.out.print("Cantidad (disponible: " + repuesto.getStockDisponible() + "): ");
        if (!scanner.hasNextInt()) {
            System.out.println("⚠ Cantidad inválida.");
            scanner.next();
            return;
        }
        int cantidad = scanner.nextInt();
        scanner.nextLine();

        try {
            if (orden.agregarRepuesto(repuesto, cantidad)) {
                System.out.println("\n✓ Repuesto agregado correctamente a la orden #" + idOrden);
                System.out.println("  " + repuesto.getNombre() + " x" + cantidad);
                System.out.println("  Subtotal: $" + String.format("%.2f", repuesto.getPrecioUnitario() * cantidad));
                System.out.println("  Stock restante: " + repuesto.getStockDisponible());
            } else {
                System.out.println("\n⚠ No se pudo agregar el repuesto (stock insuficiente).");
            }
        } catch (Exception e) {
            System.out.println("\n⚠ Error: " + e.getMessage());
        }
    }

    private static void verTodasLasOrdenes() {
        if (ordenes.isEmpty()) {
            System.out.println("\n⚠ No hay órdenes registradas.");
            return;
        }

        System.out.println("\n=== LISTA DE ÓRDENES DE TRABAJO ===");
        System.out.println("═".repeat(80));

        for (OrdenDeTrabajo orden : ordenes) {
            System.out.println(orden.generarResumenCorto());
            System.out.println("  Cliente: " + orden.getCliente().getNombre() +
                    " | Servicios: " + orden.getCantidadServicios());
            System.out.println("-".repeat(80));
        }
    }

    private static void verDetalleOrden(Scanner scanner) {
        if (ordenes.isEmpty()) {
            System.out.println("\n⚠ No hay órdenes registradas.");
            return;
        }

        // Mostrar todas las órdenes disponibles
        System.out.println("\n=== Órdenes Registradas ===");
        for (OrdenDeTrabajo o : ordenes) {
            System.out.println("ID: " + o.getIdOrden() + " | " +
                    o.getVehiculo().getPlaca() + " - " +
                    o.getVehiculo().getMarca() + " " + o.getVehiculo().getModelo() +
                    " | Estado: " + o.getEstado().name() +
                    " | Cliente: " + o.getCliente().getNombre());
        }

        System.out.print("\nIngrese el ID de la orden: ");
        if (!scanner.hasNextInt()) {
            System.out.println("⚠ ID inválido.");
            scanner.next();
            return;
        }
        int idOrden = scanner.nextInt();
        scanner.nextLine();

        OrdenDeTrabajo orden = buscarOrdenPorId(idOrden);
        if (orden != null) {
            System.out.println("\n" + orden.generarFactura());
        } else {
            System.out.println("\n⚠ Orden no encontrada.");
        }
    }

    private static void agregarServicioAOrden(Scanner scanner) {
        if (ordenes.isEmpty()) {
            System.out.println("\n⚠ No hay órdenes registradas.");
            return;
        }

        // Filtrar solo órdenes que pueden recibir servicios
        List<OrdenDeTrabajo> ordenesDisponibles = new ArrayList<>();
        for (OrdenDeTrabajo o : ordenes) {
            if (o.getEstado() != EstadoOrden.ENTREGADA && o.getEstado() != EstadoOrden.CANCELADA) {
                ordenesDisponibles.add(o);
            }
        }

        if (ordenesDisponibles.isEmpty()) {
            System.out.println("\n⚠ No hay órdenes disponibles para agregar servicios.");
            System.out.println("   (Todas las órdenes están entregadas o canceladas)");
            return;
        }

        // Mostrar solo las órdenes válidas
        System.out.println("\n=== Órdenes Disponibles ===");
        for (OrdenDeTrabajo o : ordenesDisponibles) {
            System.out.println("ID: " + o.getIdOrden() + " | " +
                    o.getVehiculo().getPlaca() + " - " +
                    o.getVehiculo().getMarca() + " " + o.getVehiculo().getModelo() +
                    " | Estado: " + o.getEstado() +
                    " | Servicios: " + o.getCantidadServicios());
        }

        System.out.print("\nIngrese el ID de la orden: ");
        if (!scanner.hasNextInt()) {
            System.out.println("⚠ ID inválido.");
            scanner.next();
            return;
        }
        int idOrden = scanner.nextInt();
        scanner.nextLine();

        OrdenDeTrabajo orden = buscarOrdenPorId(idOrden);
        if (orden == null) {
            System.out.println("\n⚠ Orden no encontrada.");
            return;
        }

        // Verificar que la orden esté en la lista de disponibles
        if (!ordenesDisponibles.contains(orden)) {
            System.out.println("\n⚠ Esta orden no puede recibir servicios (Estado: " + orden.getEstado() + ")");
            return;
        }

        System.out.println("\n=== Tipo de Servicio ===");
        System.out.println("1. Diagnóstico");
        System.out.println("2. Mantenimiento");
        System.out.println("3. Pintura");
        System.out.println("4. Reparación");
        System.out.print("Seleccione el tipo: ");

        if (!scanner.hasNextInt()) {
            System.out.println("⚠ Opción inválida.");
            scanner.next();
            return;
        }
        int tipo = scanner.nextInt();
        scanner.nextLine();

        try {
            Servicio servicio = null;

            switch (tipo) {
                case 1:
                    servicio = crearServicioDiagnostico(scanner);
                    break;
                case 2:
                    servicio = crearServicioMantenimiento(scanner);
                    break;
                case 3:
                    servicio = crearServicioPintura(scanner);
                    break;
                case 4:
                    servicio = crearServicioReparacion(scanner);
                    break;
                default:
                    System.out.println("⚠ Tipo de servicio inválido.");
                    return;
            }

            if (servicio != null) {
                orden.agregarServicio(servicio);
                System.out.println("\n✓ Servicio agregado correctamente a la orden #" + idOrden);
                System.out.println("  " + servicio.generarResumen());
                System.out.println("  Total de servicios en la orden: " + orden.getCantidadServicios());
            }
        } catch (Exception e) {
            System.out.println("\n⚠ Error: " + e.getMessage());
        }
    }

    private static ServicioDiagnostico crearServicioDiagnostico(Scanner scanner) {
        System.out.println("\n--- Servicio de Diagnóstico ---");
        System.out.print("Descripción: ");
        String desc = scanner.nextLine();
        System.out.print("Costo base: ");
        double costo = scanner.nextDouble();
        scanner.nextLine();

        System.out.println("Nivel de complejidad (1=BAJO, 2=MEDIO, 3=ALTO): ");
        int nivelNum = scanner.nextInt();
        scanner.nextLine();

        NivelComplejidad nivel = NivelComplejidad.BAJO;
        if (nivelNum == 2) nivel = NivelComplejidad.MEDIO;
        else if (nivelNum == 3) nivel = NivelComplejidad.ALTO;

        return new ServicioDiagnostico(servicioIdCounter++, desc, costo, nivel, false);
    }

    private static ServicioMantenimiento crearServicioMantenimiento(Scanner scanner) {
        System.out.println("\n--- Servicio de Mantenimiento ---");
        System.out.print("Descripción: ");
        String desc = scanner.nextLine();
        System.out.print("Costo base: ");
        double costo = scanner.nextDouble();
        scanner.nextLine();

        System.out.print("Tipo (Preventivo/Correctivo): ");
        String tipo = scanner.nextLine();
        System.out.print("Kilometraje actual: ");
        int km = scanner.nextInt();
        scanner.nextLine();

        return new ServicioMantenimiento(servicioIdCounter++, desc, costo, tipo, km);
    }

    private static ServicioPintura crearServicioPintura(Scanner scanner) {
        System.out.println("\n--- Servicio de Pintura ---");
        System.out.print("Descripción: ");
        String desc = scanner.nextLine();
        System.out.print("Costo base (mano de obra): ");
        double costo = scanner.nextDouble();
        scanner.nextLine();

        System.out.print("Código de color: ");
        String color = scanner.nextLine();
        System.out.print("Tipo de pintura (Acrilica/Uretano/Laca): ");
        String tipo = scanner.nextLine();
        System.out.print("Superficie en m²: ");
        double superficie = scanner.nextDouble();
        scanner.nextLine();

        return new ServicioPintura(servicioIdCounter++, desc, costo, color, tipo, superficie);
    }

    private static ServicioReparacion crearServicioReparacion(Scanner scanner) {
        System.out.println("\n--- Servicio de Reparación ---");
        System.out.print("Descripción: ");
        String desc = scanner.nextLine();
        System.out.print("Costo de repuestos/materiales: ");
        double costo = scanner.nextDouble();
        scanner.nextLine();

        System.out.print("Horas de trabajo: ");
        double horas = scanner.nextDouble();
        scanner.nextLine();
        System.out.print("Costo por hora: ");
        double costoPorHora = scanner.nextDouble();
        scanner.nextLine();

        return new ServicioReparacion(servicioIdCounter++, desc, costo, horas, costoPorHora);
    }

    private static void cambiarEstadoOrden(Scanner scanner) {
        if (ordenes.isEmpty()) {
            System.out.println("\n⚠ No hay órdenes registradas.");
            return;
        }

        // Filtrar solo órdenes que pueden cambiar de estado
        List<OrdenDeTrabajo> ordenesModificables = new ArrayList<>();
        for (OrdenDeTrabajo o : ordenes) {
            if (o.getEstado() != EstadoOrden.ENTREGADA && o.getEstado() != EstadoOrden.CANCELADA) {
                ordenesModificables.add(o);
            }
        }

        if (ordenesModificables.isEmpty()) {
            System.out.println("\n⚠ No hay órdenes disponibles para cambiar de estado.");
            System.out.println("   (Todas las órdenes están entregadas o canceladas)");
            return;
        }

        // Mostrar solo las órdenes válidas
        System.out.println("\n=== Órdenes Disponibles ===");
        for (OrdenDeTrabajo o : ordenesModificables) {
            System.out.println("ID: " + o.getIdOrden() + " | " +
                    o.getVehiculo().getPlaca() + " - " +
                    o.getVehiculo().getMarca() + " " + o.getVehiculo().getModelo() +
                    " | Estado Actual: " + o.getEstado().getDescripcion() +
                    " | Servicios: " + o.getCantidadServicios());
        }

        System.out.print("\nIngrese el ID de la orden: ");
        if (!scanner.hasNextInt()) {
            System.out.println("⚠ ID inválido.");
            scanner.next();
            return;
        }
        int idOrden = scanner.nextInt();
        scanner.nextLine();

        OrdenDeTrabajo orden = buscarOrdenPorId(idOrden);
        if (orden == null) {
            System.out.println("\n⚠ Orden no encontrada.");
            return;
        }

        // Verificar que la orden esté en la lista de modificables
        if (!ordenesModificables.contains(orden)) {
            System.out.println("\n⚠ Esta orden no puede cambiar de estado (Estado: " + orden.getEstado() + ")");
            return;
        }

        EstadoOrden estadoActual = orden.getEstado();
        System.out.println("\nEstado actual: " + estadoActual.getDescripcion());

        // Mostrar solo las transiciones válidas según el estado actual
        System.out.println("\n=== Cambios de Estado Disponibles ===");
        List<Integer> opcionesValidas = new ArrayList<>();
        int numOpcion = 1;

        switch (estadoActual) {
            case PENDIENTE:
                System.out.println(numOpcion + ". Iniciar Trabajo (Pendiente → En Proceso)");
                opcionesValidas.add(1);
                numOpcion++;
                System.out.println(numOpcion + ". Cancelar Orden");
                opcionesValidas.add(4);
                break;

            case EN_PROCESO:
                System.out.println(numOpcion + ". Completar Orden (En Proceso → Completada)");
                opcionesValidas.add(2);
                numOpcion++;
                System.out.println(numOpcion + ". Marcar como Esperando Repuestos");
                opcionesValidas.add(5);
                numOpcion++;
                System.out.println(numOpcion + ". Cancelar Orden");
                opcionesValidas.add(4);
                break;

            case COMPLETADA:
                System.out.println(numOpcion + ". Entregar al Cliente (Completada → Entregada)");
                opcionesValidas.add(3);
                break;

            case ESPERANDO_REPUESTOS:
                System.out.println(numOpcion + ". Reanudar Trabajo (Esperando Repuestos → En Proceso)");
                opcionesValidas.add(6);
                numOpcion++;
                System.out.println(numOpcion + ". Cancelar Orden");
                opcionesValidas.add(4);
                break;

            case ESPERANDO_APROBACION:
                System.out.println(numOpcion + ". Iniciar Trabajo (Aprobado → En Proceso)");
                opcionesValidas.add(1);
                numOpcion++;
                System.out.println(numOpcion + ". Cancelar Orden");
                opcionesValidas.add(4);
                break;

            default:
                System.out.println("⚠ No hay transiciones disponibles para este estado.");
                return;
        }

        System.out.print("\nSeleccione una opción: ");
        if (!scanner.hasNextInt()) {
            System.out.println("⚠ Opción inválida.");
            scanner.next();
            return;
        }
        int opcionSeleccionada = scanner.nextInt();
        scanner.nextLine();

        if (opcionSeleccionada < 1 || opcionSeleccionada > opcionesValidas.size()) {
            System.out.println("⚠ Opción inválida.");
            return;
        }

        // Obtener la acción real basada en la opción seleccionada
        int accion = opcionesValidas.get(opcionSeleccionada - 1);

        try {
            switch (accion) {
                case 1: // Iniciar trabajo
                    orden.iniciarTrabajo();
                    System.out.println("\n✓ Orden iniciada correctamente.");
                    System.out.println("  Estado: " + estadoActual + " → " + orden.getEstado());
                    break;

                case 2: // Completar
                    orden.completarOrden();
                    System.out.println("\n✓ Orden completada. Lista para entrega.");
                    System.out.println("  Estado: " + estadoActual + " → " + orden.getEstado());
                    break;

                case 3: // Entregar
                    orden.entregar();
                    System.out.println("\n✓ Orden entregada al cliente.");
                    System.out.println("  Estado: " + estadoActual + " → " + orden.getEstado());
                    break;

                case 4: // Cancelar
                    System.out.print("Motivo de cancelación: ");
                    String motivo = scanner.nextLine();
                    orden.cancelar(motivo);
                    System.out.println("\n✓ Orden cancelada.");
                    System.out.println("  Estado: " + estadoActual + " → " + orden.getEstado());
                    break;

                case 5: // Esperando repuestos
                    orden.setEstado(EstadoOrden.ESPERANDO_REPUESTOS);
                    System.out.println("\n✓ Orden marcada como esperando repuestos.");
                    System.out.println("  Estado: " + estadoActual + " → " + orden.getEstado());
                    break;

                case 6: // Reanudar desde esperando repuestos
                    orden.setEstado(EstadoOrden.EN_PROCESO);
                    System.out.println("\n✓ Trabajo reanudado.");
                    System.out.println("  Estado: " + estadoActual + " → " + orden.getEstado());
                    break;

                default:
                    System.out.println("⚠ Opción inválida.");
            }
        } catch (Exception e) {
            System.out.println("\n⚠ Error: " + e.getMessage());
        }
    }

    private static void generarFactura(Scanner scanner) {
        if (ordenes.isEmpty()) {
            System.out.println("\n⚠ No hay órdenes registradas.");
            return;
        }

        // Mostrar todas las órdenes disponibles
        System.out.println("\n=== Órdenes Registradas ===");
        for (OrdenDeTrabajo o : ordenes) {
            System.out.println("ID: " + o.getIdOrden() + " | " +
                    o.getVehiculo().getPlaca() + " - " +
                    o.getVehiculo().getMarca() + " " + o.getVehiculo().getModelo() +
                    " | Estado: " + o.getEstado().name() +
                    " | Total: $" + String.format("%.2f", o.calcularTotalConIVA()));
        }

        System.out.print("\nIngrese el ID de la orden: ");
        if (!scanner.hasNextInt()) {
            System.out.println("⚠ ID inválido.");
            scanner.next();
            return;
        }
        int idOrden = scanner.nextInt();
        scanner.nextLine();

        OrdenDeTrabajo orden = buscarOrdenPorId(idOrden);
        if (orden != null) {
            System.out.println("\n" + orden.generarFactura());
        } else {
            System.out.println("\n⚠ Orden no encontrada.");
        }
    }

    // ========== REPORTES ==========

    private static void verReportes() {
        System.out.println("\n╔═══════════════════════════════════════════╗");
        System.out.println("║       REPORTES DEL SISTEMA                ║");
        System.out.println("╚═══════════════════════════════════════════╝");
        System.out.println("Total de Clientes:        " + clientes.size());
        System.out.println("Total de Vehículos:       " + vehiculos.size());
        System.out.println("Total de Mecánicos:       " + mecanicos.size());
        System.out.println("Total de Repuestos:       " + repuestos.size());
        System.out.println("Total de Órdenes:         " + ordenes.size());

        // Estadísticas de órdenes
        int pendientes = 0, enProceso = 0, completadas = 0, entregadas = 0;
        double totalFacturado = 0;
        int ordenesConMecanico = 0;

        for (OrdenDeTrabajo orden : ordenes) {
            if (orden.getMecanicoAsignado() != null) {
                ordenesConMecanico++;
            }

            switch (orden.getEstado()) {
                case PENDIENTE:
                    pendientes++;
                    break;
                case EN_PROCESO:
                    enProceso++;
                    break;
                case COMPLETADA:
                    completadas++;
                    break;
                case ENTREGADA:
                    entregadas++;
                    totalFacturado += orden.calcularTotalConIVA();
                    break;
            }
        }

        System.out.println("\n--- Estado de Órdenes ---");
        System.out.println("Pendientes:               " + pendientes);
        System.out.println("En Proceso:               " + enProceso);
        System.out.println("Completadas:              " + completadas);
        System.out.println("Entregadas:               " + entregadas);
        System.out.println("Con Mecánico Asignado:    " + ordenesConMecanico);
        System.out.printf("Total Facturado:          $%,.2f\n", totalFacturado);

        // Mecánicos disponibles
        if (!mecanicos.isEmpty()) {
            int disponibles = 0;
            for (Mecanico m : mecanicos) {
                if (m.isDisponible()) disponibles++;
            }
            System.out.println("\n--- Estado de Mecánicos ---");
            System.out.println("Disponibles:              " + disponibles);
            System.out.println("Ocupados:                 " + (mecanicos.size() - disponibles));
        }

        // Repuestos con bajo stock
        if (!repuestos.isEmpty()) {
            List<Repuesto> bajoStock = new ArrayList<>();
            for (Repuesto r : repuestos) {
                if (r.tieneBajoStock()) {
                    bajoStock.add(r);
                }
            }

            if (!bajoStock.isEmpty()) {
                System.out.println("\n--- ⚠ Repuestos con Bajo Stock ---");
                for (Repuesto r : bajoStock) {
                    System.out.println("  " + r.getNombre() + " - Stock: " + r.getStockDisponible());
                }
            }
        }

        // Clientes con más vehículos
        if (!clientes.isEmpty()) {
            System.out.println("\n--- Clientes con Vehículos ---");
            for (Cliente c : clientes) {
                if (c.tieneVehiculos()) {
                    System.out.printf("%s: %d vehículo(s)\n", c.getNombre(), c.getCantidadVehiculos());
                }
            }
        }
    }

    // ========== MÉTODOS AUXILIARES ==========

    private static Cliente buscarClientePorId(int id) {
        for (Cliente c : clientes) {
            if (c.getId() == id) return c;
        }
        return null;
    }

    private static Vehiculo buscarVehiculoPorPlaca(String placa) {
        for (Vehiculo v : vehiculos) {
            if (v.getPlaca().equalsIgnoreCase(placa)) return v;
        }
        return null;
    }

    private static Vehiculo buscarVehiculoPorId(int id) {
        for (Vehiculo v : vehiculos) {
            if (v.getIdVehiculo() == id) return v;
        }
        return null;
    }

    private static OrdenDeTrabajo buscarOrdenPorId(int id) {
        for (OrdenDeTrabajo o : ordenes) {
            if (o.getIdOrden() == id) return o;
        }
        return null;
    }

    private static Mecanico buscarMecanicoPorId(int id) {
        for (Mecanico m : mecanicos) {
            if (m.getIdMecanico() == id) return m;
        }
        return null;
    }

    private static Repuesto buscarRepuestoPorId(int id) {
        for (Repuesto r : repuestos) {
            if (r.getIdRepuesto() == id) return r;
        }
        return null;
    }

    private static void verTodosLosVehiculos() {
        if (vehiculos.isEmpty()) {
            System.out.println("\n⚠ No hay vehículos registrados.");
            return;
        }

        System.out.println("\n=== LISTA COMPLETA DE VEHÍCULOS ===");
        System.out.println("═".repeat(70));

        for (int i = 0; i < vehiculos.size(); i++) {
            Vehiculo v = vehiculos.get(i);
            System.out.println((i + 1) + ". " + v.toString());
            System.out.println("   Propietario: " + v.getPropietario().getNombre() +
                    " (ID: " + v.getPropietario().getId() + ")");
            System.out.println("   Contacto: " + v.getPropietario().getTelefono());
            System.out.println("   Antigüedad: " + v.calcularAntiguedad() + " años" +
                    (v.esAntiguo() ? " ⚠ (Antiguo)" : ""));
            System.out.println("-".repeat(70));
        }

        System.out.println("Total de vehículos: " + vehiculos.size());
    }
}